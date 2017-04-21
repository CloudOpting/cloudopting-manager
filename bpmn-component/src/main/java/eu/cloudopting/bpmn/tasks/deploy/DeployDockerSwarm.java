package eu.cloudopting.bpmn.tasks.deploy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import eu.cloudopting.docker.DockerService;
import eu.cloudopting.tosca.ToscaService;

@Service
public class DeployDockerSwarm implements JavaDelegate {
	private final Logger log = LoggerFactory.getLogger(DeployDockerSwarm.class);
	@Autowired
	ToscaService toscaService;

	@Autowired
	DockerService dockerService;

	@Value("${server.ip}")
	private String hostip;

	@Value("${orchestrator.caPath}")
	private String caPath;

	@Value("${orchestrator.caPassword}")
	private String caPassword;

	@Value("${orchestrator.hostCertPath}")
	private String hostCertPath;

	@Value("${docker.port}")
	String dockerPort = "2377";
	
	@Value("${swarm.ip}")
	String swarmIp = "0.0.0.0";

	@Value("${swarm.port}")
	String swarmPort = "2377";
	private static int SSH_PORT = 22;
	private static String ROOT_USER = "root";

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		String ip = (String) execution.getVariable("ip");
		String serviceHome = (String) execution.getVariable("serviceHome");
		String coRoot = (String) execution.getVariable("coRoot");
		String customizationId = (String) execution.getVariable("customizationId");
		HashMap<String, String> data = toscaService.getCloudData(customizationId);

		// TODO al momento è vuoto perchè non lo leggiamo da nulla valutare se
		// leggerlo dinamicamente o da file di configurazione
		String privateKeyPath = (String) execution.getVariable("privateKeyPath");
		String passphrase = (String) execution.getVariable("passphrase");

		// prepare the certificates
		shellCert(serviceHome, caPath, data.get("vmname"), ip, caPassword);
		// send the key
		transferFile(serviceHome, "key.pem", privateKeyPath, passphrase, ip);
		// send the cert
		transferFile(serviceHome, "cert.pem", privateKeyPath, passphrase, ip);

		// now that all the pieces are in the machine we can join it to swarm
		String remote_command = "systemctl start docker && /usr/bin/docker run -itd --name=swarm-agent --expose=2376 -e SWARM_HOST=:2376 swarm join --advertise="
				+ ip + ":" + dockerPort + " consul://" + hostip + ":8500";

		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no"); // without this it cannot
													// connect because the host
													// key verification fails
													// the right way would be to
													// somehow get the server
													// key and add it to the
													// trusted keys
													// jsch.setKnownHosts()
													// https://epaul.github.io/jsch-documentation/javadoc/com/jcraft/jsch/JSch.html#setKnownHosts-java.lang.String-
													// see also
													// http://stackoverflow.com/a/32858953/28582
		JSch jsch = new JSch();
		jsch.addIdentity(privateKeyPath, passphrase);

		Session session = jsch.getSession(ROOT_USER, ip, SSH_PORT);
		session.setConfig(config);
		session.connect();

		ChannelExec channel = (ChannelExec) session.openChannel("exec");
		int exitStatus = sendCommand(remote_command, channel);
		log.debug("Command: [" + remote_command + "]");
		if (exitStatus != 0) {
			log.debug("FAILED - exit status: " + exitStatus);
		} else {
			log.debug("Executed successfully");
		}
		channel.disconnect();
		session.disconnect();

		log.debug("in DeployDockerSwarm");
		// toscaService.getNodeType("");
		// dockerService.addMachine(ip, 2376); //SSL
		String clusterToken = dockerService.addMachine(swarmIp, Integer.parseInt(swarmPort)); // no SSL
		execution.setVariable("clusterToken", clusterToken);
		String clusterInfo = dockerService.clusterDetail(clusterToken);
	}

	/**
	 * @param remote_command
	 * @param channel
	 * @return
	 * @throws IOException
	 * @throws JSchException
	 */
	private int sendCommand(String remote_command, ChannelExec channel) throws IOException, JSchException {
		log.debug(remote_command);
		channel.setCommand(remote_command);

		channel.setInputStream(null);
		channel.setErrStream(System.err);

		InputStream in = channel.getInputStream();

		channel.connect();

		byte[] tmp = new byte[1024];

		int exitStatus = -1;
		while (true) {
			while (in.available() > 0) {
				int i = in.read(tmp, 0, 1024);
				if (i < 0) {
					break;
				}
				log.debug(new String(tmp, 0, i));
			}
			if (channel.isClosed()) {
				if (in.available() > 0) {
					continue;
				}
				exitStatus = channel.getExitStatus();
				log.debug("exit status: " + exitStatus);
				break;
			}
			try {
				Thread.sleep(1000);
			} catch (Exception ee) {
				log.debug("Exception: " + ee.getMessage());
			}
		}
		return exitStatus;
	}

	public void shellCert(String pathService, String pathCa, String hostName, String hostIp, String passwordCA) {

		String command = "mkdir " + pathService + "/certs;openssl genrsa -out " + pathService
				+ "/certs/key.pem 4096 && openssl req -subj \"/CN=" + hostName + "\" -sha256 -new -key " + pathService
				+ "/certs/key.pem -out " + pathService + "/certs/host.csr && echo subjectAltName = IP:" + hostIp + " > "
				+ pathService + "/certs/extfile.cnf && openssl x509 -req -days 365 -sha256 -in " + pathService
				+ "/certs/host.csr -CA " + pathCa + "/ca.pem -CAkey " + pathCa + "/ca-key.pem -CAcreateserial -out "
				+ pathService + "/certs/cert.pem -extfile " + pathService + "/certs/extfile.cnf -passin pass:"
				+ passwordCA;

		log.debug("Creating certificates");
		log.debug(command);
		List<String> commands = new ArrayList<String>();
		commands.add("/bin/sh");
		commands.add("-c");
		commands.add(command);
		log.debug(command);
		// execute the command
		SystemCommandExecutor commandExecutor = new SystemCommandExecutor(commands);
		int result = 0;
		try {
			result = commandExecutor.executeCommand();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// get the stdout and stderr from the command that was run
		StringBuilder stdout = commandExecutor.getStandardOutputFromCommand();
		StringBuilder stderr = commandExecutor.getStandardErrorFromCommand();

		// print the stdout and stderr
		System.out.println("The numeric result of the command was: " + result);
		System.out.println("STDOUT:");
		System.out.println(stdout);
		System.out.println("STDERR:");
		System.out.println(stderr);
	}

	public void transferFile(String pathService, String fileName, String privateKeyPath, String passphrase,
			String hostIp) {
		// String command = "scp -i "+privateKeyPath+"
		// "+pathService+"/certs/"+fileName+"
		// root@"+hostIp+":"+hostCertPath+fileName;
		log.debug("tranferring file");
		// log.debug(command);

		FileInputStream fis = null;

		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no"); // without this it cannot
													// connect because the host
													// key verification fails
													// the right way would be to
													// somehow get the server
													// key and add it to the
													// trusted keys
													// jsch.setKnownHosts()
													// https://epaul.github.io/jsch-documentation/javadoc/com/jcraft/jsch/JSch.html#setKnownHosts-java.lang.String-
													// see also
													// http://stackoverflow.com/a/32858953/28582
		try {
			JSch jsch = new JSch();
			jsch.addIdentity(privateKeyPath, passphrase);

			Session session = jsch.getSession(ROOT_USER, hostIp, SSH_PORT);
			session.setConfig(config);
			session.connect();

			ChannelExec channel = (ChannelExec) session.openChannel("exec");
			String command = "scp -p -t " + hostCertPath + fileName;
			log.debug("command0: " + command);
			channel.setCommand(command);
			OutputStream out = channel.getOutputStream();
			InputStream in = channel.getInputStream();
			channel.connect();
			String lfile = pathService + "/certs/" + fileName;
			long filesize = (new File(lfile)).length();
			command = "C0644 " + filesize + " ";
			if (lfile.lastIndexOf('/') > 0) {
				command += lfile.substring(lfile.lastIndexOf('/') + 1);
			} else {
				command += lfile;
			}
			command += "\n";
			log.debug("command1: " + command);
			out.write(command.getBytes());
			out.flush();
			// miss check
			fis = new FileInputStream(lfile);
			byte[] buf = new byte[1024];
			while (true) {
				int len = fis.read(buf, 0, buf.length);
				if (len <= 0)
					break;
				out.write(buf, 0, len); // out.flush();
			}
			fis.close();
			fis = null;
			// send '\0'
			buf[0] = 0;
			out.write(buf, 0, 1);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (fis != null)
					fis.close();
			} catch (Exception ee) {
			}

		}
		/////

		/*
		 * int exitStatus = sendCommand(command, channel); log.debug(
		 * "Command: [" + command + "]"); if (exitStatus != 0) { log.debug(
		 * "FAILED - exit status: " + exitStatus); } else { log.debug(
		 * "Executed successfully"); } channel.disconnect();
		 * session.disconnect(); try { // exec 'scp -t rfile' remotely String
		 * command = "scp -p -t " + hostCertPath+fileName;
		 * 
		 * ((ChannelExec) channel).setCommand(command); OutputStream out =
		 * channel.getOutputStream(); InputStream in = channel.getInputStream();
		 * channel.connect(); if (checkAck(in) != 0) { System.exit(0); } // send
		 * "C0644 filesize filename", where filename should not include // '/'
		 * long filesize = (new File(lfile)).length(); command = "C0644 " +
		 * filesize + " "; if (lfile.lastIndexOf('/') > 0) { command +=
		 * lfile.substring(lfile.lastIndexOf('/') + 1); } else { command +=
		 * lfile; } command += "\n"; out.write(command.getBytes()); out.flush();
		 * if (checkAck(in) != 0) { System.exit(0); }
		 * 
		 * // send a content of lfile fis = new FileInputStream(lfile); byte[]
		 * buf = new byte[1024]; while (true) { int len = fis.read(buf, 0,
		 * buf.length); if (len <= 0) break; out.write(buf, 0, len); //
		 * out.flush(); } fis.close(); fis = null; // send '\0' buf[0] = 0;
		 * out.write(buf, 0, 1); out.flush(); if (checkAck(in) != 0) {
		 * System.exit(0); } out.close(); channel.disconnect(); } catch
		 * (Exception e) { e.printStackTrace(); try { if (fis != null)
		 * fis.close(); } catch (Exception ee) { } }
		 * 
		 * /*
		 * 
		 * List<String> commands = new ArrayList<String>();
		 * commands.add("/bin/sh"); commands.add("-c"); commands.add(command);
		 * log.debug(command); // execute the command SystemCommandExecutor
		 * commandExecutor = new SystemCommandExecutor(commands); int result =
		 * 0; try { result = commandExecutor.executeCommand(); } catch
		 * (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } catch (InterruptedException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 * 
		 * // get the stdout and stderr from the command that was run
		 * StringBuilder stdout =
		 * commandExecutor.getStandardOutputFromCommand(); StringBuilder stderr
		 * = commandExecutor.getStandardErrorFromCommand();
		 * 
		 * // print the stdout and stderr System.out.println(
		 * "The numeric result of the command was: " + result);
		 * System.out.println("STDOUT:"); System.out.println(stdout);
		 * System.out.println("STDERR:"); System.out.println(stderr);
		 */
	}

}

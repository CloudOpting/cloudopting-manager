package eu.cloudopting.tosca.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.environment.EnvironmentUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import eu.cloudopting.tosca.ToscaService;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

@Service
public class ToscaUtils {

	private static final int BUFFER_SIZE = 4096;
	private final Logger log = LoggerFactory.getLogger(ToscaUtils.class);

	public void generatePuppetfile(HashMap<String, Object> templData, String serviceHome) {
		// write the "Puppetfile" file
		Configuration cfg = new Configuration();
		cfg.setClassForTemplateLoading(ToscaService.class, "/templates");

		Template tpl = null;
		try {
			tpl = cfg.getTemplate("Puppetfile.ftl");
		} catch (TemplateNotFoundException e) {
			log.error("TemplateNotFoundException in ToscaUtils.generatePuppetfile getting puppetfile template.");
			e.printStackTrace();
		} catch (MalformedTemplateNameException e) {
			log.error("MalformedTemplateNameException in ToscaUtils.generatePuppetfile getting puppetfile template.");
			e.printStackTrace();
		} catch (ParseException e) {
			log.error("ParseException in ToscaUtils.generatePuppetfile getting puppetfile template.");
			e.printStackTrace();
		} catch (IOException e) {
			log.error("IOException in ToscaUtils.generatePuppetfile getting puppetfile template.");
			e.printStackTrace();
		}

		OutputStreamWriter outputTempl = new OutputStreamWriter(System.out);
		PrintWriter outFile = null;
		String puppetFile = new String("Puppetfile");
		try {
			outFile = new PrintWriter(serviceHome + "/" + puppetFile, "UTF-8");
		} catch (FileNotFoundException e1) {
			log.error("FileNotFoundException in ToscaUtils.generatePuppetfile writting the puppetfile.");
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			log.error("UnsupportedEncodingException in ToscaUtils.generatePuppetfile writting the puppetfile.");
			e1.printStackTrace();
		}

		try {
			// tpl.process(nodeData, outputTempl);
			tpl.process(templData, outFile);
		} catch (TemplateException e) {
			log.error("TemplateException in ToscaUtils.generatePuppetfile processing the puppetfile.");
			e.printStackTrace();
		} catch (IOException e) {
			log.error("IOException in ToscaUtils.generatePuppetfile processing the puppetfile.");
			e.printStackTrace();
		}

	}

	public void generateDockerCompose(HashMap<String, Object> templData, String serviceHome) {
		Configuration cfg = new Configuration();
		cfg.setClassForTemplateLoading(ToscaService.class, "/templates");
		Template tpl = null;
		try {
			tpl = cfg.getTemplate("docker-compose.ftl");
		} catch (TemplateNotFoundException e) {
			log.error("TemplateNotFoundException in ToscaUtils.generateDockerCompose getting docker-compose template.");
			e.printStackTrace();
		} catch (MalformedTemplateNameException e) {
			log.error("MalformedTemplateNameException in ToscaUtils.generateDockerCompose getting docker-compose template.");
			e.printStackTrace();
		} catch (ParseException e) {
			log.error("ParseException in ToscaUtils.generateDockerCompose getting docker-compose template.");
			e.printStackTrace();
		} catch (IOException e) {
			log.error("IOException in ToscaUtils.generateDockerCompose getting docker-compose template.");
			e.printStackTrace();
		}

		OutputStreamWriter outputTempl = new OutputStreamWriter(System.out);
		// FileOutputStream outFile = new FileOutputStream("the-file-name");
		PrintWriter outFile = null;
		String composeFile = new String("docker-compose.yml");
		try {
			outFile = new PrintWriter(serviceHome + "/" + composeFile, "UTF-8");
		} catch (FileNotFoundException e1) {
			log.error("FileNotFoundException in ToscaUtils.generateDockerCompose writting the docker-compose.");
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			log.error("UnsupportedEncodingException in ToscaUtils.generateDockerCompose writting the docker-compose.");
			e1.printStackTrace();
		}

		try {
			// tpl.process(nodeData, outputTempl);
			tpl.process(templData, outFile);
		} catch (TemplateException e) {
			log.error("TemplateException in ToscaUtils.generateDockerCompose processing the docker-compose.");
			e.printStackTrace();
		} catch (IOException e) {
			log.error("IOException in ToscaUtils.generateDockerCompose processing the docker-compose.");
			e.printStackTrace();
		}
	}

	public void unzip(String zipFilePath, String destDirectory) throws IOException {
		File destDir = new File(destDirectory);
		if (!destDir.exists()) {
			destDir.mkdir();
		}
		log.debug("ToscaUtils.unzip zipFilePath: " + zipFilePath);
		log.debug("ToscaUtils.unzip destDirectory: " + destDirectory);
		ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
		ZipEntry entry = zipIn.getNextEntry();
		// iterates over entries in the zip file
		while (entry != null) {
			String filePath = destDirectory + File.separator + entry.getName();
			log.debug("ToscaUtils.unzip filePath: " + filePath);
			if (!entry.isDirectory()) {
				String dir = dirpart(entry.getName());
				if (dir != null)
					mkdirs(destDir, dir);

				// if the entry is a file, extracts it
				extractFile(zipIn, filePath);
			} else {
				// if the entry is a directory, make the directory
				File dir = new File(filePath);
				dir.mkdir();
			}
			zipIn.closeEntry();
			entry = zipIn.getNextEntry();
		}
		zipIn.close();
	}

	private void mkdirs(File outdir, String path) {
		File d = new File(outdir, path);
		if (!d.exists())
			d.mkdirs();
	}

	private String dirpart(String name) {
		int s = name.lastIndexOf(File.separatorChar);
		return s == -1 ? null : name.substring(0, s);
	}

	public void unzip(InputStream stream, String destDirectory) throws IOException {
		File destDir = new File(destDirectory);
		if (!destDir.exists()) {
			destDir.mkdir();
		}
		ZipInputStream zipIn = new ZipInputStream(stream);
		ZipEntry entry = zipIn.getNextEntry();
		// iterates over entries in the zip file
		while (entry != null) {
			String filePath = destDirectory + File.separator + entry.getName();
			if (!entry.isDirectory()) {
				// if the entry is a file, extracts it
				extractFile(zipIn, filePath);
			} else {
				// if the entry is a directory, make the directory
				File dir = new File(filePath);
				dir.mkdir();
			}
			zipIn.closeEntry();
			entry = zipIn.getNextEntry();
		}
		zipIn.close();
	}

	/**
	 * Extracts a zip entry (file entry)
	 * 
	 * @param zipIn
	 * @param filePath
	 * @throws IOException
	 */
	private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
		byte[] bytesIn = new byte[BUFFER_SIZE];
		int read = 0;
		while ((read = zipIn.read(bytesIn)) != -1) {
			bos.write(bytesIn, 0, read);
		}
		bos.close();
	}

	public R10kResultHandler runR10k(String puppetFile, String puppetDir, final long r10kJobTimeout,
			final boolean r10kInBackground, String workingDir) {
		int exitValue = 0;
		ExecuteWatchdog watchdog = null;
		R10kResultHandler resultHandler;
		Map<String, String> env = null;
		try {
			env = EnvironmentUtils.getProcEnvironment();
			env.put("PUPPETFILE", puppetFile);
			env.put("PUPPETFILE_DIR", puppetDir);
			System.out.println(env);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// env.put("PUPPETFILE", puppetFile);
		// env.put("PUPPETFILE_DIR", puppetDir);
		// final CommandLine commandLine = new CommandLine("/bin/sh");
		// commandLine.addArgument("-c", false);
		final CommandLine commandLine = new CommandLine("/usr/local/bin/r10k");
		commandLine.addArgument("puppetfile");
		commandLine.addArgument("install");
		// commandLine.addArgument("PUPPETFILE=" + puppetFile, false);
		// commandLine.addArgument("PUPPETFILE_DIR="+ puppetDir, false);
		// commandLine.addArgument("puppetfile", false);
		// commandLine.addArgument("r10k", false);
		// commandLine.addArgument("install", false);
		// commandLine.addArgument("'PUPPETFILE=" + puppetFile + "
		// PUPPETFILE_DIR="
		// + puppetDir + " /usr/local/bin/r10k puppetfile install'", false);
		log.debug("ToscaUtils.runR10k command line executable: " + commandLine.getExecutable());
		// create the executor and consider the exitValue '1' as success
		final Executor executor = new DefaultExecutor();
		executor.setWorkingDirectory(new File(workingDir));

		// executor.setExitValue(1);

		// create a watchdog if requested
		if (r10kJobTimeout > 0) {
			watchdog = new ExecuteWatchdog(r10kJobTimeout);
			executor.setWatchdog(watchdog);
		}

		// pass a "ExecuteResultHandler" when doing background printing
		if (r10kInBackground) {
			log.debug("ToscaUtils.runR10k [print] Executing non-blocking r10k job  ...");
			resultHandler = new R10kResultHandler(watchdog);
			try {
				executor.execute(commandLine, env, resultHandler);
			} catch (IOException e) {
				log.error("IOException ToscaUtils.runR10k executing non-blocking r10k job.");
				e.printStackTrace();
			}
		} else {
			log.debug("ToscaUtils.runR10k [print] Executing blocking r10k job  ...");
			try {
				exitValue = executor.execute(commandLine);
			} catch (IOException e) {
				log.error("IOException ToscaUtils.runR10k executing blocking r10k job.");
				e.printStackTrace();
			}
			resultHandler = new R10kResultHandler(exitValue);
		}

		return resultHandler;
		/*
		 * List<String> commands = new ArrayList<String>();
		 * commands.add("/bin/sh"); commands.add("-c");
		 * commands.add("PUPPETFILE=" +path+"/"+customer+"-"+service+
		 * "/Puppetfile PUPPETFILE_DIR=" +path+
		 * "/puppet/modules r10k puppetfile install");
		 * System.out.println("PUPPETFILE=" +path+"/"+customer+"-"+service+
		 * "/Puppetfile PUPPETFILE_DIR=" +path+
		 * "/puppet/modules r10k puppetfile install"); // execute the command
		 * SystemCommandExecutor commandExecutor = new
		 * SystemCommandExecutor(commands); int result = 0; try { result =
		 * commandExecutor.executeCommand(); } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } catch
		 * (InterruptedException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
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

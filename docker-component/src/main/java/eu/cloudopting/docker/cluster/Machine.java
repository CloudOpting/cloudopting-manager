package eu.cloudopting.docker.cluster;

import java.io.File;


/**
 *
 * Represents a machine
 *
 */
public class Machine {
	private String hostname;
	private int port;
	private int dockerPort;
	private File privateKey;
	private String passphrase;
	private String user;
	private String password;
	
	/**
	 * New machine from private key + passphrase credentials.
	 * @param hostname Hostname or ip address
	 * @param port SSH port
	 * @param privateKey Private key with SSH privileges 
	 * @param passphrase Passphrase to decrypt the privateKey. Null if passphrase not needed.
	 */
	public Machine(String hostname, int port, File privateKey, String passphrase){
		this.hostname = hostname;
		this.port = port;
		this.privateKey = privateKey;
		this.passphrase = passphrase;
		this.user = null;
		this.password = null;
		this.dockerPort = 0;
	}
	
	/**
	 * New machine from user + password credentials.
	 * @param hostname Hostname or ip address
	 * @param port SSH port
	 * @param user User
	 * @param password Password for user
	 */
	public Machine(String hostname, int port, String user, String password){
		this.hostname = hostname;
		this.port = port;
		this.user = user;
		this.password = password;
		this.privateKey = null;
		this.passphrase = null;
		this.dockerPort = 0;
	}
	
	/**
	 * New machine from docker port.
	 * @param hostname Hostname or ip address
	 * @param dockerPort Docker port
	 */
	public Machine(String hostname, int dockerPort){
		this.hostname = hostname;
		this.port = 0;
		this.privateKey = null;
		this.passphrase = null;
		this.user = null;
		this.password = null;
		this.dockerPort = dockerPort;
	}

	/**
	 * @return the hostname
	 */
	public String getHostname() {
		return hostname;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @return the privateKey
	 */
	public File getPrivateKey() {
		return privateKey;
	}

	/**
	 * @return the passphrase
	 */
	public String getPassphrase() {
		return passphrase;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return the dockerPort
	 */
	public int getDockerPort() {
		return dockerPort;
	}

}

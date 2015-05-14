package eu.cloudopting.docker;


/**
 *
 * Means that the Docker commander API is reporting an error.
 *
 */
public class DockerError extends Exception{
    public DockerError() {}

    public DockerError(String message){
       super(message);
    }

}

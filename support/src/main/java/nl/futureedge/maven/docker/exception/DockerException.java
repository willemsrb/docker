package nl.futureedge.maven.docker.exception;

/**
 * Docker exception.
 */
public class DockerException extends Exception {

    /**
     * Docker exception.
     * @param message message
     */
    protected DockerException(String message) {
        super(message);
    }

    /**
     * Docker exception.
     * @param message message
     * @param cause cause
     */
    protected DockerException(String message, Throwable cause) {
        super(message, cause);
    }
}

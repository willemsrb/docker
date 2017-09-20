package nl.futureedge.maven.docker.exception;

public class DockerException extends Exception {

    protected DockerException(String message) {
        super(message);
    }

    protected DockerException(String message, Throwable cause) {
        super(message, cause);
    }
}

package nl.futureedge.maven.docker.exception;

public final class DockerConfigurationException extends DockerException {
    private static final long serialVersionUID = 1L;

    public DockerConfigurationException(final String message) {
        super(message);
    }

    public DockerConfigurationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}

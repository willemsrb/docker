package nl.futureedge.maven.docker.exception;

/**
 * Configuration exception.
 */
public final class DockerConfigurationException extends DockerException {
    private static final long serialVersionUID = 1L;

    /**
     * Configuration exception.
     * @param message message
     */
    public DockerConfigurationException(final String message) {
        super(message);
    }
}

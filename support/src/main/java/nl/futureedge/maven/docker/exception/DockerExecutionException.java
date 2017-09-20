package nl.futureedge.maven.docker.exception;

import java.io.IOException;

/**
 * Docker execution exception.
 */
public final class DockerExecutionException extends DockerException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     * @param message message
     */
    public DockerExecutionException(final String message) {
        super(message);
    }

    /**
     * Constructor.
     * @param message message
     * @param cause cause
     */
    public DockerExecutionException(final String message, final IOException cause) {
        super(message, cause);
    }
}

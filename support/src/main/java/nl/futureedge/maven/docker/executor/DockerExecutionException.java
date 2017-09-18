package nl.futureedge.maven.docker.executor;

import java.io.IOException;

/**
 * Docker execution exception.
 */
public final class DockerExecutionException extends Exception {
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

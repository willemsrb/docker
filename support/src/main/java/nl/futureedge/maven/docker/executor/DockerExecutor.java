package nl.futureedge.maven.docker.executor;

import java.util.List;
import nl.futureedge.maven.docker.exception.DockerExecutionException;

/**
 * Docker command executor.
 */
public interface DockerExecutor {

    /**
     * Execute a docker command.
     * @param arguments arguments (should not contain docker only the arguments)
     * @param logOut true, if output from StdOut should be logged
     * @param returnOut true if output from StdOut should be returned
     * @return the output
     * @throws DockerExecutionException on failures
     */
    List<String> execute(final List<String> arguments, final boolean logOut, final boolean returnOut) throws DockerExecutionException;
}

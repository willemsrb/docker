package nl.futureedge.maven.docker.support;

import java.util.ArrayList;
import java.util.List;
import nl.futureedge.maven.docker.exception.DockerExecutionException;
import nl.futureedge.maven.docker.executor.DockerExecutor;

/**
 * Stop containers.
 */
public final class StopContainersExecutable extends FilteredListExecutable {

    /**
     * Create a new docker command execution.
     * @param settings settings.
     */
    public StopContainersExecutable(final StopContainersSettings settings) {
        super(settings);
    }

    @Override
    protected List<String> list(final DockerExecutor executor, final String filter) throws DockerExecutionException {
        debug("Stop containers configuration: ");
        debug("- filter: " + filter);

        return RemoveContainersExecutable.listContainers(executor, filter);
    }

    @Override
    protected void execute(final DockerExecutor executor, final String container) throws DockerExecutionException {
        info("Stop container: " + container);

        final List<String> arguments = new ArrayList<>();
        arguments.add("stop");
        arguments.add(container);

        executor.execute(arguments, false, false);
    }
}

package nl.futureedge.maven.docker.support;

import java.util.ArrayList;
import java.util.List;
import nl.futureedge.maven.docker.exception.DockerExecutionException;
import nl.futureedge.maven.docker.executor.DockerExecutor;

/**
 * Start containers.
 */
public final class StartContainersExecutable extends FilteredListExecutable {

    /**
     * Create a new docker command execution.
     * @param settings settings.
     */
    public StartContainersExecutable(final StartContainersSettings settings) {
        super(settings);
    }

    @Override
    protected List<String> list(final DockerExecutor executor, final String filter) throws DockerExecutionException {
        debug("Start containers configuration: ");
        debug("- filter: " + filter);

        return RemoveContainersExecutable.listContainers(executor, filter);
    }

    @Override
    protected void execute(final DockerExecutor executor, final String container) throws DockerExecutionException {
        info(String.format("Start container: %s", container));

        final List<String> arguments = new ArrayList<>();
        arguments.add("start");
        arguments.add(container);

        executor.execute(arguments, false, false);
    }

}

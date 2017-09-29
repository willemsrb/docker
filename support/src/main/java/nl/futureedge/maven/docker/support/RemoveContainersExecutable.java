package nl.futureedge.maven.docker.support;

import java.util.ArrayList;
import java.util.List;
import nl.futureedge.maven.docker.exception.DockerExecutionException;
import nl.futureedge.maven.docker.executor.DockerExecutor;

/**
 * Remove containers.
 */
public final class RemoveContainersExecutable extends FilteredListExecutable {

    /**
     * Create a new docker command execution.
     * @param settings settings.
     */
    public RemoveContainersExecutable(final RemoveContainersSettings settings) {
        super(settings);
    }

    /**
     * List containers.
     * @param executor executor
     * @param filter filter
     * @return list of containers
     * @throws DockerExecutionException on any errors
     */
    protected static List<String> listContainers(final DockerExecutor executor, String filter) throws DockerExecutionException {
        final List<String> arguments = new ArrayList<>();
        arguments.add("ps");
        arguments.add("-a");
        arguments.add("-q");
        if ((filter != null) && !"".equals(filter)) {
            arguments.add("--filter");
            arguments.add(filter);
        }
        arguments.add("--format");
        arguments.add("{{.Names}}");

        return executor.execute(arguments, false, true);
    }

    @Override
    protected List<String> list(final DockerExecutor executor, String filter) throws DockerExecutionException {
        debug("Remove containers configuration: ");
        debug("- filter: " + filter);

        return listContainers(executor, filter);
    }

    @Override
    protected void execute(final DockerExecutor executor, final String container) throws DockerExecutionException {
        info(String.format("Remove container: %s", container));

        final List<String> arguments = new ArrayList<>();
        arguments.add("rm");
        arguments.add("-vf");
        arguments.add(container);

        executor.execute(arguments, false, false);
    }

}

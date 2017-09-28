package nl.futureedge.maven.docker.support;

import java.util.ArrayList;
import java.util.List;
import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.exception.DockerExecutionException;
import nl.futureedge.maven.docker.executor.DockerExecutor;


public final class RemoveContainersExecutable extends DockerExecutable {

    private final String filter;

    public RemoveContainersExecutable(final RemoveContainersSettings settings) {
        super(settings);

        this.filter = settings.getFilter();
    }

    @Override
    public void execute() throws DockerException {
        debug("Remove containers configuration: ");
        debug("- filter: " + filter);

        final DockerExecutor executor = createDockerExecutor();
        final List<String> containers = doIgnoringFailure(() -> listContainers(executor, filter));

        if (containers != null) {
            for (final String container : containers) {
                if ("".equals(container.trim())) {
                    continue;
                }
                info("Remove container: " + container);
                doIgnoringFailure(() -> removeContainer(executor, container));
            }
        }
    }

    static List<String> listContainers(final DockerExecutor executor, String filter) throws DockerExecutionException {
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

    private static void removeContainer(final DockerExecutor executor, final String container) throws DockerExecutionException {
        final List<String> arguments = new ArrayList<>();
        arguments.add("rm");
        arguments.add("-vf");
        arguments.add(container);

        executor.execute(arguments, false, false);
    }

}

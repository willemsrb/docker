package nl.futureedge.maven.docker.support;

import java.util.List;
import nl.futureedge.maven.docker.executor.DockerCommands;
import nl.futureedge.maven.docker.executor.DockerExecutionException;
import nl.futureedge.maven.docker.executor.DockerExecutor;

public final class RemoveContainersExecutable extends DockerExecutable {

   private final String filter;

    public RemoveContainersExecutable(final RemoveContainersSettings settings) {
        super(settings);

        this.filter = settings.getFilter();
    }

    @Override
    public void execute() throws DockerExecutionException {
        debug("Remove containers configuration: ");
        debug("- filter: " + filter);

        final DockerExecutor executor = createDockerExecutor();
        final List<String> containers = doIgnoringFailure(() -> DockerCommands.listContainers(executor, filter));

        if (containers != null) {
            for (final String container : containers) {
                if ("".equals(container.trim())) {
                    continue;
                }
                info("Remove container: " + container);
                doIgnoringFailure(() -> DockerCommands.removeContainer(executor, container));
            }
        }
    }
}

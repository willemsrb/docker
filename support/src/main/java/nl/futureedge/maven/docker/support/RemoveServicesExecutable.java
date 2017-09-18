package nl.futureedge.maven.docker.support;

import java.util.List;
import nl.futureedge.maven.docker.executor.DockerCommands;
import nl.futureedge.maven.docker.executor.DockerExecutionException;
import nl.futureedge.maven.docker.executor.DockerExecutor;

public final class RemoveServicesExecutable extends DockerExecutable {

    private final String filter;

    public RemoveServicesExecutable(final RemoveServicesSettings settings) {
        super(settings);

        this.filter = settings.getFilter();
    }

    @Override
    public void execute() throws DockerExecutionException {
        debug("Remove services configuration: ");
        debug("- filter: " + filter);

        final DockerExecutor executor = createDockerExecutor();
        final List<String> services = doIgnoringFailure(() -> DockerCommands.listServices(executor, filter));

        if (services != null) {
            for (final String service : services) {
                if ("".equals(service.trim())) {
                    continue;
                }
                info("Remove service: " + service);
                doIgnoringFailure(() -> DockerCommands.removeService(executor, service));
            }
        }
    }
}

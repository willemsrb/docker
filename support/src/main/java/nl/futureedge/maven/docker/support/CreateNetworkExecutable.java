package nl.futureedge.maven.docker.support;

import java.util.ArrayList;
import java.util.List;
import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.executor.Docker;
import nl.futureedge.maven.docker.executor.DockerExecutor;

public final class CreateNetworkExecutable extends DockerExecutable {

    private final String networkOptions;
    private final String networkName;

    public CreateNetworkExecutable(final CreateNetworkSettings settings) {
        super(settings);

        this.networkOptions = settings.getNetworkOptions();
        this.networkName = settings.getNetworkName();
    }

    public void execute() throws DockerException {
        debug("Create network configuration: ");
        debug("- networkOptions: " + networkOptions);
        debug("- networkName: " + networkName);

        final DockerExecutor executor = createDockerExecutor();
        doIgnoringFailure(() -> createNetwork(executor));
    }

    private void createNetwork(final DockerExecutor executor) throws DockerException {
        final List<String> arguments = new ArrayList<>();
        arguments.add("network");
        arguments.add("create");
        arguments.addAll(Docker.splitOptions(networkOptions));
        arguments.add(networkName);

        executor.execute(arguments);
    }
}

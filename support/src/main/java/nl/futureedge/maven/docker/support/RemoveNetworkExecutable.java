package nl.futureedge.maven.docker.support;

import java.util.ArrayList;
import java.util.List;
import nl.futureedge.maven.docker.executor.DockerExecutionException;
import nl.futureedge.maven.docker.executor.DockerExecutor;

public final class RemoveNetworkExecutable extends DockerExecutable {

    private final String networkName;

    public RemoveNetworkExecutable(final RemoveNetworkSettings settings) {
        super(settings);

        this.networkName = settings.getNetworkName();
    }

    @Override
    public void execute() throws DockerExecutionException {
        debug("Remove network configuration: ");
        debug("- networkName: " + networkName);

        final DockerExecutor executor = createDockerExecutor();
        doIgnoringFailure(() -> createNetwork(executor));
    }

    private void createNetwork(final DockerExecutor executor) throws DockerExecutionException {
        final List<String> arguments = new ArrayList<>();
        arguments.add("network");
        arguments.add("rm");
        arguments.add(networkName);

        executor.execute(arguments, false, false);
    }
}

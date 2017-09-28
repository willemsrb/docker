package nl.futureedge.maven.docker.support;

import java.util.ArrayList;
import java.util.List;
import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.executor.DockerExecutor;

/**
 * Remove a network.
 */
public final class RemoveNetworkExecutable extends DockerExecutable {

    private final String networkName;

    /**
     * Create a new docker command execution.
     * @param settings settings.
     */
    public RemoveNetworkExecutable(final RemoveNetworkSettings settings) {
        super(settings);

        this.networkName = settings.getNetworkName();
    }

    @Override
    public void execute() throws DockerException {
        debug("Remove network configuration: ");
        debug("- networkName: " + networkName);

        final DockerExecutor executor = createDockerExecutor();
        doIgnoringFailure(() -> removeNetwork(executor));
    }

    private void removeNetwork(final DockerExecutor executor) throws DockerException {
        final List<String> arguments = new ArrayList<>();
        arguments.add("network");
        arguments.add("rm");
        arguments.add(networkName);

        executor.execute(arguments, false, false);
    }
}

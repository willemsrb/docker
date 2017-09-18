package nl.futureedge.maven.docker.mojo;

import nl.futureedge.maven.docker.executor.DockerExecutionException;
import nl.futureedge.maven.docker.support.RemoveContainersExecutable;
import nl.futureedge.maven.docker.support.RemoveContainersSettings;
import nl.futureedge.maven.docker.support.RemoveNetworkExecutable;
import nl.futureedge.maven.docker.support.RemoveNetworkSettings;
import nl.futureedge.maven.docker.support.StopContainersExecutable;
import nl.futureedge.maven.docker.support.StopContainersSettings;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Remove a network.
 */
@Mojo(name = "remove-network", requiresProject = false)
public final class RemoveNetworkMojo extends AbstractDockerMojo implements RemoveNetworkSettings, StopContainersSettings, RemoveContainersSettings {

    /**
     * Should containers connected to the network be stopped before removing the network?
     */
    @Parameter(name = "stopContainers", property = "docker.stopContainers", defaultValue = "false")
    private boolean stopContainers;

    /**
     * Should containers connected to the network be removed before removing the network?
     */
    @Parameter(name = "removeContainers", property = "docker.removeContainers", defaultValue = "false")
    private boolean removeContainers;

    /**
     * Network (name) to stop.
     */
    @Parameter(name = "networkName", property = "docker.networkName", required = true)
    private String networkName;

    @Override
    public String getNetworkName() {
        return networkName;
    }

    @Override
    public String getFilter() {
        return "network=" + networkName;
    }

    @Override
    protected void executeInternal() throws DockerExecutionException {
        if (stopContainers) {
            new StopContainersExecutable(this).execute();
        }
        if (removeContainers) {
            new RemoveContainersExecutable(this).execute();
        }

        new RemoveNetworkExecutable(this).execute();

    }
}

package nl.futureedge.maven.docker.mojo;

import java.util.function.Function;
import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.support.DockerExecutable;
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

    private Function<StopContainersSettings, DockerExecutable> stopContainersExecutableCreator = StopContainersExecutable::new;
    private Function<RemoveContainersSettings, DockerExecutable> removeContainersExecutableCreator = RemoveContainersExecutable::new;
    private Function<RemoveNetworkSettings, DockerExecutable> removeNetworkExecutableCreator = RemoveNetworkExecutable::new;

    @Override
    public String getNetworkName() {
        return networkName;
    }

    @Override
    public String getFilter() {
        return "network=" + networkName;
    }

    /**
     * For testing purposes only: command creator.
     * @param creator command creator
     */
    void setStopContainersExecutableCreator(final Function<StopContainersSettings, DockerExecutable> creator) {
        this.stopContainersExecutableCreator = creator;
    }

    /**
     * For testing purposes only: command creator.
     * @param creator command creator
     */
    void setRemoveContainersExecutableCreator(final Function<RemoveContainersSettings, DockerExecutable> creator) {
        this.removeContainersExecutableCreator = creator;
    }

    /**
     * For testing purposes only: command creator.
     * @param creator command creator
     */
    void setRemoveNetworkExecutableCreator(final Function<RemoveNetworkSettings, DockerExecutable> creator) {
        this.removeNetworkExecutableCreator = creator;
    }

    @Override
    protected void executeInternal() throws DockerException {
        if (stopContainers) {
            stopContainersExecutableCreator.apply(this).execute();
        }
        if (removeContainers) {
            removeContainersExecutableCreator.apply(this).execute();
        }

        removeNetworkExecutableCreator.apply(this).execute();
    }
}

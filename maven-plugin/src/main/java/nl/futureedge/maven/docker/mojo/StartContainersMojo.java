package nl.futureedge.maven.docker.mojo;

import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.support.StartContainersExecutable;
import nl.futureedge.maven.docker.support.StartContainersSettings;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Start containers.
 */
@Mojo(name = "start-containers", requiresProject = false)
public final class StartContainersMojo extends AbstractDockerMojo implements StartContainersSettings {

    /**
     * Filter for containers to start (if empty, all containers will be started).
     */
    @Parameter(name = "filter", property = "docker.filter")
    private String filter;

    @Override
    public String getFilter() {
        return filter;
    }

    @Override
    protected void executeInternal() throws DockerException {
        new StartContainersExecutable(this).execute();
    }

}

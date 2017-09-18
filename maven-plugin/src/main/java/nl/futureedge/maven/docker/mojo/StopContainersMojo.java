package nl.futureedge.maven.docker.mojo;

import nl.futureedge.maven.docker.executor.DockerExecutionException;
import nl.futureedge.maven.docker.support.StopContainersExecutable;
import nl.futureedge.maven.docker.support.StopContainersSettings;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Stop containers.
 */
@Mojo(name = "stop-containers", requiresProject = false)
public final class StopContainersMojo extends AbstractDockerMojo implements StopContainersSettings {

    /**
     * Filter for containers to stop (if empty, all containers will be stopped).
     */
    @Parameter(name = "filter", property = "docker.filter")
    private String filter;

    @Override
    public String getFilter() {
        return filter;
    }

    @Override
    protected void executeInternal() throws DockerExecutionException {
        new StopContainersExecutable(this).execute();
    }

}

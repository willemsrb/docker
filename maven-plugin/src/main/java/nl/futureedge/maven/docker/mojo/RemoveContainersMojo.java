package nl.futureedge.maven.docker.mojo;

import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.support.RemoveContainersExecutable;
import nl.futureedge.maven.docker.support.RemoveContainersSettings;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Remove containers.
 */
@Mojo(name = "remove-containers", requiresProject = false)
public final class RemoveContainersMojo extends AbstractDockerMojo implements RemoveContainersSettings {

    /**
     * Filter for containers to remove (if empty, all containers will be removed).
     */
    @Parameter(name = "filter", property = "docker.filter")
    private String filter;

    @Override
    public String getFilter() {
        return filter;
    }

    @Override
    protected void executeInternal() throws DockerException {
        new RemoveContainersExecutable(this).execute();
    }
}

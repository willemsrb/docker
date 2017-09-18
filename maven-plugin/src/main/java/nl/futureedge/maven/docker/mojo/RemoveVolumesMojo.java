package nl.futureedge.maven.docker.mojo;

import nl.futureedge.maven.docker.executor.DockerExecutionException;
import nl.futureedge.maven.docker.support.RemoveVolumesExecutable;
import nl.futureedge.maven.docker.support.RemoveVolumesSettings;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Remove volumes.
 */
@Mojo(name = "remove-volumes", requiresProject = false)
public final class RemoveVolumesMojo extends AbstractDockerMojo implements RemoveVolumesSettings {

    /**
     * Filter for volumes to be removed (if empty, all volumes will be removed).
     */
    @Parameter(name = "filter", property = "docker.filter")
    private String filter;

    @Override
    public String getFilter() {
        return filter;
    }

    @Override
    protected void executeInternal() throws DockerExecutionException {
        new RemoveVolumesExecutable(this).execute();
    }
}

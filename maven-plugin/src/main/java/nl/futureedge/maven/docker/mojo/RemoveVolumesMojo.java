package nl.futureedge.maven.docker.mojo;

import java.util.function.Function;
import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.support.DockerExecutable;
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

    private Function<RemoveVolumesSettings, DockerExecutable> removeVolumesExecutableCreator = RemoveVolumesExecutable::new;

    @Override
    public String getFilter() {
        return filter;
    }

    /**
     * For testing purposes only: command creator.
     * @param creator command creator
     */
    void setRemoveVolumesExecutableCreator(final Function<RemoveVolumesSettings, DockerExecutable> creator) {
        this.removeVolumesExecutableCreator = creator;
    }

    @Override
    protected void executeInternal() throws DockerException {
        removeVolumesExecutableCreator.apply(this).execute();
    }
}

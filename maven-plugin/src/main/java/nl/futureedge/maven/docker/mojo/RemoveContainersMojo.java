package nl.futureedge.maven.docker.mojo;

import java.util.function.Function;
import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.support.DockerExecutable;
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

    private Function<RemoveContainersSettings, DockerExecutable> removeContainersExecutableCreator = RemoveContainersExecutable::new;

    @Override
    public String getFilter() {
        return filter;
    }

    /**
     * For testing purposes only: command creator.
     * @param creator command creator
     */
    void setRemoveContainersExecutableCreator(final Function<RemoveContainersSettings, DockerExecutable> creator) {
        this.removeContainersExecutableCreator = creator;
    }

    @Override
    protected void executeInternal() throws DockerException {
        removeContainersExecutableCreator.apply(this).execute();
    }
}

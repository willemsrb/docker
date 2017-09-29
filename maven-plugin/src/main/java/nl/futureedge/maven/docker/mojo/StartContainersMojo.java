package nl.futureedge.maven.docker.mojo;

import java.util.function.Function;
import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.support.DockerExecutable;
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

    private Function<StartContainersSettings, DockerExecutable> startContainersExecutableCreator = StartContainersExecutable::new;

    @Override
    public String getFilter() {
        return filter;
    }

    /**
     * For testing purposes only: command creator.
     * @param creator command creator
     */
    void setStartContainersExecutableCreator(final Function<StartContainersSettings, DockerExecutable> creator) {
        this.startContainersExecutableCreator = creator;
    }

    @Override
    protected void executeInternal() throws DockerException {
        startContainersExecutableCreator.apply(this).execute();
    }

}

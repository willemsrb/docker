package nl.futureedge.maven.docker.mojo;

import java.util.function.Function;
import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.support.DockerExecutable;
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

    private Function<StopContainersSettings, DockerExecutable> stopContainersExecutableCreator = StopContainersExecutable::new;

    @Override
    public String getFilter() {
        return filter;
    }

    /**
     * For testing purposes only: command creator.
     * @param creator command creator
     */
    void setStopContainersExecutableCreator(final Function<StopContainersSettings, DockerExecutable> creator) {
        this.stopContainersExecutableCreator = creator;
    }

    @Override
    protected void executeInternal() throws DockerException {
        stopContainersExecutableCreator.apply(this).execute();
    }

}

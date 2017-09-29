package nl.futureedge.maven.docker.mojo;

import java.util.function.Function;
import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.support.DockerExecutable;
import nl.futureedge.maven.docker.support.RemoveServicesExecutable;
import nl.futureedge.maven.docker.support.RemoveServicesSettings;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Remove services.
 */
@Mojo(name = "remove-services", requiresProject = false)
public final class RemoveServicesMojo extends AbstractDockerMojo implements RemoveServicesSettings {

    /**
     * Filter for services to remove (if empty, all services will be removed).
     */
    @Parameter(name = "filter", property = "docker.filter")
    private String filter;

    private Function<RemoveServicesSettings, DockerExecutable> removeServicesExecutableCreator = RemoveServicesExecutable::new;

    @Override
    public String getFilter() {
        return filter;
    }

    /**
     * For testing purposes only: command creator.
     * @param creator command creator
     */
    void setRemoveServicesExecutableCreator(final Function<RemoveServicesSettings, DockerExecutable> creator) {
        this.removeServicesExecutableCreator = creator;
    }

    @Override
    protected void executeInternal() throws DockerException {
        removeServicesExecutableCreator.apply(this).execute();
    }

}

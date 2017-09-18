package nl.futureedge.maven.docker.mojo;

import nl.futureedge.maven.docker.executor.DockerExecutionException;
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

    @Override
    public String getFilter() {
        return filter;
    }

    @Override
    protected void executeInternal() throws DockerExecutionException {
        new RemoveServicesExecutable(this).execute();
    }

}

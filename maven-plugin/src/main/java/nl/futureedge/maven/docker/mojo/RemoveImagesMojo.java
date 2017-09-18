package nl.futureedge.maven.docker.mojo;

import nl.futureedge.maven.docker.executor.DockerExecutionException;
import nl.futureedge.maven.docker.support.RemoveImagesExecutable;
import nl.futureedge.maven.docker.support.RemoveImagesSettings;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Remove images.
 */
@Mojo(name = "remove-images", requiresProject = false)
public final class RemoveImagesMojo extends AbstractDockerMojo implements RemoveImagesSettings {

    /**
     * Filter for images to remove (if empty, all images will be removed).
     */
    @Parameter(name = "filter", property = "docker.filter")
    private String filter;

    @Override
    public String getFilter() {
        return filter;
    }

    @Override
    protected void executeInternal() throws DockerExecutionException {
        new RemoveImagesExecutable(this).execute();
    }

}

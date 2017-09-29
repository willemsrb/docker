package nl.futureedge.maven.docker.mojo;

import java.util.function.Function;
import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.support.DockerExecutable;
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

    private Function<RemoveImagesSettings, DockerExecutable> removeImagesExecutableCreator = RemoveImagesExecutable::new;

    @Override
    public String getFilter() {
        return filter;
    }

    /**
     * For testing purposes only: command creator.
     * @param creator command creator
     */
    public void setRemoveImagesExecutableCreator(final Function<RemoveImagesSettings, DockerExecutable> creator) {
        this.removeImagesExecutableCreator = creator;
    }

    @Override
    protected void executeInternal() throws DockerException {
        removeImagesExecutableCreator.apply(this).execute();
    }

}

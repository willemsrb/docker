package nl.futureedge.maven.docker.support;

import java.util.List;
import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.executor.DockerCommands;
import nl.futureedge.maven.docker.executor.DockerExecutor;

public final class RemoveImagesExecutable extends DockerExecutable {

    private final String filter;

    public RemoveImagesExecutable(final RemoveImagesSettings settings) {
        super(settings);

        this.filter = settings.getFilter();
    }

    @Override
    public void execute() throws DockerException {
        debug("Remove images configuration: ");
        debug("- filter: " + filter);

        final DockerExecutor executor = createDockerExecutor();
        final List<String> images = doIgnoringFailure(() -> DockerCommands.listImages(executor, filter));

        if (images != null) {
            for (final String image : images) {
                if ("".equals(image.trim())) {
                    continue;
                }
                info("Remove image: " + image);
                doIgnoringFailure(() -> DockerCommands.removeImage(executor, image));
            }
        }
    }
}

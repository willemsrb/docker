package nl.futureedge.maven.docker.support;

import java.util.ArrayList;
import java.util.List;
import nl.futureedge.maven.docker.exception.DockerExecutionException;
import nl.futureedge.maven.docker.executor.DockerExecutor;

/**
 * Remove images.
 */
public final class RemoveImagesExecutable extends FilteredListExecutable {

    /**
     * Create a new docker command execution.
     * @param settings settings.
     */
    public RemoveImagesExecutable(final RemoveImagesSettings settings) {
        super(settings);
    }

    @Override
    protected List<String> list(final DockerExecutor executor, final String filter) throws DockerExecutionException {
        debug("Remove images configuration: ");
        debug("- filter: " + filter);

        final List<String> arguments = new ArrayList<>();
        arguments.add("images");
        arguments.add("-q");
        if ((filter != null) && !"".equals(filter)) {
            arguments.add("--filter");
            arguments.add(filter);
        }

        return executor.execute(arguments, false, true);
    }

    @Override
    protected void execute(final DockerExecutor executor, final String image) throws DockerExecutionException {
        info("Remove image: " + image);

        final List<String> arguments = new ArrayList<>();
        arguments.add("rmi");
        arguments.add("-f");
        arguments.add(image);

        executor.execute(arguments, false, false);
    }

}

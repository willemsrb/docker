package nl.futureedge.maven.docker.support;

import java.util.List;
import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.exception.DockerExecutionException;
import nl.futureedge.maven.docker.executor.DockerExecutor;

/**
 * Base docker command execution that executes a docker command for each item in a list.
 */
public abstract class FilteredListExecutable extends DockerExecutable {

    private final String filter;

    /**
     * Create a new docker command execution.
     * @param settings settings.
     */
    public FilteredListExecutable(final FilteredListSettings settings) {
        super(settings);

        this.filter = settings.getFilter();
    }

    @Override
    public final void execute() throws DockerException {
        final DockerExecutor executor = createDockerExecutor();
        final List<String> containers = doIgnoringFailure(() -> list(executor, filter));

        if (containers != null) {
            for (final String container : containers) {
                if ("".equals(container.trim())) {
                    continue;
                }
                doIgnoringFailure(() -> execute(executor, container));
            }
        }
    }

    /**
     * List the items.
     * @param executor docker executor
     * @param filter filter
     * @return item list
     * @throws DockerExecutionException on any errors
     */
    protected abstract List<String> list(final DockerExecutor executor, final String filter) throws DockerExecutionException;

    /**
     * Execute the functionality for a specific item.
     * @param executor docker executor
     * @param item item
     * @throws DockerExecutionException on any errors
     */
    protected abstract void execute(final DockerExecutor executor, final String item) throws DockerExecutionException;

}

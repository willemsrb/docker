package nl.futureedge.maven.docker.support;

import java.util.ArrayList;
import java.util.List;
import nl.futureedge.maven.docker.exception.DockerExecutionException;
import nl.futureedge.maven.docker.executor.DockerExecutor;

/**
 * Remove volumes.
 */
public final class RemoveVolumesExecutable extends FilteredListExecutable {

    /**
     * Create a new docker command execution.
     * @param settings settings.
     */
    public RemoveVolumesExecutable(final RemoveVolumesSettings settings) {
        super(settings);
    }

    @Override
    protected List<String> list(final DockerExecutor executor, final String filter) throws DockerExecutionException {
        debug("Remove volumes configuration: ");
        debug("- filter: " + filter);

        final List<String> arguments = new ArrayList<>();
        arguments.add("volume");
        arguments.add("ls");
        arguments.add("-q");
        if ((filter != null) && !"".equals(filter)) {
            arguments.add("--filter");
            arguments.add(filter);
        }

        return executor.execute(arguments, false, true);
    }

    @Override
    protected void execute(final DockerExecutor executor, final String volume) throws DockerExecutionException {
        info("Remove volume: " + volume);

        final List<String> arguments = new ArrayList<>();
        arguments.add("volume");
        arguments.add("rm");
        arguments.add(volume);

        executor.execute(arguments, false, false);
    }
}

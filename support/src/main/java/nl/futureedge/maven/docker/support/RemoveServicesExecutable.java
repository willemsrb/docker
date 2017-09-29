package nl.futureedge.maven.docker.support;

import java.util.ArrayList;
import java.util.List;
import nl.futureedge.maven.docker.exception.DockerExecutionException;
import nl.futureedge.maven.docker.executor.DockerExecutor;

/**
 * Remove services.
 */
public final class RemoveServicesExecutable extends FilteredListExecutable {

    /**
     * Create a new docker command execution.
     * @param settings settings.
     */
    public RemoveServicesExecutable(final RemoveServicesSettings settings) {
        super(settings);
    }

    @Override
    protected List<String> list(final DockerExecutor executor, final String filter) throws DockerExecutionException {
        debug("Remove services configuration: ");
        debug("- filter: " + filter);

        final List<String> arguments = new ArrayList<>();
        arguments.add("service");
        arguments.add("ls");
        arguments.add("-q");
        if ((filter != null) && !"".equals(filter)) {
            arguments.add("--filter");
            arguments.add(filter);
        }

        return executor.execute(arguments, false, true);
    }

    @Override
    protected void execute(final DockerExecutor executor, final String service) throws DockerExecutionException {
        info(String.format("Remove service: %s", service));

        final List<String> arguments = new ArrayList<>();
        arguments.add("service");
        arguments.add("rm");
        arguments.add(service);

        executor.execute(arguments, false, false);
    }

}

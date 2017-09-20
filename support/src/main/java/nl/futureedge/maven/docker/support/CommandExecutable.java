package nl.futureedge.maven.docker.support;

import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.executor.Docker;
import nl.futureedge.maven.docker.executor.DockerExecutor;

/**
 * Execute an arbitrary docker command.
 */
public final class CommandExecutable extends DockerExecutable {

    private final String command;

    public CommandExecutable(final CommandSettings settings) {
        super(settings);

        this.command = settings.getCommand();
    }

    public void execute() throws DockerException {
        debug("Command configuration: ");
        debug("- command: " + command);

        final DockerExecutor executor = createDockerExecutor();
        doIgnoringFailure(() -> execute(executor));
    }

    private void execute(final DockerExecutor executor) throws DockerException {
        executor.execute(Docker.splitOptions(command), true, false);
    }
}

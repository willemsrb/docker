package nl.futureedge.maven.docker.mojo;

import nl.futureedge.maven.docker.executor.DockerExecutionException;
import nl.futureedge.maven.docker.support.CommandExecutable;
import nl.futureedge.maven.docker.support.CommandSettings;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Run an arbitrary docker command.
 */
@Mojo(name = "command", requiresProject = false)
public final class CommandMojo extends AbstractDockerMojo implements CommandSettings {

    /**
     * Command to execute.
     */
    @Parameter(name = "command", property = "docker.command", required = true)
    private String command;

    @Override
    public String getCommand() {
        return command;
    }

    @Override
    protected void executeInternal() throws DockerExecutionException {
        new CommandExecutable(this).execute();
    }
}

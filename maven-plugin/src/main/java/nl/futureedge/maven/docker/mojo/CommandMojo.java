package nl.futureedge.maven.docker.mojo;

import java.util.function.Function;
import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.support.CommandExecutable;
import nl.futureedge.maven.docker.support.CommandSettings;
import nl.futureedge.maven.docker.support.DockerExecutable;
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

    private Function<CommandSettings, DockerExecutable> commandExecutableCreator = CommandExecutable::new;

    @Override
    public String getCommand() {
        return command;
    }

    /**
     * For testing purposes only: command creator.
     * @param creator command creator
     */
    void setCommandExecutableCreator(final Function<CommandSettings, DockerExecutable> creator) {
        this.commandExecutableCreator = creator;
    }

    @Override
    protected void executeInternal() throws DockerException {
        commandExecutableCreator.apply(this).execute();
    }
}

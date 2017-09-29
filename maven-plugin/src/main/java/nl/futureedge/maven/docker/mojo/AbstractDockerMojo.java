package nl.futureedge.maven.docker.mojo;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.support.DockerSettings;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Abstract mojo with common functionality for docker operations.
 */
public abstract class AbstractDockerMojo extends AbstractMojo implements DockerSettings {

    /**
     * Docker options. Could for example contain a host for a docker deamon (-H &lt;host>:&lt;port>) or TLS parameters.
     */
    @Parameter(name = "dockerOptions", property = "docker.dockerOptions")
    private String dockerOptions;

    /**
     * Should docker command failures be ignored?.
     */
    @Parameter(name = "ignoreFailures", property = "docker.ignoreFailures", defaultValue = "false")
    private boolean ignoreFailures;

    /**
     * Should this execution be skipped?
     */
    @Parameter(name = "skip", property = "docker.skip", defaultValue = "false")
    private boolean skip;


    @Override
    public final String getDockerOptions() {
        return dockerOptions;
    }

    @Override
    public final boolean isIgnoreFailures() {
        return ignoreFailures;
    }

    protected final boolean isSkip() {
        return skip;
    }

    @Override
    public final Consumer<String> getDebugLogger() {
        return getLog()::debug;
    }

    @Override
    public final Consumer<String> getInfoLogger() {
        return getLog()::info;
    }

    @Override
    public final BiConsumer<String, Exception> getWarnLogger() {
        return getLog()::warn;
    }

    @Override
    public final void execute() throws MojoExecutionException, MojoFailureException {
        if (isSkip()) {
            return;
        }

        try {
            executeInternal();
        } catch (DockerException e) {
            throw new MojoExecutionException("Docker command failed", e);
        }
    }

    protected abstract void executeInternal() throws DockerException;
}

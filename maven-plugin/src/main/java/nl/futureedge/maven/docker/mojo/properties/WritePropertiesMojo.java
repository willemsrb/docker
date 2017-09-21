package nl.futureedge.maven.docker.mojo.properties;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "write-properties", requiresProject = false)
public final class WritePropertiesMojo extends AbstractPropertiesMojo {

    /**
     * Target property file (will be overwritten if it exists).
     */
    @Parameter(name = "target", property = "properties.target", defaultValue = "${project.build.directory}/extracted.properties", required = true)
    private File target;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        // Write
        try (final OutputStream out = new FileOutputStream(target)) {
            getProperties().store(out, null);
        } catch (IOException e) {
            throw new MojoFailureException("Could not write property file", e);
        }
    }
}

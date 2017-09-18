package nl.futureedge.maven.docker.mojo.properties;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Properties;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = "write-properties", requiresProject = false)
public final class WritePropertiesMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    /**
     * Target property file (will be overwritten if it exists).
     */
    @Parameter(name = "target", property = "properties.target", defaultValue = "${project.build.directory}/extracted.properties", required = true)
    private File target;

    /**
     * Property keys to write (empty for all).
     */
    @Parameter(name = "keys", property = "properties.keys")
    private String[] keys;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().debug("Configuration: ");
        getLog().debug("- project: " + project);
        getLog().debug("- target: " + target);
        getLog().debug("- keys: " + (keys == null ? " null" : Arrays.asList(keys).toString()));

        final Properties properties = project.getProperties();

        // Get relevant properties
        final Properties result;
        if (keys != null && keys.length > 0) {
            result = new Properties();
            for (String key : keys) {
                result.setProperty(key, properties.getProperty(key, ""));
            }
        } else {
            result = properties;
        }

        // Write
        try (final OutputStream out = new FileOutputStream(target)) {
            result.store(out, null);
        } catch (IOException e) {
            throw new MojoFailureException("Could not write property file", e);
        }
    }
}

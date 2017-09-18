package nl.futureedge.maven.docker.mojo.properties;

import java.util.Arrays;
import java.util.Properties;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * Export properties to the System properties.
 */
@Mojo(name = "system-properties", requiresProject = false)
public final class SystemPropertiesMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    /**
     * Property keys to write (empty for all).
     */
    @Parameter(name = "keys", property = "properties.keys")
    private String[] keys;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Configuration: ");
        getLog().info("- project: " + project);
        getLog().info("- keys: " + (keys == null ? " null" : Arrays.asList(keys).toString()));

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

        for (final String propertyName : result.stringPropertyNames()) {
            System.setProperty(propertyName, result.getProperty(propertyName));
        }
    }
}

package nl.futureedge.maven.docker.mojo.properties;

import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = "echo-properties", requiresProject = false)
public final class EchoPropertiesMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        final Properties properties = project.getProperties();
        final SortedMap<String, String> display = new TreeMap<>();

        for (final String propertyName : properties.stringPropertyNames()) {
            display.put(propertyName, properties.getProperty(propertyName));
        }

        display.forEach((key, value) -> getLog().info(key + ": " + value));
    }
}

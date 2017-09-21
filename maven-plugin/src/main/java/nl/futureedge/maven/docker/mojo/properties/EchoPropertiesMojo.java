package nl.futureedge.maven.docker.mojo.properties;

import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "echo-properties", requiresProject = false)
public final class EchoPropertiesMojo extends AbstractPropertiesMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        final Properties properties = getProperties();
        final SortedMap<String, String> display = new TreeMap<>();

        for (final String propertyName : properties.stringPropertyNames()) {
            display.put(propertyName, properties.getProperty(propertyName));
        }

        display.forEach((key, value) -> getLog().info(key + ": " + value));
    }
}

package nl.futureedge.maven.docker.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import nl.futureedge.maven.docker.exception.DockerConfigurationException;

public class ConfigurationLoader {

    public static final String RESOURCE_BASE = "META-INF/docker/configuration/";

    private static final Gson GSON = new GsonBuilder().create();

    public static final Configuration loadConfiguration(final String configurationName) throws DockerConfigurationException {
        // Load configuration
        final String resourceName = RESOURCE_BASE + configurationName + ".json";
        final URL resource = ConfigurationLoader.class.getClassLoader().getResource(resourceName);
        if (resource == null) {
            throw new DockerConfigurationException("Could not find configuration '" + configurationName + "'");
        }

        final Configuration configuration;
        try (final Reader reader = new InputStreamReader(resource.openStream())) {
            configuration = GSON.fromJson(reader, Configuration.class);
        } catch (IOException e) {
            throw new DockerConfigurationException("Could not read configuration '" + configurationName + "'");
        }

        return configuration;
    }

}

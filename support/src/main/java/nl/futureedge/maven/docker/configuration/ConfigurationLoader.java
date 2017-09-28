package nl.futureedge.maven.docker.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import nl.futureedge.maven.docker.exception.DockerConfigurationException;

/**
 * Configuration loader.
 */
public class ConfigurationLoader {

    /**
     * Resource base.
     */
    public static final String RESOURCE_BASE = "META-INF/docker/configuration/";

    private static final Gson GSON = new GsonBuilder().create();

    /**
     * Loads a configuration from the {@link #RESOURCE_BASE} with the given name (suffix with .json).
     * @param configurationName configuration name
     * @return the loaded configuration
     * @throws DockerConfigurationException when a error occurs during the loading of the configuration
     */
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
        } catch (JsonParseException | IOException e) {
            throw new DockerConfigurationException("Could not read configuration '" + configurationName + "'");
        }

        return configuration;
    }

}

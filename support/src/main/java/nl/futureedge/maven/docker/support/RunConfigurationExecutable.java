package nl.futureedge.maven.docker.support;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Properties;
import java.util.Set;
import java.util.Stack;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import nl.futureedge.maven.docker.configuration.Configuration;
import nl.futureedge.maven.docker.executor.Docker;
import nl.futureedge.maven.docker.executor.DockerExecutionException;
import org.apache.commons.text.StrSubstitutor;

public class RunConfigurationExecutable extends DockerExecutable {

    public static final String RESOURCE_BASE = "META-INF/docker/configuration/";

    private final RunConfigurationSettings settings;
    private Set<String> loaded;
    private Stack<String> stack;
    private final String configurationName;

    private static final Gson GSON = new GsonBuilder().create();

    public RunConfigurationExecutable(final RunConfigurationSettings settings) {
        super(settings);

        this.settings = settings;
        this.loaded = settings.getLoaded();
        this.stack = settings.getStack();
        this.configurationName = settings.getConfigurationName();
    }

    @Override
    public void execute() throws DockerExecutionException {
        // Check already loaded
        if (loaded.contains(configurationName)) {
            debug("Skipping previously loaded configuration: " + configurationName);
            return;
        }

        // Check stack for cyclic dependency
        if (stack.contains(configurationName)) {
            throw new DockerExecutionException(
                    "Cyclic dependency detected; current stack: " + stack.stream().collect(Collectors.joining(" -> ")) + " -> " + configurationName);
        }
        stack.push(configurationName);

        // Load configuration
        Configuration configuration = loadConfiguration();

        // Recursive run 'dependsOn' configurations
        if (configuration.getDependsOn() != null && !configuration.getDependsOn().isEmpty()) {
            for (final String dependsOn : configuration.getDependsOn()) {
                debug("Dependent configuration: " + dependsOn);
                final RunConfigurationSettings runConfigurationSettings = new ConfiguredRunConfigurationSettings(settings, stack, loaded, dependsOn);
                new RunConfigurationExecutable(runConfigurationSettings).execute();
            }
        }

        // Run this configuration
        if (configuration.getImageName() != null && !"".equals(configuration.getImageName())) {
            info("Running configuration: " + configurationName);
            final RunSettings runSettings = new ConfiguredRunSettings(settings, configuration);
            final RunExecutable runExecutable = new RunExecutable(runSettings);
            runExecutable.execute();
            final String containerId = runExecutable.getContainerId();

            final InspectContainerSettings inspectContainerSettings = new ConfiguredInspectContainerSettings(settings, configuration, containerId);
            new InspectContainerExecutable(inspectContainerSettings).execute();
        }

        // Remove from stack
        final String finished = stack.pop();
        if (!configurationName.equals(finished)) {
            throw new DockerExecutionException("Stack corrupted; removed '" + finished + "' but expected '" + configurationName + "'");
        }
        loaded.add(configurationName);
    }

    private Configuration loadConfiguration() throws DockerExecutionException {
        // Load configuration
        final String resourceName = RESOURCE_BASE + configurationName + ".json";
        debug("Loading configuration: " + resourceName);
        final URL resource = getClass().getClassLoader().getResource(resourceName);
        if (resource == null) {
            throw new DockerExecutionException("Could not find configuration '" + configurationName + "'");
        }

        final Configuration configuration;
        try (final Reader reader = new InputStreamReader(resource.openStream())) {
            configuration = GSON.fromJson(reader, Configuration.class);
        } catch (IOException e) {
            throw new DockerExecutionException("Could not read configuration '" + configurationName + "'");
        }

        return configuration;
    }

    private static class ConfiguredDockerSettings implements DockerSettings {
        private final DockerSettings settings;

        ConfiguredDockerSettings(final RunConfigurationSettings settings) {
            this.settings = settings;
        }

        @Override
        public final Consumer<String> getDebugLogger() {
            return settings.getDebugLogger();
        }

        @Override
        public final Consumer<String> getInfoLogger() {
            return settings.getInfoLogger();
        }

        @Override
        public final BiConsumer<String, Exception> getWarnLogger() {
            return settings.getWarnLogger();
        }

        @Override
        public final String getDockerOptions() {
            return settings.getDockerOptions();
        }

        @Override
        public final boolean isIgnoreFailures() {
            return settings.isIgnoreFailures();
        }
    }

    private static class ConfiguredRunConfigurationSettings extends ConfiguredDockerSettings implements RunConfigurationSettings {

        private final RunConfigurationSettings settings;
        private final Stack<String> stack;
        private final Set<String> loaded;
        private final String configurationName;

        ConfiguredRunConfigurationSettings(final RunConfigurationSettings settings, final Stack<String> stack, final Set<String> loaded,
                                           final String configurationName) {
            super(settings);

            this.settings = settings;
            this.stack = stack;
            this.loaded = loaded;
            this.configurationName = configurationName;
        }

        @Override
        public Stack<String> getStack() {
            return stack;
        }

        @Override
        public Set<String> getLoaded() {
            return loaded;
        }

        @Override
        public Properties getProjectProperties() {
            return settings.getProjectProperties();
        }

        @Override
        public String getConfigurationName() {
            return configurationName;
        }

        @Override
        public String getNetworkName() {
            return settings.getNetworkName();
        }

        @Override
        public boolean isRandomPorts() {
            return settings.isRandomPorts();
        }
    }

    private static class ConfiguredRunSettings extends ConfiguredDockerSettings implements RunSettings {

        private final RunConfigurationSettings settings;
        private final Configuration configuration;

        ConfiguredRunSettings(final RunConfigurationSettings settings, final Configuration configuration) {
            super(settings);
            this.settings = settings;
            this.configuration = configuration;
        }

        @Override
        public final Properties getProjectProperties() {
            return settings.getProjectProperties();
        }

        @Override
        public final String getRunOptions() {
            final StringBuilder runOptions = new StringBuilder(configuration.getRunOptions());

            // Add network
            if (settings.getNetworkName() != null && !"".equals(settings.getNetworkName().trim())) {
                runOptions.append(" --network ").append(settings.getNetworkName());
            }

            // Add port mappings
            if (configuration.getPorts() != null) {
                for (final Configuration.Port port : configuration.getPorts()) {
                    runOptions.append(" -p ");
                    if (!settings.isRandomPorts() && port.getExternal() != null && !"".equals(port.getExternal().trim())) {
                        runOptions.append(port.getExternal()).append(":");
                    }
                    runOptions.append(port.getPort());
                }
            }

            return StrSubstitutor.replace(runOptions.toString(), settings.getProjectProperties());
        }

        @Override
        public final String getImage() {
            final String imageRegistry = StrSubstitutor.replace(configuration.getImageRegistry(), settings.getProjectProperties());
            final String imageName = StrSubstitutor.replace(configuration.getImageName(), settings.getProjectProperties());
            final String imageTag = StrSubstitutor.replace(configuration.getImageTag(), settings.getProjectProperties());

            return Docker.getImage(imageRegistry, imageName, imageTag);
        }

        @Override
        public final String getCommand() {
            return StrSubstitutor.replace(configuration.getCommand(), settings.getProjectProperties());
        }

        @Override
        public final String getContainerIdProperty() {
            return configuration.getContainerIdProperty();
        }
    }

    private static class ConfiguredInspectContainerSettings extends ConfiguredDockerSettings implements InspectContainerSettings {

        private final RunConfigurationSettings settings;
        private final Configuration configuration;
        private final String containerId;

        ConfiguredInspectContainerSettings(final RunConfigurationSettings settings, final Configuration configuration, final String containerId) {
            super(settings);
            this.settings = settings;
            this.configuration = configuration;
            this.containerId = containerId;
        }

        @Override
        public final Properties getProjectProperties() {
            return settings.getProjectProperties();
        }

        @Override
        public final String getContainerId() {
            return containerId;
        }

        @Override
        public final String getContainerNameProperty() {
            return configuration.getContainerNameProperty();
        }

        @Override
        public String getHostnameProperty() {
            return configuration.getHostnameProperty();
        }

        @Override
        public final Properties getPortProperties() {
            final Properties result = new Properties();
            for (final Configuration.Port port : configuration.getPorts()) {
                result.setProperty(port.getPort(), port.getProperty());
            }
            return result;
        }
    }
}

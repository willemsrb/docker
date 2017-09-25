package nl.futureedge.maven.docker.support;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Stack;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import nl.futureedge.maven.docker.configuration.Configuration;
import nl.futureedge.maven.docker.configuration.ConfigurationLoader;
import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.exception.DockerExecutionException;
import nl.futureedge.maven.docker.executor.Docker;
import org.apache.commons.text.StrSubstitutor;

public class RunConfigurationExecutable extends DockerExecutable {

    private final RunConfigurationSettings settings;
    private Set<String> loaded;
    private Stack<String> stack;
    private final String configurationName;
    private final boolean skipDependencies;

    public RunConfigurationExecutable(final RunConfigurationSettings settings) {
        super(settings);

        this.settings = settings;
        this.loaded = settings.getLoaded();
        this.stack = settings.getStack();
        this.configurationName = settings.getConfigurationName();
        this.skipDependencies = settings.isSkipDependencies();
    }

    @Override
    public void execute() throws DockerException {
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
        debug("Loading configuration: " + configurationName);
        Configuration configuration = ConfigurationLoader.loadConfiguration(configurationName);

        // Recursive run 'dependsOn' configurations
        if (skipDependencies) {
            debug("Skipping dependent configurations");
        } else {
            if (configuration.getDependsOn() != null && !configuration.getDependsOn().isEmpty()) {
                for (final String dependsOn : configuration.getDependsOn()) {
                    debug("Dependent configuration: " + dependsOn);
                    final RunConfigurationSettings runConfigurationSettings = new ConfiguredRunConfigurationSettings(settings, stack, loaded, dependsOn);
                    new RunConfigurationExecutable(runConfigurationSettings).execute();
                }
            }
        }

        // Run this configuration
        if (configuration.getImageName() != null && !"".equals(configuration.getImageName())) {
            info("Running configuration: " + configurationName);
            final RunSettings runSettings = new ConfiguredRunSettings(settings, configuration);
            final RunExecutable runExecutable = new RunExecutable(runSettings);
            runExecutable.execute();
            final String containerId = runExecutable.getContainerId();

            // Inspect
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
        public String getAdditionalRunOptions() {
            return null;
        }

        @Override
        public String getCommand() {
            return null;
        }

        @Override
        public String getNetworkName() {
            return settings.getNetworkName();
        }

        @Override
        public boolean isRandomPorts() {
            return settings.isRandomPorts();
        }

        @Override
        public boolean isSkipDependencies() {
            return settings.isSkipDependencies();
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

        private String replace(final String value) {
            final Properties valueProperties = settings.getProjectProperties();
            final Map<String, String> valueMap = new HashMap<>();
            final Enumeration<?> propNames = valueProperties.propertyNames();
            while (propNames.hasMoreElements()) {
                final String propName = (String) propNames.nextElement();
                final String propValue = valueProperties.getProperty(propName);
                valueMap.put(propName, propValue);
            }

            final StrSubstitutor substitutor = new StrSubstitutor(valueMap);
            substitutor.setValueDelimiter(':');

            return substitutor.replace(value);
        }

        @Override
        public final String getRunOptions() {
            final StringBuilder runOptions = new StringBuilder();
            if (configuration.getRunOptions() != null) {
                runOptions.append(configuration.getRunOptions());
            }

            if (settings.getAdditionalRunOptions() != null) {
                runOptions.append(settings.getAdditionalRunOptions());
            }

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

            return replace(runOptions.toString());
        }

        @Override
        public boolean isDaemon() {
            return configuration.isDaemon();
        }

        @Override
        public final String getImage() {
            final String imageRegistry = replace(configuration.getImageRegistry());
            final String imageName = replace(configuration.getImageName());
            final String imageTag = replace(configuration.getImageTag());

            return Docker.getImage(imageRegistry, imageName, imageTag);
        }

        @Override
        public final String getCommand() {
            final String command;
            if (settings.getCommand() != null && !"".equals(settings.getCommand().trim())) {
                command = settings.getCommand();
            } else {
                command = configuration.getCommand();
            }

            return replace(command);
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
            if (configuration.getPorts() != null) {
                for (final Configuration.Port port : configuration.getPorts()) {
                    result.setProperty(port.getPort(), port.getProperty());
                }
            }
            return result;
        }
    }
}

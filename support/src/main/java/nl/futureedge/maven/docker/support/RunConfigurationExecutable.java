package nl.futureedge.maven.docker.support;

import java.util.Deque;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import nl.futureedge.maven.docker.configuration.Configuration;
import nl.futureedge.maven.docker.configuration.ConfigurationLoader;
import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.exception.DockerExecutionException;
import nl.futureedge.maven.docker.executor.Docker;
import org.apache.commons.text.StrSubstitutor;

/**
 * Run a configured docker execution.
 */
public class RunConfigurationExecutable extends DockerExecutable {

    private final RunConfigurationSettings settings;
    private Set<String> loaded;
    private Deque<String> stack;
    private final String configurationName;
    private final boolean skipDependencies;

    /**
     * Create a new docker command execution.
     * @param settings settings.
     */
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
            debug(String.format("Skipping previously loaded configuration: %s", configurationName));
            return;
        }

        // Check stack for cyclic dependency
        if (stack.contains(configurationName)) {
            throw new DockerExecutionException(
                    "Cyclic dependency detected; current stack: " + stack.stream().collect(Collectors.joining(" -> ")) + " -> " + configurationName);
        }
        stack.push(configurationName);

        // Load configuration
        debug(String.format("Loading configuration: %s", configurationName));
        Configuration configuration = ConfigurationLoader.loadConfiguration(configurationName);

        // Recursive run 'dependsOn' configurations
        if (skipDependencies) {
            debug("Skipping dependent configurations");
        } else {
            if (configuration.getDependsOn() != null && !configuration.getDependsOn().isEmpty()) {
                for (final String dependsOn : configuration.getDependsOn()) {
                    debug(String.format("Dependent configuration: %s", dependsOn));
                    final RunConfigurationSettings runConfigurationSettings = RunConfigurationSettings.builder()
                            .setDebugLogger(settings.getDebugLogger())
                            .setInfoLogger(settings.getInfoLogger())
                            .setWarnLogger(settings.getWarnLogger())
                            .setDockerOptions(settings.getDockerOptions())
                            .setIgnoreFailures(settings.isIgnoreFailures())
                            .setStack(stack)
                            .setLoaded(loaded)
                            .setProjectProperties(settings.getProjectProperties())
                            .setConfigurationName(dependsOn)
                            .setNetworkName(settings.getNetworkName())
                            .setRandomPorts(settings.isRandomPorts())
                            .setSkipDependencies(settings.isSkipDependencies())
                            .build();
                    final RunConfigurationExecutable runConfigurationExecutable = new RunConfigurationExecutable(runConfigurationSettings);
                    runConfigurationExecutable.setFactory(getFactory());
                    runConfigurationExecutable.execute();
                }
            }
        }

        // Run this configuration
        if (configuration.getImageName() != null && !"".equals(configuration.getImageName())) {
            info(String.format("Running configuration: %s", configurationName));
            final RunSettings runSettings = RunSettings.builder()
                    .setDebugLogger(settings.getDebugLogger())
                    .setInfoLogger(settings.getInfoLogger())
                    .setWarnLogger(settings.getWarnLogger())
                    .setDockerOptions(settings.getDockerOptions())
                    .setIgnoreFailures(settings.isIgnoreFailures())
                    .setProjectProperties(settings.getProjectProperties())
                    .setRunOptions(createRunOptions(configuration))
                    .setDaemon(configuration.isDaemon())
                    .setImage(createImage(configuration))
                    .setCommand(createCommand(configuration))
                    .setContainerIdProperty(configuration.getContainerIdProperty())
                    .build();
            final RunExecutable runExecutable = new RunExecutable(runSettings);
            runExecutable.setFactory(getFactory());
            runExecutable.execute();
            final String containerId = runExecutable.getContainerId();

            // Inspect
            final InspectContainerSettings inspectContainerSettings = InspectContainerSettings.builder()
                    .setDebugLogger(settings.getDebugLogger())
                    .setInfoLogger(settings.getInfoLogger())
                    .setWarnLogger(settings.getWarnLogger())
                    .setDockerOptions(settings.getDockerOptions())
                    .setIgnoreFailures(settings.isIgnoreFailures())
                    .setProjectProperties(settings.getProjectProperties())
                    .setContainerId(containerId)
                    .setContainerNameProperty(configuration.getContainerNameProperty())
                    .setHostnameProperty(configuration.getHostnameProperty())
                    .setPortProperties(createPortProperties(configuration))
                    .build();
            final InspectContainerExecutable inspectContainerExecutable = new InspectContainerExecutable(inspectContainerSettings);
            inspectContainerExecutable.setFactory(getFactory());
            inspectContainerExecutable.execute();
        }

        // Remove from stack
        final String finished = stack.pop();
        if (!configurationName.equals(finished)) {
            throw new DockerExecutionException("Stack corrupted; removed '" + finished + "' but expected '" + configurationName + "'");
        }
        loaded.add(configurationName);
    }

    private String createRunOptions(final Configuration configuration) {
        final StringBuilder runOptions = new StringBuilder();
        if (configuration.getRunOptions() != null) {
            runOptions.append(configuration.getRunOptions());
        }

        if (settings.getAdditionalRunOptions() != null) {
            if (runOptions.length() > 0) {
                runOptions.append(' ');
            }
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

    private String createImage(final Configuration configuration) {
        final String imageRegistry = replace(configuration.getImageRegistry());
        final String imageName = replace(configuration.getImageName());
        final String imageTag = replace(configuration.getImageTag());

        return Docker.getImage(imageRegistry, imageName, imageTag);
    }

    private String createCommand(final Configuration configuration) {
        final String command;
        if (settings.getCommand() != null && !"".equals(settings.getCommand().trim())) {
            command = settings.getCommand();
        } else {
            command = configuration.getCommand();
        }

        return replace(command);
    }

    private Properties createPortProperties(final Configuration configuration) {
        final Properties result = new Properties();
        if (configuration.getPorts() != null) {
            for (final Configuration.Port port : configuration.getPorts()) {
                result.setProperty(port.getPort(), port.getProperty());
            }
        }
        return result;
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
}

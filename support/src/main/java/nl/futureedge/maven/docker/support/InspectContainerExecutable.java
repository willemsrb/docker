package nl.futureedge.maven.docker.support;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.executor.DockerExecutor;

public final class InspectContainerExecutable extends DockerExecutable {

    private final Properties projectProperties;
    private final String containerId;
    private final String containerNameProperty;
    private final String hostnameProperty;
    private final Properties portProperties;

    public InspectContainerExecutable(final InspectContainerSettings settings) {
        super(settings);

        this.projectProperties = settings.getProjectProperties();
        this.containerId = settings.getContainerId();
        this.containerNameProperty = settings.getContainerNameProperty();
        this.hostnameProperty = settings.getHostnameProperty();
        this.portProperties = settings.getPortProperties();
    }

    public void execute() throws DockerException {
        info("Inspect container configuration: ");
        debug("- projectProperties: " + projectProperties);
        info("- containerId: " + containerId);
        info("- containerNameProperty: " + containerNameProperty);
        info("- hostnameProperty: " + hostnameProperty);
        info("- portProperties: " + portProperties);

        final DockerExecutor executor = createDockerExecutor();
        final String containerInfoJson = doIgnoringFailure(() -> inspectContainer(executor, containerId));

        final JsonParser parser = new JsonParser();
        final JsonArray containerInfos = parser.parse(containerInfoJson).getAsJsonArray();
        final JsonObject containerInfo = containerInfos.get(0).getAsJsonObject();

        handleContainerName(containerInfo);
        handleHostname(containerInfo);
        handlePorts(containerInfo);
    }

    private void handleContainerName(final JsonObject containerInfo) {
        if (containerNameProperty == null || "".equals(containerNameProperty.trim())) {
            debug("Skipping container name as no property is set");
            return;
        }

        final JsonPrimitive name = containerInfo.getAsJsonPrimitive("Name");
        if (name == null) {
            warn("Name: Name primitive not found");
            return;
        }

        String containerName = name.getAsString();
        if (containerName.startsWith("/")) {
            containerName = containerName.substring(1);
        }
        info(String.format("Name: %s", containerName));
        projectProperties.setProperty(containerNameProperty, containerName);
    }

    private void handleHostname(final JsonObject containerInfo) {
        if (hostnameProperty == null || "".equals(hostnameProperty.trim())) {
            debug("Skipping hostname as no property is set");
            return;
        }

        final JsonObject config = containerInfo.getAsJsonObject("Config");
        if (config == null) {
            warn("Hostname: Config object not found");
            return;
        }
        final JsonPrimitive hostname = config.getAsJsonPrimitive("Hostname");
        if (hostname == null) {
            warn("Hostname: Hostname primitive not found");
            return;
        }

        info(String.format("Hostname: %s", hostname.getAsString()));
        projectProperties.setProperty(hostnameProperty, hostname.getAsString());
    }


    private void handlePorts(JsonObject containerInfo) {
        if (portProperties == null || portProperties.isEmpty()) {
            debug("Skipping port as no property is set");
            return;
        }

        final JsonObject networkSettings = containerInfo.getAsJsonObject("NetworkSettings");
        if (networkSettings == null) {
            warn("Ports: NetworkSettings object not found");
            return;
        }
        final JsonObject ports = networkSettings.getAsJsonObject("Ports");
        if (ports == null) {
            warn("Ports: Ports object not found");
            return;
        }

        for (final String portPropertyKey : portProperties.stringPropertyNames()) {
            final JsonArray mappings = ports.getAsJsonArray(portPropertyKey);
            if (mappings == null || mappings.size() == 0) {
                warn(String.format("Port %s: not mapped", portPropertyKey));
            } else {
                if (mappings.size() > 1) {
                    warn(String.format("Port %s: mapped multiple times; an undetermined mapping wil be returned", portPropertyKey));
                }
                final JsonObject mapping = mappings.get(0).getAsJsonObject();
                final JsonPrimitive port = mapping.getAsJsonPrimitive("HostPort");
                if (port == null) {
                    warn(String.format("Port %s: HostPort primitive not found", portPropertyKey));
                    continue;
                }
                info(String.format("Port %s: %s", portPropertyKey, port.getAsString()));
                projectProperties.setProperty(portProperties.getProperty(portPropertyKey), port.getAsString());
            }
        }
    }

    private String inspectContainer(final DockerExecutor executor, final String containerId) throws DockerException {
        final List<String> arguments = new ArrayList<>();
        arguments.add("inspect");
        arguments.add("--type");
        arguments.add("container");
        arguments.add(containerId);

        final List<String> result = executor.execute(arguments, false, true);
        return result.stream().collect(Collectors.joining());

    }
}

package nl.futureedge.maven.docker.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.exception.DockerExecutionException;
import nl.futureedge.maven.docker.executor.Docker;
import nl.futureedge.maven.docker.executor.DockerExecutor;

public final class RunExecutable extends DockerExecutable {

    private final Properties projectProperties;
    private final String runOptions;
    private final boolean daemon;
    private final String image;
    private final String command;
    private final String containerIdProperty;

    private String containerId;

    public RunExecutable(final RunSettings settings) {
        super(settings);

        this.projectProperties = settings.getProjectProperties();
        this.runOptions = settings.getRunOptions();
        this.daemon = settings.isDaemon();
        this.image = settings.getImage();
        this.command = settings.getCommand();
        this.containerIdProperty = settings.getContainerIdProperty();
    }

    public String getContainerId() {
        return containerId;
    }

    @Override
    public void execute() throws DockerException {
        info("Run configuration: ");
        debug("- projectProperties: " + projectProperties);
        debug("- runOptions: " + runOptions);
        debug("- daemon: " + daemon);
        info("- image: " + image);
        debug("- command: " + command);
        debug("- containerIdProperty: " + containerIdProperty);

        // Execute
        final DockerExecutor executor = createDockerExecutor();
        if (daemon) {
            containerId = doIgnoringFailure(() -> runContainer(executor, runOptions, image, command));
            info("ContainerId: " + containerId);

            // Set property in maven
            if (containerIdProperty != null && !"".equals(containerIdProperty.trim())) {
                projectProperties.setProperty(containerIdProperty, containerId);
            }
        } else {
            if(containerIdProperty != null && !"".equals(containerIdProperty)) {
                warn("ContainerIdProperty set but container is not run as daemon; container id will not be set!");
            }
            doIgnoringFailure(() -> executeContainer(executor, runOptions, image, command));
        }
    }

    private String runContainer(final DockerExecutor executor, final String runOptions, final String image, final String command)
            throws DockerExecutionException {
        final List<String> arguments = new ArrayList<>();
        arguments.add("run");
        arguments.addAll(Docker.splitOptions(runOptions));
        arguments.add("-d");

        arguments.add(image);

        if (command != null && !"".equals(command)) {
            arguments.addAll(Docker.splitOptions(command));
        }

        final List<String> executionResult = executor.execute(arguments, false, true);
        return executionResult.get(0);
    }

    private void executeContainer(final DockerExecutor executor, final String runOptions, final String image, final String command)
            throws DockerExecutionException {
        final List<String> arguments = new ArrayList<>();
        arguments.add("run");
        arguments.addAll(Docker.splitOptions(runOptions));

        arguments.add(image);

        if (command != null && !"".equals(command)) {
            arguments.addAll(Docker.splitOptions(command));
        }

        executor.execute(arguments, true, false);
    }
}

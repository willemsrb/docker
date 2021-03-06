package nl.futureedge.maven.docker.support;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.exception.DockerExecutionException;
import nl.futureedge.maven.docker.executor.Docker;
import nl.futureedge.maven.docker.executor.DockerExecutor;

/**
 * Run a docker container.
 */
public final class RunExecutable extends DockerExecutable {

    private final Properties projectProperties;
    private final String runOptions;
    private final boolean daemon;
    private final String image;
    private final String command;
    private final String containerIdProperty;

    private String containerId;

    /**
     * Create a new docker command execution.
     * @param settings settings.
     */
    public RunExecutable(final RunSettings settings) {
        super(settings);

        this.projectProperties = settings.getProjectProperties();
        this.runOptions = settings.getRunOptions();
        this.daemon = settings.isDaemon();
        this.image = settings.getImage();
        this.command = settings.getCommand();
        this.containerIdProperty = settings.getContainerIdProperty();
    }

    /**
     * Return the container id retrieved after running the container.
     * @return container id
     */
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
        } else {
            containerId = doIgnoringFailure(() -> executeContainer(executor, runOptions, image, command));
        }
        info(String.format("Container id: %s", containerId));

        // Set property in maven
        if (containerIdProperty != null && !"".equals(containerIdProperty.trim())) {
            projectProperties.setProperty(containerIdProperty, containerId);
        }

    }

    private String runContainer(final DockerExecutor executor, final String runOptions, final String image, final String command)
            throws DockerExecutionException {
        final List<String> arguments = new ArrayList<>();
        // run
        arguments.add("run");
        arguments.addAll(Docker.splitOptions(runOptions));

        // daemon
        arguments.add("-d");

        // image
        arguments.add(image);

        // command
        if (command != null && !"".equals(command)) {
            arguments.addAll(Docker.splitOptions(command));
        }

        // execute
        final List<String> executionResult = executor.execute(arguments, false, true);

        // return container id from output
        if (executionResult.isEmpty()) {
            throw new DockerExecutionException("Docker command did not return a container id");
        }
        return executionResult.get(0);
    }

    private String executeContainer(final DockerExecutor executor, final String runOptions, final String image, final String command)
            throws DockerExecutionException {
        final List<String> arguments = new ArrayList<>();

        // run
        arguments.add("run");
        arguments.addAll(Docker.splitOptions(runOptions));

        // add cidfile to run options
        Path cidFile = null;
        try {
            cidFile = Files.createTempFile("docker-maven-plugin", "cidfile");
            Files.delete(cidFile);

            arguments.add("--cidfile");
            arguments.add(cidFile.toFile().getAbsolutePath());

            // image
            arguments.add(image);

            // command
            if (command != null && !"".equals(command)) {
                arguments.addAll(Docker.splitOptions(command));
            }

            // execute
            executor.execute(arguments, true, false);

            // read container id from cidfile
            try (final BufferedReader reader = new BufferedReader(new FileReader(cidFile.toFile()))) {
                return reader.readLine();
            }
        } catch (IOException e) {
            throw new DockerExecutionException("Could not read cidfile", e);
        } finally {
            if (cidFile != null) {
                try {
                    Files.delete(cidFile);
                } catch (final IOException e) {
                    // Ignore
                }
            }
        }
    }
}

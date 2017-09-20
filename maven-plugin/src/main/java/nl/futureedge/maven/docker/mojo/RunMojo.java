package nl.futureedge.maven.docker.mojo;

import java.util.Properties;
import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.exception.DockerExecutionException;
import nl.futureedge.maven.docker.executor.Docker;
import nl.futureedge.maven.docker.support.InspectContainerExecutable;
import nl.futureedge.maven.docker.support.InspectContainerSettings;
import nl.futureedge.maven.docker.support.RunExecutable;
import nl.futureedge.maven.docker.support.RunSettings;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * Run a container.
 */
@Mojo(name = "run", requiresProject = false)
public final class RunMojo extends AbstractDockerMojo implements RunSettings, InspectContainerSettings {

    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    /**
     * Run options.
     */
    @Parameter(name = "runOptions", property = "docker.runOptions")
    private String runOptions;

    /**
     * Should this container be run as a daemon?
     */
    @Parameter(name = "daemon", property = "docker.daemon", defaultValue = "true")
    private boolean daemon;

    /**
     * Image to run (should contain complete image identifier including registry and version; else use the imageRegistry, imageName and imageTag arguments).
     */
    @Parameter(name = "image", property = "docker.image")
    private String image;

    /**
     * Registry of image to run.
     */
    @Parameter(name = "imageRegistry", property = "docker.imageRegistry")
    private String imageRegistry;
    /**
     * Name of image to run.
     */
    @Parameter(name = "imageName", property = "docker.imageRegistry")
    private String imageName;
    /**
     * Tag of image to run.
     */
    @Parameter(name = "imageTag", property = "docker.imageRegistry")
    private String imageTag;

    /**
     * Command to run (override default image command).
     */
    @Parameter(name = "command", property = "docker.command")
    private String command;

    private String containerId;

    /**
     * Project property to store container id in.
     */
    @Parameter(name = "containerIdProperty", property = "docker.containerIdProperty")
    private String containerIdProperty;

    /**
     * Property name to store the container name.
     */
    @Parameter(name = "containerNameProperty", property = "docker.containerNameProperty")
    private String containerNameProperty;

    /**
     * Property name to store the host name.
     */
    @Parameter(name = "hostnameProperty", property = "docker.hostnameProperty")
    private String hostnameProperty;

    /**
     * Properties to store mapped ports in (should be in the form of &lt;port>=&lt;property>,&lt;port>=&lt;property>).
     */
    @Parameter(name = "portPropertiesAsString", property = "docker.portProperties")
    private String[] portPropertiesAsString;

    /**
     * Properties to store mapped ports in (keys should contain port to inspect, value should contain property name to store mapping in).
     */
    @Parameter(name = "portProperties")
    private Properties portProperties;

    @Override
    public Properties getProjectProperties() {
        return project.getProperties();
    }

    @Override
    public String getRunOptions() {
        return runOptions;
    }

    @Override
    public boolean isDaemon() {
        return daemon;
    }

    @Override
    public String getImage() {
        if (image == null || "".equals(image.trim())) {
            return Docker.getImage(imageRegistry, imageName, imageTag);
        } else {
            return image;
        }
    }

    @Override
    public String getCommand() {
        return command;
    }

    @Override
    public String getContainerId() {
        return containerId;
    }

    @Override
    public String getContainerIdProperty() {
        return containerIdProperty;
    }

    @Override
    public String getContainerNameProperty() {
        return containerNameProperty;
    }

    @Override
    public String getHostnameProperty() {
        return hostnameProperty;
    }

    @Override
    public Properties getPortProperties() {
        return InspectContainerMojo.getPortProperties(portPropertiesAsString, portProperties);
    }

    @Override
    protected void executeInternal() throws DockerException {
        if (getImage() == null || "".equals(getImage().trim())) {
            throw new DockerExecutionException("No image (user attribute image or attributes imageRegistry, imageName and imageTag configured to run");
        }

        final RunExecutable runExecutable = new RunExecutable(this);
        runExecutable.execute();

        // Quality of life: Inspect container after run
        this.containerId = runExecutable.getContainerId();
        new InspectContainerExecutable(this).execute();
    }
}

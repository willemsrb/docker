package nl.futureedge.maven.docker.mojo;

import java.util.Properties;
import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.support.InspectContainerExecutable;
import nl.futureedge.maven.docker.support.InspectContainerSettings;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * Inspect a container.
 */
@Mojo(name = "inspect-container", requiresProject = false)
public final class InspectContainerMojo extends AbstractDockerMojo implements InspectContainerSettings {

    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    /**
     * Identifier of the container to inspect.
     */
    @Parameter(name = "containerId", property = "docker.containerId", required = true)
    private String containerId;

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
    public String getContainerId() {
        return containerId;
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
        return getPortProperties(portPropertiesAsString, portProperties);
    }

    public static Properties getPortProperties(final String[] portPropertiesAsString, final Properties portProperties) {
        final Properties result = new Properties();
        if (portProperties != null) {
            for (String name : portProperties.stringPropertyNames()) {
                result.setProperty(name, portProperties.getProperty(name));
            }
        }

        if (portPropertiesAsString != null) {
            for (String portProperty : portPropertiesAsString) {
                int separator = portProperty.indexOf('=');
                if (separator != -1) {
                    result.setProperty(portProperty.substring(0, separator), portProperty.substring(separator + 1));
                }
            }
        }

        return result;
    }

    @Override
    protected void executeInternal() throws DockerException {
        new InspectContainerExecutable(this).execute();
    }
}

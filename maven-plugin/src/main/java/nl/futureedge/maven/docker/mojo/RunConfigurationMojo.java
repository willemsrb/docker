package nl.futureedge.maven.docker.mojo;


import java.util.Properties;
import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.support.RunConfigurationExecutable;
import nl.futureedge.maven.docker.support.RunConfigurationSettings;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * Run a configuration.
 */
@Mojo(name = "run-configuration", requiresProject = false)
public final class RunConfigurationMojo extends AbstractDockerMojo implements RunConfigurationSettings {

    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    /**
     * Properties to load before running the configuration.
     */
    @Parameter(name = "configurationProperties", property = "docker.configurationProperties")
    private Properties configurationProperties;

    /**
     * Configuration name to run.
     */
    @Parameter(name = "configurationName", property = "docker.configurationName", required = true)
    private String configurationName;

    /**
     * Network to bind all configurations to (adds --network &lt;name> to all 'run' commands).
     */
    @Parameter(name = "networkName", property = "docker.networkName")
    private String networkName;

    /**
     * Should the port mapping of configurations be ignored to let docker assign random port mappings?.
     */
    @Parameter(name = "randomPorts", property = "docker.randomPorts", defaultValue = "false")
    private boolean randomPorts;

    /**
     * Should the dependencies (dependsOn) of this configuration be skipped?
     */
    @Parameter(name = "skipDependencies", property = "docker.skipDependencies", defaultValue = "false")
    private boolean skipDependencies;


    @Override
    public Properties getProjectProperties() {
        return project.getProperties();
    }

    @Override
    public String getConfigurationName() {
        return configurationName;
    }

    @Override
    public String getNetworkName() {
        return networkName;
    }

    @Override
    public boolean isRandomPorts() {
        return randomPorts;
    }

    @Override
    public boolean isSkipDependencies() {
        return skipDependencies;
    }

    @Override
    protected void executeInternal() throws DockerException {
        // Load properties
        for (final String propertyName : configurationProperties.stringPropertyNames()) {
            project.getProperties().setProperty(propertyName, configurationProperties.getProperty(propertyName));
        }

        new RunConfigurationExecutable(this).execute();
    }
}

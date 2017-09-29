package nl.futureedge.maven.docker.mojo;

import java.util.UUID;
import java.util.function.Function;
import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.support.CreateNetworkExecutable;
import nl.futureedge.maven.docker.support.CreateNetworkSettings;
import nl.futureedge.maven.docker.support.DockerExecutable;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * Create a network.
 */
@Mojo(name = "create-network", requiresProject = false)
public final class CreateNetworkMojo extends AbstractDockerMojo implements CreateNetworkSettings {

    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    /**
     * Network options.
     */
    @Parameter(name = "networkOptions", property = "docker.networkOptions")
    private String networkOptions;

    /**
     * Network name (if none is given a random name will be generated).
     */
    @Parameter(name = "networkName", property = "docker.networkName")
    private String networkName;

    /**
     * Property to store the network name in.
     */
    @Parameter(name = "networkNameProperty", property = "docker.networkNameProperty")
    private String networkNameProperty;

    private Function<CreateNetworkSettings, DockerExecutable> createNetworkExecutableCreator = CreateNetworkExecutable::new;

    @Override
    public String getNetworkOptions() {
        return networkOptions;
    }

    @Override
    public String getNetworkName() {
        return networkName;
    }


    /**
     * For testing purposes only: command creator.
     * @param creator command creator
     */
    void setCreateNetworkExecutableCreator(final Function<CreateNetworkSettings, DockerExecutable> creator) {
        this.createNetworkExecutableCreator = creator;
    }

    @Override
    protected void executeInternal() throws DockerException {
        if (networkName == null || "".equals(networkName.trim())) {
            // Generate random name
            getLog().info("Generating random network name");
            networkName = "generated-" + UUID.randomUUID().toString();
        }

        createNetworkExecutableCreator.apply(this).execute();

        if (networkNameProperty != null && !"".equals(networkNameProperty)) {
            project.getProperties().setProperty(networkNameProperty, networkName);
        }
    }
}

package nl.futureedge.maven.docker.configuration;

import nl.futureedge.maven.docker.exception.DockerConfigurationException;
import org.junit.Assert;
import org.junit.Test;

public class ConfigurationLoaderTest {

    @Test
    public void test() throws DockerConfigurationException {
        Configuration configuration = ConfigurationLoader.loadConfiguration("test");
        Assert.assertEquals("registry", configuration.getImageRegistry());
        Assert.assertEquals("alpine", configuration.getImageName());
        Assert.assertEquals("tag", configuration.getImageTag());
        Assert.assertEquals("runOption1 runOption2", configuration.getRunOptions());
        Assert.assertEquals(false, configuration.isDaemon());
        Assert.assertEquals("command", configuration.getCommand());
        Assert.assertEquals("test.containerId", configuration.getContainerIdProperty());
        Assert.assertEquals("test.containerName", configuration.getContainerNameProperty());
        Assert.assertEquals("test.host", configuration.getHostnameProperty());
        Assert.assertNotNull(configuration.getPorts());
        Assert.assertEquals(2, configuration.getPorts().size());
        Assert.assertEquals("80/tcp", configuration.getPorts().get(0).getPort());
        Assert.assertEquals("80", configuration.getPorts().get(0).getExternal());
        Assert.assertEquals("test.port.http", configuration.getPorts().get(0).getProperty());
        Assert.assertEquals("82/tcp", configuration.getPorts().get(1).getPort());
        Assert.assertEquals("82", configuration.getPorts().get(1).getExternal());
        Assert.assertEquals("test.port.test", configuration.getPorts().get(1).getProperty());
        Assert.assertNotNull(configuration.getDependsOn());
        Assert.assertEquals(2, configuration.getDependsOn().size());
        Assert.assertEquals("test-2", configuration.getDependsOn().get(0));
        Assert.assertEquals("test-3", configuration.getDependsOn().get(1));
    }

    @Test(expected = DockerConfigurationException.class)
    public void nonExistent() throws DockerConfigurationException {
        ConfigurationLoader.loadConfiguration("nonexistent");
    }

    @Test(expected = DockerConfigurationException.class)
    public void invalidJson() throws DockerConfigurationException {
        ConfigurationLoader.loadConfiguration("invalid");
    }
}

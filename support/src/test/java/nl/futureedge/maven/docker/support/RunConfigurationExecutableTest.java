package nl.futureedge.maven.docker.support;

import java.util.Arrays;
import java.util.Properties;
import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.executor.DockerExecutor;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class RunConfigurationExecutableTest {

    @Test
    public void test() throws DockerException {
        final DockerExecutor executor = Mockito.mock(DockerExecutor.class);
        Mockito.when(executor.execute(
                Arrays.asList("run", "--network", "my-network", "-d", "alpine-2"), false, true))
                .thenReturn(Arrays.asList("container-2"));

        Mockito.when(executor.execute(Arrays.asList("inspect", "--type", "container", "container-2"), false, true))
                .thenReturn(Arrays.asList("[{}]"));

        Mockito.when(executor.execute(
                Arrays.asList("run", "runOption1", "runOption2", "--run", "options", "--network", "my-network", "-p", "80/tcp", "-p", "82/tcp", "-d",
                        "registry/alpine-1:tag-1", "theCommand", "to", "execute")
                , false, true)).thenReturn(Arrays.asList("container-1"));

        Mockito.when(executor.execute(Arrays.asList("inspect", "--type", "container", "container-1"), false, true))
                .thenReturn(Arrays.asList("[{"
                        + "  \"Name\": \"TheName-1\", "
                        + "  \"Config\": {"
                        + "      \"Hostname\": \"TheHost-1\""
                        + "  },"
                        + "  \"NetworkSettings\": {"
                        + "      \"Ports\": {"
                        + "          \"80/tcp\": [{"
                        + "              \"HostPort\": \"12345\""
                        + "          }],"
                        + "          \"82/tcp\": [{"
                        + "              \"HostPort\": \"12346\""
                        + "          }]"
                        + "      }"
                        + "  }"
                        + "}]"
                ));

        final Properties projectProperties = new Properties();

        final RunConfigurationSettings settings = RunConfigurationSettings.builder()
                .setProjectProperties(projectProperties)
                .setConfigurationName("configuration-test")
                .setAdditionalRunOptions("--run options")
                .setCommand("theCommand to execute")
                .setNetworkName("my-network")
                .setRandomPorts(true)
                .setSkipDependencies(false)
                .build();

        final RunConfigurationExecutable subject = new RunConfigurationExecutable(settings);
        subject.setFactory(new StaticDockerExecutorFactory(executor));
        subject.execute();

        Mockito.verify(executor).execute(Arrays.asList("run", "--network", "my-network", "-d", "alpine-2"), false, true);
        Mockito.verify(executor).execute(Arrays.asList("inspect", "--type", "container", "container-2"), false, true);
        Mockito.verify(executor).execute(Arrays.asList("run", "runOption1", "runOption2", "--run", "options", "--network", "my-network", "-p", "80/tcp",
                "-p", "82/tcp", "-d", "registry/alpine-1:tag-1", "theCommand", "to", "execute"), false, true);
        Mockito.verify(executor).execute(Arrays.asList("inspect", "--type", "container", "container-1"), false, true);
        Mockito.verifyNoMoreInteractions(executor);

        Assert.assertEquals(5, projectProperties.size());
        Assert.assertEquals("container-1", projectProperties.getProperty("test.containerId"));
        Assert.assertEquals("TheName-1", projectProperties.getProperty("test.containerName"));
        Assert.assertEquals("TheHost-1", projectProperties.getProperty("test.host"));
        Assert.assertEquals("12345", projectProperties.getProperty("test.port.http"));
        Assert.assertEquals("12346", projectProperties.getProperty("test.port.test"));
    }


    @Test
    public void test2() throws DockerException {
        final DockerExecutor executor = Mockito.mock(DockerExecutor.class);
        Mockito.when(executor.execute(
                Arrays.asList("run", "runOption1", "runOption2", "--network", "my-network", "-p", "80:80/tcp", "-p", "82:82/tcp", "-d",
                        "registry/alpine-1:tag-1", "command"), false, true)).thenReturn(Arrays.asList("container-1"));

        Mockito.when(executor.execute(Arrays.asList("inspect", "--type", "container", "container-1"), false, true))
                .thenReturn(Arrays.asList("[{"
                        + "  \"Name\": \"TheName-1\", "
                        + "  \"Config\": {"
                        + "      \"Hostname\": \"TheHost-1\""
                        + "  },"
                        + "  \"NetworkSettings\": {"
                        + "      \"Ports\": {"
                        + "          \"80/tcp\": [{"
                        + "              \"HostPort\": \"80\""
                        + "          }],"
                        + "          \"82/tcp\": [{"
                        + "              \"HostPort\": \"82\""
                        + "          }]"
                        + "      }"
                        + "  }"
                        + "}]"
                ));

        final Properties projectProperties = new Properties();

        final RunConfigurationSettings settings = RunConfigurationSettings.builder()
                .setProjectProperties(projectProperties)
                .setConfigurationName("configuration-test")
                .setNetworkName("my-network")
                .setRandomPorts(false)
                .setSkipDependencies(true)
                .build();

        final RunConfigurationExecutable subject = new RunConfigurationExecutable(settings);
        subject.setFactory(new StaticDockerExecutorFactory(executor));
        subject.execute();

        Mockito.verify(executor).execute(Arrays.asList("run", "runOption1", "runOption2", "--network", "my-network", "-p", "80:80/tcp", "-p", "82:82/tcp", "-d",
                "registry/alpine-1:tag-1", "command"), false, true);
        Mockito.verify(executor).execute(Arrays.asList("inspect", "--type", "container", "container-1"), false, true);
        Mockito.verifyNoMoreInteractions(executor);

        Assert.assertEquals(5, projectProperties.size());
        Assert.assertEquals("container-1", projectProperties.getProperty("test.containerId"));
        Assert.assertEquals("TheName-1", projectProperties.getProperty("test.containerName"));
        Assert.assertEquals("TheHost-1", projectProperties.getProperty("test.host"));
        Assert.assertEquals("80", projectProperties.getProperty("test.port.http"));
        Assert.assertEquals("82", projectProperties.getProperty("test.port.test"));
    }
}

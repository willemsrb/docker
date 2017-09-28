package nl.futureedge.maven.docker.support;

import java.util.Arrays;
import java.util.Properties;
import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.executor.DockerExecutor;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class InspectContainerExecutableTest {

    @Test
    public void test() throws DockerException {
        final Properties projectProperties = new Properties();
        final InspectContainerSettings settings = InspectContainerSettings.builder()
                .setProjectProperties(projectProperties)
                .setContainerId("containerId333")
                .setContainerNameProperty("container.name")
                .setHostnameProperty("host.name")
                .setPortProperties(buildPortProperties())
                .build();

        final DockerExecutor executor = Mockito.mock(DockerExecutor.class);
        Mockito.when(executor.execute(Mockito.eq(Arrays.asList("inspect", "--type", "container", "containerId333")), Mockito.eq(false), Mockito.eq(true)))
                .thenReturn(Arrays.asList("[{"
                        + "  \"Name\": \"TheName\", "
                        + "  \"Config\": {"
                        + "      \"Hostname\": \"TheHost\""
                        + "  },"
                        + "  \"NetworkSettings\": {"
                        + "      \"Ports\": {"
                        + "          \"80/tcp\": [{"
                        + "              \"HostPort\": \"12345\""
                        + "          }],"
                        + "          \"83/tcp\": [{"
                        + "              \"HostPort\": \"23456\""
                        + "          }],"
                        + "          \"84/tcp\": [{"
                        + "              \"HostPort\": \"34567\""
                        + "          }, {"
                        + "              \"HostPort\": \"45678\""
                        + "          }]"
                        + "      }"
                        + "  }"
                        + "}]"));

        final InspectContainerExecutable subject = new InspectContainerExecutable(settings);
        subject.setFactory(new StaticDockerExecutorFactory(executor));
        subject.execute();

        Mockito.verify(executor).execute(Mockito.eq(Arrays.asList("inspect", "--type", "container", "containerId333")), Mockito.eq(false), Mockito.eq(true));
        Mockito.verifyNoMoreInteractions(executor);

        Assert.assertEquals(4, projectProperties.size());
        Assert.assertEquals("TheName", projectProperties.getProperty("container.name"));
        Assert.assertEquals("TheHost", projectProperties.getProperty("host.name"));
        Assert.assertEquals("12345", projectProperties.getProperty("port.80"));
        Assert.assertEquals("34567", projectProperties.getProperty("port.84"));
    }

    @Test
    public void testSpecialCaseContainerName() throws DockerException {
        final Properties projectProperties = new Properties();
        final InspectContainerSettings settings = InspectContainerSettings.builder()
                .setProjectProperties(projectProperties)
                .setContainerId("containerId333")
                .setContainerNameProperty("container.name")
                .build();

        final DockerExecutor executor = Mockito.mock(DockerExecutor.class);
        Mockito.when(executor.execute(Mockito.eq(Arrays.asList("inspect", "--type", "container", "containerId333")), Mockito.eq(false), Mockito.eq(true)))
                .thenReturn(Arrays.asList("[{"
                        + "  \"Name\": \"/TheName\""
                        + "}]"));

        final InspectContainerExecutable subject = new InspectContainerExecutable(settings);
        subject.setFactory(new StaticDockerExecutorFactory(executor));
        subject.execute();

        Mockito.verify(executor).execute(Mockito.eq(Arrays.asList("inspect", "--type", "container", "containerId333")), Mockito.eq(false), Mockito.eq(true));
        Mockito.verifyNoMoreInteractions(executor);

        Assert.assertEquals(1, projectProperties.size());
        Assert.assertEquals("TheName", projectProperties.getProperty("container.name"));
    }

    @Test
    public void testNoObjectLevel1() throws DockerException {
        final Properties projectProperties = new Properties();
        final InspectContainerSettings settings = InspectContainerSettings.builder()
                .setProjectProperties(projectProperties)
                .setContainerId("containerId333")
                .setContainerNameProperty("container.name")
                .setHostnameProperty("host.name")
                .setPortProperties(buildPortProperties())
                .build();

        final DockerExecutor executor = Mockito.mock(DockerExecutor.class);
        Mockito.when(executor.execute(Mockito.eq(Arrays.asList("inspect", "--type", "container", "containerId333")), Mockito.eq(false), Mockito.eq(true)))
                .thenReturn(Arrays.asList("[{"
                        + "}]"));

        final InspectContainerExecutable subject = new InspectContainerExecutable(settings);
        subject.setFactory(new StaticDockerExecutorFactory(executor));
        subject.execute();

        Mockito.verify(executor).execute(Mockito.eq(Arrays.asList("inspect", "--type", "container", "containerId333")), Mockito.eq(false), Mockito.eq(true));
        Mockito.verifyNoMoreInteractions(executor);

        Assert.assertEquals(0, projectProperties.size());
    }

    @Test
    public void testNoObjectLevel2() throws DockerException {
        final Properties projectProperties = new Properties();
        final InspectContainerSettings settings = InspectContainerSettings.builder()
                .setProjectProperties(projectProperties)
                .setContainerId("containerId333")
                .setHostnameProperty("host.name")
                .setPortProperties(buildPortProperties())
                .build();

        final DockerExecutor executor = Mockito.mock(DockerExecutor.class);
        Mockito.when(executor.execute(Mockito.eq(Arrays.asList("inspect", "--type", "container", "containerId333")), Mockito.eq(false), Mockito.eq(true)))
                .thenReturn(Arrays.asList("[{"
                        + "  \"Name\": \"TheName\", "
                        + "  \"Config\": {"
                        + "  },"
                        + "  \"NetworkSettings\": {"
                        + "  }"
                        + "}]"));

        final InspectContainerExecutable subject = new InspectContainerExecutable(settings);
        subject.setFactory(new StaticDockerExecutorFactory(executor));
        subject.execute();

        Mockito.verify(executor).execute(Mockito.eq(Arrays.asList("inspect", "--type", "container", "containerId333")), Mockito.eq(false), Mockito.eq(true));
        Mockito.verifyNoMoreInteractions(executor);

        Assert.assertEquals(0, projectProperties.size());
    }

    @Test
    public void testNoObjectLevel3() throws DockerException {
        final Properties projectProperties = new Properties();
        final InspectContainerSettings settings = InspectContainerSettings.builder()
                .setProjectProperties(projectProperties)
                .setContainerId("containerId333")
                .setPortProperties(buildPortProperties())
                .build();

        final DockerExecutor executor = Mockito.mock(DockerExecutor.class);
        Mockito.when(executor.execute(Mockito.eq(Arrays.asList("inspect", "--type", "container", "containerId333")), Mockito.eq(false), Mockito.eq(true)))
                .thenReturn(Arrays.asList("[{"
                        + "  \"Name\": \"TheName\", "
                        + "  \"Config\": {"
                        + "      \"Hostname\": \"TheHost\""
                        + "  },"
                        + "  \"NetworkSettings\": {"
                        + "      \"Ports\": {"
                        + "          \"80/tcp\": [{"
                        + "          }]"
                        + "      }"
                        + "  }"
                        + "}]"));

        final InspectContainerExecutable subject = new InspectContainerExecutable(settings);
        subject.setFactory(new StaticDockerExecutorFactory(executor));
        subject.execute();

        Mockito.verify(executor).execute(Mockito.eq(Arrays.asList("inspect", "--type", "container", "containerId333")), Mockito.eq(false), Mockito.eq(true));
        Mockito.verifyNoMoreInteractions(executor);

        Assert.assertEquals(0, projectProperties.size());
    }


    private Properties buildPortProperties() {
        Properties portProperties = new Properties();

        portProperties.setProperty("80/tcp", "port.80");
        portProperties.setProperty("82/tcp", "port.82");
        portProperties.setProperty("84/tcp", "port.84");

        return portProperties;

    }
}

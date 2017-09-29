package nl.futureedge.maven.docker.mojo;

import java.util.Properties;
import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.support.DockerExecutable;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class InspectContainerMojoTest {

    @Test
    public void test() throws MojoExecutionException, MojoFailureException, DockerException {
        final MavenProject project = Mockito.mock(MavenProject.class);
        final Properties projectProperties = new Properties();
        Mockito.when(project.getProperties()).thenReturn(projectProperties);

        final InspectContainerMojo subject = new InspectContainerMojo();
        ReflectionTestUtils.setField(subject, "project", project);
        ReflectionTestUtils.setField(subject, "containerId", "containerId");
        ReflectionTestUtils.setField(subject, "containerNameProperty", "containerNameProperty");
        ReflectionTestUtils.setField(subject, "hostnameProperty", "hostnameProperty");
        ReflectionTestUtils.setField(subject, "portPropertiesAsString", new String[]{"80/tcp=test.port.80", "81/tcp=test.port.81"});
        final Properties portProperties = new Properties();
        portProperties.setProperty("82/tcp", "test.port.82");
        portProperties.setProperty("83/tcp", "test.port.83");
        ReflectionTestUtils.setField(subject, "portProperties", portProperties);

        final DockerExecutable executable = Mockito.mock(DockerExecutable.class);
        subject.setInspectContainerExecutableCreator((settings) -> {
            Assert.assertNotNull(settings.getProjectProperties());
            Assert.assertEquals("containerId", settings.getContainerId());
            Assert.assertEquals("containerNameProperty", settings.getContainerNameProperty());
            Assert.assertEquals("hostnameProperty", settings.getHostnameProperty());
            Assert.assertEquals(4, settings.getPortProperties().size());
            Assert.assertEquals("test.port.80", settings.getPortProperties().getProperty("80/tcp"));
            Assert.assertEquals("test.port.81", settings.getPortProperties().getProperty("81/tcp"));
            Assert.assertEquals("test.port.82", settings.getPortProperties().getProperty("82/tcp"));
            Assert.assertEquals("test.port.83", settings.getPortProperties().getProperty("83/tcp"));

            return executable;
        });
        subject.execute();

        Mockito.verify(executable).execute();
        Mockito.verifyNoMoreInteractions(executable);
    }
}

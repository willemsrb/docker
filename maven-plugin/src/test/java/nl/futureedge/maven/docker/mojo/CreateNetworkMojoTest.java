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

public class CreateNetworkMojoTest {


    @Test
    public void test() throws MojoExecutionException, MojoFailureException, DockerException {
        final MavenProject project = Mockito.mock(MavenProject.class);
        final Properties projectProperties = new Properties();
        Mockito.when(project.getProperties()).thenReturn(projectProperties);

        final CreateNetworkMojo subject = new CreateNetworkMojo();
        ReflectionTestUtils.setField(subject, "project", project);
        ReflectionTestUtils.setField(subject, "networkOptions", "networkOptions");
        ReflectionTestUtils.setField(subject, "networkName", "networkName");
        ReflectionTestUtils.setField(subject, "networkNameProperty", "networkNameProperty");

        final DockerExecutable executable = Mockito.mock(DockerExecutable.class);
        subject.setCreateNetworkExecutableCreator((settings) -> {
            Assert.assertEquals("networkOptions", settings.getNetworkOptions());
            Assert.assertEquals("networkName", settings.getNetworkName());
            return executable;
        });
        subject.execute();

        Mockito.verify(executable).execute();
        Mockito.verifyNoMoreInteractions(executable);

        Assert.assertEquals(1, projectProperties.size());
        Assert.assertEquals("networkName", projectProperties.getProperty("networkNameProperty"));
    }

    @Test
    public void testGenerateName() throws MojoExecutionException, MojoFailureException, DockerException {
        final MavenProject project = Mockito.mock(MavenProject.class);
        final Properties projectProperties = new Properties();
        Mockito.when(project.getProperties()).thenReturn(projectProperties);

        final CreateNetworkMojo subject = new CreateNetworkMojo();
        ReflectionTestUtils.setField(subject, "project", project);
        ReflectionTestUtils.setField(subject, "networkNameProperty", "networkNameProperty");

        final DockerExecutable executable = Mockito.mock(DockerExecutable.class);
        subject.setCreateNetworkExecutableCreator((settings) -> {
            Assert.assertNotNull(settings.getNetworkName());
            return executable;
        });
        subject.execute();

        Mockito.verify(executable).execute();
        Mockito.verifyNoMoreInteractions(executable);

        Assert.assertEquals(1, projectProperties.size());
        Assert.assertNotNull(projectProperties.getProperty("networkNameProperty"));
    }
}

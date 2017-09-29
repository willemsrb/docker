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

public class RunConfigurationMojoTest {

    @Test
    public void test() throws MojoExecutionException, MojoFailureException, DockerException {
        final MavenProject project = Mockito.mock(MavenProject.class);
        final Properties projectProperties = new Properties();
        Mockito.when(project.getProperties()).thenReturn(projectProperties);

        final RunConfigurationMojo subject = new RunConfigurationMojo();
        ReflectionTestUtils.setField(subject, "project", project);
        ReflectionTestUtils.setField(subject, "configurationName", "configurationName");
        ReflectionTestUtils.setField(subject, "additionalRunOptions", "additionalRunOptions");
        ReflectionTestUtils.setField(subject, "command", "command");
        ReflectionTestUtils.setField(subject, "networkName", "networkName");
        ReflectionTestUtils.setField(subject, "randomPorts", true);
        ReflectionTestUtils.setField(subject, "skipDependencies", false);

        final DockerExecutable executable = Mockito.mock(DockerExecutable.class);
        subject.setRunConfigurationExecutableCreator((settings) -> {
            Assert.assertNotNull(settings.getProjectProperties());
            Assert.assertEquals("configurationName", settings.getConfigurationName());
            Assert.assertEquals("additionalRunOptions", settings.getAdditionalRunOptions());
            Assert.assertEquals("command", settings.getCommand());
            Assert.assertEquals("networkName", settings.getNetworkName());
            Assert.assertEquals(true, settings.isRandomPorts());
            Assert.assertEquals(false, settings.isSkipDependencies());

            return executable;
        });
        subject.execute();

        Mockito.verify(executable).execute();
        Mockito.verifyNoMoreInteractions(executable);
    }
}

package nl.futureedge.maven.docker.mojo;

import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.support.DockerExecutable;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class RemoveVolumesMojoTest {

    @Test
    public void test() throws MojoExecutionException, MojoFailureException, DockerException {
        final RemoveVolumesMojo subject = new RemoveVolumesMojo();
        ReflectionTestUtils.setField(subject, "filter", "filter");

        final DockerExecutable executable = Mockito.mock(DockerExecutable.class);
        subject.setRemoveVolumesExecutableCreator((settings) -> {
            Assert.assertEquals("filter", settings.getFilter());
            return executable;
        });
        subject.execute();

        Mockito.verify(executable).execute();
        Mockito.verifyNoMoreInteractions(executable);
    }
}

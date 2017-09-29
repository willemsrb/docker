package nl.futureedge.maven.docker.mojo;

import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.support.DockerExecutable;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class CommandMojoTest {

    @Test
    public void test() throws MojoExecutionException, MojoFailureException, DockerException {
        final CommandMojo subject = new CommandMojo();
        ReflectionTestUtils.setField(subject, "command", "command");

        final DockerExecutable executable = Mockito.mock(DockerExecutable.class);
        subject.setCommandExecutableCreator((settings) -> {
            Assert.assertEquals("command", settings.getCommand());
            return executable;
        });
        subject.execute();

        Mockito.verify(executable).execute();
        Mockito.verifyNoMoreInteractions(executable);
    }
}

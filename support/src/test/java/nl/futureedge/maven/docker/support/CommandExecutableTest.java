package nl.futureedge.maven.docker.support;

import java.util.List;
import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.executor.DockerExecutor;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class CommandExecutableTest {

    @Test
    public void test() throws DockerException {
        final DockerExecutor executor = Mockito.mock(DockerExecutor.class);

        final CommandSettings settings = CommandSettings.builder()
                .setCommand("theCommand to execute")
                .build();

        final CommandExecutable subject = new CommandExecutable(settings);
        subject.setFactory(new StaticDockerExecutorFactory(executor));
        subject.execute();

        final ArgumentCaptor<List<String>> commandCaptor = ArgumentCaptor.forClass(List.class);

        Mockito.verify(executor).execute(commandCaptor.capture(), Mockito.eq(true), Mockito.eq(false));
        final List<String> command = commandCaptor.getValue();
        Assert.assertNotNull(command);
        Assert.assertEquals(3, command.size());
        Assert.assertEquals("theCommand", command.get(0));
        Assert.assertEquals("to", command.get(1));
        Assert.assertEquals("execute", command.get(2));

        Mockito.verifyNoMoreInteractions(executor);
    }
}

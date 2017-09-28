package nl.futureedge.maven.docker.support;

import java.util.List;
import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.executor.DockerExecutor;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class CreateNetworkExecutableTest {

    @Test
    public void test() throws DockerException {
        final DockerExecutor executor = Mockito.mock(DockerExecutor.class);

        final CreateNetworkSettings settings = CreateNetworkSettings.builder()
                .setNetworkOptions(" --some option")
                .setNetworkName("netName")
                .build();

        final CreateNetworkExecutable subject = new CreateNetworkExecutable(settings);
        subject.setFactory(new StaticDockerExecutorFactory(executor));
        subject.execute();

        final ArgumentCaptor<List<String>> commandCaptor = ArgumentCaptor.forClass(List.class);

        Mockito.verify(executor).execute(commandCaptor.capture(), Mockito.eq(true), Mockito.eq(false));
        final List<String> command = commandCaptor.getValue();
        Assert.assertNotNull(command);
        Assert.assertEquals(5, command.size());
        Assert.assertEquals("network", command.get(0));
        Assert.assertEquals("create", command.get(1));
        Assert.assertEquals("--some", command.get(2));
        Assert.assertEquals("option", command.get(3));
        Assert.assertEquals("netName", command.get(4));

        Mockito.verifyNoMoreInteractions(executor);    }
}

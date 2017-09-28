package nl.futureedge.maven.docker.support;

import java.util.Arrays;
import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.executor.DockerExecutor;
import org.junit.Test;
import org.mockito.Mockito;

public class RemoveNetworkExecutableTest {

    @Test
    public void test() throws DockerException {
        final DockerExecutor executor = Mockito.mock(DockerExecutor.class);

        final RemoveNetworkSettings settings = RemoveNetworkSettings.builder()
                .setNetworkName("TheNetwork")
                .build();

        final RemoveNetworkExecutable subject = new RemoveNetworkExecutable(settings);
        subject.setFactory(new StaticDockerExecutorFactory(executor));
        subject.execute();

        Mockito.verify(executor).execute(Arrays.asList("network", "rm", "TheNetwork"), false, false);

        Mockito.verifyNoMoreInteractions(executor);
    }
}

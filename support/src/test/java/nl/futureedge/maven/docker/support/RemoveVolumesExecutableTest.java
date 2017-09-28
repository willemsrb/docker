package nl.futureedge.maven.docker.support;

import java.util.Arrays;
import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.executor.DockerExecutor;
import org.junit.Test;
import org.mockito.Mockito;

public class RemoveVolumesExecutableTest {

    @Test
    public void test() throws DockerException {
        final DockerExecutor executor = Mockito.mock(DockerExecutor.class);
        Mockito.when(executor.execute(Arrays.asList("volume", "ls", "-q", "--filter", "network=something"), false, true))
                .thenReturn(Arrays.asList("itemOne", "itemTwo"));

        final RemoveVolumesSettings settings = RemoveVolumesSettings.builder()
                .setFilter("network=something")
                .build();

        final RemoveVolumesExecutable subject = new RemoveVolumesExecutable(settings);
        subject.setFactory(new StaticDockerExecutorFactory(executor));
        subject.execute();

        Mockito.verify(executor).execute(Arrays.asList("volume", "ls", "-q", "--filter", "network=something"), false, true);
        Mockito.verify(executor).execute(Arrays.asList("volume", "rm", "itemOne"), false, false);
        Mockito.verify(executor).execute(Arrays.asList("volume", "rm", "itemTwo"), false, false);

        Mockito.verifyNoMoreInteractions(executor);
    }
}

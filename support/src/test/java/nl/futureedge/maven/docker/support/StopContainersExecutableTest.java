package nl.futureedge.maven.docker.support;

import java.util.Arrays;
import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.executor.DockerExecutor;
import org.junit.Test;
import org.mockito.Mockito;

public class StopContainersExecutableTest {

    @Test
    public void test() throws DockerException {
        final DockerExecutor executor = Mockito.mock(DockerExecutor.class);
        Mockito.when(executor.execute(Arrays.asList("ps", "-a", "-q", "--filter", "network=something", "--format", "{{.Names}}"), false, true))
                .thenReturn(Arrays.asList("itemOne", "itemTwo"));

        final StopContainersSettings settings = StopContainersSettings.builder()
                .setFilter("network=something")
                .build();

        final StopContainersExecutable subject = new StopContainersExecutable(settings);
        subject.setFactory(new StaticDockerExecutorFactory(executor));
        subject.execute();

        Mockito.verify(executor).execute(Arrays.asList("ps", "-a", "-q", "--filter", "network=something", "--format", "{{.Names}}"), false, true);
        Mockito.verify(executor).execute(Arrays.asList("stop", "itemOne"), false, false);
        Mockito.verify(executor).execute(Arrays.asList("stop", "itemTwo"), false, false);

        Mockito.verifyNoMoreInteractions(executor);
    }
}

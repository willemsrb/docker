package nl.futureedge.maven.docker.support;

import java.util.Arrays;
import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.executor.DockerExecutor;
import org.junit.Test;
import org.mockito.Mockito;

public class RemoveContainersExecutableTest {

    @Test
    public void test() throws DockerException {
        final DockerExecutor executor = Mockito.mock(DockerExecutor.class);
        Mockito.when(executor.execute(Arrays.asList("ps", "-a", "-q", "--filter", "network=something", "--format", "{{.Names}}"), false, true))
                .thenReturn(Arrays.asList("itemOne", "itemTwo"));

        final RemoveContainersSettings settings = RemoveContainersSettings.builder()
                .setFilter("network=something")
                .build();

        final RemoveContainersExecutable subject = new RemoveContainersExecutable(settings);
        subject.setFactory(new StaticDockerExecutorFactory(executor));
        subject.execute();

        Mockito.verify(executor).execute(Arrays.asList("ps", "-a", "-q", "--filter", "network=something", "--format", "{{.Names}}"), false, true);
        Mockito.verify(executor).execute(Arrays.asList("rm", "-vf", "itemOne"), false, false);
        Mockito.verify(executor).execute(Arrays.asList("rm", "-vf", "itemTwo"), false, false);

        Mockito.verifyNoMoreInteractions(executor);
    }
}

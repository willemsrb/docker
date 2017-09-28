package nl.futureedge.maven.docker.support;

import java.util.Arrays;
import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.executor.DockerExecutor;
import org.junit.Test;
import org.mockito.Mockito;

public class RemoveImagesExecutableTest {

    @Test
    public void test() throws DockerException {
        final DockerExecutor executor = Mockito.mock(DockerExecutor.class);
        Mockito.when(executor.execute(Arrays.asList("images", "-q", "--filter", "network=something"), false, true))
                .thenReturn(Arrays.asList("itemOne", "itemTwo"));

        final RemoveImagesSettings settings = RemoveImagesSettings.builder()
                .setFilter("network=something")
                .build();

        final RemoveImagesExecutable subject = new RemoveImagesExecutable(settings);
        subject.setFactory(new StaticDockerExecutorFactory(executor));
        subject.execute();

        Mockito.verify(executor).execute(Arrays.asList("images", "-q", "--filter", "network=something"), false, true);
        Mockito.verify(executor).execute(Arrays.asList("rmi", "-f", "itemOne"), false, false);
        Mockito.verify(executor).execute(Arrays.asList("rmi", "-f", "itemTwo"), false, false);

        Mockito.verifyNoMoreInteractions(executor);
    }
}

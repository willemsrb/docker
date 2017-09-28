package nl.futureedge.maven.docker.support;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.exception.DockerExecutionException;
import nl.futureedge.maven.docker.executor.DockerExecutor;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

public class RunExecutableTest {

    @Test
    public void testDaemon() throws DockerException {
        final DockerExecutor executor = Mockito.mock(DockerExecutor.class);
        Mockito.when(executor.execute(Arrays.asList("run", "--run", "options", "-d", "localhost:5000/my/image:latest",
                "theCommand", "to", "execute"), false, true)).thenReturn(Arrays.asList("TheContainerId"));
        final Properties projectProperties = new Properties();

        final RunSettings settings = RunSettings.builder()
                .setProjectProperties(projectProperties)
                .setRunOptions("--run options")
                .setDaemon(true)
                .setImage("localhost:5000/my/image:latest")
                .setCommand("theCommand to execute")
                .setContainerIdProperty("container.id")
                .build();

        final RunExecutable subject = new RunExecutable(settings);
        subject.setFactory(new StaticDockerExecutorFactory(executor));
        subject.execute();

        Mockito.verify(executor).execute(Arrays.asList("run", "--run", "options", "-d", "localhost:5000/my/image:latest",
                "theCommand", "to", "execute"), false, true);
        Mockito.verifyNoMoreInteractions(executor);

        Assert.assertEquals("TheContainerId", subject.getContainerId());
        Assert.assertEquals("TheContainerId", projectProperties.getProperty("container.id"));
    }

    @Test
    public void testExecutable() throws DockerException {
        final DockerExecutor executor = Mockito.mock(DockerExecutor.class);
        Mockito.when(executor.execute(Mockito.anyList(), Mockito.eq(true), Mockito.eq(false))).then((invocation) -> fillCidFile(invocation));
        final Properties projectProperties = new Properties();

        final RunSettings settings = RunSettings.builder()
                .setProjectProperties(projectProperties)
                .setRunOptions("--run options")
                .setDaemon(false)
                .setImage("localhost:5000/my/image:latest")
                .setCommand("theCommand to execute")
                .setContainerIdProperty("container.id")
                .build();

        final RunExecutable subject = new RunExecutable(settings);
        subject.setFactory(new StaticDockerExecutorFactory(executor));
        subject.execute();

        Mockito.verify(executor).execute(Mockito.anyList(), Mockito.eq(true), Mockito.eq(false));
        Mockito.verifyNoMoreInteractions(executor);

        Assert.assertEquals("TheCidFileContainerId", subject.getContainerId());
        Assert.assertEquals("TheCidFileContainerId", projectProperties.getProperty("container.id"));
    }


    @Test
    public void testCidfileFailure() throws DockerException {
        final DockerExecutor executor = Mockito.mock(DockerExecutor.class);
        Mockito.when(executor.execute(Mockito.anyList(), Mockito.eq(true), Mockito.eq(false))).thenReturn(Arrays.asList("Any", "Random", "Output"));
        final Properties projectProperties = new Properties();

        final RunSettings settings = RunSettings.builder()
                .setProjectProperties(projectProperties)
                .setRunOptions("--run options")
                .setDaemon(false)
                .setImage("localhost:5000/my/image:latest")
                .setCommand("theCommand to execute")
                .setContainerIdProperty("container.id")
                .build();

        final RunExecutable subject = new RunExecutable(settings);
        subject.setFactory(new StaticDockerExecutorFactory(executor));
        try {
            subject.execute();
            Assert.fail();
        } catch (DockerExecutionException e) {
            // Expected
        }

        Mockito.verify(executor).execute(Mockito.anyList(), Mockito.eq(true), Mockito.eq(false));
        Mockito.verifyNoMoreInteractions(executor);

        Assert.assertEquals(null, subject.getContainerId());
    }


    private List<String> fillCidFile(InvocationOnMock invocation) throws IOException {
        List<String> arguments = invocation.getArgument(0);
        Assert.assertEquals(9, arguments.size());
        Assert.assertEquals("run", arguments.get(0));
        Assert.assertEquals("--run", arguments.get(1));
        Assert.assertEquals("options", arguments.get(2));
        Assert.assertEquals("--cidfile", arguments.get(3));
        Assert.assertEquals("localhost:5000/my/image:latest", arguments.get(5));
        Assert.assertEquals("theCommand", arguments.get(6));
        Assert.assertEquals("to", arguments.get(7));
        Assert.assertEquals("execute", arguments.get(8));

        String cidFileName = arguments.get(4);
        try (PrintWriter writer = new PrintWriter(new FileWriter(cidFileName))) {
            writer.println("TheCidFileContainerId");
        }

        return Arrays.asList("Any", "Random", "Output");
    }
}

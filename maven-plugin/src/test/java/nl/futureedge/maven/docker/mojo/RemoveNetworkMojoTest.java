package nl.futureedge.maven.docker.mojo;

import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.support.DockerExecutable;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class RemoveNetworkMojoTest {

    @Test
    public void testWithStop() throws MojoExecutionException, MojoFailureException, DockerException {
        final RemoveNetworkMojo subject = new RemoveNetworkMojo();
        ReflectionTestUtils.setField(subject, "stopContainers", true);
        ReflectionTestUtils.setField(subject, "networkName", "my-network");

        final DockerExecutable stopContainersExecutable = Mockito.mock(DockerExecutable.class);
        subject.setStopContainersExecutableCreator((settings) -> {
            Assert.assertEquals("network=my-network", settings.getFilter());
            return stopContainersExecutable;
        });
        final DockerExecutable removeContainersExecutable = Mockito.mock(DockerExecutable.class);
        subject.setRemoveContainersExecutableCreator((settings) -> {
            Assert.assertEquals("network=my-network", settings.getFilter());
            return removeContainersExecutable;
        });
        final DockerExecutable removeNetworkExecutable = Mockito.mock(DockerExecutable.class);
        subject.setRemoveNetworkExecutableCreator((settings) -> {
            Assert.assertEquals("my-network", settings.getNetworkName());
            return removeNetworkExecutable;
        });
        subject.execute();

        Mockito.verify(stopContainersExecutable).execute();
        Mockito.verify(removeNetworkExecutable).execute();
        Mockito.verifyNoMoreInteractions(stopContainersExecutable, removeContainersExecutable, removeNetworkExecutable);
    }


    @Test
    public void testWithRemove() throws MojoExecutionException, MojoFailureException, DockerException {
        final RemoveNetworkMojo subject = new RemoveNetworkMojo();
        ReflectionTestUtils.setField(subject, "removeContainers", true);
        ReflectionTestUtils.setField(subject, "networkName", "my-network");

        final DockerExecutable stopContainersExecutable = Mockito.mock(DockerExecutable.class);
        subject.setStopContainersExecutableCreator((settings) -> {
            Assert.assertEquals("network=my-network", settings.getFilter());
            return stopContainersExecutable;
        });
        final DockerExecutable removeContainersExecutable = Mockito.mock(DockerExecutable.class);
        subject.setRemoveContainersExecutableCreator((settings) -> {
            Assert.assertEquals("network=my-network", settings.getFilter());
            return removeContainersExecutable;
        });
        final DockerExecutable removeNetworkExecutable = Mockito.mock(DockerExecutable.class);
        subject.setRemoveNetworkExecutableCreator((settings) -> {
            Assert.assertEquals("my-network", settings.getNetworkName());
            return removeNetworkExecutable;
        });
        subject.execute();

        Mockito.verify(removeContainersExecutable).execute();
        Mockito.verify(removeNetworkExecutable).execute();
        Mockito.verifyNoMoreInteractions(stopContainersExecutable, removeContainersExecutable, removeNetworkExecutable);
    }
}

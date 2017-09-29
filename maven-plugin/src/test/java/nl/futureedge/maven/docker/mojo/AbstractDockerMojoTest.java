package nl.futureedge.maven.docker.mojo;

import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.exception.DockerExecutionException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.Assert;
import org.junit.Test;

public class AbstractDockerMojoTest {

    @Test
    public void test() throws MojoExecutionException, MojoFailureException {
        TestDockerMojo subject = new TestDockerMojo();
        Assert.assertEquals(null, subject.getDockerOptions());
        Assert.assertEquals(false, subject.isIgnoreFailures());
        Assert.assertEquals(false, subject.isSkip());
        Assert.assertNotNull(subject.getDebugLogger());
        Assert.assertNotNull(subject.getInfoLogger());
        Assert.assertNotNull(subject.getWarnLogger());

        ReflectionTestUtils.setField(subject, "dockerOptions", "dockerOptions");
        ReflectionTestUtils.setField(subject, "ignoreFailures", true);
        ReflectionTestUtils.setField(subject, "skip", true);

        Assert.assertEquals("dockerOptions", subject.getDockerOptions());
        Assert.assertEquals(true, subject.isIgnoreFailures());
        Assert.assertEquals(true, subject.isSkip());

        Assert.assertEquals(false, subject.internalExecuted);
        subject.execute();
        Assert.assertEquals(false, subject.internalExecuted);
        ReflectionTestUtils.setField(subject, "skip", false);
        subject.execute();
        Assert.assertEquals(true, subject.internalExecuted);
        try {
            subject.execute();
            Assert.fail();
        } catch (MojoExecutionException e) {
            // Expected
        }
    }

    private static final class TestDockerMojo extends AbstractDockerMojo {

        boolean internalExecuted;

        @Override
        protected void executeInternal() throws DockerException {
            if (internalExecuted) {
                throw new DockerExecutionException("Already executed");
            } else {
                internalExecuted = true;
            }
        }
    }
}

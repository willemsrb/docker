package nl.futureedge.maven.docker.mojo.properties;

import java.util.Properties;
import nl.futureedge.maven.docker.mojo.ReflectionTestUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class AbstractPropertiesMojoTest {

    @Test
    public void test() throws MojoFailureException, MojoExecutionException {
        final MavenProject project = Mockito.mock(MavenProject.class);
        final Properties projectProperties = new Properties();
        projectProperties.setProperty("prefix.suffix", "value");
        projectProperties.setProperty("prefix..suffix", "value");
        projectProperties.setProperty("prefix.name.suffix", "value");
        projectProperties.setProperty("preflix.name.suffix", "value");
        projectProperties.setProperty("prefix.name.sufflix", "value");

        projectProperties.setProperty("prefix-xx", "value");
        projectProperties.setProperty("prefix-xxx", "value");
        projectProperties.setProperty("prefix-xxxx", "value");

        projectProperties.setProperty("path\\path", "value");
        projectProperties.setProperty("path\\\\path", "value");
        Mockito.when(project.getProperties()).thenReturn(projectProperties);

        final EchoPropertiesMojo subject = new EchoPropertiesMojo();
        ReflectionTestUtils.setField(subject, "project", project);
        ReflectionTestUtils.setField(subject, "keys", new String[]{
                "prefix.*.suffix",
                "prefix-???",
                "path\\path"
        });

        final Properties result = subject.getProperties();
        Assert.assertEquals(4, result.size());
        Assert.assertTrue(result.containsKey("prefix..suffix"));
        Assert.assertTrue(result.containsKey("prefix.name.suffix"));
        Assert.assertTrue(result.containsKey("prefix-xxx"));
        Assert.assertTrue(result.containsKey("path\\path"));
    }

    @Test
    public void testNoKeys() throws MojoFailureException, MojoExecutionException {
        final MavenProject project = Mockito.mock(MavenProject.class);
        final Properties projectProperties = new Properties();
        projectProperties.setProperty("prop-001", "value");
        projectProperties.setProperty("prop-002", "value");
        projectProperties.setProperty("prop-003", "value");
        Mockito.when(project.getProperties()).thenReturn(projectProperties);

        final EchoPropertiesMojo subject = new EchoPropertiesMojo();
        ReflectionTestUtils.setField(subject, "project", project);

        final Properties result = subject.getProperties();
        Assert.assertEquals(3, result.size());
        Assert.assertTrue(result.containsKey("prop-001"));
        Assert.assertTrue(result.containsKey("prop-002"));
        Assert.assertTrue(result.containsKey("prop-003"));
    }

}

package nl.futureedge.maven.docker.mojo.properties;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.util.Properties;
import nl.futureedge.maven.docker.mojo.ReflectionTestUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class WritePropertiesMojoTest {

    @Test
    public void test() throws IOException, MojoFailureException, MojoExecutionException {
        final MavenProject project = Mockito.mock(MavenProject.class);
        final Properties projectProperties = new Properties();
        projectProperties.setProperty("filtered-001", "value");
        projectProperties.setProperty("prop-001", "value");
        projectProperties.setProperty("prop-002", "value");
        projectProperties.setProperty("prop-overlap-002", "overlap-002");
        Mockito.when(project.getProperties()).thenReturn(projectProperties);

        final File mergeFile001 = Files.createTempFile("to-be-merged-001-", ".properties").toFile();
        mergeFile001.deleteOnExit();
        try (final Writer writer = new FileWriter(mergeFile001)) {
            final Properties properties = new Properties();
            properties.setProperty("prop-001-001", "value");
            properties.setProperty("prop-001-002", "value");
            properties.setProperty("prop-overlap-001", "overlap-001-001");
            properties.setProperty("prop-overlap-002", "overlap-001-002");
            properties.setProperty("notfiltered-001", "value");
            properties.store(writer, "");
        }

        final File mergeFile002 = Files.createTempFile("to-be-merged-002-", ".properties").toFile();
        mergeFile002.deleteOnExit();
        try (final Writer writer = new FileWriter(mergeFile002)) {
            final Properties properties = new Properties();
            properties.setProperty("prop-002-001", "value");
            properties.setProperty("prop-002-002", "value");
            properties.setProperty("prop-overlap-001", "overlap-002-001");
            properties.setProperty("notfiltered-002", "value");
            properties.store(writer, "");
        }

        final File targetFile = Files.createTempFile("target-", ".properties").toFile();
        targetFile.deleteOnExit();

        final WritePropertiesMojo subject = new WritePropertiesMojo();
        ReflectionTestUtils.setField(subject, "project", project);
        ReflectionTestUtils.setField(subject, "mergePropertyFiles", new File[]{mergeFile001, mergeFile002});
        ReflectionTestUtils.setField(subject, "target", targetFile);
        ReflectionTestUtils.setField(subject, "keys", new String[]{"prop-*"});
        subject.execute();

        final Properties result = new Properties();
        try (final Reader reader = new FileReader(targetFile)) {
            result.load(reader);
        }

        Assert.assertEquals(10, result.size());
        Assert.assertEquals("value", result.getProperty("prop-001-001"));
        Assert.assertEquals("value", result.getProperty("prop-001-002"));
        Assert.assertEquals("value", result.getProperty("prop-002-001"));
        Assert.assertEquals("value", result.getProperty("prop-002-002"));
        Assert.assertEquals("value", result.getProperty("prop-001"));
        Assert.assertEquals("value", result.getProperty("prop-002"));
        Assert.assertEquals("overlap-002-001", result.getProperty("prop-overlap-001"));
        Assert.assertEquals("overlap-001-002", result.getProperty("prop-overlap-002"));
        Assert.assertEquals("value", result.getProperty("notfiltered-001"));
        Assert.assertEquals("value", result.getProperty("notfiltered-002"));
    }
}

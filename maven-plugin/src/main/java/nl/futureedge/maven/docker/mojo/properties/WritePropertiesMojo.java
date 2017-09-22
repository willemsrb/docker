package nl.futureedge.maven.docker.mojo.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "write-properties", requiresProject = false)
public final class WritePropertiesMojo extends AbstractPropertiesMojo {

    @Parameter(name = "mergePropertyFiles", property = "properties.merge")
    private File[] mergePropertyFiles;

    /**
     * Target property file (will be overwritten if it exists).
     */
    @Parameter(name = "target", property = "properties.target", defaultValue = "${project.build.directory}/extracted.properties", required = true)
    private File target;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        // Filtered properties
        final Properties result = getProperties();

        // Merge
        if (mergePropertyFiles != null) {
            for (final File mergePropertyFile : mergePropertyFiles) {
                try (final FileInputStream is = new FileInputStream(mergePropertyFile)) {
                    result.load(is);
                } catch (IOException e) {
                    throw new MojoFailureException("Could not load property file to merge", e);
                }
            }
        }

        // Write
        try (final OutputStream out = new FileOutputStream(target)) {
            result.store(out, null);
        } catch (IOException e) {
            throw new MojoFailureException("Could not write property file", e);
        }
    }
}

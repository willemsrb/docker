package nl.futureedge.maven.docker.mojo.properties;

import java.util.Properties;
import java.util.regex.Pattern;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

public abstract class AbstractPropertiesMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    /**
     * Glob expressions containing keys (empty for all).
     */
    @Parameter(name = "keys", property = "properties.keys")
    private String[] keys;

    protected final Properties getProperties() {
        final Properties properties = project.getProperties();

        // Get relevant properties
        final Properties result;
        if (keys != null && keys.length > 0) {
            final Pattern keysPattern = createKeysPattern();
            result = new Properties();
            for (final String key : properties.stringPropertyNames()) {
                if (keysPattern.matcher(key).matches()) {
                    result.setProperty(key, properties.getProperty(key, ""));
                }

            }
        } else {
            result = properties;
        }

        return result;
    }

    private Pattern createKeysPattern() {
        final StringBuilder pattern = new StringBuilder();
        pattern.append("^");

        for (final String key : keys) {
            if (pattern.length() != 1) {
                pattern.append("|");
            }

            pattern.append("(").append(globToRegex(key)).append(")");
        }

        pattern.append("$");

        return Pattern.compile(pattern.toString());
    }

    private static String globToRegex(final String glob) {
        final StringBuilder out = new StringBuilder();
        for (int i = 0; i < glob.length(); ++i) {
            final char c = glob.charAt(i);
            switch (c) {
                case '*':
                    out.append(".*");
                    break;
                case '?':
                    out.append('.');
                    break;
                case '.':
                    out.append("\\.");
                    break;
                case '\\':
                    out.append("\\\\");
                    break;
                default:
                    out.append(c);
            }
        }

        return out.toString();
    }
}

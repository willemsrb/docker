package nl.futureedge.maven.docker.executor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class Docker {

    private static final String REGISTRY_SEPARATOR = "/";
    private static final String TAG_SEPARATOR = ":";

    private static final String ALL_WHITESPACE = "\\s+";
    private static final String SINGLE_SPACE = " ";


    public static String getImage(final String registry, final String name, final String tag) {
        if (name == null || "".equals(name.trim())) {
            return name;
        }

        final StringBuilder image = new StringBuilder();
        if (registry != null && !"".equals(registry.trim())) {
            image.append(registry);
            if (!registry.endsWith(REGISTRY_SEPARATOR)) {
                image.append(REGISTRY_SEPARATOR);
            }
        }

        image.append(name);

        if (tag != null && !"".equals(tag.trim())) {
            image.append(TAG_SEPARATOR);
            image.append(tag);
        }

        return image.toString();
    }

    public static List<String> splitOptions(final String options) {
        if ((options != null) && !"".equals(options.trim())) {
            final String normalized = options.replaceAll(ALL_WHITESPACE, SINGLE_SPACE).trim();
            return Arrays.asList(normalized.split(SINGLE_SPACE));
        } else {
            return Collections.emptyList();
        }
    }
}

package nl.futureedge.maven.docker.executor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class Docker {

    public static String getImage(final String registry, final String name, final String tag) {
        if (name == null || "".equals(name.trim())) {
            return name;
        }

        final StringBuilder image = new StringBuilder();
        if (registry != null && !"".equals(registry.trim())) {
            image.append(registry);
            if (!registry.endsWith("/")) {
                image.append('/');
            }
        }

        image.append(name);

        if (tag != null && !"".equals(tag.trim())) {
            image.append(':');
            image.append(tag);
        }

        return image.toString();
    }

    public static List<String> splitOptions(final String options) {
        if ((options != null) && !"".equals(options)) {
            return Arrays.asList(options.trim().split(" "));
        } else {
            return Collections.emptyList();
        }
    }
}

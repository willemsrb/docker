package nl.futureedge.maven.docker.support;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface DockerSettings {

    Consumer<String> getDebugLogger();

    Consumer<String> getInfoLogger();

    BiConsumer<String, Exception> getWarnLogger();

    String getDockerOptions();

    boolean isIgnoreFailures();
}

package nl.futureedge.maven.docker.support;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface DockerSettings {

    Consumer<String> getDebugLogger();

    Consumer<String> getInfoLogger();

    BiConsumer<String, Exception> getWarnLogger();

    String getDockerOptions();

    boolean isIgnoreFailures();

    static Builder builder() {
        return new Builder();
    }

    static void defaultDebug(final String message) {
        // Nothing
    }

    static void defaultInfo(final String message) {
        System.out.println(message);
    }

    static void defaultWarn(final String message, final Exception exception) {
        System.err.println(message);
        if (exception != null) {
            exception.printStackTrace(System.err);
        }
    }


    class Builder<T extends Builder> {

        private T builder;

        private Consumer<String> debugLogger = DockerSettings::defaultDebug;
        private Consumer<String> infoLogger = DockerSettings::defaultInfo;
        private BiConsumer<String, Exception> warnLogger = DockerSettings::defaultWarn;
        private String dockerOptions;
        private boolean ignoreFailures = false;

        public Builder() {
        }

        protected final void setBuilder(final T builder) {
            this.builder = builder;
        }

        public DockerSettings build() {
            return new DockerSettingsImpl(this);
        }

        public final T setDebugLogger(final Consumer<String> debugLogger) {
            this.debugLogger = debugLogger;
            return builder;
        }

        public final T setInfoLogger(final Consumer<String> infoLogger) {
            this.infoLogger = infoLogger;
            return builder;
        }

        public final T setWarnLogger(final BiConsumer<String, Exception> warnLogger) {
            this.warnLogger = warnLogger;
            return builder;
        }

        public final T setDockerOptions(final String dockerOptions) {
            this.dockerOptions = dockerOptions;
            return builder;
        }

        public final T setIgnoreFailures(final boolean ignoreFailures) {
            this.ignoreFailures = ignoreFailures;
            return builder;
        }
    }

    class DockerSettingsImpl implements DockerSettings {

        private final Consumer<String> debugLogger;
        private final Consumer<String> infoLogger;
        private final BiConsumer<String, Exception> warnLogger;
        private final String dockerOptions;
        private final boolean ignoreFailures;

        protected DockerSettingsImpl(final Builder builder) {
            this.debugLogger = builder.debugLogger;
            this.infoLogger = builder.infoLogger;
            this.warnLogger = builder.warnLogger;
            this.dockerOptions = builder.dockerOptions;
            this.ignoreFailures = builder.ignoreFailures;
        }

        @Override
        public Consumer<String> getDebugLogger() {
            return debugLogger;
        }

        @Override
        public Consumer<String> getInfoLogger() {
            return infoLogger;
        }

        @Override
        public BiConsumer<String, Exception> getWarnLogger() {
            return warnLogger;
        }

        @Override
        public String getDockerOptions() {
            return dockerOptions;
        }

        @Override
        public boolean isIgnoreFailures() {
            return ignoreFailures;
        }
    }
}

package nl.futureedge.maven.docker.support;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Settings.
 */
public interface DockerSettings {

    /**
     * @return logger for debug messages
     */
    Consumer<String> getDebugLogger();

    /**
     * @return logger for informational messages
     */
    Consumer<String> getInfoLogger();

    /**
     * @return logger for warning messages
     */
    BiConsumer<String, Exception> getWarnLogger();

    /**
     * @return docker options
     */
    String getDockerOptions();

    /**
     * @return should errors executing the docker command be ignore?
     */
    boolean isIgnoreFailures();

    /**
     * Default logging for debug messages (does nothing).
     * @param message message
     */
    static void defaultDebug(final String message) {
        // Nothing
    }

    /**
     * Default logging for informational messages (prints to System.out)
     * @param message message
     */
    static void defaultInfo(final String message) {
        System.out.println(message);
    }

    /**
     * Default logging for warning messages (prints to System.err)
     * @param message message
     */
    static void defaultWarn(final String message, final Exception exception) {
        System.err.println(message);
        if (exception != null) {
            exception.printStackTrace(System.err);
        }
    }

    /**
     * Builder.
     *
     * <ul>
     * <li>Default debuglogger = {@link #defaultDebug(String)}</li>
     * <li>Default infoLogger = {@link #defaultInfo(String)}</li>
     * <li>Default warnLogger = {@link #defaultWarn(String, Exception)}</li>
     * <li>Default ignoreFailures = false</li>
     * </ul>
     *
     * @param <T> builder type
     */
    public abstract class Builder<T extends Builder> {

        private T builder;

        private Consumer<String> debugLogger = DockerSettings::defaultDebug;
        private Consumer<String> infoLogger = DockerSettings::defaultInfo;
        private BiConsumer<String, Exception> warnLogger = DockerSettings::defaultWarn;
        private String dockerOptions;
        private boolean ignoreFailures = false;

        protected Builder() {
        }

        /**
         * Sets 'this' builder (subclasses need to call this in the constructor after calling super()).
         * @param builder 'this' builder
         */
        protected final void setBuilder(final T builder) {
            this.builder = builder;
        }

        /**
         * @return 'this' builder
         */
        protected final T getBuilder() {
            return builder;
        }

        /**
         * Build the settings.
         * @return settings
         */
        public abstract DockerSettings build();

        /**
         * Set the logger for debug messages.
         * @param debugLogger logger for debug messages
         * @return this builder
         */
        public final T setDebugLogger(final Consumer<String> debugLogger) {
            this.debugLogger = debugLogger;
            return builder;
        }

        /**
         * Set the logger for informational messages.
         * @param infoLogger logger for informational messages
         * @return this builder
         */
        public final T setInfoLogger(final Consumer<String> infoLogger) {
            this.infoLogger = infoLogger;
            return builder;
        }

        /**
         * Set the logger for warning messages.
         * @param warnLogger logger for warning messages
         * @return this builder
         */
        public final T setWarnLogger(final BiConsumer<String, Exception> warnLogger) {
            this.warnLogger = warnLogger;
            return builder;
        }

        /**
         * Set the docker options.
         * @param dockerOptions docker options
         * @return this builder
         */
        public final T setDockerOptions(final String dockerOptions) {
            this.dockerOptions = dockerOptions;
            return builder;
        }

        /**
         * Sets if errors executing the docker command should be ignore?
         * @param ignoreFailures true, if errors should be ignored
         * @return this builder
         */
        public final T setIgnoreFailures(final boolean ignoreFailures) {
            this.ignoreFailures = ignoreFailures;
            return builder;
        }
    }

    /**
     * Settings implementation.
     */
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

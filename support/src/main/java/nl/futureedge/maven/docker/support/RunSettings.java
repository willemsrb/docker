package nl.futureedge.maven.docker.support;

import java.util.Properties;

/**
 * Settings.
 */
public interface RunSettings extends DockerSettings {

    /**
     * @return project properties (to store inspected values)
     */
    Properties getProjectProperties();

    /**
     * @return run options
     */
    String getRunOptions();

    /**
     * @return should this container be run as a daemon?
     */
    boolean isDaemon();

    /**
     * @return image
     */
    String getImage();

    /**
     * @return command
     */
    String getCommand();

    /**
     * @return property to store container id
     */
    String getContainerIdProperty();

    /**
     * @return builder
     */
    static RunSettingsBuilder builder() {
        return new RunSettingsBuilder();
    }

    /**
     * Builder.
     *
     * <ul>
     *     <li>Default projectProperties = {@link System#getProperties()}</li>
     *     <li>Default daemon = true</li>
     * </ul>
     */
    final class RunSettingsBuilder extends DockerSettings.Builder<RunSettingsBuilder> {

        private Properties projectProperties = System.getProperties();
        private String runOptions;
        private boolean daemon = true;
        private String image;
        private String command;
        private String containerIdProperty;

        protected RunSettingsBuilder() {
            super();
            super.setBuilder(this);
        }

        @Override
        public RunSettings build() {
            return new RunSettingsImpl(this);
        }

        /**
         * Set project properties (to store inspected values)
         * @param projectProperties project properties
         * @return this builder
         */
        public RunSettingsBuilder setProjectProperties(final Properties projectProperties) {
            this.projectProperties = projectProperties;
            return this;
        }

        /**
         * Set run options.
         * @param runOptions run options
         * @return this builder
         */
        public RunSettingsBuilder setRunOptions(final String runOptions) {
            this.runOptions = runOptions;
            return this;
        }

        /**
         * Sets if this container should be run as a daemon?
         * @param daemon true, if this container should be run as a daemon
         * @return this builder
         */
        public RunSettingsBuilder setDaemon(final boolean daemon) {
            this.daemon = daemon;
            return this;
        }

        /**
         * Set image.
         * @param image image
         * @return this builder
         */
        public RunSettingsBuilder setImage(final String image) {
            this.image = image;
            return this;
        }

        /**
         * Set command.
         * @param command command
         * @return this builder
         */
        public RunSettingsBuilder setCommand(final String command) {
            this.command = command;
            return this;
        }

        /**
         * Set property to store container id.
         * @param containerIdProperty property to store container id
         * @return this builder
         */
        public RunSettingsBuilder setContainerIdProperty(final String containerIdProperty) {
            this.containerIdProperty = containerIdProperty;
            return this;
        }
    }

    /**
     * Settings implementation.
     */
    final class RunSettingsImpl extends DockerSettingsImpl implements RunSettings {

        private final Properties projectProperties;
        private final String runOptions;
        private final boolean daemon;
        private final String image;
        private final String command;
        private final String containerIdProperty;

        protected RunSettingsImpl(final RunSettingsBuilder builder) {
            super(builder);
            this.projectProperties = builder.projectProperties;
            this.runOptions = builder.runOptions;
            this.daemon = builder.daemon;
            this.image = builder.image;
            this.command = builder.command;
            this.containerIdProperty = builder.containerIdProperty;
        }

        @Override
        public Properties getProjectProperties() {
            return projectProperties;
        }

        @Override
        public String getRunOptions() {
            return runOptions;
        }

        @Override
        public boolean isDaemon() {
            return daemon;
        }

        @Override
        public String getImage() {
            return image;
        }

        @Override
        public String getCommand() {
            return command;
        }

        @Override
        public String getContainerIdProperty() {
            return containerIdProperty;
        }
    }
}


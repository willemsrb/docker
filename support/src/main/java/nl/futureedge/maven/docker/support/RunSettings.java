package nl.futureedge.maven.docker.support;

import java.util.Properties;

public interface RunSettings extends DockerSettings {

    Properties getProjectProperties();

    String getRunOptions();

    boolean isDaemon();

    String getImage();

    String getCommand();

    String getContainerIdProperty();

    static Builder builder() {
        return new Builder();
    }

    final class Builder extends DockerSettings.Builder<Builder> {

        private Properties projectProperties = System.getProperties();;
        private String runOptions;
        private boolean daemon = true;
        private String image;
        private String command;
        private String containerIdProperty;

        protected Builder() {
            super();
            super.setBuilder(this);
        }

        public RunSettings build() {
            return new RunSettingsImpl(this);
        }

        public Builder setProjectProperties(final Properties projectProperties) {
            this.projectProperties = projectProperties;
            return this;
        }

        public Builder setRunOptions(final String runOptions) {
            this.runOptions = runOptions;
            return this;
        }

        public Builder setDaemon(final boolean daemon) {
            this.daemon = daemon;
            return this;
        }

        public Builder setImage(final String image) {
            this.image = image;
            return this;
        }

        public Builder setCommand(final String command) {
            this.command = command;
            return this;
        }

        public Builder setContainerIdProperty(final String containerIdProperty) {
            this.containerIdProperty = containerIdProperty;
            return this;
        }
    }

    final class RunSettingsImpl extends DockerSettingsImpl implements RunSettings {

        private final Properties projectProperties;
        private final String runOptions;
        private final boolean daemon;
        private final String image;
        private final String command;
        private final String containerIdProperty;

        protected RunSettingsImpl(final RunSettings.Builder builder) {
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


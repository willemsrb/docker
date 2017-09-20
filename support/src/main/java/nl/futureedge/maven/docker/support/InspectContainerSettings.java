package nl.futureedge.maven.docker.support;

import java.util.Properties;

public interface InspectContainerSettings extends DockerSettings {

    Properties getProjectProperties();

    String getContainerId();

    String getContainerNameProperty();

    String getHostnameProperty();

    Properties getPortProperties();

    static Builder builder() {
        return new Builder();
    }

    final class Builder extends DockerSettings.Builder<Builder> {

        private Properties projectProperties = System.getProperties();
        private String containerId;
        private String containerNameProperty;
        private String hostnameProperty;
        private Properties portProperties;

        protected Builder() {
            super();
            super.setBuilder(this);
        }

        public InspectContainerSettings build() {
            return new InspectContainerSettings.InspectContainerSettingsImpl(this);
        }

        public Builder setProjectProperties(final Properties projectProperties) {
            this.projectProperties = projectProperties;
            return this;
        }

        public Builder setContainerId(final String containerId) {
            this.containerId = containerId;
            return this;
        }

        public Builder setContainerNameProperty(final String containerNameProperty) {
            this.containerNameProperty = containerNameProperty;
            return this;
        }

        public Builder setHostnameProperty(final String hostnameProperty) {
            this.hostnameProperty = hostnameProperty;
            return this;
        }

        public Builder setPortProperties(final Properties portProperties) {
            this.portProperties = portProperties;
            return this;
        }
    }

    final class InspectContainerSettingsImpl extends DockerSettingsImpl implements InspectContainerSettings {

        private final Properties projectProperties;
        private final String containerId;
        private final String containerNameProperty;
        private final String hostnameProperty;
        private final Properties portProperties;

        protected InspectContainerSettingsImpl(final InspectContainerSettings.Builder builder) {
            super(builder);
            this.projectProperties = builder.projectProperties;
            this.containerId = builder.containerId;
            this.containerNameProperty = builder.containerNameProperty;
            this.hostnameProperty = builder.hostnameProperty;
            this.portProperties = builder.portProperties;
        }

        @Override
        public Properties getProjectProperties() {
            return projectProperties;
        }

        @Override
        public String getContainerId() {
            return containerId;
        }

        @Override
        public String getContainerNameProperty() {
            return containerNameProperty;
        }

        @Override
        public String getHostnameProperty() {
            return hostnameProperty;
        }

        @Override
        public Properties getPortProperties() {
            return portProperties;
        }
    }
}


package nl.futureedge.maven.docker.support;

import java.util.Properties;

/**
 * Settings.
 */
public interface InspectContainerSettings extends DockerSettings {

    /**
     * @return project properties (to store inspected values)
     */
    Properties getProjectProperties();

    /**
     * @return container id (to inspect)
     */
    String getContainerId();

    /**
     * @return property name to store container name
     */
    String getContainerNameProperty();

    /**
     * @return property name to store host
     */
    String getHostnameProperty();

    /**
     * @return port properties (key contains port to inspect, value contains property name to store exposed port)
     */
    Properties getPortProperties();

    /**
     * @return builder
     */
    static Builder builder() {
        return new Builder();
    }

    /**
     * Builder.
     *
     * <ul>
     *     <li>Default projectProperties = {@link System#getProperties()}</li>
     * </ul>
     */
    public final class Builder extends DockerSettings.Builder<Builder> {

        private Properties projectProperties = System.getProperties();
        private String containerId;
        private String containerNameProperty;
        private String hostnameProperty;
        private Properties portProperties;

        protected Builder() {
            super();
            super.setBuilder(this);
        }

        @Override
        public InspectContainerSettings build() {
            return new InspectContainerSettings.InspectContainerSettingsImpl(this);
        }

        /**
         * Set project properties (to store inspected values)
         * @param projectProperties project properties
         * @return this builder
         */
        public Builder setProjectProperties(final Properties projectProperties) {
            this.projectProperties = projectProperties;
            return this;
        }

        /**
         * Set container id (to inspect).
         * @param containerId container id
         * @return this builder
         */
        public Builder setContainerId(final String containerId) {
            this.containerId = containerId;
            return this;
        }

        /**
         * Set property to store container name.
         * @param containerNameProperty property to store container name
         * @return this builder
         */
        public Builder setContainerNameProperty(final String containerNameProperty) {
            this.containerNameProperty = containerNameProperty;
            return this;
        }

        /**
         * Set property to store host name.
         * @param hostnameProperty property to store host name
         * @return this builder
         */
        public Builder setHostnameProperty(final String hostnameProperty) {
            this.hostnameProperty = hostnameProperty;
            return this;
        }

        /**
         * Set port properties (key contains port to inspect, value contains property name to store exposed port).
         * @param portProperties port properties
         * @return this builder
         */
        public Builder setPortProperties(final Properties portProperties) {
            this.portProperties = portProperties;
            return this;
        }
    }

    /**
     * Settings implementation.
     */
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


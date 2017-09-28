package nl.futureedge.maven.docker.support;

/**
 * Settings.
 */
public interface RemoveNetworkSettings extends DockerSettings {

    /**
     * Network name.
     * @return network name
     */
    String getNetworkName();

    /**
     * @return builder
     */
    static Builder builder() {
        return new Builder();
    }

    /**
     * Builder.
     */
    final class Builder extends DockerSettings.Builder<Builder> {

        private String networkName;

        protected Builder() {
            super();
            super.setBuilder(this);
        }

        @Override
        public RemoveNetworkSettings build() {
            return new RemoveNetworkSettingsImpl(this);
        }

        /**
         * Set network name.
         * @param networkName network name
         * @return this builder
         */
        public Builder setNetworkName(final String networkName) {
            this.networkName = networkName;
            return this;
        }
    }

    /**
     * Settings implementation.
     */
    final class RemoveNetworkSettingsImpl extends DockerSettingsImpl implements RemoveNetworkSettings {

        private final String networkName;

        protected RemoveNetworkSettingsImpl(final RemoveNetworkSettings.Builder builder) {
            super(builder);
            this.networkName = builder.networkName;
        }

        @Override
        public String getNetworkName() {
            return networkName;
        }
    }
}

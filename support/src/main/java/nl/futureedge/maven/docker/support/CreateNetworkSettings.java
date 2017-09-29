package nl.futureedge.maven.docker.support;

/**
 * Settings.
 */
public interface CreateNetworkSettings extends DockerSettings {

    /**
     * @return network options
     */
    String getNetworkOptions();

    /**
     * @return network name
     */
    String getNetworkName();

    /**
     * @return builder
     */
    static CreateNetworkBuilder builder() {
        return new CreateNetworkBuilder();
    }

    /**
     * Builder.
     */
    public final class CreateNetworkBuilder extends DockerSettings.Builder<CreateNetworkBuilder> {

        private String networkOptions;
        private String networkName;

        protected CreateNetworkBuilder() {
            super();
            super.setBuilder(this);
        }

        /**
         * Build the settings.
         * @return settings
         */
        public CreateNetworkSettings build() {
            return new CreateNetworkSettingsImpl(this);
        }

        /**
         * Set the network options.
         * @param networkOptions network options
         * @return this builder
         */
        public CreateNetworkBuilder setNetworkOptions(final String networkOptions) {
            this.networkOptions = networkOptions;
            return this;
        }

        /**
         * Set the network name.
         * @param networkName network name
         * @return this builder
         */
        public CreateNetworkBuilder setNetworkName(final String networkName) {
            this.networkName = networkName;
            return this;
        }
    }

    /**
     * Settings implementation.
     */
    final class CreateNetworkSettingsImpl extends DockerSettingsImpl implements CreateNetworkSettings {

        private final String networkOptions;
        private final String networkName;

        protected CreateNetworkSettingsImpl(final CreateNetworkBuilder builder) {
            super(builder);
            this.networkOptions = builder.networkOptions;
            this.networkName = builder.networkName;
        }

        @Override
        public String getNetworkOptions() {
            return networkOptions;
        }

        @Override
        public String getNetworkName() {
            return networkName;
        }
    }
}

package nl.futureedge.maven.docker.support;

public interface CreateNetworkSettings extends DockerSettings {

    String getNetworkOptions();

    String getNetworkName();

    static Builder builder() {
        return new Builder();
    }

    final class Builder extends DockerSettings.Builder<Builder> {

        private String networkOptions;
        private String networkName;

        protected Builder() {
            super();
            super.setBuilder(this);
        }

        public CreateNetworkSettings build() {
            return new CreateNetworkSettingsImpl(this);
        }

        public Builder setNetworkOptions(final String networkOptions) {
            this.networkOptions = networkOptions;
            return this;
        }

        public Builder setNetworkName(final String networkName) {
            this.networkName = networkName;
            return this;
        }
    }

    final class CreateNetworkSettingsImpl extends DockerSettingsImpl implements CreateNetworkSettings {

        private final String networkOptions;
        private final String networkName;

        protected CreateNetworkSettingsImpl(final CreateNetworkSettings.Builder builder) {
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

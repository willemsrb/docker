package nl.futureedge.maven.docker.support;

public interface RemoveNetworkSettings extends DockerSettings {

    String getNetworkName();

    static Builder builder() {
        return new Builder();
    }

    final class Builder extends DockerSettings.Builder<Builder> {

        private String networkName;

        protected Builder() {
            super();
            super.setBuilder(this);
        }

        public RemoveNetworkSettings build() {
            return new RemoveNetworkSettingsImpl(this);
        }

        public Builder setNetworkName(final String networkName) {
            this.networkName = networkName;
            return this;
        }
    }

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

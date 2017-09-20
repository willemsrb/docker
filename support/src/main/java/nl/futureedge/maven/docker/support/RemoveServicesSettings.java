package nl.futureedge.maven.docker.support;

public interface RemoveServicesSettings extends DockerSettings {

    String getFilter();

    static Builder builder() {
        return new Builder();
    }

    final class Builder extends DockerSettings.Builder<Builder> {

        private String filter;

        protected Builder() {
            super();
            super.setBuilder(this);
        }

        public RemoveServicesSettings build() {
            return new RemoveServicesSettingsImpl(this);
        }

        public RemoveServicesSettings.Builder setFilter(final String filter) {
            this.filter = filter;
            return this;
        }
    }

    final class RemoveServicesSettingsImpl extends DockerSettingsImpl implements RemoveServicesSettings {

        private final String filter;

        protected RemoveServicesSettingsImpl(final RemoveServicesSettings.Builder builder) {
            super(builder);
            this.filter = builder.filter;
        }

        @Override
        public String getFilter() {
            return filter;
        }
    }
}

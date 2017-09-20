package nl.futureedge.maven.docker.support;

public interface StartContainersSettings extends DockerSettings {

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

        public StartContainersSettings build() {
            return new StartContainersSettingsImpl(this);
        }


        public Builder setFilter(final String filter) {
            this.filter = filter;
            return this;
        }
    }

    final class StartContainersSettingsImpl extends DockerSettingsImpl implements StartContainersSettings {

        private final String filter;

        protected StartContainersSettingsImpl(final StartContainersSettings.Builder builder) {
            super(builder);
            this.filter = builder.filter;
        }

        @Override
        public String getFilter() {
            return filter;
        }
    }
}

package nl.futureedge.maven.docker.support;

public interface StopContainersSettings extends DockerSettings {

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

        public StopContainersSettings build() {
            return new StopContainersSettingsImpl(this);
        }


        public Builder setFilter(final String filter) {
            this.filter = filter;
            return this;
        }
    }

    final class StopContainersSettingsImpl extends DockerSettingsImpl implements StopContainersSettings {

        private final String filter;

        protected StopContainersSettingsImpl(final StopContainersSettings.Builder builder) {
            super(builder);
            this.filter = builder.filter;
        }

        @Override
        public String getFilter() {
            return filter;
        }
    }
}

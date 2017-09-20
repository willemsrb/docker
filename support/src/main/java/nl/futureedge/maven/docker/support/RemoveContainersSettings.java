package nl.futureedge.maven.docker.support;

public interface RemoveContainersSettings extends DockerSettings {

    String getFilter();

    static Builder builder() {
        return new Builder();
    }

    final class Builder extends DockerSettings.Builder<RemoveContainersSettings.Builder> {

        private String filter;

        protected Builder() {
            super();
            super.setBuilder(this);
        }

        public RemoveContainersSettings build() {
            return new RemoveContainersSettingsImpl(this);
        }


        public Builder setFilter(final String filter) {
            this.filter = filter;
            return this;
        }
    }

    final class RemoveContainersSettingsImpl extends DockerSettingsImpl implements RemoveContainersSettings {

        private final String filter;

        protected RemoveContainersSettingsImpl(final RemoveContainersSettings.Builder builder) {
            super(builder);
            this.filter = builder.filter;
        }

        @Override
        public String getFilter() {
            return filter;
        }
    }
}

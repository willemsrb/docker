package nl.futureedge.maven.docker.support;

public interface RemoveVolumesSettings extends DockerSettings {

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

        public RemoveVolumesSettings build() {
            return new RemoveVolumesSettingsImpl(this);
        }


        public Builder setFilter(final String filter) {
            this.filter = filter;
            return this;
        }
    }

    final class RemoveVolumesSettingsImpl extends DockerSettingsImpl implements RemoveVolumesSettings {

        private final String filter;

        protected RemoveVolumesSettingsImpl(final RemoveVolumesSettings.Builder builder) {
            super(builder);
            this.filter = builder.filter;
        }

        @Override
        public String getFilter() {
            return filter;
        }
    }
}

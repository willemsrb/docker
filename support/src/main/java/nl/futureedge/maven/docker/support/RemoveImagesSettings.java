package nl.futureedge.maven.docker.support;

public interface RemoveImagesSettings extends DockerSettings {

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

        public RemoveImagesSettings build() {
            return new RemoveImagesSettingsImpl(this);
        }

        public Builder setFilter(final String filter) {
            this.filter = filter;
            return this;
        }
    }

    final class RemoveImagesSettingsImpl extends DockerSettingsImpl implements RemoveImagesSettings {

        private final String filter;

        protected RemoveImagesSettingsImpl(final RemoveImagesSettings.Builder builder) {
            super(builder);
            this.filter = builder.filter;
        }

        @Override
        public String getFilter() {
            return filter;
        }
    }
}

package nl.futureedge.maven.docker.support;

/**
 * Settings.
 */
public interface StopContainersSettings extends FilteredListSettings {

    /**
     * @return builder
     */
    static Builder builder() {
        return new Builder();
    }

    /**
     * Builder.
     */
    final class Builder extends FilteredListSettings.Builder<Builder> {

        protected Builder() {
            super();
            super.setBuilder(this);
        }

        @Override
        public StopContainersSettings build() {
            return new StopContainersSettingsImpl(this);
        }
    }

    /**
     * Settings implementation.
     */
    final class StopContainersSettingsImpl extends FilteredListSettingsImpl implements StopContainersSettings {

        protected StopContainersSettingsImpl(final StopContainersSettings.Builder builder) {
            super(builder);
        }
    }
}

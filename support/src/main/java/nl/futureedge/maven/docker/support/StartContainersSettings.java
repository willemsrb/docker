package nl.futureedge.maven.docker.support;

/**
 * Settings.
 */
public interface StartContainersSettings extends FilteredListSettings {

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
        public StartContainersSettings build() {
            return new StartContainersSettingsImpl(this);
        }
    }

    /**
     * Settings implementation.
     */
    final class StartContainersSettingsImpl extends FilteredListSettingsImpl implements StartContainersSettings {

        protected StartContainersSettingsImpl(final StartContainersSettings.Builder builder) {
            super(builder);
        }
    }
}

package nl.futureedge.maven.docker.support;

/**
 * Settings.
 */
public interface StartContainersSettings extends FilteredListSettings {

    /**
     * @return builder
     */
    static StartContainersSettingsBuilder builder() {
        return new StartContainersSettingsBuilder();
    }

    /**
     * Builder.
     */
    final class StartContainersSettingsBuilder extends FilteredListSettingsBuilder<StartContainersSettingsBuilder> {

        protected StartContainersSettingsBuilder() {
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

        protected StartContainersSettingsImpl(final StartContainersSettingsBuilder builder) {
            super(builder);
        }
    }
}

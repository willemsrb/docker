package nl.futureedge.maven.docker.support;

/**
 * Settings.
 */
public interface StopContainersSettings extends FilteredListSettings {

    /**
     * @return builder
     */
    static StopContainersSettingsBuilder builder() {
        return new StopContainersSettingsBuilder();
    }

    /**
     * Builder.
     */
    final class StopContainersSettingsBuilder extends FilteredListSettings.Builder<StopContainersSettingsBuilder> {

        protected StopContainersSettingsBuilder() {
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

        protected StopContainersSettingsImpl(final StopContainersSettingsBuilder builder) {
            super(builder);
        }
    }
}

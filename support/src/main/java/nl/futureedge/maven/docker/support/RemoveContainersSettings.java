package nl.futureedge.maven.docker.support;


/**
 * Settings.
 */
public interface RemoveContainersSettings extends FilteredListSettings {

    /**
     * @return builder
     */
    static RemoveContainersSettingsBuilder builder() {
        return new RemoveContainersSettingsBuilder();
    }

    /**
     * Builder.
     */
    final class RemoveContainersSettingsBuilder extends FilteredListSettings.Builder<RemoveContainersSettingsBuilder> {

        protected RemoveContainersSettingsBuilder() {
            super();
            super.setBuilder(this);
        }

        @Override
        public RemoveContainersSettings build() {
            return new RemoveContainersSettingsImpl(this);
        }
    }

    /**
     * Settings implementation.
     */
    final class RemoveContainersSettingsImpl extends FilteredListSettingsImpl implements RemoveContainersSettings {

        protected RemoveContainersSettingsImpl(final RemoveContainersSettingsBuilder builder) {
            super(builder);
        }
    }
}

package nl.futureedge.maven.docker.support;

/**
 * Settings.
 */
public interface RemoveVolumesSettings extends FilteredListSettings {

    /**
     * @return builder
     */
    static RemoveVolumesSettingsBuilder builder() {
        return new RemoveVolumesSettingsBuilder();
    }

    /**
     * Builder.
     */
    public final class RemoveVolumesSettingsBuilder extends FilteredListSettingsBuilder<RemoveVolumesSettingsBuilder> {
        protected RemoveVolumesSettingsBuilder() {
            super();
            super.setBuilder(this);
        }

        @Override
        public RemoveVolumesSettings build() {
            return new RemoveVolumesSettingsImpl(this);
        }
    }

    /**
     * Settings implementation.
     */
    final class RemoveVolumesSettingsImpl extends FilteredListSettingsImpl implements RemoveVolumesSettings {

        protected RemoveVolumesSettingsImpl(final RemoveVolumesSettingsBuilder builder) {
            super(builder);
        }
    }
}

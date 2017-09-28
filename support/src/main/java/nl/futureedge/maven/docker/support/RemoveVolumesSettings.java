package nl.futureedge.maven.docker.support;

/**
 * Settings.
 */
public interface RemoveVolumesSettings extends FilteredListSettings {

    /**
     * @return builder
     */
    static Builder builder() {
        return new Builder();
    }

    /**
     * Builder.
     */
    public final class Builder extends FilteredListSettings.Builder<Builder> {
        protected Builder() {
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

        protected RemoveVolumesSettingsImpl(final RemoveVolumesSettings.Builder builder) {
            super(builder);
        }
    }
}

package nl.futureedge.maven.docker.support;


/**
 * Settings.
 */
public interface RemoveContainersSettings extends FilteredListSettings {

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
        public RemoveContainersSettings build() {
            return new RemoveContainersSettingsImpl(this);
        }
    }

    /**
     * Settings implementation.
     */
    final class RemoveContainersSettingsImpl extends FilteredListSettingsImpl implements RemoveContainersSettings {

        protected RemoveContainersSettingsImpl(final RemoveContainersSettings.Builder builder) {
            super(builder);
        }
    }
}

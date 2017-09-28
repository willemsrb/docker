package nl.futureedge.maven.docker.support;

/**
 * Settings.
 */
public interface RemoveServicesSettings extends FilteredListSettings {

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
        public RemoveServicesSettings build() {
            return new RemoveServicesSettingsImpl(this);
        }
    }

    /**
     * Settings implementation.
     */
    final class RemoveServicesSettingsImpl extends FilteredListSettingsImpl implements RemoveServicesSettings {
        protected RemoveServicesSettingsImpl(final RemoveServicesSettings.Builder builder) {
            super(builder);
        }
    }
}

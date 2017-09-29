package nl.futureedge.maven.docker.support;

/**
 * Settings.
 */
public interface RemoveServicesSettings extends FilteredListSettings {

    /**
     * @return builder
     */
    static RemoveServicesSettingsBuilder builder() {
        return new RemoveServicesSettingsBuilder();
    }

    /**
     * Builder.
     */
    public final class RemoveServicesSettingsBuilder extends FilteredListSettingsBuilder<RemoveServicesSettingsBuilder> {
        protected RemoveServicesSettingsBuilder() {
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
        protected RemoveServicesSettingsImpl(final RemoveServicesSettingsBuilder builder) {
            super(builder);
        }
    }
}

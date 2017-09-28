package nl.futureedge.maven.docker.support;

/**
 * Settings.
 */
public interface RemoveImagesSettings extends FilteredListSettings {

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
        public RemoveImagesSettings build() {
            return new RemoveImagesSettingsImpl(this);
        }
    }

    /**
     * Settings implementation.
     */
    final class RemoveImagesSettingsImpl extends FilteredListSettingsImpl implements RemoveImagesSettings {

        protected RemoveImagesSettingsImpl(final RemoveImagesSettings.Builder builder) {
            super(builder);
        }
    }
}

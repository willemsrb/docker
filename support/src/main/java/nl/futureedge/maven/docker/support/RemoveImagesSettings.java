package nl.futureedge.maven.docker.support;

/**
 * Settings.
 */
public interface RemoveImagesSettings extends FilteredListSettings {

    /**
     * @return builder
     */
    static RemoveImagesSettingsBuilder builder() {
        return new RemoveImagesSettingsBuilder();
    }

    /**
     * Builder.
     */
    final class RemoveImagesSettingsBuilder extends FilteredListSettingsBuilder<RemoveImagesSettingsBuilder> {

        protected RemoveImagesSettingsBuilder() {
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

        protected RemoveImagesSettingsImpl(final RemoveImagesSettingsBuilder builder) {
            super(builder);
        }
    }
}

package nl.futureedge.maven.docker.support;

/**
 * Settings.
 */
public interface FilteredListSettings extends DockerSettings {

    /**
     * @return filter
     */
    String getFilter();

    /**
     * Builder.
     */
    public abstract class FilteredListSettingsBuilder<T extends FilteredListSettingsBuilder> extends DockerSettings.Builder<T> {

        private String filter;

        protected FilteredListSettingsBuilder() {
            super();
        }

        /**
         * Set the filter.
         * @param filter filter
         * @return this builder
         */
        public T setFilter(final String filter) {
            this.filter = filter;
            return getBuilder();
        }
    }

    /**
     * Settings implementation.
     */
    class FilteredListSettingsImpl extends DockerSettingsImpl implements FilteredListSettings {

        private final String filter;

        protected FilteredListSettingsImpl(final FilteredListSettingsBuilder builder) {
            super(builder);
            this.filter = builder.filter;
        }

        @Override
        public final String getFilter() {
            return filter;
        }
    }
}

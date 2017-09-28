package nl.futureedge.maven.docker.support;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.Stack;

/**
 * Settings.
 */
public interface RunConfigurationSettings extends DockerSettings {

    /**
     * @return stack to 'store' dependencies
     */
    default Stack<String> getStack() {
        return new Stack<>();
    }

    /**
     * @return list of previously loaded configurations
     */
    default Set<String> getLoaded() {
        return new HashSet<>();
    }

    /**
     * @return project properties (to store inspected values)
     */
    Properties getProjectProperties();

    /**
     * @return configuration name
     */
    String getConfigurationName();

    /**
     * @return additional run options
     */
    String getAdditionalRunOptions();

    /**
     * @return command (overrides command in loaded configuration)
     */
    String getCommand();

    /**
     * @return network name
     */
    String getNetworkName();

    /**
     * @return should all ports be mapped to random ports (overrides external ports in loaded configuration and dependencies)?
     */
    boolean isRandomPorts();

    /**
     * @return should dependencies be skipped?
     */
    boolean isSkipDependencies();

    /**
     * @return builder
     */
    static Builder builder() {
        return new Builder();
    }

    /**
     * Builder.
     *
     * <ul>
     *     <li>Default stack = empty stack</li>
     *     <li>Default loaded = empty list</li>
     *     <li>Default projectProperties = {@link System#getProperties()}</li>
     *     <li>Default randomPorts = false</li>
     *     <li>Default skipDependencies = false</li>
     * </ul>
     */
    final class Builder extends DockerSettings.Builder<Builder> {
        private Stack stack = new Stack();
        private Set<String> loaded = new HashSet<>();
        private Properties projectProperties = System.getProperties();
        private String configurationName;
        private String additionalRunOptions;
        private String command;
        private String networkName;
        private boolean randomPorts = false;
        private boolean skipDependencies = false;

        protected Builder() {
            super();
            super.setBuilder(this);
        }

        @Override
        public RunConfigurationSettings build() {
            return new RunConfigurationSettingsImpl(this);
        }

        /**
         * Set stack to 'store' dependencies.
         * @param stack stack
         * @return this builder
         */
        public Builder setStack(final Stack stack) {
            this.stack = stack;
            return this;
        }

        /**
         * Set list of previously loaded configurations.
         * @param loaded list of previously loaded configurations
         * @return this builder
         */
        public Builder setLoaded(final Set<String> loaded) {
            this.loaded = loaded;
            return this;
        }

        /**
         * Set project properties (to store inspected values)
         * @param projectProperties project properties
         * @return this builder
         */
        public Builder setProjectProperties(final Properties projectProperties) {
            this.projectProperties = projectProperties;
            return this;
        }

        /**
         * Set configuration name.
         * @param configurationName configuration name
         * @return this builder
         */
        public Builder setConfigurationName(final String configurationName) {
            this.configurationName = configurationName;
            return this;
        }

        /**
         * Set additional run options.
         * @param additionalRunOptions additional run options
         * @return this builder
         */
        public Builder setAdditionalRunOptions(final String additionalRunOptions) {
            this.additionalRunOptions = additionalRunOptions;
            return this;
        }

        /**
         * Set command.
         * @param command command
         * @return this builder
         */
        public Builder setCommand(final String command) {
            this.command = command;
            return this;
        }

        /**
         * Set network name.
         * @param networkName network name
         * @return this builder
         */
        public Builder setNetworkName(final String networkName) {
            this.networkName = networkName;
            return this;
        }

        /**
         * Set if all ports should be mapped to random ports (overrides external ports in loaded configuration and dependencies)?
         * @param randomPorts true, if all ports should be mapped to random ports
         */
        public Builder setRandomPorts(final boolean randomPorts) {
            this.randomPorts = randomPorts;
            return this;
        }

        /**
         * Sets if dependencies should be skipped?
         * @param skipDependencies true, if dependencies should be skipped
         */
        public Builder setSkipDependencies(final boolean skipDependencies) {
            this.skipDependencies = skipDependencies;
            return this;
        }
    }

    /**
     * Settings implementation.
     */
    final class RunConfigurationSettingsImpl extends DockerSettingsImpl implements RunConfigurationSettings {

        private final Stack stack;
        private final Set<String> loaded;
        private final Properties projectProperties;
        private final String configurationName;
        private final String additionalRunOptions;
        private final String command;
        private final String networkName;
        private final boolean randomPorts;
        private final boolean skipDependencies;

        protected RunConfigurationSettingsImpl(final RunConfigurationSettings.Builder builder) {
            super(builder);
            this.stack = builder.stack;
            this.loaded = builder.loaded;
            this.projectProperties = builder.projectProperties;
            this.configurationName = builder.configurationName;
            this.additionalRunOptions = builder.additionalRunOptions;
            this.command = builder.command;
            this.networkName = builder.networkName;
            this.randomPorts = builder.randomPorts;
            this.skipDependencies = builder.skipDependencies;
        }

        @Override
        public Stack getStack() {
            return stack;
        }

        @Override
        public Set<String> getLoaded() {
            return loaded;
        }

        @Override
        public Properties getProjectProperties() {
            return projectProperties;
        }

        @Override
        public String getConfigurationName() {
            return configurationName;
        }

        @Override
        public String getAdditionalRunOptions() {
            return additionalRunOptions;
        }

        @Override
        public String getCommand() {
            return command;
        }

        @Override
        public String getNetworkName() {
            return networkName;
        }

        @Override
        public boolean isRandomPorts() {
            return randomPorts;
        }

        @Override
        public boolean isSkipDependencies() {
            return skipDependencies;
        }
    }
}

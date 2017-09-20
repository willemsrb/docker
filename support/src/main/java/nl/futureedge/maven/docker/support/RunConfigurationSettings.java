package nl.futureedge.maven.docker.support;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.Stack;

public interface RunConfigurationSettings extends DockerSettings {

    default Stack<String> getStack() {
        return new Stack<>();
    }

    default Set<String> getLoaded() {
        return new HashSet<>();
    }

    Properties getProjectProperties();

    String getConfigurationName();

    String getNetworkName();

    boolean isRandomPorts();

    boolean isSkipDependencies();

    static Builder builder() {
        return new Builder();
    }

    final class Builder extends DockerSettings.Builder<Builder> {
        private Stack stack = new Stack();
        private Set<String> loaded = new HashSet<>();
        private Properties projectProperties = System.getProperties();
        private String configurationName;
        private String networkName;
        private boolean randomPorts = false;
        private boolean skipDependencies = false;

        protected Builder() {
            super();
            super.setBuilder(this);
        }

        public RunConfigurationSettings build() {
            return new RunConfigurationSettingsImpl(this);
        }

        public Builder setStack(final Stack stack) {
            this.stack = stack;
            return this;
        }

        public Builder setLoaded(final Set<String> loaded) {
            this.loaded = loaded;
            return this;
        }

        public Builder setProjectProperties(final Properties projectProperties) {
            this.projectProperties = projectProperties;
            return this;
        }

        public Builder setConfigurationName(final String configurationName) {
            this.configurationName = configurationName;
            return this;
        }

        public Builder setNetworkName(final String networkName) {
            this.networkName = networkName;
            return this;
        }

        public Builder setRandomPorts(final boolean randomPorts) {
            this.randomPorts = randomPorts;
            return this;
        }

        public Builder setSkipDependencies(final boolean skipDependencies) {
            this.skipDependencies = skipDependencies;
            return this;
        }
    }

    final class RunConfigurationSettingsImpl extends DockerSettingsImpl implements RunConfigurationSettings {

        private final Stack stack;
        private final Set<String> loaded;
        private final Properties projectProperties;
        private final String configurationName;
        private final String networkName;
        private final boolean randomPorts;
        private final boolean skipDependencies;

        protected RunConfigurationSettingsImpl(final RunConfigurationSettings.Builder builder) {
            super(builder);
            this.stack = builder.stack;
            this.loaded = builder.loaded;
            this.projectProperties = builder.projectProperties;
            this.configurationName = builder.configurationName;
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

package nl.futureedge.maven.docker.support;

/**
 * Settings.
 */
public interface CommandSettings extends DockerSettings {

    /**
     * @return the command to execute
     */
    String getCommand();

    /**
     * @return builder
     */
    static Builder builder() {
        return new Builder();
    }

    /**
     * Builder.
     */
    public final class Builder extends DockerSettings.Builder<CommandSettings.Builder> {

        private String command;

        protected Builder() {
            super();
            super.setBuilder(this);
        }

        @Override
        public CommandSettings build() {
            return new CommandSettingsImpl(this);
        }

        /**
         * Set the command to execute.
         * @param command command
         * @return this builder
         */
        public Builder setCommand(final String command) {
            this.command = command;
            return this;
        }
    }

    /**
     * Settings implementation.
     */
    final class CommandSettingsImpl extends DockerSettingsImpl implements CommandSettings {

        private final String command;

        protected CommandSettingsImpl(final CommandSettings.Builder builder) {
            super(builder);
            this.command = builder.command;
        }

        @Override
        public String getCommand() {
            return command;
        }
    }
}

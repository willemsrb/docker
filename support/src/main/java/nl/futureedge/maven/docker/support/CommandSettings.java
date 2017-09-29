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
    static CommandSettingsBuilder builder() {
        return new CommandSettingsBuilder();
    }

    /**
     * Builder.
     */
    public final class CommandSettingsBuilder extends DockerSettings.Builder<CommandSettingsBuilder> {

        private String command;

        protected CommandSettingsBuilder() {
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
        public CommandSettingsBuilder setCommand(final String command) {
            this.command = command;
            return this;
        }
    }

    /**
     * Settings implementation.
     */
    final class CommandSettingsImpl extends DockerSettingsImpl implements CommandSettings {

        private final String command;

        protected CommandSettingsImpl(final CommandSettingsBuilder builder) {
            super(builder);
            this.command = builder.command;
        }

        @Override
        public String getCommand() {
            return command;
        }
    }
}

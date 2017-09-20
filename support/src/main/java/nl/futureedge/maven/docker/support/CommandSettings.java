package nl.futureedge.maven.docker.support;

public interface CommandSettings extends DockerSettings {

    String getCommand();

    static Builder builder() {
        return new Builder();
    }

    final class Builder extends DockerSettings.Builder<CommandSettings.Builder> {

        private String command;

        protected Builder() {
            super();
            super.setBuilder(this);
        }

        public CommandSettings build() {
            return new CommandSettingsImpl(this);
        }


        public Builder setCommand(final String command) {
            this.command = command;
            return this;
        }
    }

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

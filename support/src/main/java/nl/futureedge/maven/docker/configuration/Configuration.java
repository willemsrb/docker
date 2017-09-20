package nl.futureedge.maven.docker.configuration;

import java.util.List;

public class Configuration {


    private String imageRegistry;
    private String imageName;
    private String imageTag;

    private String runOptions;
    private boolean daemon = true;
    private String command;

    private String containerIdProperty;
    private String containerNameProperty;
    private String hostnameProperty;

    private List<Port> ports;

    private List<String> dependsOn;


    public String getImageRegistry() {
        return imageRegistry;
    }

    public String getImageName() {
        return imageName;
    }

    public String getImageTag() {
        return imageTag;
    }

    public String getRunOptions() {
        return runOptions;
    }

    public boolean isDaemon() {
        return daemon;
    }

    public String getCommand() {
        return command;
    }

    public String getContainerIdProperty() {
        return containerIdProperty;
    }

    public String getContainerNameProperty() {
        return containerNameProperty;
    }

    public String getHostnameProperty() {
        return hostnameProperty;
    }

    public List<Port> getPorts() {
        return ports;
    }

    public List<String> getDependsOn() {
        return dependsOn;
    }

    public class Port {

        private String port;
        private String external;
        private String property;

        public String getPort() {
            return port;
        }

        public String getExternal() {
            return external;
        }

        public String getProperty() {
            return property;
        }
    }
}

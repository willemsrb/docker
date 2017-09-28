package nl.futureedge.maven.docker.configuration;

import java.util.List;

/**
 * Configuration.
 */
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


    /**
     * @return image registry
     */
    public String getImageRegistry() {
        return imageRegistry;
    }

    /**
     * @return image name
     */
    public String getImageName() {
        return imageName;
    }

    /**
     * @return image tag
     */
    public String getImageTag() {
        return imageTag;
    }

    /**
     * @return run options
     */
    public String getRunOptions() {
        return runOptions;
    }

    /**
     * @return should this container be run as a daemon?
     */
    public boolean isDaemon() {
        return daemon;
    }

    /**
     * @return command to run
     */
    public String getCommand() {
        return command;
    }

    /**
     * @return property name to store the container id
     */
    public String getContainerIdProperty() {
        return containerIdProperty;
    }

    /**
     * @return property name to store the container name
     */
    public String getContainerNameProperty() {
        return containerNameProperty;
    }

    /**
     * @return property name to store the host name
     */
    public String getHostnameProperty() {
        return hostnameProperty;
    }

    /**
     * @return ports
     */
    public List<Port> getPorts() {
        return ports;
    }

    /**
     * @return configurations this configuration depends on
     */
    public List<String> getDependsOn() {
        return dependsOn;
    }

    /**
     * Port.
     */
    public class Port {

        private String port;
        private String external;
        private String property;

        /**
         * @return port (eg 80/tcp)
         */
        public String getPort() {
            return port;
        }

        /**
         * @return external port (not used when using random ports)
         */
        public String getExternal() {
            return external;
        }

        /**
         * @return property name to store this external port (when using random ports)
         */
        public String getProperty() {
            return property;
        }
    }
}

package nl.futureedge.maven.docker.support;

import java.util.Properties;

public interface InspectContainerSettings extends DockerSettings {

    Properties getProjectProperties();

    String getContainerId();

    String getContainerNameProperty();

    String getHostnameProperty();

    Properties getPortProperties();
}

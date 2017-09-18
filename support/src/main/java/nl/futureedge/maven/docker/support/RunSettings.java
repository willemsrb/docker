package nl.futureedge.maven.docker.support;

import java.util.Properties;

public interface RunSettings extends DockerSettings {

    Properties getProjectProperties();

    String getRunOptions();

    String getImage();

    String getCommand();

    String getContainerIdProperty();
}

package nl.futureedge.maven.docker;

import org.junit.Test;

public class PropertiesIT {

    @Test
    public void test() {
        System.err.println("ALPINE-1 - container name: " + System.getProperty("alpine-1.name"));
        System.err.println("ALPINE-2 - container name: " + System.getProperty("alpine-2.name"));
        System.err.println("ALPINE-3 - container name: " + System.getProperty("alpine-3.name"));
    }
}

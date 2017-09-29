package nl.futureedge.maven.docker.mojo;

public class RunMojoTest {
// TODO: Mocking RunExecutable is problematic (final)
//    @Test
//    public void test() throws MojoExecutionException, MojoFailureException, DockerException {
//        final MavenProject project = Mockito.mock(MavenProject.class);
//        final Properties projectProperties = new Properties();
//        Mockito.when(project.getProperties()).thenReturn(projectProperties);
//
//        final RunMojo subject = new RunMojo();
//        ReflectionTestUtils.setField(subject, "project", project);
//        ReflectionTestUtils.setField(subject, "runOptions", "runOptions");
//        ReflectionTestUtils.setField(subject, "daemon", true);
//        ReflectionTestUtils.setField(subject, "image", "registry/image:tag");
//        ReflectionTestUtils.setField(subject, "command", "command");
//        ReflectionTestUtils.setField(subject, "containerIdProperty", "containerIdProperty");
//
//        ReflectionTestUtils.setField(subject, "containerNameProperty", "containerNameProperty");
//        ReflectionTestUtils.setField(subject, "hostnameProperty", "hostnameProperty");
//        ReflectionTestUtils.setField(subject, "portPropertiesAsString", new String[]{"80/tcp=test.port.80", "81/tcp=test.port.81"});
//        final Properties portProperties = new Properties();
//        portProperties.setProperty("82/tcp", "test.port.82");
//        portProperties.setProperty("83/tcp", "test.port.83");
//        ReflectionTestUtils.setField(subject, "portProperties", portProperties);
//
//        final RunExecutable runExecutable = Mockito.mock(RunExecutable.class);
//        subject.setRunExecutableCreator((settings) -> {
//            Assert.assertNotNull(settings.getProjectProperties());
//            Assert.assertEquals("networkOptions", settings.getRunOptions());
//            Assert.assertEquals("networkName", settings.isDaemon());
//            Assert.assertEquals("networkOptions", settings.getImage());
//            Assert.assertEquals("command", settings.getCommand());
//            Assert.assertEquals("containerIdProperty", settings.getContainerIdProperty());
//
//            return runExecutable;
//        });
//        Mockito.doReturn("container-id-001").when(runExecutable).execute();
//
//        final DockerExecutable inspectExecutable = Mockito.mock(DockerExecutable.class);
//        subject.setInspectContainerExecutableCreator((settings) -> {
//            Assert.assertNotNull(settings.getProjectProperties());
//            Assert.assertEquals("container-id-001", settings.getContainerId());
//            Assert.assertEquals("containerNameProperty", settings.getContainerNameProperty());
//            Assert.assertEquals("hostnameProperty", settings.getHostnameProperty());
//            Assert.assertEquals(4, settings.getPortProperties().size());
//            Assert.assertEquals("test.port.80", settings.getPortProperties().getProperty("80/tcp"));
//            Assert.assertEquals("test.port.81", settings.getPortProperties().getProperty("81/tcp"));
//            Assert.assertEquals("test.port.82", settings.getPortProperties().getProperty("82/tcp"));
//            Assert.assertEquals("test.port.83", settings.getPortProperties().getProperty("83/tcp"));
//
//            return inspectExecutable;
//        });
//        Assert.assertNull(subject.getContainerId());
//        subject.execute();
//        Assert.assertEquals("container-id-001", subject.getContainerId());
//
//        Mockito.verify(runExecutable).execute();
//        Mockito.verify(inspectExecutable).execute();
//        Mockito.verifyNoMoreInteractions(runExecutable, inspectExecutable);
//    }
}

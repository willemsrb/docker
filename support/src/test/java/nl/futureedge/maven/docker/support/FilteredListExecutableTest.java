package nl.futureedge.maven.docker.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.exception.DockerExecutionException;
import nl.futureedge.maven.docker.executor.DockerExecutor;
import org.junit.Assert;
import org.junit.Test;

public class FilteredListExecutableTest {

    @Test
    public void testOk() throws DockerException {
        final TestSettings settings = TestSettings.builder()
                .setFilter("filter")
                .setIgnoreFailures(true)
                .build();

        final TestExecutable subject = new TestExecutable(settings);
        subject.execute();

        Assert.assertEquals(true, subject.listed);
        Assert.assertEquals(Arrays.asList("one", "two", "three"), subject.items);
    }

    @Test
    public void testListFail() {
        final TestSettings settings = TestSettings.builder()
                .setFilter("fail")
                .setIgnoreFailures(false)
                .build();

        final TestExecutable subject = new TestExecutable(settings);
        try {
            subject.execute();
            Assert.fail();
        } catch (DockerException e) {
            // Expected
        }

        Assert.assertEquals(true, subject.listed);
        Assert.assertEquals(Collections.emptyList(), subject.items);
    }

    @Test
    public void testExecuteFail() throws DockerException {
        final TestSettings settings = TestSettings.builder()
                .setFilter("filter")
                .setIgnoreFailures(false)
                .build();

        final TestExecutable subject = new TestExecutable(settings);
        try {
            subject.execute();
            Assert.fail();
        } catch (DockerException e) {
            // Expected
        }

        Assert.assertEquals(true, subject.listed);
        Assert.assertEquals(Arrays.asList("one", "two"), subject.items);
    }


    public static class TestExecutable extends FilteredListExecutable {

        private boolean listed = false;
        private List<String> items = new ArrayList<>();


        public TestExecutable(final TestSettings settings) {
            super(settings);
        }


        protected List<String> list(final DockerExecutor executor, final String filter) throws DockerExecutionException {
            listed = true;
            if ("fail".equals(filter)) {
                throw new DockerExecutionException("Fail!");
            }

            return Arrays.asList("", "one", "", "two", "three");
        }

        protected void execute(final DockerExecutor executor, final String item) throws DockerExecutionException {
            items.add(item);
            if (item.equals("two")) {
                throw new DockerExecutionException("Fail!");
            }
        }
    }

    public interface TestSettings extends FilteredListSettings {

        static Builder builder() {
            return new Builder();
        }

        final class Builder extends FilteredListSettings.Builder<Builder> {

            protected Builder() {
                super();
                super.setBuilder(this);
            }

            public TestSettings build() {
                return new TestSettingsImpl(this);
            }
        }

        final class TestSettingsImpl extends FilteredListSettingsImpl implements TestSettings {

            protected TestSettingsImpl(final TestSettings.Builder builder) {
                super(builder);
            }
        }
    }
}

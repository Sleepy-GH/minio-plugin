package io.jenkins.plugins.minio.config;

import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.RestartableJenkinsRule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class GlobalMinioConfigurationTest {

    @Rule
    public RestartableJenkinsRule rr = new RestartableJenkinsRule();

    /**
     * Tries to exercise enough code paths to catch common mistakes:
     * <ul>
     * <li>missing {@code load}
     * <li>missing {@code save}
     * <li>misnamed or absent getter/setter
     * <li>misnamed {@code textbox}
     * </ul>
     */
    @Test
    public void uiAndStorage() {
        rr.then(r -> {
            assertNull("not set initially", GlobalMinioConfiguration.get().getConfiguration());
            HtmlForm config = r.createWebClient().goTo("configure").getFormByName("config");
            HtmlTextInput textbox = config.getInputByName("_.host");
            textbox.setText("hello");
            r.submit(config);
            assertEquals("global config page let us edit it", "hello", GlobalMinioConfiguration.get().getConfiguration().getHost());
        });
        rr.then(r -> {
            assertEquals("still there after restart of Jenkins", "hello", GlobalMinioConfiguration.get().getConfiguration().getHost());
        });
    }

}
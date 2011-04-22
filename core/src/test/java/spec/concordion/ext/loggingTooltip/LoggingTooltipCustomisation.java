package spec.concordion.ext.loggingTooltip;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

import test.concordion.TestRig;

@RunWith(ConcordionRunner.class)
public class LoggingTooltipCustomisation {
    public String acronym;
    private static final Logger fooLogger = Logger.getLogger("foo");
    private static final Logger barLogger = Logger.getLogger("bar");
    private static final Logger bazLogger = Logger.getLogger("baz");
    
    public void configureLogLevel(String logLevel) {
        LoggingTooltipExtensionFactory.loggers = "";
        LoggingTooltipExtensionFactory.logLevel = Level.parse(logLevel);
    }
    
    public void configureLoggers(String loggers) {
        LoggingTooltipExtensionFactory.loggers = loggers;
        LoggingTooltipExtensionFactory.logLevel = Level.FINEST;
    }
    
    public void setSystemProperty(String key, String value) {
        System.setProperty(key, value);
    }

    public String render(String fragment) throws Exception {
        String outputFragment = new TestRig()
            .withFixture(this)
            .withSourceFilter("/org/concordion/ext/resource")
            .processFragment(fragment)
            .getOutputFragmentXML();
        return outputFragment;
    }

    public String getMessagesFrom(String outputFragment) {
        String message = "";
        String[] lines = outputFragment.split("<br />");
        for (String line : lines) {
            message += extractLogMessage(line) + "\n";
        }
        return message.trim();
    }

    private String extractLogMessage(String outputFragment) {
        int logMessageStart = outputFragment.indexOf("]");
        if (logMessageStart == -1) {
            return "";
        }
        return "[timestamp]" + (outputFragment.substring(logMessageStart + 1)).trim();
    }
    
    public String getName() throws Exception {
        fooLogger.info("Hello Foo");    
        barLogger.info("Hello Bar");    
        bazLogger.info("Hello Baz");    
        return "Frank";
    }

    public void logStuff() throws Exception {
        fooLogger.info("Hello Info");    
        barLogger.fine("Hello Fine");    
        bazLogger.warning("Hello Warn");    
    }
}
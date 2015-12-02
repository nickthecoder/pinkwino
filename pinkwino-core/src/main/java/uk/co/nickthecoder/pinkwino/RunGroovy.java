package uk.co.nickthecoder.pinkwino;

import groovy.lang.GroovyShell;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RunGroovy
{
    /**
     * This logger can be used by the scripts themselves, so turn on logging for this
     * logger to see the script's log messages.
     */
    protected static Logger _logger = LogManager.getLogger(RunGroovy.class);

    /**
     * Run a groovy script from a file.
     */
    public static void fromFile(String scriptFilename)
    {
        try {

            InputStream input = new FileInputStream(scriptFilename);

            RunGroovy.fromStream(input);

        } catch (Exception e) {
            _logger.error("An error occurred when executing the wiki bean shell init script file : " + scriptFilename);
            _logger.error(e);
            e.printStackTrace();
        }
    }

    /**
     * Run a groovy script from the classpath (usually
     * WEB-INF/classes/pinkwino.groovy).
     */
    public static void fromClasspath(String scriptName)
    {
        InputStream input = WikiEngine.instance().getClass().getClassLoader().getResourceAsStream(scriptName);

        if (input == null) {
            _logger.error("Failed to find script : " + scriptName + " within the classpath.");
            _logger.error("Place scripts in WEB-INF/classes/");
        } else {

            try {
                RunGroovy.fromStream(input);
            } catch (Exception e) {
                e.printStackTrace();
                _logger.error("An error occurred when executing the groovy init script from classpath : " + scriptName);
                _logger.error(e);
            }
        }
    }

    public static void fromStream(InputStream input)
    {
        GroovyShell shell = new GroovyShell();
        shell.setVariable("logger", _logger);
        shell.evaluate(new InputStreamReader(input));
    }
}

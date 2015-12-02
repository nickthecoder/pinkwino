/*

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.

 */

package uk.co.nickthecoder.pinkwino.commands;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Runs a command, handling stdout, stderr, stdin in a flexible manner.
 */

public class Command implements Runnable
{

    protected static Logger _logger = LogManager.getLogger(Command.class);

    private InputStream _stdIn;

    /**
     * Is notified about new text from stdout and stderr, and also the exit
     * status when the comman finishes
     */
    private CommandListener _commandListener;

    /**
     * The command and its arguments
     */
    private String[] _commandArray;

    /**
     * A list of environment variables formated as name=value
     */
    private String[] _environmentParameters;

    /**
     * The working directory for the new Process, or null for no-change
     */
    private File _workingDirectory;

    public Command()
    {
    }

    public void setCommandListener(CommandListener commandListener)
    {
        _commandListener = commandListener;
    }

    /**
     * Get method for attribute {@link #_environmentParameters}. A list of
     * environment variables formated as name=value
     */
    public String[] getEnvironmentParameters()
    {
        return _environmentParameters;
    }

    /**
     * Set method for attribute {@link #_environmentParameters}. A list of
     * environment variables formated as name=value
     */
    public void setEnvironmentParameters(String[] value)
    {
        _environmentParameters = value;
    }

    /**
     * Get method for attribute {@link #_workingDirectory}. The working
     * directory for the new Process, or null for no-change
     */
    public File getWorkingDirectory()
    {
        return _workingDirectory;
    }

    /**
     * Set method for attribute {@link #_workingDirectory}. The working
     * directory for the new Process, or null for no-change
     */
    public void setWorkingDirectory(File value)
    {
        _workingDirectory = value;
    }

    public InputStream getStdIn()
    {
        return _stdIn;
    }

    public void setStdIn(InputStream inputStream)
    {
        _stdIn = inputStream;
    }

    public void setStdIn(String string)
    {
        _stdIn = new ByteArrayInputStream(string.getBytes());
        // _stdIn = new StringBufferInputStream( string );
    }

    public String[] getCommandArray()
    {
        return _commandArray;
    }

    public void setCommandArray(String[] value)
    {
        _commandArray = value;
    }

    public void run()
    {
        try {

            // Note, either envp or workingDirectory or both may be null; thats
            // OK.
            Process process = Runtime.getRuntime().exec(getCommandArray(), getEnvironmentParameters(),
                            getWorkingDirectory());

            Thread stdoutConsumer = new ConsumerThread(process.getInputStream())
            {
                public void consume(String line)
                {
                    _commandListener.eatStdOut(line);
                }
            };
            Thread stderrConsumer = new ConsumerThread(process.getErrorStream())
            {
                public void consume(String line)
                {
                    _commandListener.eatStdErr(line);
                }
            };

            stdoutConsumer.start();
            stderrConsumer.start();

            InputStream stdIn = getStdIn();

            if (stdIn != null) {
                try {

                    OutputStream processOS = process.getOutputStream();
                    byte[] buffer = new byte[1000];

                    while (true) {
                        int bytes = stdIn.read(buffer, 0, buffer.length);
                        if (bytes == -1) {
                            break; // EOF
                        }
                        processOS.write(buffer, 0, bytes);
                    }

                    stdIn.close();
                    processOS.close();

                } catch (Exception e) {
                    _logger.info("Exception - possibly irrelevant : " + e);
                }

            }

            int exitStatus;
            try {
                exitStatus = process.waitFor();

            } catch (Exception e) {
                _logger.error("process waitFor failed : " + e);
                process.destroy();
                throw e;
            }

            /*
             * // There is the possibility that the consumer threads have not
             * quite // caught up yet - so they may still be running, even
             * though the // process has finished. // This code works, but takes
             * AGES to run, so I've disabled it. while( stderrConsumer.isAlive()
             * ) { stderrConsumer.join(); } while( stdoutConsumer.isAlive() ) {
             * stdoutConsumer.join(); }
             */

            if (_commandListener != null) {
                _commandListener.finished(exitStatus);
            }

        } catch (Exception e) {

            _logger.error("Command failed : " + e);
            e.printStackTrace();

            if (_commandListener != null) {
                _commandListener.eatStdErr(e.toString());
                _commandListener.finished(-999);
            }

        }
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Command : ");

        for (int i = 0; i < getCommandArray().length; i++) {
            if (i == 0) {
                buffer.append(getCommandArray()[i]).append(" ");
            } else {
                buffer.append("'").append(getCommandArray()[i]).append("' ");
            }
        }
        buffer.append(" working dir : " + _workingDirectory);
        return buffer.toString();
    }

    // {{{ ---------------- [[Inner Class ConsumerThread]] --------------------

    /**
     * A seperate thread is created to read the output from the command. A
     * command has a stderr, and a stdout, so there can be up to two new threads
     * created.
     */
    abstract class ConsumerThread extends Thread
    {

        private BufferedReader _bufferedReader;

        protected ConsumerThread(InputStream stream)
        {
            _bufferedReader = new BufferedReader(new InputStreamReader(stream));
        }

        public void run()
        {
            try {

                String line = _bufferedReader.readLine();
                while (line != null) {

                    if (_commandListener != null) {
                        consume(line);
                    }

                    line = _bufferedReader.readLine();
                }

            } catch (IOException e) {
                // Do nothing on IOExceptions.
                // One example of an IOException, is when
                // the command is killed, in which case, we get a IOException
                // with
                // message : Bad file descriptor

                // Log.info( this,
                // "Unhandled IOException while consuming command output"
                // + e.getClass().getName() + " : " + e.getMessage() );
            }
        }

        public abstract void consume(String line);

    }

}

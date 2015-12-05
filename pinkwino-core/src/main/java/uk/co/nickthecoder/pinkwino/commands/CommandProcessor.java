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

import java.text.DateFormat;
import java.util.Date;

import uk.co.nickthecoder.pinkwino.WikiPage;

public class CommandProcessor extends BatchProcessor implements CommandListener
{

    private StringBuffer _stdOut;

    private StringBuffer _stdErr;

    private boolean _combinedStdOutErr;

    public CommandProcessor(Command command)
    {
        this(command, null);
    }

    public CommandProcessor(Command command, WikiPage wikiPage)
    {
        this(command, wikiPage, true);
    }

    public CommandProcessor(Command command, WikiPage logPage, boolean combinedStdOutErr)
    {
        super(command, logPage);

        command.setCommandListener(this);

        _stdOut = new StringBuffer();
        _stdErr = new StringBuffer();

        _combinedStdOutErr = combinedStdOutErr;

        _stdOut.append(DateFormat.getDateTimeInstance().format(new Date())).append("\n");
        _stdOut.append("Started command : \n").append(command).append("\n\n");

    }

    public StringBuffer getStdOut()
    {
        return _stdOut;
    }

    public StringBuffer getStdErr()
    {
        return _stdErr;
    }

    public void eatStdOut(String line)
    {
        _stdOut.append(line).append("\n");
    }

    public void eatStdErr(String line)
    {
        if (_combinedStdOutErr) {
            _stdOut.append(line).append("\n");
        } else {
            _stdErr.append(line).append("\n");
        }
    }

    public void finished(int exitStatus)
    {
        _stdOut.append("\nCommand complete. Exit status = ").append(exitStatus).append("\n");
    }

    public void update()
    {
        super.update();
        String results = _combinedStdOutErr ? _stdOut.toString() : _stdOut.toString() + _stdErr.toString();
        updateResults(results);
    }

}

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

package uk.co.nickthecoder.pinkwino.plugins;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.nickthecoder.pinkwino.WikiPage;
import uk.co.nickthecoder.pinkwino.commands.Command;
import uk.co.nickthecoder.pinkwino.commands.CommandProcessor;

public class MakeStaticHelper extends CommandProcessor
{

    protected static Logger _logger = LogManager.getLogger(MakeStaticHelper.class);

    private File _directory;

    private String _oldBaseUrl;

    /**
     * This is the base url for the new web site - it is used for any absolute
     * url's. It is a shame that this is needed, however, when using PayPal, we
     * need to give them a complete url for the cancel and return addresses, and
     * wget cannot magically convert this for us.
     */
    private String _newBaseUrl;

    private Pattern _pattern;

    private String _replacement;

    private static Command makeCommand(String wget, File directory, String startUrl)
    {
        String[] commandArray = new String[] { wget, "--no-verbose", "--recursive", "-e", "robots=off",
                        "--page-requisites", "--html-extension", "--convert-links", startUrl };
        // Note, I used to use : "--convert-links", then took it out, and now
        // put it back.
        // Its needed to convert to relative urls, which is vital.
        // However, wget may have a bug, where the escaped colons were being
        // unescaped, which caused namespace qualified links
        // not to work (as the href would end up being something like this :
        // namespace:pagename, and not namespace%3Apagename
        // Note that it in addition to --convert-links, this code also replaces
        // absolute references to the source web site
        // into absolute references to the destination web site if oldBaseUrl is
        // specified in the constructor.
        // This is still needed for places where wget fails to place, such as
        // the post data of Pay-Pal payments.

        Command command = new Command();
        command.setCommandArray(commandArray);
        command.setWorkingDirectory(directory);

        return command;
    }

    public MakeStaticHelper(String wget, File directory, String startUrl, String newBaseUrl, String oldBaseUrl,
                    WikiPage logPage)
    {
        super(makeCommand(wget, directory, startUrl), logPage);

        _directory = directory;
        _newBaseUrl = newBaseUrl;
        _oldBaseUrl = oldBaseUrl;

        _pattern = Pattern.compile("\"" + _oldBaseUrl + "(.*)\"");
        _replacement = "\"" + _newBaseUrl + "$1.html\"";

    }

    public void makeStaticSite()
    {

        start();

    }

    public void finished(int exitStatus)
    {
        super.finished(exitStatus);

        if (exitStatus == 0) {
            replaceDirectoryUrls(_directory);
        }

    }

    public File getDirectory()
    {
        return _directory;
    }

    /**
     * Looks for all html files in the directory, and recurses down into sub
     * directories using recursion.
     */
    private void replaceDirectoryUrls(File directory)
    {
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (int i = 0; i < files.length; i++) {
            File file = files[i];

            if (file.isDirectory()) {
                replaceDirectoryUrls(file);
            } else {
                if (file.getName().endsWith(".html")) {
                    replaceFileUrls(file);
                }
            }
        }
    }

    private void replaceFileUrls(File file)
    {
        try {

            getStdOut().append("Post processing : ").append(file.toString()).append("\n");

            String contents = readFile(file);
            String newContents = processContents(contents);
            if (newContents != contents) {

                getStdOut().append("Replacing file : ").append(file.toString()).append("\n");
                saveFile(file, newContents);
            }

        } catch (Exception e) {
            _logger.error("Failed to post process file");
            _logger.error(e);
        }
    }

    private String readFile(File file) throws Exception
    {
        StringBuffer buffer = new StringBuffer();
        LineNumberReader reader = new LineNumberReader(new InputStreamReader(new FileInputStream(file)));

        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line).append("\n");
        }

        reader.close();
        return buffer.toString();

    }

    private void saveFile(File file, String contents) throws Exception
    {
        Writer writer = new OutputStreamWriter(new FileOutputStream(file));
        writer.write(contents);
        writer.close();
    }

    protected String processContents(String contents)
    {
        Matcher matcher = _pattern.matcher(contents);
        if (contents.indexOf(_oldBaseUrl) != -1) {
            contents = matcher.replaceAll(_replacement);
        }

        return contents;
    }

}

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

package uk.co.nickthecoder.pinkwino.optional.image;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.nickthecoder.pinkwino.util.Parameters;

/**
 * Displays an image, with the option of floating the element to either side of
 * the page, with a caption, and a link when the image is clicked.
 */

public class Resize extends AbstractImageTransform
{
    protected static Logger _logger = LogManager.getLogger(Resize.class);

    private int _quality;

    private int _requiredWidth;

    private int _requiredHeight;

    private String _convertPath = "/usr/bin/convert";

    /**
     * Sets the location of the image magik 'convert' command. The default is
     * /usr/bin/convert.
     *
     * @param convertPath
     *            An absolute path string.
     * @return this (to allow method chaining).
     */
    public Resize _convertPath(String convertPath)
    {
        _convertPath = convertPath;
        return this;
    }

    public Resize(int requiredWidth, int requiredHeight)
    {
        this(requiredWidth, requiredHeight, 80);
    }

    public Resize(int requiredWidth, int requiredHeight, int quality)
    {
        _requiredWidth = requiredWidth;
        _requiredHeight = requiredHeight;
        _quality = quality;
    }

    @Override
    public long getDependentDate()
    {
        return 0;
    }

    @Override
    public void transform(File input, File output, Parameters parameters)
    {
        _logger.trace("Resizing  : " + input);
        String[] command = { _convertPath, "-quality", Integer.toString(_quality), "-thumbnail",
                        _requiredWidth + "x" + _requiredHeight, input.getAbsolutePath(), output.getAbsolutePath() };

        _logger.trace("Resize command : " + command);

        try {
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
        } catch (IOException e) {
            _logger.error("Failed to resize image : " + e);
        } catch (InterruptedException e) {
            // Do nothing
        }
    }

}

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

import uk.co.nickthecoder.pinkwino.util.Parameters;

/**
 * Displays an image, with the option of floating the element to either side of
 * the page, with a caption, and a link when the image is clicked.
 */

public class Resize extends AbstractImageTransform
{

    private int _requiredWidth;

    private int _requiredHeight;

    public Resize(int requiredWidth, int requiredHeight)
    {
        _requiredWidth = requiredWidth;
        _requiredHeight = requiredHeight;
    }

    public long getDependentDate()
    {
        return 0;
    }

    public void transform(File input, File output, Parameters parameters)
    {
        // TODO Copy from gidea web app
    }

}

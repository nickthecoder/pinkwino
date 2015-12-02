/* {{{ GPL

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

}}} */

package uk.co.nickthecoder.pinkwino.optional.image;

import java.io.File;

import uk.co.nickthecoder.pinkwino.util.ParameterDescriptions;
import uk.co.nickthecoder.pinkwino.util.Parameters;

/**
 * Displays an image, with the option of floating the element to either side of
 * the page, with a caption, and a link when the image is clicked.
 */

public interface ImageTransform
{
    public void transform(File input, File output, Parameters parameters) throws Exception;

    /**
     * Returns a the latest date that this transform has as a dependency, or
     * zero if it has no time constraints. If the ImageTransform is based on one
     * or more images for example, it needs to return the maximum of the last
     * modified dates of those images.
     */
    public long getDependentDate();

    /**
     * If this image transform takes parameters, this returns the parameters it
     * expects. If it doesn't require parameters, return an empty
     * ParameterDescriptions object, do NOT return null.
     */
    public ParameterDescriptions getParameterDescriptions();

}

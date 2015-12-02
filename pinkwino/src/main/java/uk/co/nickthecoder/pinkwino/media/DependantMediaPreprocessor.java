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

package uk.co.nickthecoder.pinkwino.media;

import java.io.InputStream;
import java.util.Date;

import uk.co.nickthecoder.pinkwino.Version;
import uk.co.nickthecoder.pinkwino.WikiPage;
import uk.co.nickthecoder.pinkwino.util.ParameterDescriptions;
import uk.co.nickthecoder.pinkwino.util.Parameters;

/**
 * Only processes the image if the dependant page was last modified before the
 * main page (or if the dependant page doesn't exist).
 * 
 * Also, this class has an optional baseDate. If set, then the media will
 * processed if the dependant page was modified before the baseDate regardless
 * of the main page's date. This can be used when a MediaProcessor has been
 * changed, and all cached versions are no loger valid, and need to be
 * regenerated. For example, if this is used to create thumbnail images, and the
 * required size has been changed.
 */

public abstract class DependantMediaPreprocessor implements MediaPreprocessor
{

    private long _baseDate;

    public DependantMediaPreprocessor()
    {
        this(null);
    }

    public DependantMediaPreprocessor(Date baseDate)
    {
        if (baseDate == null) {
            _baseDate = 0;
        } else {
            _baseDate = baseDate.getTime();
        }

    }

    public InputStream process(Version destinationVersion, Parameters parameters)
    {
        // We only process the current version.
        if (destinationVersion.getWikiPage().getCurrentVersion() != destinationVersion) {
            return null;
        }

        WikiPage destinationPage = destinationVersion.getWikiPage();
        WikiPage sourcePage = destinationPage.getMainPage();

        if (sourcePage.getExists()) {

            long dependentDate = Math.max(getDependentDate(),
                            Math.max(_baseDate, sourcePage.getLastModified().getTime()));

            if ((!destinationPage.getExists()) || (destinationPage.getLastModified().getTime() < dependentDate)) {

                return processMedia(sourcePage, destinationPage, parameters);

            }
        }

        return null;
    }

    public abstract InputStream processMedia(WikiPage sourcePage, WikiPage destinationPage, Parameters parameters);

    public abstract ParameterDescriptions getParameterDescriptions();

    public abstract MimeType getMimeType();

    public long getDependentDate()
    {
        return 0;
    }

}

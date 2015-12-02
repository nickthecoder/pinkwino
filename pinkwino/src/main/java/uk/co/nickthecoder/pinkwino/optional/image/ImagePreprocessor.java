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
import java.io.InputStream;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.WikiPage;
import uk.co.nickthecoder.pinkwino.media.DependantMediaPreprocessor;
import uk.co.nickthecoder.pinkwino.media.MimeType;
import uk.co.nickthecoder.pinkwino.util.ParameterDescriptions;
import uk.co.nickthecoder.pinkwino.util.Parameters;

public class ImagePreprocessor extends DependantMediaPreprocessor
{

    protected static Logger _logger = LogManager.getLogger(ImagePreprocessor.class);

    private ImageTransform _imageTransform;

    private MimeType _mimeType;

    public ImagePreprocessor(Date baseDate, ImageTransform imageTransform)
    {
        this(baseDate, imageTransform, WikiEngine.instance().getMediaManager().getMimeTypeByExtension("jpeg"));
        _logger.info("Using default mime type ");
        _logger.info(":" + WikiEngine.instance().getMediaManager().getMimeTypeByExtension("jpeg"));
    }

    public ImagePreprocessor(Date baseDate, ImageTransform imageTransform, MimeType mimeType)
    {
        super(baseDate);
        _imageTransform = imageTransform;
        _mimeType = mimeType;
        _logger.info("Using mime type " + _mimeType);
    }

    public ImagePreprocessor(ImageTransform imageTransform)
    {
        super();
        _imageTransform = imageTransform;
        _logger.info("Using default mime type2 ");
        _logger.info(":" + WikiEngine.instance().getMediaManager().getMimeTypeByExtension("jpeg"));
        _mimeType = WikiEngine.instance().getMediaManager().getMimeTypeByExtension("jpeg");
    }

    public ParameterDescriptions getParameterDescriptions()
    {
        return _imageTransform.getParameterDescriptions();
    }

    public MimeType getMimeType()
    {
        return _mimeType;
    }

    // {{{ processMedia
    public InputStream processMedia(WikiPage sourcePage, WikiPage destinationPage, Parameters parameters)
    {
        _logger.info("Mimetype = " + _mimeType);

        String mimeType = _mimeType.getMimeType();

        String suffix = null;
        if ("image/jpeg".equals(mimeType)) {
            suffix = ".jpg";
        } else if ("image/png".equals(mimeType)) {
            suffix = ".png";
        }

        if (suffix != null) {

            File tmpFile = null;
            try {

                tmpFile = File.createTempFile(this.getClass().getName(), "-tmpimage" + suffix);
                // Source = sourcePage
                // Destination = tmpFile

                File sourceFile = sourcePage.getWikiStorage().getMediaFile(sourcePage.getCurrentVersion());

                this.processImage(sourceFile, tmpFile, parameters);
                // String message =
                // destinationPage.getCurrentVersion().getContent();
                String message = "Auto generated by ImagePreprocessor";

                // Don't save directly, do it through WikiEngine
                // destinationPage.getWikiStorage().save( destinationPage,
                // message, tmpFile );
                WikiEngine.instance().save(destinationPage, message, tmpFile);

                return destinationPage.getWikiStorage().getMedia(destinationPage.getCurrentVersion());

            } catch (Exception e) {

                _logger.error("ImageProcessor failed");
                _logger.error(e);
                e.printStackTrace();
                throw new RuntimeException(e);

            } finally {

                if (tmpFile != null) {
                    tmpFile.delete();
                }
            }

        }

        return null;
    }

    protected void processImage(File input, File output, Parameters parameters) throws Exception
    {
        _imageTransform.transform(input, output, parameters);
    }

    public long getDependentDate()
    {
        return _imageTransform.getDependentDate();
    }
}

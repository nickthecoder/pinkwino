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

package uk.co.nickthecoder.pinkwino.optional.publish;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.nickthecoder.pinkwino.WikiName;
import uk.co.nickthecoder.pinkwino.WikiPage;
import uk.co.nickthecoder.pinkwino.plugins.MakeStaticPlugin;

public class PublishPlugin extends MakeStaticPlugin
{
    protected static Logger _logger = LogManager.getLogger(FtpSync.class);

    private FtpDetails _ftpDetails;

    public PublishPlugin(File directory, WikiName startPageName, String newBaseUrl, FtpDetails ftpDetails)
    {
        super(MakeStaticPlugin._defaultWgetPath, "publish", "makeStatic.jsp", directory, startPageName, newBaseUrl);
        _ftpDetails = ftpDetails;
    }

    public PublishPlugin(String wget, File directory, WikiName startPageName, String newBaseUrl, FtpDetails ftpDetails)
    {
        super(wget, "publish", "makeStatic.jsp", directory, startPageName, newBaseUrl);
        _ftpDetails = ftpDetails;
    }

    public PublishPlugin(String wget, String name, String jspPath, File directory, WikiName startPageName,
                    String newBaseUrl, FtpDetails ftpDetails)
    {
        super(wget, name, jspPath, directory, startPageName, newBaseUrl);
        _ftpDetails = ftpDetails;
    }

    protected void makeStaticSite(WikiPage logPage)
    {
        super.makeStaticSite(logPage);

        publishSite(logPage);
    }

    protected void publishSite(WikiPage logPage)
    {
        _logger.info("publishSite Begin");

        try {
            FtpSync ftpSync = new FtpSync(_ftpDetails);
            ftpSync.upload();
        } catch (Exception e) {
            _logger.error("FTP Upload failed. " + e);
        }

        _logger.info("publishSite Complete");
    }

}

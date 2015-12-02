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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.nickthecoder.pinkwino.WikiName;
import uk.co.nickthecoder.pinkwino.WikiPage;

public class MakeStaticUploadPlugin extends MakeStaticPlugin
{

    protected static Logger _logger = LogManager.getLogger(MakeStaticUploadPlugin.class);

    public MakeStaticUploadPlugin(File directory, WikiName startPageName, String newBaseUrl)
    {
        this("/usr/bin/wget", directory, startPageName, newBaseUrl);
    }

    public MakeStaticUploadPlugin(String wget, File directory, WikiName startPageName, String newBaseUrl)
    {
        this(wget, "makeStatic", "makeStatic.jsp", directory, startPageName, newBaseUrl);
    }

    public MakeStaticUploadPlugin(String wget, String name, String jspPath, File directory, WikiName startPageName,
                    String newBaseUrl)
    {
        super(wget, name, jspPath, directory, startPageName, newBaseUrl);
    }


    protected MakeStaticHelper createMakeStaticHelper(String startUrl, WikiPage logPage)
    {
        MakeStaticHelper msh = new MakeStaticHelper(_wget, _directory, startUrl, _newBaseUrl, _oldBaseUrl, logPage);
        return msh;
    }

}


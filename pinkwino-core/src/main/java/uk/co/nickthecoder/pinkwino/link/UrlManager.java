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

package uk.co.nickthecoder.pinkwino.link;

import java.util.Map;

import uk.co.nickthecoder.pinkwino.Version;
import uk.co.nickthecoder.pinkwino.WikiName;
import uk.co.nickthecoder.pinkwino.WikiPage;

/**
 * Defines the urls for the various functions you can do to a wiki.
 */

public interface UrlManager
{

    public static final String URL_TYPE_VIEW = "view";
    public static final String URL_TYPE_EDIT = "edit";
    public static final String URL_TYPE_DIFF = "diff";
    public static final String URL_TYPE_RENAME = "rename";
    public static final String URL_TYPE_REVERT = "revert";
    public static final String URL_TYPE_INFO = "info";
    public static final String URL_TYPE_EDIT_SECTION = "editSection";
    public static final String URL_TYPE_SAVE = "save";
    public static final String URL_TYPE_RAW = "raw";
    public static final String URL_TYPE_ERROR = "error";
    public static final String URL_TYPE_MEDIA = "media";
    public static final String URL_TYPE_DELETE = "delete";
    public static final String URL_TYPE_LOGIN = "login";
    public static final String URL_TYPE_LOGOUT = "logout";

    public String getErrorUrl();

    /**
     * Returns a map of all parameterless urls, such as login, logout. it is
     * also ok if the parameterised urls are also in the Map.
     */
    public Map<String,String> getUrls();

    public String getUrl(String type);

    public String getUrl(String type, WikiName wikiName);

    public String getUrl(String type, WikiPage wikiPage);

    public String getUrl(String type, Version version);

    public String getEditSectionUrl(WikiPage wikiPage, String section);

    public String getTemplate();

    /**
     * Useful for finding resources which are within the template's directory.
     * 
     * @return the path to the template's parent directory excluing the hostname
     *         and context path.
     */
    public String getTemplateDirectory();

    public WikiName decodePageParameter(String pageParameter);

}


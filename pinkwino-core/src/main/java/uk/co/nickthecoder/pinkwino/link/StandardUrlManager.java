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

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.nickthecoder.pinkwino.Version;
import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.WikiName;
import uk.co.nickthecoder.pinkwino.WikiPage;

public class StandardUrlManager implements UrlManager
{

    public static final String DEFAULT_TEMPLATE = "/templates/default/template.jsp";

    protected static Logger _logger = LogManager.getLogger(StandardUrlManager.class);

    /**
     * The name of the jsp tiles template.
     */
    private String _template;

    private String _templateDirectory;

    private String _errorUrl;

    private Map<String, String> _baseUrls;

    /**
     * Initially all urls are set to the errorUrl, use the set methods to assign
     * each url appropriately.
     */
    public StandardUrlManager(String errorUrl)
    {
        _errorUrl = errorUrl;

        _template = null;
        _templateDirectory = null;
        setTemplate(DEFAULT_TEMPLATE);

        _baseUrls = new HashMap<String, String>();
        _baseUrls.put(URL_TYPE_ERROR, _errorUrl);
    }

    public Map<String, String> getUrls()
    {
        return _baseUrls;
    }

    public void addUrl(String type, String url)
    {
        _baseUrls.put(type, url);
    }

    public String getUrl(String type)
    {
        String result = (String) _baseUrls.get(type);

        if (result == null) {
            return _errorUrl;
        }

        return result;
    }

    public String getUrl(String type, WikiPage wikiPage)
    {
        return getUrl(type, wikiPage.getWikiName());
    }

    public String getUrl(String type, WikiName wikiName)
    {
        String result = replace(getUrl(type), wikiName);
        if ((type.equals(URL_TYPE_MEDIA)) && (wikiName.getMediaPreprocessor() != null)
                        && (wikiName.getMediaPreprocessor().getMimeType() != null)) {
            result = result + "." + wikiName.getMediaPreprocessor().getMimeType().getExtension();
        }
        return result;
    }

    public String getUrl(String type, Version version)
    {
        String pageUrl = getUrl(type, version.getWikiPage());
        if (version.getVersionNumber() > 0) {
            String versionParameter = getVersionParameter(version);

            if (pageUrl.indexOf("?") >= 0) {
                return pageUrl + ";" + versionParameter;
            } else {
                return pageUrl + "?" + versionParameter;
            }
        }

        return pageUrl;
    }

    public String getEditSectionUrl(WikiPage wikiPage, String section)
    {
        String editUrl = getUrl(URL_TYPE_EDIT, wikiPage);
        if (editUrl.indexOf("?") >= 0) {
            return editUrl + ";" + "section=" + uk.co.nickthecoder.webwidgets.util.TagUtil.encodeUrl(section);
        } else {
            return editUrl + "?" + "section=" + uk.co.nickthecoder.webwidgets.util.TagUtil.encodeUrl(section);
        }
    }

    /**
     * Get method for attribute {@link #_template}. The name of the jsp tiles
     * template.
     */
    public String getTemplate()
    {
        return _template;
    }

    /**
     * Get method for attribute {@link #_template}. The name of the jsp tiles
     * template.
     */
    public String getTemplateDirectory()
    {
        return _templateDirectory;
    }

    /**
     * Set method for attribute {@link #_template}. The name of the jsp tiles
     * template.
     */
    public final void setTemplate(String value)
    {
        _template = value;
        int lastSlash = _template.lastIndexOf('/');
        if (lastSlash > 0) {
            _templateDirectory = _template.substring(0, lastSlash);
        } else {
            _templateDirectory = "/";
        }
    }

    public void setErrorUrl(String value)
    {
        _errorUrl = value;
    }

    public String getErrorUrl()
    {
        return _errorUrl;
    }

    protected String replace(String url, WikiName wikiName)
    {
        String path = WikiEngine.instance().getWikiNameFormat().format(wikiName);

        String result = url;
        result = result.replaceAll("_PATH_", uk.co.nickthecoder.webwidgets.util.TagUtil.encodePath(path));
        result = result.replaceAll("_", "%5F"); // If a name uses an underscore,
                                                // then use its escaped ascii
                                                // code
        result = result.replaceAll("\\+", "_"); // Use underscores to represent
                                                // spaces
        result = result.replaceAll("%2F", "/"); // Unescape the slash between
                                                // namespace and title
        result = result.replaceAll("%1", uk.co.nickthecoder.webwidgets.util.TagUtil.encodeUrl(path));

        return result;
    }

    protected String getVersionParameter(Version version)
    {
        return "version=" + version.getVersionNumber();
    }

    public WikiName decodePageParameter(String pageParameter)
    {
        pageParameter = pageParameter.replaceAll("_", " ");
        pageParameter = pageParameter.replaceAll("%5F", "_");

        return WikiEngine.instance().getWikiNameFormat().parse(pageParameter);
    }

}

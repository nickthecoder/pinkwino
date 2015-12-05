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

package uk.co.nickthecoder.pinkwino;

import java.io.File;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.nickthecoder.pinkwino.link.ExternalLinkManager;
import uk.co.nickthecoder.pinkwino.link.StandardExternalLinkManager;
import uk.co.nickthecoder.pinkwino.link.StandardWikiNameFormat;
import uk.co.nickthecoder.pinkwino.link.UrlManager;
import uk.co.nickthecoder.pinkwino.link.WikiNameFormat;
import uk.co.nickthecoder.pinkwino.media.MediaManager;
import uk.co.nickthecoder.pinkwino.media.StandardMediaManager;
import uk.co.nickthecoder.pinkwino.metadata.MetaData;
import uk.co.nickthecoder.pinkwino.metadata.NullMetaData;
import uk.co.nickthecoder.pinkwino.parser.Renderer;
import uk.co.nickthecoder.pinkwino.parser.StandardRenderer;
import uk.co.nickthecoder.pinkwino.parser.SyntaxManager;
import uk.co.nickthecoder.pinkwino.plugins.PluginManager;
import uk.co.nickthecoder.pinkwino.plugins.StandardPluginManager;
import uk.co.nickthecoder.pinkwino.security.AuthenticationManager;
import uk.co.nickthecoder.pinkwino.security.AuthorisationManager;
import uk.co.nickthecoder.pinkwino.security.NullAuthenticationManager;
import uk.co.nickthecoder.pinkwino.security.NullAuthorisationManager;
import uk.co.nickthecoder.pinkwino.storage.NullStorage;
import uk.co.nickthecoder.pinkwino.storage.SaveFilter;
import uk.co.nickthecoder.pinkwino.storage.SecureStorage;
import uk.co.nickthecoder.pinkwino.util.CompoundAttributes;
import uk.co.nickthecoder.pinkwino.util.SimpleAttributes;

/**
 * A singleton which is the top-level structure for all wiki related classes
 */

public class WikiEngine
{
    /**
     * Singleton design pattern. <br>
     * <br>
     * See Gamma, Helm, Johnson, Vlissides. Design Patterns, pages 127-134.
     * Addison-Wesley
     */
    private static WikiEngine _instance;

    protected static Logger _logger = LogManager.getLogger(WikiEngine.class);

    /**
     * The name of the servlet parameter which holds the name of the groovy
     * script (for initialising the Wiki Engine)
     */
    public static final String INIT_SCRIPT_PARAMETER = "pinkwino.initscript";

    /**
     * If no value is give by the INIT_SCRIPT_PARAMETER, then this default init
     * script name is used.
     */
    public static final String DEFAULT_GROOVY_SCRIPT = "pinkwino.groovy";

    private String _server;

    private String _contextPath;

    /**
     * Holds information about mime types and file extensions
     */
    private MediaManager _mediaManager;

    /**
     * Determines what are external links, and builds the full url.
     */
    private ExternalLinkManager _externalLinkManager;

    /**
     * Holds the imformation about how wiki markup is parsed
     */
    private SyntaxManager _syntaxManager;

    /**
     * Defines the urls for each action you can perform on a wiki
     */
    private UrlManager _urlManager;

    /**
     * Determines if a given user is allowed to edit a given page.
     */
    private AuthorisationManager _authorisationManager;

    /**
     * In charge of telling one person from another, holds passwords etc/
     */
    private AuthenticationManager _authenticationManager;

    /**
     * Creates WikiName objects from their string representations
     */
    private WikiNameFormat _wikiNameFormat;

    /**
     * Determines how wiki syntax is converted into html
     */
    private Renderer _renderer;

    /**
     * A collection of Namespace objects keyed by the name of the namespace
     */
    private HashMap<String, Namespace> _namespaces;

    /**
     * Pages within this namepsace do not need to be qualified.
     */
    private Namespace _defaultNamespace;

    /**
     * The namespace which holds all registered users - used by
     * pinkwino.security
     */
    private Namespace _userNamespace;

    /**
     * The namespace which holds messages (usually the "wiki" namespace).
     */
    private Namespace _messageNamespace;

    /**
     * A namespace which is used whenever a namespace name is used which does
     * not exist in the _namespaces, the hashmap of all valid namespaces.
     */
    private Namespace _unknownNamespace;

    private PluginManager _pluginManager;

    private MetaData _metaData;

    private String _defaultPageName;

    private CompoundAttributes _attributes;

    private String _characterSet;

    private Charset _charset;

    /*
     * A list of filters which are checked before a page is saved. If any
     * filters return an error message, then the page won't be saved.
     */
    private List<SaveFilter> _saveFilters;

    /**
     * Singleton design pattern. <br>
     * <br>
     * See Gamma, Helm, Johnson, Vlissides. Design Patterns, pages 127-134.
     * Addison-Wesley
     */
    public synchronized static WikiEngine instance()
    {
        if (_instance == null) {
            _instance = new WikiEngine();

            _logger.warn("Warning, the wiki engine is being initialised without a ServletContext.");
            _logger.warn("Therefore, parameters within the web.xml are being ignored.");
            _logger.warn("To avoid this, ensure that you have the following in your web.xml :");
            _logger.warn("  <listener>");
            _logger.warn("    <listener-class>uk.co.nickthecoder.pinkwino.WikiServletContextListener</listener-class>");
            _logger.warn("  </listener>");

            RunGroovy.fromClasspath(DEFAULT_GROOVY_SCRIPT);
        }

        return _instance;
    }

    /**
     * Used by the unit tests, so that they can be run outside of servlet
     * container. Never call this method within a production environment.
     */
    public synchronized static void initialise(String initScript)
    {
        _instance = new WikiEngine();
        RunGroovy.fromFile(initScript);
    }

    public synchronized static void initialise(ServletContext servletContext)
    {
        if (_instance != null) {
            throw new RuntimeException("WikiEngine.initialise : The wiki engine has already been initialised");
        }

        _logger.info("Initialising pinkwino wiki engine");
        _instance = new WikiEngine();
        _instance.setContextPath(servletContext.getContextPath());        
        String initScript = servletContext.getInitParameter(INIT_SCRIPT_PARAMETER);

        if (initScript == null) {
            _logger.warn("init script not specified. Loading : " + DEFAULT_GROOVY_SCRIPT + " from the classpath");
            RunGroovy.fromClasspath(DEFAULT_GROOVY_SCRIPT);
        } else {
            _logger.info("Loading init script from file : " + initScript);
            RunGroovy.fromFile(initScript);
        }

    }

    /**
     * Constructor must be protected to ensure that the singleton pattern is not
     * broken.
     */
    protected WikiEngine()
    {
        _wikiNameFormat = new StandardWikiNameFormat();
        _renderer = new StandardRenderer();
        _namespaces = new HashMap<String, Namespace>();
        _pluginManager = new StandardPluginManager();
        _externalLinkManager = new StandardExternalLinkManager();
        _mediaManager = new StandardMediaManager();
        _metaData = new NullMetaData();

        _saveFilters = new ArrayList<SaveFilter>();

        _authenticationManager = new NullAuthenticationManager();
        _authorisationManager = new NullAuthorisationManager();

        _unknownNamespace = new Namespace("unknown");
        _unknownNamespace.setStorage(new NullStorage());
        _unknownNamespace.setExists(false);

        _userNamespace = null;
        _defaultNamespace = null; // This one must be set later.

        _server = "";
        _contextPath = "/pinkwino";
        _defaultPageName = "home";

        _attributes = new CompoundAttributes();
        _attributes.add(new SimpleAttributes());
        _attributes.setAttribute("wiki_backgroundColor", java.awt.Color.WHITE);

        setCharacterSet("UTF-8");
    }

    /**
     * May be needed by 3rd party services to determine the full urls. For
     * example wikiEngine.getServer() + wikiPage.getViewUrl() will return a
     * fully qualified url, but only if wikiEngine.setServer has been correctly
     * called (which should be in the bean shell initialisation.
     */
    public String getServer()
    {
        return _server;
    }

    public void setServer(String server)
    {
        _server = server;
    }

    /**
     * Get method for attribute {@link #_contextPath}.
     */
    public String getContextPath()
    {
        return _contextPath;
    }

    /**
     * Set method for attribute {@link #_contextPath}.
     */
    public void setContextPath(String value)
    {
        _contextPath = value;
        setAttribute("wiki_contextPath", value);
    }

    /**
     * Get method for attribute {@link #_characterSet}.
     */
    public String getCharacterSet()
    {
        return _characterSet;
    }

    /**
     * Set method for attribute {@link #_characterSet}.
     */
    public void setCharacterSet(String value)
    {
        _charset = Charset.forName(value);
        _characterSet = value;
        setAttribute("wiki_characterSet", value);
    }

    public Charset getCharset()
    {
        return _charset;
    }

    /**
     * Get method for attribute {@link #_mediaManager}. Holds information about
     * mime types and file extensions
     */
    public MediaManager getMediaManager()
    {
        return _mediaManager;
    }

    /**
     * Set method for attribute {@link #_mediaManager}. Holds information about
     * mime types and file extensions
     */
    public void setMediaManager(MediaManager value)
    {
        _mediaManager = value;
    }

    /**
     * Get method for attribute {@link #_externalLinkManager}. Determines what
     * are external links, and builds the full url.
     */
    public ExternalLinkManager getExternalLinkManager()
    {
        return _externalLinkManager;
    }

    /**
     * Set method for attribute {@link #_externalLinkManager}. Determines what
     * are external links, and builds the full url.
     */
    public void setExternalLinkManager(ExternalLinkManager value)
    {
        _externalLinkManager = value;
    }

    /**
     * Get method for attribute {@link #_syntaxManager}. Holds the imformation
     * about how wiki markup is parsed
     */
    public SyntaxManager getSyntaxManager()
    {
        return _syntaxManager;
    }

    /**
     * Set method for attribute {@link #_syntaxManager}. Holds the imformation
     * about how wiki markup is parsed
     */
    public void setSyntaxManager(SyntaxManager value)
    {
        _syntaxManager = value;
    }

    /**
     * Get method for attribute {@link #_urlManager}. Defines the urls for each
     * action you can perform on a wiki
     */
    public UrlManager getUrlManager()
    {
        return _urlManager;
    }

    /**
     * Set method for attribute {@link #_urlManager}. Defines the urls for each
     * action you can perform on a wiki
     */
    public void setUrlManager(UrlManager value)
    {
        _urlManager = value;
    }

    /**
     * Get method for attribute {@link #_wikiNameFormat}. Creates WikiName
     * objects from page name Strings
     */
    public WikiNameFormat getWikiNameFormat()
    {
        return _wikiNameFormat;
    }

    /**
     * Set method for attribute {@link #_wikiNameFormat}. Creates WikiName
     * objects from page name Strings
     */
    public void setWikiNameFormat(WikiNameFormat value)
    {
        _wikiNameFormat = value;
    }

    /**
     * Get method for attribute {@link #_renderer}. Determines how wiki syntax
     * is converted into html
     */
    public Renderer getRenderer()
    {
        return _renderer;
    }

    /**
     * Set method for attribute {@link #_renderer}. Determines how wiki syntax
     * is converted into html
     */
    public void setRenderer(Renderer value)
    {
        _renderer = value;
    }

    public PluginManager getPluginManager()
    {
        return _pluginManager;
    }

    public void setPluginManager(PluginManager value)
    {
        _pluginManager = value;
    }

    public MetaData getMetaData()
    {
        return _metaData;
    }

    public void setMetaData(MetaData metaData)
    {
        _metaData = metaData;
    }

    public AuthenticationManager getAuthenticationManager()
    {
        return _authenticationManager;
    }

    public void setAuthenticationManager(AuthenticationManager value)
    {
        _authenticationManager = value;
    }

    public AuthorisationManager getAuthorisationManager()
    {
        return _authorisationManager;
    }

    public void setAuthorisationManager(AuthorisationManager value)
    {
        _authorisationManager = value;
    }

    public void addNamespace(Namespace namespace)
    {
        _namespaces.put(namespace.getName(), namespace);
    }

    public Namespace getNamespace(String namespaceName)
    {
        if (namespaceName == null) {
            return getDefaultNamespace();
        }

        Namespace result = (Namespace) _namespaces.get(namespaceName);
        if (result == null) {
            return _unknownNamespace;
        }

        return result;
    }

    public Iterator<Namespace> getNamespaces()
    {
        return _namespaces.values().iterator();
    }

    public Namespace getDefaultNamespace()
    {
        return _defaultNamespace;
    }

    public void setUserNamespace(Namespace value)
    {
        _userNamespace = value;
        setAttribute("wiki_defaultNamespace", value.getName());
    }

    /**
     * The namespace used to hold messages, this is usually the "wiki"
     * namespace.
     */
    public Namespace getMessageNamespace()
    {
        return _messageNamespace;
    }

    public void setMessageNamespace(Namespace value)
    {
        _messageNamespace = value;
    }

    /**
     * The namespace used to hold registered users, can be null if there is no
     * security for this wiki.
     */
    public Namespace getUserNamespace()
    {
        return _userNamespace;
    }

    public void setDefaultNamespace(Namespace value)
    {
        _defaultNamespace = value;
        setAttribute("wiki_userNamespace", value.getName());
    }

    public String getDefaultPageName()
    {
        return _defaultPageName;
    }

    public void setDefaultPageName(String value)
    {
        _defaultPageName = value;
        setAttribute("wiki_defaultPageName", value);
    }

    public WikiPage getDefaultWikiPage()
    {
        return getWikiPage(getDefaultPageName());
    }

    public void addSaveFilter(SaveFilter saveFilter)
    {
        _saveFilters.add(saveFilter);
    }

    /**
     * If you want to restrict who can edit pages, you should be call this at
     * the end of the initialisation script. As well as begin a convienience for
     * setUserNamespace, setAuthenticationManager and setAuthorisationManager,
     * it also adds a SecureStorage wrapper around each namespace's Storage.
     * WARNING : If you call this BEFORE the namespaces' storages are set up,
     * then they won't end up being secured.
     */
    public void enableSecurity(Namespace userNamespace, AuthenticationManager authenticationManager,
                    AuthorisationManager authorisationManager)
    {
        setUserNamespace(userNamespace);
        setAuthenticationManager(authenticationManager);
        setAuthorisationManager(authorisationManager);

        for (Iterator<Namespace> i = getNamespaces(); i.hasNext();) {
            Namespace namespace = i.next();
            namespace.setStorage(new SecureStorage(namespace.getStorage()));
        }
    }

    public boolean canEdit(WikiPage wikiPage)
    {
        if (wikiPage.getNamespace().getStorage().isReadOnly()) {
            return false;
        }

        return getAuthorisationManager().canEdit(getAuthenticationManager().getUser(), wikiPage);
    }

    public boolean canDelete(WikiPage wikiPage)
    {
        if (wikiPage.getNamespace().getStorage().isReadOnly()) {
            return false;
        }

        return getAuthorisationManager().canDelete(getAuthenticationManager().getUser(), wikiPage);
    }

    /**
     * Returns all pages with the wiki
     */
    public List<WikiPage> getPages()
    {
        List<WikiPage> result = new ArrayList<WikiPage>();
        for (Iterator<Namespace> i = getNamespaces(); i.hasNext();) {
            Namespace namespace = i.next();

            result.addAll(namespace.getPages());
        }

        return result;
    }

    // }}}

    // {{{ getRecentChanges
    /**
     * Returns pages that have been modified since xxx
     */
    public List<WikiPage> getRecentChanges(Date since)
    {
        List<WikiPage> result = new ArrayList<WikiPage>();
        for (Iterator<Namespace> i = getNamespaces(); i.hasNext();) {
            Namespace namespace = i.next();

            result.addAll(namespace.getRecentChanges(since));
        }

        return result;
    }

    /**
     * Gets the wiki context for the current request.
     */
    public WikiContext getWikiContext()
    {
        return WikiContext.getWikiContext();
    }

    /*
     * // {{{ beginWikiInstance public void beginWikiInstance(
     * HttpServletRequest request, HttpServletResponse response ) {
     * WikiContext.begin( request, response ); } // }}}
     * 
     * // {{{ endWikiInstance public void endWikiInstance( HttpServletRequest
     * request, HttpServletResponse response ) { WikiContext.end( request,
     * response ); } // }}}
     */

    public void save(WikiPage wikiPage, String markup, File mediaFile)
    {
        // Check that the file can be saved - if any of the filters return
        // false, then it will not be saved.
        for (Iterator<SaveFilter> i = _saveFilters.iterator(); i.hasNext();) {
            SaveFilter saveFilter = i.next();

            String message = saveFilter.filter(wikiPage, markup, mediaFile);
            if (message != null) {
                throw new UserMessageException(message);
            }
        }

        // Ok, we can save it, lets move on.
        wikiPage.getNamespace().getStorage().save(wikiPage, markup, mediaFile);
        try {
            getMetaData().update(wikiPage);
        } catch (Exception e) {
            _logger.error("Failed to update metadata: " + e.toString());
            e.printStackTrace();
        }
    }

    public void saveSection(WikiPage wikiPage, String sectionId, String markup)
    {

        Version version = wikiPage.getCurrentVersion();
        WikiDocument wikiDocument = WikiEngine.instance().getRenderer().parse(version);
        ;
        String content = version.getContent();

        String message = "The section you edited has changed since you started editing.";

        Section section = wikiDocument.getSection(sectionId);
        if (section == null) {
            throw new UserMessageException(message);
        }

        // Arrange the three pieces together (the existing part before the
        // section,
        // the new part, and the existing part after the section).

        StringBuffer buffer = new StringBuffer();
        try {

            LineNumberReader reader = new LineNumberReader(new StringReader(content));
            int i;
            for (i = 1; i < section.getStartLineNumber(); i++) {
                String line = reader.readLine();
                if (line == null) {
                    throw new UserMessageException(message);
                }
                buffer.append(line).append("\n");
            }

            buffer.append(markup);

            if (!markup.endsWith("\n")) {
                buffer.append("\n");
            }

            // If there is no end line number, it means it goes on till the end
            // of the document
            // so we don't need to bother (as there is no remainder)
            if (section.getEndLineNumber() >= 0) {

                for (int j = i; j <= section.getEndLineNumber(); j++) {
                    String line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                }

                while (true) {

                    String line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    buffer.append(line).append("\n");
                }

            }

        } catch (IOException e) {
            // I don't believe this will ever happen.
            throw new RuntimeException(e);
        }

        save(wikiPage, buffer.toString(), null);
    }

    public void delete(WikiPage wikiPage)
    {
        wikiPage.getNamespace().getStorage().delete(wikiPage);

        try {
            getMetaData().remove(wikiPage.getWikiName());
        } catch (Exception e) {
            _logger.error("Failed to remove metadata: " + e.toString());
            e.printStackTrace();
        }
    }

    public String getMessage(String messageName)
    {
        WikiName wikiName = new WikiName(getMessageNamespace(), messageName);
        WikiPage messagePage = getWikiPage(wikiName);

        return messagePage.getCurrentVersion().getContent();
    }

    /**
     * Returns the WikiPage for the specified page name. If the page does not
     * exist, then null is NOT returned, instead, a WikiPage instance is
     * created, which has the correct title, but no content.
     */
    public WikiPage getWikiPage(String pageName)
    {
        WikiName wikiName = _wikiNameFormat.parse(pageName);
        return new WikiPage(wikiName);
    }

    /**
     * Returns the WikiName for the specified page name.
     */
    public WikiName getWikiName(String pageName)
    {
        return _wikiNameFormat.parse(pageName);
    }

    /**
     * Returns the WikiPage for the specified page name. If the page does not
     * exist, then null is NOT returned, instead, a WikiPage instance is
     * created, which has the correct title, but no content.
     */
    public WikiPage getWikiPage(WikiName wikiName)
    {
        return new WikiPage(wikiName);
    }

    public CompoundAttributes getAttributes()
    {
        return _attributes;
    }

    public Object getAttribute(String name)
    {
        return getAttributes().get(name);
    }

    public void setAttribute(String name, Object value)
    {
        getAttributes().setAttribute(name, value);
    }

    /**
     * Returns the first part of the urls for this web site - upto, and NOT
     * including the web context. For example, if a wiki page is at
     * http://foo.com/myWebApp/view/MyPage then this will return http://foo.com
     * 
     * It can be used when you need a complete url, i.e. you could concatenate
     * WikiEngine.getbaseUrl() and WikiPage.getViewUrl() to form an absolute
     * url.
     */
    public String getBaseUrl()
    {
        HttpServletRequest request = WikiContext.getWikiContext().getServletRequest();
        StringBuffer result = new StringBuffer();

        String scheme = request.getScheme();
        int port = request.getServerPort();

        String portString = null;
        if (scheme.equals("http") && (port != 80)) {
            portString = ":" + port;
        }
        if (scheme.equals("https") && (port != 23)) {
            portString = ":" + port;
        }

        result.append(scheme).append("://").append(request.getServerName());

        if (portString != null) {
            result.append(portString);
        }

        return result.toString();

    }

}

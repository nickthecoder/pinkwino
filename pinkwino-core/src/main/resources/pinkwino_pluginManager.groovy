/*
  This is a groovy script, which is used to initialise the wiki engine.
*/

import uk.co.nickthecoder.pinkwino.*
import uk.co.nickthecoder.pinkwino.util.*
import uk.co.nickthecoder.pinkwino.plugins.*
import uk.co.nickthecoder.pinkwino.parser.*

logger.trace( "Configuring PluginManager" )

def wikiEngine = WikiEngine.instance()
def pluginManager = new StandardPluginManager()
wikiEngine.setPluginManager( pluginManager )

pluginManager.setJspPluginRoot( "/wiki/jspPlugins/" )

pluginManager.add( new JspPlugin( "plugins", "plugins.jsp", VisualPlugin.BODY_TYPE_NONE ) )
pluginManager.add( new JspPlugin( "pluginsIndex", "pluginsIndex.jsp",VisualPlugin.BODY_TYPE_NONE ) )
pluginManager.add( new JspPlugin( "namespaces", "namespaces.jsp", VisualPlugin.BODY_TYPE_NONE ) )
pluginManager.add( new JspPlugin( "exampleJsp", "example.jsp", VisualPlugin.BODY_TYPE_WIKI ) )
pluginManager.add( new AttributePlugin() )
pluginManager.add( new IndexPlugin( ) )

def toc = new JspPlugin( "toc", "toc.jsp", VisualPlugin.BODY_TYPE_NONE )
toc.addParameterDescription( new RegexParameterDescription( "levels", "[0-9]+", false, "3", "" ) )
pluginManager.add( toc )

pluginManager.add( new PluginPlugin() )
pluginManager.add( new RecentChangesPlugin() )
pluginManager.add( new BoxPlugin() )
pluginManager.add( new ExpandPlugin() )
pluginManager.add( new IncludePlugin() )
pluginManager.add( new TemplatePlugin() )
pluginManager.add( new TilesPlugin() )
pluginManager.add( new TilePlugin() )
pluginManager.add( new SimpleImagePlugin() )
pluginManager.add( new ImagePlugin() )
pluginManager.add( new MediaPlugin() )
pluginManager.add( new GoogleMapPlugin() )
pluginManager.add( new GoogleMapLinkPlugin() )
pluginManager.add( new DivPlugin( "right", "wiki_right", false ) )
pluginManager.add( new DivPlugin( "left", "wiki_left", false ) )
pluginManager.add( new DivPlugin( "center", "wiki_center", true ) )
pluginManager.add( new DivPlugin( "clear", "wiki_clear", false ) )
pluginManager.add( new DivPlugin( "menu", "wiki_menu", false ) )
pluginManager.add( new NamedNodePlugin( "aside" ) )
pluginManager.add( new JspPlugin( "systemStatus", "systemStatus.jsp", VisualPlugin.BODY_TYPE_NONE ) )
pluginManager.add( new JspPlugin( "backLinks", "backLinks.jsp", VisualPlugin.BODY_TYPE_NONE ) )
pluginManager.add( new LucenePageInfoPlugin() )

def quote = new JspPlugin( "quote", "quote.jsp", VisualPlugin.BODY_TYPE_WIKI )
quote.addParameterDescription( ParameterDescription.find( "align" ).defaultValue( "center" ) )
pluginManager.add( quote )

pluginManager.add( new JspPlugin( "helloWorldJsp", "helloWorld.jsp", VisualPlugin.BODY_TYPE_NONE ) )
pluginManager.add( new HelloWorldPlugin( "helloWorld" ) )
pluginManager.add( new JspPlugin( "exampleStringBodyJsp", "exampleStringBody.jsp", VisualPlugin.BODY_TYPE_TEXT ) )
pluginManager.add( new SearchPlugin( ) )
pluginManager.add( new ExternalLinkPlugin() )


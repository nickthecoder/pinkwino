/*
  This is a groovy script, which is used to initialise the wiki engine.
*/
import java.text.*

import uk.co.nickthecoder.pinkwino.*
import uk.co.nickthecoder.pinkwino.util.*
import uk.co.nickthecoder.pinkwino.storage.*
import uk.co.nickthecoder.pinkwino.security.*
import uk.co.nickthecoder.pinkwino.plugins.*
import uk.co.nickthecoder.pinkwino.plugins.*
import uk.co.nickthecoder.pinkwino.link.*
import uk.co.nickthecoder.pinkwino.metadata.*
import uk.co.nickthecoder.pinkwino.media.*


logger.trace( "default pinkwino.groovy : begin" )

def wikiEngine = WikiEngine.instance()
wikiEngine.setDefaultPageName( "home" )

// Include this namespace in all of your wikis, because it contains useful documentation.
def wikiNamespace = new Namespace( "wiki", "Home", "Index" )
wikiNamespace.setStorage( new VersioningFileSystemStorage( new File( "documents/wiki" ) ) )
// You may like to make it read-only though instead
// wikiNamespace.setStorage( new ReadOnlyStorage( new VersioningFileSystemStorage( new File( "documents/wiki" ) ) ) )
wikiEngine.addNamespace( wikiNamespace )
wikiEngine.setMessageNamespace( wikiNamespace )

// Always have a default namespace, but it doesn't have to be the "wiki" Namespace.
// wikiEngine.setDefaultNamespace( wikiNamespace )


// ************* TODO Remove Later ***********

logger.trace( "Adding extra namespaces" )

defaultNamespace = new Namespace( "default", "Home", "Index" )
defaultNamespace.setStorage( new VersioningFileSystemStorage( new File( "/gidea/documents/pinkwino/sites/giddyserv/content/public" ) ) )
wikiEngine.addNamespace( defaultNamespace )
wikiEngine.setDefaultNamespace( defaultNamespace )

def recipeNamespace = new Namespace( "recipe", "Recipes", "Index" )
recipeNamespace.setStorage( new VersioningFileSystemStorage( new File( "/gidea/documents/pinkwino/sites/recipe/content/recipe" ) ) )
wikiEngine.addNamespace( recipeNamespace )

def ingredientNamespace = new Namespace( "ingredient", "Index", "Index" )
ingredientNamespace.setStorage( new VersioningFileSystemStorage( new File( "/gidea/documents/pinkwino/sites/recipe/content/ingredient" ) ) )
wikiEngine.addNamespace( ingredientNamespace )

def gardenNamespace = new Namespace( "garden", "Home", "Index" )
gardenNamespace.setStorage( new VersioningFileSystemStorage( new File( "/gidea/documents/pinkwino/sites/garden/content/garden" ) ) )
wikiEngine.addNamespace( gardenNamespace )

def softwareNamespace = new Namespace( "software", "Home", "Index" )
softwareNamespace.setStorage( new VersioningFileSystemStorage( new File( "/gidea/documents/pinkwino/sites/software/content/default" ) ) )
wikiEngine.addNamespace( softwareNamespace )

def itchyNamespace = new Namespace( "itchy", "Home", "Index" )
itchyNamespace.setStorage( new VersioningFileSystemStorage( new File( "/gidea/documents/pinkwino/sites/software/content/itchy" ) ) )
wikiEngine.addNamespace( itchyNamespace )

def userNamespace = new Namespace( "user", "Home", "Index" )
userNamespace.setStorage( new VersioningFileSystemStorage( new File( "/gidea/documents/pinkwino/sites/giddyserv/content/user" ) ) )
wikiEngine.addNamespace( userNamespace )

// *******************************************

RunGroovy.fromClasspath( "pinkwino_externalLinkManager.groovy" )
RunGroovy.fromClasspath( "pinkwino_mediaManager.groovy" )
RunGroovy.fromClasspath( "pinkwino_syntaxManager.groovy" )
RunGroovy.fromClasspath( "pinkwino_urlManager.groovy" )
RunGroovy.fromClasspath( "pinkwino_pluginManager.groovy" )
RunGroovy.fromClasspath( "pinkwino_imageProcessing.groovy" )

// ************* TODO Remove Later ***********

logger.trace( "Adding extra External Links" )

elm = wikiEngine.getExternalLinkManager();
elm.add( new ExternalLinkType( "wikipedia", "http://en.wikipedia.org/wiki/%1", false, "wiki_external" ) );
elm.add( new ExternalLinkType( "search", "http://www.google.co.uk/search?hl=en&q=%1", false, "wiki_external" ) );
elm.add( new  ExternalLinkType( "wikipedia", "http://en.wikipedia.org/wiki/%1", true, "wiki_external" ) );
elm.add( new  ExternalLinkType( "nibbledish", "http://www.nibbledish.com/recipes/search/%1", true, "wiki_external" ) );
elm.add( new  ExternalLinkType( "images", "http://images.google.co.uk/images?q=%1", true, "wiki_external" ) );

logger.trace( "Adding extra Mime Types" )

def mediaManager = wikiEngine.getMediaManager()
mediaManager.addMimeType( new MimeType( "application/x-onesquarefoot", [ "osf" ], false, true ) );

logger.trace( "Adding extra Plugins" )

def pluginManager = wikiEngine.getPluginManager();
pluginManager.add( new DivPlugin( "plantDetails", "plantDetails", false ) );
pluginManager.add( new DivPlugin( "plantData", "plantData", false ) );
pluginManager.add( new DivPlugin( "plantLinks", "plantLinks", false ) );

def oneSquareFootPlugin = new JspPlugin( "oneSquareFoot", "/extra/oneSquareFoot/oneSquareFoot.jsp", VisualPlugin.BODY_TYPE_NONE );
oneSquareFootPlugin.addParameterDescription( ParameterDescription.find( "page" ).required() );
pluginManager.add( oneSquareFootPlugin );

logger.trace( "Adding security" )


def authenticationManager = new StandardAuthenticationManager( new File( "documents/users.txt" ) );

authorisationManagerA = new IPAuthorisationManager( "192.168.1.*", "127.0.0.1" );
authorisationManagerB = new StandardAuthorisationManager( "security" );
authorisationManagerB.addSuperUser( "nick" );
authorisationManager = new OrAuthorisationManager( authorisationManagerA, authorisationManagerB );

wikiEngine.enableSecurity( userNamespace, authenticationManager, authorisationManager );

// *******************************************

// Choose the look and feel */
// wikiEngine.getUrlManager().setTemplate( "/templates/default/template.jsp" )
// ***** Change this back *****
wikiEngine.getUrlManager().setTemplate( "/templates/ntc/template.jsp" )

// Image manipulation store temporary files. Choose the directory
wikiEngine.getMediaManager().setTempDirectory( new File( "/tmp" ) )

// Do NOT use this key within your own web site
// Go here for a key : https://developers.google.com/maps/documentation/javascript/get-api-key
// ***** Remove this *****
wikiEngine.getAttributes().setAttribute( "googleMap_key", "AIzaSyCpZxghYNTdsc5KXL483zoCMIN9tRLzHWw" );

// LuceneMetaSata add searching to your wiki, as well as get info, such as which pages links
// to a given page.
def luceneMetaData = new LuceneMetaData( new File( "documents/lucene-metadata" ) );
wikiEngine.setMetaData( luceneMetaData )
wikiEngine.addWikiPageListener( luceneMetaData )
luceneMetaData.rebuild()

logger.trace( "default pinkwino.groovy : end" );


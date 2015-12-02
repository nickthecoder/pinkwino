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

logger.trace( "default pinkwino.groovy : begin" )

def wikiEngine = WikiEngine.instance()
wikiEngine.setDefaultPageName( "home" )


def namespace = new Namespace( "default", "Home", "Index" )
namespace.setStorage( new VersioningFileSystemStorage( new File( "documents/pinkwino" ) ) )
wikiEngine.addNamespace( namespace )
wikiEngine.setDefaultNamespace( namespace )
wikiEngine.setMessageNamespace( namespace )


RunGroovy.fromClasspath( "common_externalLinkManager.groovy" )
//RunGroovy.fromClasspath( "common_mediaManager.groovy" )
RunGroovy.fromClasspath( "common_syntaxManager.groovy" )
RunGroovy.fromClasspath( "common_urlManager.groovy" )
RunGroovy.fromClasspath( "common_pluginManager.groovy" )
RunGroovy.fromClasspath( "common_imageProcessing.groovy" )

wikiEngine.getUrlManager().setTemplate( "/wiki/templates/default/template.jsp" )
wikiEngine.getMediaManager().setTempDirectory( new File( "/tmp" ) )

//wikiEngine.setMetaData( new LuceneMetaData( new File( "/tmp/pinkwino/lucene" ) ) )

logger.trace( "default pinkwino.groovy : end" );


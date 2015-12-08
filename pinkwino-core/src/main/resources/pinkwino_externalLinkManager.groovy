/*
  This is a groovy script, which is used to initialise the wiki engine.
*/

import uk.co.nickthecoder.pinkwino.*
import uk.co.nickthecoder.pinkwino.link.*

logger.trace( "Configuring ExternalLinkManager" )

def wikiEngine = WikiEngine.instance()
def elm = new StandardExternalLinkManager()

elm.add( new ExternalLinkType( "http", "http:%1", false, "wiki_external" ) )
elm.add( new ExternalLinkType( "https", "https:%1", false, "wiki_external" ) )
elm.add( new ExternalLinkType( "mailto", "mailto:%1", false, "wiki_mail" ) )
elm.add( new ExternalLinkType( "google", "http://www.google.com/search?q=%1", true, "wiki_search" ) )
elm.add( new ExternalLinkType( "edit", wikiEngine.getContextPath() + "/edit/%1", true ) )
elm.add( new ExternalLinkType( "media", wikiEngine.getContextPath() + "/media/%1", true ) )
elm.add( new ExternalLinkType( "local", "%1", false ) )

wikiEngine.setExternalLinkManager( elm )

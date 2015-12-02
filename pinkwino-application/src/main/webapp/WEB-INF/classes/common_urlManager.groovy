/*
  This is a groovy script, which is used to initialise the wiki engine.
*/

import uk.co.nickthecoder.pinkwino.*
import uk.co.nickthecoder.pinkwino.link.*

logger.trace( "Configuring URLManager" )

def wikiEngine = WikiEngine.instance() 
def contextPath = wikiEngine.getContextPath()
def urlManager = new StandardUrlManager( contextPath + "/wiki/error.jsp" )


urlManager.addUrl( UrlManager.URL_TYPE_VIEW, contextPath + "/view/_PATH_" )
urlManager.addUrl( UrlManager.URL_TYPE_EDIT, contextPath + "/edit/_PATH_" )
urlManager.addUrl( UrlManager.URL_TYPE_DIFF, contextPath + "/diff/_PATH_" )
urlManager.addUrl( UrlManager.URL_TYPE_RENAME, contextPath + "/rename/_PATH_" )
urlManager.addUrl( UrlManager.URL_TYPE_REVERT, contextPath + "/revert/_PATH_" )
urlManager.addUrl( UrlManager.URL_TYPE_INFO, contextPath + "/info/_PATH_" )
urlManager.addUrl( UrlManager.URL_TYPE_RAW, contextPath + "/raw/_PATH_" )
urlManager.addUrl( UrlManager.URL_TYPE_MEDIA, contextPath + "/media/_PATH_" )
urlManager.addUrl( UrlManager.URL_TYPE_DELETE, contextPath + "/delete/_PATH_" )
urlManager.addUrl( UrlManager.URL_TYPE_LOGIN, contextPath + "/login" )
urlManager.addUrl( UrlManager.URL_TYPE_LOGOUT,  contextPath + "/logout" )

urlManager.setTemplate( "/wiki/templates/default/template.jsp" )

wikiEngine.setUrlManager( urlManager )

import java.text.*
import java.awt.Color

import uk.co.nickthecoder.pinkwino.*
import uk.co.nickthecoder.pinkwino.storage.*
import uk.co.nickthecoder.pinkwino.plugins.*
import uk.co.nickthecoder.pinkwino.optional.image.*

logger.trace( "Configuring Image Processing" )

def wikiEngine = WikiEngine.instance()
def mediaManager = wikiEngine.getMediaManager()
def pluginManager = wikiEngine.getPluginManager()

// ---- plain thumbnail ----
def thumbnail = new Resize( 100, 100 )
baseDate = new SimpleDateFormat( "yyyy-MM-dd" ).parse( "2011-08-19" )
mediaManager.addMediaPreprocessor( "thumbnail", new ImagePreprocessor( baseDate, thumbnail ) )
pluginManager.add( new ProcessedImagePlugin( "thumbnail" ) )

// ---- teaser ----
def teaser = new Resize( 300, 300 )
baseDate = new SimpleDateFormat( "yyyy-MM-dd" ).parse( "2011-08-19" )
mediaManager.addMediaPreprocessor( "teaser", new ImagePreprocessor( baseDate, teaser ) )
pluginManager.add( new ProcessedImagePlugin( "teaser" ) )


pluginManager.add( new ImageIndexPlugin( "thumbnail" ) )

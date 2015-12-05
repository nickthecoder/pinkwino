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

// ---- thumbnail ----
def thumbnail = new Resize( 100, 100 )
baseDate = new SimpleDateFormat( "yyyy-MM-dd" ).parse( "2015-12-04" )
mediaManager.addMediaPreprocessor( "thumbnail", new ImagePreprocessor( baseDate, thumbnail ) )
pluginManager.add( new ProcessedImagePlugin( "thumbnail" ) )

// ---- teaser ----
def teaser = new Resize( 300, 300 )
baseDate = new SimpleDateFormat( "yyyy-MM-dd" ).parse( "2015-12-04" )
mediaManager.addMediaPreprocessor( "teaser", new ImagePreprocessor( baseDate, teaser ) )
pluginManager.add( new ProcessedImagePlugin( "teaser" ) )

// capture web pages as images ----

def capturePagePlugin = new CaptureWebPagePlugin( "/usr/bin/wkhtmltoimage", 1024, 768 )
pluginManager.add( capturePagePlugin )
pluginManager.add( new ThumbnailLinkPlugin( "thumbnailLink", "thumbnail" ) );
pluginManager.add( new ThumbnailLinkPlugin( "teaserLink", "teaser" ) );

pluginManager.add( new ImageIndexPlugin( "thumbnail" ) )

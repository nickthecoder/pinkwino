/*
  This is a groovy script, which is used to initialise the wiki engine.
*/

import uk.co.nickthecoder.pinkwino.*
import uk.co.nickthecoder.pinkwino.media.*

logger.trace( "Configuring MediaManager" );

def wikiEngine = WikiEngine.instance()
def mediaManager = new StandardMediaManager()

def jpeg = new MimeType( "image/jpeg", [ "jpeg", "jpg", "jpe" ] )
jpeg.setView( "view", "/wiki/viewImage.jsp")
mediaManager.addMimeType( jpeg )

def png = new MimeType( "image/png", [ "png" ] )
png.setView( "view", "/wiki/viewImage.jsp")
mediaManager.addMimeType( png )

def gif = new MimeType( "image/gif", [ "gif" ] )
gif.setView( "view", "/wiki/viewImage.jsp")
mediaManager.addMimeType( gif )

mediaManager.addMimeType( new MimeType( "image/tiff", [ "tiff", "tif" ] ) )
mediaManager.addMimeType( new MimeType( "image/svg+xml", [ "svg", "svgz" ] ) )
mediaManager.addMimeType( new MimeType( "image/x-icon", [ "ico" ] ) )
mediaManager.addMimeType( new MimeType( "image/x-ms-bmp", [ "bmp" ] ) )

mediaManager.addMimeType( new MimeType( "application/pdf", [ "pdf" ] ) )

mediaManager.addMimeType( new MimeType( "text/plain", [ "txt" ] ) )
mediaManager.addMimeType( new MimeType( "text/html", [ "html", "htm" ] ) )
mediaManager.addMimeType( new MimeType( "text/css", [ "css" ] ) )

mediaManager.addMimeType( new MimeType( "text/x-boo", [ "boo" ] ) );
mediaManager.addMimeType( new MimeType( "text/x-c++hdr", [ "h++", "hpp", "hxx", "hh" ] ) )
mediaManager.addMimeType( new MimeType( "text/x-c++src", [ "c+", "cpp", "cxx", "cc" ] ) )
mediaManager.addMimeType( new MimeType( "text/x-chdr", [ "h" ] ) )
mediaManager.addMimeType( new MimeType( "text/x-csrc", [ "c" ] ) )
mediaManager.addMimeType( new MimeType( "text/x-java", [ "java" ] ) )
mediaManager.addMimeType( new MimeType( "text/x-pascal", [ "p", "pascal" ] ) )
mediaManager.addMimeType( new MimeType( "text/x-perl", [ "pl", "pm" ] ) )
mediaManager.addMimeType( new MimeType( "text/x-python", [ "py" ] ) )
mediaManager.addMimeType( new MimeType( "text/x-sh", [ "sh" ] ) )

mediaManager.addMimeType( new MimeType( "application/x-javascript", [ "js" ] ) )
mediaManager.addMimeType( new MimeType( "application/xhtml+xml", [ "xhtml", "xht" ] ) )
mediaManager.addMimeType( new MimeType( "application/xml", [ "xml", "xsl" ] ) )
mediaManager.addMimeType( new MimeType( "application/json", [ "json" ], true, true ) )

mediaManager.addMimeType( new MimeType( "application/x-gtar", [ "gtar", "tgz", "taz", "tar.gz" ] ) )
mediaManager.addMimeType( new MimeType( "application/zip", [ "zip" ] ) )

wikiEngine.setMediaManager( mediaManager )


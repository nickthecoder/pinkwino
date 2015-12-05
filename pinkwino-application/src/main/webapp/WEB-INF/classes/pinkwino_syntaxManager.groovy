/*
  This is a groovy script, which is used to initialise the wiki engine.
*/

import uk.co.nickthecoder.pinkwino.*
import uk.co.nickthecoder.pinkwino.parser.*
import uk.co.nickthecoder.pinkwino.parser.tree.*
import uk.co.nickthecoder.pinkwino.parser.syntax.*
import uk.co.nickthecoder.pinkwino.plugins.*

logger.trace( "Configuring SyntaxManager" )

def wikiEngine = WikiEngine.instance()
def sm = new SyntaxManager()

wikiEngine.setSyntaxManager( sm )

// Debugging aids
//sm.addWikiSyntax( new ThrowSyntax( "<!#throw#!>" ) )
//sm.addWikiSyntax( new SimpleWikiSyntax( "<!#throw2#!>", "", new ThrowNode() ) )

// Blocks
sm.addWikiLineSyntax( new PreBlockSyntax( "{{{", "}}}" ) )
sm.addWikiLineSyntax( new PreBlockSyntax( "<pre>", "</pre>" ) )
sm.addWikiLineSyntax( new DivWikiSyntax( "((", "))", true ) )
sm.addWikiLineSyntax( new DivWikiSyntax( "{{[", "]}}", true ) )
sm.addWikiLineSyntax( new DivWikiSyntax( ">>", "<<", true, "blockquote", null ) )
sm.addWikiLineSyntax( new DivWikiSyntax( "\"\"\"", "\"\"\"", true, "blockquote", "wiki_quote" ) )
sm.addWikiLineSyntax( new PreBlockSyntax( "\$\$\$", "\$\$\$", "wiki_code" ) )

// Text styles
sm.addWikiSyntax( new SimpleWikiSyntax( "++", new SimpleParentNode( "b", false ) ) )
sm.addWikiSyntax( new SimpleWikiSyntax( "__", new SimpleParentNode( "u", false ) ) )
sm.addWikiSyntax( new SimpleWikiSyntax( "''", new SimpleParentNode( "i", false )  ) )
sm.addWikiSyntax( new SimpleWikiSyntax( "--", "--", new SimpleParentNode( "s", false ) ) )
sm.addWikiSyntax( new SimpleWikiSyntax( "^^", "^^", new SimpleParentNode( "sup", false ) ) )
sm.addWikiSyntax( new SimpleWikiSyntax( ",,", ",,", new SimpleParentNode( "sub", false ) ) )
sm.addWikiSyntax( new SimpleWikiSyntax( "\$\$", "\$\$", new SimpleParentNode( "code", false ) ) )
sm.addWikiSyntax( new SimpleWikiSyntax( "``",   "``",   new SimpleParentNode( "code", false ) ) ) // Backwards compatible
sm.addWikiSyntax( new SimpleWikiSyntax( "\"\"", "\"\"", new SimpleParentNode( "q", false ) ) )

// Comment and nowiki
sm.addWikiSyntax( new SimpleWikiSyntax( "<!--", "-->", new Comment() ) )
sm.addWikiSyntax( new NoWikiSyntax( "<nowiki>", "</nowiki>" ) )

// Line breaks
sm.addWikiSyntax( new SimpleWikiSyntax( "<br>", new LineBreak() ) )
sm.addWikiSyntax( new SimpleWikiSyntax( "<br/>", new LineBreak() ) )
sm.addWikiSyntax( new SimpleWikiSyntax( "\\\\", new LineBreak() ) )

// CharacterEntities
sm.addWikiSyntax( new CharacterEntitySyntax() )
  
// Links
sm.addWikiSyntax( new LinkSyntax() )

// Headings
sm.addWikiLineSyntax( new HeadingSyntax( 4, "!!!!" ) )
sm.addWikiLineSyntax( new HeadingSyntax( 3, "!!!" ) )
sm.addWikiLineSyntax( new HeadingSyntax( 2, "!!" ) )
sm.addWikiLineSyntax( new HeadingSyntax( 1, "!" ) )

sm.addWikiLineSyntax( new HeadingSyntax( 4, "=====", "=====" ) )
sm.addWikiLineSyntax( new HeadingSyntax( 3, "====", "====" ) )
sm.addWikiLineSyntax( new HeadingSyntax( 2, "===", "===" ) )
sm.addWikiLineSyntax( new HeadingSyntax( 1, "==", "==" ) )


// Plugins
sm.addWikiSyntax( new PluginSyntax( "{{:", "}}" ) ) // Backwards compatible.
sm.addWikiSyntax( new PluginSyntax( "{{", "}}" ) )

// Paragraph separator and Horizontal Rule
sm.addWikiLineSyntax( new ParagraphSyntax() )
sm.addWikiLineSyntax( new HorizontalRuleSyntax() )

// Lists
sm.addWikiLineSyntax( new ListItemSyntax( true, 4, "####" ) )
sm.addWikiLineSyntax( new ListItemSyntax( true, 3, "###" ) )
sm.addWikiLineSyntax( new ListItemSyntax( true, 2, "##" ) )
sm.addWikiLineSyntax( new ListItemSyntax( true, 1, "#" ) )

sm.addWikiLineSyntax( new ListItemSyntax( false, 4, "****" ) )
sm.addWikiLineSyntax( new ListItemSyntax( false, 3, "***" ) )
sm.addWikiLineSyntax( new ListItemSyntax( false, 2, "**" ) )
sm.addWikiLineSyntax( new ListItemSyntax( false, 1, "*" ) )

// Table
sm.addWikiLineSyntax( new TableBeginSyntax( "{|" ) )
sm.addWikiLineSyntax( new TableEndSyntax( "|}" ) )
sm.addWikiLineSyntax( new TableRowSyntax( "|-" ) )
def headingSyntax = new TableCellSyntax( "||", true )
def cellSyntax = new TableCellSyntax( "|", false )
headingSyntax.setAlternate( cellSyntax )
cellSyntax.setAlternate( headingSyntax )
sm.addWikiLineSyntax( headingSyntax )
sm.addWikiLineSyntax( cellSyntax )


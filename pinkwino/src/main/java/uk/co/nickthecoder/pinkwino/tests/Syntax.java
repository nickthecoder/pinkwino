package uk.co.nickthecoder.pinkwino.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.co.nickthecoder.pinkwino.Version;
import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.WikiName;
import uk.co.nickthecoder.pinkwino.WikiPage;
import uk.co.nickthecoder.pinkwino.parser.Renderer;

/**
 * JUnit Test Class
 */
public class Syntax
{

    public static void main(String args[])
    {
        org.junit.runner.JUnitCore.main("pinkwino.tests.Syntax");
    }

    @Before
    public void setUp()
    {
        WikiEngine.initialise("webapp/WEB-INF/classes/pinkwino.bsh");
    }

    @After
    public void tearDown()
    {
    }

    protected String render(String wiki)
    {
        // WikiEngine.instance().beginWikiInstance( (HttpServletRequest)
        // pageContext.getRequest(), (HttpServletResponse)
        // pageContext.getResponse() );
        Renderer renderer = WikiEngine.instance().getRenderer();

        // We need the wiki name primarily for the the namespace. Any
        // unqualified names
        // will use the current namespace's name.
        WikiName wikiName = WikiEngine.instance().getWikiNameFormat().parse("fakePage");

        WikiPage page = new WikiPage(wikiName);
        Version version = new Version(page);
        page.setCurrentVersion(version);
        version.setContent(wiki);

        String result = renderer.render(version);
        // WikiEngine.instance().endWikiInstance( (HttpServletRequest)
        // pageContext.getRequest(), (HttpServletResponse)
        // pageContext.getResponse() );

        return result;
    }

    protected void assertRendered(String wiki, String html)
    {
        assertEquals("Rendered wiki->html : ", render(wiki), html);
    }

    protected void assertRenderedMatches(String wiki, String htmlRegex)
    {
        String rendered = render(wiki);
        if (!rendered.matches(htmlRegex)) {
            fail("Rendered does not match : " + wiki + " --> " + rendered + " !matches " + htmlRegex);
        }
    }

    // Uncomment below to check that jUnit is really working!
    /*
     * @Test public void testRealityCheck() { assertRendered( "Reality Check",
     * "real test" ); }
     */

    @Test
    public void testSyntax()
    {
        plainText();
        bold();
        italics();
        underline();
        strike();
        hr();
        headings();
        link();
        pre();
    }

    public void plainText()
    {
        assertRenderedMatches("Hello World", "<p>\\s*Hello World\\s*</p>");
    }

    public void bold()
    {
        assertRenderedMatches("Hi, ++Hello++ World", "<p>\\s*Hi, <b>Hello</b> World\\s*</p>");
    }

    public void italics()
    {
        assertRenderedMatches("Hi, ''Hello'' World", "<p>\\s*Hi, <i>Hello</i> World\\s*</p>");
    }

    public void underline()
    {
        assertRenderedMatches("Hi, __Hello__ World", "<p>\\s*Hi, <u>Hello</u> World\\s*</p>");
    }

    public void strike()
    {
        assertRenderedMatches("Hi, --Hello-- World",
                        "<p>\\s*Hi, <span style=\"text-decoration:line-through;\">Hello</span> World\\s*</p>");
    }

    public void hr()
    {
        assertRenderedMatches("Hello\n----\nWorld", "<p>\\s*Hello\\s*</p>\\s*<hr/>\\s*<p>\\s*World\\s*</p>");
    }

    public void headings()
    {
        String namePattern = "<a name=\"section_\\d*\"></a>";
        String editPattern = "<div class=\".*\">\\s*<a href=\".*\">.*</a>\\s*</div>";

        assertRenderedMatches("== Heading1 ==", namePattern + "\\s*" + "<h2>\\s*Heading1\\s*</h2>" + "\\s*"
                        + editPattern);
        assertRenderedMatches("=== Heading2 ===", namePattern + "\\s*" + "<h3>\\s*Heading2\\s*</h3>" + "\\s*"
                        + editPattern);
        assertRenderedMatches("==== Heading3 ====", namePattern + "\\s*" + "<h4>\\s*Heading3\\s*</h4>" + "\\s*"
                        + editPattern);
    }

    public void link()
    {
        assertRenderedMatches("Hello [World]",
                        "<p>\\s*Hello <a class=\"wiki_notFound\" href=\"/pinkwino/view/World\">World</a>\\s*</p>");
        assertRenderedMatches("Hello [earth|World]",
                        "<p>\\s*Hello <a class=\"wiki_notFound\" href=\"/pinkwino/view/World\">earth</a>\\s*</p>");
        assertRenderedMatches("Hello [google|http://google.com]",
                        "<p>\\s*Hello <a class=\"wiki_external\" href=\"http://google.com\">google</a>\\s*</p>");
    }

    public void pre()
    {
        assertRenderedMatches("{{{\nhello\n\n}}}", "<pre>hello</pre>");
    }
}

/* {{{ GPL
 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
}}} */

package uk.co.nickthecoder.pinkwino.parser;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.nickthecoder.pinkwino.Namespace;
import uk.co.nickthecoder.pinkwino.Section;
import uk.co.nickthecoder.pinkwino.Version;
import uk.co.nickthecoder.pinkwino.WikiDocument;
import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.WikiName;
import uk.co.nickthecoder.pinkwino.parser.syntax.WikiLineSyntax;
import uk.co.nickthecoder.pinkwino.parser.syntax.WikiSyntax;
import uk.co.nickthecoder.pinkwino.parser.tree.AutoParagraphBlock;
import uk.co.nickthecoder.pinkwino.parser.tree.EndNode;
import uk.co.nickthecoder.pinkwino.parser.tree.ErrorText;
import uk.co.nickthecoder.pinkwino.parser.tree.ExceptionNode;
import uk.co.nickthecoder.pinkwino.parser.tree.Node;
import uk.co.nickthecoder.pinkwino.parser.tree.ParentNode;
import uk.co.nickthecoder.pinkwino.parser.tree.PlainText;
import uk.co.nickthecoder.pinkwino.parser.tree.SimpleParentNode;
import uk.co.nickthecoder.pinkwino.parser.tree.Unterminated;
import uk.co.nickthecoder.pinkwino.plugins.PluginSupport;

/**
 * Converts wiki syntax text into a tree structured of nodes. This is not thread
 * safe (probably), so you should NOT use a single Parser instance for all
 * parsing in a multi-threaded application.
 */

public class Parser implements PluginSupport
{

    public static String[] NO_TERMINATORS = {};

    protected static Logger _logger = LogManager.getLogger(Parser.class);

    private Version _version;

    private boolean _finished;

    private LineNumberReader _reader;

    private WikiDocument _wikiDocument;

    private ParentNode _everything;

    private List<ParentNode> _parents; // a stack of ParentNode objects.

    private ParentNode _currentParent;

    private String _line;

    private int _start;

    private int _latestFailedSyntaxIndex;

    private SyntaxManager _syntaxManager;

    private Section _latestSection;

    public Parser(Version version, SyntaxManager syntaxManager)
    {
        _version = version;
        _syntaxManager = syntaxManager;
    }

    public ParentNode getCurrentParent()
    {
        return this._currentParent;
    }

    /**
     * Allows a piece of wiki text to be parsed and the results inserted into
     * the tree of nodes currently being created. This was created to allow a
     * plugin to pass arbitary wiki markup, and have it parsed as if it were
     * within the page being parsed. Used by RandomPickPlugin.
     */
    public void insertWikiMarkup(Reader wikiMarkup)
    {

        // push the state of the old parse onto the stack
        LineNumberReader oldReader = _reader;
        String oldLine = _line;
        int oldStart = _start;
        boolean oldFinished = _finished;

        _reader = new LineNumberReader(wikiMarkup);
        doParse();

        // Pop the state back off the stack.
        _reader = oldReader;
        _line = oldLine;
        _start = oldStart;
        _finished = oldFinished;

    }

    public WikiDocument parse()
    {
        String syntax = _version.getContent();

        if (_version.getWikiPage().getWikiName().isText()) {
            SimpleParentNode parentNode = new SimpleParentNode("pre", true);

            parentNode.add(new PlainText(_version.getContent()));
            return new WikiDocument(_version, parentNode);
        }

        _finished = false;
        _everything = new AutoParagraphBlock();
        _wikiDocument = new WikiDocument(_version, _everything);
        _latestSection = _wikiDocument;
        _currentParent = _everything;
        _parents = new LinkedList<ParentNode>();
        _parents.add(_everything);

        _reader = new LineNumberReader(new StringReader(syntax));
        _line = "";
        _start = 1;
        _latestFailedSyntaxIndex = -1;

        doParse();

        return _wikiDocument;

    }

    private void doParse()
    {
        try {
            parseRemainder(NO_TERMINATORS);
            add(new EndNode());

            // Close all tags that can't close themselves.
            while (getParentNode() instanceof Unterminated) {
                end(getParentNode());
            }

            // The idea of this, is to inform the user of all unclosed tags
            while (_parents.size() > 1) {
                ParentNode parent = (ParentNode) _parents.get(_parents.size() - 1);
                end(parent);
                add(new ErrorText(" unfinished " + parent.getClass().getName().replaceAll(".*\\.", "")));
            }

        } catch (Exception e) {
            _everything.add(new ExceptionNode(e));
        }

    }

    public WikiDocument getWikiDocument()
    {
        return _wikiDocument;
    }

    public Namespace getDefaultNamespace()
    {
        if ((_version != null) && (_version.getWikiPage() != null)) {
            return _version.getWikiPage().getNamespace();
        }

        return WikiEngine.instance().getDefaultNamespace();

    }

    public WikiName parseWikiName(String name)
    {
        return WikiEngine.instance().getWikiNameFormat().parse(getDefaultNamespace(), name);
    }

    /**
     * Returns the version of the page that is being parsed. This was added
     * primarily so that links could know the page they were linking from, so
     * that they could know the current namespace.
     */
    public Version getVersion()
    {
        return _version;
    }

    public int getLineNumber()
    {
        return _reader.getLineNumber();
    }

    /**
     * Parses a single line of wiki syntax. This can include the WikiLineSyntaxs
     * such as headings, horizontal rules, paragraph breaks etc.
     */
    private void parseLineSyntaxs(String[] terminators)
    {
        for( String terminator : terminators ) {
            if (terminator.equals(_line)) {
                return;
            }
        }
        
        for (Iterator<WikiLineSyntax> i = _syntaxManager.getWikiLineSyntax(); i.hasNext();) {
            WikiLineSyntax wlm = i.next();

            int matching = wlm.matchingPrefixLength(_line);
            if (matching != WikiLineSyntax.NO_MATCH) {

                _start = matching;
                String matchingSection = _line.substring(0, _start);

                try {

                    wlm.processSyntax(this);

                } catch (Exception e) {
                    // An error occurred while parsing a particular syntax. Flag
                    // to the end user the
                    // tag which caused the problem, and hide the underlying
                    // cause in html comments.
                    add(new ErrorText("(lineSyntax) " + matchingSection));
                    add(new ExceptionNode(e));
                }

                return;
            }
        }

    }

    /**
     * Parses the remainder of the document, or until a terminator is found. It
     * looks for WikiSyntax prefixes along the way Returns the terminator found,
     * or null if the end of document was reached before finding a terminator.
     */
    private String parseSyntax(String[] terminators)
    {
        if (_finished) {
            return null;
        }

        String firstTerminator = null;

        int firstIndex = -1;
        WikiSyntax firstSyntax = null;

        // Look for terminators
        for (int i = 0; i < terminators.length; i++) {
            int index = _line.indexOf(terminators[i], _start);
            if ((index != -1) && ((index < firstIndex) || (firstIndex == -1))) {
                firstIndex = index;
                firstTerminator = terminators[i];
            }
        }

        // Look for syntax prefixes
        for (Iterator<WikiSyntax> i = _syntaxManager.getWikiSyntax(); i.hasNext();) {
            WikiSyntax wm = i.next();

            int index = _line.indexOf(wm.getPrefix(), _start);
            if (index >= _latestFailedSyntaxIndex) {
                if ((index != -1) && ((index < firstIndex) || (firstIndex == -1))) {
                    firstIndex = index;
                    firstSyntax = wm;
                }
            }
        }

        _latestFailedSyntaxIndex = -1;

        // Add the preceeding text
        if (firstIndex > _start) {
            add(new PlainText(_line.substring(_start, firstIndex)));
            _start = firstIndex;
        }

        // There are three possibilities
        // 1) We found a terminator first
        // 2) We found a wiki syntax prefix first
        // 3) We didn't find a terminator or a prefix

        // (1) If we've hit and end marker, then return
        if ((firstTerminator != null) && (firstSyntax == null)) {
            _start += firstTerminator.length();
            return firstTerminator;
        }

        // (2) If we've hit a syntax prefix, then parse the contents, and
        // recurse to complete the line.
        if (firstSyntax != null) {
            _start += firstSyntax.getPrefix().length();

            try {
                firstSyntax.processSyntax(this);
            } catch (Exception e) {

                // An error occurred while parsing a particular syntax. Flag to
                // the end user the
                // tag which caused the problem, and hide the underlying cause
                // in html comments.
                add(new ErrorText("(firstSyntax) " + firstSyntax.getPrefix()));
                add(new ExceptionNode(e));

            }
            return parseSyntax(terminators);
        }

        // (3) if nothing was found, then add the remaining text
        if (firstIndex == -1) {
            add(new PlainText(_line.substring(_start) + "\n"));
            _start = _line.length();
            return null;
        }

        // We shouldn't get here
        throw new RuntimeException("Parser.parseSyntax() has a bug!");
    }

    // }}}

    // {{{ begin
    public void begin(ParentNode parent)
    {
        if (parent.isBlock() && !(parent instanceof Unterminated)) {
            /*
             * Terminate all unterminated tags, such as the ListItem and
             * ListNode (they are the only ones right now)
             */
            while (getParentNode() instanceof Unterminated) {
                end(getParentNode());
            }
        }

        _parents.add(parent);
        _currentParent = parent;
    }

    // }}}

    // {{{ end
    public void end(ParentNode parent)
    {
        if (parent.isBlock() && !(parent instanceof Unterminated)) {
            /*
             * Terminate all unterminated tags, such as the ListItem and
             * ListNode (they are the only ones right now)
             */
            while ((parent != getParentNode()) && (getParentNode() instanceof Unterminated)) {
                if (parent == getParentNode()) {
                    break;
                }
                end(getParentNode());
            }
        }

        if (parent != _currentParent) {
            _logger.info("Attempted to end a parent incorrectly");
        } else {
            _parents.remove(parent);
            _currentParent = (ParentNode) _parents.get(_parents.size() - 1);
            add(parent);
        }

    }

    public void abandon(ParentNode parent, String startText)
    {
        if (parent != _currentParent) {
            _logger.info("Attempted to abandon a parent that is not active");
        } else {
            _parents.remove(parent);
            _currentParent = (ParentNode) _parents.get(_parents.size() - 1);
        }

        add(new ErrorText("(abandoned) " + startText));

        for (Iterator<Node> i = parent.getChildren(); i.hasNext();) {
            Node node = i.next();

            add(node);
        }
    }

    public void autoClose()
    {
        /*
         * Terminate all unterminated tags, such as the ListItem and ListNode
         * (they are the only ones right now)
         */
        while (getParentNode() instanceof Unterminated) {
            end(getParentNode());
        }
    }

    public void add(Node node)
    {
        if (node.isBlock() && !(node instanceof Unterminated)) {
            autoClose();
        }

        _currentParent.add(node);
    }

    public ParentNode getParentNode()
    {
        return _currentParent;
    }

    /**
     * Parses the remainder of the text from the current line only. Returns the
     * terminator that was found, or null if the end of line was reached.
     */
    public String parseLineRemainder(String[] terminators)
    {
        if ((_line != null) && (_start < _line.length())) {
            String terminator = parseSyntax(terminators);
            if (terminator != null) {
                return terminator;
            }
        }

        // Terminator not found
        return null;
    }

    public String getLineRemainder()
    {
        String result = _line.substring(_start);
        _start = _line.length();
        return result;
    }

    public void ungetLineRemainder(String remainder)
    {
        _line = remainder;
        _start = 0;
    }

    public String parseRemainder(String[] terminators)
    {
        try {

            while (!_finished) {

                if (_start >= _line.length()) {

                    // We've done the current line, so move on to the next

                    _line = _reader.readLine();
                    _start = 0;
                    if (_line == null) {
                        _finished = true;
                        break;
                    }

                    // We can only allow blocks inside blocks. Blocks are not
                    // allowed inside spans.
                    if (getParentNode().isBlock()) {
                        parseLineSyntaxs(terminators);
                    }
                }

                if ((_line != null) && (_start < _line.length())) {
                    String terminator = parseSyntax(terminators);
                    if (terminator != null) {
                        return terminator;
                    }
                }

            }

            // Terminator not found
            for (int i = 0; i < terminators.length; i++) {
                add(new ErrorText("(terminator not found) : " + terminators[i]));
            }

            return null;

        } catch (IOException e) {
            // Grr, annoying - no error will ever get thrown!
            throw new RuntimeException(e);
        }
    }

    public RemainderResult getRemainder(String[] terminators, boolean multipleLines)
    {
        StringBuffer buffer = new StringBuffer();
        String terminator = null;

        try {

            while (!_finished) {

                if (_start >= _line.length()) {

                    // We've done the current line, so move on to the next

                    if (multipleLines) {
                        buffer.append("\n");
                        _line = _reader.readLine();
                        _start = 0;
                        if (_line == null) {
                            _finished = true;
                            break;
                        }

                    } else {
                        break;
                    }

                }

                if (_start < _line.length()) {

                    // Look for the terminators
                    int firstTerminatorIndex = Integer.MAX_VALUE;

                    for (int i = 0; i < terminators.length; i++) {
                        int index = _line.indexOf(terminators[i], _start);
                        if ((index != -1) && (index < firstTerminatorIndex)) {
                            firstTerminatorIndex = index;
                            terminator = terminators[i];
                        }
                    }
                    if (terminator != null) {
                        buffer.append(_line.substring(_start, firstTerminatorIndex));
                        _start = firstTerminatorIndex + terminator.length();

                        return new RemainderResult(buffer.toString(), terminator);
                    }

                    // Terminator not found
                    buffer.append(_line.substring(_start));
                    _start = _line.length();
                }

                if (!multipleLines) {
                    break;
                }

            }

            return new RemainderResult(buffer.toString(), null);

        } catch (IOException e) {
            // Grr, annoying - no error will ever get thrown!
            throw new RuntimeException(e);
        }
    }

    /**
     * Can only be called to undo a part of a <b>single</b> line
     */
    public void unget(String text)
    {

        if (text == null) {
            return;
        }

        int undoSize = text.length();
        int newStart = _start - undoSize;

        if (_start - undoSize < 0) {
            throw new RuntimeException("Illegal unget: " + text + " but currentPosition is " + _start + " of " + _line);
        }

        if (!(_line.substring(newStart, newStart + undoSize).equals(text))) {

            throw new RuntimeException("Illegal unget: " + text + " vs "
                            + _line.substring(newStart, newStart + undoSize));

        }

        _latestFailedSyntaxIndex = _start;
        _start = newStart;
    }

    public Section getLatestSection()
    {
        return _latestSection;
    }

    public void setLatestSection(Section value)
    {
        _latestSection = value;
    }

}

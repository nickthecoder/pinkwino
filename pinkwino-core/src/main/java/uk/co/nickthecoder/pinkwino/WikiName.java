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

package uk.co.nickthecoder.pinkwino;

import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.nickthecoder.pinkwino.link.UrlManager;
import uk.co.nickthecoder.pinkwino.media.MediaPreprocessor;
import uk.co.nickthecoder.pinkwino.media.MimeType;
import uk.co.nickthecoder.pinkwino.parser.ParametersParser;
import uk.co.nickthecoder.pinkwino.parser.ParametersParserResults;
import uk.co.nickthecoder.pinkwino.util.Parameter;
import uk.co.nickthecoder.pinkwino.util.ParameterDescriptions;
import uk.co.nickthecoder.pinkwino.util.Parameters;

/**
 * Holds the name of a wiki page, including its namespace
 */

public class WikiName implements Comparable<WikiName>
{
    protected static Logger _logger = LogManager.getLogger(WikiName.class);

    /**
     * The way for a user to refer to a wiki page. Note, that this name isn't
     * guareteed to be unique for some namespaces (e.g. a persons name in a
     * database of people).
     */
    private String _title;

    /**
     * The namespace that holds the name
     */
    private Namespace _namespace;

    /**
     * The type of page, it is "talk" for the discussion page, if it is the main
     * wiki page, then it is null.
     */
    private String _relation;

    /**
     * WikiNames can have special values in their relation, which is used to
     * feed the wiki page's media though some kind of process. For example, an
     * image could be scaled down to a thumbnail size, in which case the
     * relation may be called "thumbnail". For normal pages, this will be null.
     */
    private MediaPreprocessor _mediaPreprocessor;

    /**
     * The parameters given by the relation. For example, if the relation is
     * "scale(width='100')", then parameters will contain one parameter called
     * width, value of 100.
     * 
     * The parameter descriptions must be defined by the MediaProcessor for this
     * relationship type (in this case the "scale" media processor).
     */
    private Parameters _parameters;

    public static WikiName create(String name)
    {
        return WikiEngine.instance().getWikiNameFormat().parse(name);
    }

    public static WikiName create(String namespaceName, String title, String relation)
    {
        Namespace namespace;
        WikiEngine engine = WikiEngine.instance();

        if (namespaceName == null) {
            namespace = engine.getDefaultNamespace();
        } else {
            namespace = engine.getNamespace(namespaceName);
        }

        return new WikiName(namespace, title, relation);
    }

    public WikiName(Namespace namespace, String title)
    {
        this(namespace, title, null);
    }

    public WikiName(Namespace namespace, String title, String relation)
    {
        _namespace = namespace;
        _title = title;
        setRelation(relation);
    }

    private void setRelation(String relation)
    {
        _mediaPreprocessor = null;
        _parameters = null;

        if ((relation == null) || (relation.length() == 0)) {
            _relation = null;
            return;
        }

        String key = relation;

        int open = relation.indexOf("(");
        if (open <= 0) {

            _mediaPreprocessor = WikiEngine.instance().getMediaManager().getMediaPreprocessor(relation);
            _relation = relation;
            return;

        }

        // We have dynamic relation i.e. one with parameters

        key = relation.substring(0, open).trim();

        _mediaPreprocessor = WikiEngine.instance().getMediaManager().getMediaPreprocessor(key);
        if (_mediaPreprocessor == null) {
            _relation = "error";
            return;
        }

        String parametersString = relation.substring(open);
        ParameterDescriptions pds = _mediaPreprocessor.getParameterDescriptions();

        ParametersParser pp = new ParametersParser("(", ")", '\'', pds, _namespace);
        ParametersParserResults ppr = pp.parseParameters(parametersString);
        if (ppr.illegalParameters.size() > 0) {
            _relation = "error";
            return;
        }

        _parameters = ppr.parameters;

        // MORE Check that all the required parameters were specified.

        // Rebuild the relation using the parameter value.
        // This will normalise it, ensuring that the parameters are in a
        // standard order,
        // and there isn't any superfluous white space.
        StringBuffer buffer = new StringBuffer();
        buffer.append(key);
        boolean first = true;
        for (Iterator<Parameter> i = _parameters.iterator(); i.hasNext();) {
            if (first) {
                first = false;
                buffer.append("(");
            }
            Parameter parameter = i.next();
            buffer.append(parameter.getName());
            buffer.append("='");
            buffer.append(parameter.getValue());
            buffer.append("'");
            if (i.hasNext()) {
                buffer.append(" ");
            }
        }
        if (!first) {
            buffer.append(")");
        }

        _relation = buffer.toString();

    }

    /**
     * Get method for attribute {@link #_title}.
     */
    public String getTitle()
    {
        return _title;
    }

    /**
     * Set method for attribute {@link #_title}.
     */
    public void setTitle(String value)
    {
        _title = value;
    }

    /**
     * If the name has an extension, which is a known mime type, then remove
     * that extension, and return just the part before the period.
     */
    public String getStrippedTitle()
    {
        return WikiEngine.instance().getMediaManager().stripKnownExtentions(getTitle());
    }

    public String getRelation()
    {
        return _relation;
    }

    /**
     * Get method for attribute {@link #_namespace}.
     */
    public Namespace getNamespace()
    {
        return _namespace;
    }

    public MediaPreprocessor getMediaPreprocessor()
    {
        return _mediaPreprocessor;
    }

    public Parameters getRelationParameters()
    {
        return _parameters;
    }

    public WikiPage getWikiPage()
    {
        return WikiEngine.instance().getWikiPage(this);
    }

    public boolean getExists()
    {
        return getWikiPage().getExists();
    }

    /**
     * Returns true if there is an extension to the title which is registered
     * with the MediaManager.
     */
    public boolean isMedia()
    {
        if (getMimeType() == null) {
            return false;
        }

        return !getMimeType().isText();
    }

    /**
     * Returns true if the MediaManager considers this page to represent an
     * image.
     */
    public boolean isImage()
    {
        if (getMimeType() == null) {
            return false;
        }

        return getMimeType().isImage();
    }

    /**
     * Returns true if there is an extension to the title which is registered
     * with the MediaManager as a text mime type, such as "text/css".
     */
    public boolean isText()
    {
        if (getMimeType() == null) {
            return false;
        }

        if (getRelation() != null) {
            return false;
        }

        return getMimeType().isText();
    }

    /**
     * If there is an extension to the title which is registered with the
     * MediaManager, then it returns the appropriate mime type, otherwise it
     * returns null.
     */
    public MimeType getMimeType()
    {
        return WikiEngine.instance().getMediaManager().getMimeType(this);
    }

    public String getFormatted()
    {
        return WikiEngine.instance().getWikiNameFormat().format(this);
    }

    public WikiName getRelatedName(String relation)
    {
        if (relation == null) {
            if (_relation == null) {
                return this;
            }
        } else {
            if (relation.equals(_relation)) {
                return this;
            }
        }

        return new WikiName(_namespace, _title, relation);
    }

    public String getViewUrl()
    {
        return WikiEngine.instance().getUrlManager().getUrl(UrlManager.URL_TYPE_VIEW, this);
    }

    public String getMediaUrl()
    {
        return WikiEngine.instance().getUrlManager().getUrl(UrlManager.URL_TYPE_MEDIA, this);
    }

    public String toString()
    {
        if (getRelation() == null) {
            return "WikiName:" + getNamespace().getName() + "/" + getTitle();
        } else {
            return "WikiName:" + getNamespace().getName() + "/" + getTitle() + "{" + getRelation() + "}";
        }
    }

    public int hashCode()
    {
        // While this isn't a perfect hash (as the namespace and the relation
        // are ignored), it is probably pretty good in
        // practice, as most pages will be unique with just the name's hash, and
        // for those that are not, there will not be
        // many rivals with a colliding hash code.
        return getTitle().hashCode();
    }

    public boolean equals(WikiName other)
    {
        if ((other == null) || (!(other instanceof WikiName))) {
            return false;
        }
        WikiName otherName = (WikiName) other;

        return equals(otherName.getTitle(), (getTitle())) && equals(otherName.getNamespace().equals(getNamespace()))
                        && equals(otherName.getRelation().equals(getRelation()));
    }

    private static boolean equals(Object a, Object b)
    {
        if (a == null)
            return b == null;
        return a.equals(b);
    }

    public int compareTo(WikiName other)
    {
        int byTitle = getTitle().compareTo(other.getTitle());
        if (byTitle != 0) {
            return byTitle;
        }
        if (getRelation() == null) {
            return other.getRelation() == null ? 0 : 1;
        }

        if (other.getRelation() == null) {
            return -1;
        }

        return getRelation().compareTo(other.getRelation());
    }

}

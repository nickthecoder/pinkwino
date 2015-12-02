/*
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
 */

package uk.co.nickthecoder.pinkwino.parser.tree;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.nickthecoder.pinkwino.parser.NodeFactory;

/**
 * Renders as a html br tag.
 */

public class CharacterEntity implements Node, NodeFactory
{

    protected static Logger _logger = LogManager.getLogger(CharacterEntity.class);

    // This list abtained from the wiki page :
    // http://en.wikipedia.org/wiki/List_of_XML_and_HTML_character_entity_references
    // Copy and pasted the table into a file, then issued the command :
    // sed -e 's/^/"/' -e 's/ .*/",/' foo | tr -d '\n\' > bar
    public static final String[] entityArray = { "quot", "amp", "apos", "lt", "gt", "nbsp", "iexcl", "cent", "pound",
                    "curren", "yen", "brvbar", "sect", "uml", "copy", "ordf", "laquo", "not", "shy", "reg", "macr",
                    "deg", "plusmn", "sup2", "sup3", "acute", "micro", "para", "middot", "cedil", "sup1", "ordm",
                    "raquo", "frac14", "frac12", "frac34", "iquest", "Agrave", "Aacute", "Acirc", "Atilde", "Auml",
                    "Aring", "AElig", "Ccedil", "Egrave", "Eacute", "Ecirc", "Euml", "Igrave", "Iacute", "Icirc",
                    "Iuml", "ETH", "Ntilde", "Ograve", "Oacute", "Ocirc", "Otilde", "Ouml", "times", "Oslash",
                    "Ugrave", "Uacute", "Ucirc", "Uuml", "Yacute", "THORN", "szlig", "agrave", "aacute", "acirc",
                    "atilde", "auml", "aring", "aelig", "ccedil", "egrave", "eacute", "ecirc", "euml", "igrave",
                    "iacute", "icirc", "iuml", "eth", "ntilde", "ograve", "oacute", "ocirc", "otilde", "ouml",
                    "divide", "oslash", "ugrave", "uacute", "ucirc", "uuml", "yacute", "thorn", "yuml", "OElig",
                    "oelig", "Scaron", "scaron", "Yuml", "fnof", "circ", "tilde", "Alpha", "Beta", "Gamma", "Delta",
                    "Epsilon", "Zeta", "Eta", "Theta", "Iota", "Kappa", "Lambda", "Mu", "Nu", "Xi", "Omicron", "Pi",
                    "Rho", "Sigma", "Tau", "Upsilon", "Phi", "Chi", "Psi", "Omega", "alpha", "beta", "gamma", "delta",
                    "epsilon", "zeta", "eta", "theta", "iota", "kappa", "lambda", "mu", "nu", "xi", "omicron", "pi",
                    "rho", "sigmaf", "sigma", "tau", "upsilon", "phi", "chi", "psi", "omega", "thetasym", "upsih",
                    "piv", "ensp", "emsp", "thinsp", "zwnj", "zwj", "lrm", "rlm", "ndash", "mdash", "lsquo", "rsquo",
                    "sbquo", "ldquo", "rdquo", "bdquo", "dagger", "Dagger", "bull", "hellip", "permil", "prime",
                    "Prime", "lsaquo", "rsaquo", "oline", "frasl", "euro", "image", "weierp", "real", "trade",
                    "alefsym", "larr", "uarr", "rarr", "darr", "harr", "crarr", "lArr", "uArr", "rArr", "dArr", "hArr",
                    "forall", "part", "exist", "empty", "nabla", "isin", "notin", "ni", "prod", "sum", "minus",
                    "lowast", "radic", "prop", "infin", "ang", "and", "or", "cap", "cup", "int", "there4", "sim",
                    "cong", "asymp", "ne", "equiv", "le", "ge", "sub", "sup", "nsub", "sube", "supe", "oplus",
                    "otimes", "perp", "sdot", "lceil", "rceil", "lfloor", "rfloor", "lang", "rang", "loz", "spades",
                    "clubs", "hearts", "diams" };

    public static final Map<String, String> entityMap;
    static {
        Map<String, String> tempMap = new HashMap<String, String>();
        for (int i = 0; i < entityArray.length; i++) {
            tempMap.put(entityArray[i], entityArray[i]);
        }
        entityMap = Collections.unmodifiableMap(tempMap);
    }

    private String _entityLabel;

    public CharacterEntity(String entityLabel) throws Exception
    {
        if (!entityMap.containsKey(entityLabel)) {
            throw new Exception("Unknown Entity : " + entityLabel);
        }

        _entityLabel = entityLabel;
    }

    public void render(StringBuffer buffer)
    {
        buffer.append("&").append(_entityLabel).append(";");
    }

    public void text(StringBuffer buffer)
    {
        buffer.append("&").append(_entityLabel).append(";");
    }

    public boolean isBlock()
    {
        return false;
    }

    public Node createNode()
    {
        return this;
    }

}

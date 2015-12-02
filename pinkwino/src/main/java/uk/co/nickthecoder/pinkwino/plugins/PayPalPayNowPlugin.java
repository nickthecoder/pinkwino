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

package uk.co.nickthecoder.pinkwino.plugins;

import uk.co.nickthecoder.pinkwino.WikiConstants;
import uk.co.nickthecoder.pinkwino.WikiDocument;
import uk.co.nickthecoder.pinkwino.parser.tree.JspNode;
import uk.co.nickthecoder.pinkwino.parser.tree.Node;
import uk.co.nickthecoder.pinkwino.util.Parameters;
import uk.co.nickthecoder.pinkwino.util.RegexParameterDescription;

/**
 * Renders a PayPal "Pay Now" button. To use this plugin, you need to set up the
 * plugin with the following code in your pinkwino config file :
 * 
 * <pre>
 * wikiEngine.getPluginManager().add(new PayPalPayNowPlugin(&quot;YOUR_EMAIL_ADDRESS&quot;, &quot;GBP&quot;, &quot;GB&quot;));
 * </pre>
 * 
 * Replace your email address, currency code, and locale are required.
 * 
 * Note, if your wiki requires more than one business code, currency code or
 * locale, then you can create more than one instance of this plugin, each with
 * different plugin names. i.e. use the second constructor.
 */

public class PayPalPayNowPlugin extends JspPlugin
{

    private String _business;
    private String _currencyCode;
    private String _lc;

    /**
     * business is the e-mail address that is used by PalPay to identify the
     * account that gets the money.
     * 
     * currencyCode is GBP for Great Britain Pounds.
     * 
     * lc is GB for Greaat Britain.
     * 
     * See PayPal's website for your own locale, and currency codes.
     */
    public PayPalPayNowPlugin(String business, String currencyCode, String lc)
    {
        this("payPalPayNow", business, currencyCode, lc);
    }

    public PayPalPayNowPlugin(String name, String business, String currencyCode, String lc)
    {
        super(name, "payPalPayNow.jsp", BODY_TYPE_WIKI);

        _business = business;
        _currencyCode = currencyCode;
        _lc = lc;

        addParameterDescription(new RegexParameterDescription("itemName", ".*", true, "", WikiConstants.USAGE_GENERAL));
        addParameterDescription(new RegexParameterDescription("amount", "[0-9.]*", true, "",
                        WikiConstants.USAGE_GENERAL));
    }

    protected Node decorateJspNode(JspNode jspNode, WikiDocument wikiDocument, Parameters parameters)
    {
        jspNode.setRequestAttribute("paypal_business", _business);
        jspNode.setRequestAttribute("paypal_currencyCode", _currencyCode);
        jspNode.setRequestAttribute("paypal_lc", _lc);

        return super.decorateJspNode(jspNode, wikiDocument, parameters);
    }

}

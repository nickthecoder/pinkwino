package uk.co.nickthecoder.pinkwino.plugins;

import uk.co.nickthecoder.pinkwino.parser.tree.VerbatimNode;
import uk.co.nickthecoder.pinkwino.util.ParameterDescription;
import uk.co.nickthecoder.pinkwino.util.Parameters;

/**
 * Adds a css class to an element on the page.
 * I use this to add a class to the "body" tag, which changes the layout of the page.
 * e.g. if a wiki page is wide, then make the "left navigation" appear at the bottom of the page.
 */
public class AddCSSPlugin extends AbstractVisualPlugin
{
    public AddCSSPlugin()
    {
        this( "addCSS");
    }
    
    public AddCSSPlugin(String name)
    {
        super(name, BODY_TYPE_NONE);
        this.addParameterDescription(ParameterDescription.find("id").defaultValue("body"));
        this.addParameterDescription(ParameterDescription.find("class").required());
    }

    @Override
    public Object doBegin(PluginSupport pluginSupport, Parameters parameters)
    {
        String id = parameters.getParameter("id").getValue();
        String cssClass = parameters.getParameter("class").getValue();
        
        String element;
        if (id.equals("body")) {
            element = "document.getElementsByTagName('body')[0]";
        } else if (id.equals("html")) {
            element = "document.documentElement";
        } else {
            element = "document.getElementById('" + id + "')";
        }
        String html = "\n<script><!--\nwindow.addEventListener('load', function() { " + element + ".className += ' " + cssClass + "';});\n--></script>\n";
        VerbatimNode node = new VerbatimNode(html);
        pluginSupport.add(node);
        
        return null;
    }
}

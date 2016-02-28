package uk.co.nickthecoder.pinkwino.plugins;

import uk.co.nickthecoder.pinkwino.WikiContext;
import uk.co.nickthecoder.pinkwino.WikiEngine;
import uk.co.nickthecoder.pinkwino.WikiName;
import uk.co.nickthecoder.pinkwino.WikiPage;
import uk.co.nickthecoder.pinkwino.parser.tree.VerbatimNode;
import uk.co.nickthecoder.pinkwino.util.Parameter;
import uk.co.nickthecoder.pinkwino.util.ParameterDescription;
import uk.co.nickthecoder.pinkwino.util.Parameters;

public class EditPlantChartPlugin extends AbstractVisualPlugin
{

    public EditPlantChartPlugin()
    {
        super("editPlantChart", BODY_TYPE_NONE);

        addParameterDescription(ParameterDescription.find("page").required());
    }

    @Override
    public Object doBegin(PluginSupport pluginSupport, Parameters parameters)
    {

        Parameter pageParameter = parameters.getParameter("page");
        if (pageParameter != null) {

            WikiName wikiName = WikiEngine.instance().getWikiNameFormat().parse(pluginSupport.getDefaultNamespace(), pageParameter.getValue());
            WikiPage wikiPage = wikiName.getWikiPage();
            if (!wikiPage.getExists()) {
                WikiEngine.instance().save(wikiPage, "", null);
            }
            String contextPath = WikiContext.getWikiContext().getServletRequest().getContextPath();
            String html = "\n"
                + "<script src=\"" + contextPath + "/extra/plantChart.js\"></script>\n"
                + "<script src=\"" + contextPath + "/extra/oneSquareFoot/js/jquery-1.8.2.js\"></script>\n"
                + "<script src=\"" + contextPath + "/extra/oneSquareFoot/js/jquery.json-2.3.js\"></script>\n"
                + "<script><!--\nplantChart.load('"
                + wikiPage.getRawUrl()
                + "','"
                + wikiPage.getSaveUrl()
                + "');\n--></script>";
            
            VerbatimNode htmlNode = new VerbatimNode(html);
            pluginSupport.add(htmlNode);
        }
        
        return null;
    }

}

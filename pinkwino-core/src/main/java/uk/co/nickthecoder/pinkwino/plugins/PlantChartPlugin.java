package uk.co.nickthecoder.pinkwino.plugins;

import java.util.HashMap;
import java.util.Map;

import uk.co.nickthecoder.pinkwino.WikiName;
import uk.co.nickthecoder.pinkwino.parser.tree.InternalLinkDestination;
import uk.co.nickthecoder.pinkwino.parser.tree.Link;
import uk.co.nickthecoder.pinkwino.parser.tree.PlainText;
import uk.co.nickthecoder.pinkwino.parser.tree.Table;
import uk.co.nickthecoder.pinkwino.parser.tree.TableCell;
import uk.co.nickthecoder.pinkwino.parser.tree.TableRow;
import uk.co.nickthecoder.pinkwino.util.BooleanParameter;
import uk.co.nickthecoder.pinkwino.util.BooleanParameterDescription;
import uk.co.nickthecoder.pinkwino.util.Parameter;
import uk.co.nickthecoder.pinkwino.util.ParameterDescription;
import uk.co.nickthecoder.pinkwino.util.Parameters;

public class PlantChartPlugin extends AbstractVisualPlugin
{
    public static final String[] headings =
        new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

    public Map<Character, String> cssClasses;

    public Map<Character, String> titleMap;

    public PlantChartPlugin()
    {
        super("plantChart", BODY_TYPE_TEXT);
        addParameterDescription(new BooleanParameterDescription("labels", false, "false",
            ParameterDescription.USAGE_GENERAL));

        cssClasses = new HashMap<Character, String>();
        cssClasses.put('X', ""); // Blank
        cssClasses.put('I', "sowIndoors");
        cssClasses.put('S', "sow");
        cssClasses.put('P', "plantOut");
        cssClasses.put('H', "harvest");
        cssClasses.put('F', "flowers");
        cssClasses.put('C', "cuttings");
        cssClasses.put('O', "other");
        cssClasses.put('?', "other"); // Backwards compatible only, use "O" instead.

        titleMap = new HashMap<Character, String>();
        titleMap.put('I', "Sow Indoors");
        titleMap.put('S', "Sow Outdoors");
        titleMap.put('P', "Plant Out");
        titleMap.put('H', "Harvest");
        titleMap.put('F', "Flowers");
        titleMap.put('C', "Take Cuttings");
        titleMap.put('O', "Other (see wiki page for details)");

    }

    @Override
    public Object doBegin(PluginSupport pluginSupport, Parameters parameters)
    {
        return null;
    }

    @Override
    public void bodyText(PluginSupport pluginSupport, Parameters parameters, String text, Object pluginState)
    {
        Parameter labelsParameter = parameters.getParameter("labels");
        boolean labels = labelsParameter instanceof BooleanParameter ? ((BooleanParameter) labelsParameter)
            .getBooleanValue() : false;

        Table table = new Table();
        table.addParameter(new Parameter(ParameterDescription.find("class"), "plantChart"));
        pluginSupport.begin(table);

        TableRow headingRow = new TableRow();
        table.add(headingRow);

        if (labels) {
            TableCell first = new TableCell(true);
            headingRow.add(first);
        }

        for (int i = 0; i < 12; i++) {
            TableCell cell = new TableCell(true);
            cell.addParameter(new Parameter(ParameterDescription.find("colspan"), "2"));

            cell.add(new PlainText(headings[i]));
            headingRow.add(cell);
        }

        String[] lines = text.split("[\\r\\n]+");
        TableCell labelCell = null;
        int colSpanCount = 1;

        for (String line : lines) {
            if (line.trim().length() == 0)
                continue;

            TableRow row = new TableRow();
            table.add(row);

            String label = null;
            if (labels && (labelCell != null) && (line.startsWith(" "))) {
                colSpanCount += 1;
            } else {
                if (labels) {
                    int space = line.indexOf("|");
                    if (space < 0) {
                        space = line.indexOf(" ");
                    }
                    if (space >= 0) {
                        label = line.substring(0, space);
                        line = line.substring(space + 1);
                    } else {
                        label = "";
                    }

                    if ((labelCell != null) && (colSpanCount > 1)) {
                        labelCell.addParameter(new Parameter(ParameterDescription.find("rowspan"), Integer
                            .toString(colSpanCount)));

                        colSpanCount = 1;
                        labelCell = null;
                    }
                }
            }

            if (label != null) {
                labelCell = new TableCell(true);
                labelCell.addParameter(new Parameter(ParameterDescription.find("class"), "label"));
                row.add(labelCell);

                WikiName name = new WikiName(pluginSupport.getDefaultNamespace(), label);
                if (name.getExists()) {
                    Link link = new Link();
                    link.setDestination(new InternalLinkDestination(name));
                    link.add(new PlainText(label));
                    labelCell.add(link);
                } else {
                    labelCell.add(new PlainText(label));
                }

            }

            for (char c : line.trim().toCharArray()) {
                String cssClass = cssClasses.get(Character.toUpperCase(c));
                if (cssClass == null)
                    continue;

                TableCell cell = new TableCell(false);
                row.add(cell);
                if ((!Character.isAlphabetic(c)) || Character.isUpperCase(c)) {
                    cell.addParameter(new Parameter(ParameterDescription.find("colspan"), "2"));
                }
                if (cssClass.length() > 0) {
                    cell.addParameter(new Parameter(ParameterDescription.find("class"), cssClass));
                }
                String title = titleMap.get(Character.toUpperCase(c));
                if (title != null) {
                    cell.addParameter(new Parameter(ParameterDescription.find("title"), title));
                }

            }

        }

        if ((labelCell != null) && (colSpanCount > 1)) {
            labelCell.addParameter(new Parameter(ParameterDescription.find("rowspan"), Integer
                .toString(colSpanCount)));
        }

        pluginSupport.end(table);

    }

}

package uk.co.nickthecoder.pinkwino.parser.tree;


public class VerbatimNode implements Node
{
    private String _html;

    public VerbatimNode(String html)
    {
        _html = html;
    }

    public void render(StringBuffer buffer)
    {
        buffer.append(_html);
    }

    public void text(StringBuffer buffer)
    {
        buffer.append(_html);
    }

    public boolean isBlock()
    {
        return true;
    }

    public String toString()
    {
        return "HTML:'" + _html + "'";
    }

}

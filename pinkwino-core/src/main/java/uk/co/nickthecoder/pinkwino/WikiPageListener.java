package uk.co.nickthecoder.pinkwino;

public interface WikiPageListener
{
    public void onSave(WikiPage wikiPage) throws Exception;

    public void onDelete(WikiPage wikiPage) throws Exception;
}

package uk.co.nickthecoder.pinkwino;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Listens to when a wiki page has been edited or deleted, and informs an
 * external entity by way of a HTTP POST message.
 * 
 * The post can have one of two parameters. Either a page name (i.e. the
 * formatted name of the wiki page that has changed). Or the URL of the view
 * page for that wiki page.
 */
public class PostWikiPageListener implements WikiPageListener
{
    private static final Logger _logger = LogManager.getLogger(PostWikiPageListener.class);

    private String _onSaveURL;

    private String _onDeleteURL;

    private String _pageNameParameter;

    private String _viewURLParameter;

    private String _submitParameter;
    
    private String _submitParameterValue;
    
    /**
     * The same URL is used for both pages that have been edited, and those that
     * have been deleted.
     * 
     * @param baseURL
     *            The url of the external entity that is to be informed of edits
     *            and deletes.
     */
    public PostWikiPageListener(String url)
    {
        this(url, url);
    }

    public PostWikiPageListener(String onSaveURL, String onDeleteURL)
    {
        _onSaveURL = onSaveURL;
        _onDeleteURL = onDeleteURL;
    }

    /**
     * Set the name of the parameter, which will hold the wiki page's formatted
     * name.
     * 
     * @param parameterName
     * @return this (fluent API)
     */
    public PostWikiPageListener pageNameParameter(String parameterName)
    {
        _pageNameParameter = parameterName;
        return this;
    }

    /**
     * Set the name of the parameter, which will hold the wiki page's view url.
     * 
     * @param parameterName
     * @return this (fluent API)
     */
    public PostWikiPageListener viewURLParameter(String parameterName)
    {
        _viewURLParameter = parameterName;
        return this;
    }

    /**
     * Sets the name and value of an additional parameter. If we are mimicing a form submission, 
     * then this will be the name and value of the submit button.
     * @param name
     * @param value
     * @return this (flued API)
     */
    public PostWikiPageListener submitParameter( String name, String value )
    {
        _submitParameter = name;
        _submitParameterValue = value;
        return this;
    }
    
    @Override
    public void onSave(WikiPage wikiPage) throws Exception
    {
        doPost(_onSaveURL, wikiPage);
    }

    @Override
    public void onDelete(WikiPage wikiPage) throws Exception
    {
        doPost(_onDeleteURL, wikiPage);
    }

    protected void doPost(String url, WikiPage wikiPage)
    {
        try {

            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);

            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            if (_viewURLParameter != null) {
                formparams.add(new BasicNameValuePair(_viewURLParameter, wikiPage.getViewUrl()));
            }
            if (_pageNameParameter != null) {
                formparams.add(new BasicNameValuePair(_pageNameParameter, wikiPage.getWikiName().getFormatted()));
            }
            if (_submitParameter != null) {
                formparams.add(new BasicNameValuePair(_submitParameter, _submitParameterValue ));
            }
            
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
            httpPost.setEntity(entity);

            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() != 200) {
                    _logger.info("Post to " + url + " for page " + wikiPage.getWikiName().getFormatted()
                                    + " returned response code : " + statusLine.getStatusCode());
                }
            } finally {
                response.close();
            }

        } catch (Exception e) {
            _logger.info("Failed to POST to : " + url + " for page : " + wikiPage.getWikiName().getFormatted());
        }
    }
}

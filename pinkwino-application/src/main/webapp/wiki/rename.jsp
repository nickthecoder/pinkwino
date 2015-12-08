<%@ page contentType="text/html" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://nickthecoder.co.uk/webwidgets" prefix="ww" %>
<%@ taglib uri="http://nickthecoder.co.uk/pinkwino" prefix="pw" %>
 
<tiles:insert template="${wikiEngine.urlManager.template}" flush="true">

  <tiles:put name="title" type="string">Rename : <c:out value="${wikiPage.wikiName.title}"/></tiles:put>

  <tiles:put name="wikiPage" value="${wikiPage}"/>

  <tiles:put name="pageType" value="rename"/>

  <tiles:put name="content" type="string" >

    <form name="deleteForm" id="deleteForm" class="wiki_form" method="post" action="<c:out value="${wikiPage.renameUrl}"/>">

        <input type="hidden" name="page" value="<c:out value="${wikiPage.wikiName.formatted}"/>"/>
        <input type="hidden" name="referrer" value="<ww:referrer/>"/>

        <p>
          Enter the new name for the wiki page.
        </p>
        <p>
            Name : <input name="newName" type="text" length="30" value="${wikiPage.wikiName.formatted}"/>
        </p>
        
        <div class="pw_buttons">
            <button name="rename" type="submit" value="1">Rename</button>
            <button name="cancel" type="submit" value="1">Cancel</button>
        </div>

    </form>

  </tiles:put>

</tiles:insert>


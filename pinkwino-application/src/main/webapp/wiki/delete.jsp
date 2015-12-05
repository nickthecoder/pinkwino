<%@ page contentType="text/html" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://nickthecoder.co.uk/webwidgets" prefix="ww" %>
<%@ taglib uri="http://nickthecoder.co.uk/pinkwino" prefix="pw" %>

<tiles:insert template="${wikiEngine.urlManager.template}" flush="true">

    <tiles:put name="title" type="string">Delete : <pw:wikiName wikiName="${wikiPage.wikiName}"/></tiles:put>

    <tiles:put name="wikiPage" value="${wikiPage}"/>

    <tiles:put name="pageType" value="confirmDelete"/>

    <tiles:put name="content" type="string" >

        <form name="deleteForm" id="deleteForm" method="post" action="${wikiPage.deleteUrl}">

            <input type="hidden" name="page" value="<c:out value="${wikiPage.wikiName.formatted}"/>"/>
            <input type="hidden" name="referrer" value="<ww:referrer/>"/>

            <p>
                Are you sure you want to delete this page?
            </p>

            <div class="wiki_buttons">
                <button name="delete" type="submit" value="1">Ok</button>
                <button name="cancel" type="submit" value="1">Cancel</button>
            </div>

        </form>

        <br/>

        <div class="wiki_content">
            ${version.rendered}
        </div>

    </tiles:put>

</tiles:insert>


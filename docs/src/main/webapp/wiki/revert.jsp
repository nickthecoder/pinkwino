<%@ page contentType="text/html" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://nickthecoder.co.uk/webwidgets" prefix="ww" %>
<%@ taglib uri="http://nickthecoder.co.uk/pinkwino" prefix="pw" %>

<tiles:insert template="${wikiEngine.urlManager.template}" flush="true">

  <tiles:put name="wikiPage" value="${wikiPage}"/>

  <tiles:put name="pageType" value="edit"/>

  <tiles:put name="title" type="string">Revert : <pw:wikiName wikiName="${wikiPage.wikiName}"/></tiles:put>

  <tiles:put name="fullWidth" type="string" >

    <c:if test="${ ! empty message}">
      <div class="wiki_warning">
        <p>
          <c:out value="${message}"/>
        </p>
      </div>
    </c:if>

    <div class="wiki_info">
      <p>
        Check that this is the version that you want to revert back to, and the click save to revert to this version.
      </p>
      <p>
        Note, this does <i>not</i> delete any versions, it creates a new version, with identical text.
      </p>
    </div>

    <tiles:insert template="editForm.jsp" flush="true">
      <tiles:put name="wikiPage" value="${wikiPage}"/>
      <tiles:put name="markup" type="string">${markup}</tiles:put>
    </tiles:insert>


  </tiles:put>

</tiles:insert>


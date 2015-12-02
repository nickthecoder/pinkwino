<%@ page contentType="text/html" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://nickthecoder.co.uk/webwidgets" prefix="ww" %>
<%@ taglib uri="http://nickthecoder.co.uk/pinkwino" prefix="pw" %>

<tiles:insert template="${wikiEngine.urlManager.template}">

  <tiles:put name="wikiPage" value="${wikiPage}"/>

  <tiles:put name="pageType" value="edit"/>

  <tiles:put name="title" type="string">Edit : <pw:wikiName wikiName="${wikiPage.wikiName}"/></tiles:put>

  <tiles:put name="content" type="string" >

    <c:if test="${ ! empty message}">
      <div class="wiki_warning">
        <p>
          <c:out value="${message}"/>
        </p>
      </div>
    </c:if>

    <tiles:insert template="editForm.jsp" flush="false">
      <tiles:put name="wikiPage" value="${wikiPage}"/>
      <tiles:put name="markup" type="string">${markup}</tiles:put>

      <c:if test="${ ! empty message }">
        <tiles:put name="markup" type="string" value="${param.markup}"/>
      </c:if>

    </tiles:insert>

  </tiles:put>

</tiles:insert>


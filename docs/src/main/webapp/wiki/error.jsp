<%@ page contentType="text/html" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://nickthecoder.co.uk/webwidgets" prefix="ww" %>
<%@ taglib uri="http://nickthecoder.co.uk/pinkwino" prefix="pw" %>

<tiles:insert template="${wikiEngine.urlManager.template}" flush="true">

  <tiles:put name="wikiPage" value="${wikiPage}"/>
  <tiles:put name="pageType" value="error"/>
  <tiles:put name="title" type="string">Error</tiles:put>

  <tiles:put name="content" type="string" >

    <c:if test="${ ! empty message}">
      <div class="wiki_warning">
        <p>
          <c:out value="${message}"/>
        </p>
      </div>
    </c:if>

    <c:if test="${empty message}">
    <p>
      An unexpected error occurrered while processing the wiki component.
    </p>

    <p>
      If the problem persists, please report the problem to the web site owner.
    </p>
  </c:if>

  </tiles:put>

</tiles:insert>


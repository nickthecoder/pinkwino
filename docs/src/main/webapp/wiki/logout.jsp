<%@ page contentType="text/html" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://nickthecoder.co.uk/webwidgets" prefix="ww" %>
<%@ taglib uri="http://nickthecoder.co.uk/pinkwino" prefix="pw" %>

<tiles:insert template="${wikiEngine.urlManager.template}" flush="true">

  <tiles:put name="wikiPage" value="${wikiPage}"/>

  <tiles:put name="pageType" value="login"/>

  <tiles:put name="title" type="string">Logout</tiles:put>

  <tiles:put name="content" type="string" >

    <p>
      You are now logged out.
    </p>

    <p>
      [ <a href="${wikiEngine.defaultWikiPage.viewUrl}"><c:out value="${wikiEngine.defaultPageName}"/></a> ]
    </p>

  </tiles:put>

</tiles:insert>


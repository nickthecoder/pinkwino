<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://nickthecoder.co.uk/webwidgets" prefix="ww" %>
<%@ taglib uri="http://nickthecoder.co.uk/pinkwino" prefix="pw" %>

<!-- lists pages with have been changed recently -->

<table class="wiki_table">
  <tr>
    <th>Page</th>
    <c:if test="${empty parameters.namespace}"><th>Namespace</th></c:if>
    <th>Last Modified</th>
  </tr>

  <ww:sort items="${wikiPages}" var="sorted" field="lastModified" reverse="true"/>

  <c:forEach items="${sorted}" var="wikiPage">

    <tr>
      <td>
        <a href="${wikiPage.viewUrl}"><c:out value="${wikiPage.wikiName.title}"/></a>
      </td>
      <c:if test="${empty parameters.namespace}">
      <td>
        <c:out value="${wikiPage.wikiName.namespace.name}"/>
      </td>
      </c:if>
      <td>
        <c:out value="${wikiPage.lastModified}"/>
      </td>
    </tr>

  </c:forEach>
</table>


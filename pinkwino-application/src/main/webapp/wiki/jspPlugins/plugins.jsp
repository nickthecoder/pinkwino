<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://nickthecoder.co.uk/webwidgets" prefix="ww" %>
<%@ taglib uri="http://nickthecoder.co.uk/pinkwino" prefix="pw" %>

<!-- Lists all plugins -->

<table class="wiki_table">
  <tr>
    <th>Name</th>
    <th>About</th>
    <th>Body Type</th>
  </tr>

  <ww:sort items="${wikiEngine.pluginManager.visualPlugins}" var="visualPlugins"/>
  <c:forEach items="${visualPlugins}" var="plugin">

    <tr>
      <td><c:out value="${plugin.name}"/></td>
      <td>
        <pw:wikiPage namespace="wiki" title="${plugin.name} (plugin)">
          <a href="${wikiPage.viewUrl}">about</a>
        </pw:wikiPage>
      </td>
      <td align="center">
        <c:choose>
          <c:when test="${plugin.bodyType == 0}">none</c:when>
          <c:when test="${plugin.bodyType == 1}">wiki</c:when>
          <c:when test="${plugin.bodyType == 2}">text</c:when>
          <c:otherwise>?</c:otherwise>
        </c:choose>
      </td>
    </tr>

  </c:forEach>
</table>


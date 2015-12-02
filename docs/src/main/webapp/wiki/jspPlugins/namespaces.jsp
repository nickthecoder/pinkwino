<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://nickthecoder.co.uk/webwidgets" prefix="ww" %>
<%@ taglib uri="http://nickthecoder.co.uk/pinkwino" prefix="pw" %>

<!-- lists all namespaces -->

<table class="wiki_table">
  <tr>
    <th>Namespace</th>
    <th>Home Page</th>
    <th>Index</th>
  </tr>

  <c:forEach items="${wikiEngine.namespaces}" var="namespace">
    <tr>

      <th>
        <c:out value="${namespace.name}"/>
      </th>
      <td>
        <pw:wikiPage namespace="${namespace.name}" title="${namespace.homeName.title}">
          <a href="${wikiPage.viewUrl}"><c:out value="${namespace.homeName}"/></a>
        </pw:wikiPage>
      </td>

      <td>
        <pw:wikiPage namespace="${namespace.name}" title="${namespace.indexName.title}">
          <a href="${wikiPage.viewUrl}">index</a>
        </pw:wikiPage>
      </td>
    </tr>
  </c:forEach>

</table>

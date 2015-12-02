<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://nickthecoder.co.uk/webwidgets" prefix="ww" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- Gives details about a single plugin -->

<h3>Plugin Name : <c:out value="${plugin.name}"/></h3>

Body type :
<c:if test="${plugin.bodyType == 0}">none</c:if>
<c:if test="${plugin.bodyType == 1}">wiki markup</c:if>
<c:if test="${plugin.bodyType == 2}">text / special</c:if>

<h3>Parameters</h3>
<c:choose>
  <c:when test="${fn:length(plugin.parameterDescriptions.parameterDescriptions) == 0}">
    none
  </c:when>

  <c:otherwise>
    <table class="wiki_table">
      <tr>
        <th>Name</th>
        <th>Details</th>
        <th>Default</th>
        <th>Required</th>
      </tr>

      <c:forEach items="${plugin.parameterDescriptions.parameterDescriptions}" var="parameterDescription">
        <tr>
          <td><c:out value="${parameterDescription.name}"/></td>
          <td><c:out value="${parameterDescription.summary}"/></td>
          <td><c:out value="${parameterDescription.defaultValue}"/></td>
          <td><c:out value="${parameterDescription.required}"/></td>
        </tr>
      </c:forEach>
    </table>
  </c:otherwise>
</c:choose>


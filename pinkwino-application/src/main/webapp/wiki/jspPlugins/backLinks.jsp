<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://nickthecoder.co.uk/webwidgets" prefix="ww" %>
<%@ taglib uri="http://nickthecoder.co.uk/pinkwino" prefix="pw" %>

<ul class="wiki_backlinks">
  <c:forEach items="${wikiPage.backLinks.iterator}" var="backLink">
    <li><a href="${backLink.wikiName.wikiPage.viewUrl}"><c:out value="${backLink.wikiName.title}"/></a></li>
  </c:forEach>
</ul>


<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://nickthecoder.co.uk/webwidgets" prefix="ww" %>
<%@ taglib uri="http://nickthecoder.co.uk/pinkwino" prefix="pw" %>

<ww:sort items="${wikiPage.backLinks.iterator}" var="backLinks" field="wikiName"/>
<ul class="wiki_backlinks">
  <c:forEach items="${backLinks}" var="backLink">
    <li><a href="${backLink.wikiName.wikiPage.viewUrl}"><c:out value="${backLink.wikiName.title}"/></a></li>
  </c:forEach>
</ul>


<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

This is a test
${jspNode}

<ul>
<c:forEach items="${jspNode.children}" var="child">
  <li>${child}</li>
</c:forEach>
</ul>


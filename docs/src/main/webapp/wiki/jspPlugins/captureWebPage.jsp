<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<form name="captureWebPageForm" action="<c:out value="${captureWebPage_url}"/>" method="POST">

  <input type="hidden" name="captureWebPage_key" value="<c:out value="${captureWebPage_key}"/>"/>
  <button name="captureWebPage" type="submit" value="1"><c:out value="${parameters.label.value}"/></button>

</form>


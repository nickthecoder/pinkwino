<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${!empty makeStatic_allow}">
  <form name="makeStaticForm" action="<c:out value="${makeStatic_url}"/>" method="POST">

    <input type="hidden" name="makeStatic_page" value="<c:out value="${parameters.log.value}"/>"/>
    <button name="makeStatic" type="submit" value="1"><c:out value="${parameters.label.value}"/></button>

  </form>
</c:if>

<c:if test="${! empty makeStatic_message}">
  <div class="wiki_message">
    <c:out value="${makeStatic_message}"/>
  </div>
</c:if>


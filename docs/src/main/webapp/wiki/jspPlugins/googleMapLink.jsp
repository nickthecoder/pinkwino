<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<a href="#" <c:choose
    ><c:when test="${! empty parameters.target.value }">onclick="return googleMap_setTarget('<c:out value="${parameters.target.value}"/>','<c:out value="${parameters.longitude.value}"/>','<c:out value="${parameters.latitude.value}"/>','<c:out value="${parameters.zoom.value}"/>');"</c:when
    ><c:otherwise>onclick="return googleMap_setStart('<c:out value="${parameters.start.value}"/>','<c:out value="${parameters.startLongitude.value}"/>','<c:out value="${parameters.startLatitude.value}"/>');"</c:otherwise
  ></c:choose
>><c:choose
  ><c:when test="${ empty parameters.label.value }"><c:out value="${parameters.start.value}"/><c:if test="${(! empty parameters.target.value) && (! empty parameters.start.value) }"> to <c:out value="${parameters.target.value}"/></c:if></c:when
  ><c:otherwise><c:out value="${parameters.label.value}"/></c:otherwise
></c:choose
></a>


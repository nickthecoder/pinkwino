<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://nickthecoder.co.uk/webwidgets" prefix="ww" %>

<ww:box className="${parameters['class'].value}" width="${parameters['width'].value}">
  <ww:boxTitle>
    <c:out value="${parameters['title'].value}"/>
  </ww:boxTitle>
  <ww:boxContent>
    ${jspNode.renderChildren}
  </ww:boxContent>
</ww:box>


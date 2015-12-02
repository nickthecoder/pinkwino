<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://nickthecoder.co.uk/webwidgets" prefix="ww" %>

<!--
  Renders the body of the tag twice, once as normal, again again, so that the
  user can see the html code
-->

<table class="wiki_table" width="100%">
  <tr>
    <td>
      ${jspNode.renderChildren}
    </td>
    <td>
<pre>
<c:out value="${jspNode.renderChildren}"/>
</pre>
    </td>
  </tr>
</table>


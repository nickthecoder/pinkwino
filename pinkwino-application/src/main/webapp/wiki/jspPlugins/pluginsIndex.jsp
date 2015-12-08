<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://nickthecoder.co.uk/webwidgets" prefix="ww" %>
<%@ taglib uri="http://nickthecoder.co.uk/pinkwino" prefix="pw" %>

<ww:sort items="${wikiEngine.pluginManager.visualPlugins}" var="visualPlugins"/>
<ul>
<c:forEach items="${visualPlugins}" var="plugin">

  <pw:wikiPage namespace="wiki" title="${plugin.name} (plugin)">
      <li><a href="${wikiPage.viewUrl}"><c:out value="${plugin.name}"/></a></li>
  </pw:wikiPage>

</c:forEach>
</ul>

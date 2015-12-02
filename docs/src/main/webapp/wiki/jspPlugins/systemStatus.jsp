<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://nickthecoder.co.uk/webwidgets" prefix="ww" %>
<%@ taglib uri="http://nickthecoder.co.uk/pinkwino" prefix="pw" %>

<h2>System Status</h2>

<div class="wiki_toc">
  <ol>
    <li><a href="#general">General</a></li>
    <li><a href="#namespaces">Namespaces</a></li>
    <li><a href="#attributes">Attributes</a></li>
    <li><a href="#externalLinks">External Links</a></li>
    <li><a href="#mimeTypes">Mime Types</a></li>
    <li><a href="#syntax">Syntax</a></li>
    <li><a href="#plugins">Plugins</a></li>
    <li><a href="#urlManager">Url Manager</a></li>
  </ol>
</div>

<!-- {{{ template -->
<!--
<a name="template"></a>
<h3>Name</h3>
<table class="wiki_table">

  <tr>
    <th>Property</th>
    <th>Value</th>
  </tr>

  <tr>
  </tr>
</table>
-->
<!-- end }}} -->


<!-- {{{ general  -->
<a name="general"></a>
<h3>General</h3>
<table class="wiki_table">

  <tr>
    <th>Property</th>
    <th>Value</th>
  </tr>

  <tr>
    <td>Home Page</td>
    <td>
      <pw:wikiPage namespace="${wikiEngine.defaultWikiPage.namespace.name}" title="${wikiEngine.defaultWikiPage.wikiName.title}">
        <a href="${wikiPage.viewUrl}"><c:out value="${wikiEngine.defaultPageName}"/></a>
      </pw:wikiPage>
    </td>
  </tr>

  <tr>
    <td>Default Namespace</td>
    <td><c:out value="${wikiEngine.defaultNamespace}"/></td>
  </tr>

  <tr>
    <td>User Namespace</td>
    <td><c:out value="${wikiEngine.userNamespace}"/></td>
  </tr>

  <tr>
    <td>Message Namespace</td>
    <td><c:out value="${wikiEngine.messageNamespace}"/></td>
  </tr>

  <tr>
    <td>Temporary Directory</td>
    <td><c:out value="${wikiEngine.mediaManager.tempDirectory}"/></td>
  </tr>

  <tr>
    <td>Wiki Name Format</td>
    <td><c:out value="${wikiEngine.wikiNameFormat.class.name}"/></td>
  </tr>

  <tr>
    <td>Renderer</td>
    <td><c:out value="${wikiEngine.renderer.class.name}"/></td>
  </tr>

  <tr>
    <td>Authentication Manager</td>
    <td><c:out value="${wikiEngine.authenticationManager.class.name}"/></td>
  </tr>

  <tr>
    <td>Authorisation Manager</td>
    <td><c:out value="${wikiEngine.authorisationManager.class.name}"/></td>
  </tr>

  <tr>
    <td>Meta Data</td>
    <td><c:out value="${wikiEngine.metaData.class.name}"/></td>
  </tr>

</table>
<!-- general }}} -->

<!-- {{{ namespaces  -->
<a name="namespaces"></a>
<h3>Namespaces</h3>
<table class="wiki_table">

  <tr>
    <th>Namespace</th>
    <th>Index</th>
    <th>Storage</th>
  </tr>

  <c:forEach items="${wikiEngine.namespaces}" var="namespace">
  <tr>

    <td>
      <pw:wikiPage namespace="${namespace.name}" title="${namespace.homeName.title}">
        <a href="${wikiPage.viewUrl}"><c:out value="${namespace.name}"/></a>
      </pw:wikiPage>
    </td>

    <td>
      <pw:wikiPage namespace="${namespace.name}" title="${namespace.indexName.title}">
        <a href="${wikiPage.viewUrl}">index</a>
      </pw:wikiPage>
    </td>


    <td><c:out value="${namespace.storage}"/></td>

  </tr>
  </c:forEach>

</table>


<!-- end namespaces }}} -->

<!-- {{{ attributes -->
<a name="attributes"></a>
<h3>Attributes</h3>
<table class="wiki_table">

  <tr>
    <th>Name</th>
    <th>Value</th>
  </tr>

  <ww:sort items="${wikiEngine.attributes}" var="sorted"/>
  <c:forEach items="${sorted}" var="key">
  <tr>

    <td><c:out value="${key}"/></td>
    <td><c:out value="${wikiEngine.attributes[key]}"/></td>

  </tr>
  </c:forEach>

</table>
<!-- end plugins }}} -->

<!-- {{{ external links -->
<a name="externalLinks"></a>
<h3>External Links</h3>
<table class="wiki_table">

  <tr>
    <th>Prefix</th>
    <th>Template</th>
    <th>Escape</th>
    <th>css Class</th>
  </tr>

  <ww:sort items="${wikiEngine.externalLinkManager.externalLinkTypes}" var="sorted"/>

  <c:forEach items="${sorted}" var="externalLinkType">
  <tr>

    <td><c:out value="${externalLinkType.prefix}"/></td>
    <td><c:out value="${externalLinkType.template}"/></td>
    <td><c:out value="${externalLinkType.escape}"/></td>
    <td>
      <c:choose>
      <c:when test="${! empty externalLinkType.cssClass}">
        <a href="#" class="${externalLinkType.cssClass}"><c:out value="${externalLinkType.cssClass}"/></a>
      </c:when>
      <c:otherwise>
        none
      </c:otherwise>
      </c:choose>
    </td>

  </tr>
  </c:forEach>

</table>
<!-- end }}} -->

<!-- {{{ mime types -->
<a name="mimeTypes"></a>
<h3>Mime Types</h3>
<table class="wiki_table">

  <tr>
    <th>Mime Type</th>
    <th>File Extensions</th>
  </tr>

  <ww:sort items="${wikiEngine.mediaManager.mimeTypes}" var="sorted"/>

  <c:forEach items="${sorted}" var="mimeType">
  <tr>

    <td><c:out value="${mimeType.mimeType}"/></td>
    <td><c:out value="${mimeType.extensions}"/></td>

  </tr>
  </c:forEach>

</table>
<!-- end mime types }}} -->


<!-- {{{ syntax-->
<a name="syntax"></a>
<h3>Syntax</h3>
<table class="wiki_table">

  <tr>
    <th>Prefix</th>
    <th>Java Class</th>
    <th>Line ?</th>
  </tr>

  <c:forEach items="${wikiEngine.syntaxManager.wikiLineSyntax}" var="syntax">
  <tr>

    <td><c:out value="${syntax.prefix}"/></td>
    <td>${syntax.class.name}</td>
    <td>true</td>

  </tr>
  </c:forEach>
  <c:forEach items="${wikiEngine.syntaxManager.wikiSyntax}" var="syntax">
  <tr>

    <td><c:out value="${syntax.prefix}"/></td>
    <td>${syntax.class.name}</td>
    <td>false</td>

  </tr>
  </c:forEach>

</table>
<!-- end syntax }}} -->

<!-- {{{ plugins -->
<a name="plugins"></a>
<h3>Plugins</h3>
<table class="wiki_table">

  <tr>
    <th>Name</th>
    <th>Java Class</th>
    <th>Body Type</th>
  </tr>

  <ww:sort items="${wikiEngine.pluginManager.visualPlugins}" var="visualPlugins"/>
  <c:forEach items="${visualPlugins}" var="plugin">

    <tr>
      <td>
        <pw:wikiPage namespace="wiki" title="${plugin.name} (plugin)">
          <a href="${wikiPage.viewUrl}"><c:out value="${plugin.name}"/></a>
        </pw:wikiPage>
      </td>

      <td>
        ${plugin.class.name}
      </td>

      <td align="center">
        <c:choose>
          <c:when test="${plugin.bodyType == 0}">none</c:when>
          <c:when test="${plugin.bodyType == 1}">wiki</c:when>
          <c:when test="${plugin.bodyType == 2}">text</c:when>
          <c:otherwise>?</c:otherwise>
        </c:choose>
      </td>
    </tr>

  </c:forEach>

</table>
<!-- end plugins }}} -->

<!-- {{{ url manager-->
<a name="urlManager"></a>
<h3>URL Manager</h3>
<table class="wiki_table">

  <tr>
    <th>View</th>
    <th>URL Template</th>
  </tr>

  <ww:sort items="${wikiEngine.urlManager.urls}" var="sorted"/>

  <c:forEach items="${sorted}" var="key">
  <tr>

    <td><c:out value="${key}"/></td>
    <td><c:out value="${wikiEngine.urlManager.urls[key]}"/></td>

  </tr>
  </c:forEach>

</table>
<!-- end }}} -->



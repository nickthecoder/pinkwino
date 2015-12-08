<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://nickthecoder.co.uk/webwidgets" prefix="ww" %>
<%@ taglib uri="http://nickthecoder.co.uk/pinkwino" prefix="pw" %>

<tiles:insert template="layout.jsp">

  <tiles:importAttribute name="pageType"/>
  <tiles:importAttribute name="title"/>
  <tiles:put name="title" type="string">${title}</tiles:put>

  <c:if test="${pageType == 'view'}">
    <c:choose>
      <c:when test="${empty version.wikiDocument.namedContents.aside}">
        <pw:wikiPage wikiPageVar="navigation" namespace="${wikiPage.namespace.name}" title="navigation">
          <c:if test="${navigation.exists}">
            <tiles:put name="navigation" type="string">
              <div class="wiki_noEdit">
                ${navigation.rendered}
              </div>
            </tiles:put>
          </c:if>
        </pw:wikiPage>
        </c:when>
        <c:otherwise>
          <tiles:put name="navigation" type="string">
            ${version.wikiDocument.namedContents.aside}
          </tiles:put>
        </c:otherwise>
      </c:choose>
  </c:if>

  <tiles:put name="content" type="string" >

    <h1>${title}</h1>

    <div class="wiki_editSection wiki_editSection1">
        <c:if test="${pageType == 'view'}">
            <span class="wiki_tool"><a href="${wikiPage.infoUrl}">Info</a></span>
            <span class="wiki_tool"><a href="${wikiPage.editUrl}">Edit</a></span>
        </c:if>
        <c:if test="${pageType == 'info'}">
            <span class="wiki_tool"><a href="${wikiPage.viewUrl}">View</a></span>
            <span class="wiki_tool"><a href="${wikiPage.editUrl}">Edit</a></span>
        </c:if>
    </div>

    <tiles:insert attribute="content" ignore="true" flush="false"/>
            
  </tiles:put>
</tiles:insert>

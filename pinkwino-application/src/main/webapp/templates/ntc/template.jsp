<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://nickthecoder.co.uk/webwidgets" prefix="ww" %>
<%@ taglib uri="http://nickthecoder.co.uk/pinkwino" prefix="pw" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


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

    <c:if test="${pageType == 'view'}">
    <c:if test="${wikiPage.namespace.name == 'ingredient'}">
        <h2><c:out value="${wikiPage.wikiName.title}"/> Referenced By</h2>
        <div class="wiki_metaData_section">
        
            <ul class="wiki_infoList">
              <c:forEach var="searchResult" items="${wikiPage.backLinks.iterator}">
                <li><a href="<c:out value="${searchResult.wikiName.wikiPage.viewUrl}"/>"><c:out value="${searchResult.wikiName.title}"/></a></li>
              </c:forEach>
            </ul>
        </div>
    </c:if>    
    </c:if>
    
  </tiles:put>
  
  <tiles:put name="footer" type="string">
      <!-- related pages -->
        <c:if test="${fn:length( wikiPage.relatedPages ) > 0}">
        <div class="wiki_metaData_section">
        <h3><a name="relatedPages"></a>Related Pages</h3>
          <ww:sort items="${wikiPage.relatedPages}" var="sorted"/>
            <ul class="wiki_infoList">
              <li><a href="<c:out value="${wikiPage.mainPage.viewUrl}"/>">Main</a></li>
              <c:forEach items="${sorted}" var="relatedPage">
                <li><a href="<c:out value="${relatedPage.viewUrl}"/>"><c:out value="${relatedPage.wikiName.relation}"></c:out></a></li>
              </c:forEach>
            </ul>
        </div>
        </c:if>

        <!-- back links -->
        <c:if test="${wikiPage.backLinks.length > 0}">
        <div class="wiki_metaData_section">
        <h3><a name="backLinks"></a>Referenced By</h3>
            <ul class="wiki_infoList">
              <c:forEach var="searchResult" items="${wikiPage.backLinks.iterator}">
                <li><a href="<c:out value="${searchResult.wikiName.wikiPage.viewUrl}"/>"><c:out value="${searchResult.wikiName.title}"/></a></li>
              </c:forEach>
            </ul>
        </div>
        </c:if>

        <!-- dependents -->
        <c:if test="${wikiPage.dependents.length > 0}">
        <div class="wiki_metaData_section">
        <h3><a name="dependents"></a>Dependents</h3>
            <ul class="wiki_infoList">
              <c:forEach var="searchResult" items="${wikiPage.dependents.iterator}">
                <li><a href="<c:out value="${searchResult.wikiName.wikiPage.viewUrl}"/>"><c:out value="${searchResult.wikiName.title}"/></a></li>
              </c:forEach>
            </ul>
        </div>
        </c:if>

  </tiles:put>
  
</tiles:insert>

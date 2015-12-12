<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://nickthecoder.co.uk/webwidgets" prefix="ww" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div class="wiki_searchForm">
  <form action="<c:out value="${search_url}"/>" method="GET">

    <input type="text" name="pageName" size="55" value="<c:out value="${param.pageName}"/>"/>
    <input type="submit" name="submit" value="Go"/>

  </form>
</div>

<c:if test="${ result != null }">

    <div class="wiki_searchResults">

        <h2>Searchable Content</h2>

        <div class="wiki_searchResult">
        <div class="wiki_searchResultHeading">
            <span class="wiki_title">
              <a href="${result.wikiName.viewUrl}"><c:out value="${result.wikiName.title}"/></a>
            </span>
            <c:if test="${result.wikiName.namespace != wikiEngine.defaultNamespace}">
              <span class="wiki_namespace">
                  <c:out value="${result.wikiName.namespace.name}"/>
              </span>
            </c:if>
        </div>
        
        <div class="wiki_searchSummary">
            <c:out value="${result.content}"/>
        </div>
        <div class="wiki_lastUpdated">
            Last Updated : <c:out value="${result.lastUpdated}"/>
        </div>
        </div>
        
        <h2>Meta Data</h2>
        
        <!-- links -->
        <c:if test="${result.wikiDocument.links.hasNext}">
        <div class="wiki_metaData_section">
        <h3><a name="links"/></a>Links</h3>
            <ul class="wiki_infoList">
              <c:forEach items="${result.wikiDocument.links}" var="linkName">
                <li><a href="<c:out value="${linkName.wikiPage.viewUrl}"/>"><c:out value="${linkName.title}"/></a></li>
              </c:forEach>
            </ul>
        </div>
        </c:if>
    
        <!-- attachments -->
        <c:if test="${result.wikiDocument.attachments.hasNext}">
        <div class="wiki_metaData_section">
        <h3><a name="attachments"></a>Attachments</h3>
            <ul class="wiki_infoList">
              <c:forEach items="${result.wikiDocument.attachments}" var="attachName">
                <li><a href="<c:out value="${attachName.wikiPage.viewUrl}"/>"><c:out value="${attachName.title}"/></a></li>
              </c:forEach>
            </ul>
        </div>
        </c:if>

        <!-- includes -->
        <c:if test="${result.wikiDocument.includes.hasNext}">
        <div class="wiki_metaData_section">
        <h3><a name="includes"></a>Includes</h3>
            <ul class="wiki_infoList">
              <c:forEach items="${result.wikiDocument.includes}" var="includeName">
                <li><a href="<c:out value="${includeName.wikiPage.viewUrl}"/>"><c:out value="${includeName.title}"/></a></li>
              </c:forEach>
            </ul>
        </div>
        </c:if>

        <!-- related pages -->
        <c:if test="${fn:length( result.wikiPage.relatedPages ) > 0}">
        <div class="wiki_metaData_section">
        <h3><a name="relatedPages"></a>Related Pages</h3>
          <ww:sort items="${result.wikiPage.relatedPages}" var="sorted"/>
            <ul class="wiki_infoList">
              <li><a href="<c:out value="${wikiPage.mainPage.viewUrl}"/>">Main</a></li>
              <c:forEach items="${sorted}" var="relatedPage">
                <li><a href="<c:out value="${relatedPage.viewUrl}"/>"><c:out value="${relatedPage.wikiName.relation}"></c:out></a></li>
              </c:forEach>
            </ul>
        </div>
        </c:if>

        <!-- back links -->
        <c:if test="${result.wikiPage.backLinks.length > 0}">
        <div class="wiki_metaData_section">
        <h3><a name="backLinks"></a>Referenced By</h3>
            <ul class="wiki_infoList">
              <c:forEach var="searchResult" items="${result.wikiPage.backLinks.iterator}">
                <li><a href="<c:out value="${searchResult.wikiName.wikiPage.viewUrl}"/>"><c:out value="${searchResult.wikiName.title}"/></a></li>
              </c:forEach>
            </ul>
        </div>
        </c:if>

        <!-- dependents -->
        <c:if test="${result.wikiPage.dependents.length > 0}">
        <div class="wiki_metaData_section">
        <h3><a name="dependents"></a>Dependents</h3>
            <ul class="wiki_infoList">
              <c:forEach var="searchResult" items="${result.wikiPage.dependents.iterator}">
                <li><a href="<c:out value="${searchResult.wikiName.wikiPage.viewUrl}"/>"><c:out value="${searchResult.wikiName.title}"/></a></li>
              </c:forEach>
            </ul>
        </div>
        </c:if>


        <h2>Re-Index</h2>
        
        <div class="wiki_searchForm">
          <form action="<c:out value="${search_url}"/>" method="POST">
            <p>
                In rare cases, the Lucene database can get out of sync with the wiki page's content,
                and so re-indexing may be necessary.
            </p>
            <input type="hidden" name="pageName" value="<c:out value="${param.pageName}"/>"/>
            <input type="submit" name="reindex" value="Re-index Now"/>
            
            <p>
                You can also force a re-index by editing the wiki page, even if you click <i>save</i>
                without making any changes. 
                </p>
          </form>
        </div>

  </div>

</c:if>

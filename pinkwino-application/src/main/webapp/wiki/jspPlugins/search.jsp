<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://nickthecoder.co.uk/webwidgets" prefix="ww" %>

<div class="wiki_searchForm">
  <form name="searchPluginForm" action="<c:out value="${search_url}"/>" method="GET">

    <input type="text" name="search" size="55" maxlength="2000" value="<c:out value="${param.search}"/>"/>
    <input type="submit" name="searchButton" value="Search"/>

  </form>
</div>

<c:if test="${! empty searchResults}">

  <div class="wiki_searchResults">
    <ww:pager items="${searchResults.iterator}" itemsPerPage="20" subsetVar="items" pageParameterName="pageNo">

      <h3>Search Results : ${searchResults.length} matches</h3>

      <c:forEach items="${items}" var="result">

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
          <c:forEach items="${result.summary.sections}" var="section">
            <c:if test="${section.matched}"><b></c:if>
            <c:out value="${section.text}"/>
            <c:if test="${section.matched}"></b></c:if>
          </c:forEach>
          </div>
          <div class="wiki_lastUpdated">
            Last Updated : <c:out value="${result.lastUpdated}"/>
          </div>
        </div>

      </c:forEach>

      <div class="wiki_pager">
        <ww:linkInfo href="${search_url}" useContextPath="false">
          <ww:linkParameter name="search" value="${param.search}"/>

          <ww:pagerLinks type="previous"> <ww:link>Previous</ww:link></ww:pagerLinks>
          <ww:pagerLinks type="before"><ww:link>${page}</ww:link></ww:pagerLinks>
          <ww:pagerLinks type="current">${page}</ww:pagerLinks>
          <ww:pagerLinks type="after"><ww:link>${page}</ww:link></ww:pagerLinks>
          <ww:pagerLinks type="next"><ww:link>Next</ww:link></ww:pagerLinks>
        </ww:linkInfo>
      </div>

    </ww:pager>

  </div>

</c:if>

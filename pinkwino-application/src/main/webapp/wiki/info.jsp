<%@ page contentType="text/html" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://nickthecoder.co.uk/webwidgets" prefix="ww" %>
<%@ taglib uri="http://nickthecoder.co.uk/pinkwino" prefix="pw" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<tiles:insert template="${wikiEngine.urlManager.template}" flush="true">

  <tiles:put name="pageType" value="info"/>

  <tiles:put name="wikiPage" value="${wikiPage}"/>

  <tiles:put name="title" type="string">Info : <pw:wikiName wikiName="${wikiPage.wikiName}"/></tiles:put>

  <tiles:put name="content" type="string" >

    <form method="get" action="${wikiPage.diffUrl}">
    
    <c:if test="${! wikiPage.exists}">
      This page does not exist. Do you want to
      <a href="<c:out value="${wikiPage.editUrl}"/>">create it</a>?
    </c:if>

    <c:if test="${wikiPage.exists}">

        <p>
          [ <a href="<c:out value="${wikiPage.rawUrl}"/>">raw</a> ]
          &nbsp;
          [ <a href="<c:out value="${wikiPage.deleteUrl}"/>">Delete Entire page</a> ]
          &nbsp;
          [ <a href="<c:out value="${wikiPage.securityUrl}"/>">Security</a> ]
           &nbsp;
          [ <a href="<c:out value="${wikiPage.renameUrl}"/>">Rename</a> ]
        </p>

        
        <h2><a name="versions"></a>Versions</h2>
        
        <ww:sort var="sortedVersions" items="${wikiPage.versions}" reverse="true"/>
        <ww:pager items="${sortedVersions}" itemsPerPage="20" subsetVar="subsetVersions" pageParameterName="pager_page">
        
        <table class="wiki_table">
          <tr>
          
            <th>Version</th>
            <th>Date</th>
            <th>View</th>
            <th>Raw</th>
            
            <c:if test="${wikiPage.wikiName.media}">
              <th>Media Changed?</th>
            </c:if>
            
            <c:if test="${wikiPage.hasVersions}">
              <th>Revert</th>
              <th colspan="2"><input name="diff" type="submit" value="Differences"/></th>
            </c:if>
            
          </tr>

          <c:forEach items="${subsetVersions}" var="version" varStatus="status">
            <tr>
            
              <td align="center"><c:out value="${version.versionNumber}"/></td>
              <td align="center"><c:out value="${version.date}"/></td>
              <td align="center">
                <a href="<c:out value="${version.viewUrl}"/>">view</a>
              </td>
              <td align="center">
                <a href="<c:out value="${version.rawUrl}"/>">raw</a>
              </td>
              
              <c:if test="${wikiPage.wikiName.media}">
                <td align="center"><c:if test="${version.hasMedia}">yes</c:if></td>
              </c:if>

              <c:if test="${wikiPage.hasVersions}">
                <td>
                  <a href="<c:out value="${version.revertUrl}"/>">revert</a>
                </td>
                <td><input type="radio" name="diffb" value="${version.versionNumber}" <c:if test="${status.count == 2}"> checked="checked"</c:if>/></td>
                <td><input type="radio" name="diffa" value="${version.versionNumber}" <c:if test="${status.count == 1}"> checked="checked"</c:if>/></td>
              </c:if>

            </tr>
          </c:forEach>
        </table>
      
        <div style="text-align: center">
          <ww:linkInfo href="${wikiPage.infoUrl}" useContextPath="false">
            <ww:pagerLinks type="previous"> <ww:link>Previous</ww:link></ww:pagerLinks>
            <ww:pagerLinks type="before"> (<ww:link>${page}</ww:link>) </ww:pagerLinks>
            <ww:pagerLinks type="current"> (${page}) </ww:pagerLinks>
            <ww:pagerLinks type="after"> (<ww:link>${page}</ww:link>) </ww:pagerLinks>
            <ww:pagerLinks type="next"> <ww:link>Next</ww:link></ww:pagerLinks>
          </ww:linkInfo>
        </div>
        
        </ww:pager>
        
      </c:if>
      
    </form>

    <br/>
    
    <!-- attachments -->
    <h4><a name="attachments"></a>Attachments</h4>
    <c:if test="${version.wikiDocument.attachments.hasNext}">
      <div class="wiki_infoListContainer">
        <ul class="wiki_infoList">
          <c:forEach items="${version.wikiDocument.attachments}" var="attachName">
            <li><a href="<c:out value="${attachName.wikiPage.viewUrl}"/>"><c:out value="${attachName.formatted}"/></a></li>
          </c:forEach>
        </ul>
      </div>
    </c:if>

    <!-- includes -->
    <h4><a name="includes"></a>Includes</h4>
    <c:if test="${version.wikiDocument.includes.hasNext}">
      <div class="wiki_infoListContainer">
        <ul class="wiki_infoList">
          <c:forEach items="${version.wikiDocument.includes}" var="includeName">
            <li><a href="<c:out value="${includeName.wikiPage.viewUrl}"/>"><c:out value="${includeName.formatted}"/></a></li>
          </c:forEach>
        </ul>
      </div>
    </c:if>

    <!-- related pages -->
    <h4><a name="relatedPages"></a>Related Pages</h4>
    <c:if test="${fn:length( wikiPage.relatedPages ) > 0}">
      <div class="wiki_infoListContainer">
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
    <h4><a name="backLinks"></a>Back Links</h4>
    <c:if test="${wikiPage.backLinks.length > 0}">
      <div class="wiki_infoListContainer">
        <ul class="wiki_infoList">
          <c:forEach var="searchResult" items="${wikiPage.backLinks.iterator}">
            <li><a href="<c:out value="${searchResult.wikiName.wikiPage.viewUrl}"/>"><c:out value="${searchResult.wikiName.formatted}"/></a></li>
          </c:forEach>
        </ul>
      </div>
    </c:if>

    <!-- dependents -->
    <h4><a name="dependents"></a>Dependents</h4>
    <c:if test="${wikiPage.dependents.length > 0}">
      <div class="wiki_infoListContainer">
        <ul class="wiki_infoList">
          <c:forEach var="searchResult" items="${wikiPage.dependents.iterator}">
            <li><a href="<c:out value="${searchResult.wikiName.wikiPage.viewUrl}"/>"><c:out value="${searchResult.wikiName.formatted}"/></a></li>
          </c:forEach>
        </ul>
      </div>
    </c:if>

    <!-- links -->
    <h4><a name="links"/></a>Links</h4>
    <c:if test="${version.wikiDocument.links.hasNext}">
      <div class="wiki_infoListContainer">
        <ul class="wiki_infoList">
          <c:forEach items="${version.wikiDocument.links}" var="linkName">
            <li><a href="<c:out value="${linkName.wikiPage.viewUrl}"/>"><c:out value="${linkName.formatted}"/></a></li>
          </c:forEach>
        </ul>
      </div>
    </c:if>

    
  </tiles:put>
  
</tiles:insert>


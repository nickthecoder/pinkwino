<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://nickthecoder.co.uk/webwidgets" prefix="ww" %>

<!-- lists all images in a given namespace, show the thumbnail -->

<ww:sort items="${thumbnailPages}" var="sorted"/>

<ww:pager items="${sorted}" itemsPerPage="9" subsetVar="subset" pageParameterName="pager_page">

<ww:portion portionSize="3" items="${subset}" var="rows">
<table class="wiki_imageIndex">

  <c:forEach var="row" items="${rows}">
    <tr>

      <c:forEach var="thumbnailPage" items="${row}">
       <td>
         <a href="${thumbnailPage.mainPage.viewUrl}">
           <img alt="${thumbnailPage.wikiName.title}" src="${thumbnailPage.mediaUrl}"/><br/><c:out value="${thumbnailPage.wikiName.title}"/>
         </a>
       </td>
      </c:forEach>

    </tr>
  </c:forEach>

</table>
</ww:portion>

<div style="text-align: center">
  <ww:linkInfo href="${imageIndex_url}" useContextPath="false">
    <ww:pagerLinks type="previous"> <ww:link>Previous</ww:link></ww:pagerLinks>
    <ww:pagerLinks type="before"> (<ww:link>${page}</ww:link>) </ww:pagerLinks>
    <ww:pagerLinks type="current"> (${page}) </ww:pagerLinks>
    <ww:pagerLinks type="after"> (<ww:link>${page}</ww:link>) </ww:pagerLinks>
    <ww:pagerLinks type="next"> <ww:link>Next</ww:link></ww:pagerLinks>
  </ww:linkInfo>
</div>

</ww:pager>


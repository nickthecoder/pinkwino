<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://nickthecoder.co.uk/webwidgets" prefix="ww" %>
<%@ taglib uri="http://nickthecoder.co.uk/pinkwino" prefix="pw" %>

<c:choose>

  <c:when test="${parameters['float'].value == 'none'}">
    <div style="text-align: ${parameters.align.value}">
      <c:choose>
        <c:when test="${parameters.renderLink.value == 'false'}">
          <img src="${imageWikiPage.mediaUrl}" alt="${imageWikiPage.wikiName.formatted}" />
        </c:when>
        <c:otherwise>
          <a href="${imageWikiPage.mainPage.viewUrl}"><img src="${imageWikiPage.mediaUrl}" alt="${imageWikiPage.wikiName.formatted}" /></a>
        </c:otherwise>
      </c:choose>
      ${jspNode.renderChildren}
    </div>
  </c:when>

  <c:otherwise>

    <div class="wiki_image_float_${parameters['float'].value}">
      <c:choose>
        <c:when test="${parameters.renderLink.value == 'false'}">
          <img src="${imageWikiPage.mediaUrl}" alt="${imageWikiPage.wikiName.formatted}" />
        </c:when>
        <c:otherwise>
          <a href="${imageWikiPage.mainPage.viewUrl}"><img src="${imageWikiPage.mediaUrl}" alt="${imageWikiPage.wikiName.formatted}" /></a>
        </c:otherwise>
      </c:choose>

      <div class="wiki_caption" style="text-align: ${parameters.align.value};">
        ${jspNode.renderChildren}
      </div>
    </div>
  </c:otherwise>

</c:choose>


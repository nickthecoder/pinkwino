<%@ page contentType="text/html" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://nickthecoder.co.uk/webwidgets" prefix="ww" %>
<%@ taglib uri="http://nickthecoder.co.uk/pinkwino" prefix="pw" %>

<tiles:insert template="${wikiEngine.urlManager.template}" flush="true">

  <tiles:put name="pageType" value="view"/>

  <tiles:put name="wikiPage" value="${wikiPage}"/>

  <tiles:put name="namedContents" value="${version.wikiDocument.namedContents}"/>

  <tiles:put name="version" value="${version}"/>

  <tiles:put name="title" type="string">
    <c:out value="${wikiPage.wikiName.title}"/>
    <c:if test="${param.version != null}">
      : Version <c:out value="${param.version}"/>
    </c:if>
  </tiles:put>

  <tiles:put name="content" type="string" >

    <c:if test="${! wikiPage.exists}">
      This page does not exist. Do you want to
      <a href="<c:out value="${wikiPage.editUrl}"/>">create it</a>?
    </c:if>

    <c:choose>
      <c:when test="${wikiPage.wikiName.text}">
        <pre><c:out value="${version.content}"/></pre>
      </c:when>
      <c:otherwise>
        <div class="wiki_content">
          ${version.rendered}
        </div>
      </c:otherwise>
    </c:choose>

    <c:if test="${version.hasMedia}">

      <a href="${version.mediaUrl}"><img id="wiki_image" src="${version.mediaUrl}" alt="${wikiPage.wikiName.formatted}" /></a>

      <div id="wiki_imageFullWidth">
      </div>

      
        
    </c:if>

  <script type="text/javascript">
    ww_onLoadAdd( function() { resizeImage(); } );
    
    function resizeImage() {
      var img = document.getElementById( "wiki_image" );
      var fullWidth = document.getElementById( "wiki_imageFullWidth" );
    
      if ( (img !=null) && (fullWidth != null) ) {
        
        if ( img.width > fullWidth.clientWidth ) {
          img.width = fullWidth.clientWidth;
        }
        
      }
    }
    
  </script>

  </tiles:put>

</tiles:insert>


<%@ page contentType="text/html" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://nickthecoder.co.uk/webwidgets" prefix="ww" %>
<%@ taglib uri="http://nickthecoder.co.uk/pinkwino" prefix="pw" %>

<tiles:insert template="${wikiEngine.urlManager.template}" flush="true">

  <tiles:put name="pageType" type="string" value="view"/>
  <tiles:put name="wikiPage" type="string" value="${wikiPage}"/>

  <tiles:put name="namedContents" type="string" value="${version.wikiDocument.namedContents}"/>

  <tiles:put name="version" type="string" value="${version}"/>

  <tiles:put name="title" type="string">
    <pw:wikiName wikiName="${wikiPage.wikiName}"/>
    
    <c:if test="${param.version != null}">
      : Version <c:out value="${param.version}"/>
    </c:if>
  </tiles:put>

  <tiles:put name="content" type="string" >

    <c:if test="${! wikiPage.exists}">
      This page does not exist. Do you want to
      <a href="<c:out value="${wikiPage.editUrl}"/>">create it</a>?
    </c:if>

    <div class="wiki_content">
      ${version.rendered}
    </div>

    <c:if test="${version.hasMedia}">
      <h2>Attachment</h2>

      <p>
        Mime Type : <c:out value="${wikiPage.wikiName.mimeType}"/>
        [ <a href="${version.mediaUrl}">download</a> ]
      </p>
      <c:if test="${version.image}">
        <img src="${version.mediaUrl}" alt="${wikiPage.wikiName.formatted}" />
      </c:if>
    </c:if>


  </tiles:put>

</tiles:insert>



<%@ page contentType="text/html" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://nickthecoder.co.uk/webwidgets" prefix="ww" %>
<%@ taglib uri="http://nickthecoder.co.uk/pinkwino" prefix="pw" %>

<tiles:insert template="${wikiEngine.urlManager.template}" flush="true">

  <tiles:put name="wikiPage" value="${wikiPage}"/>

  <tiles:put name="pageType" value="preview"/>

  <tiles:put name="title" type="string">Preview : <c:out value="${wikiPage.wikiName.title}"/></tiles:put>

  <tiles:put name="content" type="string" >

    <div class="wiki_warning">
        <p>
          This is only a preview, if you want to save your changes,
          <a href="#pw_editForm">scroll down</a>, and click the <i>Save</i> button.
        </p>
    </div>

    <div class="wiki_content wiki_preview">
      ${version.rendered}
    </div>

    <h1>Edit</h1>
    
    <tiles:insert template="editForm.jsp" flush="false">
      <tiles:put name="wikiPage" value="${wikiPage}"/>
      <tiles:put name="markup" type="string" value="${param.markup}"/>
    </tiles:insert>

  </tiles:put>

</tiles:insert>


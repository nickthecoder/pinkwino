<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://nickthecoder.co.uk/webwidgets" prefix="ww" %>
<%@ taglib uri="http://nickthecoder.co.uk/pinkwino" prefix="pw" %>

EditForm wikiPage ${wikiPage}

<form id="pw_editForm" method="post" enctype="multipart/form-data" action="${wikiPage.editUrl}">

  <c:if test="${ (wikiPage.wikiName.media) && (empty param.section) }">
    <input id="pw_file" type="file" name="media" size="60"/>
  </c:if>

  <textarea id="pw_editTextArea" style="width: 100%" name="markup" cols="180" rows="20"><tiles:insert name="markup" flush="false"/></textarea>

  <input type="hidden" name="page" value="<c:out value="${wikiPage.wikiName.formatted}"/>"/>
  <input type="hidden" name="section" value="<c:out value="${param.section}"/>"/>
  <input type="hidden" name="referrer" value="<ww:referrer/>"/>

  <div class="pw_buttons">
    <input type="submit" name="preview" value="Preview" accesskey="p"/>
    <input type="submit" name="save" value="Save" accesskey="s"/>
    <input type="submit" name="cancel" value="Cancel" accesskey="c"/>
  </div>
  
</form>


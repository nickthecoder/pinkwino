<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://nickthecoder.co.uk/webwidgets" prefix="ww" %>
<%@ taglib uri="http://nickthecoder.co.uk/pinkwino" prefix="pw" %>

<tiles:insert template="layout.jsp">

  <tiles:put name="title" type="string"><tiles:insert attribute="title" ignore="true" flush="false"/></tiles:put>

  <tiles:put name="content" type="string" >

    <div class="tools">
        <span class="tool"><a href="${wikiPage.infoUrl}">Info</a></span>
        <span class="tool"><a href="${wikiPage.editUrl}">Edit</a></span>
    </div>

    <h1><tiles:insert attribute="title" ignore="true" flush="false"/></h1>

    <tiles:insert attribute="content" ignore="true" flush="false"/>
            
  </tiles:put>
</tiles:insert>

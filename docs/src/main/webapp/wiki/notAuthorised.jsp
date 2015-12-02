<%@ page contentType="text/html" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://nickthecoder.co.uk/webwidgets" prefix="ww" %>
<%@ taglib uri="http://nickthecoder.co.uk/pinkwino" prefix="pw" %>

<pw:wikiEngine var="wikiEngine"/>

<tiles:insert template="${wikiEngine.urlManager.template}" flush="true">

  <tiles:put name="pageType" value="error"/>

  <tiles:put name="title" type="string" value="Error" />

  <tiles:put name="content" type="string" >

    <p>
      You are not permitted to perform this action.
    </p>

  </tiles:put>

</tiles:insert>


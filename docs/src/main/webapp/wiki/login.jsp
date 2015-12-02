<%@ page contentType="text/html" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://nickthecoder.co.uk/webwidgets" prefix="ww" %>
<%@ taglib uri="http://nickthecoder.co.uk/pinkwino" prefix="pw" %>

<tiles:insert template="${wikiEngine.urlManager.template}" flush="true">

  <tiles:put name="wikiPage" value="${wikiPage}"/>

  <tiles:put name="pageType" value="login"/>

  <tiles:put name="title" type="string">Login</tiles:put>

  <tiles:put name="content" type="string" >

    <form name="login" method="post" action="${wikiEngine.urlManager.urls.login}">

      <input type="hidden" name="referrer" value="<ww:referrer/>"/>

      <table class="wiki_form">

        <tr>
          <td>User Name</td>
          <td><input type="text" name="userName"/></td>
        </tr>

        <tr>
          <td>Password</td>
          <td><input type="password" name="password"/></td>
        </tr>

        <tr>
          <td colspan="2" class="wiki_formButtons">
            <input type="submit" name="ok" value="Ok"/>
            <input type="submit" name="cancel" value="Cancel"/>
          </td>
        </tr>

      </table>

    </form>

    <c:if test="${! empty message}">
      <p class="wiki_warning">
        <c:out value="${message}"/>
      </p>
    </c:if>

  </tiles:put>

</tiles:insert>


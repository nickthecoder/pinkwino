<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://nickthecoder.co.uk/webwidgets" prefix="ww" %>

<!-- lists all pages within a given namespace -->

<div class="wiki_chooseLetter">
  <ww:groupByInitial var="letters" items="${wikiPages}" expression="wikiName.title">

    <c:forEach var="letter" items="${letters}">
      <c:choose>
        <c:when test="${letter.count == 0}">
          <c:out value="${letter}"/> 
        </c:when>
        <c:otherwise>
          <a class="button" href="#${namespace.name}_letter_${letter}"><c:out value="${letter}"/></a>
        </c:otherwise>
      </c:choose>
    </c:forEach>

  </ww:groupByInitial>
</div>

<ww:groupByInitial var="letters" items="${wikiPages}" expression="wikiName.title">
  <c:forEach var="letter" items="${letters}">

    <c:if test="${letter.count > 0}">

      <h2><a id="${namespace.name}_letter_${letter}" href="#${namespace.name}_letter_${letter}"><c:out value="${letter}"/></a></h2>
      <div class="wiki_editSection wiki_editSection2">
          <a href="#">top</a>
      </div>

      <div class="wiki_index_group">
        <ww:sort items="${letter.items}" var="sorted"/>
        <c:forEach var="wikiPage" items="${sorted}" varStatus="status">
          <c:if test="${status.count > 1}">,&nbsp; </c:if>
          <a href="${wikiPage.viewUrl}"><c:out value="${wikiPage.wikiName.title}"/></a>
        </c:forEach>
      </div>
    </c:if>

  </c:forEach>
</ww:groupByInitial>

<script type="text/javascript">
<!--
/* accunote shortcuts for each letter group */
<ww:groupByInitial var="letter" items="${wikiPages}" expression="wikiName.title">
  <c:forEach var="letter" items="${letters}">
      shortcutListener.add( ["l", "${letter}"], function() { ww_followLink( "${namespace.name}_letter_${letter}" ); } );
  </c:forEach>
</ww:groupByInitial>
-->
</script>


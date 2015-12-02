<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://nickthecoder.co.uk/webwidgets" prefix="ww" %>
<%@ taglib uri="http://nickthecoder.co.uk/pinkwino" prefix="pw" %>

<c:if test="${! empty wikiDocument.subsections}">

  <!-- Table of Contents -->
  <ww:box styleClass="wiki_toc">
    <ww:boxTitle title="Contents" clickable="true"/>
    <ww:boxContent>
      <ol>
        <c:forEach var="section" items="${wikiDocument.subsections}">
          <li>
            <a href="#section${section.linkName}"><c:out value="${section.title}"/></a>
            
            <c:if test="${ (! empty section.subsections) && (parameters['levels'].value != '1') }"><ul>
              <c:forEach var="section2" items="${section.subsections}">
                <li>
                  <a href="#section${section2.linkName}"><c:out value="${section2.title}"/></a>
                  
                  <c:if test="${ (! empty section2.subsections) && (parameters['levels'].value != '2')}"><ul>
                    <c:forEach var="section3" items="${section2.subsections}">
                      <li>
                        <a href="#section${section3.linkName}"><c:out value="${section3.title}"/></a>
                      </li>
                    </c:forEach>
                  </ul></c:if>
                  
                </li>
              </c:forEach>
            </ul></c:if>
            
          </li>
        </c:forEach>
      </ol>
    </ww:boxContent>
  </ww:box>

</c:if>


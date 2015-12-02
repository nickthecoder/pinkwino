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
    <pw:wikiName wikiName="${wikiPage.wikiName}"/> (Version ${versionB.versionNumber} vs ${versionA.versionNumber})
  </tiles:put>

  <tiles:put name="content" type="string" >

    <div id="wiki_diffTools">
      Opacity :<br/>
      <a href="#" onmouseover="wiki_diffOpacity(0)">${versionB.versionNumber}</a>
      <a href="#" onmouseover="wiki_diffOpacity(0.1)">.</a>
      <a href="#" onmouseover="wiki_diffOpacity(0.2)">.</a>
      <a href="#" onmouseover="wiki_diffOpacity(0.3)">.</a>
      <a href="#" onmouseover="wiki_diffOpacity(0.4)">.</a>
      <a href="#" onmouseover="wiki_diffOpacity(0.5)">.</a>
      <a href="#" onmouseover="wiki_diffOpacity(0.6)">.</a>
      <a href="#" onmouseover="wiki_diffOpacity(0.7)">.</a>
      <a href="#" onmouseover="wiki_diffOpacity(0.8)">.</a>
      <a href="#" onmouseover="wiki_diffOpacity(0.9)">.</a>
      <a href="#" onmouseover="wiki_diffOpacity(1)">${versionA.versionNumber}</a>
      
      <br/>
      <a href="#" onclick="wiki_diffTogglePingPong();" />Auto/Manual</a>
      <br/>

      Sections :<br/>        
      <c:forEach var="section" items="${versionA.wikiDocument.subsections}">
        <li>
          <a onclick="wiki_diffAlignSection( '#section${section.linkName}' )" href="#section${section.linkName}"><c:out value="${section.title}"/></a>
          
          <c:if test="${ (! empty section.subsections) && (parameters['levels'].value != '1') }"><ul>
            <c:forEach var="section2" items="${section.subsections}">
              <li>
                <a onclick="wiki_diffAlignSection( '#section${section2.linkName}' )" href="#section${section2.linkName}"><c:out value="${section2.title}"/></a>
                
                <c:if test="${ (! empty section2.subsections) && (parameters['levels'].value != '2')}"><ul>
                  <c:forEach var="section3" items="${section2.subsections}">
                    <li>
                      <a onclick="wiki_diffAlignSection( '#section${section3.linkName}' )" href="#section${section3.linkName}"><c:out value="${section3.title}"/></a>
                    </li>
                  </c:forEach>
                </ul></c:if>
                
              </li>
            </c:forEach>
          </ul></c:if>
          
        </li>
      </c:forEach>
      
    </div>

    <div id="wiki_versions">
      <div id="wiki_versionA">
        ${versionA.rendered}
      </div>
      
      <div id="wiki_versionB">
        ${versionB.rendered}
      </div>
    </div>
          
    <script type="text/javascript">
      function wiki_diffOpacity( n ) {
        document.getElementById( "wiki_versionA" ).style.opacity = n;
        document.getElementById( "wiki_versionB" ).style.opacity = 1 - n;
      }
      function wiki_diffAlignSection( section ) {
        var underscore = section.lastIndexOf( "_" );
        var name = section.substring( underscore + 1 );
        //alert( "Section " + name );
        
        var from = wiki_diffFindSection( document.getElementById( "wiki_versionA" ), name );
        var to = wiki_diffFindSection( document.getElementById( "wiki_versionB" ), name );
        //alert( from.offsetTop + " vs " + to.offsetTop );
        var diff = from.offsetTop - to.offsetTop; 
        if ( diff > 0 ) {
          document.getElementById( "wiki_versionB" ).style.top = diff + "px"; 
          document.getElementById( "wiki_versionA" ).style.top = "0px"; 
        } else {
          document.getElementById( "wiki_versionA" ).style.top = -diff + "px"; 
          document.getElementById( "wiki_versionB" ).style.top = "0px"; 
        }
      }
      function wiki_diffFindSection( element, name ) {
        var as = element.getElementsByTagName( "a" );
        var i;
        for ( i = 0; i < as.length; i ++ ) {
          var aname = as[i].getAttribute( "name" );
          if ( (aname != null) && ( aname.substring( 0, 7) == "section" ) ) {
            var underscore = aname.lastIndexOf( "_" );
            if ( name == aname.substring( underscore + 1 ) ) { 
              //alert( "Found " + aname );
              return as[i].nextSibling;
              // Returns the next sibling, because the invisible 'a' tag may return
              // a dubious value for its offsetTop (and does for the first heading!).
            }
          }
        }
        //alert( "Found nothing" );
        return null;
      }
      wiki_pingPongInterval = null;
      wiki_pingPongDelta = 0.1;
      wiki_pingPongValue = 0;
      function wiki_diffTogglePingPong() {
        if ( wiki_pingPongInterval == null ) {
          wiki_pingPongInterval = window.setInterval( wiki_diffPingPong, 100 );
        } else {
          window.clearInterval( wiki_pingPongInterval );
          wiki_pingPongInterval = null;
        }
      }
      function wiki_diffPingPong() {
        wiki_pingPongValue += wiki_pingPongDelta;
        if ( wiki_pingPongValue > 1 ) {
          wiki_pingPongValue = 1;
          wiki_pingPongDelta = -0.1;
        }
        if ( wiki_pingPongValue < 0 ) {
          wiki_pingPongValue = 0;
          wiki_pingPongDelta = 0.1;
        }
        wiki_diffOpacity( wiki_pingPongValue );
      }
    </script>
    
  </tiles:put>

</tiles:insert>


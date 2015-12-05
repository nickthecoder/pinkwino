<%@ page contentType="text/html" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://nickthecoder.co.uk/webwidgets" prefix="ww" %>
<%@ taglib uri="http://nickthecoder.co.uk/pinkwino" prefix="pw" %>

<tiles:insert template="${wikiEngine.urlManager.template}" flush="true">

  <tiles:put name="pageType" value="diff"/>

  <tiles:put name="wikiPage" value="${wikiPage}"/>

  <tiles:put name="namedContents" value="${version.wikiDocument.namedContents}"/>

  <tiles:put name="version" value="${version}"/>

  <tiles:put name="title" type="string">
      Diff : <c:out value="${wikiPage.wikiName.title}"/>
      (versions
      <span class="wiki_versionB">${versionB.versionNumber}</span>
      &amp;
      <span class="wiki_versionA">${versionA.versionNumber}</span>)
  </tiles:put>

  <tiles:put name="content" type="string" >

    <div id="wiki_diffTools">
      Opacity :
      &nbsp;
      <span class="wiki_opacitySlider">
          <span class="wiki_versionB" onclick="wiki_diffOpacity(0)">${versionB.versionNumber}</span
          ><span onclick="wiki_diffOpacity(0.1)" title="10%"> </span
          ><span onclick="wiki_diffOpacity(0.2)" title="20%"> </span
          ><span onclick="wiki_diffOpacity(0.3)" title="30%"> </span
          ><span onclick="wiki_diffOpacity(0.4)" title="40%"> </span
          ><span onclick="wiki_diffOpacity(0.5)" title="50%"> </span
          ><span onclick="wiki_diffOpacity(0.6)" title="60%"> </span
          ><span onclick="wiki_diffOpacity(0.7)" title="70%"> </span
          ><span onclick="wiki_diffOpacity(0.8)" title="80%"> </span
          ><span onclick="wiki_diffOpacity(0.9)" title="90%"> </span
          ><span class="wiki_versionA" onclick="wiki_diffOpacity(1)">${versionA.versionNumber}</span>
      </span>
      &nbsp;
      <a href="#" onclick="wiki_diffTogglePingPong();" />Auto/Manual</a>
      <br/>

      <ul class="wiki_infoList">     
      <li>
        <a onclick="wiki_diffAlignSection( '#sectionTOP' )" href="#sectionTOP">TOP</a>
      </li>
      <c:forEach var="section" items="${versionA.wikiDocument.subsections}">
        <li>
          <a onclick="wiki_diffAlignSection( '#section${section.linkName}' )" href="#section${section.linkName}"><c:out value="${section.title}"/></a>
        </li>
        <c:forEach var="section2" items="${section.subsections}">
          <li>
            <a onclick="wiki_diffAlignSection( '#section${section2.linkName}' )" href="#section${section2.linkName}"><c:out value="${section2.title}"/></a>
          </li> 
          <c:if test="${ (! empty section2.subsections) && (parameters['levels'].value != '2')}">
            <c:forEach var="section3" items="${section2.subsections}">
              <li>
                <a onclick="wiki_diffAlignSection( '#section${section3.linkName}' )" href="#section${section3.linkName}"><c:out value="${section3.title}"/></a>
              </li>
            </c:forEach>
          </c:if>
                
        </c:forEach>
        
      </c:forEach>
      </ul>
      
    </div>

    <div id="wiki_versions">
      <div id="wiki_versionA">
        <a name="sectionTOP"></a>
      
        ${versionA.rendered}
      </div>
      
      <div id="wiki_versionB">
        <a name="sectionTOP"></a>
        
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


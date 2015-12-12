<!DOCTYPE html>
<html lang="en-GB">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://nickthecoder.co.uk/webwidgets" prefix="ww" %>

<% request.setAttribute( "server", request.getServerName() ); %>
<tiles:useAttribute name="navigation" ignore="true"/>

<head>
  <title>
	<tiles:insert attribute="title"/>
  </title>
  <ww:styleSheet href="/templates/ntc/style.css"/>
  <link rel="icon" href="<ww:contextPath/>/templates/ntc/icon.png"/>
  <link href='http://fonts.googleapis.com/css?family=Arvo' rel='stylesheet' type='text/css' />

  <ww:script src="/templates/ntc/acunote-shortcuts.js"></ww:script>
  <ww:script src="/templates/ntc/shortcuts.js"></ww:script>
  <ww:script src="/templates/ntc/webwidgets-min.js"/>

  <tiles:insert attribute="extraHead" ignore="true"/>  
</head>

<body>
  <div id="whole">
    <div id="header">
      <div id="logo">
      	<c:choose>
          <c:when test="${server == 'nickthecoder.co.uk'}">
	    <h1><a id="home" href="/">Nick The Coder . co . uk</a></h1>
	  </c:when>
	  <c:otherwise>
	    <h1><a id="home" href="/">Giddyserv</a></h1>
	  </c:otherwise>
	</c:choose>
      </div>
      
      <div id="shortcut_status"></div>

      <c:choose>
          <c:when test="${server == 'nickthecoder.co.uk'}">
      <form id="search" method="get" action="/search/search">
          </c:when>
	      <c:otherwise>
      <form id="search" method="get" action="/isearch/search">
          </c:otherwise>
      </c:choose>

        <input id="searchText" type="text" size="15" name="q" placeholder="search"/>
        <input id="searchSubmit" type="submit" value="&#10140;"/>
      </form>

      <ww:local var="isLocal"/>
      <ww:tabs id="tabs">
      <c:choose>
        <c:when test="${server == 'giddyserv'}">
            <ww:tab useContextPath="true" pattern="/iwiki/view/[^/]*"><a id="wikiLink" href="/iwiki/view/Home">Wiki</a></ww:tab>
            <ww:tab useContextPath="true" pattern="/gidea/music.*"><a id="musicLink" href="/gidea/music">Music</a></ww:tab>
            <ww:tab useContextPath="true" pattern="/gidea/image.*"><a id="photosLink" href="/gidea/images/photos">Photos</a></ww:tab>
            <ww:tab useContextPath="true" pattern="/familyalbum/.*"><a id="familyAlbumLink" href="/familyAlbum/">Family Album</a></ww:tab>
            <ww:tab useContextPath="true" pattern="/iwiki/view/software/.*"><a id="softwareLink" href="/iwiki/view/software/Software">Software</a></ww:tab>
            <ww:tab useContextPath="true" pattern="/gidea/games/.*"><a id="gamesLink" href="/gidea/games/">Games</a></ww:tab>
            <ww:tab useContextPath="true" pattern="/iwiki/view/recipe/.*"><a id="recipesLink" href="/iwiki/view/recipe/Recipes">Recipes</a></ww:tab>
            <ww:tab useContextPath="true" pattern="/iwiki/view/garden/.*"><a id="gardenLink" href="/iwiki/view/garden/Garden">Garden</a></ww:tab>
        </c:when>
        <c:otherwise>
            <ww:tab useContextPath="true" pattern="/wiki/view/[^/]*"><a id="wikiLink" href="/wiki/view/Home">Wiki</a></ww:tab>
            <c:if test="${isLocal}">
                <ww:tab useContextPath="true" pattern="/gidea/music.*"><a id="musicLink" href="/gidea/music">Music</a></ww:tab>
            </c:if>
            <ww:tab useContextPath="true" pattern="/gidea/image.*"><a id="photosLink" href="/gidea/images/photos">Photos</a></ww:tab>
            <ww:tab useContextPath="true" pattern="/familyalbum/.*"><a id="familyAlbumLink" href="/familyAlbum/">Family Album</a></ww:tab>
            <ww:tab useContextPath="true" pattern="/wiki/view/software/.*"><a id="softwareLink" href="/wiki/view/software/Software">Software</a></ww:tab>
            <ww:tab useContextPath="true" pattern="/gidea/games/.*"><a id="gamesLink" href="/gidea/games/">Games</a></ww:tab>
            <ww:tab useContextPath="true" pattern="/wiki/view/recipe/.*"><a id="recipesLink" href="/wiki/view/recipe/Recipes">Recipes</a></ww:tab>
            <ww:tab useContextPath="true" pattern="/wiki/view/garden/.*"><a id="gardenLink" href="/wiki/view/garden/Garden">Garden</a></ww:tab>
        </c:otherwise>        
      </c:choose>    
      </ww:tabs>

    </div>

    <div id="belowTabs">
    </div>
    
    <div id="main">
	  <c:if test="${navigation==null}">
      <div id="full">
	  </c:if>
	  <c:if test="${navigation!=null}">
      <div id="columns">
	  </c:if>

        <div id="content">  	  


          <tiles:insert attribute="content" ignore="true"/>
        </div>

        <div id="navigation">
          <tiles:insert attribute="navigation" ignore="true"/>
        </div>
        
      </div>

      <div id="belowColumns">
      </div>

    </div>

    <div id="footer">        
      &copy; <a href="http://nickthecoder.co.uk">nickthecoder.co.uk</a>
    </div>
        
  </div>
  
</body>
</html>

<!DOCTYPE html>
<html lang="en-GB">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://nickthecoder.co.uk/webwidgets" prefix="ww" %>

<tiles:useAttribute name="navigation" ignore="true"/>

<head>
  <title><tiles:insert attribute="title" ignore="true"/></title>
  <ww:styleSheet href="/wiki/templates/default/style.css"/>
  <link rel="icon" href="<ww:contextPath/>/wiki/templates/default/icon.png"/>
  <link href='http://fonts.googleapis.com/css?family=Arvo' rel='stylesheet' type='text/css' />
  <ww:script src="/wiki/templates/default/webwidgets-min.js"/>
  
  <tiles:insert attribute="extraHead" ignore="true"/>  
</head>

<body>
  <div id="whole">
    <div id="header">
      <div id="logo">
	    <h1>Pinkwino</h1>
	  </div>
      
      <ww:tabs id="tabs">
        <ww:tab useContextPath="false" pattern="/view/Home.*"><ww:link href="/view/Home">Home</ww:link></ww:tab>
        <ww:tab useContextPath="false" pattern="/view/Index.*"><ww:link href="/view/Index">Index</ww:link></ww:tab>
        <ww:tab useContextPath="false" pattern="/view/.*lugin.*"><ww:link href="/view/Plugins">Plugins</ww:link></ww:tab>
        <ww:tab useContextPath="false" pattern="/view/Syntax.*"><ww:link href="/view/Syntax">Syntax</ww:link></ww:tab>
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
        Powered by <a href="http://nickthecoder.co.uk/software/view/Pinkwino">Pinkwino</a>
    </div>
        
  </div>
  
</body>
</html>

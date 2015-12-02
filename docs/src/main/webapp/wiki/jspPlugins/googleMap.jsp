<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="googleMap_mapDiv" class="googleMap_map">
</div>

<div class="googleMap_caption">
  [ <a href="#" onclick="return googleMap_reset();">Reset Map</a> ]
</div>

<div id="googleMap_directionsDiv" class="googleMap_directions">
</div>


<script type="text/javascript">
//<![CDATA[

window.onload = googleMap_load;

//]]>
</script>


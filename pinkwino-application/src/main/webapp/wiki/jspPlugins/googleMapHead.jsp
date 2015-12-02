<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script src="http://maps.google.com/maps?file=api&amp;v=2.x&amp;key=<c:out value="${parameters.key.value}"/>"type="text/javascript"></script>

<script type="text/javascript">
//<![CDATA[

googleMap_info = "<c:out value="${parameters.info.value}"/>";


function googleMap_load()
{
  if (GBrowserIsCompatible()) {

    googleMap_map = new GMap2( document.getElementById("googleMap_mapDiv") );
    googleMap_map.addControl( new GSmallMapControl() );
    googleMap_map.addControl( new GMapTypeControl() );
    googleMap_directions = null;

    <c:if test="${! empty parameters.target.value }">
      googleMap_setTarget(
        '<c:out value="${parameters.target.value}"/>',
        '<c:out value="${parameters.longitude.value}"/>',
        '<c:out value="${parameters.latitude.value}"/>',
        <c:out value="${parameters.zoom.value}"/>.0
      );
    </c:if>
    <c:if test="${! empty parameters.start.value }">
      googleMap_setStart(
        '<c:out value="${parameters.start.value}"/>',
        '<c:out value="${parameters.startLongitude.value}"/>',
        '<c:out value="${parameters.startLatitude.value}"/>'
      );
    </c:if>
  }
}

function googleMap_setTarget( target, longitude, latitude, zoom )
{
  // Save the position, so that the map can be reset.
  googleMap_longitude = longitude;
  googleMap_latitude = latitude;
  googleMap_zoom = zoom;

  googleMap_map.setCenter( new GLatLng( googleMap_longitude, googleMap_latitude ), googleMap_zoom );

  var marker = new GMarker( new GLatLng( googleMap_longitude, googleMap_latitude ) );
  googleMap_map.addOverlay( marker );

  if ( target != "" ) {

    GEvent.addListener( marker, "click", function() {
      marker.openInfoWindowHtml( target );
    });
  }

  return false;
}

function googleMap_setStart( start, longitude, latitude )
{
  if ( googleMap_directions == null ) {
    var div = document.getElementById( "googleMap_directionsDiv" );
    googleMap_directions = new GDirections( googleMap_map, div );
  }

  if ( longitude == "" ) {
    // alert( start + " to " + googleMap_longitude + "," + googleMap_latitude );
    googleMap_directions.load( start + " to " + googleMap_longitude + "," + googleMap_latitude );
  } else {
    // alert( longitude + "," + latitude + " to " + googleMap_longitude + "," + googleMap_latitude );
    googleMap_directions.load( longitude + "," + latitude + " to " + googleMap_longitude + "," + googleMap_latitude );
  }
  return false;
}

function googleMap_reset() {
  googleMap_map.setCenter(new GLatLng( googleMap_longitude, googleMap_latitude ), googleMap_zoom);
  if ( googleMap_directions != null ) {
    googleMap_directions.clear();
  }
  return false;
}

//]]>
</script>


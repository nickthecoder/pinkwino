<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="googleMap_mapDiv" class="googleMap_map">
</div>

<div class="googleMap_caption">
  [ <a href="#" onclick="return googleMap_reset();">Reset Map</a> ]
</div>

<div id="googleMap_directionsDiv" class="googleMap_directions">
</div>


<script src="https://maps.googleapis.com/maps/api/js?key=<c:out value="${parameters.key.value}&amp;sensor=false"/>" type="text/javascript"></script>

<script type="text/javascript">
//<![CDATA[

function googleMap_load()
{
    var lngLat = new google.maps.LatLng( <c:out value="${parameters.longitude.value}"/>, <c:out value="${parameters.latitude.value}"/> );
    var zoom = <c:out value="${parameters.zoom.value}"/>;
    var mapTypeId = google.maps.MapTypeId.ROADMAP;

    var mapOptions = {
        center: lngLat,
        zoom: zoom,
        mapTypeId: mapTypeId,
        scrollwheel: false
    };
    
    googleMap_map = new google.maps.Map(document.getElementById("googleMap_mapDiv"), mapOptions);
    googleMap_directions = null;
    
    var startAddress = "<c:out value="${parameters.longitude.value}"/>";
    if (startAddress != "") {
        googleMap_setStartAddress( address );
    }
}

function googleMap_setStartAddress( address )
{
    var request = { address: address, location: this.map.getCenter(), region: "uk" };
      this.geocoder.geocode( request, function ( results, status ) {
    
        if ( status == google.maps.GeocoderStatus.OK ) {
            result = results[0];    
            googleMap_map.setCenter( result.geometry.location );    
            googleMap_map.fitBounds( result.geometry.viewport );  
        } else {
            alert( "Location : " + address + " not found." );
        }
    });    
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
    var lngLat = new google.maps.LatLng( <c:out value="${parameters.longitude.value}"/>, <c:out value="${parameters.latitude.value}"/> );
    var zoom = <c:out value="${parameters.zoom.value}"/>;

      googleMap_map.setCenter(lngLat);
      googleMap_map.setZoom( zoom );
      if ( googleMap_directions != null ) {
        googleMap_directions.clear();
      }
      return false;
}

if (window.addEventListener) {
     window.addEventListener('load', googleMap_load, false);
} else if (element.attachEvent) {
     window.attachEvent('onload', googleMap_load );
}

//]]>
</script>


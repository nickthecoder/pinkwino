<%@ taglib uri="http://nickthecoder.co.uk/webwidgets" prefix="ww" %>
<%@ taglib uri="http://nickthecoder.co.uk/pinkwino" prefix="pw" %>

<div id="toolbar">
    <input type="button" value="Save" onclick="osf_garden.save()">
    <input type="button" value="New Bed" onclick="osf_garden.newBed()">
    <span id="toggleToolbox" class="hidden">
        <button onclick="osf_showToolbox( true )" class="show">Show Toolbox</button>
        <button onclick="osf_showToolbox( false)" class="hide">Hide Toolbox</button>
    </span>
</div>

<div id="garden" class="garden">
</div>

<div id="pickCrop"></div>

<div id="toolbox">
    <div class="title">Drag Crops from here</div>
</div>

<div id="newBed" title="New Bed">
    <form>
        <table class="form">
            <tr>
                <th>Width In Feet</th>
                <td><input id="newBedWidth" type="text" size="2" value="4"></td>
            </tr>
            <tr>
                <th>Length in Feet</th>
                <td><input id="newBedHeight" type="text" size="2" value="4"></td>
            </tr>
        </table>
        <button type="button" onclick="osf_garden.onNewBed()">Ok</button>
    </form>
</div>
                
<div id="editSquare" title="Edit Square">
    <form>
    <table class="form">
        <tr>
            <th>Crop</td>
            <td>
                <img onclick="Crop.picker.pick( $('#patch_image')[0], osf_onCropPicked );" id="patch_image" src="icons/empty.png">(<span id="patch_cropName">Empty</span>)
            </td>
        </tr>
        <tr>
            <th>Comment</td>
            <td><input id="patch_comment" type="text" length="20" onchange="osf_onCropChanged()"></td>
        </tr>
        <tr>
            <th>Sow Date</th>
            <td><input id="patch_sowDate" type="text" value="3-10-2012" size="10" onchange="osf_onCropChanged()"></td>
        </tr>
    </table>
    </form>
</div>

<ww:script src="/extra/oneSquareFoot/js/jquery-1.8.2.js"></ww:script>
<ww:script src="/extra/oneSquareFoot/js/jquery-ui.js"></ww:script>
<ww:script src="/extra/oneSquareFoot/js/jquery.json-2.3.js"></ww:script>
<ww:script src="/extra/oneSquareFoot/oneSquareFoot.js"></ww:script>
<!--Ho -->

<script type="text/javascript">
<!--

<pw:wikiPage wikiPageVar="dummy" title="PAGENAME">
Garden.loadTemplate = "${dummy.rawUrl}";
Garden.saveTemplate = "${dummy.saveUrl}";
</pw:wikiPage>

function osf_onload() {
    $( "#patch_sowDate" ).datepicker({ dateFormat: 'dd-mm-yy' });
    Crop.createPicker( "pickCrop" );
    Crop.createToolbox( "toolbox" );
    osf_garden.load( "${parameters.page.value}" );
}


if (window.addEventListener) {
    window.addEventListener('load',osf_onload,false); //W3C
} else {
    window.attachEvent('onload',osf_onload); //IE
}

$('script').each(function(i,el){
    var path = el.src.match(/^(.+)\/oneSquareFoot.js$/);
    if (path) {
        $('body').append('<link rel="stylesheet" ' + 'href="' + path[1] + '/css/style.css" ' + 'type="text/css" />' );
        $('body').append('<link rel="stylesheet" ' + 'href="' + path[1] + '/css/jquery-ui.css" ' + 'type="text/css" />' );
        Crop.load( path[1] + "/icons" );
    }
})


-->
</script>


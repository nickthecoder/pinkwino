
function Crop( code, density, name, imgUrl )
{
    this.code = code;
    this.density = density; // In plants per square foot
    
    if ( name == null ) {
        this.name = code.substring( 0,1 ).toUpperCase() + code.substring( 1 );
    } else {
        this.name = name;
    }
    if ( imgUrl == null ) {
        this.imgUrl = Crop.iconsDirectory + "/" + code + ".png"
    } else {
        this.imgUrl = imgUrl;
    }
    
    Crop.crops[ this.code ] = this;
}
Crop.iconsDirectory = "icons";

Crop.crops = new Object();

Crop.find = function( code ) {
    return Crop.crops[ code ];
}

Crop.createPicker = function( id ) {
    var column = 0;

    var html = '<div class="picker">';
    for ( var cropName in Crop.crops ) {
        var crop = Crop.crops[ cropName ];
        html += '<div class="item" onclick="Crop.picker.handler(\'' + crop.code + '\');" title="' + crop.name + '">';
        html += '<img src="' + crop.imgUrl + '" alt="' + crop.name + '">';
        html += '</div>';
        
    }
    html += "</div>";

    Crop.pickerSelector = '#' + id;
    Crop.picker = document.getElementById( id );
    Crop.picker.innerHTML = html;

    Crop.picker.pick = function( parent, handler ) {

        Crop.picker.handler = function( cropCode ) {
            $( Crop.pickerSelector ).dialog( "close" );
            handler( Crop.find( cropCode ) );
        };
        $( Crop.pickerSelector ).dialog( {
            title: "Pick a Crop",
            position: { my: "left top", at: "left bottom", of: $("#patch_image")[0] },
            modal: true,
            within: window
        } );

    };
    
};

Crop.createToolbox = function( id ) {
    var container = document.getElementById( id );
    
    for ( var cropName in Crop.crops ) {    
        
        var crop = Crop.crops[ cropName ];
        var html = '';
        var div = document.createElement( 'div' );
        div.setAttribute( 'class', 'item tool' );
        html += '<img src="' + crop.imgUrl + '" alt="' + crop.name + '">'; 
        html += '<span class="toolNameContainer"><span class="toolName">';
        html += osf_htmlEscape( crop.name );;
        html += '</span></span>';
        div.innerHTML = html;
        div.setAttribute( "title", crop.name );

        container.appendChild( div );
        div.crop = crop;
    }
    
    $( '#' + id + ' .item' ).draggable( {
        revert: "invalid", revertDuration: 200,
        stop: function( event, ui ) {
            $(ui.helper).animate(ui.originalPosition, 0 );
        }
    } );
    
    $( '#' + id ).draggable( {
        position: { collision: "none" }
    } );
    
};

Crop.prototype = {
};

Crop.load = function( iconBaseDir ) {
    
    Crop.iconsDirectory = iconBaseDir;

    new Crop( "empty", 0 );
    new Crop( "nothing", 0  );
    new Crop( "other", 0  );
    new Crop( "snail trap", 0, "Snail Trap" );
    new Crop( "aubergine", 1 );
    new Crop( "asparagus", 1 );
    new Crop( "basil", 16  );
    new Crop( "beetroot", 9 );
    new Crop( "broccoli", 1 );
    new Crop( "carrot", 16 );
    new Crop( "cabbage", 1 );
    new Crop( "cauliflower", 1 );
    new Crop( "celeriac", 1 ); // ?
    new Crop( "celery", 4 );
    new Crop( "chives", 25 );
    new Crop( "coriander", 16 );
    new Crop( "courgette", 1 );
    new Crop( "cucumber", 1 );
    new Crop( "chilli", 1 );
    new Crop( "dill", 1 );
    new Crop( "fennel", 1 );
    new Crop( "garlic", 16 );
    new Crop( "leek", 4 );
    new Crop( "lettuce", 4 );
    new Crop( "marjoram", 1 );
    new Crop( "mustard", 16 );
    new Crop( "onion", 16 );
    new Crop( "parsley", 4 );
    new Crop( "parsnip", 4 );
    new Crop( "pea", 9 );
    new Crop( "pepper", 1 );
    new Crop( "pumpkin", 1 );
    new Crop( "raspberry", 1);
    new Crop( "radish", 16 );
    new Crop( "rocket", 9 ); // ?
    new Crop( "red orach", 16, "Red Orach" );
    new Crop( "rhubarb", 1 );
    new Crop( "leaf mix", 16, "Leaf Mix" );
    new Crop( "sage", 1 );
    new Crop( "spring onion", 16, "Spring Onion" );
    new Crop( "spinach", 9 );
    new Crop( "shallot", 16 );
    new Crop( "sweetcorn", 1 ); // ?
    new Crop( "squash", 1 );
    new Crop( "swiss chard", 4, "Swiss Chard" );
    new Crop( "strawberry", 1 );
    new Crop( "thyme", 1 );
    new Crop( "tomato", 1 );
    new Crop( "welsh onion", 16, "Welsh Onion" );
};

function Garden( id )
{
    this.id = id;
    this.beds = new Array();
}

Garden.rawTemplate = "";

Garden.prototype = {
    addBed: function( bed ) {
        this.beds.push( bed );
    },
    
    removeBed: function( bedId ) {
        for( var i = 0; i < this.beds.length; i ++ ) {
            if ( this.beds[ i ].id == bedId ) { 
                this.beds.splice( i, 1 );
                this.create();
                return;
            }
        }
    },
    
    create: function() {
        var i;
        this.container = document.getElementById( this.id );
        this.container.innerHTML = "";
        for ( i = 0; i < this.beds.length; i ++ ) {
            this.beds[i].create( this.container );
        }
    },

    load: function( pageName )
    {
        this.pageName = pageName;

        var url= Garden.loadTemplate.replace( "PAGENAME", this.pageName );
        $.ajax( {
            url: url,
            type: "GET",
            error: function(jqXHR, textStatus, errorThrown) {
                alert( "Load failed : " + textStatus + " - " + errorThrown + ": " + jqXHR );
            },
            success: function(data, textStatus, jqXHR) {

                var parsed = $.evalJSON( data );
                // osf_garden = new Garden( parsed.id );
                
                var i,x,y;
                for ( i = 0; i < parsed.beds.length; i ++ ) {
                    var parsedBed = parsed.beds[i];
                    var bed = new Bed( parsedBed.width, parsedBed.height, parsedBed.x - parsedBed.x % 10, parsedBed.y - parsedBed.y % 10 );
                    for ( x = 0; x < parsedBed.patches.length; x ++ ) {
                        for ( y = 0; y < parsedBed.patches[x].length; y ++ ) {

                            var parsedPatch = parsedBed.patches[x][y];

                            var sowDate = parsedPatch.sowDate ? new Date( parsedPatch.sowDate ) : null;
                            var patch = new Patch( parsedPatch.x, parsedPatch.y, parsedPatch.cropCode );
                            patch.sowDate = sowDate;
                            patch.comment = parsedPatch.comment;
                            bed.addPatch( patch );
                        }
                    }
                    osf_garden.addBed( bed );   
                }
                
                osf_garden.create();
            }
        }).done(function() { 

        });
    },

    save: function() {
        var json = $.toJSON( this );
               
        var url = Garden.saveTemplate.replace( "PAGENAME", this.pageName );
        
        $.ajax( {
            url: url,
            type: "POST",
            data: { "data": json },
            error: function(jqXHR, textStatus, errorThrown) {
                alert( "Save failed : " + textStatus + " - " + errorThrown );
            },
        }).done(function() { 

        });
    },


    newBed: function()
    {
        $('#newBed').dialog();
    },
    
    onNewBed: function() {
        $('#newBed').dialog("close");
        var bed = new Bed( $('#newBedWidth').val(), $('#newBedHeight').val() );
        this.addBed( bed );
        this.create();
    }

};

function Bed( width, height, x, y )
{
    this.id = "bed" + Bed.nextId ++ ;
    this.patches = new Array();
    this.width = 0;
    this.height = 0;
    this.x = x;
    this.y = y;
    
    this.expand( width, height );
    
    var x, y;
    for ( x = 0; x < this.width; x ++ ) {
        for ( y = 0; y < this.height; y ++ ) {
            this.addPatch( new Patch( x, y, 'empty' ) );
        }
    }
}
Bed.nextId = 0;

Bed.prototype = {
    expand: function( width, height ) {
        var x, y;
        
        if ( width >= this.width ) {
            for ( x = this.width; x < width; x ++ ) {
                this.width = width;
                this.patches.push( new Array( this.height ) );
                for ( y = 0; y < this.height; y ++ ) {
                    this.addPatch( new Patch(x,y, 'empty' ) );
                }
            }
        }
        
        if ( height > this.height ) {
            var x;
            for ( x = 0; x < this.width; x ++ ) {
                this.height = height;
                for ( i = this.height; i < height; i ++ ) {
                    this.patches[x].push( null );
                    this.addPatch( new Patch(x, i, 'empty') );
                }
            }
        }
    },
    
    addPatch: function( patch ) {
        this.expand( patch.x + 1, patch.y + 1 );
        this.patches[patch.x][patch.y] = patch;
        patch.bed = this;
    },
    
    getPatch: function( x, y ) {
        return this.patches[x][y];
    },

    create: function( gardenContainer ) {
        var container = document.createElement( "div" );
        var html = '';
        
        html += '<div class="bedHead"><input type="button" value="Delete" onclick="osf_deleteBed( \'' + this.id + '\')"></div>';
        html += '<table class="osf_bed">';
    
        var x, y;
        for ( y = 0; y < this.height; y ++ ) {
            html += '<tr id="' + this.id + '_row' + y + '">';
            for ( x = 0; x < this.width; x ++ ) {
                html += '<td class="osf_patch" id="' + this.patches[x][y].id + 'td" ';
                html += 'onclick="osf_editPatch(\'' + this.id + '\',' + x + ',' + y + ')">';
                // if ( (x == 0) && (y == 0) ) alert( html );
            }
        }
        html += "</table>";
        
        container.innerHTML = html;
        container.bed = this;
        
        gardenContainer.appendChild( container );
        container.setAttribute( "id", this.id );
        container.setAttribute( "class", "bed" );
                
        $( '#' + this.id ).position( {
            my: 'left+' + this.x + ' top+' +  this.y,
            at: 'left top',
            of: '#garden',
            collision: "none",
            within: '#garden' } );
            
        var bed = this;
        $( '#' + this.id ).draggable( {
            grid : [ 10, 10 ],
            stop : function( event, ui ) {
                bed.x += ui.position.left - ui.originalPosition.left;
                bed.y += ui.position.top - ui.originalPosition.top;
            }
        } );
        
        for ( y = 0; y < this.height; y ++ ) {
            for ( x = 0; x < this.width; x ++ ) {
                this.patches[x][y].updateDisplay();
            }
        }
    }

};

function Patch( x, y, cropCode, sowDate, comment )
{
    this.comment = comment ? comment : "";
    this.x = x;
    this.y = y;
    this.crop = Crop.find( cropCode );
    this.sowDate = sowDate;
    this.id = "patch" + Patch.nextId ++;
}
Patch.nextId = 0;

Patch.current = null;

Patch.prototype = {
    toJSON: function() {
        var obj = {
            comment: this.comment,
            x: this.x,
            y: this.y,
            cropCode: this.crop.code,
            sowDate: this.sowDate,
        };
        return obj;
    },
    
    edit: function() {
        Patch.current = this;
        $( "#patch_image" ).attr( 'src', this.crop.imgUrl );
        $( "#patch_sowDate" ).datepicker( "setDate", this.sowDate );
        var link = '<a href="http://nickthecoder.co.uk/garden/view/' + this.crop.name + '" target="crop">' + this.crop.name + '</a>';
        $( "#patch_cropName" ).html( link );
        $( "#patch_comment" )[0].value = this.comment;
        
        $( ".patchDiv" ).removeClass( 'selectedPatch' );
        $( "#" + this.id ).addClass( 'selectedPatch' );

        $( "#editSquare" ).dialog( {
            position: { my: "left top", at: "left bottom", of: $( "#" + this.id )[0] },
            close: function(event, ui) {
                $( ".osf_patch" ).removeClass( 'selectedPatch' );
                Patch.current = null;
            },
            minWidth: 400
        } );
    },
    
    setCrop: function( crop ) {
        this.crop = crop;
        this.updateDisplay();
    },
    
    updateDisplay: function() {
        var date = osf_shortDate( this.sowDate );
        var html = '<div class="patchDiv" id="' + this.id + '">';
        html += '<div class="osf_overlay"><div class="osf_overlayText">';
        html += date + '</div></div>';
        html += '<img class="osf_icon" src="' + this.crop.imgUrl + '" alt="">'
        html += '</div>';
        this.getTD().innerHTML = html;
        this.getTD().title =
            this.crop.name +
            (this.crop.density ? " (" + this.crop.density + ")" : "" ) +
            (this.comment ? " : " + this.comment : "" );
        
        $( '#' + this.id ).draggable( { revert: "invalid", revertDuration: 200, zIndex: 1000 } );
        $( '#' + this.id ).droppable( {
            tolerance: "pointer",
            accept: ".patchDiv, #toolbox .item",
            
            drop: function( event, ui ) {
            
                var draggable = ui.draggable[0];
                var destPatch = event.target.patch;

                if ( $(draggable).hasClass( 'tool' ) ) {
                    
                    destPatch.crop = draggable.crop;
                    destPatch.updateDisplay();
                    
                } else {


                    var srcPatch = draggable.patch;
                    
                    if ( ! event.ctrlKey ) {
                        var tmpCrop = destPatch.crop;
                        var tmpSowDate = destPatch.sowDate;
                        var tmpComment = destPatch.comment;
                    }
                    
                    destPatch.crop = srcPatch.crop;
                    destPatch.sowDate = srcPatch.sowDate;
                    destPatch.comment = srcPatch.comment;
                    
                    if ( ! event.ctrlKey ) {
                        srcPatch.crop = tmpCrop
                        srcPatch.comment = tmpComment;
                        srcPatch.sowDate = tmpSowDate;
                    }
                    srcPatch.updateDisplay();
                    destPatch.updateDisplay();
                }
            }
        } );
        
        $( '#' + this.id )[0].patch = this;
    },
    
    
    getTD: function() {
        return document.getElementById( this.id + "td" );
    }
};


function osf_shortDate( date )
{
    if ( date == null ) {
        return "";
    } else {
        return $.datepicker.formatDate("dd-M", date );
    }
}

function osf_editPatch( bedId, x, y )
{
    var container = document.getElementById( bedId );
    container.bed.getPatch(x,y).edit();
}

function osf_deleteBed( bedId )
{
    osf_garden.removeBed( bedId );
}

// Escape all of the following :
// &, <, >, ", ', `,  , !, @, $, %, (, ), =, +, {, }, [, ]
function osf_htmlEscape( text )
{
    return text.replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/`/g, "&#96;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#39;")
        .replace(/!/g, "&#33;")
        .replace(/@/g, "&#64;")
        .replace(/\$/g, "&#36;")
        .replace(/%/g, "&#37;")
        .replace(/\(/g, "&#40;")
        .replace(/\)/g, "&#41;")
        .replace(/=/g, "&#61;")
        .replace(/ /g, "&nbsp;")
        .replace(/\+/g, "&#43;")
        .replace(/{/g, "&#123;")
        .replace(/}/g, "&#125;")
        .replace(/\[/g, "&#91;")
        .replace(/]/g, "&#93;");      
}
//alert( osf_htmlEscape( "&_<_>_\"_'_`_!_@_$_%_(_)_=_ _+_{_}_[_]" ) );
//&amp;_&lt;_&gt;_&quot;_&#39;_&#96;_&#33;_&#64;_&#36;_&#37;_&#40;_&#41;_&#61;_&nbsp;_&#43;_&#123;_&#125;_&#91;_&#93;


function osf_onCropPicked( crop )
{
    Patch.current.setCrop( crop );
    $('#patch_image')[0].src = crop.imgUrl;
    var link = '<a href="http://nickthecoder.co.uk/garden/view/' + this.crop.name + '" target="crop">' + this.crop.name + '</a>';
    $('#patch_cropName').html( link );
}
function osf_onCropChanged()
{
    Patch.current.comment = $( "#patch_comment" )[0].value;
    Patch.current.sowDate = $( "#patch_sowDate" ).datepicker( "getDate" );
    Patch.current.updateDisplay();
}

function osf_showToolbox( state )
{
    if ( state ) {
        $( '#toolbox' ).show( { effect : "blind", duration : 300 } );
        $( '#toggleToolbox' ).addClass( 'shown' );
        $( '#toggleToolbox' ).removeClass( 'hidden' );
    } else {
        $( '#toolbox' ).hide( { effect : "blind", duration : 300 } );
        $( '#toggleToolbox' ).removeClass( 'shown' );
        $( '#toggleToolbox' ).addClass( 'hidden' );
    }
}

var osf_garden = new Garden( "garden" );


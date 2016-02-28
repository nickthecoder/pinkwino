var plantChart = {

    // Map of label-> array(one per "merged" row) of array(one per half month) of values( "y", "n" or "" )
    groups : new Object(),

    load : function( loadUrl, saveUrl )
    {
        plantChart.saveUrl = saveUrl;

        $.ajax( {
            url: loadUrl,
            type: "GET",
            error: function(jqXHR, textStatus, errorThrown) {
                alert( "Load failed : " + textStatus + " - " + errorThrown + ": " + jqXHR );
            },
            success: function(data, textStatus, jqXHR) {
                plantChart.groups = data;
                plantChart.updateTables();
            }
        });
    },

    updateTables : function() {
        var tables = document.getElementsByClassName("plantChart");
        var i,j,k;
        for ( i = 0; i < tables.length; i ++ ) {

            var label = null;
            var mergedIndex = 0;
            var mergeCount = 0;
            
            var table = tables[i];
            var rows = table.rows;
            for (j = 0; j < rows.length; j ++ ) {
                var row = rows[j];
                var cells = row.cells;
                var halfMonthIndex = 0;
                if (mergeCount > 0) {
                    mergeCount --;
                    mergedIndex ++;
                }
                
                for ( k = 0; k < cells.length; k ++ ) {
                    var cell = cells[k];
                    if ( (k == 0) && (mergeCount == 0) && (cell.nodeName == "TH") ) {
                        // The TH contains the label, (e.g. the plant name)
                        label = cell.textContent;
                        mergeCount = cell.rowSpan - 1;
                        mergedIndex = 0;
                    } else {
                        if (cell.nodeName == "TD") {
                            // It is a cell which can have a tick, cross or blank.
                            var value = plantChart.getValue( label, mergedIndex, halfMonthIndex );
                            cell.innerHTML = value;
                            plantChart.addOnClick( cell );
                            halfMonthIndex += cell.colSpan;
                        }
                    }
                }
            }
            
            var button = document.createElement("BUTTON");
            button.onclick = plantChart.save;
            button.innerHTML = "Save";
            table.parentNode.insertBefore(button, table.nextSibling);
        }
    },
    
    addOnClick : function(cell) {
        cell.onclick = function() {
            var oldValue = cell.innerHTML;
            if (oldValue == "") {
                cell.innerHTML = "Y";
            } else if (oldValue == "Y") {
                cell.innerHTML = "N";
            } else {
                cell.innerHTML = "";
            }
        }
    },
    
    updateGroups : function() {
        plantChart.groups = new Object();
        
        var tables = document.getElementsByClassName("plantChart");
        var i,j,k;
        for ( i = 0; i < tables.length; i ++ ) {

            var label = null;
            var mergedIndex = 0;
            var mergeCount = 0;
            
            var table = tables[i];
            var rows = table.rows;

            for (j = 0; j < rows.length; j ++ ) {
                var row = rows[j];
                var cells = row.cells;
                var halfMonthIndex = 0;
                if (mergeCount > 0) {
                    mergeCount --;
                    mergedIndex ++;
                }
                
                for ( k = 0; k < cells.length; k ++ ) {
                    var cell = cells[k];
                    if ( (k == 0) && (mergeCount == 0) && (cell.nodeName == "TH") ) {
                        // The TH contains the label, (e.g. the plant name)
                        label = cell.textContent;
                        mergeCount = cell.rowSpan - 1;
                        mergedIndex = 0;
                    } else {
                        if (cell.nodeName == "TD") {
                            // It is a cell which can have a tick, cross or blank.
                            var value = cell.innerHTML;
                            plantChart.setValue( label, mergedIndex, halfMonthIndex, value );
                            halfMonthIndex += cell.colSpan;
                        }
                    }
                }
            }
        }
    },

    getValue : function( label, mergedIndex, halfMonthIndex ) {
        group = plantChart.groups[ label ];
        if ( group ) {
            if (group.length >= mergedIndex) {
                if (group.length <= mergedIndex) {
                    return "";
                }
                return group[mergedIndex][halfMonthIndex];
            }
        }
        return "";
    },
    
    setValue : function( label, mergedIndex, halfMonthIndex, value ) {
        group = plantChart.groups[ label ];
        if ( group === undefined ) {
            group = [];
            plantChart.groups[label] = group;
        }
        while (group.length <= mergedIndex) {
            group.push( ["","","","","","","","","","","","","","","","","","","","","","","",""] );
        }
        group[mergedIndex][halfMonthIndex] = value;
    },

    save : function() {
        plantChart.updateGroups();                
        var json = $.toJSON( plantChart.groups );        

        $.ajax( {
            url: plantChart.saveUrl,
            type: "POST",
            data: { "data": json },
            error: function(jqXHR, textStatus, errorThrown) {
                alert( "Save failed : " + textStatus + " - " + errorThrown + ": " + jqXHR );
            }
        }).done(function() { 
        });

    }
};


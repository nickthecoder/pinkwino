shortcuts = new ShortcutListener();

shortcuts.add( ["g", "h"], function() { followLink( "home" ); } );
shortcuts.add( ["g", "w"], function() { followLink( "wikiLink" ); } );
shortcuts.add( ["g", "m"], function() { followLink( "musicLink" ); } );
shortcuts.add( ["g", "p"], function() { followLink( "photosLink" ); } );
shortcuts.add( ["g", "f"], function() { followLink( "familyAlbumLink" ); } );
shortcuts.add( ["g", "r"], function() { followLink( "recipesLink" ); } );
shortcuts.add( ["g", "s"], function() { followLink( "softwareLink" ); } );
shortcuts.add( ["g", "g"], function() { followLink( "gardenLink" ); } );

shortcuts.add( ["n", "t", "c"], function() { followLink( "ntcLink" ); } );

shortcuts.onEscape = function() { document.getElementById( "home" ).focus(); };

window.addEventListener( "load", function() { shortcuts.init(); } );


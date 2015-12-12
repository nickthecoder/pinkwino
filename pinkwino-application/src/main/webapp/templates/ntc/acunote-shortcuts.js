/*
 *  Acunote Shortcuts.
 *  Javascript keyboard shortcuts mini-framework.
 *
 *  Copyright (c) 2007-2008 Pluron, Inc.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining
 *  a copy of this software and associated documentation files (the
 *  "Software"), to deal in the Software without restriction, including
 *  without limitation the rights to use, copy, modify, merge, publish,
 *  distribute, sublicense, and/or sell copies of the Software, and to
 *  permit persons to whom the Software is furnished to do so, subject to
 *  the following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 *  LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 *  OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 *  WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 *  Modified by Nick Robinson
 *  * can add shortcuts one at a time, rather than in one big hash
 *  * removed the global window.SHORTCUTS and replaced with this.root
 *  * check if the shortcut_status div already exists, if so don't create it (allows apps freedom of where to put it).
 *  * Made it a class rather.
 */
 
ShortcutListener = function()
{
    this.listen= true;
    this.root=null;
    this.onEscape=null;
    this.shortcut=null;
    this.combination='';
    this.lastKeypress=0;
    this.clearTimeout=2000;

    // Keys we don't listen 
    this.erkeys={
        KEY_BACKSPACE: 8,
        KEY_TAB:       9,
        KEY_ENTER:    13,
        KEY_SHIFT:    16,
        KEY_CTRL:     17,
        KEY_ALT:      18,
        KEY_ESC:      27,
        KEY_SPACE:    32,
        KEY_LEFT:     37,
        KEY_UP:       38,
        KEY_RIGHT:    39,
        KEY_DOWN:     40,
        KEY_DELETE:   46,
        KEY_HOME:     36,
        KEY_END:      35,
        KEY_PAGEUP:   33,
        KEY_PAGEDOWN: 34
    };
};

ShortcutListener.prototype =
{

    init: function() {
        if (!this.root) return false;
        if ( document.getElementById('shortcut_status') == null ) {
          this.createStatusArea();
        }
        this.setObserver();
    },

    add: function( combo /* array of keys */, func ) {
        if (! this.root) {
            this.root = {}
        }
        var shortcut = this.root;
        for (var i = 0; i < combo.length; i ++) {
            var key = combo[i];
            if ( ! shortcut[ key ] ) {
                if (i == combo.length -1) {
                    shortcut[ key ] = func;
                } else {
                    shortcut[ key ] = {};
                }
            } else {
              if (i == combo.length -1) {
                  alert( "shortcut '" + combo + "' clashes with one or more longer shortcuts." );
                  return;
              } else {
                  if (typeof(shortcut[key]) == "function") {
                      alert( "shortcut '" + combo + "' clashes with an existing shortcut length " + (i + 1) );
                      return;
                  }
              }
              
            }
            shortcut = shortcut[ key ];
        }
    },
    
    addEscape: function() {
        var prev = this.onEscape;
        shortcuts.onEscape = function()
        {
            onClosePreview();
            if ( prev ) {
                prev();
            }
        };
    },

    isInputTarget: function(e) {
        var target = e.target || e.srcElement;
        if (target && target.nodeName) {
            var targetNodeName = target.nodeName.toLowerCase();
            if (targetNodeName == "textarea" || targetNodeName == "select" ||
                (targetNodeName == "input" && target.type &&
                    (target.type.toLowerCase() == "text" ||
                         target.type.toLowerCase() == "password"))
                             )  {
                return true;
            }
        }
        return false;
    },

    stopEvent: function(event) {
        if (event.preventDefault) {
            event.preventDefault();
            event.stopPropagation();
        } else {
            event.returnValue = false;
            event.cancelBubble = true;
        }
    },

    // shortcut notification/status area
    createStatusArea: function() {
        var area = document.createElement('div');
        area.setAttribute('id', 'shortcut_status');
        area.style.display = 'none';
        document.body.appendChild(area);
    },

    showStatus: function() {
        document.getElementById('shortcut_status').style.display = 'block';
    },

    hideStatus: function() {
        document.getElementById('shortcut_status').style.display = 'none';
    },

    showCombination: function() {
        var bar = document.getElementById('shortcut_status');
        bar.innerHTML = this.combination;
        this.showStatus();
    },

    // This method creates event observer for the whole document
    // This is the common way of setting event observer that works 
    // in all modern brwosers with "keypress" fix for
    // Konqueror/Safari/KHTML borrowed from Prototype.js
    setObserver: function() {
        var name = 'keypress';
        if (navigator.appVersion.match(/Konqueror|Safari|KHTML/) || document.detachEvent) {
          name = 'keydown';
        }
        var me = this;

        if (document.addEventListener) {
            document.addEventListener(name, function(e) {me.keyCollector(e)}, false);
        } else if (document.attachEvent) {
            document.attachEvent('on'+name, function(e) {me.keyCollector(e)});
        }
    },

    // Key press collector. Collects all keypresses into combination 
    // and checks it we have action for it
    keyCollector: function(e) {
        // do not listen if no shortcuts defined
        if (!this.root) return false;
        // do not listen if listener was explicitly turned off
        if (!this.listen) return false;
        // leave modifiers for browser
        if (e.altKey || e.ctrlKey || e.metaKey) return false;
        var keyCode = e.keyCode;

        // On escape, clear the combination, and let the application do its processing if needed.
        if ( e.keyCode == 27) {
          this.clearCombination();
          if ( this.onEscape ) { 
            this.onEscape();
          }
        }
        
        // do not listen for Ctrl, Alt, Tab, Space, Esc and others
        for (var key in this.keys) {
            if (e.keyCode == this.keys[key]) return false;
        }
        // do not listen for functional keys
        if (navigator.userAgent.match(/Gecko/)) {
            if (e.keyCode >= 112 && e.keyCode <=123) return false;
        }
        // do not listen in input/select/textarea fields
        if (this.isInputTarget(e)) return false;
        // get letter pressed for different browsers
        var code = e.which ? e.which : e.keyCode
        var letter = String.fromCharCode(code).toLowerCase();
        if (e.shiftKey) letter = letter.toUpperCase();
        if (this.process(letter)) this.stopEvent(e);
    },

    // process keys
    process: function(letter) {
        if (!this.root) return false;
        if (!this.listen) return false;
        // if no combination then start from the begining
        if (!this.shortcut) { this.shortcut = this.root; }
        // if unknown letter then say goodbye
        if (!this.shortcut[letter]) return false
        if (typeof(this.shortcut[letter]) == "function") {
            this.shortcut[letter]();
            this.clearCombination();
        } else {
            this.shortcut = this.shortcut[letter];
            // append combination
            this.combination = this.combination + letter;
            if (this.combination.length > 0) {
                this.showCombination();
                // save last keypress timestamp (for autoclear)
                var d = new Date;
                this.lastKeypress = d.getTime();
                // autoclear combination in 2 seconds
                var me = this;
                setTimeout( function() { me.clearCombinationOnTimeout(); }, this.clearTimeout );
            };
        }
        return true;
    },

    // clear combination
    clearCombination: function() {
        this.shortcut = null;
        this.combination = '';
        this.hideStatus();
    },

    clearCombinationOnTimeout: function() {
        var d = new Date;
        // check if last keypress was earlier than (now - clearTimeout)
        // 100ms here is used just to be sure that this will work in superfast browsers :)
        if ((d.getTime() - this.lastKeypress) >= (this.clearTimeout - 100)) {
            this.clearCombination();
        }
    }
}


function followLink( linkId ) {
  var link = document.getElementById( linkId );
  if ( link != null ) {
    link.click();
    //document.location = link;
  }
}

function clickButton( buttonId ) {
  var button = document.getElementById( buttonName );
  if ( button != null ) {
    button.click();
  }
}




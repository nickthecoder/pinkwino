Pinkwino is a Wiki Engine.
It is designed as a simple content management system for small web sites.

I use it as a stand-alone webapp to power much of my own web site (http://nickthecoder.co.uk).
I also use it embedded within other webapps (such as my family album).

It is customisable through a groovy configuration script (think of it as interpreted java).
It has a plugin system, so you can create your own special wiki components.

Changing the look and feel is easy, as it uses a template system (from struts).
The entire look and feel can be changed by writing your own layout template, and accompanying css style sheet.

If you are more adventurous, you could extend Pinkwino.
For example, currently all wiki pages are stored in the file system, but if you wanted to save them in a database,
it would be reasonably simple to write another implementation of the Storage interface.

It is also fairly easy to alter or add to the wiki syntax (without writing new java classes).

Useful Gradle Targets
---------------------

* gradle jettyRun (Runs the webapp)
* gradle build    (Builds the war file)
* gradle install  (Adds the jar to mavern local cache)
* gradle pinkwiki-application:eclipse
* gradle pinkwiki-core:eclipse


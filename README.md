## Sample CRUD application ##

This is sample Play frameworkapplication, which is represented as:

- <a href="http://en.wikipedia.org/wiki/Representational_state_transfer">RESTful server</a> on backend

- <a href="http://en.wikipedia.org/wiki/Single-page_application">Single-page application</a> on frontend

Basically it is pretty straight-forward port of [sample-crud-mvc](https://github.com/nmartynenko/sample-crud-mvc/) project

### Applied technologies ###
The major difference from the original is that all back-end is written with [Play Framework](http://www.playframework.com/)

Beside Play, here is a stack of Java-based technologies:

- [Spring](http://www.springsource.org/) &mdash; IoC/DI/AOP container, as well as MVC-framework;

- [Spring Data JPA](http://www.springsource.org/spring-data/jpa) &mdash; JPA persistence;

- [Hibernate](http://www.hibernate.org/) &mdash; JPA-vendor;

- [HyperSQL](http://hsqldb.org/) &mdash; embedded RDMBS;

And lots of things, which are provided by Play itself.

Here is a stack of Javascript-based technologies:

- [jQuery](http://jquery.com/) &mdash; main cross-browser framework;

- [jQuery UI](http://jqueryui.com/) &mdash; UI framework;

- [Datatables](http://datatables.net/) &mdash; jQuery-based tables plugin;

- [jQuery Validation plugin](http://bassistance.de/jquery-plugins/jquery-plugin-validation/) &mdash; clien-side from validation;

- [HandlebarsJS](http://handlebarsjs.com/) &mdash; [Mustache](http://mustache.github.com/)-like template engine with pre-compilation;

- [GlobalizeJS](https://github.com/jquery/globalize) &mdash; client-side l10n and i18n;

- [JSON-js](https://github.com/douglascrockford/JSON-js/) &mdash; fallback for those browsers, which don't support native ```JSON.parse``` and ```JSON.stringify``` functions.

### License ###
The app is open sourced under <a href="http://www.opensource.org/licenses/mit-license.php">MIT</a> license.
If this license doesn't suit you mail me at n.martynenko (at) aimprosoft.com.

### Download ###

* <a href="https://github.com/nmartynenko/sample-crud-play/zipball/master">sample-crud-play.zip</a>

* <a href="https://github.com/nmartynenko/sample-crud-play/tarball/master">sample-crud-play.tar.gz</a>

### Getting started ###
You need to [download](#download) latest version of sources, unpack it and launch either ```start.sh``` on *nix systems or ```start.bat``` on windows platform.
After that you need to open in browser [http://localhost:8080/](http://localhost:8080/) and enter following credentials:

- Admin credentials:
	- login: admin@example.com
	- pass:  admin

- User credentials:
	- login: user@example.com
	- pass:  user

The differences between them are in "write" abilities of Admin role.

__Note__: by default, DB is In-Memory only, therefore all changes will disappear after server's stopped.

### REST testing ###
In ```/etc``` folder of project there is [curl](http://curl.haxx.se/)-based sample of collaborating with REST-server in non-browser environment (for *nix and win platforms).

### Dependencies ###
For launching application you must to have installed:

- <a href="http://www.oracle.com/technetwork/java/index.html">Java</a> with exposed ```JAVA_HOME``` env variable

- <a href="http://www.scala-sbt.org/">Simple Build Toolkit</a> with exposed ```sbt``` executable

- Internet connection (at least all necessary artifacts must be present)

### Known Issues ###
This application is for training purposes of technologies listed above.
The only issues may happen if there is wrong environment to launch the application.

###TODO###

* add validation

* security should work

* probably improve Play philosophy
## Sample CRUD application ##

This is sample Play framework application, which is represented as:

- <a href="http://en.wikipedia.org/wiki/Representational_state_transfer">RESTful server</a> on backend

- <a href="http://en.wikipedia.org/wiki/Single-page_application">Single-page application</a> on frontend

Basically it is pretty straight-forward port of [sample-crud-mvc](https://github.com/nmartynenko/sample-crud-mvc/) project

### Applied technologies ###
The major difference from the original is that all back-end is written with [Play Framework](http://www.playframework.com/)

In order to "feel" the Play, there are 2 alternative implementations:

- with Play stack only &mdash; [scala-style](scala-style);

- with Spring stack &mdash; [spring-style](spring-style);

####Play stack####
It doesn't have any IoC/DI containers, as well as any "JavaEE-specific" frameworks.
Beside play there is a stack of technologies:

- [Slick](http://slick.typesafe.com/) &mdash; Scala ORM, supported by Typesafe;

- [HyperSQL](http://hsqldb.org/) &mdash; embedded RDMBS;

- [Deadbolt 2 Scala Plugin](https://github.com/schaloner/deadbolt-2) &mdash; advanced security plugin for Play;

- [jBCrypt](http://www.mindrot.org/projects/jBCrypt/) &mdash; strong hashing function for security purposes;

####Spring stack####
Here is a stack of Java-based technologies:

- [Spring](http://www.springsource.org/) &mdash; IoC/DI/AOP container;

- [Spring Security](http://www.springsource.org/spring-security/) &mdash; ACL/security (it also uses JBCrypt hash function internally);

- [Spring Data JPA](http://www.springsource.org/spring-data/jpa) &mdash; JPA persistence;

- [Hibernate](http://www.hibernate.org/) &mdash; JPA-vendor;

- [HyperSQL](http://hsqldb.org/) &mdash; embedded RDMBS;

- [OVal](http://oval.sourceforge.net/) &mdash; object validation (for me it's more preferable than [JSR-303](http://jcp.org/en/jsr/detail?id=303));

- [Jackson](http://jackson.codehaus.org/) &mdash; Java JSON processor along with [Jackson Scala module](https://github.com/FasterXML/jackson-module-scala);

- [Spring Scala](https://github.com/spring-projects/spring-scala) &mdash; Spring support of Scala's classes and Scala's friendly wrappers;

And lots of things, which are provided by Play itself.

In both cases there is the same stack of Javascript-based technologies:

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

### Dependencies ###
For launching application you must to have installed:

- <a href="http://www.oracle.com/technetwork/java/index.html">Java</a> with exposed ```JAVA_HOME``` env variable

- <a href="http://www.scala-sbt.org/">Simple Build Toolkit</a> with exposed ```sbt``` executable

- Internet connection (at least all necessary artifacts must be present)

### Known Issues ###
This application is for training purposes of technologies listed above.
The only issues may happen if there is wrong environment to launch the application.
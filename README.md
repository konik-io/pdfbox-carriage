pdfbox-carriage
===============

Addon for the Konik library allows attaching and extracting XML content to PDFs with the help of PDFBox.


Quick Start
-----------
Place the ``pdfbox-carriage.jar`` next to Konik in your classpath. Konik will discover any existing PDF-Carriages and use it.
make sure there is only one PDF-Carriage in your classpath.

```xml
   <dependencies>
       <dependency>
           <groupId>io.konik</groupId>
           <artifactId>konik</artifactId>
           <version>x.y.z</version>
       </dependency>
   </dependencies>

   <dependencies>
       <dependency>
           <groupId>io.konik</groupId>
           <artifactId>pdfbox-carriage</artifactId>
           <version>x.y.z</version>
       </dependency>
   </dependencies>
```

Full documentation
-------------
See [konik.io/docs](http://konik.io/docs) for information on how to use Konik or PDFBox-Carriage.

Links
-------------
Continuous Integration Server [ci.konik.io](http://ci.konik.io)

Continuous Inspection of code quality [sonar.konik.io](http://sonar.konik.io)

Maven Archetypes on Maven Central [search.maven.org](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22io.konik%22)
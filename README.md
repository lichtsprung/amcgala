## amCGAla

Informationen und Anleitungen zu diesem Projekt sind im Wiki und der Projektwebsite zu finden.

http://www.amcgala.org

https://github.com/amcgala/amcgala/wiki

## Getting started

Um das Framework zu verwenden benötigt man entweder die Distribution-jar, in der die externen Abhängigkeiten mit eingebunden
sind, oder man verwendet die aktuellste Version, die über Maven zur Verfügung gestellt wird.
Hierfür muss in die pom.xml des Projekts folgende Abhängigkeit hinzugefügt werden:

```xml
<dependency>
    <groupId>org.amcgala</groupId>
    <artifactId>amcgala</artifactId>
    <version>2.0-SNAPSHOT</version>
</dependency>
```

Das Package org.amcgala.example bietet einige Beispiele, wie das Framework benutzt werden kann. Für den schnellen
Einstieg reicht es, die Klasse Amcgala zu erweitern und auf die dort zu Verfügung gestellte Framework Instanz zurückzugreifen.

amcgala ist Opensource und wird unter der Educational Community License, Version 2.0 (http://opensource.org/licenses/ECL-2.0) zur Verfügung gestellt.

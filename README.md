# TP1SOA

Small Java project that validates and queries a cinema XML resource.

Prerequisites
- Java 17 (JDK)
- Maven

Build
Open PowerShell at the project root (where `pom.xml` is) and run:

```powershell
mvn -B test
```

This will compile the code and run the unit tests.

Run
To run the `Main` program (it validates `cinema.xml` against `cinema.xsd` and prints XPath query results) use:

```powershell
mvn -B compile exec:java
```

Notes
- Resources (`src/main/resources/cinema.xml` and `cinema.xsd`) are loaded from the classpath.
- If you want to run the compiled JAR instead, you can add assembly/jar configuration and run the jar.

Troubleshooting
- If Maven complains about Java version, ensure `JAVA_HOME` points to a Java 17 JDK.
- To run from an IDE, make sure resources are on the classpath (IDEs normally copy `src/main/resources` to the classpath automatically).

## Example output

When you run `mvn -B compile exec:java` you should see output similar to:

```text
=== Validation XML/XSD ===
Le document XML est valide : true

=== Interrogation XPath ===
Tous les titres de films :
- Inception
- La La Land
- Interstellar

Films réalisés par Christopher Nolan :
- Inception
- Interstellar

Films de plus de 140 minutes :
- Inception
- Interstellar
```

## Developer notes

Where files live
- XML/XSD resources: `src/main/resources/cinema.xml` and `src/main/resources/cinema.xsd`.

Adding a new film
1. Open `src/main/resources/cinema.xml`.
2. Add a new `<film>` element inside the root `<cinema>` element. Example:

```xml
	<film id="4">
		<titre>The New Movie</titre>
		<realisateur>Some Director</realisateur>
		<annee>2025</annee>
		<genre>Drama</genre>
		<duree>110</duree>
		<acteurs>
			<acteur>Actor One</acteur>
			<acteur>Actor Two</acteur>
		</acteurs>
	</film>
```

3. Save the file. You can validate and run the program locally with:

```powershell
mvn -B test
mvn -B compile exec:java
```

Validation tips
- If adding a field, make sure the `cinema.xsd` allows it. The current schema requires: `titre`, `realisateur`, `annee` (integer), `genre`, `duree` (integer), `acteurs` with one or more `acteur`, and a required `id` attribute on each `film` element.
- If validation fails, `Main` prints the validation exception message to stdout. Use `mvn -B test` to run unit tests which include XML validation.

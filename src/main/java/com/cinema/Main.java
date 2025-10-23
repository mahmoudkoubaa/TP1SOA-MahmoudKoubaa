package com.cinema;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            // Chemins des fichiers
            String xmlFile = "/cinema.xml"; // classpath resource (leading slash)
            String xsdFile = "/cinema.xsd"; // classpath resource

            // Étape 1 : Validation XML/XSD
            System.out.println("=== Validation XML/XSD ===");
            boolean isValid = validateXML(xmlFile, xsdFile);
            System.out.println("Le document XML est valide : " + isValid);

            // Étape 2 : Interrogation XPath
            System.out.println("\n=== Interrogation XPath ===");
            Document doc = parseXML(xmlFile);
            XPath xpath = XPathFactory.newInstance().newXPath();

            // Exemple 1 : Tous les titres de films
            System.out.println("Tous les titres de films :");
            XPathExpression expr = xpath.compile("//film/titre");
            NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++) {
                System.out.println("- " + nodes.item(i).getTextContent());
            }

            // Exemple 2 : Films de Christopher Nolan
            System.out.println("\nFilms réalisés par Christopher Nolan :");
            expr = xpath.compile("//film[realisateur='Christopher Nolan']/titre");
            nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++) {
                System.out.println("- " + nodes.item(i).getTextContent());
            }

            // Exemple 3 : Films de plus de 140 minutes
            System.out.println("\nFilms de plus de 140 minutes :");
            expr = xpath.compile("//film[duree > 140]/titre");
            nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++) {
                System.out.println("- " + nodes.item(i).getTextContent());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean validateXML(String xmlPath, String xsdPath) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            // load XSD and XML from classpath
            byte[] xsdBytes = readResourceToBytes(xsdPath);
            byte[] xmlBytes = readResourceToBytes(xmlPath);
            Source schemaFile = new StreamSource(new ByteArrayInputStream(xsdBytes));
            Schema schema = factory.newSchema(schemaFile);
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new ByteArrayInputStream(xmlBytes)));
            return true;
        } catch (Exception e) {
            System.out.println("Erreur de validation : " + e.getMessage());
            return false;
        }
    }

    public static Document parseXML(String xmlPath) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        byte[] xmlBytes = readResourceToBytes(xmlPath);
        try (InputStream is = new ByteArrayInputStream(xmlBytes)) {
            return builder.parse(is);
        }
    }

    private static byte[] readResourceToBytes(String resourcePath) throws IOException {
        try (InputStream is = Main.class.getResourceAsStream(resourcePath)) {
            if (is == null) throw new IOException("Resource not found: " + resourcePath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            int r;
            while ((r = is.read(buf)) != -1) baos.write(buf, 0, r);
            return baos.toByteArray();
        }
    }
}
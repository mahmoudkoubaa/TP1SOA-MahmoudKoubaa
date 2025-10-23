package com.cinema;

import org.junit.jupiter.api.Test;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    @Test
    public void testValidateXml() {
        boolean valid = Main.validateXML("/cinema.xml", "/cinema.xsd");
        assertTrue(valid, "cinema.xml should be valid against cinema.xsd");
    }

    @Test
    public void testXPathQueries() throws Exception {
        Document doc = Main.parseXML("/cinema.xml");
        XPath xpath = XPathFactory.newInstance().newXPath();

        XPathExpression expr = xpath.compile("//film/titre");
        NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        assertEquals(3, nodes.getLength(), "There should be 3 film titles");

        expr = xpath.compile("//film[realisateur='Christopher Nolan']/titre");
        nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        assertEquals(2, nodes.getLength(), "Christopher Nolan should have 2 films in sample");

        expr = xpath.compile("//film[duree > 140]/titre");
        nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        assertEquals(2, nodes.getLength(), "There should be 2 films longer than 140 minutes");
    }
}

/*
   Copyright 2005, 2017 Jochen Linnemann

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
/*
 * Created on 27.05.2005
 */
package de.netshore.tcg.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import de.netshore.tcg.Character;

/**
 * @author jlin
 */
public class CharacterTransformer {
    public static final URL DEFAULT_EXPORT = CharacterTransformer.class.getResource("transform-exportDefault.xslt");
    public static final URL DEFAULT_VIEWER = CharacterTransformer.class.getResource("transform-viewerDefault.xslt");

    public static void transform(Character character, OutputStream out, URL xslt) {
        transform(character, out, getTransformerTemplates(xslt));
    }

    public static void transform(Character character, OutputStream out, Templates templates) {
        transform(character, new StreamResult(out), templates);
    }

    public static void transform(Character character, Result result, Templates templates) {
        try {
            Transformer transformer = templates.newTransformer();
            transformer.transform(new DOMSource(CharacterSerializer.createDOM(character)), result);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerFactoryConfigurationError e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public static String createResult(Character character, Templates templates) {
        StringWriter writer = new StringWriter();
        transform(character, new StreamResult(writer), templates);
        return writer.toString();
    }

    private static TreeMap transformerTemplatesMap = new TreeMap();

    public static Templates getTransformerTemplates(URL xslt) {
        String urlString = xslt.toExternalForm();

        if (!transformerTemplatesMap.containsKey(urlString)) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            try {
                DocumentBuilder docBuilder = factory.newDocumentBuilder();
                Document doc = docBuilder.parse(xslt.toExternalForm());

                Templates templates = TransformerFactory.newInstance().newTemplates(new DOMSource(doc));
                transformerTemplatesMap.put(urlString, templates);
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TransformerConfigurationException e) {
                e.printStackTrace();
            } catch (TransformerFactoryConfigurationError e) {
                e.printStackTrace();
            }
        }

        return (Templates) transformerTemplatesMap.get(urlString);
    }

    public static void initialize() {
        getTransformerTemplates(DEFAULT_EXPORT);
        getTransformerTemplates(DEFAULT_VIEWER);
    }

    private CharacterTransformer() {
        // only to keep'em from instantiating
    }
}

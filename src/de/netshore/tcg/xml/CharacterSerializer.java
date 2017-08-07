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
 * Created on 25.05.2005
 */
package de.netshore.tcg.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.netshore.tcg.Character;

/**
 * @author jlin
 */
public class CharacterSerializer {
    public static Character read(File file) {
        try {
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = docBuilder.parse(new FileInputStream(file), CharacterSerializer.class.getResource("").toExternalForm());

            Element root = doc.getDocumentElement();
            if ("character".equals(root.getTagName())) {
                if ("0.9".equals(root.getAttribute("version"))) {
                    Character character = new Character();

                    character.setDateOfPreparation(extractTextNodeValue(doc, "dateOfPreparation"));
                    character.setName(extractTextNodeValue(doc, "name"));
                    character.setCharacteristic(Character.UPP.STR, Integer.parseInt(extractAttributeNodeValue(doc, "str", "val")));
                    character.setCharacteristic(Character.UPP.DEX, Integer.parseInt(extractAttributeNodeValue(doc, "dex", "val")));
                    character.setCharacteristic(Character.UPP.END, Integer.parseInt(extractAttributeNodeValue(doc, "end", "val")));
                    character.setCharacteristic(Character.UPP.INT, Integer.parseInt(extractAttributeNodeValue(doc, "int", "val")));
                    character.setCharacteristic(Character.UPP.EDU, Integer.parseInt(extractAttributeNodeValue(doc, "edu", "val")));
                    character.setCharacteristic(Character.UPP.SOC, Integer.parseInt(extractAttributeNodeValue(doc, "soc", "val")));
                    character.setNobleTitle(extractTextNodeValue(doc, "nobleTitle"));
                    character.setMilitaryRank(extractTextNodeValue(doc, "militaryRank"));
                    character.setBirthdate(extractTextNodeValue(doc, "birthdate"));
                    character.setAgeModifiers(extractTextNodeValue(doc, "ageModifiers"));
                    character.setBirthworld(extractTextNodeValue(doc, "birthworld"));
                    character.setService(extractTextNodeValue(doc, "service"));
                    character.setBranch(extractTextNodeValue(doc, "branch"));
                    character.setDischargeworld(extractTextNodeValue(doc, "dischargeworld"));
                    character.setTermsServed(Integer.parseInt(extractTextNodeValue(doc, "termsServed")));
                    character.setRankPrefix(extractAttributeNodeValue(doc, "finalRank", "prefix"));
                    character.setRankNum(Integer.parseInt(extractAttributeNodeValue(doc, "finalRank", "num")));
                    character.setFinalRank(extractTextNodeValue(doc, "finalRank"));
                    character.setSpecialAssignments(extractTextNodeValue(doc, "specialAssignments"));
                    character.setAwardsAndDecorations(extractTextNodeValue(doc, "awardsAndDecorations"));
                    character.setEquipmentQualifiedOn(extractTextNodeValue(doc, "equipmentQualifiedOn"));
                    NodeList skillList = doc.getElementsByTagName("skill");
                    for (int s = 0; s < skillList.getLength(); s++) {
                        Node skill = skillList.item(s);
                        String name = skill.getAttributes().getNamedItem("name").getNodeValue();
                        int level = Integer.parseInt(skill.getAttributes().getNamedItem("level").getNodeValue());
                        for (int l = 0; l < level; l++) {
                            character.addSkillLevel(name);
                        }
                    }
                    character.setPreferredWeapon(extractTextNodeValue(doc, "preferredWeapon"));
                    character.setCash(Integer.parseInt(extractAttributeNodeValue(doc, "cashStatus", "val")));
                    NodeList possList = doc.getElementsByTagName("item");
                    for (int i = 0; i < possList.getLength(); i++) {
                        Node item = possList.item(i);
                        String name = item.getAttributes().getNamedItem("name").getNodeValue();
                        int count = Integer.parseInt(item.getAttributes().getNamedItem("count").getNodeValue());
                        for (int c = 0; c < count; c++) {
                            character.addPossession(name);
                        }
                    }

                    return character;
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (FactoryConfigurationError e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String extractTextNodeValue(Document doc, String tagName) {
        NodeList tagList = doc.getElementsByTagName(tagName);
        if (tagList.item(0).getChildNodes().getLength() > 0) {
            return tagList.item(0).getFirstChild().getNodeValue();
        } else {
            return "";
        }
    }

    private static String extractAttributeNodeValue(Document doc, String tagName, String attrName) {
        NodeList tagList = doc.getElementsByTagName(tagName);
        return tagList.item(0).getAttributes().getNamedItem(attrName).getNodeValue();
    }

    public static Document createDOM(Character character) {
        Document doc = null;
        try {
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc = docBuilder.newDocument();

            DOMImplementation impl = doc.getImplementation();
            DocumentType docType = impl.createDocumentType("character", null, "character.dtd");
            doc.appendChild(docType);

            Element root = doc.createElement("character");
            doc.appendChild(root);
            appendTextNodeValue(root, "dateOfPreparation", character.getDateOfPreparation());
            appendTextNodeValue(root, "name", character.getName());
            Element upp = doc.createElement("upp");
            root.appendChild(upp);
            appendNode(upp, "str", new String[]{"val"},
                    new String[]{Integer.toString(character.getCharacteristic(Character.UPP.STR))}, null
            );
            appendNode(upp, "dex", new String[]{"val"},
                    new String[]{Integer.toString(character.getCharacteristic(Character.UPP.DEX))}, null
            );
            appendNode(upp, "end", new String[]{"val"},
                    new String[]{Integer.toString(character.getCharacteristic(Character.UPP.END))}, null
            );
            appendNode(upp, "int", new String[]{"val"},
                    new String[]{Integer.toString(character.getCharacteristic(Character.UPP.INT))}, null
            );
            appendNode(upp, "edu", new String[]{"val"},
                    new String[]{Integer.toString(character.getCharacteristic(Character.UPP.EDU))}, null
            );
            appendNode(upp, "soc", new String[]{"val"},
                    new String[]{Integer.toString(character.getCharacteristic(Character.UPP.SOC))}, null
            );
            appendTextNodeValue(root, "nobleTitle", character.getNobleTitle());
            appendTextNodeValue(root, "militaryRank", character.getMilitaryRank());
            appendTextNodeValue(root, "birthdate", character.getBirthdate());
            appendTextNodeValue(root, "ageModifiers", character.getAgeModifiers());
            appendTextNodeValue(root, "birthworld", character.getBirthworld());
            appendTextNodeValue(root, "service", character.getService());
            appendTextNodeValue(root, "branch", character.getBranch());
            appendTextNodeValue(root, "dischargeworld", character.getDischargeworld());
            appendTextNodeValue(root, "termsServed", Integer.toString(character.getTermsServed()));
            appendNode(
                    root, "finalRank",
                    new String[]{"prefix", "num"},
                    new String[]{character.getRankPrefix(), Integer.toString(character.getRankNum())},
                    character.getFinalRank()
            );
            appendTextNodeValue(root, "specialAssignments", character.getSpecialAssignments());
            appendTextNodeValue(root, "awardsAndDecorations", character.getAwardsAndDecorations());
            appendTextNodeValue(root, "equipmentQualifiedOn", character.getEquipmentQualifiedOn());
            Element skills = doc.createElement("skills");
            root.appendChild(skills);
            Character.SkillIterator skillIter = character.skillIterator();
            while (skillIter.hasNext()) {
                skillIter.next();
                appendNode(
                        skills, "skill",
                        new String[]{"name", "level"},
                        new String[]{skillIter.getName(), Integer.toString(skillIter.getLevel())},
                        null
                );
            }
            appendTextNodeValue(root, "preferredWeapon", character.getPreferredWeapon());
            appendNode(
                    root, "cashStatus",
                    new String[]{"val"},
                    new String[]{Integer.toString(character.getCash())},
                    null
            );
            Element personalPossessions = doc.createElement("personalPossessions");
            root.appendChild(personalPossessions);
            Character.PossessionIterator possIter = character.possessionIterator();
            while (possIter.hasNext()) {
                possIter.next();
                appendNode(
                        personalPossessions, "item",
                        new String[]{"name", "count"},
                        new String[]{possIter.getNameOfItem(), Integer.toString(possIter.getNumberOfItems())},
                        null
                );
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (FactoryConfigurationError e) {
            e.printStackTrace();
        }
        return doc;
    }

    public static void write(Character character, File file) {
        Document doc = createDOM(character);
        String systemValue = (new File(doc.getDoctype().getSystemId())).getName();
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, systemValue);
            transformer.transform(new DOMSource(doc), new StreamResult(file));
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerFactoryConfigurationError e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    private static void appendTextNodeValue(Element parent, String tagName, String text) {
        appendNode(parent, tagName, new String[0], new String[0], text);
    }

    private static void appendNode(Element parent, String tagName, String[] attrNames, String[] attrValues, String text) {
        Document doc = parent.getOwnerDocument();
        Element child = doc.createElement(tagName);
        parent.appendChild(child);
        for (int i = 0; i < attrNames.length; i++) {
            child.setAttribute(attrNames[i], attrValues[i]);
        }
        if (text != null) {
            child.appendChild(doc.createTextNode(text));
        }
    }

    private CharacterSerializer() {
        // only to keep'em from instantiating
    }
}

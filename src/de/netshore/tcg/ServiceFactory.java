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
 * Created on 07.04.2005
 */
package de.netshore.tcg;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.netshore.tcg.Service.SuccessNumber;
import de.netshore.tcg.Service.SuccessNumber.DieModifier;

/**
 * @author jlin
 *         <p>
 *         ServiceFactory - creates Service instances from XML files
 */
public class ServiceFactory {
    public static Service[] getServices(String resourceName) {
        Service[] services = null;
        InputStream is = ServiceFactory.class.getResourceAsStream(resourceName);
        String baseUri = ServiceFactory.class.getResource("").toExternalForm();
        TCGApp.debug(baseUri);

        try {
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            services = getServices(docBuilder.parse(is, baseUri));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return services;
    }

    private static Service[] getServices(Document servicesDocument) {
        Service[] services = null;

        DocumentType docType = servicesDocument.getDoctype();
        String dtName = docType.getName();
        if ("services".equals(dtName) || // document may contain several services
                "service".equals(dtName)) {  // document contains only one service
            NodeList serviceList = servicesDocument.getElementsByTagName("service");
            int serviceCount = serviceList.getLength();
            services = new Service[serviceCount];
            for (int i = 0; i < serviceCount; i++) {
                Node serviceNode = serviceList.item(i);

                String serviceName = getNamedAttributeValue(serviceNode, "name");
                int serviceDraft = Integer.parseInt(getNamedAttributeValue(serviceNode, "draft"));

                services[i] = new Service(serviceName, serviceDraft);

                NodeList childList = serviceNode.getChildNodes();
                int childCount = childList.getLength();
                for (int j = 0; j < childCount; j++) {
                    Node childNode = childList.item(j);
                    String tagName = childNode.getNodeName();
                    if ("enlistment".equals(tagName)) {
                        services[i].setEnlistment(getSuccessNumber(childNode));
                    } else if ("survival".equals(tagName)) {
                        services[i].setSurvival(getSuccessNumber(childNode));
                    } else if ("commission".equals(tagName)) {
                        services[i].setCommission(getSuccessNumber(childNode));
                    } else if ("promotion".equals(tagName)) {
                        services[i].setPromotion(getSuccessNumber(childNode));
                    } else if ("reenlistment".equals(tagName)) {
                        services[i].setReenlistment(getSuccessNumber(childNode));
                    } else if ("ranks".equals(tagName)) {
                        services[i].setWithoutRank(getNamedAttributeValue(childNode, "without"));
                        services[i].setRankPrefix(getNamedAttributeValue(childNode, "prefix"));
                        services[i].setRanks(getRanks(childNode));
                    } else if ("mustering-out".equals(tagName)) {
                        NodeList musteringOutList = childNode.getChildNodes();
                        int musteringOutCount = musteringOutList.getLength();
                        for (int k = 0; k < musteringOutCount; k++) {
                            Node musteringOutChild = musteringOutList.item(k);
                            String moTagName = musteringOutChild.getNodeName();
                            if ("benefits".equals(moTagName)) {
                                services[i].setBenefits(getBenefits(musteringOutChild));
                            } else if ("cash".equals(moTagName)) {
                                services[i].setCashTable(getCashTable(musteringOutChild));
                            }
                        }
                    } else if ("skills".equals(tagName)) {
                        services[i].setSkillEligibility(Integer.parseInt(getNamedAttributeValue(childNode, "eligibility")));

                        NodeList skillsListList = childNode.getChildNodes();
                        int skillsListCount = skillsListList.getLength();
                        for (int k = 0; k < skillsListCount; k++) {
                            Node skillsListChild = skillsListList.item(k);
                            String slTagName = skillsListChild.getNodeName();
                            if ("automatic-skills".equals(slTagName)) {
                                services[i].setAutomaticSkills(getAutomaticSkills(skillsListChild));
                            } else if ("personal-development".endsWith(slTagName)) {
                                services[i].setPersonalDevelopment(getSkillTable(skillsListChild));
                            } else if ("service-skills".endsWith(slTagName)) {
                                services[i].setServiceSkills(getSkillTable(skillsListChild));
                            } else if ("advanced-education".endsWith(slTagName)) {
                                services[i].setAdvancedEducation(getSkillTable(skillsListChild));
                            } else if ("advanced-education-edu8plus".endsWith(slTagName)) {
                                services[i].setAdvancedEducationEdu8Plus(getSkillTable(skillsListChild));
                            }
                        }
                    }
                }
            }
        }

        return services;
    }

    private static SuccessNumber getSuccessNumber(Node successNumberNode) {
        SuccessNumber successNumber = null;

        NamedNodeMap attrs = successNumberNode.getAttributes();
        Node minNode = attrs.getNamedItem("min");
        if (minNode != null) {
            int min = Integer.parseInt(minNode.getNodeValue());
            successNumber = new SuccessNumber(min);

            NodeList dmList = successNumberNode.getChildNodes();
            int dmCount = dmList.getLength();
            for (int j = 0; j < dmCount; j++) {
                Node dmNode = dmList.item(j);
                String tagName = dmNode.getNodeName();
                if ("str".equals(tagName)) {
                    successNumber.setStrDM(getDieModifier(dmNode));
                } else if ("dex".equals(tagName)) {
                    successNumber.setDexDM(getDieModifier(dmNode));
                } else if ("end".equals(tagName)) {
                    successNumber.setEndDM(getDieModifier(dmNode));
                } else if ("int".equals(tagName)) {
                    successNumber.setIntDM(getDieModifier(dmNode));
                } else if ("edu".equals(tagName)) {
                    successNumber.setEduDM(getDieModifier(dmNode));
                } else if ("soc".equals(tagName)) {
                    successNumber.setSocDM(getDieModifier(dmNode));
                }
            }
        }

        return successNumber;
    }

    private static DieModifier getDieModifier(Node dmNode) {
        int min = Integer.parseInt(getNamedAttributeValue(dmNode, "min"));
        int dm = Integer.parseInt(getNamedAttributeValue(dmNode, "dm"));

        return new DieModifier(min, dm);
    }

    private static String[] getRanks(Node childNode) {
        String[] ranks = null;

        NodeList rankList = childNode.getChildNodes();
        String r1 = null;
        String r2 = null;
        String r3 = null;
        String r4 = null;
        String r5 = null;
        String r6 = null;
        int rankCount = rankList.getLength();
        for (int i = 0; i < rankCount; i++) {
            Node rankNode = rankList.item(i);
            String tagName = rankNode.getNodeName();
            if ("r1".equals(tagName)) {
                r1 = getNamedAttributeValue(rankNode, "name");
            } else if ("r2".equals(tagName)) {
                r2 = getNamedAttributeValue(rankNode, "name");
            } else if ("r3".equals(tagName)) {
                r3 = getNamedAttributeValue(rankNode, "name");
            } else if ("r4".equals(tagName)) {
                r4 = getNamedAttributeValue(rankNode, "name");
            } else if ("r5".equals(tagName)) {
                r5 = getNamedAttributeValue(rankNode, "name");
            } else if ("r6".equals(tagName)) {
                r6 = getNamedAttributeValue(rankNode, "name");
            }
        }

        if (r1 != null) {
            if (r2 != null) {
                if (r3 != null) {
                    if (r4 != null) {
                        if (r5 != null) {
                            if (r6 != null) {
                                ranks = new String[6];
                                ranks[5] = r6;
                            } else {
                                ranks = new String[5];
                            }
                            ranks[4] = r5;
                        } else {
                            ranks = new String[4];
                        }
                        ranks[3] = r4;
                    } else {
                        ranks = new String[3];
                    }
                    ranks[2] = r3;
                } else {
                    ranks = new String[2];
                }
                ranks[1] = r2;
            } else {
                ranks = new String[1];
            }
            ranks[0] = r1;
        } else {
            ranks = null;
        }

        return ranks;
    }

    private static String[] getBenefits(Node childNode) {
        String[] benefits = null;

        NodeList benefitsList = childNode.getChildNodes();
        String b1 = null;
        String b2 = null;
        String b3 = null;
        String b4 = null;
        String b5 = null;
        String b6 = null;
        String b7 = null;
        int benefitsCount = benefitsList.getLength();
        for (int i = 0; i < benefitsCount; i++) {
            Node benefitNode = benefitsList.item(i);
            String tagName = benefitNode.getNodeName();
            if ("b1".equals(tagName)) {
                b1 = getNamedAttributeValue(benefitNode, "name");
            } else if ("b2".equals(tagName)) {
                b2 = getNamedAttributeValue(benefitNode, "name");
            } else if ("b3".equals(tagName)) {
                b3 = getNamedAttributeValue(benefitNode, "name");
            } else if ("b4".equals(tagName)) {
                b4 = getNamedAttributeValue(benefitNode, "name");
            } else if ("b5".equals(tagName)) {
                b5 = getNamedAttributeValue(benefitNode, "name");
            } else if ("b6".equals(tagName)) {
                b6 = getNamedAttributeValue(benefitNode, "name");
            } else if ("b7".equals(tagName)) {
                b7 = getNamedAttributeValue(benefitNode, "name");
            }
        }

        if (b1 != null) {
            if (b2 != null) {
                if (b3 != null) {
                    if (b4 != null) {
                        if (b5 != null) {
                            if (b6 != null) {
                                if (b7 != null) {
                                    benefits = new String[7];
                                    benefits[6] = b7;
                                } else {
                                    benefits = new String[6];
                                }
                                benefits[5] = b6;
                            } else {
                                benefits = new String[5];
                            }
                            benefits[4] = b5;
                        } else {
                            benefits = new String[4];
                        }
                        benefits[3] = b4;
                    } else {
                        benefits = new String[3];
                    }
                    benefits[2] = b3;
                } else {
                    benefits = new String[2];
                }
                benefits[1] = b2;
            } else {
                benefits = new String[1];
            }
            benefits[0] = b1;
        } else {
            benefits = null;
        }

        return benefits;
    }

    private static int[] getCashTable(Node childNode) {
        int[] cashTable = null;

        NodeList cashList = childNode.getChildNodes();
        String c1 = null;
        String c2 = null;
        String c3 = null;
        String c4 = null;
        String c5 = null;
        String c6 = null;
        String c7 = null;
        int cashCount = cashList.getLength();
        for (int i = 0; i < cashCount; i++) {
            Node cashNode = cashList.item(i);
            String tagName = cashNode.getNodeName();
            if ("c1".equals(tagName)) {
                c1 = getNamedAttributeValue(cashNode, "name");
            } else if ("c2".equals(tagName)) {
                c2 = getNamedAttributeValue(cashNode, "name");
            } else if ("c3".equals(tagName)) {
                c3 = getNamedAttributeValue(cashNode, "name");
            } else if ("c4".equals(tagName)) {
                c4 = getNamedAttributeValue(cashNode, "name");
            } else if ("c5".equals(tagName)) {
                c5 = getNamedAttributeValue(cashNode, "name");
            } else if ("c6".equals(tagName)) {
                c6 = getNamedAttributeValue(cashNode, "name");
            } else if ("c7".equals(tagName)) {
                c7 = getNamedAttributeValue(cashNode, "name");
            }
        }

        if (c1 != null) {
            if (c2 != null) {
                if (c3 != null) {
                    if (c4 != null) {
                        if (c5 != null) {
                            if (c6 != null) {
                                if (c7 != null) {
                                    cashTable = new int[7];
                                    cashTable[6] = Integer.parseInt(c7);
                                } else {
                                    cashTable = new int[6];
                                }
                                cashTable[5] = Integer.parseInt(c6);
                            } else {
                                cashTable = new int[5];
                            }
                            cashTable[4] = Integer.parseInt(c5);
                        } else {
                            cashTable = new int[4];
                        }
                        cashTable[3] = Integer.parseInt(c4);
                    } else {
                        cashTable = new int[3];
                    }
                    cashTable[2] = Integer.parseInt(c3);
                } else {
                    cashTable = new int[2];
                }
                cashTable[1] = Integer.parseInt(c2);
            } else {
                cashTable = new int[1];
            }
            cashTable[0] = Integer.parseInt(c1);
        } else {
            cashTable = null;
        }

        return cashTable;
    }

    private static String[] getAutomaticSkills(Node childNode) {
        String[] autoSkills = new String[7];

        NodeList skillList = childNode.getChildNodes();
        int skillCount = skillList.getLength();
        for (int i = 0; i < skillCount; i++) {
            Node skillNode = skillList.item(i);
            String tagName = skillNode.getNodeName();
            if ("r0".equals(tagName)) {
                autoSkills[0] = getNamedAttributeValue(skillNode, "name");
            } else if ("r1".equals(tagName)) {
                autoSkills[1] = getNamedAttributeValue(skillNode, "name");
            } else if ("r2".equals(tagName)) {
                autoSkills[2] = getNamedAttributeValue(skillNode, "name");
            } else if ("r3".equals(tagName)) {
                autoSkills[3] = getNamedAttributeValue(skillNode, "name");
            } else if ("r4".equals(tagName)) {
                autoSkills[4] = getNamedAttributeValue(skillNode, "name");
            } else if ("r5".equals(tagName)) {
                autoSkills[5] = getNamedAttributeValue(skillNode, "name");
            } else if ("r6".equals(tagName)) {
                autoSkills[6] = getNamedAttributeValue(skillNode, "name");
            }
        }

        return autoSkills;
    }

    private static String[] getSkillTable(Node childNode) {
        String[] skillTable = new String[6];

        NodeList skillList = childNode.getChildNodes();
        int skillCount = skillList.getLength();
        for (int i = 0; i < skillCount; i++) {
            Node skillNode = skillList.item(i);
            String tagName = skillNode.getNodeName();
            if ("s1".equals(tagName)) {
                skillTable[0] = getNamedAttributeValue(skillNode, "name");
            } else if ("s2".equals(tagName)) {
                skillTable[1] = getNamedAttributeValue(skillNode, "name");
            } else if ("s3".equals(tagName)) {
                skillTable[2] = getNamedAttributeValue(skillNode, "name");
            } else if ("s4".equals(tagName)) {
                skillTable[3] = getNamedAttributeValue(skillNode, "name");
            } else if ("s5".equals(tagName)) {
                skillTable[4] = getNamedAttributeValue(skillNode, "name");
            } else if ("s6".equals(tagName)) {
                skillTable[5] = getNamedAttributeValue(skillNode, "name");
            }
        }

        return skillTable;
    }

    private static String getNamedAttributeValue(Node node, String attributeName) {
        String attributeValue = null;

        NamedNodeMap nodeAttrs = node.getAttributes();
        attributeValue = nodeAttrs.getNamedItem(attributeName).getNodeValue();

        return attributeValue;
    }

    private ServiceFactory() {
        // only to keep'em from instantiating
    }
}

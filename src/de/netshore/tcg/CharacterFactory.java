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
 * Created on 20.04.2005
 */
package de.netshore.tcg;

import de.netshore.tcg.Character.UPP;
import de.netshore.tcg.Service.SuccessNumber;

/**
 * @author jlin
 *         <p>
 *         CharacterFactory - generates characters as defined in CT Book 1
 */
public class CharacterFactory {
    public interface GenerationListener {
        public void startGeneration(Character character);

        public void infoGeneration(int event, String message);

        public void endGeneration();

        /**
         * @param services loaded services to choose from
         * @return choosed service's index or -1 for random service selection
         */
        public int chooseService(Service[] services);

        /**
         * @param tableNames  names of available skill tables to choose from
         * @param skillTables content of available skill tables
         * @return choosed table's name index or -1 for random skill table selection
         */
        public int chooseSkillTable(String[] tableNames, String[][] skillTables);

        /**
         * @param currentTerm number of terms already served
         * @return true: reenlist, false: quit service
         */
        public boolean chooseReenlistment(int currentTerm);

        /**
         * @param cashTable content of cash table
         * @param benefits  content of benefit table
         * @return 0: benefits, 1: cash, -1 for random selection
         */
        public int chooseBenefitsOrCash(String[] benefits, int[] cashTable);
    }

    public static class GeneratorConfig {
        public Service[] services = ServiceFactory.getServices("book1.xml");
        public int preferredService = -1; // -1 means random service, no preferance
        public int preferredTermNum = -1; // -1 means as long as possible
        public boolean injuryPreferred = false; // true means injury instead of death on failure to survive  
        private GenerationListener listener = null;

        public GeneratorConfig() {
            injuryPreferred = TCGPrefs.getInstance().isInjuryPreferred();
        }
    }

    private static class DefaultGenerationListener implements GenerationListener {
        private GeneratorConfig config = null;

        private DefaultGenerationListener(GeneratorConfig config) {
            this.config = config;
        }

        public void startGeneration(Character character) {
            // nothing
        }

        public void infoGeneration(int event, String message) {
            //TCGApp.debug(message);
        }

        public void endGeneration() {
            // nothing
        }

        public int chooseService(Service[] services) {
            return config.preferredService;
        }

        public int chooseSkillTable(String[] tableNames, String[][] skillTables) {
            return -1;
        }

        public boolean chooseReenlistment(int currentTerm) {
            return (0 > config.preferredTermNum || config.preferredTermNum > currentTerm);
        }

        public int chooseBenefitsOrCash(String[] benefits, int[] cashTable) {
            return -1;
        }
    }

    public static Character generateCharacter(GeneratorConfig config, GenerationListener listener) {
        config.listener = listener;
        return generateCharacter(config);
    }

    /**
     * @param config
     * @return
     */
    public static Character generateCharacter(GeneratorConfig config) {
        if (config.listener == null) {
            config.listener = new DefaultGenerationListener(config);
        }

        Character character = new Character();

        config.listener.startGeneration(character);

        Service career = null;
        config.preferredService = config.listener.chooseService(config.services);
        if (0 <= config.preferredService && config.preferredService < config.services.length) {
            career = config.services[config.preferredService];
        } else { // assume preferredService -1
            career = config.services[Dice.roll(1, config.services.length) - 1];
        }
        
        /*
         * ENLISTMENT
         */
        TCGApp.debug("trying to enlist to service '" + career.getName() + "'");
        if (career.getEnlistment().test(character)) {
            // successfull enlistment
            TCGApp.debug("enlistment succeeded");
            character.setService(career.getName());
            config.listener.infoGeneration(0, "enlistment succeeded");
        } else {
            career = null;

            // draft roll
            int draftRoll = Dice.roll(1);
            TCGApp.debug("enlistment failed, rolled " + draftRoll + " for draft");
            for (int i = 0; i < config.services.length; i++) {
                if (config.services[i].getDraftNum() == draftRoll) {
                    career = config.services[i];

                    TCGApp.debug("drafted into service '" + career.getName() + "'");
                    character.setDrafted(true);
                    character.setService(career.getName());
                    break;
                }
            }

            // random fallback draft
            if (career == null) {
                career = config.services[Dice.roll(1, config.services.length) - 1];

                TCGApp.debug("draft failed to resolve, randomly drafted into service '" + career.getName() + "'");
                character.setDrafted(true);
                character.setService(career.getName());
            }

            config.listener.infoGeneration(0, "enlistment failed, drafted into " + career.getName());
        }
        character.setFinalRank(career.getWithoutRank());
        character.setRankPrefix(career.getRankPrefix());
        
        /*
         * TERM RESOLUTION
         */
        while (character.isAlive() && !character.isMusteredOut()) {
            int term = character.getTermsServed() + 1;
            int skillEligibility = (term == 1 ? 2 : career.getSkillEligibility());
            
            /*
             * SURVIVAL
             */
            TCGApp.debug("trying to survive term " + term);
            if (career.getSurvival().test(character)) {
                // successful survival
                TCGApp.debug("term " + term + " survived");
                character.setTermsServed(term);
                config.listener.infoGeneration(0, "term " + term + " survived");
            } else if (config.injuryPreferred) {
                TCGApp.debug("character got injured");
                character.setAlive(false);
                config.listener.infoGeneration(0, "character got injured during term " + term);
                character.setMusteredOut(true);
                break;
            } else {
                TCGApp.debug("character died");
                character.setAlive(false);
                config.listener.infoGeneration(0, "character died during term " + term);
                break;
            }
            /*
             * Automatic Service Skill 
             */
            if (term == 1) {
                TCGApp.debug("checking for automatic service skills");
                String skill = career.getAutomaticSkill(character.getRankNum());
                if (skill != null) {
                    TCGApp.debug("received: " + skill);
                    if (!consumeCharacteristicModifier(skill, character)) {
                        character.addSkillLevel(skill);
                    }
                    config.listener.infoGeneration(0, "automatic skill received: " + skill);
                } else {
                    TCGApp.debug("received nothing");
                }
            }

            if (career.getCommission() != null && career.getPromotion() != null && career.getRanks() != null && !(character.isDrafted() && term == 1)) {
                int rank = character.getRankNum();
                if (rank == 0) {
                    /*
                     * COMMISSION
                     */
                    TCGApp.debug("trying to receive commission");
                    if (career.getCommission().test(character)) {
                        rank++;
                        skillEligibility++;

                        TCGApp.debug("commission received, new rank is '" + career.getRanks()[rank - 1] + "'");
                        character.setRankNum(rank);
                        character.setFinalRank(career.getRanks()[rank - 1]);
                        config.listener.infoGeneration(0, "commission received");
                        /*
                         * Automatic Skill for new rank
                         */
                        TCGApp.debug("checking for automatic skills");
                        String skill = career.getAutomaticSkill(character.getRankNum());
                        if (skill != null) {
                            TCGApp.debug("received: " + skill);
                            if (!consumeCharacteristicModifier(skill, character)) {
                                character.addSkillLevel(skill);
                            }
                            config.listener.infoGeneration(0, "automatic skill received: " + skill);
                        } else {
                            TCGApp.debug("received nothing");
                        }
                    } else {
                        TCGApp.debug("commission denied");
                    }
                }
                if (0 < rank && rank < career.getRanks().length) {
                    /*
                     * PROMOTION
                     */
                    TCGApp.debug("trying to receive promotion");
                    if (career.getPromotion().test(character)) {
                        rank++;
                        skillEligibility++;

                        TCGApp.debug("promotion received, new rank is '" + career.getRanks()[rank - 1] + "'");
                        character.setRankNum(rank);
                        character.setFinalRank(career.getRanks()[rank - 1]);
                        config.listener.infoGeneration(0, "promotion to " + career.getRanks()[rank - 1]);
                        /*
                         * Automatic Skill for new rank
                         */
                        TCGApp.debug("checking for automatic skills");
                        String skill = career.getAutomaticSkill(character.getRankNum());
                        if (skill != null) {
                            TCGApp.debug("received: " + skill);
                            if (!consumeCharacteristicModifier(skill, character)) {
                                character.addSkillLevel(skill);
                            }
                            config.listener.infoGeneration(0, "automatic skill received: " + skill);
                        } else {
                            TCGApp.debug("received nothing");
                        }
                    } else {
                        TCGApp.debug("promotion denied");
                    }
                }
            }
            
            /*
             * SKILLS
             */
            String[] tableNames = {
                    "Personal Development",
                    "Service Skills",
                    "Advanced Education"
            };
            String[][] skillTables = {
                    career.getPersonalDevelopment(),
                    career.getServiceSkills(),
                    career.getAdvancedEducation()
            };
            String[] tableNamesEdu8Plus = {
                    "Personal Development",
                    "Service Skills",
                    "Advanced Education",
                    "Advanced Education (Edu 8+)"
            };
            String[][] skillTablesEdu8Plus = {
                    career.getPersonalDevelopment(),
                    career.getServiceSkills(),
                    career.getAdvancedEducation(),
                    career.getAdvancedEducationEdu8Plus()
            };
            TCGApp.debug("character becomes eligible for " + skillEligibility + " skill" + (skillEligibility != 1 ? "s" : ""));
            config.listener.infoGeneration(0, "skill eligibility: " + skillEligibility);
            for (int i = 0; i < skillEligibility; i++) {
                int whichTable = 0;
                whichTable = (character.getCharacteristic(UPP.EDU) >= 8 ?
                        config.listener.chooseSkillTable(tableNamesEdu8Plus, skillTablesEdu8Plus) :
                        config.listener.chooseSkillTable(tableNames, skillTables));
                if (0 > whichTable || whichTable >= (character.getCharacteristic(UPP.EDU) >= 8 ? 4 : 3)) {
                    whichTable = Dice.roll(1, (character.getCharacteristic(UPP.EDU) >= 8 ? 4 : 3)) - 1;
                }

                int whichEntry = Dice.roll(1) - 1;
                TCGApp.debug("rolling on table " + tableNamesEdu8Plus[whichTable]);
                String skill = skillTablesEdu8Plus[whichTable][whichEntry];

                TCGApp.debug("received: " + skill);
                if (!consumeCharacteristicModifier(skill, character)) {
                    character.addSkillLevel(skill);
                }
                config.listener.infoGeneration(0, "skill received: " + skill);
            }
            
            /*
             * AGING
             */
            doAging(character);
            
            /*
             * REENLISTMENT
             */
            if (Dice.roll(2) == 12) {
                // mandatory reenlistment
                TCGApp.debug("mandatory reenlistment");
                config.listener.infoGeneration(0, "mandatory reenlistment");
                continue;
            }
            if (term >= 7) {
                // mandatory retirement
                TCGApp.debug("mandatory retirement");
                character.setMusteredOut(true);
                config.listener.infoGeneration(0, "mandatory retirement");
                break;
            }
            if (config.listener.chooseReenlistment(term)) {
                TCGApp.debug("trying to reenlist to service '" + career.getName() + "'");
                if (career.getReenlistment().test(character)) {
                    // successful reenlistment
                    TCGApp.debug("reenlistment succeeded");
                    continue;
                } else {
                    // leaving service
                    TCGApp.debug("reenlistment failed, leaving service");
                    character.setMusteredOut(true);
                    break;
                }
            } else {
                // leaving service or retiring
                if (term >= 5) {
                    TCGApp.debug("character decided to retire");
                } else {
                    TCGApp.debug("character decided to leave service");
                }
                character.setMusteredOut(true);
                break;
            }
        }
        
        /*
         * MUSTERING OUT
         */
        if ((character.isAlive() || config.injuryPreferred) && character.isMusteredOut()) {
            int numberOfRolls = character.getTermsServed() + (int) Math.ceil(character.getRankNum() / 2.0);

            TCGApp.debug("mustering out, rolling " + numberOfRolls + " time" + (numberOfRolls != 1 ? "s" : ""));
            config.listener.infoGeneration(0, "mustering out, benefit eligibility: " + numberOfRolls);

            int maxNumOfCashRollsRemaining = 3;
            int cashTotal = 0;

            String[] benefits = career.getBenefits();
            int[] cashTable = career.getCashTable();
            for (int i = 0; i < numberOfRolls; i++) {
                int chosenTable = (maxNumOfCashRollsRemaining > 0 ? config.listener.chooseBenefitsOrCash(benefits, cashTable) : 0);
                if (0 > chosenTable || chosenTable > 1) {
                    chosenTable = Dice.roll(1, 2) - 1;
                }
                int roll = 0;
                switch (chosenTable) {
                    case 0:
                        TCGApp.debug("rolling for benefit");
                        roll = Dice.roll(1) + (character.getRankNum() >= 5 ? 1 : 0);
                        roll = (roll > benefits.length ? benefits.length : roll);
                        String benefit = benefits[roll - 1];

                        TCGApp.debug("received beenfit: " + benefit);
                        if (!consumeCharacteristicModifier(benefit, character)) {
                            character.addPossession(benefit);
                        }
                        config.listener.infoGeneration(0, "received benefit: " + benefit);
                        break;
                    case 1:
                        maxNumOfCashRollsRemaining--;

                        TCGApp.debug("rolling for cash");
                        roll = Dice.roll(1) + (character.getSkillLevel("Gambling") >= 1 ? 1 : 0);
                        roll = (roll > cashTable.length ? cashTable.length : roll);
                        int cash = cashTable[roll - 1];

                        TCGApp.debug("received cash: Cr" + cash);
                        cashTotal += cash;
                        character.setCash(cashTotal);
                        config.listener.infoGeneration(0, "received cash: Cr" + cash);
                        break;
                }
            }
        }
        
        /*
         * FINISHING UP
         */
        int age = 18 + character.getTermsServed() * 4;
        if (!character.isAlive() && config.injuryPreferred) {
            age += 2;
            character.setAwardsAndDecorations("(got severely injured - and maybe decorated - during term " + (character.getTermsServed() + 1) + ")");
        }
        character.setBirthdate("Age " + age);
        config.listener.infoGeneration(0, "done");

        config.listener.endGeneration();

        return character;
    }

    private static boolean consumeCharacteristicModifier(String possibleModifier, Character character) {
        boolean consumed = true;

        if (possibleModifier.startsWith("Str+")) {
            TCGApp.debug("modifying original " + possibleModifier.substring(0, 3) + " of " + character.getCharacteristic(UPP.STR));
            character.modifyCharacteristic(UPP.STR, Integer.parseInt(possibleModifier.substring(4)));
        } else if (possibleModifier.startsWith("Dex+")) {
            TCGApp.debug("modifying original " + possibleModifier.substring(0, 3) + " of " + character.getCharacteristic(UPP.DEX));
            character.modifyCharacteristic(UPP.DEX, Integer.parseInt(possibleModifier.substring(4)));
        } else if (possibleModifier.startsWith("End+")) {
            TCGApp.debug("modifying original " + possibleModifier.substring(0, 3) + " of " + character.getCharacteristic(UPP.END));
            character.modifyCharacteristic(UPP.END, Integer.parseInt(possibleModifier.substring(4)));
        } else if (possibleModifier.startsWith("Int+")) {
            TCGApp.debug("modifying original " + possibleModifier.substring(0, 3) + " of " + character.getCharacteristic(UPP.INT));
            character.modifyCharacteristic(UPP.INT, Integer.parseInt(possibleModifier.substring(4)));
        } else if (possibleModifier.startsWith("Edu+")) {
            TCGApp.debug("modifying original " + possibleModifier.substring(0, 3) + " of " + character.getCharacteristic(UPP.EDU));
            character.modifyCharacteristic(UPP.EDU, Integer.parseInt(possibleModifier.substring(4)));
        } else if (possibleModifier.startsWith("Soc+")) {
            TCGApp.debug("modifying original " + possibleModifier.substring(0, 3) + " of " + character.getCharacteristic(UPP.SOC));
            character.modifyCharacteristic(UPP.SOC, Integer.parseInt(possibleModifier.substring(4)));
        } else if (possibleModifier.startsWith("Str-")) {
            TCGApp.debug("modifying original " + possibleModifier.substring(0, 3) + " of " + character.getCharacteristic(UPP.STR));
            character.modifyCharacteristic(UPP.STR, Integer.parseInt(possibleModifier.substring(3)));
        } else if (possibleModifier.startsWith("Dex-")) {
            TCGApp.debug("modifying original " + possibleModifier.substring(0, 3) + " of " + character.getCharacteristic(UPP.DEX));
            character.modifyCharacteristic(UPP.DEX, Integer.parseInt(possibleModifier.substring(3)));
        } else if (possibleModifier.startsWith("End-")) {
            TCGApp.debug("modifying original " + possibleModifier.substring(0, 3) + " of " + character.getCharacteristic(UPP.END));
            character.modifyCharacteristic(UPP.END, Integer.parseInt(possibleModifier.substring(3)));
        } else if (possibleModifier.startsWith("Int-")) {
            TCGApp.debug("modifying original " + possibleModifier.substring(0, 3) + " of " + character.getCharacteristic(UPP.INT));
            character.modifyCharacteristic(UPP.INT, Integer.parseInt(possibleModifier.substring(3)));
        } else if (possibleModifier.startsWith("Edu-")) {
            TCGApp.debug("modifying original " + possibleModifier.substring(0, 3) + " of " + character.getCharacteristic(UPP.EDU));
            character.modifyCharacteristic(UPP.EDU, Integer.parseInt(possibleModifier.substring(3)));
        } else if (possibleModifier.startsWith("Soc-")) {
            TCGApp.debug("modifying original " + possibleModifier.substring(0, 3) + " of " + character.getCharacteristic(UPP.SOC));
            character.modifyCharacteristic(UPP.SOC, Integer.parseInt(possibleModifier.substring(3)));
        } else {
            consumed = false;
        }

        return consumed;
    }

    private static void doAging(Character character) {
        // term  0 -  3: no aging
        // term  4 -  7: aging level 1
        // term  8 - 11: aging level 2
        // term 12+    : aging level 3+
        int agingLevel = (int) Math.floor(character.getTermsServed() / 4);
        if (agingLevel < 0) {
            agingLevel = 0;
        } else if (agingLevel > 3) {
            agingLevel = 3;
        }
        SuccessNumber strAging = null;
        SuccessNumber dexAging = null;
        SuccessNumber endAging = null;
        SuccessNumber intAging = null;
        int strReduction = 0;
        int dexReduction = 0;
        int endReduction = 0;
        int intReduction = 0;
        switch (agingLevel) {
            case 0:
                // no aging;
                break;
            case 1:
                strAging = new SuccessNumber(8);
                dexAging = new SuccessNumber(7);
                endAging = new SuccessNumber(8);
                strReduction = -1;
                dexReduction = -1;
                endReduction = -1;
                break;
            case 2:
                strAging = new SuccessNumber(9);
                dexAging = new SuccessNumber(8);
                endAging = new SuccessNumber(9);
                strReduction = -1;
                dexReduction = -1;
                endReduction = -1;
                break;
            case 3:
                strAging = new SuccessNumber(9);
                dexAging = new SuccessNumber(9);
                endAging = new SuccessNumber(9);
                intAging = new SuccessNumber(9);
                strReduction = -2;
                dexReduction = -2;
                endReduction = -2;
                intReduction = -1;
                break;
        }
        if (strAging != null) {
            TCGApp.debug("checking for aging effects on Str");
            if (!strAging.test(character)) {
                TCGApp.debug("aging: Str" + strReduction);
                character.modifyCharacteristic(UPP.STR, strReduction);
            }
        }
        if (dexAging != null) {
            TCGApp.debug("checking for aging effects on Dex");
            if (!dexAging.test(character)) {
                TCGApp.debug("aging: Dex" + dexReduction);
                character.modifyCharacteristic(UPP.DEX, dexReduction);
            }
        }
        if (endAging != null) {
            TCGApp.debug("checking for aging effects on End");
            if (!endAging.test(character)) {
                TCGApp.debug("aging: End" + endReduction);
                character.modifyCharacteristic(UPP.END, endReduction);
            }
        }
        if (intAging != null) {
            TCGApp.debug("checking for aging effects on Int");
            if (!intAging.test(character)) {
                TCGApp.debug("aging: Int" + intReduction);
                character.modifyCharacteristic(UPP.INT, intReduction);
            }
        }
    }

    private CharacterFactory() {
        // only to keep'em from instantiating
    }
}

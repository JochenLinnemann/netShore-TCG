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
 * Created on 04.04.2005
 */
package de.netshore.tcg;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * @author jlin
 *         <p>
 *         Character - represents a generated Traveller character
 */
public class Character implements Serializable {
    /**
     * @author jlin
     *         <p>
     *         UPP - Universal Personality Profile
     */
    public class UPP implements Serializable {
        /*
         * Characteristics constants (UPP index)
         */
        public static final int STR = 0;
        public static final int DEX = 1;
        public static final int END = 2;
        public static final int INT = 3;
        public static final int EDU = 4;
        public static final int SOC = 5;
        /*
         * Characteristics constants
         */
        public static final int MIN_CHAR_VAL = 1;
        public static final int MAX_CHAR_VAL = 15;
        /**
         * UPP - Universal Personality Profile
         */
        private int[] upp = new int[6];

        public UPP() {
            upp[STR] = Dice.roll(2);
            upp[DEX] = Dice.roll(2);
            upp[END] = Dice.roll(2);
            upp[INT] = Dice.roll(2);
            upp[EDU] = Dice.roll(2);
            upp[SOC] = Dice.roll(2);
        }

        public void modifyCharacteristic(int which, int mod) {
            setCharacteristic(which, getCharacteristic(which) + mod);
        }

        public void setCharacteristic(int which, int value) {
            if (STR <= which && which <= SOC && MIN_CHAR_VAL <= value && value <= MAX_CHAR_VAL) {
                upp[which] = value;
            }
        }

        public int getCharacteristic(int which) {
            if (STR <= which && which <= SOC) {
                return upp[which];
            } else {
                return -1;
            }
        }

        public String toString() {
            return (Integer.toHexString(upp[STR]) +
                    Integer.toHexString(upp[DEX]) +
                    Integer.toHexString(upp[END]) +
                    Integer.toHexString(upp[INT]) +
                    Integer.toHexString(upp[EDU]) +
                    Integer.toHexString(upp[SOC])).toUpperCase();
        }
    }

    /**
     * @author jlin
     *         <p>
     *         Possession - item owned
     */
    public class Possession implements Serializable {
        private String nameOfItem = null;
        private int numberOfItems = 0;

        public Possession(String nameOfItem) {
            setNameOfItem(nameOfItem);
            setNumberOfItems(1);
        }

        public void setNameOfItem(String newNameOfItem) {
            nameOfItem = newNameOfItem;
        }

        public String getNameOfItem() {
            return nameOfItem;
        }

        public void setNumberOfItems(int newNumberOfItems) {
            numberOfItems = newNumberOfItems;
        }

        public int getNumberOfItems() {
            return numberOfItems;
        }
    }

    /**
     * @author jlin
     *         <p>
     *         PossessionIterator - iterate over character's posession
     */
    public class PossessionIterator implements Iterator {
        private Iterator iter = possessions.values().iterator();
        private Possession current = null;

        public String getNameOfItem() {
            return current.getNameOfItem();
        }

        public int getNumberOfItems() {
            return current.getNumberOfItems();
        }

        public void remove() {
            iter.remove();
        }

        public boolean hasNext() {
            return iter.hasNext();
        }

        public Object next() {
            current = (Possession) iter.next();
            return current;
        }

    }

    /**
     * @author jlin
     *         <p>
     *         Skill - just that, complete with level
     */
    public class Skill implements Serializable {
        private String name = null;
        private int level = 0;

        public Skill(String name) {
            setName(name);
            setLevel(1);
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getLevel() {
            return level;
        }
    }

    /**
     * @author jlin
     *         <p>
     *         SkillIterator - iterate over character's skills
     */
    public class SkillIterator implements Iterator {
        private Iterator iter = skills.values().iterator();
        private Skill current = null;

        public String getName() {
            return current.getName();
        }

        public int getLevel() {
            return current.getLevel();
        }

        public void remove() {
            iter.remove();
        }

        public boolean hasNext() {
            return iter.hasNext();
        }

        public Object next() {
            current = (Skill) iter.next();
            return current;
        }
    }

    private Date creationDate = new Date();
    // 1. Date of Preparation
    private String dateOfPreparation = "";
    // 2. Name
    private String name = "";
    // 3. UPP
    private UPP upp = new UPP();
    // 4. Noble Title
    private String nobleTitle = "";
    // 5. Military Rank
    private String militaryRank = "";
    // 6. Birthdate
    private String birthdate = "";
    // 7. Age Modifiers
    private String ageModifiers = "";
    // 8. Birthworld
    private String birthworld = "";
    private boolean drafted = false;
    // 9. Service
    private String service = "";
    // 10. Branch
    private String branch = "";
    // 11. Dischargeworld
    private String dischargeworld = "";
    private boolean alive = true;
    // 12. Terms Served, via calculation: 14a. Retired?, 14b. Retirement Pay
    private int termsServed = 0;
    private String rankPrefix = "";
    private int rankNum = 0;
    // 13. Final Rank
    private String finalRank = "";
    // 15. Special Assignments
    private String specialAssignments = "";
    // 16. Awards and Decorations
    private String awardsAndDecorations = "";
    // 17. Equipment Qualified On
    private String equipmentQualifiedOn = "";
    // 18a. Primary Skill
    // 18b. Secondary Skill
    // 18c. Additional Skills
    private TreeMap skills = new TreeMap();
    // 19a. Preferred Weapon
    private String preferredWeapon = "";
    // 19b. Preferred Pistol
    private String preferredPistol = "";
    // 19c. Preferred Blade
    private String preferredBlade = "";
    // 21. Date of Test
    // 22. PSR
    // 23a. Trained?
    // 23b. Date Completed
    // 24. Talents and Current Levels
    // NO PSIONICS
    private boolean musteredOut = false;
    private int cash = 0;
    private TreeMap possessions = new TreeMap();

    public Character() {
        setDateOfPreparation(DateFormat.getDateInstance(DateFormat.MEDIUM).format(creationDate));
    }

    public void setDateOfPreparation(String dateOfPreparation) {
        this.dateOfPreparation = dateOfPreparation;
    }

    public String getDateOfPreparation() {
        return dateOfPreparation;
    }

    public void setName(String newName) {
        name = newName;
    }

    public String getName() {
        return name;
    }

    public void modifyCharacteristic(int which, int mod) {
        upp.modifyCharacteristic(which, mod);
    }

    public void setCharacteristic(int which, int value) {
        upp.setCharacteristic(which, value);
    }

    public int getCharacteristic(int which) {
        return upp.getCharacteristic(which);
    }

    public String uppAsString() {
        return upp.toString();
    }

    public void setNobleTitle(String newTitle) {
        nobleTitle = newTitle;
    }

    public String getNobleTitle() {
        return nobleTitle;
    }

    public void setMilitaryRank(String militaryRank) {
        this.militaryRank = militaryRank;
    }

    public String getMilitaryRank() {
        return militaryRank;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setAgeModifiers(String ageModifiers) {
        this.ageModifiers = ageModifiers;
    }

    public String getAgeModifiers() {
        return ageModifiers;
    }

    public void setBirthworld(String birthworld) {
        this.birthworld = birthworld;
    }

    public String getBirthworld() {
        return birthworld;
    }

    public void setDrafted(boolean drafted) {
        this.drafted = drafted;
    }

    public boolean isDrafted() {
        return drafted;
    }

    public void setService(String serviceName) {
        this.service = serviceName;
    }

    public String getService() {
        return service;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getBranch() {
        return branch;
    }

    public void setDischargeworld(String dischargeworld) {
        this.dischargeworld = dischargeworld;
    }

    public String getDischargeworld() {
        return dischargeworld;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setTermsServed(int terms) {
        this.termsServed = terms;
    }

    public int getTermsServed() {
        return termsServed;
    }

    public int getRetirementPay() {
        return (getTermsServed() < 5 ? 0 : (getTermsServed() - 3) * 2000);
    }

    public void setRankPrefix(String rankPrefix) {
        this.rankPrefix = rankPrefix;
    }

    public String getRankPrefix() {
        return rankPrefix;
    }

    public void setRankNum(int rankNum) {
        this.rankNum = rankNum;
    }

    public int getRankNum() {
        return rankNum;
    }

    public void setFinalRank(String rank) {
        this.finalRank = rank;
    }

    public String getFinalRank() {
        return finalRank;
    }

    public void setSpecialAssignments(String specialAssignments) {
        this.specialAssignments = specialAssignments;
    }

    public String getSpecialAssignments() {
        return specialAssignments;
    }

    public void setAwardsAndDecorations(String awardsAndDecorations) {
        this.awardsAndDecorations = awardsAndDecorations;
    }

    public String getAwardsAndDecorations() {
        return awardsAndDecorations;
    }

    public void setEquipmentQualifiedOn(String equipmentQualifiedOn) {
        this.equipmentQualifiedOn = equipmentQualifiedOn;
    }

    public String getEquipmentQualifiedOn() {
        return equipmentQualifiedOn;
    }

    public void addSkillLevel(String name) {
        if (skills.containsKey(name)) {
            Skill skill = (Skill) skills.get(name);
            skill.setLevel(skill.getLevel() + 1);
        } else {
            Skill skill = new Skill(name);
            skills.put(name, skill);
        }
    }

    public void removeSkillLevel(String name) {
        if (skills.containsKey(name)) {
            Skill skill = (Skill) skills.get(name);
            skill.setLevel(skill.getLevel() - 1);
            if (skill.getLevel() < 1) {
                skills.remove(name);
            }
        }
    }

    public int getSkillLevel(String name) {
        int skillLevel = -1;

        if (skills.containsKey(name)) {
            Skill skill = (Skill) skills.get(name);
            skillLevel = skill.getLevel();
        }

        return skillLevel;
    }

    public SkillIterator skillIterator() {
        return new SkillIterator();
    }

    public void setPreferredWeapon(String preferredWeapon) {
        this.preferredWeapon = preferredWeapon;
    }

    public String getPreferredWeapon() {
        return preferredWeapon;
    }

    public void setPreferredPistol(String preferredPistol) {
        this.preferredPistol = preferredPistol;
    }

    public String getPreferredPistol() {
        return preferredPistol;
    }

    public void setPreferredBlade(String preferredBlade) {
        this.preferredBlade = preferredBlade;
    }

    public String getPreferredBlade() {
        return preferredBlade;
    }

    public void setMusteredOut(boolean musteredOut) {
        this.musteredOut = musteredOut;
    }

    public boolean isMusteredOut() {
        return musteredOut;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }

    public int getCash() {
        return cash;
    }

    public void addPossession(String nameOfItem) {
        if (possessions.containsKey(nameOfItem)) {
            Possession item = (Possession) possessions.get(nameOfItem);
            item.setNumberOfItems(item.getNumberOfItems() + 1);
        } else {
            Possession item = new Possession(nameOfItem);
            possessions.put(nameOfItem, item);
        }
    }

    public void removePossession(String nameOfItem) {
        if (possessions.containsKey(nameOfItem)) {
            Possession item = (Possession) possessions.get(nameOfItem);
            item.setNumberOfItems(item.getNumberOfItems() - 1);
            if (item.getNumberOfItems() < 1) {
                possessions.remove(nameOfItem);
            }
        }
    }

    public int getPossessionNumberOfItems(String nameOfItem) {
        int numberOfItems = 0;

        if (possessions.containsKey(nameOfItem)) {
            Possession possession = (Possession) possessions.get(nameOfItem);
            numberOfItems = possession.getNumberOfItems();
        }

        return numberOfItems;
    }

    public PossessionIterator possessionIterator() {
        return new PossessionIterator();
    }

    public String toString() {
        String skillList = "";
        SkillIterator skillIter = skillIterator();
        while (skillIter.hasNext()) {
            skillIter.next();
            if (!"".equals(skillList)) {
                skillList += ", ";
            }
            skillList += skillIter.getName() + "-" + skillIter.getLevel();
        }
        if ("".equals(skillList)) {
            skillList = "no skills";
        }

        String possessionList = "";
        PossessionIterator posIter = possessionIterator();
        while (posIter.hasNext()) {
            posIter.next();
            if (!"".equals(possessionList)) {
                possessionList += ", ";
            }
            possessionList += posIter.getNumberOfItems() + "*" + posIter.getNameOfItem();
        }
        if ("".equals(possessionList)) {
            possessionList = "no possession";
        }

        return
                (getTermsServed() >= 5 ? "Retired " : "Ex-") + (getRankNum() > 0 ? getRankPrefix() + " " : "") + getFinalRank() +
                        "     " + upp + "     Age " + (18 + getTermsServed() * 4) + "     " + getTermsServed() + " terms     Cr" + getCash() +
                        "\n" + skillList +
                        "\n" + possessionList;
    }
}

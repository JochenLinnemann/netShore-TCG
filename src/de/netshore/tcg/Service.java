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
 * Created on 05.04.2005
 */
package de.netshore.tcg;

/**
 * @author jlin
 *         <p>
 *         Service - representing archetypical careers
 */
public class Service {
    /**
     * @author jlin
     *         <p>
     *         SuccessNumber - number to roll to succeed at something
     */
    public static class SuccessNumber {
        /**
         * @author jlin
         *         <p>
         *         DieModifier - which characteristic modifies a dice roll
         */
        public static class DieModifier {
            private int min = 0;
            private int mod = 0;

            public DieModifier(int min, int mod) {
                this.setMin(min);
                this.setMod(mod);
            }

            public int getMin() {
                return min;
            }

            public void setMin(int newMin) {
                min = newMin;
            }

            public int getMod() {
                return mod;
            }

            public int getMod(int characteristic) {
                return (characteristic >= getMin() ? getMod() : 0);
            }

            public void setMod(int newMod) {
                mod = newMod;
            }

            public String toString() {
                return getMin() + "+ (DM " + (getMod() >= 0 ? "+" : "") + getMod() + ")";
            }
        }

        private int min = 0;
        private DieModifier strDM = null;
        private DieModifier dexDM = null;
        private DieModifier endDM = null;
        private DieModifier intDM = null;
        private DieModifier eduDM = null;
        private DieModifier socDM = null;

        public SuccessNumber(int min) {
            this.setMin(min);
        }

        public int getMin() {
            return min;
        }

        public void setMin(int newMin) {
            min = newMin;
        }

        public DieModifier getStrDM() {
            return strDM;
        }

        public void setStrDM(DieModifier strDM) {
            this.strDM = strDM;
        }

        public DieModifier getDexDM() {
            return dexDM;
        }

        public void setDexDM(DieModifier dexDM) {
            this.dexDM = dexDM;
        }

        public DieModifier getEndDM() {
            return endDM;
        }

        public void setEndDM(DieModifier endDM) {
            this.endDM = endDM;
        }

        public DieModifier getIntDM() {
            return intDM;
        }

        public void setIntDM(DieModifier intDM) {
            this.intDM = intDM;
        }

        public DieModifier getEduDM() {
            return eduDM;
        }

        public void setEduDM(DieModifier eduDM) {
            this.eduDM = eduDM;
        }

        public DieModifier getSocDM() {
            return socDM;
        }

        public void setSocDM(DieModifier socDM) {
            this.socDM = socDM;
        }

        public String toString() {
            return getMin() + "+" +
                    (getStrDM() != null ? ", Str " + getStrDM() : "") +
                    (getDexDM() != null ? ", Dex " + getDexDM() : "") +
                    (getEndDM() != null ? ", End " + getEndDM() : "") +
                    (getIntDM() != null ? ", Int " + getIntDM() : "") +
                    (getEduDM() != null ? ", Edu " + getEduDM() : "") +
                    (getSocDM() != null ? ", Soc " + getSocDM() : "");
        }

        public boolean test(Character character) {
            int mods = 0 +
                    (getStrDM() != null ? getStrDM().getMod(character.getCharacteristic(Character.UPP.STR)) : 0) +
                    (getDexDM() != null ? getDexDM().getMod(character.getCharacteristic(Character.UPP.DEX)) : 0) +
                    (getEndDM() != null ? getEndDM().getMod(character.getCharacteristic(Character.UPP.END)) : 0) +
                    (getIntDM() != null ? getIntDM().getMod(character.getCharacteristic(Character.UPP.INT)) : 0) +
                    (getEduDM() != null ? getEduDM().getMod(character.getCharacteristic(Character.UPP.EDU)) : 0) +
                    (getSocDM() != null ? getSocDM().getMod(character.getCharacteristic(Character.UPP.SOC)) : 0);
            int roll = Dice.roll(2);
            int total = roll + mods;

            TCGApp.debug(
                    "rolled " + roll +
                            " w/mods " + (mods >= 0 ? "+" : "") + mods +
                            " for a total of " + total +
                            " (" + this + ")");

            return (total >= getMin());
        }
    }

    private String name = "";
    private int draftNum = 0;
    private SuccessNumber enlistment = null;
    private SuccessNumber survival = null;
    private SuccessNumber commission = null;
    private SuccessNumber promotion = null;
    private SuccessNumber reenlistment = null;
    private String withoutRank = null;
    private String rankPrefix = null;
    private String[] ranks = null;
    private String[] benefits = null;
    private int[] cashTable = null;
    private int skillEligibility = 0;
    private String[] automaticSkills = null;
    private String[] personalDevelopment = null;
    private String[] serviceSkills = null;
    private String[] advancedEducation = null;
    private String[] advancedEducationEdu8Plus = null;

    public Service(String name, int draftNum) {
        this.setName(name);
        this.setDraftNum(draftNum);
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        name = newName;
    }

    public int getDraftNum() {
        return draftNum;
    }

    public void setDraftNum(int newDraftNum) {
        draftNum = newDraftNum;
    }

    public SuccessNumber getEnlistment() {
        return enlistment;
    }

    public void setEnlistment(SuccessNumber enlistment) {
        this.enlistment = enlistment;
    }

    public SuccessNumber getSurvival() {
        return survival;
    }

    public void setSurvival(SuccessNumber survival) {
        this.survival = survival;
    }

    public SuccessNumber getCommission() {
        return commission;
    }

    public void setCommission(SuccessNumber commission) {
        this.commission = commission;
    }

    public SuccessNumber getPromotion() {
        return promotion;
    }

    public void setPromotion(SuccessNumber promotion) {
        this.promotion = promotion;
    }

    public SuccessNumber getReenlistment() {
        return reenlistment;
    }

    public void setReenlistment(SuccessNumber reenlistment) {
        this.reenlistment = reenlistment;
    }

    public String getWithoutRank() {
        return withoutRank;
    }

    public void setWithoutRank(String withoutRank) {
        this.withoutRank = withoutRank;
    }

    public String getRankPrefix() {
        return rankPrefix;
    }

    public void setRankPrefix(String rankPrefix) {
        this.rankPrefix = rankPrefix;
    }

    public String[] getRanks() {
        return ranks;
    }

    public void setRanks(String[] ranks) {
        this.ranks = ranks;
    }

    public String[] getBenefits() {
        return benefits;
    }

    public void setBenefits(String[] benefits) {
        this.benefits = benefits;
    }

    public int[] getCashTable() {
        return cashTable;
    }

    public void setCashTable(int[] cashTable) {
        this.cashTable = cashTable;
    }

    public int getSkillEligibility() {
        return skillEligibility;
    }

    public void setSkillEligibility(int skillEligibility) {
        this.skillEligibility = skillEligibility;
    }

    public String[] getAutomaticSkills() {
        return automaticSkills;
    }

    public void setAutomaticSkills(String[] automaticSkills) {
        this.automaticSkills = automaticSkills;
    }

    public String[] getPersonalDevelopment() {
        return personalDevelopment;
    }

    public void setPersonalDevelopment(String[] personalDevelopment) {
        this.personalDevelopment = personalDevelopment;
    }

    public String[] getServiceSkills() {
        return serviceSkills;
    }

    public void setServiceSkills(String[] serviceSkills) {
        this.serviceSkills = serviceSkills;
    }

    public String[] getAdvancedEducation() {
        return advancedEducation;
    }

    public void setAdvancedEducation(String[] advancedEducation) {
        this.advancedEducation = advancedEducation;
    }

    public String[] getAdvancedEducationEdu8Plus() {
        return advancedEducationEdu8Plus;
    }

    public void setAdvancedEducationEdu8Plus(String[] advancedEducationEdu8Plus) {
        this.advancedEducationEdu8Plus = advancedEducationEdu8Plus;
    }

    public String toString() {
        String rankTable = "";
        if (getRanks() != null) {
            String[] ranks = getRanks();
            for (int i = 0; i < ranks.length; i++) {
                rankTable += "\n\trank " + (i + 1) + ": " + ranks[i];
            }
        } else {
            rankTable = "\n\tno ranks";
        }

        String benefitTable = "";
        if (getBenefits() != null) {
            String[] benefits = getBenefits();
            for (int i = 0; i < benefits.length; i++) {
                benefitTable += "\n\tbenefit " + (i + 1) + ": " + benefits[i];
            }
        } else {
            benefitTable = "\n\tno benefits";
        }

        String cashTable = "";
        if (getCashTable() != null) {
            int[] cash = getCashTable();
            for (int i = 0; i < cash.length; i++) {
                cashTable += "\n\tcash " + (i + 1) + ": " + cash[i];
            }
        } else {
            cashTable = "\n\tno cash table";
        }

        String skillsTable = "";
        for (int i = 0; i <= 6; i++) {
            if (getAutomaticSkill(i) != null) {
                String rank = null;
                if (i == 0) {
                    rank = getWithoutRank();
                } else if ((i - 1) < getRanks().length) {
                    rank = getRankPrefix() + " " + getRanks()[i - 1];
                }
                skillsTable += "\n\t" + rank + ": " + getAutomaticSkill(i);
            }
        }
        if (getPersonalDevelopment() != null) {
            String[] persDev = getPersonalDevelopment();
            for (int i = 0; i < persDev.length; i++) {
                skillsTable += "\n\tpersonal development " + (i + 1) + ": " + persDev[i];
            }
        }
        if (getServiceSkills() != null) {
            String[] serviceSkills = getServiceSkills();
            for (int i = 0; i < serviceSkills.length; i++) {
                skillsTable += "\n\tservice skill " + (i + 1) + ": " + serviceSkills[i];
            }
        }
        if (getAdvancedEducation() != null) {
            String[] advEdu = getAdvancedEducation();
            for (int i = 0; i < advEdu.length; i++) {
                skillsTable += "\n\tadvanced education " + (i + 1) + ": " + advEdu[i];
            }
        }
        if (getAdvancedEducationEdu8Plus() != null) {
            String[] advEdu8Plus = getAdvancedEducationEdu8Plus();
            for (int i = 0; i < advEdu8Plus.length; i++) {
                skillsTable += "\n\tadvanced education (Edu 8+) " + (i + 1) + ": " + advEdu8Plus[i];
            }
        }

        return getName() + " (" + (getDraftNum() != 0 ? "draft on " + getDraftNum() : "no draft") + ")" +
                "\n\tenlistment on " + getEnlistment() +
                "\n\tsurvival on " + getSurvival() +
                "\n\t" + (getCommission() != null ? "commission on " + getCommission() : "no commission") +
                "\n\t" + (getPromotion() != null ? "promotion on " + getPromotion() : "no promotion") +
                "\n\treenlistment on " + getReenlistment() +
                rankTable +
                benefitTable +
                cashTable +
                "\n\tskill eligibility of " + getSkillEligibility() + " per subsequent term" +
                skillsTable;
    }

    public String getAutomaticSkill(int rankNum) {
        String autoSkillForRankNum = null;

        String[] autoSkills = getAutomaticSkills();
        if (autoSkills != null) {
            if (autoSkills.length > rankNum) {
                autoSkillForRankNum = autoSkills[rankNum];
            }
        }

        return autoSkillForRankNum;
    }
}

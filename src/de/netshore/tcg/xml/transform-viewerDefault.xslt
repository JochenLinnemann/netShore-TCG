<?xml version="1.0" encoding="UTF-8"?>
<!-- DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2 Final//EN" "url:unknown" -->
<!-- DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd" -->
<!--
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
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" version="3.2" encoding="UTF-8"/>
    <xsl:variable name="TAS">Travellers' Aid</xsl:variable>

    <xsl:template match="/">
        <html>
            <head>
                <title>netShore TCG character sheet</title>
                <meta name="generator" content="netShore(R) TCG v0.9"/>
                <style type="text/css">
                    body {
                    text-align: center;
                    font-family: Arial,Helvetica,sans-serif;
                    }
                    table {
                    border-width: 1px;
                    border-style: solid;
                    border-color: #000000;
                    }
                    td {
                    border-width: 1px;
                    border-style: solid;
                    border-color: #000000;
                    font-size: 9px;
                    height: 28px;
                    }
                    small {
                    font-size: 9px;
                    }
                    big {
                    font-size: 13px;
                    font-weight: bold;
                    }
                    div {
                    color: #000099;
                    padding-left: 11px;
                    font-size: 11px;
                    font-weight: bold;
                    }
                    tr.head {
                    border-width: 1px;
                    border-style: solid;
                    border-color: #000000;
                    }
                    td.head-left {
                    border-top-width: 1px;
                    border-right-width: 0px;
                    border-bottom-width: 1px;
                    border-left-width: 1px;
                    border-width: 0px;
                    }
                    td.head-right {
                    border-top-width: 1px;
                    border-right-width: 1px;
                    border-bottom-width: 1px;
                    border-left-width: 0px;
                    border-width: 0px;
                    }
                    td.multiline {
                    height: 70px;
                    }
                    td.extramultiline {
                    height: 140px;
                    }
                    table.foot {
                    border-width: 0px;
                    }
                    td.foot-left {
                    border-width: 0px;
                    text-align: left;
                    font-size: 11px;
                    font-weight: bold;
                    }
                    td.foot-right {
                    border-width: 0px;
                    text-align: right;
                    font-size: 11px;
                    font-weight: bold;
                    }
                    td.copy {
                    border-width: 0px;
                    text-align: center;
                    }
                </style>
            </head>
            <body>
                <br/>
                <table cellpadding="2" cellspacing="0" valign="top" width="600">
                    <!-- colgroup>
                        <col width="25%"/>
                        <col width="25%"/>
                        <col width="25%"/>
                        <col width="25%"/>
                    </colgroup -->
                    <tr>
                        <td colspan="3">
                            <big>PERSONAL DATA AND HISTORY</big>
                        </td>
                        <td colspan="1">
                            <small>1. Date of Preparation</small>
                            <div>
                                <xsl:value-of select="character/dateOfPreparation"/>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="3">
                            <small>2. Name</small>
                            <div>
                                <xsl:value-of select="character/name"/>
                            </div>
                        </td>
                        <td colspan="1">
                            <small>3. UPP</small>
                            <div>
                                <xsl:call-template name="uppDigit">
                                    <xsl:with-param name="value" select="character/upp/str/@val"/>
                                </xsl:call-template>
                                <xsl:call-template name="uppDigit">
                                    <xsl:with-param name="value" select="character/upp/dex/@val"/>
                                </xsl:call-template>
                                <xsl:call-template name="uppDigit">
                                    <xsl:with-param name="value" select="character/upp/end/@val"/>
                                </xsl:call-template>
                                <xsl:call-template name="uppDigit">
                                    <xsl:with-param name="value" select="character/upp/int/@val"/>
                                </xsl:call-template>
                                <xsl:call-template name="uppDigit">
                                    <xsl:with-param name="value" select="character/upp/edu/@val"/>
                                </xsl:call-template>
                                <xsl:call-template name="uppDigit">
                                    <xsl:with-param name="value" select="character/upp/soc/@val"/>
                                </xsl:call-template>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="1">
                            <small>4. Noble Title</small>
                            <div>
                                <xsl:value-of select="character/nobleTitle"/>
                            </div>
                        </td>
                        <td colspan="1">
                            <small>5. Military Rank</small>
                            <div>
                                <xsl:value-of select="character/militaryRank"/>
                            </div>
                        </td>
                        <td colspan="2">
                            <small>6. Birthdate</small>
                            <div>
                                <xsl:value-of select="character/birthdate"/>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <small>7. Age Modifiers
                                <i>(+ for drugs; - for sleep)</i>
                            </small>
                            <div>
                                <xsl:value-of select="character/ageModifiers"/>
                            </div>
                        </td>
                        <td colspan="2">
                            <small>8. Birthworld</small>
                            <div>
                                <xsl:value-of select="character/birthworld"/>
                            </div>
                        </td>
                    </tr>
                    <tr class="head">
                        <td class="head-left" colspan="2">
                            <big>SERVICE HISTORY</big>
                        </td>
                        <td class="head-right" colspan="2">Personal service data produced from appropriate historical
                            and personal records.
                        </td>
                    </tr>
                    <tr>
                        <td colspan="1">
                            <small>9. Service</small>
                            <div>
                                <xsl:value-of select="character/service"/>
                            </div>
                        </td>
                        <td colspan="1">
                            <small>10. Branch</small>
                            <div>
                                <xsl:value-of select="character/branch"/>
                            </div>
                        </td>
                        <td colspan="2">
                            <small>11. Dischargeworld</small>
                            <div>
                                <xsl:value-of select="character/dischargeworld"/>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="1" width="25%">
                            <small>12. Terms Served</small>
                            <div>
                                <xsl:value-of select="character/termsServed"/>
                            </div>
                        </td>
                        <td colspan="1" width="25%">
                            <small>13. Final Rank</small>
                            <div>
                                <xsl:value-of select="character/finalRank"/>
                            </div>
                        </td>
                        <td colspan="1" width="25%">
                            <small>14a. Retired?</small>
                            <div>
                                <xsl:choose>
                                    <xsl:when test="character/termsServed &gt; 4">Yes</xsl:when>
                                    <xsl:otherwise>No</xsl:otherwise>
                                </xsl:choose>
                            </div>
                        </td>
                        <td colspan="1" width="25%">
                            <small>14b. Retirement Pay</small>
                            <div>
                                <xsl:if test="character/termsServed &gt; 4">Cr<xsl:value-of
                                        select="format-number(((character/termsServed - 3) * 2000), '#,##0')"/>
                                </xsl:if>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td class="multiline" colspan="4">
                            <small>15. Special Assignments</small>
                            <div>
                                <xsl:value-of select="character/specialAssignments"/>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td class="multiline" colspan="4">
                            <small>16. Awards and Decorations
                                <i>(include Combat Commands, Commendations, Medals, etc.)</i>
                            </small>
                            <div>
                                <xsl:value-of select="character/awardsAndDecorations"/>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td class="multiline" colspan="4">
                            <small>17. Equipment Qualified On</small>
                            <div>
                                <xsl:value-of select="character/equipmentQualifiedOn"/>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <small>18a. Primary Skill</small>
                            <div></div>
                        </td>
                        <td colspan="2">
                            <small>18b. Secondary Skill</small>
                            <div></div>
                        </td>
                    </tr>
                    <tr>
                        <td class="multiline" colspan="4">
                            <small>18c. Additional Skills</small>
                            <div>
                                <xsl:for-each select="character/skills/skill">
                                    <xsl:value-of select="./@name"/>-<xsl:value-of select="./@level"/>
                                    <xsl:if test="position() != last()">,</xsl:if>
                                </xsl:for-each>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="1">
                            <small>19a. Preferred Weapon</small>
                            <div>
                                <xsl:value-of select="character/preferredWeapon"/>
                            </div>
                        </td>
                        <td colspan="1">
                            <small>19b. Preferred Pistol</small>
                            <div></div>
                        </td>
                        <td colspan="1">
                            <small>19c. Preferred Blade</small>
                            <div></div>
                        </td>
                        <td colspan="1">
                            <small>20. TAS Member?</small>
                            <div>
                                <xsl:choose>
                                    <xsl:when test="character/personalPossessions/item[@name = $TAS]/@count &gt; 0">
                                        Yes
                                    </xsl:when>
                                    <xsl:otherwise>No</xsl:otherwise>
                                </xsl:choose>
                            </div>
                        </td>
                    </tr>
                    <tr class="head">
                        <td class="head-left" colspan="1">
                            <big>PSIONICS</big>
                        </td>
                        <td class="head-right" colspan="3">
                            <i>Warning:</i>
                            Information regarding an individual's psionic ability is confidential, and may not be
                            released without consent.
                        </td>
                    </tr>
                    <tr>
                        <td colspan="1">
                            <small>21. Date of Test</small>
                            <div></div>
                        </td>
                        <td colspan="1">
                            <small>22. PSR</small>
                            <div></div>
                        </td>
                        <td colspan="1">
                            <small>23a. Trained?</small>
                            <div></div>
                        </td>
                        <td colspan="1">
                            <small>23b. Date Completed</small>
                            <div></div>
                        </td>
                    </tr>
                    <tr>
                        <td class="multiline" colspan="4">
                            <small>24. Talents and Current Levels</small>
                            <div></div>
                        </td>
                    </tr>
                </table>
                <table class="foot" cellpadding="2" cellspacing="0" valign="top" width="600">
                    <tr>
                        <td class="foot-left">TAS Form 2</td>
                        <td class="foot-right">Personal Data And History</td>
                    </tr>
                </table>

                <br/>

                <table cellpadding="2" cellspacing="0" valign="top" width="600">
                    <!-- colgroup>
                        <col width="25%"/>
                        <col width="25%"/>
                        <col width="25%"/>
                        <col width="25%"/>
                    </colgroup -->
                    <tr>
                        <td colspan="4">
                            <small>25. Name</small>
                            <div>
                                <xsl:value-of select="character/name"/>
                            </div>
                        </td>
                    </tr>
                    <tr class="head">
                        <td class="head-left" colspan="2" width="50%">
                            <big>CASH ACCOUNTS</big>
                        </td>
                        <td class="head-right" colspan="2" width="50%">Data concerning the individual's financial
                            affairs and cash availability.
                        </td>
                    </tr>
                    <tr>
                        <td class="extramultiline" colspan="4">
                            <small>26. Cash Status
                                <i>(note cash available to the individual)</i>
                            </small>
                            <div>
                                <xsl:if test="character/cashStatus/@val &gt; 0">Cr<xsl:value-of
                                        select="format-number((character/cashStatus/@val), '#,##0')"/>
                                </xsl:if>
                            </div>
                        </td>
                    </tr>
                    <tr class="head">
                        <td class="head-left" colspan="2">
                            <big>INVENTORY</big>
                        </td>
                        <td class="head-right" colspan="2">Information on personal inventory and possessions, including
                            vehicles.
                        </td>
                    </tr>
                    <tr>
                        <td class="extramultiline" colspan="4">
                            <small>27. Personal Possessions</small>
                            <div>
                                <xsl:for-each select="character/personalPossessions/item">
                                    <xsl:value-of select="./@name"/>
                                    <xsl:if test="./@count &gt; 1">(<xsl:value-of select="./@count"/>)
                                    </xsl:if>
                                    <xsl:if test="position() != last()">,</xsl:if>
                                </xsl:for-each>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td class="extramultiline" colspan="4">
                            <small>28. Vehicles</small>
                            <div></div>
                        </td>
                    </tr>
                </table>
                <table class="foot" cellpadding="2" cellspacing="0" valign="top" width="600">
                    <tr>
                        <td class="foot-left">TAS Form 2 (Reverse)</td>
                        <td class="foot-right">Personal Data And History</td>
                    </tr>
                </table>

                <table class="foot" cellpadding="2" cellspacing="0" valign="top" width="600">
                    <tr>
                        <td class="copy" colspan="2">The Traveller game in all forms is owned by Far Future Enterprises.
                            <br/>Copyright 1977 - 2004 Far Future Enterprises.
                        </td>
                    </tr>
                </table>
            </body>
        </html>
    </xsl:template>

    <xsl:template name="uppDigit">
        <xsl:param name="value"/>
        <xsl:choose>
            <xsl:when test="$value = 10">A</xsl:when>
            <xsl:when test="$value = 11">B</xsl:when>
            <xsl:when test="$value = 12">C</xsl:when>
            <xsl:when test="$value = 13">D</xsl:when>
            <xsl:when test="$value = 14">E</xsl:when>
            <xsl:when test="$value = 15">F</xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$value"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
</xsl:stylesheet>

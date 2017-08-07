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
 * Created on 11.05.2005
 */
package de.netshore.tcg;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import de.netshore.tcg.CharacterFactory.GenerationListener;

/**
 * @author jlin
 */
public class CharacterCreationDialog extends CharacterDialog implements GenerationListener {
    private String originalTitle = "";

    private Vector serviceHistory = new Vector();
    private JList serviceHistoryDisplay = new JList();
    private JScrollPane historyScroller = new JScrollPane(serviceHistoryDisplay);

    public CharacterCreationDialog(JFrame owner) {
        super(owner, false);
        originalTitle = getTitle();
        setTitle("Interactive Character Creation");

        initComponents();
    }

    private void initComponents() {
        Container originalContentPane = getContentPane();

        JPanel newContentPane = new JPanel();
        newContentPane.setLayout(new BorderLayout());
        setContentPane(newContentPane);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BorderLayout());
        listPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        //listPanel.setPreferredSize(new Dimension(200, 300));
        listPanel.add(historyScroller, BorderLayout.CENTER);
        serviceHistoryDisplay.setVisibleRowCount(12);
        serviceHistoryDisplay.setPrototypeCellValue("netShore TCG is about to generate a new character.");

        newContentPane.add(listPanel, BorderLayout.WEST);
        newContentPane.add(originalContentPane, BorderLayout.CENTER);
    }

    private void showDialog(boolean modal) {
        setModal(modal);
        pack();
        setLocationRelativeTo(getOwner());
        show();
    }

    /*
     * Implemenation of interface CharacterFactory.GenerationListener
     */

    public void startGeneration(Character character) {
        setCharacter(character);
        showDialog(false);
    }

    public void infoGeneration(int event, String message) {
        switch (event) {
            default:
                serviceHistory.add(message);
                serviceHistoryDisplay.setListData(serviceHistory);
                serviceHistoryDisplay.ensureIndexIsVisible(serviceHistory.size() - 1);
                updateView();
                pack();
                break;
        }
    }

    public void endGeneration() {
        dispose();
        showDialog(true);
    }

    public int chooseService(Service[] services) {
        Object[] options = new Object[services.length];

        for (int i = 0; i < options.length; i++) {
            options[i] = services[i].getName();
        }

        Object selection = JOptionPane.showInputDialog(
                this,
                "Choose the Service the character should apply for.",
                "Choose Service",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                null
        );
        for (int i = 0; i < options.length; i++) {
            if (options[i].equals(selection)) {
                return i;
            }
        }
        return -1;
    }

    public int chooseSkillTable(String[] tableNames, String[][] skillTables) {
        showSkills();

        Object[] options = new Object[tableNames.length];

        for (int i = 0; i < options.length; i++) {
            options[i] = tableNames[i];
        }

        Object selection = JOptionPane.showInputDialog(
                this,
                "The character will receive a Skill." +
                        "\nWhich Skill Table should be rolled on?",
                "Choose Skill Table",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                null
        );
        for (int i = 0; i < options.length; i++) {
            if (options[i].equals(selection)) {
                return i;
            }
        }
        return -1;
    }

    public boolean chooseReenlistment(int currentTerm) {
        return JOptionPane.showOptionDialog(
                this,
                "The character has served " + currentTerm + " term" + (currentTerm == 1 ? "" : "s") + " now." +
                        "\nTry to apply for Reenlistment?",
                "Choose whether to Reenlist",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                null
        ) == JOptionPane.YES_OPTION;
    }

    public int chooseBenefitsOrCash(String[] benefits, int[] cashTable) {
        showPossessions();

        Object[] options = new Object[2];

        options[0] = "Benefits";
        options[1] = "Cash";

        Object selection = JOptionPane.showInputDialog(
                this,
                "The character will receive a Mustering Out Benefit." +
                        "\nWhich Mustering Out Table should be rolled on?",
                "Choose Mustering Out Table",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                null
        );
        for (int i = 0; i < options.length; i++) {
            if (options[i].equals(selection)) {
                return i;
            }
        }
        return -1;
    }
}

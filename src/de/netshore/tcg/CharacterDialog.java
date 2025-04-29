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
 * Created on 15.05.2005
 */
package de.netshore.tcg;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.text.NumberFormat;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import de.netshore.tcg.Character.PossessionIterator;
import de.netshore.tcg.Character.SkillIterator;

/**
 * @author jlin
 */
public class CharacterDialog extends JDialog {
    private Character character = null;
    private JLabel label01 = new JLabel("1. Date of Preparation");
    private JLabel label02 = new JLabel("2. Name");
    private JLabel label03 = new JLabel("3. UPP");
    private JLabel label04 = new JLabel("4. Noble Title");
    private JLabel label05 = new JLabel("5. Military Rank");
    private JLabel label06 = new JLabel("6. Birthdate");
    private JLabel label07 = new JLabel("7. Age Modifiers");
    private JLabel label08 = new JLabel("8. Birthworld");
    private JLabel label09 = new JLabel("9. Service");
    private JLabel label10 = new JLabel("10. Branch");
    private JLabel label11 = new JLabel("11. Dischargeworld");
    private JLabel label12 = new JLabel("12. Terms Served");
    private JLabel label13 = new JLabel("13. Final Rank");
    private JLabel label14 = new JLabel("14. Retirement Pay");
    private JLabel label15 = new JLabel("15. Special Assignments");
    private JLabel label16 = new JLabel("16. Awards and Decorations");
    private JLabel label17 = new JLabel("17. Equipment Qualified On");
    private JLabel label18 = new JLabel("18. Skills");
    private JLabel label19 = new JLabel("19. Preferred Weapon");
    private JLabel label26 = new JLabel("26. Cash Status");
    private JLabel label27 = new JLabel("27. Personal Possessions");
    private JTextField field01 = new JTextField();
    private JTextField field02 = new JTextField();
    private JTextField field03 = new JTextField();
    private JTextField field04 = new JTextField();
    private JTextField field05 = new JTextField();
    private JTextField field06 = new JTextField();
    private JTextField field07 = new JTextField();
    private JTextField field08 = new JTextField();
    private JTextField field09 = new JTextField();
    private JTextField field10 = new JTextField();
    private JTextField field11 = new JTextField();
    private JTextField field12 = new JTextField();
    private JTextField field13 = new JTextField();
    private JTextField field14 = new JTextField();
    private JTextArea txtArea15 = new JTextArea();
    private JTextArea txtArea16 = new JTextArea();
    private JTextArea txtArea17 = new JTextArea();
    private JList<String> listedField18 = new JList<>();
    private JTextField field19 = new JTextField();
    private JTextField field26 = new JTextField();
    private JList<String> listedField27 = new JList<>();
    private JTabbedPane tabPane = new JTabbedPane();

    public CharacterDialog(Frame owner, boolean modal) {
        super(owner, "Edit Character", modal);
        initComponents();
    }

    private void initComponents() {
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new GridBagLayout());
        contentPane.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        setContentPane(contentPane);

        addCombination(label01, field01, 3, 0, 1, 1);
        addCombination(label02, field02, 0, 1, 3, 1);
        addCombination(label03, field03, 3, 1, 1, 1);
        addCombination(label04, field04, 0, 2, 1, 1);
        addCombination(label05, field05, 1, 2, 1, 1);
        addCombination(label06, field06, 2, 2, 2, 1);
        addCombination(label07, field07, 0, 3, 2, 1);
        addCombination(label08, field08, 2, 3, 2, 1);
        addCombination(label09, field09, 0, 4, 1, 1);
        addCombination(label10, field10, 1, 4, 1, 1);
        addCombination(label11, field11, 2, 4, 2, 1);
        addCombination(label12, field12, 0, 5, 1, 1);
        addCombination(label13, field13, 1, 5, 1, 1);
        addCombination(label14, field14, 2, 5, 1, 1);

        addCombination(label19, field19, 0, 6, 1, 1);
        addCombination(label26, field26, 2, 6, 1, 1);

        contentPane.add(tabPane, new GridBagConstraints(0, 7, 4, 1,
                0.5, 1.0, GridBagConstraints.LINE_START, GridBagConstraints.BOTH,
                new Insets(2, 2, 2, 2), 0, 0));

        txtArea15.setRows(4);
        txtArea16.setRows(4);
        txtArea17.setRows(4);
        listedField18.setVisibleRowCount(8);
        listedField27.setVisibleRowCount(8);
        tabPane.addTab(label15.getText(), createScrollPane(txtArea15));
        tabPane.addTab(label16.getText(), createScrollPane(txtArea16));
        tabPane.addTab(label17.getText(), createScrollPane(txtArea17));
        tabPane.addTab(label18.getText(), createScrollPane(listedField18));
        tabPane.addTab(label27.getText(), createScrollPane(listedField27));

        contentPane.add(new JButton(new AbstractAction("OK") {
            public void actionPerformed(ActionEvent e) {
                // character.setDateOfPreparation(field_01.getText());
                character.setName(field02.getText());
                character.setNobleTitle(field04.getText());
                character.setMilitaryRank(field05.getText());
                character.setBirthdate(field06.getText());
                character.setAgeModifiers(field07.getText());
                character.setBirthworld(field08.getText());
                character.setBranch(field10.getText());
                character.setDischargeworld(field11.getText());
                character.setSpecialAssignments(txtArea15.getText());
                character.setAwardsAndDecorations(txtArea16.getText());
                character.setEquipmentQualifiedOn(txtArea17.getText());
                // txtArea18.setText("skills");
                character.setPreferredWeapon(field19.getText());
                // txtArea27.setText("possessions");
                dispose();
            }
        }), new GridBagConstraints(3, 8, 1, 1,
                0.5, 0, GridBagConstraints.LINE_END, GridBagConstraints.NONE,
                new Insets(2, 2, 2, 2), 0, 0));
    }

    private void addCombination(JLabel label, JComponent field, int col, int row, int colspan, int rowspan) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = col;
        c.gridy = row;
        c.gridwidth = colspan;
        c.gridheight = rowspan;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(2, 2, 2, 2);
        c.anchor = GridBagConstraints.LINE_START;
        c.weightx = 0.5;
        c.weighty = 0.0;

        JPanel combinationPanel = new JPanel();
        combinationPanel.setLayout(new BoxLayout(combinationPanel, BoxLayout.PAGE_AXIS));
        getContentPane().add(combinationPanel, c);

        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(field.getMaximumSize().width, field.getPreferredSize().height));

        combinationPanel.add(label);
        combinationPanel.add(field);
    }

    private JScrollPane createScrollPane(JComponent component) {
        return new JScrollPane(component);
    }

    protected void updateView() {
        field01.setText(character.getDateOfPreparation());
        field01.setEditable(false);
        field02.setText(character.getName());
        field03.setText(character.uppAsString());
        field03.setEditable(false);
        field04.setText(character.getNobleTitle());
        field05.setText(character.getMilitaryRank());
        field06.setText(character.getBirthdate());
        field07.setText(character.getAgeModifiers());
        field08.setText(character.getBirthworld());
        field09.setText(character.getService());
        field09.setEditable(false);
        field10.setText(character.getBranch());
        field11.setText(character.getDischargeworld());
        field12.setText(Integer.toString(character.getTermsServed()));
        field12.setEditable(false);
        field13.setText((character.getRankNum() > 0 ? character.getFinalRank() : ""));
        field13.setEditable(false);
        field14.setText((character.getRetirementPay() > 0
                ? "Cr" + NumberFormat.getIntegerInstance().format(character.getRetirementPay())
                : "not retired"));
        field14.setEditable(false);
        txtArea15.setText(character.getSpecialAssignments());
        txtArea16.setText(character.getAwardsAndDecorations());
        txtArea17.setText(character.getEquipmentQualifiedOn());
        Vector<String> skills = new Vector<>();
        SkillIterator skillIter = character.skillIterator();
        while (skillIter.hasNext()) {
            skillIter.next();
            skills.add(skillIter.getName() + "-" + skillIter.getLevel());
        }
        listedField18.setListData(skills);
        field19.setText(character.getPreferredWeapon());
        field26.setText("Cr" + NumberFormat.getIntegerInstance().format(character.getCash()));
        field26.setEditable(false);
        Vector<String> possessions = new Vector<>();
        PossessionIterator possIter = character.possessionIterator();
        while (possIter.hasNext()) {
            possIter.next();
            possessions.add(possIter.getNameOfItem()
                    + (possIter.getNumberOfItems() > 1 ? " (" + possIter.getNumberOfItems() + ")" : ""));
        }
        listedField27.setListData(possessions);
    }

    protected void showSkills() {
        tabPane.setSelectedIndex(3);
    }

    protected void showPossessions() {
        tabPane.setSelectedIndex(4);
    }

    public void setCharacter(Character character) {
        this.character = character;
        updateView();
    }

    public Character getCharacter() {
        return character;
    }
}

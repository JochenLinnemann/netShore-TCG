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
    private JLabel label_01 = new JLabel("1. Date of Preparation");
    private JLabel label_02 = new JLabel("2. Name");
    private JLabel label_03 = new JLabel("3. UPP");
    private JLabel label_04 = new JLabel("4. Noble Title");
    private JLabel label_05 = new JLabel("5. Military Rank");
    private JLabel label_06 = new JLabel("6. Birthdate");
    private JLabel label_07 = new JLabel("7. Age Modifiers");
    private JLabel label_08 = new JLabel("8. Birthworld");
    private JLabel label_09 = new JLabel("9. Service");
    private JLabel label_10 = new JLabel("10. Branch");
    private JLabel label_11 = new JLabel("11. Dischargeworld");
    private JLabel label_12 = new JLabel("12. Terms Served");
    private JLabel label_13 = new JLabel("13. Final Rank");
    private JLabel label_14 = new JLabel("14. Retirement Pay");
    private JLabel label_15 = new JLabel("15. Special Assignments");
    private JLabel label_16 = new JLabel("16. Awards and Decorations");
    private JLabel label_17 = new JLabel("17. Equipment Qualified On");
    private JLabel label_18 = new JLabel("18. Skills");
    private JLabel label_19 = new JLabel("19. Preferred Weapon");
    private JLabel label_26 = new JLabel("26. Cash Status");
    private JLabel label_27 = new JLabel("27. Personal Possessions");
    private JTextField field_01 = new JTextField();
    private JTextField field_02 = new JTextField();
    private JTextField field_03 = new JTextField();
    private JTextField field_04 = new JTextField();
    private JTextField field_05 = new JTextField();
    private JTextField field_06 = new JTextField();
    private JTextField field_07 = new JTextField();
    private JTextField field_08 = new JTextField();
    private JTextField field_09 = new JTextField();
    private JTextField field_10 = new JTextField();
    private JTextField field_11 = new JTextField();
    private JTextField field_12 = new JTextField();
    private JTextField field_13 = new JTextField();
    private JTextField field_14 = new JTextField();
    private JTextArea txtArea15 = new JTextArea();
    private JTextArea txtArea16 = new JTextArea();
    private JTextArea txtArea17 = new JTextArea();
    private JList listedField18 = new JList();
    private JTextField field_19 = new JTextField();
    private JTextField field_26 = new JTextField();
    private JList listedField27 = new JList();
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

        addCombination(label_01, field_01, 3, 0, 1, 1);
        addCombination(label_02, field_02, 0, 1, 3, 1);
        addCombination(label_03, field_03, 3, 1, 1, 1);
        addCombination(label_04, field_04, 0, 2, 1, 1);
        addCombination(label_05, field_05, 1, 2, 1, 1);
        addCombination(label_06, field_06, 2, 2, 2, 1);
        addCombination(label_07, field_07, 0, 3, 2, 1);
        addCombination(label_08, field_08, 2, 3, 2, 1);
        addCombination(label_09, field_09, 0, 4, 1, 1);
        addCombination(label_10, field_10, 1, 4, 1, 1);
        addCombination(label_11, field_11, 2, 4, 2, 1);
        addCombination(label_12, field_12, 0, 5, 1, 1);
        addCombination(label_13, field_13, 1, 5, 1, 1);
        addCombination(label_14, field_14, 2, 5, 1, 1);

        addCombination(label_19, field_19, 0, 6, 1, 1);
        addCombination(label_26, field_26, 2, 6, 1, 1);

        contentPane.add(tabPane, new GridBagConstraints(0, 7, 4, 1,
                0.5, 1.0, GridBagConstraints.LINE_START, GridBagConstraints.BOTH,
                new Insets(2, 2, 2, 2), 0, 0)
        );

        txtArea15.setRows(4);
        txtArea16.setRows(4);
        txtArea17.setRows(4);
        listedField18.setVisibleRowCount(8);
        listedField27.setVisibleRowCount(8);
        tabPane.addTab(label_15.getText(), createScrollPane(txtArea15));
        tabPane.addTab(label_16.getText(), createScrollPane(txtArea16));
        tabPane.addTab(label_17.getText(), createScrollPane(txtArea17));
        tabPane.addTab(label_18.getText(), createScrollPane(listedField18));
        tabPane.addTab(label_27.getText(), createScrollPane(listedField27));

        contentPane.add(new JButton(new AbstractAction("OK") {
                    public void actionPerformed(ActionEvent e) {
                        //character.setDateOfPreparation(field_01.getText());
                        character.setName(field_02.getText());
                        character.setNobleTitle(field_04.getText());
                        character.setMilitaryRank(field_05.getText());
                        character.setBirthdate(field_06.getText());
                        character.setAgeModifiers(field_07.getText());
                        character.setBirthworld(field_08.getText());
                        character.setBranch(field_10.getText());
                        character.setDischargeworld(field_11.getText());
                        character.setSpecialAssignments(txtArea15.getText());
                        character.setAwardsAndDecorations(txtArea16.getText());
                        character.setEquipmentQualifiedOn(txtArea17.getText());
                        //txtArea18.setText("skills");
                        character.setPreferredWeapon(field_19.getText());
                        //txtArea27.setText("possessions");
                        dispose();
                    }
                }), new GridBagConstraints(3, 8, 1, 1,
                        0.5, 0, GridBagConstraints.LINE_END, GridBagConstraints.NONE,
                        new Insets(2, 2, 2, 2), 0, 0)
        );
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
        JScrollPane scrollPane = new JScrollPane(component);
        return scrollPane;
    }

    protected void updateView() {
        field_01.setText(character.getDateOfPreparation());
        field_01.setEditable(false);
        field_02.setText(character.getName());
        field_03.setText(character.uppAsString());
        field_03.setEditable(false);
        field_04.setText(character.getNobleTitle());
        field_05.setText(character.getMilitaryRank());
        field_06.setText(character.getBirthdate());
        field_07.setText(character.getAgeModifiers());
        field_08.setText(character.getBirthworld());
        field_09.setText(character.getService());
        field_09.setEditable(false);
        field_10.setText(character.getBranch());
        field_11.setText(character.getDischargeworld());
        field_12.setText(new Integer(character.getTermsServed()).toString());
        field_12.setEditable(false);
        field_13.setText((character.getRankNum() > 0 ? character.getFinalRank() : ""));
        field_13.setEditable(false);
        field_14.setText((character.getRetirementPay() > 0 ? "Cr" + NumberFormat.getIntegerInstance().format(new Integer(character.getRetirementPay())) : "not retired"));
        field_14.setEditable(false);
        txtArea15.setText(character.getSpecialAssignments());
        txtArea16.setText(character.getAwardsAndDecorations());
        txtArea17.setText(character.getEquipmentQualifiedOn());
        Vector skills = new Vector();
        SkillIterator skillIter = character.skillIterator();
        while (skillIter.hasNext()) {
            skillIter.next();
            skills.add(skillIter.getName() + "-" + skillIter.getLevel());
        }
        listedField18.setListData(skills);
        field_19.setText(character.getPreferredWeapon());
        field_26.setText("Cr" + NumberFormat.getIntegerInstance().format(new Integer(character.getCash())));
        field_26.setEditable(false);
        Vector possessions = new Vector();
        PossessionIterator possIter = character.possessionIterator();
        while (possIter.hasNext()) {
            possIter.next();
            possessions.add(possIter.getNameOfItem() + (possIter.getNumberOfItems() > 1 ? " (" + possIter.getNumberOfItems() + ")" : ""));
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

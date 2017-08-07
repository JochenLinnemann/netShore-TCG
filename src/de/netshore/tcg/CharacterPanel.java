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
 * Created on 05.05.2005
 */
package de.netshore.tcg;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.text.MessageFormat;

import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.xml.transform.Templates;

import de.netshore.tcg.Character.PossessionIterator;
import de.netshore.tcg.Character.SkillIterator;
import de.netshore.tcg.xml.CharacterSerializer;
import de.netshore.tcg.xml.CharacterTransformer;

/**
 * @author jlin
 */
public class CharacterPanel extends JPanel {
    public static final FileFilter tcdFilter = new FileFilter() {
        public boolean accept(File f) {
            return f.isDirectory() || f.getName().toLowerCase().endsWith(".tcd");
        }

        public String getDescription() {
            return "Traveller Character Data (*.tcd)";
        }
    };
    public static final FileFilter xmlFilter = new FileFilter() {
        public boolean accept(File f) {
            return f.isDirectory() || f.getName().toLowerCase().endsWith(".xml");
        }

        public String getDescription() {
            return "XML Traveller Character Data (*.xml)";
        }
    };
    public static final FileFilter htmlFilter = new FileFilter() {
        public boolean accept(File f) {
            return f.isDirectory() || f.getName().toLowerCase().endsWith(".html");
        }

        public String getDescription() {
            return "HTML Character Export (*.html)";
        }
    };

    private File file = null;
    private Character character = null;
    private boolean modified = false;
    private String initialStyle = "";
    private MessageFormat data = null;
    private JEditorPane viewer = new JEditorPane();
    private JScrollPane scroll = new JScrollPane(viewer);

    public CharacterPanel(File file, Character character) {
        this.file = file;

        String initialData = "";
        try {
            char[] buffer = new char[1024];
            int charCount = 0;
            Reader reader = null;

            reader = new InputStreamReader(getClass().getResourceAsStream("template.css"));
            do {
                charCount = reader.read(buffer);
                if (charCount > 0) {
                    initialStyle += String.copyValueOf(buffer, 0, charCount);
                }
            } while (charCount > -1);
            reader = new InputStreamReader(getClass().getResourceAsStream("template.html"));
            do {
                charCount = reader.read(buffer);
                if (charCount > 0) {
                    initialData += String.copyValueOf(buffer, 0, charCount);
                }
            } while (charCount > -1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        data = new MessageFormat(initialData);
        viewer.setEditable(false);
        viewer.setContentType("text/html");
        viewer.setText("");
        setLayout(new BorderLayout());
        add(scroll, BorderLayout.CENTER);

        setCharacter(character);
    }

    public void setCharacter(Character character) {
        this.character = character;
        updateViewer();
    }

    public Character getCharacter() {
        return character;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public boolean isModified() {
        return modified;
    }

    private void internalSave() {
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            if (tcdFilter.accept(file)) {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
                oos.writeObject(getCharacter());
                oos.close();
                setModified(false);
            } else if (xmlFilter.accept(file)) {
                CharacterSerializer.write(getCharacter(), file);
                setModified(false);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        if (file == null) {
            saveAs();
        } else {
            internalSave();
        }
    }

    public void saveAs() {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(tcdFilter);
        fileChooser.addChoosableFileFilter(xmlFilter);
        fileChooser.setFileFilter(xmlFilter);
        fileChooser.setSelectedFile(new File(getCharacter().getName()));
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
            if (file != null) {
                String fileExtension = ".tcd";
                if (fileChooser.getFileFilter() == xmlFilter) {
                    fileExtension = ".xml";
                }
                if (!file.getName().toLowerCase().endsWith(fileExtension)) {
                    file = new File(file + fileExtension);
                }
            }
            internalSave();
        }
    }

    public void export() {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(htmlFilter);
        fileChooser.setFileFilter(htmlFilter);
        fileChooser.setSelectedFile(new File(getCharacter().getName()));
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File exportFile = fileChooser.getSelectedFile();
            if (exportFile != null && !exportFile.getName().toLowerCase().endsWith(".html")) {
                exportFile = new File(exportFile + ".html");
            }
            try {
                if (exportFile.exists()) {
                    exportFile.delete();
                }
                exportFile.createNewFile();
                /*
	            FileWriter writer = new FileWriter(exportFile);
	            writer.write(getFormattedData());
	            writer.close();
	            */
                FileOutputStream fos = new FileOutputStream(exportFile);
                CharacterTransformer.transform(getCharacter(), fos, CharacterTransformer.DEFAULT_EXPORT);
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String sanitize(String text) {
        String sane = text;

        sane = sane.replaceAll("&", "&amp;");
        sane = sane.replaceAll("<", "&lt;");
        sane = sane.replaceAll(">", "&gt;");
        sane = sane.replaceAll("\n", "<br>");

        return sane;
    }

    private String getFormattedData() {
        Object[] arguments = new Object[28];

        arguments[0] = initialStyle;
        arguments[1] = character.getDateOfPreparation();
        arguments[2] = sanitize(character.getName());
        arguments[3] = sanitize(character.uppAsString());
        arguments[4] = sanitize(character.getNobleTitle());
        arguments[5] = sanitize(character.getMilitaryRank());
        arguments[6] = character.getBirthdate();
        arguments[7] = sanitize(character.getAgeModifiers());
        arguments[8] = sanitize(character.getBirthworld());
        arguments[9] = sanitize(character.getService());
        arguments[10] = sanitize(character.getBranch());
        arguments[11] = sanitize(character.getDischargeworld());
        arguments[12] = new Integer(character.getTermsServed());
        arguments[13] = sanitize((character.getRankNum() > 0 ? character.getFinalRank() : ""));
        arguments[14] = new Integer(character.getRetirementPay());
        arguments[15] = sanitize(character.getSpecialAssignments());
        arguments[16] = sanitize(character.getAwardsAndDecorations());
        arguments[17] = sanitize(character.getEquipmentQualifiedOn());
        String skills = "";
        SkillIterator skillIter = character.skillIterator();
        while (skillIter.hasNext()) {
            skillIter.next();
            if (!"".equals(skills)) {
                skills += ", ";
            }
            skills += skillIter.getName() + "-" + skillIter.getLevel();
        }
        arguments[18] = sanitize(skills);
        arguments[19] = sanitize(character.getPreferredWeapon());
        arguments[20] = new Integer(character.getPossessionNumberOfItems("Travellers' Aid"));
        arguments[21] = "";
        arguments[22] = "";
        arguments[23] = "";
        arguments[24] = "";
        arguments[25] = arguments[2];
        arguments[26] = new Integer(character.getCash());
        String possessions = "";
        PossessionIterator possIter = character.possessionIterator();
        while (possIter.hasNext()) {
            possIter.next();
            if (!"".equals(possessions)) {
                possessions += ", ";
            }
            possessions += possIter.getNameOfItem() + (possIter.getNumberOfItems() > 1 ? " (" + possIter.getNumberOfItems() + ")" : "");
        }
        arguments[27] = sanitize(possessions);

        return data.format(arguments);
    }

    private String getFormattedData2() {
        final Templates templates = CharacterTransformer.getTransformerTemplates(CharacterTransformer.DEFAULT_VIEWER);
        return CharacterTransformer.createResult(getCharacter(), templates);
    }

    private void updateViewer() {
        //viewer.setText(getFormattedData());
        String data = getFormattedData2();
        data = data.replaceFirst("http-equiv=\"Content-Type\"", "http-equiv=\"Ignore:Content-Type\"");
        try {
            Document doc = viewer.getDocument();
            doc.remove(0, doc.getLength());
            if (data == null || data.equals("")) {
                return;
            }
            Reader r = new StringReader(data);
            EditorKit kit = viewer.getEditorKit();
            kit.read(r, doc, 0);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (BadLocationException ble) {
            ble.printStackTrace();
        }
        viewer.setText(data);
        viewer.setCaretPosition(0);
    }
}

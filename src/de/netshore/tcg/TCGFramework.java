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
 * Created on 27.04.2005
 */
package de.netshore.tcg;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.JWindow;

import de.netshore.tcg.xml.CharacterSerializer;
import de.netshore.tcg.xml.CharacterTransformer;

/**
 * @author jlin
 */
public class TCGFramework {
    public static final String APP_TITLE = "netShore TCG";
    public static final String COPYRIGHT =
            "<html>" +
                    "Copyright &copy; 2005 Jochen Linnemann" +
                    "<br>" +
                    "netShore &reg; is a registered trademark of Jochen Linnemann" +
                    "</html>";
    public static final String APP_INFO =
            "<html>" +
                    "netShore TCG v0.9 (http://www.netshore.de/tcg)" +
                    "</html>";

    private static TCGFramework singleton = null;

    public static TCGFramework getInstance() {
        if (singleton == null) {
            singleton = new TCGFramework();
        }
        return singleton;
    }

    /**
     * private: only to keep'em from instantiating, this class is a singleton
     *
     * @author jlin
     */
    private TCGFramework() {
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('f');
        fileMenu.add(actNew);
        fileMenu.add(actOpen);
        fileMenu.add(actClose);
        fileMenu.addSeparator();
        fileMenu.add(actSave);
        fileMenu.add(actSaveAs);
        fileMenu.addSeparator();
        fileMenu.add(actExit);
        menuBar.add(fileMenu);

        JMenu characterMenu = new JMenu("Character");
        characterMenu.setMnemonic('c');
        characterMenu.add(actEdit);
        characterMenu.addSeparator();
        characterMenu.add(actExport);
        menuBar.add(characterMenu);

        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('h');
        helpMenu.add(actHelpContents);
        helpMenu.addSeparator();
        helpMenu.add(actAbout);
        menuBar.add(helpMenu);

        toolBar.add(actNew);
        toolBar.add(actOpen);
        toolBar.add(actSave);
        toolBar.addSeparator();
        toolBar.add(actEdit);
        toolBar.add(actExport);
        toolBar.addSeparator();
        toolBar.add(actHelpContents);

        viewPanel.setLayout(new BorderLayout());
        viewPanel.setBackground(Color.GRAY);
        viewPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        viewPanel.setPreferredSize(new Dimension(700, 700));
        
        /*
         * Creating the About box
         */
        aboutBox.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        aboutBox.setResizable(false);
        /*
        // top label
        JPanel titlePanel = new JPanel();
        titlePanel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        titlePanel.setAlignmentY(JComponent.CENTER_ALIGNMENT);
        titlePanel.setBackground(Color.BLACK);
        titlePanel.add(new JLabel("<html><font color=\"white\" size=\"+1\"><b>" + APP_TITLE + "</b></font></html>"));
        // info and copyright
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        infoPanel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        infoPanel.setAlignmentY(JComponent.TOP_ALIGNMENT);
        JLabel appInfo = new JLabel(APP_INFO);
        appInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(appInfo);
        JLabel copyright = new JLabel(COPYRIGHT);
        copyright.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(copyright);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JTextArea fairUse = new JTextArea();
        fairUse.setAlignmentX(Component.LEFT_ALIGNMENT);
        fairUse.setEditable(false);
        fairUse.setRows(12);
        fairUse.setColumns(80);
        fairUse.setLineWrap(true);
        fairUse.setWrapStyleWord(true);
        fairUse.setText(
        		"The Traveller game in all forms is owned by Far Future Enterprises. Copyright 1977 - 2003 Far Future Enterprises. Traveller is a registered trademark of Far Future Enterprises. Far Future permits web sites and fanzines for this game, provided it contains this notice, that Far Future is notified, and subject to a withdrawal of permission on 90 days notice. The contents of this site are for personal, non-commercial use only. Any use of Far Future Enterprises's copyrighted material or trademarks anywhere on this web site and its files should not be viewed as a challenge to those copyrights or trademarks. In addition, any program/articles/file on this site cannot be republished or distributed without the consent of the author who contributed it."
        		);
        JScrollPane fairUseScroller = new JScrollPane(fairUse);
        fairUseScroller.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(fairUseScroller);
        // command button row
        JPanel cmdPanel = new JPanel();
        cmdPanel.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
        cmdPanel.setAlignmentY(JComponent.CENTER_ALIGNMENT);
        cmdPanel.add(new JButton(actAboutBoxClose));
        // putting it all together
        */
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        /*
        contentPane.add(titlePanel, BorderLayout.NORTH);
        contentPane.add(infoPanel, BorderLayout.CENTER);
        contentPane.add(cmdPanel, BorderLayout.SOUTH);
        */
        aboutBox.setContentPane(contentPane);
    }

    public void splashLoadFramework() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        panel.add(imgSplash, BorderLayout.CENTER);

        JWindow splashScreen = new JWindow();
        splashScreen.setContentPane(panel);
        splashScreen.pack();
        splashScreen.setLocationRelativeTo(null);
        splashScreen.show();

        CharacterTransformer.initialize();

        splashScreen.dispose();
    }

    public JFrame populateContainer(JFrame container) {
        if (mainFrame == null) {
            mainFrame = container;

            container.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            container.addWindowListener(frameWatch);
            container.setTitle(APP_TITLE);
            container.setJMenuBar(menuBar);
            container.getContentPane().setLayout(new BorderLayout());
            container.getContentPane().add(toolBar, BorderLayout.NORTH);
            container.getContentPane().add(viewPanel, BorderLayout.CENTER);
        }
        return mainFrame;
    }

    private JFrame mainFrame = null;
    private JMenuBar menuBar = new JMenuBar();
    private JToolBar toolBar = new JToolBar();
    private JPanel viewPanel = new JPanel();
    private JTabbedPane tabs = new JTabbedPane();
    private JDialog aboutBox = new JDialog(mainFrame, "About " + APP_TITLE, true);
    private JLabel imgSplash = new JLabel(new ImageIcon(getClass().getResource("splashScreen.gif")));

    private long newCharacterCount = 0;

    private void addTab(File file, Character character) {
        if (tabs.getTabCount() == 0) {
            viewPanel.add(tabs, BorderLayout.CENTER);
            actClose.setEnabled(true);
            actSave.setEnabled(true);
            actSaveAs.setEnabled(true);
            actEdit.setEnabled(true);
            actExport.setEnabled(true);
        }
        int insertIndex = (tabs.getTabCount() == 0 ? 0 : tabs.getSelectedIndex() + 1);

        String tabTitle = character.getName();
        if (tabTitle == null || tabTitle.trim().equals("")) {
            if (file == null) {
                newCharacterCount++;
                tabTitle = "New Character " + newCharacterCount;
            } else {
                tabTitle = "Unnamed";
            }
            character.setName(tabTitle);
        }
        CharacterPanel characterPanel = new CharacterPanel(file, character);
        if (file == null) {
            characterPanel.setModified(true);
        }

        tabs.insertTab(tabTitle, null, characterPanel, null, insertIndex);
        tabs.setSelectedIndex(insertIndex);
    }

    private void checkModified(CharacterPanel cp, String title) {
        if (cp.isModified()) {
            int option = JOptionPane.showOptionDialog(
                    mainFrame,
                    title + " has been modified." +
                            "\nDo you want to save your changes?",
                    "Save Changes?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    null,
                    null
            );
            if (option == JOptionPane.YES_OPTION) {
                cp.save();
            }
        }
    }

    private void checkModified() {
        for (int i = 0; i < tabs.getTabCount(); i++) {
            Component component = tabs.getComponentAt(i);
            if (component instanceof CharacterPanel) {
                checkModified((CharacterPanel) component, tabs.getTitleAt(i));
            }
        }
    }

    private class TCGFrameListener extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            checkModified();
            mainFrame.dispose();
        }

        public void windowClosed(WindowEvent e) {
            mainFrame.dispose();
            System.exit(0);
        }
    }

    private TCGFrameListener frameWatch = new TCGFrameListener();

    private abstract class TCGAction extends AbstractAction {
        public TCGAction(char mnemonic, String name, boolean enabled) {
            super(name);
            putValue(Action.MNEMONIC_KEY, new Integer(mnemonic));
            setEnabled(enabled);
        }

        public TCGAction(char mnemonic, String name, Icon icon, boolean enabled) {
            super(name, icon);
            putValue(Action.MNEMONIC_KEY, new Integer(mnemonic));
            setEnabled(enabled);
        }
    }

    private Action actNew = new TCGAction('n', "New...", new ImageIcon(getClass().getResource("/icons/New16.gif")), true) {
        public void actionPerformed(ActionEvent event) {
            int option = JOptionPane.showOptionDialog(
                    mainFrame,
                    "Decide on how to create the new character." +
                            "\n'Random Character Generation' will start automatic character generation (all decisions are made by your computer)." +
                            "\n'Interactive Character Creation' will let you decide on important topics (just as if you were rolling up the character).",
                    "Choose Character Creation Method",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Random Character Generation", "Interactive Character Creation"},
                    null
            );
            if (option != JOptionPane.CLOSED_OPTION) {
                CharacterFactory.GeneratorConfig config = new CharacterFactory.GeneratorConfig();
                Character character = CharacterFactory.generateCharacter(config,
                        (option == 0 ? null : new CharacterCreationDialog(mainFrame))
                );
                if (character.isAlive() || config.injuryPreferred) {
                    addTab(null, character);
                } else if (option == 0) {
                    JOptionPane.showMessageDialog(
                            mainFrame,
                            "Sorry, but the character died during Random Character Generation." +
                                    "\nYou will have to start anew.",
                            "Character Died",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }
        }
    };
    private Action actOpen = new TCGAction('o', "Open...", new ImageIcon(getClass().getResource("/icons/Open16.gif")), true) {
        public void actionPerformed(ActionEvent event) {
            final JFileChooser fileChooser = new JFileChooser();
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(CharacterPanel.tcdFilter);
            fileChooser.addChoosableFileFilter(CharacterPanel.xmlFilter);
            fileChooser.setFileFilter(CharacterPanel.xmlFilter);
            if (fileChooser.showOpenDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (file != null && file.exists()) {
                    if (fileChooser.getFileFilter() == CharacterPanel.tcdFilter) {
                        try {
                            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                            Character character = (Character) ois.readObject();
                            ois.close();
                            addTab(file, character);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else if (fileChooser.getFileFilter() == CharacterPanel.xmlFilter) {
                        Character character = null;
                        try {
                            character = CharacterSerializer.read(file);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (character != null) {
                            addTab(file, character);
                        } else {
                            JOptionPane.showMessageDialog(
                                    mainFrame,
                                    "Unknown file format, expected 'XML Traveller Character Data'.",
                                    "Cannot read Character Data",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        }
                    }
                }
            }
        }
    };
    private Action actClose = new TCGAction('c', "Close", false) {
        public void actionPerformed(ActionEvent event) {
            Component component = tabs.getSelectedComponent();
            if (component != null) {
                if (component instanceof CharacterPanel) {
                    checkModified((CharacterPanel) component, tabs.getTitleAt(tabs.getSelectedIndex()));
                }
                tabs.remove(component);
                if (tabs.getTabCount() == 0) {
                    actClose.setEnabled(false);
                    actSave.setEnabled(false);
                    actSaveAs.setEnabled(false);
                    actEdit.setEnabled(false);
                    actExport.setEnabled(false);
                    viewPanel.remove(tabs);
                    viewPanel.repaint();
                }
            }
        }
    };
    private Action actSave = new TCGAction('s', "Save", new ImageIcon(getClass().getResource("/icons/Save16.gif")), false) {
        public void actionPerformed(ActionEvent event) {
            Component component = tabs.getSelectedComponent();
            if (component instanceof CharacterPanel) {
                ((CharacterPanel) component).save();
            }
        }
    };
    private Action actSaveAs = new TCGAction('a', "Save As...", new ImageIcon(getClass().getResource("/icons/SaveAs16.gif")), false) {
        public void actionPerformed(ActionEvent event) {
            Component component = tabs.getSelectedComponent();
            if (component instanceof CharacterPanel) {
                ((CharacterPanel) component).saveAs();
            }
        }
    };
    private Action actExit = new TCGAction('x', "Exit", true) {
        public void actionPerformed(ActionEvent event) {
            mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
        }
    };
    private Action actEdit = new TCGAction('e', "Edit...", new ImageIcon(getClass().getResource("/icons/Edit16.gif")), false) {
        public void actionPerformed(ActionEvent event) {
            int index = tabs.getSelectedIndex();
            Component component = tabs.getComponentAt(index);
            if (component instanceof CharacterPanel) {
                final CharacterDialog dialog = new CharacterDialog(mainFrame, true);
                dialog.setCharacter(((CharacterPanel) component).getCharacter());
                dialog.pack();
                dialog.setLocationRelativeTo(component);
                dialog.show();
                ((CharacterPanel) component).setCharacter(dialog.getCharacter());
                ((CharacterPanel) component).setModified(true);
                String tabTitle = dialog.getCharacter().getName();
                if (tabTitle == null || tabTitle.trim().equals("")) {
                    tabTitle = "Unnamed";
                }
                tabs.setTitleAt(index, tabTitle);
            }
        }
    };
    private Action actExport = new TCGAction('x', "Export...", new ImageIcon(getClass().getResource("/icons/Export16.gif")), false) {
        public void actionPerformed(ActionEvent event) {
            Component component = tabs.getSelectedComponent();
            if (component instanceof CharacterPanel) {
                ((CharacterPanel) component).export();
            }
        }
    };
    private Action actHelpContents = new TCGAction('c', "Contents", new ImageIcon(getClass().getResource("/icons/Help16.gif")), false) {
        public void actionPerformed(ActionEvent event) {
            // TODO : give help
        }
    };
    private Action actAbout = new TCGAction('a', "About " + APP_TITLE, new ImageIcon(getClass().getResource("/icons/About16.gif")), true) {
        public void actionPerformed(ActionEvent event) {
            if (imgSplash != null) {
                imgSplash.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        aboutBox.dispose();
                    }
                });
                aboutBox.getContentPane().add(imgSplash, BorderLayout.CENTER);
                imgSplash = null;
            }
            aboutBox.pack();
            aboutBox.setLocationRelativeTo(mainFrame);
            aboutBox.show();
        }
    };
    /*
    private Action actAboutBoxClose = new TCGAction('c', "Close", true) {
        public void actionPerformed(ActionEvent event) {
            aboutBox.dispose();
        }
    };
    */
}

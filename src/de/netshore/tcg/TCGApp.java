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

import java.text.MessageFormat;
import java.util.Locale;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;

import de.netshore.tcg.CharacterFactory.GeneratorConfig;

/**
 * @author jlin
 *         <p>
 *         TCGApp - main() wrapper for app startup
 */
public class TCGApp {
    public static void main(String[] args) {
        prepareLogging(Level.FINE);

        Locale.setDefault(Locale.US);

        //mainFeatureTest(args);
        //mainBatchTest(args);
        //mainDialogTest(args);
        mainFrameTest(args);
    }

    public static void debug(String msg) {
        //getLogger().fine(msg);
        //System.out.println("[DBG] " + msg);
    }

    private static Logger getLogger() {
        return Logger.getLogger("de.netshore.tcg");
    }

    private static void prepareLogging(Level logLevel) {
        ConsoleHandler ch = new ConsoleHandler();
        ch.setLevel(logLevel);
        ch.setFormatter(new Formatter() {
            public String format(LogRecord rec) {
                return rec.getMessage() + "\n";
            }
        });

        getLogger().setLevel(logLevel);
        getLogger().addHandler(ch);
    }

    private static void mainFeatureTest(String[] args) {
        String pattern = "Testing argument array length maximum: {0} {1} {2} {3} {4} {5} {6} {7} {8} {9} {10} {11} {12} {13} {14} {15} {16} {17} {18} {19} {20} {21} {22} {23} {24} {25} {26} {27} {28} {29} {30}";
        String[] arguments = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};

        MessageFormat msgFormat = new MessageFormat(pattern);
        System.out.println(msgFormat.format(arguments));
    }

    private static void mainBatchTest(String[] args) {
        Service[] services = ServiceFactory.getServices("book1.xml");

        //System.out.println("Number of available Services: " + services.length);
        //System.out.println("");
        //for (int i = 0; i < services.length; i++) {
        //    System.out.println(services[i]);
        //    System.out.println("");
        //}

        int numberOfCharacters = 100;
        int deathToll = 0;
        int cashTotal = 0;
        for (int i = 0; i < numberOfCharacters; i++) {
            GeneratorConfig config = new GeneratorConfig();
            config.preferredService = 4;
            config.preferredTermNum = 4;

            Character character = CharacterFactory.generateCharacter(config, null);
            if (!character.isAlive()) {
                deathToll++;
            } else {
                cashTotal += character.getCash();
                System.out.println(character);
                System.out.println("");
            }
        }
        System.out.println("");
        System.out.println("Of " + numberOfCharacters + " characters " + deathToll + " died (i.e. " + (100.0 * deathToll / numberOfCharacters) + "%)");
        System.out.println((numberOfCharacters - deathToll) + " surviving characters received Cr" + (cashTotal / (numberOfCharacters - deathToll)) + " in avarage");
    }

    private static void mainDialogTest(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            JDialog.setDefaultLookAndFeelDecorated(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void mainFrameTest(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            JFrame.setDefaultLookAndFeelDecorated(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        TCGFramework.getInstance().splashLoadFramework();

        JFrame frame = TCGFramework.getInstance().populateContainer(new JFrame());
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.show();
    }

    private TCGApp() {
        // only to keep'em from instantiating
    }
}

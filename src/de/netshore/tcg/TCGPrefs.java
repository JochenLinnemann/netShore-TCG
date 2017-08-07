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
 * Created on 02.06.2005
 */
package de.netshore.tcg;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * @author jlin
 */
public class TCGPrefs {
    private static final String INJURY_PREFERRED = "injuryPreferred";

    private static TCGPrefs singleton = null;

    public static TCGPrefs getInstance() {
        if (singleton == null) {
            singleton = new TCGPrefs();
        }
        return singleton;
    }

    private Preferences prefs = null;

    private TCGPrefs() {
        prefs = Preferences.userNodeForPackage(this.getClass());
    }

    public boolean saveChanges() {
        try {
            prefs.flush();
            return true;
        } catch (BackingStoreException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setInjuryPreferred(boolean deathAllowed) {
        prefs.putBoolean(INJURY_PREFERRED, deathAllowed);
    }

    public boolean isInjuryPreferred() {
        return prefs.getBoolean(INJURY_PREFERRED, false);
    }
}

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

import java.util.Random;

/**
 * @author jlin
 *         <p>
 *         Dice - simulating rolled dice
 */
public class Dice {
    private static final Random rand = new Random();

    public static int roll(int numberOfDice, int sidesPerDice) {
        int result = 0;

        for (int i = 0; i < numberOfDice; i++) {
            result += rand.nextInt(sidesPerDice) + 1;
        }
        return result;
    }

    public static int roll(int numberOfDice) {
        return roll(numberOfDice, 6);
    }

    private Dice() {
        // only to keep'em from instantiating
    }
}

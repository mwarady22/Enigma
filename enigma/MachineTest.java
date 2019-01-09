package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Machine class.
 *  @author Michaela Warady
 */

public class MachineTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    @Test
    public void testDoubleStep() {
        Alphabet ac = new CharacterRange('A', 'D');
        Rotor one = new Reflector("R1", new Permutation("(AC) (BD)", ac));
        Rotor two = new MovingRotor("R2", new Permutation("(ABCD)", ac), "C");
        Rotor three = new MovingRotor("R3", new Permutation("(ABCD)", ac), "C");
        Rotor four = new MovingRotor("R4", new Permutation("(ABCD)", ac), "C");
        String setting = "AAA";
        Rotor[] machineRotors = {one, two, three, four};
        String[] rotors = {"R1", "R2", "R3", "R4"};
        Machine mach = new Machine(
                ac, 4, 3, new ArrayList<>(Arrays.asList(machineRotors)));
        mach.insertRotors(rotors);
        mach.setRotors(setting);

        assertEquals("AAAA", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("AAAB", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("AAAC", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("AABD", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("AABA", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("AABB", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("AABC", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("AACD", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("ABDA", getSetting(ac, machineRotors));
    }

    /** Helper method to get the String representation of the current
     * Rotor settings */
    private String getSetting(Alphabet alph, Rotor[] machineRotors) {
        String currSetting = "";
        for (Rotor r : machineRotors) {
            currSetting += alph.toChar(r.setting());
        }
        return currSetting;
    }
}

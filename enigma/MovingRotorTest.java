package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import java.util.HashMap;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Michaela Warady
 */
public class MovingRotorTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Rotor rotor;
    private String alpha = UPPER_STRING;

    /** Check that rotor has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkRotor(String testId,
                            String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, rotor.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d (%c)", ci, c),
                         ei, rotor.convertForward(ci));
            assertEquals(msg(testId, "wrong inverse of %d (%c)", ei, e),
                         ci, rotor.convertBackward(ei));
        }
    }

    /** Set the rotor to the one with given NAME and permutation as
     *  specified by the NAME entry in ROTORS, with given NOTCHES. */
    private void setRotor(String name, HashMap<String, String> rotors,
                          String notches) {
        rotor = new MovingRotor(name, new Permutation(rotors.get(name), UPPER),
                                notches);
    }

    /* ***** TESTS ***** */

    @Test
    public void checkRotorAtA() {
        setRotor("I", NAVALA, "");
        checkRotor("Rotor I (A)", UPPER_STRING, NAVALA_MAP.get("I"));
    }

    @Test
    public void checkRotorAtB() {
        setRotor("I", NAVALB, "B");
        checkRotor("Rotor I (B)", UPPER_STRING, NAVALB_MAP.get("I"));
        rotor.set(0);
        assertFalse(rotor.atNotch());
        rotor.advance();
        assertTrue(rotor.atNotch());
    }

    @Test
    public void checkRotorAtZ() {
        setRotor("I", NAVALZ, "");
        checkRotor("Rotor I (Z)", UPPER_STRING, NAVALZ_MAP.get("I"));
    }

    @Test
    public void checkRotorAtAII() {
        setRotor("II", NAVALA, "");
        checkRotor("Rotor II (A)", UPPER_STRING, NAVALA_MAP.get("II"));
    }

    @Test
    public void checkRotorAtBIII() {
        setRotor("III", NAVALB, "");
        checkRotor("Rotor III (B)", UPPER_STRING, NAVALB_MAP.get("III"));
    }


    @Test
    public void checkRotorAtZIV() {
        setRotor("IV", NAVALZ, "ACE");
        checkRotor("Rotor IV (Z)", UPPER_STRING, NAVALZ_MAP.get("IV"));
        rotor.set(0);
        assertTrue(rotor.atNotch());
        rotor.advance();
        assertFalse(rotor.atNotch());
        rotor.advance();
        assertTrue(rotor.atNotch());
        rotor.advance();
        assertFalse(rotor.atNotch());
        rotor.advance();
        assertTrue(rotor.atNotch());
        rotor.advance();
        assertFalse(rotor.atNotch());
    }

    @Test
    public void checkRotorAdvance() {
        setRotor("I", NAVALA, "");
        rotor.advance();
        checkRotor("Rotor I advanced", UPPER_STRING, NAVALB_MAP.get("I"));
    }

    @Test
    public void checkRotorSet() {
        setRotor("I", NAVALA, "");
        rotor.set(25);
        checkRotor("Rotor I set", UPPER_STRING, NAVALZ_MAP.get("I"));
    }


}

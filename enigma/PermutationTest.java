package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Michaela Warady
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */


    @Test
    public void checkMapping() {
        perm = new Permutation("", UPPER);
        assertEquals("{A=A, B=B, C=C, D=D, E=E, F=F, G=G, H=H, I=I, J=J, "
                + "K=K, L=L, M=M, N=N, O=O, P=P, Q=Q, R=R, S=S, T=T, U=U, "
                + "V=V, W=W, X=X, Y=Y, Z=Z}", perm.getH().toString());

        perm = new Permutation("(ABCDEFGHIJKLMNOPQRSTUVWXYZ)", UPPER);
        assertEquals("{A=B, B=C, C=D, D=E, E=F, F=G, G=H, H=I, I=J, J=K, K=L, "
                + "L=M, M=N, N=O, O=P, P=Q, Q=R, R=S, S=T, T=U, U=V, V=W, "
                        + "W=X, X=Y, Y=Z, Z=A}", perm.getH().toString());

        perm = new Permutation("(ABCDE) (FG) (HIJKL) (MNOP) (STUV) (WX)",
                UPPER);
        assertEquals("{A=B, B=C, C=D, D=E, E=A, F=G, G=F, H=I, I=J, J=K, "
                + "K=L, L=H, M=N, N=O, O=P, P=M, Q=Q, R=R, S=T, T=U, U=V, "
                + "V=S, W=X, X=W, Y=Y, Z=Z}", perm.getH().toString());

        perm = new Permutation("(TDMKL) (OW) (QSAFHZ)", UPPER);
        assertEquals("{A=F, B=B, C=C, D=M, E=E, F=H, G=G, H=Z, I=I, J=J, K=L,"
                + " L=T, M=K, N=N, O=W, P=P, Q=S, R=R, S=A, T=D, U=U, V=V, "
                + "W=O, X=X, Y=Y, Z=Q}", perm.getH().toString());

        perm = new Permutation("(TDMKL) (X) (OW) (QSAFHZ)", UPPER);
        assertEquals("{A=F, B=B, C=C, D=M, E=E, F=H, G=G, H=Z, I=I, J=J,"
                + " K=L, L=T, M=K, N=N, O=W, P=P, Q=S, R=R, S=A, T=D, U=U, "
                + "V=V, W=O, X=X, Y=Y, Z=Q}", perm.getH().toString());

    }

    @Test
    public void checkSize() {
        Alphabet alph = new CharacterRange('A', 'Z');
        perm = new Permutation("", alph);
        assertEquals(26, perm.size());

        Alphabet shorter = new CharacterRange('D', 'G');
        perm = new Permutation("", shorter);
        assertEquals(4, perm.size());

        Alphabet none = new CharacterRange('Z', 'Z');
        perm = new Permutation("", none);
        assertEquals(1, perm.size());

    }



    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void checkPermuteandInvert() {
        perm = new Permutation("(ABCDEFGHIJKLMNOPQRSTUVWXYZ)", UPPER);
        checkPerm("shift1", "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
                "BCDEFGHIJKLMNOPQRSTUVWXYZA");

        perm = new Permutation("(AEIOU) (YW)", UPPER);
        checkPerm("vowels", "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
                "EBCDIFGHOJKLMNUPQRSTAVYXWZ");

        perm = new Permutation("(ZYXWVUTSRQPONMLKJIHGFEDCBA)", UPPER);
        checkPerm("shiftneg1", "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
                "ZABCDEFGHIJKLMNOPQRSTUVWXY");

    }

    @Test
    public void checkDerangement() {
        perm = new Permutation("(ABCDEFGHIJKLMNOPQRSTUVWXYZ)", UPPER);
        assertTrue(perm.derangement());

        perm = new Permutation("(QWERTYUIOPASDFGHJKLZXCVBNM)", UPPER);
        assertTrue(perm.derangement());

        perm = new Permutation("(MNBV) (CXZL) (KJHG) (FDSA) (POIU) (YTRE) (WQ)",
                UPPER);
        assertTrue(perm.derangement());

        perm = new Permutation("(ABCDEFGHIJKLMNPQRSTUVWXYZ)", UPPER);
        assertFalse(perm.derangement());




    }
}

package enigma;

import java.util.HashMap;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Michaela Warady
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        this._cycles = cycles;
        this._h = new HashMap<String, String>();
        this._hi = new HashMap<String, String>();
        if (this._cycles.length() > 0) {
            String[] strings = this._cycles.split("\\(");
            for (int x = 1; x < strings.length; x += 1) {
                addCycle(strings[x]);
            }
        }
        for (int x = 0; x < _alphabet.size(); x += 1) {
            if (!_h.containsKey(Character.toString(_alphabet.toChar(x)))) {
                _h.put(Character.toString(_alphabet.toChar(x)),
                        Character.toString(_alphabet.toChar(x)));
            }
        }
        _h.forEach((k, v) -> _hi.put(v, k));
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        if (cycle.length() > 0) {
            for (int x = 0; x < cycle.length(); x += 1) {
                if (!this._alphabet.contains(cycle.charAt(x))
                        && !(this._alphabet.contains(cycle.charAt(x + 1))
                        || Character.toString(cycle.charAt(x + 1)).equals(
                                ")"))) {
                    throw new EnigmaException("Character not in alphabet.");
                }
                if (x + 1 < cycle.length()) {
                    if (!Character.toString(cycle.charAt(x + 1)).equals(")")) {
                        _h.put(Character.toString(cycle.charAt(x)),
                                Character.toString(cycle.charAt(x + 1)));
                    } else {
                        _h.put(Character.toString(cycle.charAt(x)),
                            Character.toString(cycle.charAt(0)));
                        break;
                    }
                }
            }
        }
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        return _alphabet.toInt(_h.get(Character.
                toString(_alphabet.toChar(p))).charAt(0)) % size();
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        return wrap(_alphabet.toInt(_hi.get(Character.
                toString(_alphabet.toChar(c))).charAt(0)));
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        return _alphabet.toChar(
                this.permute(_alphabet.toInt(p)));
    }

    /** Return the result of applying the inverse of this permutation to C. */
    int invert(char c) {
        return (int) _hi.get(Character.toString(c)).charAt(0);
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (int x = 0; x < size(); x += 1) {
            if (_h.get(Character.toString(_alphabet.toChar(x))).
                    equals(Character.toString(_alphabet.toChar(x)))) {
                return false;
            }
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** The hashmap. */
    private HashMap<String, String> _h;

    /** Returns h. */
    public HashMap getH() {
        return this._h;
    }

    /** Holds cycles. */
    private String _cycles;

    /** Returns cycles. */
    public String getCycles() {
        return _cycles;
    }

    /** Holds inverse cycles. */
    private HashMap<String, String> _hi;

    /** Returns hi. */
    public HashMap getHi() {
        return this._hi;
    }

    /** Returns alphabet. */
    public Alphabet getAlphabet() {
        return _alphabet;
    }
}

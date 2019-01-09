package enigma;

import static enigma.EnigmaException.*;

/** Superclass that represents a rotor in the enigma machine.
 *  @author Michaela Warady
 */
class Rotor {

    /** A rotor named NAME whose permutation is given by PERM. */
    Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;
        this._setting = 0;
    }

    /** Return my name. */
    String name() {
        return _name;
    }

    /** Return my alphabet. */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /** Return my permutation. */
    Permutation permutation() {
        return _permutation;
    }

    /** Return the size of my alphabet. */
    int size() {
        return _permutation.size();
    }

    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return false;
    }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return false;
    }

    /** Return my current setting. */
    int setting() {
        return this._setting;
    }

    /** Set setting() to POSN.  */
    void set(int posn) {
        if (0 > posn || posn > alphabet().size() - 1) {
            throw new EnigmaException(
                    "This setting is not possible with the current alphabet.");
        } else {
            this._setting = posn;
        }
    }

    /** Set setting() to character CPOSN. */
    void set(char cposn) {
        this.set(this._permutation.getAlphabet().toInt(cposn));
    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. */
    int convertForward(int p) {
        return this._permutation.wrap(this._permutation.permute(
                this._permutation.wrap(p + _setting)) - _setting);
    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        return this._permutation.wrap(this._permutation.invert(
                this._permutation.wrap(e + _setting)) - _setting);
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        return false;
    }

    /** Advance me one position, if possible. By default, does nothing. */
    void advance() {
    }

    @Override
    public String toString() {
        return "Rotor " + _name;
    }

    /** My name. */
    private final String _name;

    /** The permutation implemented by this rotor in its 0 position. */
    private Permutation _permutation;

    /** Returns permutation. */
    public Permutation getPerm() {
        return this._permutation;
    }

    /** Holds setting. */
    private int _setting;

    /** Returns setting. */
    public int getSetting() {
        return this._setting;
    }

    /** Sets sett. */
    public void setter() {
        this._setting += 1;
        this._setting = this._setting
                % (this.permutation().getAlphabet().size());
    }


}

package enigma;

import java.util.HashMap;


/** Class that represents a rotating rotor in the enigma machine.
 *  @author Michaela Warady
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     * PERMU is permutation with which to set moving rotor.
     */
    MovingRotor(String name, Permutation permu, String notches) {
        super(name, permu);
        this.perm = permu;
        this._notches = new HashMap<Integer, String>();
        for (int x = 0; x < notches.length(); x += 1) {
            this._notches.put(this._notches.size(),
                    Character.toString(notches.charAt(x)));
        }
    }


    @Override
    void advance() {
        this.setter();
    }

    /** Holds notches. */
    private HashMap<Integer, String> _notches;


    /** The perm. */
    private Permutation perm;

    @Override
    /** Return true if rotor is at notch. */
    boolean atNotch() {
        return this._notches.containsValue(
                Character.toString(perm.getAlphabet().toChar(getSetting())));
    }

    @Override
    /** Rotates. */
    boolean rotates() {
        return true;
    }



}

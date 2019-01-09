package enigma;

/** Class that represents a rotor that has no ratchet and does not advance.
 *  @author Michaela Warady
 */
class FixedRotor extends Rotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is given by PERM. */
    FixedRotor(String name, Permutation perm) {
        super(name, perm);
    }


    @Override
    void advance() {
        throw new EnigmaException("FixedRotor can't advance!");
    }

}

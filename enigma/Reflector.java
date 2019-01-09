package enigma;

/** Class that represents a reflector in the enigma.
 *  @author Michaela Warady
 */
class Reflector extends FixedRotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is PERM. */
    Reflector(String name, Permutation perm) {
        super(name, perm);
        if (!perm.derangement()) {
            throw new EnigmaException("Reflector must take in derangement.");
        }
    }

    @Override
    void set(int posn) {
        if (posn != 0) {
            throw new EnigmaException("Reflector has only one position.");
        }
    }

    @Override
    void set(char cposn) {
        if (alphabet().toInt(cposn) != 0) {
            throw new EnigmaException("Reflector has only one position.");
        }
    }

    @Override
    int convertBackward(int e) {
        throw new EnigmaException("Reflector only goes forwards.");
    }

    @Override
    boolean reflecting() {
        return true;
    }
}

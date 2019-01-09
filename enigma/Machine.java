package enigma;

import java.util.ArrayList;
import java.util.Collection;

/** Class that represents a complete enigma machine.
 *  @author Michaela Warady
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        this._allRotorStorage = (ArrayList<Rotor>) allRotors;
        this._myRotorStorage = new ArrayList<Rotor>();
        this._numRotors = numRotors;
        this._numPawls = pawls;
        this._plugboard = new Permutation("", this._alphabet);
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return this._numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return this._numPawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        if (rotors.length != numRotors()) {
            throw new EnigmaException("Not the correct number of rotors.");
        }
        for (int x = 0; x < rotors.length; x += 1) {
            for (Rotor z : _allRotorStorage) {
                if ((z.name().toUpperCase()).equals(rotors[x])
                        && !_myRotorStorage.contains(z)) {
                    if (x == 0) {
                        if (z.reflecting()) {
                            _myRotorStorage.add(z);
                            break;
                        } else {
                            throw new EnigmaException(
                                    "First rotor is not reflector.");
                        }
                    } else {
                        if (z.reflecting()) {
                            throw new EnigmaException(
                                    "Cannot have multiple reflectors.");
                        } else if (!z.rotates()) {
                            if (_myRotorStorage.get(x - 1).rotates()) {
                                throw new EnigmaException(
                                        "Cannot have a moving rotor "
                                                + "before a fixed rotor.");
                            } else {
                                _myRotorStorage.add(z);
                                break;
                            }
                        } else {
                            _myRotorStorage.add(z);
                            break;
                        }
                    }
                }
            }
            if (_myRotorStorage.size() - 1 < x) {
                throw new EnigmaException(
                        "Rotor " + rotors[x]
                                + " is not an available "
                                + "rotor for this machine.");
            }
        }
        int numMovingRotors = 0;
        for (Rotor rot : _myRotorStorage) {
            if (rot.rotates()) {
                numMovingRotors += 1;
            }
        }
        if (numMovingRotors != numPawls()) {
            throw new EnigmaException(
                    "Number of moving rotors and number of pawls not equal.");
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 upper-case letters. The first letter refers to the
     *  leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != numRotors() - 1) {
            throw new EnigmaException(
                    "Need to set all rotors except reflector.");
        }
        for (int z = 1; z <= setting.length(); z += 1) {
            _myRotorStorage.get(z).set(setting.charAt(z - 1));
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        this._plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        int d = c;
        for (int s = 1; s < _myRotorStorage.size(); s += 1) {
            if (s == _myRotorStorage.size() - 1) {
                if (_myRotorStorage.get(s).atNotch()
                        && _myRotorStorage.get(s - 1).rotates()) {
                    _myRotorStorage.get(s - 1).advance();
                }
                _myRotorStorage.get(s).advance();
            } else if (_myRotorStorage.get(s).atNotch()
                    && _myRotorStorage.get(s - 1).rotates()) {
                _myRotorStorage.get(s).advance();
                _myRotorStorage.get(s - 1).advance();
            }
        }
        for (int q = _myRotorStorage.size() - 1; q > 0; q -= 1) {
            d = _myRotorStorage.get(q).convertForward(d);
        }
        d = _myRotorStorage.get(0).convertForward(d);
        for (int r = 1; r < _myRotorStorage.size();  r += 1) {
            d = _myRotorStorage.get(r).convertBackward(d);

        }
        return d;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String crypt = "";
        String mg = msg;
        mg = mg.replaceAll(" ", "");
        mg = mg.replaceAll("\t", "");
        for (int a = 0; a < mg.length(); a += 1) {
            char c = mg.charAt(a);
            int d;
            int e;
            char f;
            e = _alphabet.toInt(
                    (Character.toString(c)).toUpperCase().charAt(0));
            e = this._plugboard.permute(e);
            d = convert(e);
            f = _alphabet.toChar(this._plugboard.invert(d));
            crypt = crypt + f;
        }
        return crypt;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Returns alphabet. */
    public Alphabet getAlphabet() {
        return _alphabet;
    }

    /** Stores allrotors. */
    private ArrayList<Rotor> _allRotorStorage;

    /** Stores myrotors. */
    private ArrayList<Rotor> _myRotorStorage;

    /** Stores numRotors. */
    private int _numRotors;

    /** Stores numPawls. */
    private int _numPawls;

    /** Holds plugboard. */
    private Permutation _plugboard;

}

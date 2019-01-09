package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Michaela Warady
 */
public final class Main {

    /**
     * Process a sequence of encryptions and decryptions, as
     * specified by ARGS, where 1 <= ARGS.length <= 3.
     * ARGS[0] is the name of a configuration file.
     * ARGS[1] is optional; when present, it names an input file
     * containing messages.  Otherwise, input comes from the standard
     * input.  ARGS[2] is optional; when present, it names an output
     * file for processed messages.  Otherwise, output goes to the
     * standard output. Exits normally if there are no errors in the input;
     * otherwise with code 1.
     */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /**
     * Check ARGS and open the necessary files (see comment on main).
     */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /**
     * Return a Scanner reading from the file named NAME.
     */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /**
     * Return a PrintStream writing to the file named NAME.
     */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /**
     * Configure an Enigma machine from the contents of configuration
     * file _config and apply it to the messages in _input, sending the
     * results to _output.
     */
    private void process() {
        Machine mach = readConfig();
        ArrayList<String> lines = new ArrayList<String>();
        while (_input.hasNext()) {
            lines.add(_input.nextLine());
        }
        for (int x = 0; x < lines.size(); x += 1) {
            if (!lines.get(0).startsWith("*")) {
                throw new EnigmaException("File must start with settings.");
            }
            if (lines.get(x).equals("")) {
                this._output.print("\n");
            } else if (lines.get(x).startsWith("*")) {
                setUp(mach, lines.get(x));
            } else {
                String mess = mach.convert(lines.get(x));
                printMessageLine(mess);
                this._output.print("\n");
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            ArrayList<String> lines = new ArrayList<String>();
            while (_config.hasNext()) {
                lines.add(_config.nextLine());
            }
            String[] alph = lines.get(0).split("-");
            _alphabet = new CharacterRange(alph[0].charAt(0),
                    alph[1].charAt(0));
            while (Character.toString(lines.get(1).charAt(0)).equals(" ")) {
                lines.set(1, lines.get(1).substring(1));
            }
            String[] numbers = lines.get(1).split(" ");
            int nrotors = Integer.parseInt(numbers[0]);
            int npawls = Integer.parseInt(numbers[1]);
            ArrayList<String> rotorStrings = new ArrayList<String>();
            for (int x = 2; x < lines.size(); x += 1) {
                while (Character.toString(lines.get(x).charAt(0)).equals(" ")) {
                    lines.set(x, lines.get(x).substring(1));
                }
                if (Character.toString(lines.get(x).charAt(0)).equals("(")) {
                    rotorStrings.set(rotorStrings.size() - 1,
                            rotorStrings.get(rotorStrings.size() - 1)
                                    + lines.get(x));
                } else {
                    rotorStrings.add(lines.get(x));
                }
            }
            ArrayList<Rotor> rots = new ArrayList<Rotor>();
            for (String rstring : rotorStrings) {
                if (rstring != null) {
                    rots.add(readRotor(rstring));
                }
            }
            return new Machine(_alphabet, nrotors, npawls, rots);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config.
     * @param rstring string of rotors.*/
    private Rotor readRotor(String rstring) {
        try {
            String[] split = rstring.split(" ");
            String name = split[0];
            String type = split[1].substring(0, 1);
            String notches = split[1].substring(1);
            String perms = "";
            for (int x = 2; x < split.length; x += 1) {
                perms = perms + " " + split[x];
            }
            Permutation p = new Permutation(perms, _alphabet);
            if (type.equals("N")) {
                FixedRotor fix = new FixedRotor(name, p);
                return fix;
            } else if (type.equals("M")) {
                MovingRotor move = new MovingRotor(name, p, notches);
                return move;
            } else if (type.equals("R")) {
                Reflector reflec = new Reflector(name, p);
                return reflec;
            } else {
                throw new EnigmaException(
                        "Rotor type not moving, fixed or reflector.");
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        String[] sets = settings.split(" ");
        M.insertRotors(Arrays.copyOfRange(sets, 1, M.numRotors() + 1));
        M.setRotors(sets[M.numRotors() + 1]);
        if (sets.length > M.numRotors() + 1) {
            String[] plugs = Arrays.copyOfRange(sets,
                    M.numRotors() + 1, sets.length);
            String perms = "";
            for (int x = 0; x < plugs.length; x += 1) {
                perms = perms + plugs[x] + " ";
            }
            Permutation plugperm = new Permutation(perms, M.getAlphabet());
            M.setPlugboard(plugperm);
        }

    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        if (msg.length() < 5) {
            this._output.print(msg);
        } else {
            if (msg.substring(0, 5).contains("\n")) {
                for (int x = 0; x < 5; x += 1) {
                    if (Character.toString(msg.charAt(x)).equals("\n")) {
                        this._output.print(msg.substring(0, x));
                        this._output.print("\n");
                        printMessageLine(msg.substring(x + 1));
                        break;
                    }
                }
            } else {
                this._output.print(msg.substring(0, 5) + " ");
                printMessageLine(msg.substring(5));
            }
        }
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;
}

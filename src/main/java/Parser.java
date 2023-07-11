import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;


public class Parser {
    private static final int TOKEN_MAX_LENGTH = 10;

    public static Apfloat parse(String s, int pr) throws ParseException {
        return parse(new StringBuilder(s), pr);
    }

    public static Apfloat parse(StringBuilder s, int pr) throws ParseException {
        System.out.println("parseInput : " + s);
        int lastInd = 0;
        while (lastInd < s.length()) {
            if (s.charAt(lastInd) == '(') {
                int lastScobe = getLastScobe(s.substring(lastInd, s.length())) + lastInd + 1;
                if (Character.isLetterOrDigit(s.charAt(lastInd - 1))) {
                    System.out.println("parseAndApplying");
                    String token = getToken(s.substring(Math.max(lastInd - TOKEN_MAX_LENGTH, 0), lastInd));

                    s.replace(lastInd - token.length(), lastScobe,
                            parseFun(token + "(" + s.substring(lastInd + 1, lastScobe - 1) + ")", pr).toString(true));
                } else {
                    System.out.println("parsing");
                    s.replace(lastInd, lastScobe, parse(s.substring(lastInd, lastScobe), pr).toString(true));
                }
            }
            lastInd++;
        }
        Apfloat out = simpleParse(s.toString(), pr);
        System.out.println("returns apfloat from : " + out.toString(true));
        return out;
    }

    public static Apfloat simpleParse(String s, int pr) {
        int tokenIndexes[] = getAllTokens(s);
        if (tokenIndexes.length == 0 || tokenIndexes[0] == 0) {
            return new Apfloat(s, pr);
        }

        ArrayList<String> value = new ArrayList<>();
        ArrayList<Character> state = new ArrayList<>();
        int startToken = 0;
        boolean isNegative = false;

        if (tokenIndexes[0] == 0 && s.charAt(0) == '-') {
            isNegative = true;
            startToken = 1;
            value.add(getRightValue(s, 0));
            state.add('0');
        }
        for (int tokenIndex = startToken; tokenIndex < tokenIndexes.length; tokenIndex++) {
            int index = tokenIndexes[tokenIndex];
            char ch = s.charAt(index);

        }
        return new Apfloat(1, 1);
    }

    public static String getRightValue(String s, int i) {
        int lastIndex = i;
        while (Character.isDigit(s.charAt(lastIndex)) | s.charAt(lastIndex++) == '.') {
        }
        return s.substring(i, lastIndex);
    }

    public static int[] getAllTokens(String s) {
        ArrayList<Integer> indexes = new ArrayList<>();
        int index = -1;
        while (++index < s.length()) {
            char c = s.charAt(index);
            switch (c) {
                case '-':
                    indexes.add(index);
                    break;
                case '+':
                    indexes.add(index);
                    break;
                case '*':
                    indexes.add(index);
                    break;
                case '/':
                    indexes.add(index);
                    break;
            }
        }
        return indexes.stream().mapToInt(i -> i).toArray();
    }


    public static String[] splitVars(String s) {
        ArrayList<String> vals = new ArrayList<>();
        int index = -1;
        int scobes = 0;
        int startIndex = 0;
        while (++index < s.length()) {
            char c = s.charAt(index);
            switch (c) {
                case '(':
                    scobes++;
                    break;
                case ')':
                    scobes--;
                    break;
                case ',':
                    if (scobes == 0) {
                        vals.add(s.substring(startIndex, index));
                        startIndex = index + 1;
                    }
                    break;
                default:
                    break;
            }
        }
        vals.add(s.substring(startIndex, s.length()));
        return vals.toArray(new String[vals.size()]);
    }


    public static int getLastScobe(String s) {
        System.out.print("getLastScobe : " + s + "  ");
        int scobes = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') scobes++;
            if (s.charAt(i) == ')') scobes--;
            if (scobes == 0) {
                System.out.println(i);
                return i;
            }
        }
        System.out.println(s.length() - 1);
        return s.length() - 1;
    }

    public static String getToken(String s) throws ParseException {
        System.out.print("getToken : " + s + "  ");
        for (int i = s.length() - 1; i >= 0; i--) {
            if (!Character.isLetterOrDigit(s.charAt(i))) {
                System.out.println(s.substring(i + 1, s.length()));
                return s.substring(i + 1, s.length());
            }
        }
        System.out.println(s.substring(0, s.length()));
        return s.substring(0, s.length());
    }

    //=================== PARSE FUNCTION =================
    public static Apfloat parseFun(String s, int pr) throws ParseException {
        return parseFun(new StringBuilder(s), pr);
    }

    public static Apfloat parseFun(StringBuilder s, int pr) throws ParseException {
        System.out.println("parseFun Input :" + s.toString());
        StringBuilder reg = new StringBuilder();
        int lastInd = 0;
        do {
            reg.append(s.charAt(lastInd++));
            if (s.length() <= lastInd) throwException();
        } while (Character.isLetterOrDigit(s.charAt(lastInd)));
        if (s.charAt(lastInd) != '(') throwException();

        String vals[] = splitVars(s.substring(lastInd + 1, s.length() - 1).toString());
        ArrayList<Apfloat> vars = new ArrayList<>();
        for (int i = 0; i < vals.length; i++) {
            vars.add(parse(vals[i], pr));
        }
        vars.forEach(e -> System.out.print(e.toString(true) + "   "));
        System.out.println();

        switch (reg.toString()) {
            case "abs":
                if (vars.size() != 1) throwException();
                return ApfloatMath.abs(vars.get(0));
            case "acos":
                if (vars.size() != 1) throwException();
                return ApfloatMath.acos(vars.get(0));
            case "agm":
                if (vars.size() != 2) throwException();
                return ApfloatMath.agm(vars.get(0), vars.get(1));
            case "asinh":
                if (vars.size() != 1) throwException();
                return ApfloatMath.asinh(vars.get(0));
            case "asin":
                if (vars.size() != 1) throwException();
                return ApfloatMath.asin(vars.get(0));
            case "atan":
                if (vars.size() != 1) throwException();
                return ApfloatMath.atan(vars.get(0));
            case "atan2":
                if (vars.size() != 2) throwException();
                return ApfloatMath.atan2(vars.get(0), vars.get(1));
            case "cbrt":
                if (vars.size() != 1) throwException();
                return ApfloatMath.cbrt(vars.get(0));
            case "ceil":
                if (vars.size() != 1) throwException();
                return ApfloatMath.ceil(vars.get(0));
            case "cos":
                if (vars.size() != 1) throwException();
                return ApfloatMath.cos(vars.get(0));
            case "cosh":
                if (vars.size() != 1) throwException();
                return ApfloatMath.cosh(vars.get(0));
            case "exp":
                if (vars.size() != 1) throwException();
                return ApfloatMath.exp(vars.get(0));
            case "floor":
                if (vars.size() != 1) throwException();
                return ApfloatMath.floor(vars.get(0));
            case "fmod":
                if (vars.size() != 2) throwException();
                return ApfloatMath.fmod(vars.get(0), vars.get(1));
            case "frac":
                if (vars.size() != 1) throwException();
                return ApfloatMath.frac(vars.get(0));
            case "gamma":
                if (vars.size() > 3) throwException();
                switch (vars.size()) {
                    case 1:
                        return ApfloatMath.gamma(vars.get(0));
                    case 2:
                        return ApfloatMath.gamma(vars.get(0), vars.get(1));
                    case 3:
                        return ApfloatMath.gamma(vars.get(0), vars.get(1), vars.get(2));
                }
            case "inverseRoot":
                if (vars.size() != 1) throwException();
                return ApfloatMath.inverseRoot(vars.get(0), vars.get(1).longValue());
            case "log":
                if (vars.size() != 1) throwException();
                return ApfloatMath.log(vars.get(0), vars.get(1));
            case "min":
                if (vars.size() != 2) throwException();
                return ApfloatMath.min(vars.get(0), vars.get(1));
            case "max":
                if (vars.size() != 2) throwException();
                return ApfloatMath.max(vars.get(0), vars.get(1));
            case "pow":
                if (vars.size() != 2) throwException();
                try {
                    return ApfloatMath.pow(vars.get(0), vars.get(1));
                } catch (ArithmeticException e) {
                    return ApfloatMath.pow(vars.get(0), vars.get(1).longValue());
                }
            case "random":
                if (vars.size() != 2) throwException();
                return new Apfloat(Math.random() * (vars.get(1).longValue() - vars.get(0).longValue()) + vars.get(0).longValue());
            case "root":
                if (vars.size() != 2) throwException();
                return ApfloatMath.root(vars.get(0), vars.get(1).longValue());
            case "round":
                if (vars.size() != 2) throwException();
                return ApfloatMath.round(vars.get(0), vars.get(1).longValue(), RoundingMode.valueOf(BigDecimal.ROUND_HALF_EVEN));
            case "sin":
                if (vars.size() != 1) throwException();
                return ApfloatMath.sin(vars.get(0));
            case "sinh":
                if (vars.size() != 1) throwException();
                return ApfloatMath.sinh(vars.get(0));
            case "sqrt":
                if (vars.size() != 1) throwException();
                return ApfloatMath.sqrt(vars.get(0));
            case "tan":
                if (vars.size() != 1) throwException();
                return ApfloatMath.tan(vars.get(0));
            case "tanh":
                if (vars.size() != 1) throwException();
                return ApfloatMath.tanh(vars.get(0));
            case "ulp":
                if (vars.size() != 1) throwException();
                return ApfloatMath.ulp(vars.get(0));
            case "w":
                if (vars.size() != 1) throwException();
                return ApfloatMath.w(vars.get(0));
            default:
                throwException();

        }
        return null;
    }

    private static void throwException() throws ParseException {
        throw new ParseException("parse error", 1);
    }
}

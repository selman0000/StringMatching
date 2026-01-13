//Selman AKSU  21050111015
//Yusif Jabbarzade 21050141026
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Naive extends Solution {
    static {
        SUBCLASSES.add(Naive.class);
        System.out.println("Naive registered");
    }

    public Naive() {
    }

    @Override
    public String Solve(String text, String pattern) {
        List<Integer> indices = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();

        for (int i = 0; i <= n - m; i++) {
            int j;
            for (j = 0; j < m; j++) {
                if (text.charAt(i + j) != pattern.charAt(j)) {
                    break;
                }
            }
            if (j == m) {
                indices.add(i);
            }
        }

        return indicesToString(indices);
    }
}

class KMP extends Solution {
    static {
        SUBCLASSES.add(KMP.class);
        System.out.println("KMP registered");
    }

    public KMP() {
    }

    @Override
    public String Solve(String text, String pattern) {
        List<Integer> indices = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();

        if (m == 0) {
            for (int i = 0; i <= n; i++) {
                indices.add(i);
            }
            return indicesToString(indices);
        }

        int[] lps = computeLPS(pattern);
        int i = 0;
        int j = 0;

        while (i < n) {
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
            }

            if (j == m) {
                indices.add(i - j);
                j = lps[j - 1];
            } else if (i < n && text.charAt(i) != pattern.charAt(j)) {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }

        return indicesToString(indices);
    }

    private int[] computeLPS(String pattern) {
        int m = pattern.length();
        int[] lps = new int[m];
        int len = 0;
        int i = 1;

        lps[0] = 0;

        while (i < m) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }

        return lps;
    }
}

class RabinKarp extends Solution {
    static {
        SUBCLASSES.add(RabinKarp.class);
        System.out.println("RabinKarp registered.");
    }

    public RabinKarp() {
    }

    private static final int PRIME = 101;

    @Override
    public String Solve(String text, String pattern) {
        List<Integer> indices = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();

        if (m == 0) {
            for (int i = 0; i <= n; i++) {
                indices.add(i);
            }
            return indicesToString(indices);
        }

        if (m > n) {
            return "";
        }

        int d = 256;
        long patternHash = 0;
        long textHash = 0;
        long h = 1;

        for (int i = 0; i < m - 1; i++) {
            h = (h * d) % PRIME;
        }

        for (int i = 0; i < m; i++) {
            patternHash = (d * patternHash + pattern.charAt(i)) % PRIME;
            textHash = (d * textHash + text.charAt(i)) % PRIME;
        }

        for (int i = 0; i <= n - m; i++) {
            if (patternHash == textHash) {
                boolean match = true;
                for (int j = 0; j < m; j++) {
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    indices.add(i);
                }
            }

            if (i < n - m) {
                textHash = (d * (textHash - text.charAt(i) * h) + text.charAt(i + m)) % PRIME;

                if (textHash < 0) {
                    textHash = textHash + PRIME;
                }
            }
        }

        return indicesToString(indices);
    }
}

/**
 * Boyer-Moore String Matching Algorithm
 * Implements bad character rule for efficient pattern matching
 */
class BoyerMoore extends Solution {
    static {
        SUBCLASSES.add(BoyerMoore.class);
        System.out.println("BoyerMoore registered");
    }

    public BoyerMoore() {
    }

    @Override
    public String Solve(String text, String pattern) {
        List<Integer> indices = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();

        if (m == 0) {
            for (int i = 0; i <= n; i++) {
                indices.add(i);
            }
            return indicesToString(indices);
        }

        if (m > n) {
            return "";
        }

        Map<Character, Integer> badChar = buildBadCharacterTable(pattern);

        int shift = 0;

        while (shift <= n - m) {
            int j = m - 1;

            while (j >= 0 && pattern.charAt(j) == text.charAt(shift + j)) {
                j--;
            }

            if (j < 0) {
                indices.add(shift);

                if (shift + m < n) {
                    char nextChar = text.charAt(shift + m);
                    shift += m - badChar.getOrDefault(nextChar, -1) - 1;
                } else {
                    shift += 1;
                }
            } else {
                char mismatchChar = text.charAt(shift + j);
                int badCharShift = j - badChar.getOrDefault(mismatchChar, -1);
                shift += Math.max(1, badCharShift);
            }
        }

        return indicesToString(indices);
    }

    private Map<Character, Integer> buildBadCharacterTable(String pattern) {
        Map<Character, Integer> badChar = new HashMap<>();
        int m = pattern.length();

        for (int i = 0; i < m; i++) {
            badChar.put(pattern.charAt(i), i);
        }

        return badChar;
    }
}

/**
 * Adaptive Hybrid String Matching Algorithm
 * Selects strategy based on pattern length for optimal performance
 */
class GoCrazy extends Solution {
    static {
        SUBCLASSES.add(GoCrazy.class);
        System.out.println("GoCrazy registered");
    }

    public GoCrazy() {
    }

    @Override
    public String Solve(String text, String pattern) {
        List<Integer> indices = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();

        if (m == 0) {
            for (int i = 0; i <= n; i++) {
                indices.add(i);
            }
            return indicesToString(indices);
        }

        if (m > n) {
            return "";
        }

        if (m <= 3) {
            return solveWithOptimizedNaive(text, pattern, indices);
        } else if (m <= 10) {
            return solveWithHybrid(text, pattern, indices);
        } else {
            return solveWithBoyerMooreLite(text, pattern, indices);
        }
    }

    private String solveWithOptimizedNaive(String text, String pattern, List<Integer> indices) {
        int n = text.length();
        int m = pattern.length();
        char firstChar = pattern.charAt(0);

        for (int i = 0; i <= n - m; i++) {
            if (text.charAt(i) != firstChar) {
                continue;
            }

            boolean match = true;
            for (int j = 1; j < m; j++) {
                if (text.charAt(i + j) != pattern.charAt(j)) {
                    match = false;
                    break;
                }
            }

            if (match) {
                indices.add(i);
            }
        }

        return indicesToString(indices);
    }

    private String solveWithHybrid(String text, String pattern, List<Integer> indices) {
        int n = text.length();
        int m = pattern.length();
        char firstChar = pattern.charAt(0);
        char lastChar = pattern.charAt(m - 1);

        for (int i = 0; i <= n - m; i++) {
            if (text.charAt(i) != firstChar || text.charAt(i + m - 1) != lastChar) {
                continue;
            }

            boolean match = true;
            for (int j = 1; j < m - 1; j++) {
                if (text.charAt(i + j) != pattern.charAt(j)) {
                    match = false;
                    break;
                }
            }

            if (match) {
                indices.add(i);
            }
        }

        return indicesToString(indices);
    }

    private String solveWithBoyerMooreLite(String text, String pattern, List<Integer> indices) {
        int n = text.length();
        int m = pattern.length();

        Map<Character, Integer> lastOccurrence = new HashMap<>();
        for (int i = 0; i < m - 1; i++) {
            lastOccurrence.put(pattern.charAt(i), i);
        }

        int shift = 0;
        while (shift <= n - m) {
            int j = m - 1;

            while (j >= 0 && pattern.charAt(j) == text.charAt(shift + j)) {
                j--;
            }

            if (j < 0) {
                indices.add(shift);
                shift += (shift + m < n) ? m - lastOccurrence.getOrDefault(text.charAt(shift + m), -1) - 1 : 1;
            } else {
                char mismatchChar = text.charAt(shift + j);
                int lastOcc = lastOccurrence.getOrDefault(mismatchChar, -1);
                shift += Math.max(1, j - lastOcc);
            }
        }

        return indicesToString(indices);
    }
}
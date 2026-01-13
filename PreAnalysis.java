//Selman AKSU  21050111015
//Yusif Jabbarzade 21050141026

/**
 * PreAnalysis interface for students to implement their algorithm selection logic
 * 
 * Students should analyze the characteristics of the text and pattern to determine
 * which algorithm would be most efficient for the given input.
 * 
 * The system will automatically use this analysis if the chooseAlgorithm method
 * returns a non-null value.
 */
public abstract class PreAnalysis {
    
    /**
     * Analyze the text and pattern to choose the best algorithm
     * 
     * @param text The text to search in
     * @param pattern The pattern to search for
     * @return The name of the algorithm to use (e.g., "Naive", "KMP", "RabinKarp", "BoyerMoore", "GoCrazy")
     *         Return null if you want to skip pre-analysis and run all algorithms
     * 
     * Tips for students:
     * - Consider the length of the text and pattern
     * - Consider the characteristics of the pattern (repeating characters, etc.)
     * - Consider the alphabet size
     * - Think about which algorithm performs best in different scenarios
     */
    public abstract String chooseAlgorithm(String text, String pattern);
    
    /**
     * Get a description of your analysis strategy
     * This will be displayed in the output
     */
    public abstract String getStrategyDescription();
}


/**
 * Default implementation that students should modify
 * This is where students write their pre-analysis logic
 */
class StudentPreAnalysis extends PreAnalysis {

    @Override
    public String chooseAlgorithm(String text, String pattern) {
        int textLen = text.length();
        int patternLen = pattern.length();

        if (patternLen > textLen) {
            return "Naive";
        }

        if (patternLen == 0) {
            return "Naive";
        }

        if (patternLen <= 2) {
            return "Naive";
        }

        if (patternLen <= 4 && textLen < 100) {
            return "Naive";
        }

        double uniqueRatio = calculateUniqueCharacterRatio(pattern);
        boolean hasRepeatingPrefix = hasRepeatingPrefix(pattern);
        boolean hasHighRepetition = hasHighCharacterRepetition(pattern);

        if (hasRepeatingPrefix) {
            return "KMP";
        }

        if (hasHighRepetition && patternLen >= 5) {
            return "KMP";
        }

        if (patternLen >= 15 && textLen > 1000) {
            return "RabinKarp";
        }

        if (patternLen >= 5 && uniqueRatio > 0.6) {
            return "BoyerMoore";
        }

        if (patternLen >= 8 && textLen >= 100) {
            return "BoyerMoore";
        }

        if (patternLen >= 5 && patternLen <= 20) {
            return "BoyerMoore";
        }

        if (patternLen > 20) {
            return "RabinKarp";
        }

        return "BoyerMoore";
    }

    private double calculateUniqueCharacterRatio(String pattern) {
        if (pattern.length() == 0) return 0.0;

        Set<Character> uniqueChars = new HashSet<>();
        for (char c : pattern.toCharArray()) {
            uniqueChars.add(c);
        }

        return (double) uniqueChars.size() / pattern.length();
    }

    private boolean hasRepeatingPrefix(String pattern) {
        if (pattern.length() < 3) return false;

        char firstChar = pattern.charAt(0);
        int firstCharCount = 0;

        for (int i = 0; i < Math.min(pattern.length(), 5); i++) {
            if (pattern.charAt(i) == firstChar) {
                firstCharCount++;
            }
        }

        if (firstCharCount >= 3) {
            return true;
        }

        if (pattern.length() >= 4) {
            String prefix = pattern.substring(0, 2);
            if (pattern.substring(2, 4).equals(prefix)) {
                return true;
            }
        }

        return false;
    }

    private boolean hasHighCharacterRepetition(String pattern) {
        if (pattern.length() < 4) return false;

        Map<Character, Integer> charCount = new HashMap<>();
        for (char c : pattern.toCharArray()) {
            charCount.put(c, charCount.getOrDefault(c, 0) + 1);
        }

        int maxCount = 0;
        for (int count : charCount.values()) {
            maxCount = Math.max(maxCount, count);
        }

        double repetitionRatio = (double) maxCount / pattern.length();

        return repetitionRatio > 0.5;
    }

    @Override
    public String getStrategyDescription() {
        return "Hybrid strategy analyzing pattern length, character diversity, " +
                "repeating prefix, and character repetition to select optimal algorithm";
    }
}


class ExamplePreAnalysis extends PreAnalysis {

    @Override
    public String chooseAlgorithm(String text, String pattern) {
        int textLen = text.length();
        int patternLen = pattern.length();

        if (patternLen <= 3) {
            return "Naive";
        } else if (hasRepeatingPrefix(pattern)) {
            return "KMP";
        } else if (patternLen > 10 && textLen > 1000) {
            return "RabinKarp";
        } else {
            return "Naive";
        }
    }

    private boolean hasRepeatingPrefix(String pattern) {
        if (pattern.length() < 2) return false;

        char first = pattern.charAt(0);
        int count = 0;
        for (int i = 0; i < Math.min(pattern.length(), 5); i++) {
            if (pattern.charAt(i) == first) count++;
        }
        return count >= 3;
    }

    @Override
    public String getStrategyDescription() {
        return "Example strategy: Choose based on pattern length and characteristics";
    }
}

class InstructorPreAnalysis extends PreAnalysis {

    @Override
    public String chooseAlgorithm(String text, String pattern) {
        return null;
    }

    @Override
    public String getStrategyDescription() {
        return "Instructor's testing implementation";
    }
}
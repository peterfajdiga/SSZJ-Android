package peterfajdiga.sszj;

public interface Words {
    String[] getWordsStartingWith(String prefix);  // ignores case
    boolean isValidWord(String word);  // ignores case
    String getCorrectCase(String word);
}

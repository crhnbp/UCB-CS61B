public class Palindrome {
    public static Deque<Character> wordToDeque(String word){
        ArrayDeque<Character> dword = new ArrayDeque<>();
        for (int i = 0; i < word.length(); i++){
            char wChar = word.charAt(i);
            dword.addLast(wChar);
        }
        return dword;
    }

    public static boolean isPalindrome(String word){
        if (word.length() <= 1){
            return true;
        }
        else if (word.charAt(0) != word.charAt(word.length() - 1)){
            return false;
        }
        return isPalindrome(word.substring(1,word.length() - 1));
    }

    public static boolean isPalindrome(String word, CharacterComparator cc){
        if (word.length() <= 1){
            return true;
        }
        else if (!cc.equalChars(word.charAt(0), word.charAt(word.length() - 1))){
            return false;
        }
        return isPalindrome(word.substring(1,word.length() - 1), cc);
    }

}

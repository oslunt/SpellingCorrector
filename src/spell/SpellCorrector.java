package spell;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SpellCorrector implements ISpellCorrector{

    private ITrie dictionary;
    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        //Take in file name and put it into a scanner in order to parse out words

        File file = new File(dictionaryFileName);
        Scanner scanner = new Scanner(file);

        dictionary = new Trie();

        //Parse through the lists and add the word and its frequency to a map

        while(scanner.hasNext()) {
            String wordPlaceHolder = scanner.next().toLowerCase();
            dictionary.add(wordPlaceHolder);
        }
    }

    @Override
    public String suggestSimilarWord(String inputWord) {
        inputWord = inputWord.toLowerCase();

        //Check to see if the word is even misspelled
        if(dictionary.find(inputWord) != null) {
            return inputWord;
        }
        //Create all possible words based off inputWord

        Set<String> firstPossibleWords = getAllPossibleWords(inputWord);

        String chosenWord = findChosenWord(firstPossibleWords, "");

        //Return if a chosen word is found without going through 2 edit list
        if(chosenWord.length() > 0) {
            return chosenWord;
        }

        //Run again but with 2 edits

        Set<String> secondPossibleWords = new HashSet<>();

        for(String selectedWord : firstPossibleWords) {
            Set<String> selectedWordsPossible = getAllPossibleWords(selectedWord);
            secondPossibleWords.addAll(selectedWordsPossible);
        }

        chosenWord = findChosenWord(secondPossibleWords, "");

        //Return if a chosen word is found
        if(chosenWord.length() > 0) {
            return chosenWord;
        }

        return null;
    }

    public Set<String> getAllPossibleWords(String inputWord) {
        Set<String> possibleWords = new HashSet<>();
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();

        //Add Deletion words

        for(int i = 0; i < inputWord.length(); i++) {
            StringBuilder placeholder = new StringBuilder(inputWord);
            possibleWords.add(placeholder.deleteCharAt(i).toString());
        }

        //Add Transposition words

        for(int i = 0; i < inputWord.length() - 1; i++) {
            StringBuilder placeholder = new StringBuilder(inputWord);
            char charToSwitch = placeholder.charAt(i);
            placeholder.setCharAt(i, placeholder.charAt(i + 1));
            placeholder.setCharAt(i + 1, charToSwitch);
            possibleWords.add(placeholder.toString());
        }

        //Add Alteration words

        for(int i = 0; i < inputWord.length(); i++) {
            StringBuilder placeholder = new StringBuilder(inputWord);
            for(int j = 0; j < alphabet.length; j++) {
                placeholder.setCharAt(i, alphabet[j]);
                possibleWords.add(placeholder.toString());
            }
        }

        //Add Insertion Distance
        for(int i = 0; i < inputWord.length() + 1; i++) {
            for(int j = 0; j < alphabet.length; j++) {
                StringBuilder placeholder = new StringBuilder(inputWord);
                if(i < inputWord.length()) {
                    placeholder.insert(i, alphabet[j]);
                }
                else {
                    placeholder.append(alphabet[j]);
                }
                possibleWords.add(placeholder.toString());
            }
        }

        return possibleWords;
    }

    public String findChosenWord(Set<String> possibleWords, String chosenWord) {
        int chosenWordFrequency = 0;

        //Compare wordlist to dictionary

        for(String selectedWord : possibleWords) {
            if(dictionary.find(selectedWord) != null) {
                if(dictionary.find(selectedWord).getValue() == chosenWordFrequency) {
                    if(selectedWord.compareTo(chosenWord) < 0) {
                        chosenWord = selectedWord;
                    }
                }
                else if(dictionary.find(selectedWord).getValue() > chosenWordFrequency) {
                    chosenWord = selectedWord;
                    chosenWordFrequency = dictionary.find(selectedWord).getValue();
                }
            }
        }

        return chosenWord;
    }
}

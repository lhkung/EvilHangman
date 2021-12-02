import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeSet;

public class Hangman {

	private ArrayList<String> wordList;
	private HashSet<Character> previousGuesses;
	private TreeSet<Character> incorrectGuesses; // behaves like a hash set, but orders the entries!
	private Solution solution;
	private Scanner inputScanner;

	public Hangman() {
		this("engDictionary.txt");
	}
	

	public Hangman(String filename) {
		try {
			wordList = dictionaryToList(filename);
		} catch (IOException e) {
			System.out.printf(
					"Couldn't read from the file %s. Verify that you have it in the right place and try running again.",
					filename);
			e.printStackTrace();
			System.exit(0); // stop the program--no point in trying if you don't have a dictionary
		}

		previousGuesses = new HashSet<>();
		incorrectGuesses = new TreeSet<>();
		
		int maxWordLengthInDic = maxWordLength(wordList);
		ArrayList<String> targets = new ArrayList<>();
		while (targets.isEmpty()) {
			int randomLength = new Random().nextInt(maxWordLengthInDic + 1); // get a random word length
																		// return all words of that length
			targets = allWordsOfSize(wordList, randomLength);
		}
		solution = new Solution(targets);	
		inputScanner = new Scanner(System.in);
	}

	private ArrayList<String> allWordsOfSize(ArrayList<String> wordList, int randomLength) {
		ArrayList<String> targetWordsList = new ArrayList<String>();
		for (int i = 0; i < wordList.size(); i++) {
			if (wordList.get(i).length() == randomLength) {
				targetWordsList.add(wordList.get(i));
			}
		}
		return targetWordsList;
	}

	private int maxWordLength(ArrayList<String> wordList) {
		int maxLength = wordList.get(0).length();
		for (int i = 0; i < wordList.size(); i++) {
			if (wordList.get(i).length() > maxLength) {
				maxLength = wordList.get(i).length();
			}
		}
		return maxLength;
	}

	/*
	 * keep asking the user for to guess a letter and record that letter while the
	 * word is not fully guessed, print success otherwise this method can stay as is
	 */
	public void start() {
		while (!solution.isSolved()) {
			char guess = promptForGuess();
			recordGuess(guess);
		}
		printVictory();
	}

	// this method can stay as is
	private char promptForGuess() {
		while (true) {
			System.out.println("Guess a letter.\n");
			solution.printProgress();
			System.out.println("Incorrect guesses:\n" + incorrectGuesses.toString());
			String input = inputScanner.next();
			if (input.length() != 1) {
				System.out.println("Please enter a single character.");
			} else if (previousGuesses.contains(input.charAt(0))) {
				System.out.println("You've already guessed that.");
			} else {
				return input.charAt(0);
			}
		}

	}

	// this method will have to change to check against all words in the hashmap
	private void recordGuess(char guess) {
		previousGuesses.add(guess);
		boolean isCorrect = solution.addGuess(guess);
		if (!isCorrect) {
			incorrectGuesses.add(guess);
		}
	}

	private void printVictory() {
		System.out.printf("Congrats! The word was %s%n", solution.getTarget());
	}

	// this method can stay as is
	private static ArrayList<String> dictionaryToList(String filename) throws IOException {
		FileInputStream fs = new FileInputStream(filename);
		Scanner scnr = new Scanner(fs);

		ArrayList<String> wordList = new ArrayList<>();

		while (scnr.hasNext()) {
			wordList.add(scnr.next());
		}

		return wordList;
	}

}

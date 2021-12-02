import java.util.ArrayList;
import java.util.HashMap;

public class Solution {

	private ArrayList<String> targetWordList; // will have to change to HashMap
	private ArrayList<Character> partialSolution; // for one word (the best match to print to output)
	private HashMap<ArrayList<Character>, ArrayList<String>> wordFamilies;
	// private int missingChars;

	// constructor to initialize the string once a random word is picked
	public Solution(ArrayList<String> targets) {
		wordFamilies = new HashMap<>();
		this.targetWordList = targets;

		// missingChars = target.length();

		partialSolution = new ArrayList<>();
		for (int i = 0; i < targetWordList.get(0).length(); i++) {
			partialSolution.add('_');
		}
	}

	// exit loop once all characters are guessed correctly
	public boolean isSolved() {
		int missingChars = 0;
		for (int i = 0; i < partialSolution.size(); i++){
			if (partialSolution.get(i) == '_'){
				missingChars++;
			}
		}
		return missingChars == 0;
	}

	// prints out the current state of the game
	public void printProgress() {
		for (char c : partialSolution) {
			System.out.print(c + " ");
		}
		System.out.println();
	}

	// checks if the guess is correct and inserts letter in all matching positions
	public boolean addGuess(char guess) {
		boolean guessCorrect = false;
		for (int i = 0; i < this.targetWordList.size(); i++) {
			ArrayList<Character> charPosition = new ArrayList<>();
			for (int k = 0; k < partialSolution.size(); k++) {
				charPosition.add(partialSolution.get(k));
			}
			
			for (int j = 0; j < this.targetWordList.get(i).length(); j++) {
				if (targetWordList.get(i).charAt(j) == guess) {
					charPosition.set(j, guess); // replaces '_' with the guess char
					//guessCorrect = true;
				}
				// missingChars--;
			}
			if (wordFamilies.containsKey(charPosition)) {
				wordFamilies.get(charPosition).add(targetWordList.get(i));
			} else {
				ArrayList<String> temp = new ArrayList<String>();
				temp.add(targetWordList.get(i));
				wordFamilies.put(charPosition, temp);
			}
		}
		ArrayList<Character> partialSolutionKey = getLongestWordFamily(wordFamilies);
		targetWordList = wordFamilies.get(partialSolutionKey);
		//System.out.println(wordFamilies.get(partialSolutionKey));
		partialSolution = partialSolutionKey;
		wordFamilies.clear();
		for (int i = 0; i < partialSolution.size(); i++) {
			if (partialSolution.get(i) == guess) {
				guessCorrect = true;
			}
		}
		
		return guessCorrect;
	}

	public ArrayList<Character> getLongestWordFamily(HashMap<ArrayList<Character>, ArrayList<String>> wordFamilies) {
		ArrayList<Character> longestFamilyKey = new ArrayList<>();
		for (ArrayList<Character> key1 : wordFamilies.keySet()) {
			longestFamilyKey = key1;
			break;
		}
		for (ArrayList<Character> key : wordFamilies.keySet()) {
			if (wordFamilies.get(key).size() > wordFamilies.get(longestFamilyKey).size()) {
				longestFamilyKey = key;
			}
		}
		return longestFamilyKey;
	}

	// returns the original string
	public String getTarget() {
		String ret = "";
		for (int i = 0; i < partialSolution.size(); i ++) {
			ret += partialSolution.get(i); 
		}
		return ret;
	}

}

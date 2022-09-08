package spell;

public class Trie implements ITrie{

    private INode root;
    private int wordCount;
    private int nodeCount;

    public Trie() {
        this.root = new Node();
        this.wordCount = 0;
        this.nodeCount = 1;
    }

    /**
     * Adds the specified word to the trie (if necessary) and increments the word's frequency count.
     *
     * @param word the word being added to the trie
     */
    @Override
    public void add(String word) {
        String addWord = word;

        //Calling recursive function to add the word

        addRecursive(addWord, root);
    }

    public void addRecursive(String word, INode cur) {

        //Adds letter by letter checking to see if the length of the word that is left is greater than 0
        //because if it is zero we know that we have added the word and just need to increment the counter

        if(word.length() > 0) {
            if(cur.getChildren()[word.charAt(0) - 'a'] == null) {

                //Everytime a new node is added nodeCount is incremented
                //Also calls the function recursively to add the next letter in the word

                this.nodeCount++;
                cur.getChildren()[word.charAt(0) - 'a'] = new Node();
                addRecursive(word.substring(1), cur.getChildren()[word.charAt(0) - 'a']);
            }
            else {

                //If there is already a node we don't need to reinitialize it and can just call the function

                addRecursive(word.substring(1), cur.getChildren()[word.charAt(0) - 'a']);
            }
        }
        else {

            //Checks to see if it is a unique word by making sure the count is still 0
            //If it is, it then increments wordCount and then the value of the node

            if(cur.getValue() < 1) {
                this.wordCount++;
            }
            cur.incrementValue();
        }
    }

    /**
     * Searches the trie for the specified word.
     *
     * @param word the word being searched for.
     * @return a reference to the trie node that represents the word,
     * or null if the word is not in the trie
     */
    @Override
    public INode find(String word) {
        String findWord = word;

        //Calling recursive function to find the word

        return findRecursive(word, root);
    }

    public INode findRecursive(String word, INode cur) {

        //Checks to first see if the word length is 0 which means we have finished finding the end node

        if(word.length() > 0) {

            //Checks to see if the node is initialized and if not then we know that the word is not there
            //Else we call the function recursively on the rest of the word

            if(cur.getChildren()[word.charAt(0) - 'a'] == null) {
                return null;
            }
            else {
                return findRecursive(word.substring(1), cur.getChildren()[word.charAt(0) - 'a']);
            }
        }
        else {

            //Checks to see if the value of the node is greater than 0, meaning the word is there

            if(cur.getValue() > 0) {
                return cur;
            }
            else {
                return null;
            }
        }
    }

    /**
     * Returns the number of unique words in the trie.
     *
     * @return the number of unique words in the trie
     */
    @Override
    public int getWordCount() {
        return wordCount;
    }

    /**
     * Returns the number of nodes in the trie.
     *
     * @return the number of nodes in the trie
     */
    @Override
    public int getNodeCount() {
        return nodeCount;
    }

    /**
     * The toString specification is as follows:
     * For each word, in alphabetical order:
     * <word>\n
     * MUST BE RECURSIVE.
     */
    @Override
    public String toString() {
        return toStringRecursive(root, "", "");
    }

    public String toStringRecursive(INode cur, String curString, String finalString) {

        //Checks to see if the current letter we are on is the end character of a word and if so we add
        //that to the final string of the trie

        if(cur.getValue() > 0) {
            finalString += curString + "\n";
        }

        //Iterating through each node in the tree by calling the function recursively

        for(int i = 0; i < cur.getChildren().length; i++) {
            if(cur.getChildren()[i] != null) {
                char curChar = (char)(i + 'a');
                finalString = toStringRecursive(cur.getChildren()[i], curString + curChar, finalString);
            }
        }
        return finalString;
    }

    /**
     * Returns the hashcode of this trie.
     * MUST be constant time.
     *
     * @return a uniform, deterministic identifier for this trie.
     */
    @Override
    public int hashCode() {

        //Getting the index of the first non null value in the array

        int index = 0;
        while(root.getChildren()[index] == null && index < root.getChildren().length) {
            index++;
        }

        return (wordCount * nodeCount * index);
    }

    /**
     * Checks if an object is equal to this trie.
     * MUST be recursive.
     *
     * @param o Object to be compared against this trie
     * @return true if o is a Trie with same structure and node count for each node
     * false otherwise
     */
    @Override
    public boolean equals(Object o) {

        //Preliminary checks just to see if we don't need to do any recursion

        if(o == null) {
            return false;
        }
        if(o.getClass() != Trie.class) {
            return false;
        }
        if(o == this.root) {
            return true;
        }

        Trie t = (Trie)o;

        //Calling recursive function to check equality

        return equalsRecursive(this.root, t.root);
    }

    public boolean equalsRecursive(INode original, INode toCompare) {

        //This first checks to see if both of the tries are null and if so they are equal
        //Then we know if just one of them is null it will automatically return false

        if(original == null && toCompare == null) {
            return true;
        }
        else if(original == null) {
            return false;
        }
        else if(toCompare == null) {
            return false;
        }

        //We then check to see if they both have the same values and if we do we iterate over both
        //tries at the same time calling this same function recursively
        //If any of them return false then we know that the tries are not equal and can return false
        //If we make it through all of that we know the tries are equal

        if(original.getValue() == toCompare.getValue() && original != null && toCompare != null) {
            for (int i = 0; i < original.getChildren().length; i++) {
                if(!equalsRecursive(original.getChildren()[i], toCompare.getChildren()[i])) {
                    return false;
                }
            }
            return true;
        }
        else {
            return false;
        }
    }
}

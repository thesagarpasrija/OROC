package com.word2vec.SagarProjectWord2Vec;

/*
 * This class was not used.
 * 
 * googleW2V class aimed at using the pre-trained Google's news vector wordvectors model to calculate similarity between different materials
 * and shapes. 
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.aliasi.sentences.MedlineSentenceModel;
import com.aliasi.sentences.SentenceModel;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.Tokenizer;
import com.aliasi.tokenizer.TokenizerFactory;
import com.aliasi.util.Files;
import com.sun.accessibility.internal.resources.accessibility;

public class googleW2V {
private static int minimum(int a, int b, int c) {
    return Math.min(Math.min(a, b), c);
}


/*
 * This utility method computes Levenshtein Distance between two strings.
 * 
 * @param str1
 * @param str2
 */
public static int computeDistance(CharSequence str1, CharSequence str2) {
    int[][] distance = new int[str1.length() + 1][str2.length() + 1];
    for (int i = 0; i <= str1.length(); i++) {
        distance[i][0] = i;
    }
    for (int j = 0; j <= str2.length(); j++) {
        distance[0][j] = j;
    }
    for (int i = 1; i <= str1.length(); i++) {
        for (int j = 1; j <= str2.length(); j++) {
            distance[i][j] = minimum(
                    distance[i - 1][j] + 1,
                    distance[i][j - 1] + 1,
                    distance[i - 1][j - 1]
                            + ((str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0
                                    : 1));
        }
    }
    int result = distance[str1.length()][str2.length()];

    return result;
}

static final TokenizerFactory TOKENIZER_FACTORY = IndoEuropeanTokenizerFactory.INSTANCE;
static final SentenceModel SENTENCE_MODEL = new MedlineSentenceModel();


public static void main(String[] args) {
    try {
        ArrayList<String> sentences = null;
        sentences = new ArrayList<String>();
        // Reading from text file
        // sentences = readSentencesInFile("D:\\sam.txt");

        // Giving sentences
        // ArrayList<String> sentences = new ArrayList<String>();
        
        // calculating dummy similarity values
        sentences.add("Mary and Meera are my classmates.");
        sentences.add("Mary and Meera are my classmates.");
        sentences.add("Meera and Mary are my classmates.");
        sentences.add("Alice and Bobe are not my classmates.");
        sentences.add("Some totally different sentence.");
        // Self-implemented
        wordAnalyser(sentences);
        // Internet referred
        // levenshteinDistance(sentences);
    } catch (Exception e) {
        // TODO: handle exception
        e.printStackTrace();
    }
}

/*
 * This method reads in sentences from an input file and returns a list of sentences.
 * 
 * @param path path to the input file
 */
private static ArrayList<String> readSentencesInFile(String path) {
    ArrayList<String> sentencesList = new ArrayList<String>();

    try {
        System.out.println("Reading file from : " + path);
        File file = new File(path);
        String text = Files.readFromFile(file, "ISO-8859-1");
        System.out.println("INPUT TEXT: ");
        System.out.println(text);

        List<String> tokenList = new ArrayList<String>();
        List<String> whiteList = new ArrayList<String>();
        Tokenizer tokenizer = TOKENIZER_FACTORY.tokenizer(
                text.toCharArray(), 0, text.length());
        tokenizer.tokenize(tokenList, whiteList);

        System.out.println(tokenList.size() + " TOKENS");
        System.out.println(whiteList.size() + " WHITESPACES");

        String[] tokens = new String[tokenList.size()];
        String[] whites = new String[whiteList.size()];
        tokenList.toArray(tokens);
        whiteList.toArray(whites);
        int[] sentenceBoundaries = SENTENCE_MODEL.boundaryIndices(tokens,
                whites);

        System.out.println(sentenceBoundaries.length
                + " SENTENCE END TOKEN OFFSETS");

        if (sentenceBoundaries.length < 1) {
            System.out.println("No sentence boundaries found.");
            return new ArrayList<String>();
        }
        int sentStartTok = 0;
        int sentEndTok = 0;
        for (int i = 0; i < sentenceBoundaries.length; ++i) {
            sentEndTok = sentenceBoundaries[i];
            System.out.println("SENTENCE " + (i + 1) + ": ");
            StringBuffer sentenceString = new StringBuffer();
            for (int j = sentStartTok; j <= sentEndTok; j++) {
                sentenceString.append(tokens[j] + whites[j + 1]);
            }
            System.out.println(sentenceString.toString());
            sentencesList.add(sentenceString.toString());
            sentStartTok = sentEndTok + 1;
        }
    } catch (IOException e) {
        // TODO: handle exception
        e.printStackTrace();
    }

    return sentencesList;
}


/*
 * Method to calculate Levenshtein Distance.
 */
private static void levenshteinDistance(ArrayList<String> sentences) {
    System.out.println("\nLevenshteinDistance");
    for (int i = 0; i < sentences.size(); i++) {
        System.out.println("Distance between \n'" + sentences.get(0)
                + "' \nand '" + sentences.get(i) + "': \n"
                + computeDistance(sentences.get(0), 
sentences.get(i)));
    }
}


/*
 * This method analyzes words in input sentences by counting the number of sentences, words in sentences, white spaces. 
 * It then compares words of sentences and calculates the percentage of similarity between two sentences.
 */
private static void wordAnalyser(ArrayList<String> sentences) {

    System.out.println("No.of Sentences : " + sentences.size());
    List<String> stopWordsList = getStopWords();
    List<String> tokenList = new ArrayList<String>();
    ArrayList<List<String>> filteredSentences = new ArrayList<List<String>>();

    for (int i = 0; i < sentences.size(); i++) {
        tokenList = new ArrayList<String>();
        List<String> whiteList = new ArrayList<String>();
        Tokenizer tokenizer = TOKENIZER_FACTORY.tokenizer(sentences.get(i)
                .toCharArray(), 0, sentences.get(i).length());
        tokenizer.tokenize(tokenList, whiteList);

        System.out.print("Sentence " + (i + 1) + ": " + tokenList.size()
                + " TOKENS, ");
        System.out.println(whiteList.size() + " WHITESPACES");

        filteredSentences.add(filterStopWords(tokenList, stopWordsList));
    }

    for (int i = 0; i < sentences.size(); i++) {
        System.out.println("\n" + (i + 1) + ". Comparing\n      '"
                + sentences.get(0) + "' \nwith\n     '" +         
sentences.get(i)
                + "' : \n");
        System.out.println(filteredSentences.get(0) + "\n and \n"
                + filteredSentences.get(i));
        System.out.println("Percentage of similarity: "
                + calculateSimilarity(filteredSentences.get(0),
                        filteredSentences.get(i)) 
+ "%");
    }
}

/*
 * This method calculates percentage similarity between two sentences.
 * 
 * @param list1 first sentence
 * @param list2 second sentence
 */
private static double calculateSimilarity(List<String> list1,
        List<String> list2) {
    int length1 = list1.size();
    int length2 = list2.size();

    int count1 = 0;
    int count2 = 0;
    double result1 = 0.0;
    double result2 = 0.0;
    int least, highest;
    if (length2 > length1) {
        least = length1;
        highest = length2;
    } else {
        least = length2;
        highest = length1;
    }
    // computing result1
    for (String string1 : list1) {
        if (list2.contains(string1))
            count1++;
    }
    result1 = (count1 * 100) / length1;
    // computing result2
    for (String string2 : list2) {
        if (list1.contains(string2))
            count2++;
    }
    result2 = (count2 * 100) / length2;

    double avg = (result1 + result2) / 2;

    return avg;
}


//returns a list of stop words
private static List<String> getStopWords() {
	
	// add stop words to the String stopWordsString separated by ,
    String stopWordsString = ".,a,able,about,across,after,all,almost,also,am,among,an,and,any,are,as,at,be,because,been,but,by,can,cannot,could,dear,did,do,does,either,else,ever,every,for,from,get,got,had,has,have,he,her,hers,him,his,how,however,i,if,in,into,is,it,its,just,least,let,like,likely,may,me,might,most,must,my,neither,no,nor,not,of,off,often,on,only,or,other,our,own,rather,said,say,says,she,should,since,so,some,than,that,the,their,them,then,there,these,they,this,tis,to,too,twas,us,wants,was,we,were,what,when,where,which,while,who,whom,why,will,with,would,yet,you,your";
    List<String> stopWordsList = new ArrayList<String>();
    List<String> stopWordTokenList = new ArrayList<String>();
    List<String> whiteList = new ArrayList<String>();
    Tokenizer tokenizer = TOKENIZER_FACTORY.tokenizer(
            stopWordsString.toCharArray(), 0, stopWordsString.length());
    tokenizer.tokenize(stopWordTokenList, whiteList);
    for (int i = 0; i < stopWordTokenList.size(); i++) {
        // System.out.println((i + 1) + ":" + tokenList.get(i));
        if (!stopWordTokenList.get(i).equals(",")) {
            stopWordsList.add(stopWordTokenList.get(i));
        }
    }
    //System.out.println("No.of stop words: " + stopWordsList.size());
    return stopWordsList;
}

/*
 * This method returns an ArrayList of words after removing stop words from sentences
 * 
 *  @param tokenList ArrayList of sentences of strings
 *  @param stopWordsList List of all the stop words
*/
private static List<String> filterStopWords(List<String> tokenList,
        List<String> stopWordsList) {

    List<String> filteredSentenceWords = new ArrayList<String>();
    for (String sentenceToken : tokenList) {
        if (!stopWordsList.contains(sentenceToken)) {
            filteredSentenceWords.add(sentenceToken);
        }
    }
    return filteredSentenceWords;
}
}

package com.word2vec.SagarProjectWord2Vec;

/*
 * This class was not used.
 * 
 * avgVector class is aimed to calculate average of multiple vectors of same length. It adds to corresponding vector entries and divide the sum by the 
 * total number of vectors added.
 */

import java.io.FileNotFoundException;
import java.util.ArrayList;
import org.deeplearning4j.models.word2vec.Word2Vec;

public class avgVector {
	
	
	/*
	 * this function converts the input sentence into a single-row vector. It uses the pre-trained Word2Vec model to convert a sentence to vector
	 * using word vectors
	 * 
	 * @param sentence the input sentence
	 * @param vec Word2Vec model
	 * @param noOfLayers number of entries in the vector of a single word
	 */
	public ArrayList<Double> calculate(String sentence, Word2Vec vec, int noOfLayers) throws FileNotFoundException{
		String[] wordsOfSentence=sentence.split("\\s+");
		
		ArrayList<Double> returnVector=new ArrayList<>(0);
		
		for(String word:wordsOfSentence) {
			double[] vector=vec.getWordVector(word);
			for(double e:vector)
				returnVector.add(e);
		}
		
		ArrayList<Double> vectorOfSentence=new ArrayList<>();
		int index=-1;
		for(int i=0;i<noOfLayers;i++) {
			index++;
			int index2=index;
			double ans=returnVector.get(index);
			for(int j=0;j<wordsOfSentence.length-1;j++) {
				ans+=returnVector.get(index2+noOfLayers);
				index2+=noOfLayers;
			}
			ans/=wordsOfSentence.length;
			vectorOfSentence.add(ans);
		}

		return vectorOfSentence;	
	}
}

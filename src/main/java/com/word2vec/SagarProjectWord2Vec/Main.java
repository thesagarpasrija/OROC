package com.word2vec.SagarProjectWord2Vec;

/*
 * This class was not used.
 * 
 * This class was the main class for the cosine similarity and Word2Vec models approach.
 */

import java.io.IOException;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.jdom2.JDOMException;

//import com.word2vec.parser.Concept;
//import com.word2vec.parser.XmlParser;
//This may be replaced by a rest service to be accessible by a GUI.

public class Main {

	public static void main(String[] args) {
		BasicConfigurator.configure();
		//googleW2V g=new googleW2V();
		
		// providing path to XML file that is to be parsed.
		XmlParser myParser = new XmlParser("C:/Users/Sagar Pasrija/Dropbox/Ana-Maria_Sagar/Concepts1.xml");
		try {
			List<Concept> myTrainingDataList = myParser.loadTrainingData();
			for(Concept concept: myTrainingDataList) {
				System.out.println("*********Just printing name********");
				System.out.println(concept.getName());
			}
		} catch (JDOMException | IOException e) {
			e.printStackTrace();
		}
		System.out.println("program ended");
	}

}

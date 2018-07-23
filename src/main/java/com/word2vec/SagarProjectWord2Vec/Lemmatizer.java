package com.word2vec.SagarProjectWord2Vec;

/*
 * This class uses the StanfordCoreNLP lemmatizer to retrieve a list of lemmas for every word.
 * Lemmatisation (or lemmatization) in linguistics is the process of grouping together the inflected forms of a word so they can be analysed as
 * a single item, identified by the word's lemma, or dictionary form.
 * 
 * For example:
 * 1. The word "better" has "good" as its lemma.
 * 2. The word "walk" is the base form for word "walking".
 * The word "meeting" can be either the base form of a noun or a form of a verb ("to meet") depending on the context.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class Lemmatizer {
	protected StanfordCoreNLP pipeline;
	
	
	// contructor for lemmatizer
	public Lemmatizer() {
		// Create StanfordCoreNLP object properties, with POS tagging and lemmatization (required for lemmatization), 
        Properties props;
        props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");

        /*
         * This is a pipeline that takes in a string and returns various analyzed linguistic forms. 
         * The String is tokenized via a tokenizer (such as PTBTokenizerAnnotator), 
         * and then other sequence model style annotation can be used to add things like lemmas, 
         * POS tags, and named entities. These are returned as a list of CoreLabels. 
         * Other analysis components build and store parse trees, dependency graphs, etc. 
         * 
         * This class is designed to apply multiple Annotators to an Annotation. 
         * The idea is that you first build up the pipeline by adding Annotators, 
         * and then you take the objects you wish to annotate and pass them in and 
         * get in return a fully annotated object.
         * 
         *  StanfordCoreNLP loads a lot of models, so you probably
         *  only want to do this once per execution
         */
        this.pipeline = new StanfordCoreNLP(props);
    }
	
	/*
	 * This method retrieves a list of lemmas for every word in a sentence
	 * 
	 * @param documentText Sentences from a document
	 */
	public ArrayList<String> lemmatize(String documentText)
    {
        ArrayList<String> lemmas = new ArrayList<String>();
        // Create an empty Annotation just with the given text
        Annotation document = new Annotation(documentText);
        // run all Annotators on this text
        this.pipeline.annotate(document);
        // Iterate over all of the sentences found
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        for(CoreMap sentence: sentences) {
            // Iterate over all tokens in a sentence
            for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
                // Retrieve and add the lemma for each word into the
                // list of lemmas
                lemmas.add(token.get(LemmaAnnotation.class));
            }
        }
        return lemmas;
    }
	
	// uncomment the following main method to test the lemmatizer
	/*public static void main(String[] args) {
		BasicConfigurator.configure();
        System.out.println("Starting Stanford Lemmatizer");
        String text = "How could you be seeing into my eyes like open doors? \n"+
                "You led me down into my core where I've became so numb \n"+
                "Without a soul my spirit's sleeping somewhere cold \n"+
                "Until you find it there and led it back home \n"+
                "You woke me up inside \n"+
                "Called my name and saved me from the dark \n"+
                "You have bidden my blood and it ran \n"+
                "Before I would become undone \n"+
                "You saved me from the nothing I've almost become \n"+
                "You were bringing me to life \n"+
                "Now that I knew what I'm without \n"+
                "You can've just left me \n"+
                "You breathed into me and made me real \n"+
                "Frozen inside without your touch \n"+
                "Without your love, darling \n"+
                "Only you are the life among the dead \n"+
                "I've been living a lie, there's nothing inside \n"+
                "You were bringing me to life.";
        Lemmatizer slem = new Lemmatizer();
        System.out.println(slem.lemmatize(text));
    }*/
}


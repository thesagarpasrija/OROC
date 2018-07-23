package com.word2vec.SagarProjectWord2Vec;

/*
 * This class was not used.
 * 
 * This class was an initial attempt to solve object replacement problem.
 * It used the pre-trained Word2Vec model from DeepLearning4J package to calculate cosine similarity between input object's material and database
 * materials and input object's shape and database shapes respectively. The result was to be the object for which the cosine similarity values
 * for both material and shape was least (that is, most similar).
 */


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import org.jdom2.Element;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.CollectionSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentencePreProcessor;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.Tokenizer;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Cosine_Similarity
{
 public class values
 {
  int val1;
  int val2;
  values(int v1, int v2)
  {
   this.val1=v1;
   this.val2=v2;
  }
  public void Update_VAl(int v1, int v2)
  {
   this.val1=v1;
   this.val2=v2;
  }
 }//end of class values
  
 static final IndoEuropeanTokenizerFactory TOKENIZER_FACTORY = IndoEuropeanTokenizerFactory.INSTANCE;
 
 // returns a list of stop words
 public static List<String> getStopWords() {
	 
	 	// add stop words to the String stopWordsString separated by ,
	    String stopWordsString = ".,a,able,about,across,after,all,almost,also,am,among,an,and,any,are,as,at,be,because,been,but,by,can,cannot,could,dear,did,do,does,either,else,ever,every,for,from,get,got,had,has,have,he,her,hers,him,his,how,however,i,if,in,into,is,it,its,just,least,let,like,likely,may,me,might,most,must,my,neither,no,nor,not,of,off,often,on,only,or,other,our,own,rather,said,say,says,she,should,since,so,some,than,that,the,their,them,then,there,these,they,this,tis,to,too,twas,us,wants,was,we,were,what,when,where,which,while,who,whom,why,will,with,would,yet,you,your";
	    List<String> stopWordsList = new ArrayList<String>();
	    List<String> stopWordTokenList = new ArrayList<String>();
	    List<String> whiteList = new ArrayList<String>();
	    Tokenizer tokenizer = TOKENIZER_FACTORY.tokenizer(
	            stopWordsString.toCharArray(), 0, stopWordsString.length());
	    tokenizer.tokenize(stopWordTokenList, whiteList);
	    for (int i = 0; i < stopWordTokenList.size(); i++) {
	        if (!stopWordTokenList.get(i).equals(",")) {
	            stopWordsList.add(stopWordTokenList.get(i));
	        }
	    }
	    // uncomment the following line to get the number of stop words
	    //System.out.println("No.of stop words: " + stopWordsList.size());
	    return stopWordsList;
	}
 
  
 	/*
 	 * This method returns an ArrayList of words after removing stop words from sentences
 	 * 
 	 *  @param tokenList ArrayList of sentences of strings
 	 *  @param stopWordsList List of all the stop words
 	 */
	public static ArrayList<String> filterStopWords(ArrayList<String> tokenList,
	        List<String> stopWordsList) {

	    ArrayList<String> filteredSentenceWords = new ArrayList<String>();
	    for (String sentenceToken : tokenList) {
	        if (!stopWordsList.contains(sentenceToken)) {
	            filteredSentenceWords.add(sentenceToken);
	        }
	    }
	    return filteredSentenceWords;
	}
 
	
	
 /*
  * This method calculates cosine similarity values using Word2Vec model. It also removes stop words and lemmatizes every word.
  * 
  * @param list list of elements from the XML file
  */
 public double Cosine_Similarity_Score_Word2Vec(List list) throws IOException
 {

	 List<String> stopWordsList = getStopWords();
	 int i=0;
	 final Set<String>shapes1=new HashSet<>();
	 final Set<String>materials1=new HashSet<>();
	 while(i<list.size()) {
		 Element node=(Element) list.get(i);
		
		 String shape=node.getChildText("Shape");
		 String mat=node.getChildText("Material");
		
		 materials1.add(mat);
		 shapes1.add(shape);
		 i++;
	 }

	 List<String> materials=new ArrayList<>();
	 materials.addAll(materials1);	 
	 
	 List<String> shapes=new ArrayList<>();
	 shapes.addAll(shapes1);

	 
	 materials.replaceAll(String::toLowerCase);

	 shapes.replaceAll(String::toLowerCase);

	 
	 //calling lemmatizer
	 System.out.println("+++++++++++++++++++++++ Calling Lemmatizer... ++++++++++++++++");
	 Lemmatizer l=new Lemmatizer();
	 List<String> listMat=new LinkedList<>();
	 List<String> listShape=new LinkedList<>();
	 for(String mat:materials) {
		 listMat.addAll(l.lemmatize(mat));
	 }
	 
	 System.out.println(listMat);
	 for(String sha:shapes) {
		 listShape.addAll(l.lemmatize(sha));
	 }
	 
	 System.out.println(listShape);

	 SentenceIterator iter=new CollectionSentenceIterator(listMat);
	 SentenceIterator iter2=new CollectionSentenceIterator(listShape);
	 
	 // pre-processing
	 iter.setPreProcessor(new SentencePreProcessor() {
		 @Override
         public String preProcess(String sentences) {
             return sentences.toLowerCase();
		 } 
	 });
	 iter2.setPreProcessor(new SentencePreProcessor() {
		 @Override
         public String preProcess(String sentences) {
             return sentences.toLowerCase();
		 } 
	 });
	 
	 // tokenizing
	 TokenizerFactory t=new DefaultTokenizerFactory();
	 t.setTokenPreProcessor(new CommonPreprocessor());

	 
	 System.out.println("Building model...");
	 
	 // Word2Vec model for material
	 Word2Vec vec = new Word2Vec.Builder().minWordFrequency(1)
			 		.layerSize(300).windowSize(5).epochs(20)
	                .sampling(1e-5) 
	                .useAdaGrad(false)
	                .learningRate(0.025)
	                .minLearningRate(1e-15) // learning rate decays wrt # words. floor learning
	                .iterate(iter)
			 		.tokenizerFactory(t).build();
	 
	 
	 // Word2Vec model for shape
	 Word2Vec vec2 = new Word2Vec.Builder().minWordFrequency(1)
			 		.layerSize(300).windowSize(5).epochs(20)
	                .sampling(1e-5) 
	                .useAdaGrad(false)
	                .learningRate(0.025)
	                .minLearningRate(1e-15) // learning rate decays wrt # words. floor learning
	                .iterate(iter2)
			 		.tokenizerFactory(t).build();
	 
	 
	 System.out.println("Model built.");
	 System.out.println("Fitting word2vec models...");
	 vec.fit();
	 System.out.println("Model Materials fitted.");
	 vec2.fit();
	 System.out.println("Model Shapes fitted.");
	 
	 //evaluation
	 // giving path to where the vectors are to be stored
	 WordVectorSerializer.writeWordVectors(vec,"C:/Users/Sagar Pasrija/Desktop/myVectorsMat.txt");
	 WordVectorSerializer.writeWordVectors(vec2, "C:/Users/Sagar Pasrija/Desktop/myVectorsShape.txt");
	 
	 
	 String[] lstMat;
	 String[] lstShape;
	 
	 Set<String> lstMatArr=new HashSet<>();
	 Set<String> lstShapeArr=new HashSet<>();
	 
	 
	 // taking material input
	 Scanner sc=new Scanner(System.in);
	 System.out.println("Enter material: ");
	 String inputMat=sc.nextLine();
	 inputMat=inputMat.toLowerCase();
	 Lemmatizer inL=new Lemmatizer();
	 List<String> inLMat=new LinkedList<>();
	 inLMat.addAll(inL.lemmatize(inputMat));
	 
	 // printing 5 closest words to input material 
	 for(String s:inLMat) {
		 System.out.println("Closest words found are (for "+s+"): ");
		 System.out.println(vec.wordsNearestSum(s, 5));
	 }
	 
	 
	 /*for(String mat:listMat) {
		 lstMat=mat.split("\\s+");
		 for(String s:lstMat) {
			 lstMatArr.add(s);
			 
			 System.out.println("Closest words to "+s+": ");
			 System.out.println(vec.wordsNearestSum(s, 5));	//sum includes current word too
		 }
	 }*/
	 
	 
	 /*System.out.println("===================================== avgVector called ==================================");
	 avgVector avg=new avgVector();
	 ArrayList<Double> vectorOfSentence=avg.calculate("cloth and metal", vec,vec.getLayerSize());
	 
	 System.out.println("Average-----");
	 System.out.println(vectorOfSentence);*/
	 
	 
	/*for(String s:lstMatArr) {
		 for(String s1:lstMatArr) {
			double sim_score = vec.similarity(s, s1);
			System.out.println("Cosine similarity bw "+s+" and "+s1+": "+sim_score);
		 }
	}*/
	 
	 
		 
	 // taking shape input
	 Scanner sc2=new Scanner(System.in);
	 System.out.println("Enter shape: ");
	 String inputShape=sc2.nextLine();
	 inputShape=inputShape.toLowerCase();
	 Lemmatizer inL2=new Lemmatizer();
	 List<String> inLShape=new LinkedList<>();
	 inLShape.addAll(inL2.lemmatize(inputShape));
	 
	 // printing 5 closest words to input shape
	 for(String s:inLShape) {
		 System.out.println("Closest words found are (for "+s+"): ");
		 System.out.println(vec2.wordsNearestSum(s, 5));
	 }
	 
	 
	 
	 /*for(String sha:listShape) {
		 lstShape=sha.split("\\s+");
		 for(String s:lstShape) {
			 lstShapeArr.add(s);
			 
			 System.out.println("Closest words to "+s+": ");
			 System.out.println(vec2.wordsNearestSum(s, 5));
		 }
	 }*/
	 

	 /*System.out.println(lstShapeArr);
	 

	for(String s:lstShapeArr) {
		 for(String s1:lstShapeArr) {
			double sim_score = vec2.similarity(s, s1);
			System.out.println("Cosine similarity bw "+s+" and "+s1+": "+sim_score);
		 }
	}*/

	 
	 //System.out.println(lstShape);
	 /*System.out.println("Closest words to cuboid: ");
	 Collection<String> lst2=vec2.wordsNearest("cuboid", 5);
	 System.out.println(lst2);*/
	 
	 //cosine similarity 
	 // dummy cosine similarity - the model was not able to correctly calculate cosine similarity values.
	 // ceramic and porcelain are pretty similar materials. Still the model gave incorrect cosine values.
	 double sim_score = vec.similarity("ceramic", "porcelain");	
	 
	 
	 
	 /*double sim_score2 = vec2.similarity("cuboid", "cylinder");
	 System.out.println("Cosine similarity bw cuboid and cylinder: "+sim_score2);*/
	 
	 
	 return (sim_score);
	 
 }
 
 
 
 /*
  * This method calculates cosine similarity values without using Word2Vec model. It calculates cosine similarity using TF-IDF approach.
  * The value returned is a measure that calculates the cosine of the angle between them.
  * This metric is a measurement of orientation and not magnitude.
  * 
  * @param Text1 first input text which is to be compared
  * @param Text2 second input text to which the first one will be compared
  */
 public double Cosine_Similarity_Score(String Text1, String Text2) throws IOException
 {
  Text1=Text1.toLowerCase();
  Text2=Text2.toLowerCase();
  double sim_score=0.0000000;
  
  //1. Identify distinct words from both documents
  String [] word_seq_text1 = Text1.split(" ");
  String [] word_seq_text2 = Text2.split(" ");
  
  Hashtable<String, values> word_freq_vector = new Hashtable<String, Cosine_Similarity.values>();
  LinkedList<String> Distinct_words_text_1_2 = new LinkedList<String>();
   
  //prepare word frequency vector by using Text1
  for(int i=0;i<word_seq_text1.length;i++)
  {
   String tmp_wd = word_seq_text1[i].trim();
   if(tmp_wd.length()>0)
   {
    if(word_freq_vector.containsKey(tmp_wd))
    {
     values vals1 = word_freq_vector.get(tmp_wd);
     int freq1 = vals1.val1+1;
     int freq2 = vals1.val2;
     vals1.Update_VAl(freq1, freq2);
     word_freq_vector.put(tmp_wd, vals1);
    }
    else
    {
     values vals1 = new values(1, 0);
     word_freq_vector.put(tmp_wd, vals1);
     Distinct_words_text_1_2.add(tmp_wd);
    }
   }
  }
   
  //prepare word frequency vector by using Text2
  for(int i=0;i<word_seq_text2.length;i++)
  {
   String tmp_wd = word_seq_text2[i].trim();
   if(tmp_wd.length()>0)
   {
    if(word_freq_vector.containsKey(tmp_wd))
    {
     values vals1 = word_freq_vector.get(tmp_wd);
     int freq1 = vals1.val1;
     int freq2 = vals1.val2+1;
     vals1.Update_VAl(freq1, freq2);
     word_freq_vector.put(tmp_wd, vals1);
    }
    else
    {
     values vals1 = new values(0, 1);
     word_freq_vector.put(tmp_wd, vals1);
     Distinct_words_text_1_2.add(tmp_wd);
    }
   }
  }
   
  //calculate the cosine similarity score.
  double VectAB = 0.0000000;
  double VectA_Sq = 0.0000000;
  double VectB_Sq = 0.0000000;
   
  for(int i=0;i<Distinct_words_text_1_2.size();i++)
  {
   values vals12 = word_freq_vector.get(Distinct_words_text_1_2.get(i));
   
   double freq1 = (double)vals12.val1;
   double freq2 = (double)vals12.val2;
   System.out.println(Distinct_words_text_1_2.get(i)+" # "+freq1+" # "+freq2);
    
   VectAB=VectAB+(freq1*freq2);
    
   VectA_Sq = VectA_Sq + freq1*freq1;
   VectB_Sq = VectB_Sq + freq2*freq2;
  }
  System.out.println("VectAB "+VectAB+" VectA_Sq "+VectA_Sq+" VectB_Sq "+VectB_Sq);
  sim_score = ((VectAB)/(Math.sqrt(VectA_Sq)*Math.sqrt(VectB_Sq)));
  
  return(sim_score);
 }
 public static void main(String[] args) throws IOException
 {
  Cosine_Similarity CS = new Cosine_Similarity();
  BufferedReader br = new BufferedReader (new InputStreamReader (System.in));

  System.out.println("Enter String A: ");
  String A = br.readLine();
  System.out.println("Enter String B: ");
  String B = br.readLine();
  
  System.out.println("\n\nComputing cosine similarity score between A and B ...");
  System.out.println("\n\nVector representation for words in A and B: ");
 
  System.out.println("[ Word # VectorA # VectorB ]\n");
  double cos_sim = CS.Cosine_Similarity_Score(A, B);
  System.out.println("\n\nCosine similarity score = "+cos_sim);
 }
}
package com.word2vec.SagarProjectWord2Vec;

/*
 * This class was not used.
 * 
 * XmlParser class was constructed for parsing an XML file containing data for the name, shape, material and affordance of objects.
 * The structure of the XML file is as follows: 
 * 		
 * 		<Concepts>
  			<Concept type="primitive">
        		<Name>Cup</Name>
        		<Material>Ceramic</Material>
        		<Shape>high convexity</Shape>
        		<Affordance>to drink from</Affordance>
  			</Concept>
  			
  			<!--Add more objects here-->
  			
  		</Concepts>
 */

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class XmlParser {

	// path to XML file
	private String myXmlFilePath;

	// constructor
	public XmlParser(String filePath) {
		this.myXmlFilePath = filePath;	
	}


	/*
	 * This method parses the XML file and loads it into memory.
	 * Once the data has been loaded into memory, it can be used with Word2Vec models to calculate cosine similarity between materials and shapes.
	 */
	public List<Concept> loadTrainingData() throws JDOMException, IOException {
		
		SAXBuilder builder = new SAXBuilder();
		List<Concept> theConceptList = new ArrayList<>();
		File file = new File(myXmlFilePath);
		Document document = (Document) builder.build(file);
		Element rootNode = document.getRootElement();
		
		List<Element> list = rootNode.getChildren("Concept");

		System.out.println("Calling Cosine_Similarity.java... word2vec implementation");
		Cosine_Similarity cs=new Cosine_Similarity();
		double cs_value1=cs.Cosine_Similarity_Score_Word2Vec(list);
		
		/*for (int i = 0; i < list.size(); i++) {
			for(int j=0;j<list.size();j++) {
			
				if(i!=j) {
				
					Element node1 = (Element) list.get(i);
					Element node2 = (Element) list.get(j);
					String shape1=node1.getChildText("Shape");
					String shape2=node2.getChildText("Shape");
					String mat1=node1.getChildText("Material");
					String mat2=node2.getChildText("Material");
					
					Cosine_Similarity cs_shape=new Cosine_Similarity();
					double cs_value1=cs_shape.Cosine_Similarity_Score(shape1,shape2);
					Cosine_Similarity cs_mat=new Cosine_Similarity();
					double cs_value2=cs_mat.Cosine_Similarity_Score(mat1,mat2);
					System.out.println(node1.getChildText("Name")+" with "+node2.getChildText("Name"));
					System.out.println("shape: "+cs_value1+"\t material: "+cs_value2);
					//System.out.println("cs: "+ (cs_value1*cs_value2)/Math.sqrt(cs_value1*cs_value1+cs_value2*cs_value2));
					System.out.println("---------------------------");
					//System.out.println("Type : "+node.getChildText("Type"));
					//System.out.println("Name : " + node.getChildText("Name"));
					//System.out.println("Material : " + node.getChildText("Material"));
					//System.out.println("Nick Name : " + node.getChildText("nickname"));
					System.out.println("Shape : " + node.getChildText("Shape"));
					System.out.println("Affordance : " + node.getChildText("Affordance"));
					System.out.println("--------------");
					
					//System.out.println("Type : "+node.getChildText("Type"));
					System.out.println("Name : " + node2.getChildText("Name"));
					System.out.println("Material : " + node2.getChildText("Material"));
					//System.out.println("Nick Name : " + node.getChildText("nickname"));
					System.out.println("Shape : " + node2.getChildText("Shape"));
					System.out.println("Affordance : " + node2.getChildText("Affordance"));
					System.out.println("-------------------------------");
				}
			}
		}*/
		return theConceptList;


	}
}




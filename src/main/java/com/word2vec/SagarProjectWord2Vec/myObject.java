package com.word2vec.SagarProjectWord2Vec;

/*
 * The myObject class defines an object with a name and a list of materials and shapes that the object can have.
 * The constructor accepts name of the object, material map and shape map as parameters.
 * It then extracts the list of materials and shapes for the corresponding object name from those maps and stores them in
 * ArrayList<String> material and ArrayList<String> shape respectively.
 */


import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class myObject {
	String name;
	ArrayList<String> material;
	ArrayList<String> shape;
	

	
	// defining contructor
	/*
	 * name is the name of the input object.
	 * materialMap is a map with object name as keys and a list of materials as values
	 * shapeMap is a map with object name as keys and a list of shapes as values
	 */
	public myObject(String name, Map<String, ArrayList<String>> materialMap,Map<String, ArrayList<String>> shapeMap) throws IOException {
		name.toLowerCase();
		this.name=name;
		
		//extracting possible materials for the object
		for(Map.Entry<String,ArrayList<String>> entry:materialMap.entrySet()) {
			String key=entry.getKey();
			key.toLowerCase();
			entry.getValue().replaceAll(String::toLowerCase);
			if(key.equals(name)) {
				this.material=entry.getValue();
			}
		}
		
		//extracting possible shapes for the object
		for(Map.Entry<String,ArrayList<String>> entry:shapeMap.entrySet()) {
			String key=entry.getKey();
			key.toLowerCase();
			entry.getValue().replaceAll(String::toLowerCase);
			if(key.equals(name)) {
				this.shape=entry.getValue();
			}
		}
		
	}
}

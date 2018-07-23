package com.word2vec.SagarProjectWord2Vec;

/*
 *  comparison class compares the input object with the mapped data to find the list of possible objects that can replace the input object
 */


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;



public class comparison {
	
	/*
	 * This method is used to print a String array on the console
	 * 
	 * @param arr String array
	 */
	public static void printArr(String[] arr) {
		int n=arr.length;
		for(int i=0;i<n;i++)
			System.out.println(arr[i]);
	}
	
	
	/*
	 * This method is used to map every object name with its material and shape in two different hashmaps
	 * 
	 * It reads .txt file into memory.
	 * The format of the two files is as follows:
	 * 
	 * for material.txt
	 *  objectName=material1,material2...
	 *  
	 * for shape.txt
	 * 	objectName=shape1,shape2...
	 * 
	 * 
	 * @param path the path to the materials or shapes .txt files
	 */
	public static Map<String,ArrayList<String>> makeMap(String path) throws IOException { 
		
		FileReader file=new FileReader(path);
		BufferedReader bufRead=new BufferedReader(file);
		String myLine=null;
		Map<String,ArrayList<String>> map=new HashMap<>();
		
		while((myLine=bufRead.readLine())!=null) {
			String[] array1=myLine.split("=");
			String[] array2=array1[1].split(",");
			ArrayList<String> list=new ArrayList<>();
			
			for(String everyInArray2:array2)
				list.add(everyInArray2);
			
			map.put(array1[0], list);
				
		}

		return map;
	}
	
	
	/*
	 * This method sorts the map entries on the basis of map values
	 * 
	 * @param inputMap map with ArrayList of String keys and integer values
	 * @param index the index of the element inside the ArrayList of integers
	 * 
	 * 
	 * This method is not used. It was earlier used to sort the frequency of matches occurred in shapes and materials among different objects.
	 * The frequency of matches were stored in the map countMap 
	 */
	
	public static Map<String, ArrayList<Integer>> sortMapByValue(Map<String, ArrayList<Integer>> inputMap,int index) {
		 
		Set<Entry<String, ArrayList<Integer>>> set = inputMap.entrySet();
		List<Entry<String, ArrayList<Integer>>> list = new ArrayList<Entry<String, ArrayList<Integer>>>(set);
		
		Collections.sort(list, new Comparator<Map.Entry<String, ArrayList<Integer>>>() {
			
			public int compare(Map.Entry<String, ArrayList<Integer>> o1, Map.Entry<String, ArrayList<Integer>> o2) 
			{
				// For Ascending order change the place of variables o1 and o2 with each other.
				return (o2.getValue().get(index)).compareTo(o1.getValue().get(index));
			
			}
		});
		
		Map<String, ArrayList<Integer>> result = new LinkedHashMap<>();
        for (Entry<String, ArrayList<Integer>> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;

	}
	
	
	/*
	 * This method constructs an ArrayList<ArrayList<String>> that is inserted into the finalMap as value for every object in the database
	 * 
	 * @param mapKey the object name
	 * @param mMap material map
	 * @param sMap shape map
	 * @param finalMap the combined map
	 */
	public synchronized static void addToList(String mapKey,Map<String,ArrayList<String>> mMap,Map<String,ArrayList<String>> sMap,Map<String,ArrayList<ArrayList<String>>> finalMap) {
		ArrayList<ArrayList<String>> combined=new ArrayList<ArrayList<String>>();
		ArrayList<String> mat=mMap.get(mapKey);
		ArrayList<String> sha=sMap.get(mapKey);
		combined.add(0, mat);
		combined.add(1, sha);
		
		finalMap.put(mapKey, combined);
		
	}
	
	
	/*
	 * This method is used to combine material map and shape map into one single map.
	 * It returns the finalMap with object name as key and a list of the shape and material of the object as value.
	 * 
	 * @param materialMap
	 * @param shapeMap
	 */
	public static Map<String,ArrayList<ArrayList<String>>> combineMaps(Map<String,ArrayList<String>> materialMap,Map<String,ArrayList<String>> shapeMap){
		
		Map<String,ArrayList<ArrayList<String>>> finalMap=new HashMap<>();
		
		for(Map.Entry<String,ArrayList<String>> entry:materialMap.entrySet()) {
			String key=entry.getKey();
			
			addToList(key,materialMap,shapeMap,finalMap);
			
		}

		return finalMap;
	}
	
	
	
	/*
	 * This method constructs a hashmap with the String as keys and ArrayList<String> as values
	 * It is used to create two maps with:
	 * 		1. object name as keys and a list of materials as value
	 * 		2. object name as keys and a list of shapes as value  
	 * 
	 * It reads a .txt file into memory.
	 * The format of the two files is as follows:
	 * 
	 * for material_similarity.txt
	 *  material1,material2=similarityValue
	 *  
	 * for shape_similarity.txt
	 * 	shape1,shape2=similarityValue
	 * 
	 * The stop words are automatically removed from the variables material1, material2, shape1 and shape2
	 * 
	 * 
	 * @param path the path to the similarity values .txt files
	 */
	public static Map<ArrayList<String>,Double> makeSimilarityMap(String path) throws IOException { 
	
		// get the list of all the stop words
		List<String> stopWordsList = Cosine_Similarity.getStopWords();
		
		FileReader shape=new FileReader(path);
		BufferedReader bufRead=new BufferedReader(shape);
		String myLine=null;
		Map<ArrayList<String>,Double> map=new HashMap<>();
		
		
		while((myLine=bufRead.readLine())!=null) {
			String[] array2=myLine.split("=");
			String[] array1=array2[0].split(",");

			/*
			 * array2[1] contains the similarity value
			 * converting array2[1] to double
			 */
			double simValue=Double.parseDouble(array2[1]);
			
			ArrayList<String> list=new ArrayList<>();
			for(String everyInArray1:array1) {
				list.add(everyInArray1);
			}
			
			map.put(list, simValue);
				
		}
		
		// uncomment the following line to print the map
		//System.out.println(map);
		return map;
	}
	

	/*
	 * This method returns a list of the names of all the objects that can replace the input object
	 * 
	 * @param similarShapes list of shapes similar to the shape of the input object
	 * @param similarMaterials list of materials similar to the material of the input object
	 * @param finalMap
	 * @param inputObjectName
	 * 
	 * @returns similarObjects list of objects that can replace the input object
	 */
	public static ArrayList<String> getSimilarObjects(ArrayList<ArrayList<String>>similarShapes,ArrayList<ArrayList<String>>similarMaterials,Map<String,ArrayList<ArrayList<String>>> finalMap,String inputObjectName){
		ArrayList<String> similarObjects=new ArrayList<>();
		
		for(ArrayList<String> everyShape:similarShapes) {
			for(ArrayList<String> everymaterial:similarMaterials) {
				for(Map.Entry<String,ArrayList<ArrayList<String>>> entry:finalMap.entrySet()) {
					String similarObjectName=entry.getKey();
					ArrayList<ArrayList<String>> values=entry.getValue();
					
					// checking if the two ArrayLists of shapes (or materials) are same
					if(values.get(1).containsAll(everyShape) && values.get(0).containsAll(everymaterial)) {
						//System.out.println(similarObjectName);
						
						// excluding the comparison of the object with itself
						if(!similarObjectName.equals(inputObjectName))
							similarObjects.add(similarObjectName);
					}
				}
			}
		}
		
		return similarObjects;
	}


	
	
	public static void main(String args[]) throws IOException {
		
		List<String> stopWordsList = Cosine_Similarity.getStopWords();
		
		Lemmatizer l=new Lemmatizer();
		
		// path to the object-material data file
		String materialPath="C:/Users/Sagar Pasrija/Desktop/ObjectsData/material.txt";
		
		// path to the object-shape data file
		String shapePath="C:/Users/Sagar Pasrija/Desktop/ObjectsData/shapes.txt";
		
		// path to the file having similarity values among materials
		String materialSimilarityValuesPath="C:/Users/Sagar Pasrija/Desktop/ObjectsData/material_similarity.txt";
		// path to the file having similarity values among shapes
		String shapeSimilarityValuesPath="C:/Users/Sagar Pasrija/Desktop/ObjectsData/shape_similarity.txt";
		
		ArrayList<String> similarObjects=new ArrayList<>();
		
		Map<ArrayList<String>,Double> materialSimMap=makeSimilarityMap(materialSimilarityValuesPath);
		Map<ArrayList<String>,Double> shapeSimMap=makeSimilarityMap(shapeSimilarityValuesPath);
		
		Map<String,ArrayList<String>> materialMap=makeMap(materialPath);
		Map<String,ArrayList<String>> shapeMap=makeMap(shapePath);
		
		Map<String,ArrayList<ArrayList<String>>> finalMap=new HashMap<>();
		
		finalMap=combineMaps(materialMap,shapeMap);
		
		
		System.out.println("--------- printing final map ---------");
		for(Map.Entry<String, ArrayList<ArrayList<String>>> entry:finalMap.entrySet()) {
			String key=entry.getKey();
			System.out.println(key+": "+finalMap.get(key));
		}
		
		// countMap to store the frequency of matches occurred among shapes and materials of two different objects
		// NOTE: This is not used in any calculation
		Map<String,ArrayList<Integer>> countMap=new HashMap<>();
		
		//taking user input
		Scanner sc=new Scanner(System.in);
		System.out.println("Enter object: ");
		String object=sc.next();
		object.toLowerCase();
		myObject obj=new myObject(object,materialMap,shapeMap);
		
		// creating an EXCEL file to store the list of possible replacing objects with varying threshold values
		//ArrayList<Object[]> data=new ArrayList<>();
		
		// the headers for the EXCEL file
		/*Object[] headers= {"Object Name","Object Shape","Object Material","Shape Threshold","Material Threshold","Similar Shapes","Similar Materials","Similar Objects Output"};
		data.add(headers);*/
		

		
			//lemmatizing input
			ArrayList<String> shapesLemma=new ArrayList<>();
			ArrayList<String> materialsLemma=new ArrayList<>();

			for(String mat:obj.material)
				materialsLemma.addAll(l.lemmatize(mat));
		
			for(String sha:obj.shape)
				shapesLemma.addAll(l.lemmatize(sha));
		
		for(Map.Entry<String, ArrayList<ArrayList<String>>> entry:finalMap.entrySet()) {
			
			// filling frequency entries in countMap
			for(Map.Entry<String,ArrayList<ArrayList<String>>> entry1:finalMap.entrySet()) {
				int matMatches=0;
				int shaMatches=0;
				ArrayList<Integer> frequency=new ArrayList<Integer>();
				String objectName=entry1.getKey();
				ArrayList<ArrayList<String>> matAndShape=entry1.getValue();
				ArrayList<String> materials=matAndShape.get(0);
				ArrayList<String> shapes=matAndShape.get(1);
			
				for(int i=0;i<materialsLemma.size();i++) {
					if(materials.contains(materialsLemma.get(i))) {
						matMatches++;
					}
				}
				frequency.add(0, matMatches);
				for(int i=0;i<shapesLemma.size();i++) {
					if(shapes.contains(shapesLemma.get(i))) {
						shaMatches++;
					}
				}
				frequency.add(1, shaMatches);
				
				countMap.put(objectName, frequency);
				
			}
			
			
		double thresholdForShape=3.5;
		double thresholdForMaterial=3.5;
		

		// varying threshold values for shape and materials to find a suitable threshold value
		/*for(double thresholdForShape=2;thresholdForShape<=6;thresholdForShape+=1) {
			for(double thresholdForMaterial=2;thresholdForMaterial<=6;thresholdForMaterial+=1) {
		
			System.out.println("object name: "+obj.name);
			System.out.println("object material: "+obj.material);
			System.out.println("object shape: "+obj.shape);
			System.out.println("\n");*/
		
			ArrayList<ArrayList<String>> similarShapes=new ArrayList<ArrayList<String>>();
			ArrayList<ArrayList<String>> similarMaterials=new ArrayList<ArrayList<String>>();
		
		
		// getting similar shapes for the input object
		for(Map.Entry<ArrayList<String>,Double> entry2:shapeSimMap.entrySet()) {
			
			ArrayList<String> key=entry2.getKey();
			ArrayList<ArrayList<String>> finalKey=new ArrayList<>();
			key.replaceAll(String::toLowerCase);
			
			finalKey.add(l.lemmatize(key.get(0)));
			finalKey.add(l.lemmatize(key.get(1)));
			
			
			finalKey.set(0, Cosine_Similarity.filterStopWords(finalKey.get(0), stopWordsList));
			finalKey.set(1, Cosine_Similarity.filterStopWords(finalKey.get(1), stopWordsList));

				if(finalKey.get(0).containsAll(shapesLemma) && shapesLemma.containsAll(finalKey.get(0))) {
					if(entry2.getValue() > thresholdForShape) {
						similarShapes.add(finalKey.get(1));
					}
				}
		}
		
		// getting similar materials for the input object
		for(Map.Entry<ArrayList<String>,Double> entry3:materialSimMap.entrySet()) {
			
			ArrayList<String> key=entry3.getKey();
			ArrayList<ArrayList<String>> finalKey=new ArrayList<>();
			key.replaceAll(String::toLowerCase);
			
			finalKey.add(l.lemmatize(key.get(0)));
			finalKey.add(l.lemmatize(key.get(1)));
			
			
			finalKey.set(0, Cosine_Similarity.filterStopWords(finalKey.get(0), stopWordsList));
			finalKey.set(1, Cosine_Similarity.filterStopWords(finalKey.get(1), stopWordsList));

				if(finalKey.get(0).containsAll(materialsLemma) && materialsLemma.containsAll(finalKey.get(0))) {
					if(entry3.getValue() > thresholdForMaterial) {
						similarMaterials.add(finalKey.get(1));
					}
				}
		}
		
		
		/*System.out.println("Threshold shape: "+thresholdForShape);
		System.out.println("Threshold material: "+thresholdForMaterial);
		
		System.out.println("similar shapes to "+shapesLemma+" are : ");
		System.out.println(similarShapes);
		System.out.println("\n");
		System.out.println("similar materials to "+materialsLemma+" are : ");
		System.out.println(similarMaterials);
		System.out.println("\n");*/
		
		// getting the list of objects that can replace the input object
		similarObjects=getSimilarObjects(similarShapes,similarMaterials,finalMap,obj.name);
		
		// storing that data for writing on EXCEL file. The values are as follows:
		// object name, object shape, object material, current threshold value for shape, current threshold value for material, similar shapes, similar materials, similar objects
		/*Object[] objectArray= {obj.name,obj.shape,obj.material,thresholdForShape,thresholdForMaterial,similarShapes,similarMaterials,similarObjects};
		data.add(objectArray);*/
		
		
		}
		System.out.println("\nObjects which can replace "+obj.name+" are: ");
		System.out.println(similarObjects);
		System.out.println("\n");
		System.out.println("--------------------------------------------------------------------------------------");
		}
		//}
		
		// create the EXCEL file
		//ApachePOIExcelWrite.makeExcel(data);
	//}
}


package com.word2vec.SagarProjectWord2Vec;

/*
 * This class was not used.
 * 
 * Concept class was initially aimed at being used to create objects consisting of 
 * 		name
 * 		material
 * 		shape
 * 		affordance (use)
 * directly from the XML data file.
 */

public class Concept {

	String name;
	String material;
	String shape;
    String affordance;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public String getShape() {
		return shape;
	}
	public void setShape(String shape) {
		this.shape = shape;
	}
	public String getAffordance() {
		return affordance;
	}
	public void setAffordance(String affordance) {
		this.affordance = affordance;
	}
	
	// constructor
	public Concept(String name, String material, String shape, String affordance) {
		super();
		this.name = name;
		this.material = material;
		this.shape = shape;
		this.affordance = affordance;
	}

}

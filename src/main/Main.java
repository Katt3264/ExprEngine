package main;

import java.io.FileNotFoundException;

import proofEngine.Node;
import proofEngine.Utility;

public class Main {
	
	public static void main(String[] args) throws FileNotFoundException {
		
		Node node = Utility.nodeFromFile("resources/axioms.txt");
		
		System.out.println(node);
	}
}
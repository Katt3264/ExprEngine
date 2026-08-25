package proofEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import iterators.FileCharacterIterator;
import parse.PrimitiveTokenizer;
import parse.TreeExprParser;
import proofEngine.objects.Node;
import proofEngine.objects.Proof;
import proofEngine.objects.Rule;

public class Utility {
	
	public static Node nodeFromFile(String path) throws FileNotFoundException
	{
		File file = new File(path);
		Iterator<Character> chars = new FileCharacterIterator(file);
		List<String> tokens = new PrimitiveTokenizer(chars).tokenize();
		Node node = new Node (TreeExprParser.parseExpr(tokens));
		return node;
	}
	
	public static List<Node> nodesFromFolder(String path) throws FileNotFoundException
	{
		List<Node> nodes = new ArrayList<Node>();
		File file = new File(path);
		File[] files = file.listFiles();
		for(File f : files)
		{
			nodes.add(nodeFromFile(f.getPath()));
		}
		return nodes;
	}
	
	public static List<Rule> rulesFromNode(Node node) {
		List<Rule> rules = new ArrayList<Rule>();
		for(Node n : node.nodes)
		{
			rules.add(new Rule(n));
		}
		return rules;
	}
	
	public static List<String> fileToLines(String path) throws FileNotFoundException
	{
		Scanner s = new Scanner(new File(path));
		List<String> list = new ArrayList<String>();
		while (s.hasNextLine()){
		    list.add(s.nextLine());
		}
		s.close();
		return list;
	}
	
	public static void proofToFile(Proof proof, String path) throws IOException
	{
		File file = new File(path);
		file.getParentFile().mkdirs();
		FileWriter fw = new FileWriter(file);
		fw.write(proof.toString());
		fw.close();
	}

}

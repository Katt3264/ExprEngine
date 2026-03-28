package proofEngine;

import java.util.ArrayList;
import java.util.List;

public class ProofGenerator {
	
	/*
	 * Gets the location of a sub location in a location
	 */
	public static Node getLocInLoc(Node sup, Node sub)
	{
		if(sup.label != null)
			if(sup.label.equals("?"))
				return sub;
		
		if(sup.label != null)
			if(sup.label.equals("*"))
				return sup;
		
		Node node = new Node();
		for(Node subNode : sup.nodes)
		{
			node.nodes.add(getLocInLoc(subNode, sub));
		}
		return node;
	}
	
	/*
	 * Gets all possible locations for a expression
	 */
	public static List<Node> getAllLoc(Node exp)
	{
		List<Node> acc = new ArrayList<Node>();
		Node loc = new Node();
		loc.label = "?";
		addAllLoc(exp, loc, acc);
		return acc;
	}
	
	/*
	 * Adds all locations of a expression to accumulator
	 */
	private static void addAllLoc(Node exp, Node supLoc, List<Node> acc)
	{
		acc.add(supLoc);
		
		for(int i = 0; i < exp.nodes.size(); i++)
		{
			Node newSubLoc = new Node();
			for(int j = 0; j < exp.nodes.size(); j++)
			{
				Node leaf = new Node();
				if(i == j)
					leaf.label = "?";
				else
					leaf.label = "*";
				
				newSubLoc.nodes.add(leaf);
			}
			Node newLoc = getLocInLoc(supLoc, newSubLoc);
			addAllLoc(exp.nodes.get(i), newLoc, acc);
		}
	}
	
	
	/*
	 * Gets all possible new expressions by applying rule to expression at every location
	 */
	public static List<Node> getAllRewrites(Node exp, Rule rule)
	{
		List<Node> acc = new ArrayList<Node>();
		List<Node> locs = getAllLoc(exp);
		for(Node loc : locs)
		{
			Node newExp = Verifier.tryApplyRuleAt(exp, rule, loc);
			if(newExp != null)
				acc.add(newExp);
		}
		return acc;
	}

}

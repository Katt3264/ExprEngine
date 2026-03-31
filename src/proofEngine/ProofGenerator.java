package proofEngine;

import java.util.ArrayList;
import java.util.List;

import proofEngine.objects.Node;
import proofEngine.objects.Rule;
import proofEngine.objects.Transform;

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
	
	/*
	 * Searches for the transformation from src to dst
	 * Returns null in no is found.
	 */
	public static Transform tryGetTransform(Node src, Node dst, List<Rule> rules)
	{
		List<Node> allLoc = getAllLoc(src);
		
		for(Rule rule : rules) 
		{
			for(Node loc : allLoc)
			{
				Node res = Verifier.tryApplyRuleAt(src, rule, loc);
				
				if(res != null)
				{
					if(Verifier.isEqual(res, dst)) 
					{
						return new Transform(loc, rule);
					}
				}
			}
		}
		return null;
	}
	
	/*
	 * Searches for the transformations from src to dst
	 * Returns null in no is found.
	 */
	public static List<Transform> tryGetTransforms(Node src, Node dst, List<Rule> rules, int depth)
	{
		if(Verifier.isEqual(src, dst)) 
			return new ArrayList<Transform>();
		
		if(depth == 0)
			return null;
		
		List<Node> allLoc = getAllLoc(src);
		
		int shortestTransformList = depth + 1;
		List<Transform> bestTransforms = null;
		
		for(Rule rule : rules) 
		{
			for(Node loc : allLoc)
			{
				Node res = Verifier.tryApplyRuleAt(src, rule, loc);
				
				if(res != null)
				{
					List<Transform> nextTransforms = tryGetTransforms(res, dst, rules, depth - 1);
					
					if(nextTransforms != null)
					{
						if(nextTransforms.size() < shortestTransformList)
						{
							shortestTransformList = nextTransforms.size();
							bestTransforms = new ArrayList<Transform>();
							bestTransforms.add(new Transform(loc, rule));
							bestTransforms.addAll(nextTransforms);
						}
					}
				}
			}
		}
		
		return bestTransforms;
	}

}

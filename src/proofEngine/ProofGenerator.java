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
	 * Gets all locations where src and dst are different
	 */
	public static List<Node> getAllLocForDelta(Node src, Node dst)
	{
		List<Node> acc = new ArrayList<Node>();
		Node loc = new Node();
		loc.label = "?";
		addAllLocForDelta(src, dst, loc, acc);
		return acc;
	}
	
	/*
	 * Adds all locations where src and dst are different to accumulator
	 */
	private static void addAllLocForDelta(Node src, Node dst, Node supLoc, List<Node> acc)
	{
		if(Verifier.isEqual(src, dst))
			return;
		
		acc.add(supLoc);
		
		// Resolve this node before subnodes
		if(src.nodes.size() != dst.nodes.size())
			return;
		
		
		for(int i = 0; i < src.nodes.size(); i++)
		{
			Node newSubLoc = new Node();
			for(int j = 0; j < src.nodes.size(); j++)
			{
				Node leaf = new Node();
				if(i == j)
					leaf.label = "?";
				else
					leaf.label = "*";
				
				newSubLoc.nodes.add(leaf);
			}
			Node newLoc = getLocInLoc(supLoc, newSubLoc);
			
			addAllLocForDelta(src.nodes.get(i), dst.nodes.get(i), newLoc, acc);
		}
	}
	
	/*
	 * Searches for the transformation from src to dst
	 * Returns null if no is found.
	 */
	public static Transform tryGetTransform(Node src, Node dst, List<Rule> rules)
	{
		List<Node> allLoc = getAllLocForDelta(src, dst);
		
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
	 * Returns null if no is found.
	 */
	private static List<Transform> tryGetTransformsSlow(Node src, Node dst, List<Rule> rules, int depth)
	{
		if(Verifier.isEqual(src, dst)) 
			return new ArrayList<Transform>();
		
		if(depth == 0)
			return null;
		
		List<Node> allLoc = getAllLocForDelta(src, dst);
		
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
	
	/*
	 * Searches for the transformations from src to dst
	 * Returns null if no is found.
	 */
	public static List<Transform> tryGetTransforms(Node src, Node dst, List<Rule> rules, int maxDepth)
	{
		for(int i = 0; i <= maxDepth; i++)
		{
			List<Transform> res = tryGetTransformsSlow(src, dst, rules, i);
			
			if(res != null)
				return res;
		}
		
		return null;
	}
	
	/*
	 * Searches for the transformations for a list of intermediate expressions
	 * Returns null if no is found.
	 */
	public static List<Transform> tryGetTransforms(List<Node> exprs, List<Rule> rules)
	{
		List<Transform> transforms = new ArrayList<Transform>();
		for(int i = 0; i < exprs.size() - 1; i++)
		{
			Node src = exprs.get(i);
			Node dst = exprs.get(i+1);
			List<Transform> ts = tryGetTransforms(src, dst, rules, 10);
			if(ts != null)
			{
				transforms.addAll(ts);
			}
			else
			{
				return null;
			}
		}
		return transforms;
	}
}

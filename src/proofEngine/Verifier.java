package proofEngine;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import proofEngine.objects.Node;
import proofEngine.objects.Proof;
import proofEngine.objects.Rule;
import proofEngine.objects.Transform;

public class Verifier {

	/*
	 * Checks if two nodes are exactly the same
	 */
	public static boolean isEqual(Node node1, Node node2)
	{
		if(node1 == node2)
			return true;
		
		//check leaf nodes
		if(node1.label != null && node2.label != null)
		{
			if(node1.label.equals(node2.label)) {
				return true;
			} else {
				return false;
			}
		}
		
		//check subexpressions
		if(node1.nodes.size() == node2.nodes.size())
		{
			Iterator<Node> node1Iter = node1.nodes.iterator();
			Iterator<Node> node2Iter = node2.nodes.iterator();

			while (node1Iter.hasNext() && node2Iter.hasNext()) 
			{
			    boolean subMatch = isEqual(node1Iter.next(), node2Iter.next());
			    if(subMatch == false) {return false;}
			}
			return true;
		}
		
		return false;
	}
	
	/*
	 * Checks if a expression can be matched to a pattern
	 * Adds all aliases to a map of aliases
	 */
	public static boolean isMatch(Node exp, Node pat, Map<String, Node> aliases)
	{
		//check if pattern tries to match any ?x etc
		if(pat.label != null)
		{
			if(pat.label.startsWith("?")) {
				
				if(aliases.containsKey(pat.label)) {
					if(isEqual(exp, aliases.get(pat.label))) {
						return true;
					} else {
						return false;
					}
				} else {
					aliases.put(pat.label, exp);
					return true;
				}
			}
		}
		
		//check if pattern tries to match two leaf nodes
		if(exp.label != null && pat.label != null)
		{
			if(exp.label.equals(pat.label)) {
				return true;
			} else {
				return false;
			}
		}
		
		//check subexpressions
		if(exp.nodes.size() == pat.nodes.size())
		{
			Iterator<Node> expIter = exp.nodes.iterator();
			Iterator<Node> patIter = pat.nodes.iterator();

			while (expIter.hasNext() && patIter.hasNext()) 
			{
			    boolean subMatch = isMatch(expIter.next(), patIter.next(), aliases);
			    if(subMatch == false) {return false;}
			}
			return true;
		}
		return false;
	}
	
	/*
	 * Constructs a new expression from a pattern and map of aliases
	 */
	public static Node expressionFromPattern(Node pat, Map<String, Node> aliases)
	{
		if(pat.label != null)
		{
			if(pat.label.startsWith("?")) {
				Node node = aliases.get(pat.label);
				if(node == null) 
					throw new RuntimeException("No match for alias");
				return node;
			} else {
				return pat; //this is a leaf node
			}
		}
		else
		{
			Node node = new Node();
			for(Node patNode : pat.nodes)
			{
				node.nodes.add(expressionFromPattern(patNode, aliases));
			}
			return node;
		}
	}
	
	
	/*
	 * Gets the resulting expression by applying a rule
	 * Returns null if rule can not be matched
	 */
	private static Node tryApplyRule(Node exp, Rule rule)
	{
		Map<String, Node> aliases = new HashMap<String, Node>();
		if (!isMatch(exp, rule.from, aliases))
			return null;
		return expressionFromPattern(rule.to, aliases);
	}
	
	/*
	 * Gets the resulting expression by applying a rule at a location
	 * Returns null if rule can not be matched
	 */
	public static Node tryApplyRuleAt(Node exp, Rule rule, Node loc)
	{
		if(loc.label != null) {
			if(loc.label.equals("*")) {
				return exp;
			} else if(loc.label.equals("?")) {
				return tryApplyRule(exp, rule);
			} else {
				throw new RuntimeException("invalid character in loc");
			}
		} else {
			if(exp.nodes.size() != loc.nodes.size())
				throw new RuntimeException("invalid structure of loc");
			
			Node node = new Node();
			
			Iterator<Node> expIter = exp.nodes.iterator();
			Iterator<Node> locIter = loc.nodes.iterator();
			while (expIter.hasNext() && locIter.hasNext()) 
			{
				Node subNode = tryApplyRuleAt(expIter.next(), rule, locIter.next());
				
				if(subNode == null)
					return subNode;
				
			    node.nodes.add(subNode);
			}
			return node;
		}
	}
	
	/*
	 * Gets the resulting expression by applying a transform
	 * Returns null if transform can not be applied
	 */
	public static Node tryApplyTransform(Node exp, Transform transform)
	{
		return tryApplyRuleAt(exp, transform.rule, transform.loc);
	}
	
	/*
	 * Checks if a proof is valid
	 */
	public static boolean isValidProof(Proof proof, List<Rule> validRules)
	{
		Node current = proof.src;
		System.out.println(current);
		for(Transform t : proof.transforms)
		{
			boolean isValidRule = false;
			for(Rule r : validRules)
			{
				if(isEqual(r.toNode(), t.rule.toNode()))
					isValidRule = true;
			}
			
			if(!isValidRule)
				return false;
			
			Node next = tryApplyTransform(current, t);
			
			if(next == null)
				return false;
			
			System.out.println(next);
			current = next;
		}
		
		return isEqual(current, proof.dst);
	}
}

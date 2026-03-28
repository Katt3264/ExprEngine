package proofEngine;

import java.util.ArrayList;
import java.util.List;

import parse.TreeNode;

public class Node {
	
	//TODO: make immutable
	public String label = null;
	public List<Node> nodes = new ArrayList<Node>();
	
	public Node()
	{
		
	}
	
	public Node(TreeNode<String> node)
	{
		label = node.label;
		for(TreeNode<String> sub : node.nodes)
		{
			nodes.add(new Node(sub));
		}
	}
	
	@Override
	public Node clone()
	{
		Node clone = new Node();
		clone.label = label;
		for(Node node : nodes) {
			clone.nodes.add(node.clone());
		}
		return clone;
	}
	
	@Override
	public String toString()
	{
		String s = "";
		
		if(label != null)
			return label.toString();
		
		if(nodes.size() != 0)
		{
			s += "(";
			for(Node node: nodes)
			{
				s += " " + node;
			}
			s += " )";
		}
		return s;
	}
}

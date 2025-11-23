package main;

import java.util.Stack;

import bigMath.StringMath;
import treeParse.TreeNode;

public class Evaluator {
	
	/*
	 * Supported operators:
	 * 
	 * (+ a b) -> a + b
	 * (- a b) -> a - b
	 * (* a b) -> a * b
	 * (/ a b) -> a / b
	 * (% a b) -> a % b
	 * 
	 * 
	 * TODO:
	 * 
	 * (if a b c) -> b if a is true else c,
	 * (eval STRING) -> returns the evaluation of string
	 * 
	 * 
	 * 
	 */
	
	TreeNode<String> root;
	
	public Evaluator(TreeNode<String> root)
	{
		this.root = root;
	}
	
	public void Evaluate(TreeNode<String> evalNode)
	{
		Stack<TreeNode<String>> stack = new Stack<TreeNode<String>>();
		stack.push(evalNode);
		
		while(stack.size() != 0)
		{
			TreeNode<String> node = stack.peek();
			for(TreeNode<String> n : node.nodes)
			{
				if(n.label == null)
					stack.push(n);
			}
			if(stack.peek() == node)
			{
				stack.pop();
				nodeEvaluation(node);
				System.out.println("EVAL: " + root);
			}
		}
	}
	
	private void nodeEvaluation(TreeNode<String> node)
	{
		if(node.nodes.size() == 0)
		{
			// this is a leaf node
		}
		else if(node.nodes.size() == 3)
		{
			String op = node.nodes.get(0).label;
			
			if(op.equals("+")){
				String a = node.nodes.get(1).label;
				String b = node.nodes.get(2).label;
				node.label = StringMath.add(a, b, 10);
			} else if(op.equals("-")){
				String a = node.nodes.get(1).label;
				String b = node.nodes.get(2).label;
				node.label = StringMath.sub(a, b, 10);
			} else if(op.equals("*")){
				String a = node.nodes.get(1).label;
				String b = node.nodes.get(2).label;
				node.label = StringMath.mul(a, b, 10);
			} else if(op.equals("/")){
				String a = node.nodes.get(1).label;
				String b = node.nodes.get(2).label;
				node.label = StringMath.div(a, b, 10);
			} else if(op.equals("%")){
				String a = node.nodes.get(1).label;
				String b = node.nodes.get(2).label;
				node.label = StringMath.mod(a, b, 10);
			}
			else
			{
				throw new RuntimeException("invalid operator: " + op);
			}
		}
		else
		{
			throw new RuntimeException("invalid argument count");
		}
	}

}

package main;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import bigMath.StringMath;
import iterators.FileCharacterIterator;
import iterators.StringCharacterIterator;
import parse.PrimitiveTokenizer;
import parse.Tokenizer;
import parse.TreeExprParser;
import parse.TreeNode;
import proofEngine.Node;

public class Evaluator {
	
	/*
	 * Supported operators:
	 * 
	 * (+ a b) -> a + b
	 * (- a b) -> a - b
	 * (* a b) -> a * b
	 * (/ a b) -> a / b
	 * (% a b) -> a % b
	 * (^ a b) -> a ^ b
	 * 
	 * TODO:
	 * 
	 * comparators and booleans
	 * strings
	 * 
	 * (if a b c) -> b if a is true else c,
	 * (eval STRING) -> returns the evaluation of string
	 * (defun function (arg1 arg2 ...) (expression)) -> defines a function with arguments
	 * (function arg1 arg2 ...) -> evaluates a function with given arguments
	 * 
	 */
	
	public static void main(String[] args)
	{
		Scanner scanner = new Scanner(System.in);
		String expr = scanner.nextLine();
		Iterator<Character> chars = new StringCharacterIterator(expr);
		List<String> tokens = new Tokenizer(chars).tokenize();
		TreeNode<String> node = TreeExprParser.parseExpr(tokens);
		System.out.println(Evaluate(node));
	}
	
	//TreeNode<String> root;
	
	/*public Evaluator(TreeNode<String> root)
	{
		this.root = root;
	}*/
	
	/*public static String Evaluate(TreeNode<String> evalNode)
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
				String s = nodeEvaluation(node);
				//System.out.println("EVAL: " + root);
			}
		}
	}*/
	
	private static String Evaluate(TreeNode<String> node)
	{
		if(node.nodes.size() == 0)
		{
			return node.label;
		}
		else if(node.nodes.size() == 3)
		{
			String op = node.nodes.get(0).label;
			String a = Evaluate(node.nodes.get(1));
			String b = Evaluate(node.nodes.get(2));
			
			if(op.equals("+")){
				return StringMath.add(a, b, 10);
			} else if(op.equals("-")){
				return StringMath.sub(a, b, 10);
			} else if(op.equals("*")){
				return StringMath.mul(a, b, 10);
			} else if(op.equals("/")){
				return StringMath.div(a, b, 10);
			} else if(op.equals("%")){
				return StringMath.mod(a, b, 10);
			} else if(op.equals("^")){
				return StringMath.exp(a, b, 10);
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

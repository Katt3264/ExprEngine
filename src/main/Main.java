package main;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import iterators.StringCharacterIterator;
import lexer.Tokenizer;
import treeParse.TreeExprParser;
import treeParse.TreeNode;

public class Main {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		String parse = sc.nextLine();
		sc.close();
		
		Iterator<Character> chars = new StringCharacterIterator(parse);
		List<String> tokens = new Tokenizer(chars).tokenize();
		TreeNode<String> node = TreeExprParser.parseExpr(tokens);
		System.out.println("EXPR: " + node);
		new Evaluator(node).Evaluate(node);
	}
}
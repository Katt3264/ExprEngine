package main;

import java.io.FileNotFoundException;
import java.util.List;

import proofEngine.ProofGenerator;
import proofEngine.Utility;
import proofEngine.objects.Node;
import proofEngine.objects.Rule;
import proofEngine.objects.Transform;

public class Main {
	
	public static void main(String[] args) throws FileNotFoundException {
		
		List<Rule> axioms = Utility.rulesFromNode(Utility.nodeFromFile("resources/axioms.txt"));
		List<Node> proof = Utility.nodeFromFile("resources/proof.txt").nodes;
		
		
		for(int i = 0; i < proof.size() - 1; i++)
		{
			Node src = proof.get(i);
			Node dst = proof.get(i+1);
			List<Transform> ts = ProofGenerator.tryGetTransforms(src, dst, axioms, 5);
			if(ts != null)
			{
				System.out.println("Transforms found:");
				for(Transform t : ts) 
				{
					System.out.println(t);
				}
			}
			else
			{
				System.out.println("No transform found: " + src + " " + dst);
			}
		}
		
		//System.out.println(node);
	}
}
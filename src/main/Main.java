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
		List<Node> proofs = Utility.nodeFromFile("resources/proof.txt").nodes;
		
		for(Node proof : proofs)
		{
			System.out.println("Checking proof:");
			for(int i = 0; i < proof.nodes.size() - 1; i++)
			{
				Node src = proof.nodes.get(i);
				Node dst = proof.nodes.get(i+1);
				List<Transform> ts = ProofGenerator.tryGetTransforms(src, dst, axioms, 10);
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
					throw new RuntimeException("Invalid proof");
				}
			}
			
			// add this proof
			axioms.add(new Rule(proof.nodes.get(0), proof.nodes.get(proof.nodes.size() - 1)));
		}
		//System.out.println(node);
	}
}
package main;

import java.io.IOException;
import java.util.List;

import proofEngine.ProofGenerator;
import proofEngine.Utility;
import proofEngine.objects.Node;
import proofEngine.objects.Proof;
import proofEngine.objects.Rule;

public class Main {
	
	public static Proof tryGenerateProof(List<Node> exprs, List<Rule> axioms)
	{
		return new Proof(exprs.get(0), exprs.get(exprs.size()-1), ProofGenerator.tryGetTransforms(exprs, axioms));
	}
	
	public static void buildProof(String axiomFile, String machineProofsPath, String input, String output) throws IOException
	{
		List<Rule> axioms = Utility.rulesFromNode(Utility.nodeFromFile(axiomFile));
		List<Node> machineProofs = Utility.nodesFromFolder(machineProofsPath);
		for(Node n : machineProofs)
		{
			Proof p = new Proof(n);
			//TODO: verify proof
			axioms.add(new Rule(p.src, p.dst));
		}
		
		Node proofToProve = Utility.nodeFromFile(input);
		Proof proof = tryGenerateProof(proofToProve.nodes, axioms);
		Utility.proofToFile(proof, output);
	}
	
	public static void main(String[] args) throws IOException
	{
		String input = null;
		String output = null;
		String axiomFile = null;
		String machineProofFolder = null;
		
		for(int i = 0; i < args.length; i++)
		{
			String arg = args[i];
			
			if(arg.equals("-in"))
			{
				input = args[i+1];
				i++;
			}
			else if(arg.equals("-out"))
			{
				output = args[i+1];
				i++;
			}
			else if(arg.equals("-axi"))
			{
				axiomFile = args[i+1];
				i++;
			}
			else if(arg.equals("-macdir"))
			{
				machineProofFolder = args[i+1];
				i++;
			}
			else if(arg.equals("ExprEngine"))
			{
				machineProofFolder = args[i+1];
			}
			else 
			{
				throw new RuntimeException("Invalid argument: " + arg);
			}
		}
		
		buildProof(axiomFile, machineProofFolder, input, output);
	}
}
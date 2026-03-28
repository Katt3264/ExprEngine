package proofEngine;

public class Rule {
	
	public final Node from;
	public final Node to;
	
	public Rule(Node from, Node to)
	{
		this.from = from;
		this.to = to;
	}
	
	public Rule(Node rule)
	{
		this.from = rule.nodes.get(0);
		this.to = rule.nodes.get(1);
	}

}

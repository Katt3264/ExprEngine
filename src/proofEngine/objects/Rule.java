package proofEngine.objects;

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
	
	public Node toNode()
	{
		Node node = new Node();
		node.nodes.add(from);
		node.nodes.add(to);
		return node;
	}

	@Override
	public String toString()
	{
		return toNode().toString();
	}
}

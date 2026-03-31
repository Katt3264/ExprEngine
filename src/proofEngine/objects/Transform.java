package proofEngine.objects;

public class Transform {

	public final Rule rule;
	public final Node loc;
	
	public Transform(Node loc, Rule rule)
	{
		this.rule = rule;
		this.loc = loc;
	}
	
	public Transform(Node node)
	{
		this.loc = node.nodes.get(0);
		this.rule = new Rule(node.nodes.get(1));
	}
	
	public Node toNode()
	{
		Node node = new Node();
		node.nodes.add(loc);
		node.nodes.add(rule.toNode());
		return node;
	}
	
	@Override
	public String toString()
	{
		return toNode().toString();
	}
}

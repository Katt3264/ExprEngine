package proofEngine.objects;

import java.util.ArrayList;
import java.util.List;

public class Proof {

	public final Node src;
	public final Node dst;
	public final List<Transform> transforms;
	
	public Proof(Node src, Node dst, List<Transform> transforms)
	{
		this.src = src;
		this.dst = dst;
		this.transforms = transforms;
	}
	
	public Proof(Node node)
	{
		this.src = node.nodes.get(0);
		this.dst = node.nodes.get(1);
		this.transforms = new ArrayList<Transform>();
		for(Node t : node.nodes.get(2).nodes)
		{
			transforms.add(new Transform(t));
		}
	}
	
	/*public Node toNode()
	{
		//TODO
	}*/
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("(\n");
		sb.append(src);
		sb.append("\n");
		sb.append(dst);
		sb.append("\n(\n");
		for(Transform t : transforms) 
		{
			sb.append(t);
			sb.append("\n");
		}
		sb.append(")\n)");
		return sb.toString();
	}
}

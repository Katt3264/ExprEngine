package parse;

import java.util.ArrayList;
import java.util.List;

public class TreeNode<T> {
	
	public T label = null;
	public List<TreeNode<T>> nodes = new ArrayList<TreeNode<T>>();
	
	public TreeNode()
	{
		
	}
	
	@Override
	public TreeNode<T> clone()
	{
		TreeNode<T> clone = new TreeNode<T>();
		clone.label = label;
		for(TreeNode<T> node : nodes) {
			clone.nodes.add(node.clone());
		}
		return clone;
	}
	
	@Override
	public String toString()
	{
		String s = "";
		
		if(label != null)
			return label.toString();
		
		if(nodes.size() != 0)
		{
			s += "(";
			for(TreeNode<T> node: nodes)
			{
				s += " " + node;
			}
			s += " )";
		}
		return s;
	}
}

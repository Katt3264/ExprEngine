package bigMath;

public class StringMath {
	
	public static String add(String s1, String s2, int base)
	{
		BinaryString a = BinaryAlge.FromStringBigEndian(s1, base);
		BinaryString b = BinaryAlge.FromStringBigEndian(s2, base);
		BinaryString c = BinaryAlge.Add(a, b);
		return BinaryAlge.ToStringBigEndian(c, base);
	}
	
	public static String sub(String s1, String s2, int base)
	{
		BinaryString a = BinaryAlge.FromStringBigEndian(s1, base);
		BinaryString b = BinaryAlge.FromStringBigEndian(s2, base);
		BinaryString c = BinaryAlge.Subtract(a, b);
		return BinaryAlge.ToStringBigEndian(c, base);
	}
	
	public static String mul(String s1, String s2, int base)
	{
		BinaryString a = BinaryAlge.FromStringBigEndian(s1, base);
		BinaryString b = BinaryAlge.FromStringBigEndian(s2, base);
		BinaryString c = BinaryAlge.Multiply(a, b);
		return BinaryAlge.ToStringBigEndian(c, base);
	}
	
	public static String div(String s1, String s2, int base)
	{
		BinaryString a = BinaryAlge.FromStringBigEndian(s1, base);
		BinaryString b = BinaryAlge.FromStringBigEndian(s2, base);
		BinaryString c = BinaryAlge.Divide(a, b);
		return BinaryAlge.ToStringBigEndian(c, base);
	}
	
	public static String mod(String s1, String s2, int base)
	{
		BinaryString a = BinaryAlge.FromStringBigEndian(s1, base);
		BinaryString b = BinaryAlge.FromStringBigEndian(s2, base);
		BinaryString c = BinaryAlge.Mod(a, b);
		return BinaryAlge.ToStringBigEndian(c, base);
	}

}

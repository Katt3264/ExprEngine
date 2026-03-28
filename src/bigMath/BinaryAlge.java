package bigMath;

public class BinaryAlge {

	public static BinaryString Add(BinaryString a, BinaryString b)
	{
		int log2a = LogBase2(a);
		int log2b = LogBase2(b);
		
		int resultSize = Math.max(log2a, log2b) + 2;
		
		BinaryString result = new BinaryString(resultSize);
		
		boolean carry = false;
		for(int i = 0; i < resultSize; i++)
		{
			int s = 0;
			boolean ca = a.GetBit(i);
			boolean cb = b.GetBit(i);
			
			if(ca) {s++;}
			if(cb) {s++;}
			if(carry) {s++;}
			
			result.SetBit(i, (s == 1 || s == 3));
			
			carry = (s == 2 || s == 3);
		}
		return result;
	}
	
	public static BinaryString Subtract(BinaryString a, BinaryString b)
	{
		if(Compare(a, b) == -1)
			throw new RuntimeException("subtraction resulting in negative number");
		
		int log2a = LogBase2(a);
		int log2b = LogBase2(b);
		
		int resultSize = Math.max(log2a, log2b) + 1;
		
		BinaryString result = new BinaryString(resultSize);
		
		boolean borrow = false;
		for(int i = 0; i < resultSize; i++)
		{
			int s = 0;
			boolean ca = a.GetBit(i);
			boolean cb = b.GetBit(i);
			
			if(ca) {s++;}
			if(cb) {s--;}
			if(borrow) {s--;}
			
			result.SetBit(i, (s == 1 || s == -1));
			
			borrow = (s < 0);
		}
		
		return result;
	}
	
	public static BinaryString Multiply(BinaryString a, BinaryString b)
	{
		BinaryString result = FromInt(0);
		
		BinaryString shiftor = b;
		BinaryString comparor = a;
		
		if(Compare(a, b) == 1) //faster when multiplying different size numbers
		{
			shiftor = a;
			comparor = b;
		}
		
		int log2comp = LogBase2(comparor);
		
		for(int i = 0; i <= log2comp; i++)
		{
			if(!IsEven(ShiftUp(comparor, -i)))
			{
				result = Add(result, ShiftUp(shiftor, i));
			}
		}
		
		return result;
	}
	
	public static BinaryString[] DivideRemainder(BinaryString a, BinaryString b)
	{
		if(Compare(b, FromInt(0)) == 0) 
			throw new RuntimeException("division by 0");
		
		BinaryString subtractor = a;
		BinaryString result = FromInt(0);
		
		int trialLenght = LogBase2(a);
		
		for(int i = trialLenght; i >= 0; i--)
		{
			if(Compare(ShiftUp(b, i), subtractor) != 1)
			{
				subtractor = Subtract(subtractor, ShiftUp(b, i));
				result = Add(result, FromInt(1));
			}
			
			if(i != 0) {result = ShiftUp(result, 1);}
		}
		return new BinaryString[] {result, subtractor};
		
	}
	
	public static BinaryString Divide(BinaryString a, BinaryString b)
	{
		return DivideRemainder(a, b)[0];
	}
	
	public static BinaryString Mod(BinaryString a, BinaryString mod)
	{
		return DivideRemainder(a, mod)[1];
	}
	
	public static BinaryString Exp(BinaryString b, BinaryString e)
	{
		if(Compare(b, FromInt(0)) == 0 && Compare(e, FromInt(0)) == 0)
			throw new RuntimeException("0 ^ 0 is undefined");
		
		if(Compare(b, FromInt(0)) == 0)
			return FromInt(0);
		
		if(Compare(e, FromInt(0)) == 0)
			return FromInt(1);
		
		if(IsEven(e)) 
		{
			return Exp(Multiply(b, b), Divide(e, FromInt(2)));
		}
		else
		{
			return Multiply(b, Exp(Multiply(b, b), Divide(Subtract(e, FromInt(1)), FromInt(2))));
		}
	}
	
	public static BinaryString GCD(BinaryString a, BinaryString b)	//not safe
	{
		if(Compare(a, FromInt(0)) == 0)
		{
			return b;
		}
		return GCD(Mod(b, a), a);
	}
	
	public static int LogBase2(BinaryString b)
	{
		int result = 0;
		for(int i = 0; i < b.size; i++)
		{
			if(b.GetBit(i)) {result = i;}
		}
		return result;
	}
	
	public static BinaryString ShiftUp(BinaryString b, int v)
	{
		int resultSize = Math.max(LogBase2(b) + 1 + v, 1);
		
		BinaryString result = new BinaryString(resultSize);
		
		for(int i = 0; i < resultSize; i++)
		{
			result.SetBit(i, b.GetBit(i - v));
		}
		return result;
	}
	
	public static boolean IsEven(BinaryString b)
	{
		return !b.GetBit(0);
	}

	public static int Compare(BinaryString a, BinaryString b)
	{
		int log2a = LogBase2(a);
		int log2b = LogBase2(b);
		
		for(int i = Math.max(log2a, log2b); i >= 0; i--)
		{
			boolean ca = a.GetBit(i);
			boolean cb = b.GetBit(i);
			
			if(ca && !cb) {return 1;}
			if(!ca && cb) {return -1;}
		}
		return 0;
	}
	
	public static BinaryString FromInt(int n)
	{
		BinaryString b = new BinaryString(32);
		
		for(int i = 0; i < 32; i++)
		{
			b.SetBit(i, (n & 1<<i) != 0);
		}
		return b;
	}
	
	public static int ToInt(BinaryString b)
	{
		int res = 0;
		
		for(int i = 0; i < 32; i++)
		{
			if(b.GetBit(i))
			{
				res = res | (1 << i);
			}
		}
		return res;
	}
	
	public static char ToChar(BinaryString b, int base)
	{
		return Integer.toString(ToInt(b), base).charAt(0);
	}
	
	// Least significant digit first
	public static BinaryString FromStringLittleEndian(String number, int base)
	{
		char[] chars = number.toCharArray();
		
		BinaryString b = FromInt(base);
		BinaryString result = FromInt(0);
		
		for(int i = chars.length - 1; i >= 0; i--)
		{
			result = BinaryAlge.Add(result, FromInt(Integer.parseInt(String.valueOf(chars[i]),base)));
			
			if(i != 0) {result = BinaryAlge.Multiply(result, b);}
		}
		return result;
	}
	
	// Most significant digit first
	public static BinaryString FromStringBigEndian(String number, int base)
	{
		char[] chars = number.toCharArray();
		
		BinaryString b = FromInt(base);
		BinaryString result = FromInt(0);
		
		//for(int i = chars.length - 1; i >= 0; i--)
		for(int i = 0; i < chars.length; i++)
		{
			result = BinaryAlge.Add(result, FromInt(Integer.parseInt(String.valueOf(chars[i]),base)));
			
			if(i != chars.length - 1) {result = BinaryAlge.Multiply(result, b);}
		}
		return result;
	}
	
	// Most significant digit first
	public static String ToStringBigEndian(BinaryString b, int base)
	{
		String ret = "";
		//char[]
		
		while(Compare(b, FromInt(0)) != 0 || ret.equals(""))
		{
			BinaryString[] q = BinaryAlge.DivideRemainder(b, FromInt(base));
			b = q[0];
			ret = ToChar(q[1], base) + ret;
		}
		
		return ret;
	}
	
	// Least significant digit first
	public static String ToStringLittleEndian(BinaryString b, int base)
	{
		String ret = "";
		while(Compare(b, FromInt(0)) != 0 || ret.equals(""))
		{
			BinaryString[] q = BinaryAlge.DivideRemainder(b, FromInt(base));
			b = q[0];
			ret = ret + ToChar(q[1], base);
		}
		
		return ret;
	}
}

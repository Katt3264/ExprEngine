package bigMath;

public class BinaryString {
	
	private int[] digits;
	public final int size;
	
	public BinaryString(int size)
	{
		this.size = size;
		digits = new int[Math.max(((size-1)/32) + 1, 1)];
	}
	
	public boolean GetBit(int index)
	{
		return (index < size && index >= 0) ? (digits[index/32] & (1 << (index % 32))) != 0 : false;
	}
	
	public void SetBit(int index, boolean value)
	{
		if(index < size && index >= 0) 
		{
			digits[index/32] = value ? digits[index/32] | (1 << (index % 32)) : digits[index/32] & ~(1 << (index % 32));
		}
	}

}

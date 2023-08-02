package CompPack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataOUTBUS implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public List<Integer> outputs = new ArrayList<Integer>();
	public int input;
}

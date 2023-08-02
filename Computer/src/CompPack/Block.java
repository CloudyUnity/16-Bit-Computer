package CompPack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Block implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public String name;
	public int nodes;
	
	public List<Integer> inputs = new ArrayList<Integer>();
	public List<Integer> outputs = new ArrayList<Integer>();
	public List<Vector2> connections = new ArrayList<Vector2>();
	public List<Integer> states = new ArrayList<Integer>();
	
	public List<DataAND> andList = new ArrayList<DataAND>();
	public List<DataNOT> notList = new ArrayList<DataNOT>();
	public List<DataINBUS> inList = new ArrayList<DataINBUS>();
	public List<DataOUTBUS> outList = new ArrayList<DataOUTBUS>();
}
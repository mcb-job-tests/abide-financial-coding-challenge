package abideFinancial;

import java.util.ArrayList;

public class RextList {
	private ArrayList<Rext> rextList = new ArrayList<Rext>();
	private boolean filled = false;
	
	public ArrayList<Rext> getList(){
		return rextList;
	}
	
	public void add(Rext rext){
		rextList.add(rext);		
	}
	
	public void setFilled(boolean flag){
		filled = flag;
	}
	
	public boolean filled(){
		return filled;
	}
}

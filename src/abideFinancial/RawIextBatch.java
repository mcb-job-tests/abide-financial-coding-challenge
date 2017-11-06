package abideFinancial;

public class RawIextBatch {
	public String [] lines = new String[BatchQueue.BATCH_LINES];
	private int length = 0;
	
	RawIextBatch(){
	}
	
	public boolean addLine(String line){
		boolean addedToBatch = false;
			
		if (line != null && (length < BatchQueue.BATCH_LINES) ) {
			lines[length] = line;
			length++;
			addedToBatch = true;
		}
		
		return (addedToBatch);
	}
	
	public int getLength(){
		return length;
	}
	
	public void setLength(int size){
		length = size;
	}
	
	public String getLine(int index){
		return lines[index];
	}
	
}

package abideFinancial;

public class PracticeActualSpendBatch {
	private PracticeActualSpend [] practiceSpend = new PracticeActualSpend[BatchQueue.BATCH_LINES];
	private int batchLength = 0;
	
	
	PracticeActualSpendBatch(){
	}
	
	public boolean addItem(PracticeActualSpend practiceSpendItem){
		boolean addedToBatch = false;
			
		if (practiceSpend != null && (batchLength < BatchQueue.BATCH_LINES) ) {
			practiceSpend[batchLength] = practiceSpendItem;
			batchLength++;
			addedToBatch = true;
		}
		
		return (addedToBatch);
	}
	
	public int getLength(){
		return batchLength;
	}
	
	public void setLength(int size){
		batchLength = size;
	}
	
	public PracticeActualSpend getItem(int index){
		return practiceSpend[index];
	}
}



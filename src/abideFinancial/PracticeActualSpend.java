package abideFinancial;

public class PracticeActualSpend {
	private String practice;
	private Double spendAmount;
	
	PracticeActualSpend(String _practice, Double _spendAmount){
		practice = _practice;
		spendAmount = _spendAmount;
	}
	
	public String getPractice(){
		return practice;
	}
	
	public Double getspendAmount(){
		return spendAmount;
	}
}

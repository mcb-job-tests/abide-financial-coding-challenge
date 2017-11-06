package abideFinancial;

import java.util.Calendar;

public class Iext {
	private String sha;
	private String pct;
	private String practice;
	private String bnfCode;
	private String bnfName;
	private int items;
	private Double nic;
	private Double actCost;
	private Calendar period;
	
	Iext(){	
	}
	
	Iext(String _sha, String _pct, String _practice, 
		 String _bnfCode, String _bnfName, int _items, 
		 Double _nic, Double _actCost, Calendar _period) {
		
		sha = _sha;
		pct = _pct;
		practice = _practice;
		bnfCode = _bnfCode;
		bnfName = _bnfName;
		items = _items;
		nic = _nic;
		actCost = _actCost;
		period = _period;
	}
		
	Iext(String line, CsvLineFormat csvFormat) {
		practice = getPractice(line, csvFormat);
		bnfName = getBnfName(line, csvFormat);
		items = getItems(line, csvFormat);
		actCost = getActCost(line, csvFormat);
	}
	

	
	public String getSha(){
		return sha;
	}
	
	public String getPct(){
		return pct;
	}
	
	public String getPractice(){
		return practice;
	}
	
	public String getBnfCode(){
		return bnfCode;
	}
	
	public String getBnfName(){
		return bnfName;
	}
	
	public String getPractice(String line, CsvLineFormat csvFormat) {
		int splitIndex[] = csvFormat.getCsvFormat();		
		return line.substring(splitIndex[CsvLineFormat.fields.PCT.ordinal()]+1, splitIndex[CsvLineFormat.fields.PRACTICE.ordinal()]);//.trim();
	}
	
	public String getBnfName(String line, CsvLineFormat csvFormat){
		int splitIndex[] = csvFormat.getCsvFormat();		
		
		int strIndex = splitIndex[CsvLineFormat.fields.BNF_NAME.ordinal()];	
		for ( int i = strIndex-1; i > splitIndex[CsvLineFormat.fields.BNF_CODE.ordinal()]; i--) {
			if ( line.charAt(i) != ' ' ) {
				strIndex = i;
				break;
			}
		}
		return line.substring(splitIndex[CsvLineFormat.fields.BNF_CODE.ordinal()]+1, strIndex+1);//.trim();
	}
	
	public int getItems(){
		return items;
	}
	
	public int getItems(String line, CsvLineFormat csvFormat){
		int splitIndex[] = csvFormat.getCsvFormat();
		
		int strIndex = splitIndex[CsvLineFormat.fields.BNF_NAME.ordinal()]+1;				
		for ( int i = strIndex; i < splitIndex[CsvLineFormat.fields.ITEMS.ordinal()]; i++ ){
			if ( line.charAt(i) != '0' ) {
				strIndex = i;
				break;
			}
		}		
		String temp = line.substring(strIndex, splitIndex[CsvLineFormat.fields.ITEMS.ordinal()]);//.replaceAll("^0+", "");		
	 
		return Integer.parseInt(temp); 
	}
	
	public Double getNic(){
		return nic;
	}
	
	public Double getActCost(){
		return actCost;
	}
	
	public Double getActCost(String line, CsvLineFormat csvFormat) {
		int splitIndex[] = csvFormat.getCsvFormat();	
		int strIndex = splitIndex[CsvLineFormat.fields.NIC.ordinal()]+1;		
		for ( int i = strIndex; i < splitIndex[CsvLineFormat.fields.ACT_COST.ordinal()]; i++ ){
			if ( line.charAt(i) != '0' ) {
				strIndex = i;
				break;
			}
		}		
		String tempStr = line.substring(strIndex, splitIndex[CsvLineFormat.fields.ACT_COST.ordinal()]);//.replaceAll("^0+", "");
		return Double.parseDouble(tempStr);
	}
	
	public Calendar getPeriod(){
		return period;
	}
}

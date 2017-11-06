package abideFinancial;


public class IextCsvLineFormat {

	// CSV file format
	enum fields {SHA, PCT, PRACTICE, BNF_CODE, BNF_NAME, ITEMS, NIC, ACT_COST, PERIOD};
	private int[] csvFormat;
	
	
	IextCsvLineFormat(String header, String line){
//		String[] headerFields = header.split(",");
		csvFormat = new int[fields.values().length];
		
		int fieldId = 0;
		for (int i = 0; i < line.length() && fieldId < fields.values().length; i++){
        	if ( line.charAt(i) == ',' ) {
        		csvFormat[fieldId++] = i;

        	}
        }	
	}

	public int[] getCsvFormat(){
		return csvFormat;
	}
	
}

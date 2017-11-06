package abideFinancial;

import java.util.Calendar;

public class Rext {
	private Calendar period;
	private String practiceId;
	private String practiceName;
	private String officeName;
	private String streetAdress;
	private String town;
	private String region;
	private String postCode;
	
	Rext(String csvLine, int[] rextFormat){
		
		practiceId = csvLine.substring(rextFormat[0]+1, rextFormat[1]);
		practiceName = csvLine.substring(rextFormat[1]+1, rextFormat[2]);
		
		// ...
		postCode = csvLine.substring(rextFormat[6]+1, rextFormat[7]).trim();
	}
	
	
	public String getPracticeId(){
		return practiceId;
	}
	
	public String getPracticeName(){
		return practiceName;
	}
	
	public String getPostCode(){
		return postCode;
	}
	
	public String getPostCodeShort(){
		return postCode.split(" ")[0];
	}
}

package abideFinancial;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class PostCodeShortCityMap {
	// <PostCode, City>
	private HashMap< String, String > postCodeMap = new HashMap<>();
	
	PostCodeShortCityMap( String postCodesFileName, String cityName ){
			
		BufferedReader buffReader;
		try {
			buffReader = new BufferedReader( new FileReader( postCodesFileName ) );
			String line = buffReader.readLine(); // header
			
			while ( (line = buffReader.readLine() ) != null ) {
				String postCodeShort = line.split( "," )[ 0 ].split( " " )[ 0 ];
				if ( !postCodeMap.containsKey( postCodeShort ) ) {
					postCodeMap.put( postCodeShort, cityName );
				}
			}
			buffReader.close();
	
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}
	
	public HashMap< String, String > getMap(){
		return postCodeMap;
		
	}
}

package abideFinancial;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BatchQueue {
	private String csvFile = "T201109PDP IEXT.CSV";
	public final static int BATCH_LINES = 400; 
	public final static int MAX_LINE_FIELDS = 9;
	private final static int QUEUE_SIZE = 20;
	
	private BlockingQueue< RawIextBatch > queue; 
	private BlockingQueue< PracticeActualSpendBatch > practiceSpendqueue; 
	private int productTotalItems;
	private float productTotalCost;
	private int numberOfCityPractices;
	private CsvLineFormat csvFormatArray;
	private PostCodeRank[] pcRankDesc;
	private int postCodesWithZeroTotalSpend;
	
	BatchQueue(){
		queue = new ArrayBlockingQueue< RawIextBatch >( QUEUE_SIZE );
		practiceSpendqueue = new ArrayBlockingQueue< PracticeActualSpendBatch >( QUEUE_SIZE );
		productTotalItems = 0;
		productTotalCost = 0;
		numberOfCityPractices = 0;
		postCodesWithZeroTotalSpend = 0;
	}
	
	public void producer1( RextList rextList ) throws InterruptedException, IOException {
		String csvFile = "T201202ADD REXT.CSV";
		BufferedReader buffReader = new BufferedReader( new FileReader( csvFile ) );		
		String line = buffReader.readLine();
		
		int totalFields = line.split( "," ).length;
		int [] indexArray = new int[ totalFields ];
		int lineIndex = 0;			
		int lineLength = line.length();
		
		for ( int i = 0; i < line.length(); i++ ) {
			if ( line.charAt(i) == ',' )
				indexArray[ lineIndex++ ] = i;
		}		
		indexArray[ lineIndex ] = lineLength;
		
		Rext rext;
		do {						
			rext = new Rext( line, indexArray );
			rextList.add( rext );
		} while ( ( line = buffReader.readLine() ) != null );
		rextList.setFilled( true );
		buffReader.close();
		
		
		long startTime = System.nanoTime();
		long endTime;
		// www.doogal.co.uk/UKPostcodesCSV.php?area=London
		// Note this file includes outer London.		
		PostCodeShortCityMap londonPostCodesShort = new PostCodeShortCityMap( "London postcodes.csv", "London" );
		String shortPostCode;
		int count = 0;
		for ( int i = 0; i < rextList.getList().size(); i++ ){
			// Is a London Postcode ?
			shortPostCode = rextList.getList().get( i ).getPostCodeShort();
			if ( londonPostCodesShort.getMap().containsKey( shortPostCode ) ) {
				count++;
			}
		}
		endTime = System.nanoTime();
		numberOfCityPractices = count;
		System.out.println( "Number of Practices with a postcode within Greater London : " 
		                    + count + ", Time taken(ms): " + ( endTime-startTime ) / 1E6 );
	}
	
	private void fillCsvFormatArray(){
		BufferedReader buffReader;
		String header;
		String line;
		try {
			buffReader = new BufferedReader( new FileReader( csvFile ) );
			header = buffReader.readLine(); // ignore header
			line = buffReader.readLine(); 			
			csvFormatArray = new CsvLineFormat( header, line );
			buffReader.close();	
		} catch ( FileNotFoundException e ) {
			e.printStackTrace();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}

	public void producer() throws InterruptedException, FileNotFoundException{
		
		String csvFile = "T201109PDP IEXT.CSV";//"T201202ADD REXT.CSV";
		BufferedReader buffReader = new BufferedReader( new FileReader( csvFile ) );
		 
		try {
			RawIextBatch lineBatch; // = new Batch();						
			boolean finished = false;
			String line = buffReader.readLine(); // ignore header
			
			fillCsvFormatArray();			
			while ( ! finished ) {
				lineBatch = new RawIextBatch();
				for ( int i = 0; ( (i < BATCH_LINES) && ( !finished ) ); i++ ) {
					line = buffReader.readLine();
					finished = !lineBatch.addLine( line );					
				}
				queue.put( lineBatch );
			} // end while	
			buffReader.close();	
		} catch (IOException e) {
			e.printStackTrace();
		}  // end catch
				
	}	
	
	public void consumer( String productName ) throws InterruptedException {
		RawIextBatch lineBatch = null;
		Iext IextObj;
		int batchLength = 0;
		
		do {
			lineBatch = queue.take();
			batchLength = lineBatch.getLength();
			PracticeActualSpendBatch psBatch = new PracticeActualSpendBatch();
			for ( int i = 0; i < batchLength; i++ ) {			
				String line = lineBatch.getLine( i );
				IextObj = new Iext( line, csvFormatArray );
				Double actCost = IextObj.getActCost();
				PracticeActualSpend practiceSpendItem = new PracticeActualSpend( IextObj.getPractice(), IextObj.getActCost() );
				psBatch.addItem( practiceSpendItem );
				if ( IextObj.getBnfName( line, csvFormatArray ).equalsIgnoreCase( productName ) ){
					productTotalItems += IextObj.getItems( line, csvFormatArray );
					productTotalCost += actCost;	
				}		
			}
			practiceSpendqueue.put( psBatch ); // consumePracticeSpendBatches takes from practiceSpendqueue  
		} while ( batchLength == BATCH_LINES );		
	}
	
	public void consumePracticeSpendBatches( RextList rextList ) throws InterruptedException {
		HashMap< String, Double > postCodeTotalSpend = new HashMap<>();
		HashMap< String, String > practicePostCode = new HashMap<>();
		
		while ( !rextList.filled() ) {
			Thread.sleep( 10 );
		}
		
		// add HashMap entries
		for ( int i = 0; i < rextList.getList().size(); i++ ){
			String postCode = rextList.getList().get( i ).getPostCode();
			String practiceId = rextList.getList().get( i ).getPracticeId();
			if ( !practicePostCode.containsKey( practiceId ) ){
				practicePostCode.put( practiceId, postCode );
			}
			if ( !postCodeTotalSpend.containsKey( postCode ) ){
				postCodeTotalSpend.put( postCode, 0.0 );	
			}
		}
		
		HashMap< String, Double > practiceTotalSpend = new HashMap<>();
		PracticeActualSpendBatch practiceSpendBatch = null;		
		Double currentSpend;
		PracticeActualSpend item;
		int batchLength = 0;
		do {
			practiceSpendBatch = practiceSpendqueue.take();
			batchLength = practiceSpendBatch.getLength();
			for ( int i = 0; i < batchLength; i++ ) {
				item = practiceSpendBatch.getItem( i );
				if ( practiceTotalSpend.containsKey( item.getPractice() ) ){
					currentSpend = practiceTotalSpend.get( item.getPractice() ); 
					practiceTotalSpend.put( item.getPractice(), currentSpend + item.getspendAmount() );
				} else { // create new entry
					practiceTotalSpend.put( item.getPractice(), item.getspendAmount() );
				}
			}
			
		} while ( batchLength == BATCH_LINES );	
		
		updatePostCodeTotalSpend( practiceTotalSpend, practicePostCode, postCodeTotalSpend );

		long s = System.nanoTime(); 
		pcRankDesc = sortDescPostCodeRankByTotalSpend( postCodeTotalSpend);
		System.out.println(  (System.nanoTime() - s)/1E6);
		
		System.out.println( "Top Five Post Codes with Highest Actual Spend;" );	
		for (int i = 0; i < 5; i++)
			System.out.println( i + ") " + pcRankDesc[ i ].getPostCode() + ", " + pcRankDesc[ i ].getTotalSpend() );
		
		for ( int i = pcRankDesc.length-1; ( ( i >= 0 ) && (pcRankDesc[ i ].getTotalSpend() == 0.00) ); i-- ) {
			postCodesWithZeroTotalSpend++;
		}
		System.out.println( "Postcodes with zero total spend: " + postCodesWithZeroTotalSpend );
	}
	
	private PostCodeRank[] sortDescPostCodeRankByTotalSpend( HashMap< String, Double > postCodeTotalSpend){
		PostCodeRank[] postCodeRank = new PostCodeRank[ postCodeTotalSpend.size() ];
		PostCodeRank item;
		String postCode;
		Double totalSpend;
		
		int rankIndex = 0;
		for ( Map.Entry< String, Double > pctsEntry : postCodeTotalSpend.entrySet() ) {
			postCode = pctsEntry.getKey();
			totalSpend = pctsEntry.getValue();			
			item = new PostCodeRank( postCode, totalSpend );
			postCodeRank[ rankIndex++ ] = item;
		}
		// Sort in descending order	
		Arrays.sort(postCodeRank, Collections.reverseOrder());
		
		return postCodeRank;
	}
	
	private void updatePostCodeTotalSpend( HashMap< String, Double> practiceTotalSpend, 
										   HashMap<String, String> practicePostCode,
										   HashMap<String, Double> postCodeTotalSpend ){
		String practice;
		Double value;
		String postCode;
		Double currentSpend;
		Double totalSpend;
		
		for ( Map.Entry< String, Double > ptsEntry : practiceTotalSpend.entrySet() ) {
			practice = ptsEntry.getKey();
			value = ptsEntry.getValue();
		    postCode = practicePostCode.get( practice );
		    if ( postCodeTotalSpend.containsKey( postCode ) ){
		    	currentSpend = postCodeTotalSpend.get( postCode );
		    	totalSpend = currentSpend + value;
		    	postCodeTotalSpend.put( postCode, totalSpend );
		    } else {
		    	postCodeTotalSpend.put( postCode, value );
		    }
		}
	}
	
	public void setProductTotalCost( float totalCost ){
		productTotalCost = totalCost;
	}
	
	public void setProductTotalItems( int totalItems ){
		productTotalItems = totalItems;
	}

	public float getProductTotalCost( String product ){
		return productTotalCost;
	}
	
	public float getProductTotalItems( String product ){
		
		return productTotalItems;
	}
	
	public float getAverageProductCost( String product ){
		return ( productTotalCost/productTotalItems );
	}
	
	public int getNumberOfCityPractices( String city ){
		return ( numberOfCityPractices );
	}
	
	public PostCodeRank[] getPostCodeRankDescByTotalSpend(){
		return ( pcRankDesc );
	}
	
	public int getNumberOfpostCodesWithZeroTotalSpend(){
		return ( postCodesWithZeroTotalSpend );
	}
	
	public void setNumberOfpostCodesWithZeroTotalSpend(int numPostCodes){
		postCodesWithZeroTotalSpend = numPostCodes;
	}
}


package abideFinancial;

import java.io.FileNotFoundException;
import java.io.IOException;


public class AbideFinancialCodingChallange {
	private static long startTime;
	private static long endTime;
	private int timeTakenMS;
	private GuiDisplay display;
	private BatchQueue lQueue;
	private volatile boolean finished;
	private float averageProductCost;
	private String productName;

	public static void main( String[] args ) {
		@SuppressWarnings("unused")
		AbideFinancialCodingChallange challenge = new AbideFinancialCodingChallange();
	}
	
	AbideFinancialCodingChallange(){
		timeTakenMS = 0;
		lQueue = new BatchQueue();
		setProductName( "Peppermint Oil" );
		display = new GuiDisplay( this );

	}
	
	public void threadHandler() {
		startTime = System.nanoTime();		
		RextList rextList= new RextList();
		finished = false;

		Thread stopWatchThread = new Thread( new Runnable() {
        	public void run(){
        		while ( !finished ){
        			setTimetakenMs( startTime );
        			display.updateTimer( getTimeTakenMs() );
        			try {
						Thread.sleep( 112 );
					} catch ( InterruptedException e ) {
						e.printStackTrace();
					}
        		}
			}
	    });
		
		Thread t0 = new Thread(new Runnable(){
        	public void run(){
				try {
					lQueue.producer1( rextList );
				}
				catch ( InterruptedException e ) {
					e.printStackTrace();
				} catch ( FileNotFoundException e ) {
					e.printStackTrace();
				} catch ( IOException e ) {
					e.printStackTrace();
				}		
			}
	    });
        
        Thread t1 = new Thread( new Runnable() {
        	public void run(){
        		try {
					lQueue.producer();
				} catch ( InterruptedException | FileNotFoundException e ) {
					e.printStackTrace();
				}
        	}
        });
        
        Thread t2 = new Thread( new Runnable(){
        	public void run(){
        		try {
        			lQueue.consumer( getProductName() );
				} catch ( InterruptedException e ) {
					e.printStackTrace();
				}
        	}
        });
    
        Thread processPracticeSpendBatches = new Thread( new Runnable() {
        	public void run(){
        		try {
        			lQueue.consumePracticeSpendBatches( rextList );
				} catch ( InterruptedException e ) {
					e.printStackTrace();
				}
        	}
        });
        
        t0.start();
        t1.start();
        t2.start();
        processPracticeSpendBatches.start();
        stopWatchThread.start();
        
        try {
        
        	t0.join();
        	t1.join();
       		t2.join();
       		processPracticeSpendBatches.join();
       		finished = true;
       		stopWatchThread.join();
        } catch ( InterruptedException e ) {
			e.printStackTrace();
		}
		
        setTimetakenMs( startTime );
        averageProductCost = lQueue.getAverageProductCost( getProductName() );
        System.out.println( "Product: " + getProductName() + ", "
                          + "Average Actual Cost: " + averageProductCost
                          + ", Time Taken (ms): " +  getTimeTakenMs() );
        
	}
	
	public int getTimeTakenMs(){
		return ( timeTakenMS );
	}
	
	private void setTimetakenMs( long startTime ){
		endTime = System.nanoTime();
		timeTakenMS = ( int ) Math.round( ( endTime - startTime ) / 1E6 );		
	}
	
	public BatchQueue getlQueue(){
    	return ( lQueue );
    }

	public String getProductName() {
		return productName;
	}

	public void setProductName( String productName ) {
		this.productName = productName;
	}
	
	// Accessory method : used for testing purposes
	public void listLondonPostCodes() throws IOException{
		String fileName = "London postcodes.csv";
		PostCodeShortCityMap postCodeMap = new PostCodeShortCityMap( fileName, "London" );
		
		System.out.println( postCodeMap.getMap().keySet() );
	}
}

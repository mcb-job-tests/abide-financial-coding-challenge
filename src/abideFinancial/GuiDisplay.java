package abideFinancial;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;



@SuppressWarnings("serial")
public class GuiDisplay extends JFrame implements ActionListener{

	  //////////////////////////// constants ////////////////////////////
	
	private final static String LOGO_IMAGE_FILE = "logo.png";
	private final static String WINDOW_TITLE = "Abide Financial :: Coding Challenge :: Solution by Michael Bell";
	private final int DRAW_LOGO_SLEEP_MS = 100;
	/** font */
	private final static Font guiDefaultFont = new Font( "Calibri Light", Font.BOLD, 20 );
	private final static Font guiSmallFont = new Font( "Calibri Light", Font.BOLD, 17 );
	private final static Color guiDefaultTextColour = Color.CYAN;
	
	/** Restart button */
    private final JButton restart = new JButton( "Start" );
    
    private static final int windowWidth = 800;
    private static final int windowHeight = ( int )( windowWidth * 0.6182 );
    private static final Color abideGreen = new Color ( 134, 191, 24 ); 
    private static final Color abideBrightGreen = new Color ( 0, 255, 128 );
    private final static Color shadeColour = new Color( 48, 48, 48 ); // grey
    
    
 	private AbideFinancialCodingChallange challenge;
 	private Container contentPane = getContentPane();   // get content pane
 	private boolean logoDrawn = false;
 	
 	GuiDisplay( AbideFinancialCodingChallange ch ){
    	challenge = ch;
    	initialise();       
        addWindowListener(                      // heed window events
            new WindowAdapter() {               // deal with window events
                public void windowClosing       // handle window close
                    ( WindowEvent event ) {
                        System.exit( 0 );         // exit on window close
                    }
            }
        );
        
    }
    
    private void initialise(){
    	setResizable( false );
    	JPanel buttonPanel = new JPanel();         // Restart buttons   	
    	contentPane.setLayout( new FlowLayout() ); // set flow layout
    	restart.addActionListener( this );           // listen for restart button
    	restart.setBackground( Color.GRAY );
    	restart.setForeground( Color.CYAN );
    	restart.setFont( guiDefaultFont );
    	restart.setVisible( false );
    	buttonPanel.add( restart );
        buttonPanel.setBackground( shadeColour ); //Color.CYAN);
        contentPane.setBackground( shadeColour );
        contentPane.add( buttonPanel );
        
        ImageIcon img = new ImageIcon( LOGO_IMAGE_FILE );
        setIconImage( img.getImage() );
        setTitle( WINDOW_TITLE ); // set window title
        setSize( windowWidth, windowHeight ); // set window size
        setLocationRelativeTo( null ); // centre window
        setVisible( true );
    }
  
    private void drawResult( int xOffset, int yOffset, Graphics2D g2 ) {
    	String productName = challenge.getProductName();
    	float floatValue = challenge.getlQueue().getAverageProductCost( productName );
    	String resultQ2 = String.format( "%.4f", floatValue );
    	String resultQ1 = challenge.getlQueue().getNumberOfCityPractices("London")
    			          + " (within Greater London)";
    	
    	g2.translate(xOffset, yOffset);
        g2.setFont(guiDefaultFont);
        g2.setColor(guiDefaultTextColour);
        
        if (restart.getText() == "Start") {
        	restart.setFocusable(false);
        	drawLogo(xOffset, yOffset, g2, DRAW_LOGO_SLEEP_MS, false);
        	logoDrawn = true;
        	restart.setVisible(true);
        } else {
        	logoDrawn = false;
        	g2.setColor(abideGreen);
        	g2.drawRect(0, 0, 735, 380);
        	g2.fillRect(5, 5, 165, 25);
        	g2.fillRect(5, 80, 165, 25);
        	g2.fillRect(5, 155, 165, 25);
        	g2.fillRect(5, 230, 165, 25);
        	g2.setColor(Color.BLACK);
        	g2.drawRect(5, 5, 165, 25);
        	g2.drawRect(5, 80, 165, 25);
        	g2.drawRect(5, 155, 165, 25);
        	g2.drawRect(5, 230, 165, 25);
        	g2.setColor(Color.GRAY);
        	g2.fillRect(5, 40, 165, 25);
        	g2.fillRect(5, 115, 165, 25);
        	g2.fillRect(5, 190, 165, 25);
        	g2.fillRect(5, 265, 165, 25);
        	g2.setColor(Color.CYAN);
        	g2.drawString( "Answer One", 20, 60);
        	g2.drawString( "Answer Two", 20, 135);
        	g2.drawString( "Answer Three", 20, 210);
        	g2.drawString( "Answer Four", 20, 285);        	
        	g2.setColor(Color.WHITE);
        	g2.drawString("Question One",20,25);
        	g2.setFont(guiSmallFont);
        	g2.setColor(abideBrightGreen);        	
        	g2.drawString("How many practices are in London?", 175, 25);
        	g2.setColor(Color.CYAN);
        	g2.setFont(guiDefaultFont);
        	g2.drawString( resultQ1, 175, 60);
        	g2.setColor(Color.WHITE);
        	g2.setFont(guiDefaultFont);
        	g2.drawString( "Question Two", 20, 100);
        	g2.setFont(guiSmallFont);   
        	g2.setColor(abideBrightGreen); 
        	g2.drawString( "What was the average actual cost of all peppermint oil prescriptions?", 175, 100);
        	g2.drawString( "Which 5 post codes have the highest actual spend?", 175, 175);        	
        	g2.setColor(Color.CYAN);
        	g2.setFont(guiDefaultFont);
        	g2.setColor(Color.WHITE);
        	g2.drawString( "Question Three", 20, 175);
        	g2.drawString( "Question Four", 20, 250);
        	g2.setColor(Color.CYAN);
        	g2.drawString( "£ " + resultQ2 + " (per unit)", 175, 135);
        	
        	// result Q3
        	PostCodeRank[] pcr = challenge.getlQueue().getPostCodeRankDescByTotalSpend();
        	for (int i = 0; i < 5; i++) {
        		g2.drawString( "" + pcr[i].getPostCode(), 175+(i*100), 210);
        	}
        	
        	// result Q4
        	g2.setFont(guiSmallFont);
        	g2.setColor(abideBrightGreen);         	
        	g2.drawString( "How many postcodes reported zero actual spend?", 175, 250);
        	
        	g2.setFont(guiDefaultFont);
        	g2.setColor(Color.CYAN);
        	g2.drawString("" + challenge.getlQueue().getNumberOfpostCodesWithZeroTotalSpend(), 175, 285);
        	updateTimer(challenge.getTimeTakenMs());
        	

        }
        g2.translate(-xOffset, -yOffset);
    }
    
    
	private void drawLogo(int xOffset, int yOffset, Graphics2D g2, int sleepMs, boolean fill) {
		try {
			xOffset += 150;
	    	yOffset -= 100;
	    	g2.setStroke(new BasicStroke(2.0f));
	    	int[] x1 = {117,143,38,24};
	        int[] y1 = {47,47,199,179};
			drawAbidePolygon(xOffset, yOffset, x1, y1, g2);
			Thread.sleep(sleepMs);
	        int[] x2 = {165,179,63,50};
	        int[] y2 = {46,66,234,214};
	        drawAbidePolygon(xOffset, yOffset, x2, y2, g2);
	        Thread.sleep(sleepMs);
	        int[] x3 = {190,203,125,194,117,73};
	        int[] y3 = {83,101,214,314,314,250};
	        drawAbidePolygon(xOffset, yOffset, x3, y3, g2);
	        Thread.sleep(sleepMs);
	        int[] x6 = {331, 344, 251, 225};
	        int[] y6 = {161, 179, 313, 313};
	        drawAbidePolygon(xOffset, yOffset, x6, y6, g2);
	        Thread.sleep(sleepMs);
	        int[] x5 = { 306, 320, 203, 190 };
	        int[] y5 = { 126, 145, 312, 294 };
	        drawAbidePolygon( xOffset, yOffset, x5, y5, g2 );
	        Thread.sleep( sleepMs );
	        int[] x4 = { 251, 296, 179, 166, 244, 175 };
	        int[] y4 = { 46, 110, 278, 258, 146, 46 };
	        drawAbidePolygon( xOffset, yOffset, x4, y4, g2 );
	        Thread.sleep( sleepMs );
	        g2.fillPolygon( x1, y1, x1.length );
	        Thread.sleep( sleepMs );
	        g2.fillPolygon( x2, y2, x2.length );
	        Thread.sleep( sleepMs );
	        g2.fillPolygon( x3, y3, x3.length );
	        Thread.sleep( sleepMs );
	        g2.fillPolygon( x6, y6, x6.length );
	        Thread.sleep( sleepMs );
	        g2.fillPolygon( x5, y5, x5.length );
	        Thread.sleep( sleepMs );
	        g2.fillPolygon( x4, y4, x4.length );
        } catch ( InterruptedException e ) {
			e.printStackTrace();
		}
	}
	private void offSetPolygon( int xOffset, int yOffset, int [] x, int [] y ) {
		for ( int i = 0; i < x.length; i++ ){
			x[ i ] += xOffset;
			y[ i ] += yOffset;
		}	
	}
	
	private void drawAbidePolygon( int xOffset, int yOffset, int [] x, int [] y, Graphics2D g2 ){
		offSetPolygon( xOffset, yOffset, x, y );
	    g2.setColor( Color.CYAN );
		g2.drawPolygon( x, y, x.length );
	}
	
    public void paint( Graphics g ) {
    	Graphics2D g2 = ( Graphics2D ) g;
        g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);  
        super.paint( g2 );
        drawResult( 32, 80, g2);
        challenge.getlQueue().setNumberOfpostCodesWithZeroTotalSpend(0);
    	challenge.getlQueue().setProductTotalCost( 0.0f );
    	challenge.getlQueue().setProductTotalItems( 0 );
      }
    
    public void updateTimer( int milliSecs ){
    	Graphics2D g2 = ( Graphics2D )this.getContentPane().getGraphics();
    	g2.setColor( shadeColour );
    	g2.fillRect( 320, 375, 150, 40 );
    	g2.setColor( Color.BLACK );
    	g2.drawRect( 319, 374, 152, 42 );
    	g2.setColor( guiDefaultTextColour );
    	g2.drawRect( 320, 375, 150, 40 );
    	g2.setFont( guiDefaultFont );
    	g2.drawString( "" + milliSecs + " ms", 360, 400 );
    }
    
    @Override
	public void actionPerformed( ActionEvent event ) {
    	if ( event.getSource() == restart ) {  // restart button clicked?		
    		if ( restart.getText() == "Start" ) {
            	if ( logoDrawn == true ) {            			
	            	challenge.threadHandler();
	            	restart.setText( "Back" );

            	}
            } else {
            	restart.setText( "Start" );
            	restart.setVisible( false );
            }
        }
    	repaint();
	}
}

package edu.iastate.gameoflife;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class GOLSaveLoadactivity extends Activity {

	private boolean spot1 = false;
	private boolean spot2 = false;
	private boolean spot3 = false;
	private boolean spot4 = false;
	private boolean spot5 = false;
	private int mode = 0;//1 for save, 2 for load
	private boolean[][] holder1;
	private boolean[][] holder2;
	private boolean[][] holder3;
	private boolean[][] holder4;
	private boolean[][] holder5;
	
	private boolean[][] temp;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activitysaveload);
	    // TODO Auto-generated method stub
	    
	    
//	    mode = (Integer) getIntent().getExtras().get("mode");
//	    
//	    if(mode == 1)
//	    	temp = (boolean[][]) getIntent().getExtras().get("board");
//	    
//	    
//	    
	    
	}
	
	
	

}

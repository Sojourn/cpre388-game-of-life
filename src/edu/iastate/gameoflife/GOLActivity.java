package edu.iastate.gameoflife;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

public class GOLActivity extends Activity {

	private GOLModel golModel;
	private GOLView golView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		golModel = new GOLModel(128, 128);
		golView = new GOLView(golModel, this);
		
		golModel.setCell(0, 0, true);
		golModel.setCell(127, 127, true);

		ScrollView vView = new ScrollView(this);
		HorizontalScrollView hView = new HorizontalScrollView(this);
		
		vView.addView(hView);
		hView.addView(golView);
		setContentView(vView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gol, menu);
		return true;
	}
}

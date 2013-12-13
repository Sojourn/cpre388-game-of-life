package edu.iastate.gameoflife;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

public class GOLActivity extends Activity {

	protected GOLModel golModel;
	protected GOLView golView;
	private GOLController golController;
	private Button runButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		golModel = new GOLModel(128, 128);
		createUI();

		golController = new GOLController(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gol, menu);
		return true;
	}

	private void createUI() {
		golView = new GOLView(golModel, this);

		ScrollView vView = new ScrollView(this);
		HorizontalScrollView hView = new HorizontalScrollView(this);
		LinearLayout root = new LinearLayout(this);
		LinearLayout buttonLayout = new LinearLayout(this);
		buttonLayout.setOrientation(LinearLayout.VERTICAL);

		Button advanceButton = new Button(this);
		advanceButton.setText("Advance");
		advanceButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				golModel.nextGeneration();
				golView.invalidate();
			}
		});

		runButton = new Button(this);
		runButton.setText("Start");
		runButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (golController.isSimulating()) {
					stopSimulation();
				} else {
					startSimulation();
				}
			}
		});

		Button fasterButton = new Button(this);
		fasterButton.setText("+");
		fasterButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				golController.increaseSpeed();
			}
		});

		Button slowerButton = new Button(this);
		slowerButton.setText("-");
		slowerButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				golController.decreaseSpeed();
			}
		});

		Button saveButton = new Button(this);
		saveButton.setText("Save Board");
		saveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				stopSimulation();

				final EditText input = new EditText(GOLActivity.this);
				new AlertDialog.Builder(GOLActivity.this)
						.setTitle("Save board as")
						.setMessage("")
						.setView(input)
						.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										saveBoard(input.getText().toString());
									}
								})
						.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										dialog.dismiss();
									}
								}).show();
			}
		});

		Button loadButton = new Button(this);
		loadButton.setText("Load Board");
		loadButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				stopSimulation();

				AlertDialog.Builder builderSingle = new AlertDialog.Builder(
						GOLActivity.this);
				builderSingle.setIcon(R.drawable.ic_launcher);
				builderSingle.setTitle("Select a board:-");
				final ArrayAdapter<File> arrayAdapter = new ArrayAdapter<File>(
						GOLActivity.this,
						android.R.layout.select_dialog_singlechoice,
						getFilesDir().listFiles());
				builderSingle.setNegativeButton("cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});

				builderSingle.setAdapter(arrayAdapter,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								final File file = arrayAdapter.getItem(which);
								AlertDialog.Builder builderInner = new AlertDialog.Builder(
										GOLActivity.this);
								builderInner.setMessage(file.getName());
								builderInner.setTitle("Load this board?");
								builderInner.setPositiveButton("Ok",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												loadBoard(file);

												dialog.dismiss();
											}
										}).setNegativeButton("Cancel",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.dismiss();
											}
										});
								builderInner.show();
							}
						});
				builderSingle.show();
			}

		});

		Button scanButton = new Button(this);
		scanButton.setText("Scan");
		scanButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				new IntentIntegrator(GOLActivity.this).initiateScan();
			}
		});
		
		buttonLayout.addView(advanceButton);
		buttonLayout.addView(runButton);
		buttonLayout.addView(fasterButton);
		buttonLayout.addView(slowerButton);
		buttonLayout.addView(saveButton);
		buttonLayout.addView(loadButton);
		buttonLayout.addView(scanButton);
		
		root.setOrientation(LinearLayout.HORIZONTAL);
		root.addView(buttonLayout);
		root.addView(vView);

		vView.addView(hView);
		hView.addView(golView);
		setContentView(root);
	}

	private void loadBoard(File file) {
		try {
			String json = GOLUtil.readFile(file);
			GOLModel model = GOLUtil.deserialize(new JSONObject(json));
			setModel(model);
		} catch (JSONException e) {
		}
	}

	private void setModel(GOLModel model) {
		golModel = model;
		golView.setModel(model);
		golView.onChange();
	}
	
	private void saveBoard(String name) {
		try {
			FileOutputStream out = openFileOutput(name, Context.MODE_PRIVATE);
			PrintStream printOut = new PrintStream(out);
			printOut.println(GOLUtil.serialize(golModel).toString());
		} catch (FileNotFoundException e) {
		}
	}

	private void startSimulation() {
		golController.startSimulating();
		runButton.setText("Stop");
	}

	private void stopSimulation() {
		golController.stopSimulating();
		runButton.setText("Start");
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		if(scanResult != null) {
			String url = scanResult.getContents();
			new PageFetcher() {
				@Override
				protected void onPostExecute(String result) {
					try {
						GOLModel loadedModel = GOLUtil.deserialize(new JSONObject(result));
						setModel(loadedModel);
					} catch (JSONException e) {
						Toast.makeText(GOLActivity.this, "Board load failed", Toast.LENGTH_SHORT).show();
					}
				}
			}.execute(url);
		}
	}
}

package edu.iastate.gameoflife;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ScaleGestureDetector.OnScaleGestureListener;

public class GOLView extends View implements GOLModel.ModelChangeListener,
		View.OnTouchListener {
	private static final int CELL_RADIUS = 32;
	private static final int CELL_DIAMETER = 2 * CELL_RADIUS;
	private static final Paint CELL_PAINT = new Paint(Paint.ANTI_ALIAS_FLAG);

	private final GOLModel model;

	public GOLView(GOLModel model, Context context) {
		super(context);
		setOnTouchListener(this);

		this.model = model;
		setMinimumWidth(model.getWidth() * CELL_DIAMETER);
		setMinimumHeight(model.getHeight() * CELL_DIAMETER);
	}

	@Override
	public void onChange() {
		invalidate();
	}

	@Override
	public void onCellChange(int x, int y) {
		Rect dirty = new Rect();
		dirty.left = x * CELL_DIAMETER;
		dirty.right = dirty.left + CELL_DIAMETER;
		dirty.top = y * CELL_DIAMETER;
		dirty.bottom = dirty.top + CELL_DIAMETER;

		invalidate(dirty);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawColor(Color.WHITE);

		for (int y = 0; y < model.getHeight(); y++) {
			for (int x = 0; x < model.getWidth(); x++) {
				if (model.getCell(x, y))
					drawCell(x, y, canvas);
			}
		}
	}

	private void drawCell(int x, int y, Canvas canvas) {
		canvas.drawCircle(x * CELL_DIAMETER + CELL_RADIUS, y * CELL_DIAMETER
				+ CELL_RADIUS, CELL_RADIUS, CELL_PAINT);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			return true;

		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			int x = (int) event.getX() / CELL_DIAMETER;
			int y = (int) event.getY() / CELL_DIAMETER;
			model.toggleCell(x, y);
			onCellChange(x, y);
			return true;

		}

		return false;
	}
}

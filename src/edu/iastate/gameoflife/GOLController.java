package edu.iastate.gameoflife;

import android.app.Activity;

public class GOLController {
	private final GOLActivity activity;
	private final Thread workerThread;
	private volatile boolean simulating;
	private volatile long waitDurationMillis;

	public GOLController(final GOLActivity activity) {
		this.activity = activity;
		this.simulating = false;
		this.waitDurationMillis = 200;

		workerThread = new Thread(new UpdateTask());
		workerThread.setDaemon(true);
		workerThread.start();
	}

	public void startSimulating() {
		simulating = true;
		wakeWorker();
	}

	public void stopSimulating() {
		simulating = false;
		wakeWorker();
	}

	public void increaseSpeed() {
		waitDurationMillis -= 50;
		if (waitDurationMillis < 50)
			waitDurationMillis = 0;
		wakeWorker();
	}

	public void decreaseSpeed() {
		waitDurationMillis += 50;
		if (waitDurationMillis > 5000)
			waitDurationMillis = 5000;
		wakeWorker();
	}

	public boolean isSimulating() {
		return simulating;
	}

	private void wakeWorker() {
		synchronized (workerThread) {
			workerThread.notifyAll();
		}
	}
	
	private class UpdateTask implements Runnable {
		@Override
		public void run() {
			for (;;) {
				try {
					while (!simulating) {
						synchronized (workerThread) {
							workerThread.wait();
						}
					}

					Thread.sleep(waitDurationMillis);

					activity.golModel.nextGeneration();
					activity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							activity.golView.invalidate();
						}

					});

				} catch (Exception e) {
				}

			}
		}
	}
}

package com.dnake.desktop;

import com.dnake.v700.dmsg;
import com.dnake.v700.utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class SysService extends Service {

	public static Context context = null;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		context = this;

		dmsg.setup_port();

		ProcessThread p = new ProcessThread();
		Thread t = new Thread(p);
		t.start();

		if (utils.eHome.watchdog)
			utils.eHome.broadcast(this);
	}

	public static class ProcessThread implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				utils.process();
			}
		}
	}
}

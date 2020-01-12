package com.dnake.v700;

import java.util.List;

import com.dnake.desktop.SysService;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

public class utils {

	//eHome为台湾中华电信eHome.apk特殊处理及接口
	public static final Boolean eHomeMode = false;

	public static String eRestart = null;

	public static class eHome {
		public static Boolean watchdog = eHomeMode;
		public static long ts = 0;
		public static int err = 6;
		public static long idle = 0;

		public static Boolean query(Context ctx) {
			ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningAppProcessInfo> r = am.getRunningAppProcesses();
			for (RunningAppProcessInfo p: r) {
				if (p.processName != null && p.processName.equals("com.cht.ehomev2"))
					return true;
			}
			return false;
		}

		public static void start(Context ctx) {
			PackageManager pm = ctx.getPackageManager();
			Intent it = pm.getLaunchIntentForPackage("com.cht.ehomev2");
			if (it != null)
				ctx.startActivity(it);
			System.out.println("Start com.cht.ehomev2");
		}

		public static void broadcast(Context ctx) {
			Intent it = new Intent("com.dnake.broadcast");
			it.putExtra("event", "com.dnake.talk.eHome.setup");
			it.putExtra("mode", true);
			ctx.sendBroadcast(it);
		}

		public static void process() {
			if (watchdog == false)
				return;

			if (idle != 0 && Math.abs(System.currentTimeMillis()-idle) >= 3*1000) {
				eHome.start(SysService.context);
				idle = 0;
			}

			if (eHome.query(SysService.context))
				err = 0;
			else
				err++;
			if (err > 5) {
				err = 0;
				eHome.start(SysService.context);
			}
		}
	}

	public static void process() {
		if (utils.eRestart != null && SysService.context != null) {
			dmsg req = new dmsg();
			dxml p = new dxml();
			p.setText("/params/name", utils.eRestart);
			req.to("/upgrade/system/kill", p.toString());

			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}

			PackageManager pm = SysService.context.getPackageManager();
			Intent it = pm.getLaunchIntentForPackage(utils.eRestart);
			if (it != null)
				SysService.context.startActivity(it);

			utils.eRestart = null;
		}

		eHome.process();
	}
}

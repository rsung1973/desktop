package com.dnake.desktop;

import com.dnake.v700.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SysReceiver extends BroadcastReceiver {

	public static int sms_nread = 0;
	public static int msg_nread = 0;
	public static int miss_nread = 0;

	public static int security = 0;

	@Override
	public void onReceive(Context ctx, Intent it) {
		String a = it.getAction();
		if (a.equals("android.intent.action.BOOT_COMPLETED")) {
			Intent intent = new Intent(ctx, SysService.class);
			ctx.startService(intent);
		} else if (a.equals("com.dnake.broadcast")) {
			String e = it.getStringExtra("event");
			if (e.equals("com.dnake.apps.sms")) {
				sms_nread = it.getIntExtra("nRead", 0);
			} else if (e.equals("com.dnake.talk.data")) {
				msg_nread = it.getIntExtra("msg", 0);
				miss_nread = it.getIntExtra("miss", 0);
			} else if (e.equals("com.dnake.security.data")) {
				security = it.getIntExtra("defence", 0);
			} else if (e.equals("com.dnake.talk.eHome.monitor")) {
				utils.eHome.watchdog = it.getBooleanExtra("enable", false);
			} else if (e.equals("com.dnake.talk.eHome.restart")) {
				utils.eRestart = it.getStringExtra("package");
			}
		}
	}
}

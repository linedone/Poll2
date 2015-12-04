package com.ust.poll.ui.dialog;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import com.linedone.poll.R;

public class AboutDialog {
	private AlertDialog about = null;
	private Context ctx = null;
	public AboutDialog(Context ctx) {
		init(ctx);
	}

	private void init(Context ctx) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		
		String versionName = "0.0.0";
		try {
			PackageInfo pinfo;
			pinfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
			// versionNumber = pinfo.versionCode;
			versionName = pinfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		builder.setTitle(R.string.app_name);
		builder.setCancelable(true);
		builder.setIcon(R.mipmap.ic_launcher);
		builder.setMessage(""+"\nVersion: "+versionName);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	                dialog.cancel();
	           }
	       });
		about = builder.create();
	}
	
	public void show(){
		if(about==null){
			init(ctx);
		}
		about.show();
	}
	
}

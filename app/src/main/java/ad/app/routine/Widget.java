package ad.app.routine;


import android.app.*;
import android.appwidget.*;
import android.content.*;
import android.os.*;
import android.widget.*;
import java.util.*;
import android.app.PendingIntent.*;

public class Widget extends AppWidgetProvider {
	public static RemoteViews views ;
	public void onReceive(final Context context, Intent intent_) {
		if(intent_.getAction()==AppWidgetManager.ACTION_APPWIDGET_ENABLED)
			new Toast(context,"Widget Service Is Activated");
		if(intent_.getAction()==AppWidgetManager.ACTION_APPWIDGET_DISABLED)
			new Toast(context,"Widget Service Is Stopped");
		Intent intentA=new Intent(context,MainService.class);
		PendingIntent pendingIntent=PendingIntent.getService(context, 0, intentA, PendingIntent.FLAG_CANCEL_CURRENT);
		try{
			pendingIntent.send();
		}catch (Exception e){}
		views= new RemoteViews(context.getPackageName(),R.layout.widget);
		//views.setOnClickPendingIntent(R.id.widgetTextView1, pendingIntent);
		/*
		String addOn="";
		if(intent_.getType()!=null)
		if(intent_.getType().contains("Clicked"))
			addOn="YOUR ROUTINE UPDATE\n(clicked)\n";
		else addOn="YOUR ROUTINE UPDATE\n\n";
		else addOn="YOUR ROUTINE UPDATE\n\n";
		views.setTextViewText(R.id.widgetTextView1,addOn+WidgetService.WidgetText);
		 /*/
		 views.setTextViewText(R.id.widgetTextView1,MainService.WidgetText);
		//*/
		update(context,views);
	}
	
	public void update(Context context, RemoteViews remoteViews) {
        AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context, Widget.class), remoteViews);
    }
	
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		update(context,views);
	}
	
}

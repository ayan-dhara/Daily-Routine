package ad.app.routine;

import android.app.*;
import android.appwidget.*;
import android.content.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import org.w3c.dom.*;

public class Widget3 extends AppWidgetProvider{
	public static RemoteViews views ;
	public void onReceive(final Context context, Intent Intent) {
		//toast(context,Intent.getExtras());
		if(Intent.getAction()==AppWidgetManager.ACTION_APPWIDGET_ENABLED)
			new Toast(context,"Widget Service2 Is Activated");
		if(Intent.getAction()==AppWidgetManager.ACTION_APPWIDGET_DISABLED)
			new Toast(context,"Widget Service2 Is Stopped");
		Intent=new Intent(context,MainService.class);
		PendingIntent pendingIntent=PendingIntent.getService(context, 0, Intent, PendingIntent.FLAG_CANCEL_CURRENT);
		try{
			pendingIntent.send();
		}catch (Exception e){}
		Intent = new Intent();
        Intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
		pendingIntent = PendingIntent.getBroadcast(context, 0, Intent, PendingIntent.FLAG_UPDATE_CURRENT);//*/
		views= new RemoteViews(context.getPackageName(),R.layout.widget3);
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
		String Text="";
		if(MainService.times!=null&&MainService.tasks!=null){
			for(int i=4;i<15;i++)
				try{
					Text+="["+MainService.time(MainService.times.get(i),true)+"]";
					if(i==5)Text+="\n-----NOW-----";
					Text+="\n"+MainService.tasks.get(i)+"\n\n";
				}catch(Exception e){}
			if(MainService.tasks.size()>0&&MainService.times.size()>0){}
			else Text="Setup your routine first";
		}
		if(Text.length()>0)
			views.setTextViewText(R.id.widget2TextView1,Text);
		ViewGroup viewGroup=null;
		View view=views.apply(context,viewGroup);
		TextView textView=((TextView)((LinearLayout)view).getChildAt(0));
		//toast(context,textView);
		try{
			textView.setText(Text);
			//toast(context,"it's ok");
		}catch(Exception e){new Toast(context,e);}

		//*/
		update(context,views);
	}

	public void update(Context context, RemoteViews remoteViews) {
        AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context, Widget3.class), remoteViews);
    }

	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		update(context,views);
	}
}

package ad.app.routine;

import android.app.*;
import android.appwidget.*;
import android.content.*;
import android.widget.*;
import com.google.gson.*;
import com.google.gson.reflect.*;
import java.util.*;
import android.graphics.drawable.*;
import android.util.*;

public class MainService extends Service{
	private static TimerTask TimerTask1=null;
	private static Context context=null;
	public static int lastAccessedNoificationTime=0;
	private static TimerTask TimerTask2=null;
	private static long lastRoutineUpdatedTimeMills=0;
	private static SharedPreferences sharedPreferences;
	public static ArrayList<Integer> times=new ArrayList<Integer>();
	public static ArrayList<String> tasks=new ArrayList<String>();
	private static ArrayList<Object[]> Routine=new ArrayList<Object[]>();
	public static String WidgetText="Some Error Detected !! \n";
	public static String alertTitle="Updating routine",alertMessage="please wait...";
	public static NotificationManager notificationService;
	
	//private static Intent startIntent=null;
	public void onCreate(){
		super.onCreate();
		context=this;
		startTimer();
		variableSetup();
	}
	
	private void startTimer(){
		TimerTask tt=TimerTask1;
		try{
			TimerTask1 = new TimerTask(){
				public void run(){//*/
					try{
						Intent intent=new Intent();
						intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
						//intent.setType(intent.getType());
						PendingIntent pendingIntent=PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
						pendingIntent.send();
					}catch(Exception e){}
					//WidgetText=System.currentTimeMillis()+"\n";
				}
			};
			(new Timer()).schedule(TimerTask1,10000,10000);
			tt.cancel();
		}catch(Exception e){}//*/
	}
	
	
	
	private void variableSetup(){
		int indx=0;
		sharedPreferences=getSharedPreferences("Data_string",0);
		boolean textable=true;
		if(Routine==null||lastRoutineUpdatedTimeMills+60000<System.currentTimeMillis()){
			lastRoutineUpdatedTimeMills=System.currentTimeMillis();
			Recover();
			textable=false;
		}
		Calendar calendar=Calendar.getInstance();
		Date date=calendar.getTime();
		int day=date.getDay();
		int hour=date.getHours();
		int minutes=date.getMinutes();
		int time=time(day,hour,minutes);
		int time_=0;
		Object[] O=new Object[]{};
		for(Object[] o: Routine){
			int _time= (int) o[0];
			if(time<_time){
				lastAccessedNoificationTime=time_;
				alertTitle=time(time_)+" to "+time(_time);
				break;
			}
			indx++;
			O=o;
			time_=_time;
			alertMessage=(String)o[1];
		}
		if(Routine.size()>0){
			if(time<(int)(Routine.get(0)[0])||time>(int)(Routine.get(Routine.size()-1)[0])){
				alertTitle=time((int)(Routine.get(Routine.size()-1)[0]))+" to "+time((int)(Routine.get(0)[0]));
				alertMessage=(String)(Routine.get(Routine.size()-1)[1]);
			}
		}
		else{
			alertTitle="ALERT !!";
			alertMessage="Setup the routine first"; 
		}
		WidgetText="";
		//logi(indx);
		if(indx>0)indx--;
		else indx=Routine.size()-1;
		//logd(indx);
		if(Routine.size()>0&&textable){
			WidgetText+="YOUR ROUTINE UPDATE\n\n";
			for(int j=0;j<5;j++){
				Object[] o1=Routine.get((indx+j)%Routine.size());
				Object[] o2=Routine.get((indx+j+1)%Routine.size());
				//logd(j);
				WidgetText+=time((Integer) o1[0],true)+" to "+time((Integer) o2[0],true)+"\n"+o1[1]+"\n-------\n";
			}
		}
		else WidgetText="Set up your routine first";
		if(Routine.size()>0){
			int index=indx;
			times=new ArrayList<Integer>();
			tasks=new ArrayList<String>();
			index=index-5;
			while(index<0)index+=Routine.size();
			index=index%Routine.size();
			for(int i=0;i<15;i++){
				Object[] o=Routine.get((index+i)%Routine.size());
				times.add((int)o[0]);
				tasks.add((String)o[1]);
				if(i==5&&sharedPreferences.getBoolean("Notify",false))Notify(this,(NotificationManager)getSystemService(NOTIFICATION_SERVICE),"Alert",String.valueOf(o[1]),android.R.drawable.ic_dialog_info);
			}
		}
		textable=true;
		indx=0;
	}
	
	public static void Notify(Context context,NotificationManager notificationManager,String title,String message,int icon){
		//RemoteViews rView=new RemoteViews(context.getPackageName(),R.layout.widget2);
		//rView.setTextViewText(R.id.widget2TextView1,message);
		Notify(notificationManager,new Notification.Builder(context)
		.setContentTitle(title)
		.setContentText(message)
		.setSmallIcon(icon)
		//.setLargeIcon(Icon.createWithResource(context,android.R.drawable.ic_delete))
		//.setContent(rView)
		.build());
	}
	
	public static void Notify(NotificationManager notificationManager,Notification notification){
		notificationManager.notify(0xff,notification);
	}
	
	public static void Recover(){
		if(sharedPreferences!=null){
			ArrayList<Object[]> TaskArray=new ArrayList<Object[]>() ;
			ArrayList<Integer> TimeArray=new ArrayList<Integer>();
			Routine=new ArrayList<Object[]>();
			String[] times=new String[]{"sundayTimeArray","mondayTimeArray","tuesdayTimeArray","wednesdayTimeArray","thursdayTimeArray","fridayTimeArray","saturdayTimeArray"};
			String[] tasks=new String[]{"sundayTaskArray","mondayTaskArray","tuesdayTaskArray","wednesdayTaskArray","thursdayTaskArray","fridayTaskArray","saturdayTaskArray"};
			for(int i=0;i<times.length;i++){
				TaskArray=Put(tasks[i]);
				TimeArray=PutT(times[i]);
				for(int j=0;j<TimeArray.size();j++){
					Object[] objs=new Object[1+TaskArray.get(j).length];
					objs[0]=TimeArray.get(j)+(i*24*60);
					int k=0;
					for(Object obj:TaskArray.get(j))
						objs[++k]=obj;
					Routine.add(objs);
				}
			}
		}
	}
	
	private static ArrayList<Object[]> Put(String key){
		String str=sharedPreferences.getString(key,null);
		ArrayList<Object[]> var=(new Gson().fromJson(str,new TypeToken<ArrayList<Object[]>>(){}.getType()));
		return var==null?new ArrayList<Object[]>():var;
	}

	private static ArrayList<Integer> PutT(String key){
		String str=sharedPreferences.getString(key,null);
		ArrayList<Integer> var=new Gson().fromJson(str,new TypeToken<ArrayList<Integer>>(){}.getType());
		return var==null?new ArrayList<Integer>():var;
	}

	private static int time(int _day,int _hour,int _minute){
		return time((_day*24)+_hour,_minute);
	}
	private static int time(int _hour,int _minute){
		return ((_hour*60)+_minute);
	}
	public static String time(int time,boolean...day){
		if(time>(7*24*60)){
			return null;
		}
		String timeString="";
		if(day.length>0){
			boolean b=true;
			for(boolean a:day)b=b&&a;
			if(b){
				if(day.length==1){
					int t=(int)(time/(24*60));
					String[] days=new String[]{"Sun,","Mon,","Tue,","Wed,","Thu,","Fri,","Sat,",null,null};
					String d=days[t];
					timeString+=d;
				}
				if(day.length==2){
					int t=(int)(time/(24*60));
					String[] days=new String[]{"Sunday,","Monday,","Tuesday,","Wednesday,","Thrusday,","Friday,","Saturday,",null,null};
					String d=days[t];
					timeString+=d;
				}
			}
		}
		time=time%(24*60);
		int hr=(int)(time/60),min=(int)(time%60);
		String div="p.m.";
		if(hr<12){
			div="a.m.";
		}
		hr=hr%12;
		if(hr==0)hr=12;
		timeString+=((hr<10)?("0"+hr):hr)+":"+((min<10)?("0"+min):min)+" "+div;
		return timeString;
	}
	
	public void onStart(Intent intent, int startId){
		super.onStart(intent, startId);
		//startIntent=intent;
		onCreate();
	}
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		onStart(intent,startId);
		return super.onStartCommand(intent, flags, startId);
	}
	private void logd(Object o){
		//Log.d(this.getClass().getSimpleName(),String.valueOf(o));
	}
	private void logi(Object o){
		//Log.i(this.getClass().getSimpleName(),String.valueOf(o));
	}
	private void loge(Object o){
		Log.e(this.getClass().getSimpleName(),String.valueOf(o));
	}
	private void logw(Object o){
		Log.w(this.getClass().getSimpleName(),String.valueOf(o));
	}
}

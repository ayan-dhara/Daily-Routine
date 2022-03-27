package ad.app.routine;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import android.text.*;
import com.google.gson.*;
import com.google.gson.reflect.*;
import android.view.animation.*;

public class MainActivity2 extends MainActivity
{
	public static LinearLayout linear;
	public static ArrayList<Object[]> sundayTaskArray=new ArrayList(),mondayTaskArray=new ArrayList(),tuesdayTaskArray=new ArrayList(),wednesdayTaskArray=new ArrayList(),thursdayTaskArray=new ArrayList(),fridayTaskArray=new ArrayList(),saturdayTaskArray=new ArrayList();
	public static ArrayList<Integer> sundayTimeArray=new ArrayList(),mondayTimeArray=new ArrayList(),tuesdayTimeArray=new ArrayList(),wednesdayTimeArray=new ArrayList(),thursdayTimeArray=new ArrayList(),fridayTimeArray=new ArrayList(),saturdayTimeArray=new ArrayList();
	public static SharedPreferences sharedPreferences;
	
	protected void onHandleIntent(Intent p1){
		if(APPLICATION_CONTEXT==null)
			APPLICATION_CONTEXT=this;
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
	
	public static void RecoverVariables(){
		sundayTimeArray=PutT("sundayTimeArray");
		mondayTimeArray=PutT("mondayTimeArray");
		tuesdayTimeArray=PutT("tuesdayTimeArray");
		wednesdayTimeArray=PutT("wednesdayTimeArray");
		thursdayTimeArray=PutT("thursdayTimeArray");
		fridayTimeArray=PutT("fridayTimeArray");
		saturdayTimeArray=PutT("saturdayTimeArray");

		sundayTaskArray=Put("sundayTaskArray");
		mondayTaskArray=	Put("mondayTaskArray");
		tuesdayTaskArray=Put("tuesdayTaskArray");
		wednesdayTaskArray=Put("wednesdayTaskArray");
		thursdayTaskArray=Put("thursdayTaskArray");
		fridayTaskArray=Put("fridayTaskArray");
		saturdayTaskArray=Put("saturdayTaskArray");
	}
	
	public static void clear(ArrayList...list){
		for(ArrayList item:list){
			if(item==null){
				item=new ArrayList();
			}
			item.clear();
		}
	}
	
	public static void load(final Context c,final LinearLayout linearLayout){
		TextView tv;
		String str="__";
		final ArrayList<Object[]> taskArray;
		ArrayList<Object[]> taskArray1;
		taskArray1 =new ArrayList<Object[]>();
		final ArrayList<Integer> timeArray;
		ArrayList<Integer> timeArray1;
		timeArray1 =new ArrayList<Integer>();
		APPLICATION_CONTEXT=c;
		linear=linearLayout;
		linear.removeAllViews();
		if(linearLayout==sundayLinearLayout){
			str="Sunday";
			taskArray1 =sundayTaskArray;
			timeArray1 =sundayTimeArray;
		}
		else if(linearLayout==mondayLinearLayout){
			str="Monday";
			taskArray1 =mondayTaskArray;
			timeArray1 =mondayTimeArray;
		}
		else if(linearLayout==tuesdayLinearLayout){
			str="Tuesday";
			taskArray1 =tuesdayTaskArray;
			timeArray1 =tuesdayTimeArray;
		}
		else if(linearLayout==wednesdayLinearLayout){
			str="Wednesday";
			taskArray1 =wednesdayTaskArray;
			timeArray1 =wednesdayTimeArray;
		}
		else if(linearLayout==thursdayLinearLayout){
			str="Thrusday";
			taskArray1 =thursdayTaskArray;
			timeArray1 =thursdayTimeArray;
		}
		else if(linearLayout==fridayLinearLayout){
			str="Friday";
			taskArray1 =fridayTaskArray;
			timeArray1 =fridayTimeArray;
		}
		else if(linearLayout==saturdayLinearLayout){
			str="Saturday";
			taskArray1 =saturdayTaskArray;
			timeArray1 =saturdayTimeArray;
		}

		timeArray = timeArray1;
		taskArray = taskArray1;
		Arrange(timeArray,taskArray);

		for(int i=0;i<timeArray.size();i++){
			Object[] o=taskArray.get(i);
			tv=TextView();
			Animation animation=AnimationUtils.loadAnimation(APPLICATION_CONTEXT,android.R.anim.slide_in_left);
			animation.setStartOffset(100*i);
			animation.initialize(500,500,1000,100);
			tv.setAnimation(animation);
			tv.setText(fromTime(timeArray.get(i))+"\n"+o[0]);
			linear.addView(tv);
			int finalI = i;
			tv.setOnClickListener(new TextView.OnClickListener(){
					public void onClick(View p1) {
						update((TextView)p1,taskArray,timeArray, finalI);
					}
				});
		}
		tv=TextView();
		tv.setText("+ Add New");
		tv.setTextSize(19);
		tv.setGravity(Gravity.CENTER);
		tv.setPadding(10,10,10,10);
		tv.setOnClickListener(new TextView.OnClickListener(){
				public void onClick(View p1) {
					insert(linear,taskArray,timeArray);
				}
		});
		linear.addView(tv);
	}
	
	
	public static void Arrange(ArrayList<Integer> list,ArrayList<Object[]> tasks){
		int[] intList=new int[list.size()];
		Object[] taskList=new Object[tasks.size()];
		
		int index=0;
		for(int no:list){
			intList[index]=no;
			index++;
		}
		index=0;
		for(Object obj:tasks){
			taskList[index]=obj;
			index++;
		}
		
		for(int i=0;i<intList.length;i++){
			for(int j=i;j<intList.length;j++){
				if(intList[i]>intList[j]){
					int temp=intList[j];
					Object tempo=taskList[j];
					intList[j]=intList[i];
					taskList[j]=taskList[i];
					intList[i]=temp;
					taskList[i]=tempo;
				}
			}
		}
		list.clear();
		tasks.clear();
		for(int i=0;i<intList.length;i++){
			if(i>0)if(intList[i-1]==intList[i]){
				list.remove(list.size()-1);
				tasks.remove(tasks.size()-1);
			}
			list.add(intList[i]);
			tasks.add((Object[])taskList[i]);
		}
	}
	
	public static String fromTime(int time){
		String timeString="";
		time=time%(24*60);
		int hr=time/60,min=time%60;
		String div="p.m.";
		if(hr<12){
			div="a.m.";
		}
		hr=hr%12;
		if(hr==0)hr=12;
		timeString=((hr<10)?("0"+hr):hr)+":"+((min<10)?("0"+min):min)+" "+div;
		return timeString;
	}
	public static String fromTime(int hr,int min){
		String timeString="";
		String div="p.m.";
		if(hr<12){
			div="a.m.";
		}
		hr=hr%12;
		if(hr==0)hr=12;
		timeString=((hr<10)?("0"+hr):hr)+":"+((min<10)?("0"+min):min)+" "+div;
		return timeString;
	}
	public static int[] toTime(CharSequence timeString){
		return toTime(timeString.toString());
	}
	public static int[] toTime(String timeString){
		int[] time=new int[2];
		char[] validChars=new char[]{'0','1','2','3','4','5','6','7','8','9',':'};
		int div=0;
		int hr=0;
		int min=0;
		if(timeString.indexOf(".m.")>-1){
			if(timeString.indexOf("a.m.")>-1)div=0;
			else if(timeString.indexOf("p.m.")>-1)div=1;
			div=div*12;

			int in=timeString.indexOf(":");
			hr=Integer.parseInt(timeString.substring(0,in));
			min=Integer.parseInt(timeString.substring(in+1,in+3));
			if(hr==12)hr=0;

			hr=hr+div;
		}
		else{
			for(int i=0;i<timeString.length();i++){
				boolean detected=false;
				for(int j=0;j<validChars.length;j++){
					if(timeString.charAt(i)==validChars[j]){
						detected=true;
					}
				}
				if(!detected){
					toast("invalid time format");
					return time;
				}
			}
			int i=timeString.indexOf(":");
			hr=Integer.parseInt(timeString.substring(0,i));
			min=Integer.parseInt(timeString.substring(i+1,timeString.length()));
		}
		time[0]=hr;
		time[1]=min;
		return time;
	}
	
	public static TextView TextView(){
		final TextView tv=new TextView(APPLICATION_CONTEXT);
		tv.setTextSize(18);
		tv.setTextColor(Color.parseColor("#00aaff"));
		tv.setPadding(8,8,8,8);
		tv.setBackgroundColor(Color.WHITE);
		TextView tv1=new TextView(APPLICATION_CONTEXT);
		tv1.setLayoutParams(new ViewGroup.LayoutParams(-1, 1));
		tv1.setBackgroundColor(Color.parseColor("#00aaff"));
		linear.addView(tv1);
		return tv;
	}
	
	public static ArrayList filter(ArrayList al,Object Type){
		ArrayList a=new ArrayList();
		for(Object o:al){
			if(isOfSameType(o,Type))a.add(o);
		}
		return a;
	}
	
	public static boolean isOfSameType(Object o1,Object o2){
		if(o1.getClass()==o2.getClass())return true;
		return false;
	}
	
	public static boolean isFromClass(Object o,Class<?> c){
		if(o.getClass()==c)return true;
		return false;
	}

	private static void logd(Object o){
		Log.d(APPLICATION_CONTEXT.getClass().getSimpleName(),String.valueOf(o));
	}
	private static void logi(Object o){
		Log.i(APPLICATION_CONTEXT.getClass().getSimpleName(),String.valueOf(o));
	}
	private static void loge(Object o){
		Log.e(APPLICATION_CONTEXT.getClass().getSimpleName(),String.valueOf(o));
	}
	private static void logw(Object o){
		Log.w(APPLICATION_CONTEXT.getClass().getSimpleName(),String.valueOf(o));
	}
	
	public static int getRandom(int min,int max){
		return (int)(min+((max-min)*Math.random()));
	}
	
	public static void alert(Object...o)
	{
		for(int i=0;i<o.length;i++){
			new AlertDialog.Builder(APPLICATION_CONTEXT).setMessage(String.valueOf(o[i]))
				.setPositiveButton("Ok",new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface di,int p){

					}
				})
				.setTitle("Alert")
				.create().show();
		}
	}
/*
	public static void toast(Object...o)
	{
		for(int i=0;i<o.length;i++){
			Toast.makeText(APPLICATION_CONTEXT, String.valueOf(o[i]), 1500).show();
		}
	}
	*/
	public MainActivity2(){
		super();
	}
}

package ad.app.routine;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.text.*;
import android.util.*;
import android.util.Base64;
import android.view.*;
import android.widget.*;
import com.google.gson.*;
import com.google.gson.reflect.*;
import java.io.*;
import java.util.*;

public class MainActivity extends Activity
{
	private static Intent intent;
	private static LinearLayout mainLinearLayout;
	public static LinearLayout sundayLinearLayout,mondayLinearLayout,tuesdayLinearLayout,wednesdayLinearLayout,thursdayLinearLayout,fridayLinearLayout,saturdayLinearLayout;
	private static ArrayList<TextView> textViewArray=new ArrayList<TextView>();
	private static ArrayList<LinearLayout> linearLayoutArray=new ArrayList<LinearLayout>();
	public static Context APPLICATION_CONTEXT;
	private static boolean Serviceopened=false;
	private static NotificationManager notificationManager;
	private static SharedPreferences sharedPreferences;
	public static ArrayList<Object[]> Routine=new ArrayList<Object[]>();
	
	
	
	protected void onCreate(Bundle savedInstanceState){
		//ADRTLogCatReader.onContext(this, "ad.app.log");
        super.onCreate(savedInstanceState);
		notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		MainActivity2.sharedPreferences=getSharedPreferences("Data_string",0);
		APPLICATION_CONTEXT=this;
		
		if(sharedPreferences==null){
			sharedPreferences=APPLICATION_CONTEXT.getSharedPreferences("Data_string",0);
		}
		
		sundayLinearLayout=new LinearLayout(this);
		mondayLinearLayout=new LinearLayout(this);
		tuesdayLinearLayout=new LinearLayout(this);
		wednesdayLinearLayout=new LinearLayout(this);
		thursdayLinearLayout=new LinearLayout(this);
		fridayLinearLayout=new LinearLayout(this);
		saturdayLinearLayout=new LinearLayout(this);
		
		ScrollView scrollView=new ScrollView(this);
		mainLinearLayout = new LinearLayout(this);
		
		RelativeLayout rl=new RelativeLayout(this);
		rl.setLayoutParams(new ViewGroup.LayoutParams(-1,-1));
		scrollView.setLayoutParams(new ViewGroup.LayoutParams(-1,-1));
		rl.addView(scrollView);
		setContentView(rl);
		scrollView.addView(mainLinearLayout);
		mainLinearLayout.setOrientation(LinearLayout.VERTICAL);
		mainLinearLayout.setPadding(0,0,0,50);
		scrollView.setBackgroundColor(Color.WHITE);
		mainLinearLayout.setBackgroundColor(Color.WHITE);
		
		MainActivity2.RecoverVariables();
		/*
		TextView tv=new TextView(this);
		tv.setTop(80);
		tv.layout(50,50,50,50);
		ViewGroup.LayoutParams lp=new ViewGroup.LayoutParams(-1,-1);
		tv.setLayoutParams(lp);
		tv.setGravity(Gravity.CENTER);
		tv.setGravity(Gravity.BOTTOM);
		rl.addView(tv);
		tv.setText("© CopyRight Reaserved");
		//*/
		LoadDay(sundayLinearLayout);
		LoadDay(mondayLinearLayout);
		LoadDay(tuesdayLinearLayout);
		LoadDay(wednesdayLinearLayout);
		LoadDay(thursdayLinearLayout);
		LoadDay(fridayLinearLayout);
		LoadDay(saturdayLinearLayout);
		
		TextView tv1=new TextView(this);
		tv1.setLayoutParams(new ViewGroup.LayoutParams(-1, 5));
		tv1.setBackgroundColor(Color.WHITE);
		mainLinearLayout.addView(tv1);
		
		setButtons();
		if(MainActivity2.sharedPreferences.getAll().size()==0)
			toast("Welcome");
		//***********************************
		//*/
    }
	
	public void LoadDay(final LinearLayout linearLayout){
		TextView tv = textView();
		textViewArray.add(tv);
		linearLayoutArray.add(linearLayout);
		String str="";
		if(linearLayout==sundayLinearLayout)str="Sunday";
		else if(linearLayout==mondayLinearLayout)str="Monday";
		else if(linearLayout==tuesdayLinearLayout)str="Tuesday";
		else if(linearLayout==wednesdayLinearLayout)str="Wednesday";
		else if(linearLayout==thursdayLinearLayout)str="Thrusday";
		else if(linearLayout==fridayLinearLayout)str="Friday";
		else if(linearLayout==saturdayLinearLayout)str="Saturday";
		
		tv.setText(str);
		mainLinearLayout.addView(tv);
		try{
			mainLinearLayout.addView(linearLayout);
		}catch(Exception e){toast(e);}
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setVisibility(LinearLayout.GONE);
		tv.setOnClickListener(new TextView.OnClickListener(){
				public void onClick(View p1)
				{
					if (linearLayout.getAlpha() > 200)
					{
						linearLayout.setAlpha(0);
						linearLayout.setVisibility(LinearLayout.GONE);
						linearLayout.removeAllViews();
						for(TextView tv:textViewArray){
							tv.setBackgroundColor(Color.parseColor("#00aaff"));
						}
					}
					else
					{
						for(LinearLayout l:linearLayoutArray){
							l.setAlpha(0);
							l.setVisibility(LinearLayout.GONE);
							l.removeAllViews();
						}
						((TextView)p1).setBackgroundColor(Color.parseColor("#0090ff"));
						linearLayout.setAlpha(1);
						linearLayout.setVisibility(LinearLayout.VISIBLE);
						//APPLICATION_CONTEXT.startService(new Intent(APPLICATION_CONTEXT,LoadingService.class));
						MainActivity2.load(APPLICATION_CONTEXT, linearLayout);
						//*/
					}
				}
			});
	}
	
	public static void insert(final LinearLayout l,final ArrayList<Object[]> task,final ArrayList<Integer> times){
		final EditText et=new EditText(APPLICATION_CONTEXT);
		final TextView tv=new TextView(APPLICATION_CONTEXT);
		final LinearLayout ll=new LinearLayout(APPLICATION_CONTEXT);
		int h=0,m=0;
		if(times.size()>0){
			m=times.get(times.size()-1)%60;
			h=(times.get(times.size()-1)/60)%24;
		}
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setGravity(Gravity.CENTER);
		tv.setTextSize(30);
		tv.setTextColor(Color.parseColor("#00aaff"));
		tv.setLayoutParams(new ViewGroup.LayoutParams(-1,-2));
		tv.setGravity(Gravity.CENTER);
		ll.addView(tv);
		ll.addView(et);
		et.setTextColor(Color.parseColor("#00aaff"));
		et.setBackgroundColor(Color.WHITE);
		et.setSingleLine();
		tv.setBackgroundColor(Color.WHITE);
		tv.setText(MainActivity2.fromTime(h,m));
		et.setText("");
		tv.setOnClickListener(new TextView.OnClickListener(){
				public void onClick(View v1){
					TimePickerDialog tpd=new TimePickerDialog(APPLICATION_CONTEXT,new TimePickerDialog.OnTimeSetListener(){
							public void onTimeSet(TimePicker tp,int h,int m){
								tv.setText(MainActivity2.fromTime(h,m));
							}
						},MainActivity2.toTime(tv.getText())[0],MainActivity2.toTime(tv.getText())[1],false);
					tpd.show();
				}
			});
		AlertDialog ad=(new AlertDialog.Builder(APPLICATION_CONTEXT))
			.setView(ll)
			.setTitle("Edit Information ")
			.setPositiveButton("Save",new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface di,int p){
					Editable text=et.getText();
					//v.setText(tv.getText()+"\n"+text.toString());
					Object[] o=new Object[]{text.toString()};
					task.add(o);
					times.add((MainActivity2.toTime(tv.getText())[0]*60)+MainActivity2.toTime(tv.getText())[1]);
					MainActivity2.load(APPLICATION_CONTEXT,l);
				}
			})
			.create();
		ad.show();
	}

	public static void update(final TextView v,final ArrayList<Object[]> tasks,final ArrayList<Integer> times,final int index){
		final EditText et=new EditText(APPLICATION_CONTEXT);
		final TextView tv=new TextView(APPLICATION_CONTEXT);
		final LinearLayout ll=new LinearLayout(APPLICATION_CONTEXT);
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setGravity(Gravity.CENTER);
		tv.setTextSize(30);
		tv.setTextColor(Color.parseColor("#00aaff"));
		tv.setLayoutParams(new ViewGroup.LayoutParams(-1,-2));
		tv.setGravity(Gravity.CENTER);
		ll.addView(tv);
		ll.addView(et);
		et.setTextColor(Color.parseColor("#00aaff"));
		et.setBackgroundColor(Color.WHITE);
		et.setSingleLine();
		tv.setBackgroundColor(Color.WHITE);
		tv.setText(MainActivity2.fromTime(times.get(index)));
		et.setText(String.valueOf(tasks.get(index)[0]));
		tv.setOnClickListener(new TextView.OnClickListener(){
			public void onClick(View v1){
				TimePickerDialog tpd=new TimePickerDialog(APPLICATION_CONTEXT,new TimePickerDialog.OnTimeSetListener(){
					public void onTimeSet(TimePicker tp,int h,int m){
						tv.setText(MainActivity2.fromTime(h,m));
					}
				},MainActivity2.toTime(tv.getText())[0],MainActivity2.toTime(tv.getText())[1],false);
				tpd.show();
			}
		});
		AlertDialog ad=(new AlertDialog.Builder(APPLICATION_CONTEXT))
			.setView(ll)
			.setTitle("Edit Information ")
			.setPositiveButton("Save",new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface di,int p){
					Editable text=et.getText();
					//v.setText(tv.getText()+"\n"+text.toString());
					Object[] o=tasks.get(index);
					o[0]=text.toString();
					tasks.remove(index);
					tasks.add(index,o);
					times.remove(index);
					times.add(index,(MainActivity2.toTime(tv.getText())[0]*60)+MainActivity2.toTime(tv.getText())[1]);
					MainActivity2.load(APPLICATION_CONTEXT,(LinearLayout)v.getParent());
				}
			})
			.create();
		ad.show();
	}
	
	
	private static void Notify(int  icon,String title,String message,boolean vibrate,long[] patten){
		Notify((new Notification.Builder(APPLICATION_CONTEXT))
			   .setSmallIcon(icon)
			   //.setSubText("subtext")
			   .setContentTitle(title)
			   //.setVisibility(Notification.VISIBILITY_PUBLIC)
			   .setContentText(message)
			   .setAutoCancel(false)
			   //.setOngoing(false)
			   .setCategory(Notification.CATEGORY_SERVICE)
			   .setPriority(Notification.PRIORITY_HIGH)
			   .setVibrate(patten).build());
	}

	private static void Notify(Notification...notices){
		int i=0;
			for(Notification notice:notices)
				notificationManager.notify(++i+0xff,notice);
	}
	
	
	
	//****************************************************************
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
	//****************************************************************
	public void setButtons(){
		final Button button;
		Button button1;
		button1 =new Button(APPLICATION_CONTEXT);
		button1.setTextColor(Color.WHITE);
		button1.setText("Save");
		button1.setOnClickListener(new Button.OnClickListener(){
				public void onClick(View p1) {
					save();
					Recover();
				}
		});
		mainLinearLayout.addView(button1);
		button1 =new Button(APPLICATION_CONTEXT);
		button1.setTextColor(Color.WHITE);
		toogleNotify(button1);
		toogleNotify(button1);
		Button finalButton = button1;
		button1.setOnClickListener(new Button.OnClickListener(){
				public void onClick(View p1){
					toogleNotify(finalButton);
				}
			});
		mainLinearLayout.addView(button1);
		button1 =new Button(APPLICATION_CONTEXT);
		button1.setTextColor(Color.WHITE);
		button1.setText("Export Routine");
		button1.setOnClickListener(new Button.OnClickListener(){
				public void onClick(View p1) {
					int i=2;
					File file=new File(Environment.getExternalStorageDirectory().getPath()+"/Routine/BackUp/Routine_1");
					while(file.exists())file=new File(Environment.getExternalStorageDirectory().getPath()+"/Routine/BackUp/Routine_"+(i++));
					try {
						file.mkdirs();
						exportRoutine(file);
						toast("The Routine is saved as : "+file.toString());
					}
					catch (Exception e){toast("Cannot Export");loge(e);}
					//toast("Not Ready");
				}
			});
		mainLinearLayout.addView(button1);
		button1 =new Button(APPLICATION_CONTEXT);
		button1.setTextColor(Color.WHITE);
		button1.setText("Import Routine");
		button1.setOnClickListener(new Button.OnClickListener(){
				public void onClick(View p1) {
					startActivity(new Intent(APPLICATION_CONTEXT,BackupActivity.class));
				}
			});
		mainLinearLayout.addView(button1);
		button1 =new Button(APPLICATION_CONTEXT);
		button1.setTextColor(Color.WHITE);
		button1.setText("Change Password");
		button1.setOnClickListener(new Button.OnClickListener(){
				public void onClick(View p1) {
					changePassword();
				}
			});
		mainLinearLayout.addView(button1);
		button1 =new Button(APPLICATION_CONTEXT);
		button = button1;
		button.setTextColor(Color.WHITE);
		button.setText("Exit");
		button.setOnClickListener(new Button.OnClickListener(){
				public void onClick(View p1) {
					finish();
				}
			});
		mainLinearLayout.addView(button);
	}
	
	private void toogleNotify(TextView v){
		if(sharedPreferences.getBoolean("Notify",false)){
			v.setText("Start Notifying");
			sharedPreferences.edit().putBoolean("Notify",false).apply();
		}
		else{
			v.setText("Stop Notifying");
			sharedPreferences.edit().putBoolean("Notify",true).apply();
		}
	}
	
	private void changePassword() {
		LinearLayout linear=new LinearLayout(this);
		linear.setOrientation(LinearLayout.VERTICAL);
		linear.setLayoutParams(new ViewGroup.LayoutParams(-1,-2));
		final EditText edit=new EditText(this);
		edit.setLayoutParams(new ViewGroup.LayoutParams(-1,-2));
		edit.setHint("Enter the previous password");
		linear.addView(edit);
		final EditText edit1=new EditText(this);
		edit1.setLayoutParams(new ViewGroup.LayoutParams(-1,-2));
		edit1.setHint("Enter the new password");
		linear.addView(edit1);
		final EditText edit2=new EditText(this);
		edit2.setLayoutParams(new ViewGroup.LayoutParams(-1,-2));
		edit2.setHint("Confirm the new password");
		linear.addView(edit2);
		
		String txt="";
		AlertDialog ad2=(new AlertDialog.Builder(APPLICATION_CONTEXT))
			.setView(linear)
			.setTitle("Change Password")
			//.setMessage("The Routine is for "+txt)
			.setPositiveButton("Enter", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface di, int p) {
					if(!check(edit.getText().toString())){
						toast("Wrong password");
					}
					else{
						String newPassword=edit1.getText().toString();
						String confirmPassword=edit2.getText().toString();
						if((newPassword.contains(confirmPassword)||confirmPassword.contains(newPassword))&&newPassword.length()==confirmPassword.length()){
							if(newPassword.length()>7)
								WritePassword(newPassword);
							else toast("Password length must be at least of 8 characters");
						}
						else{toast("new and confirm passwords don't match");}
					}
				}
			})
			.setNegativeButton("Exit", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface di, int p) {System.exit(0);}
			})
			.setCancelable(false)
			.create();
		ad2.show();
	}
	
	private boolean WritePassword(String p){
		File file=new File(Environment.getExternalStorageDirectory()+"/.TpIzOmR/.AsDdEpTf.t");
		try{
			if(!file.exists()){
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			FileOutputStream OutputStream=new FileOutputStream(file);
			OutputStream.write(p.getBytes());
			OutputStream.close();
			toast("Password changed");
			return true;
		}
		catch (Exception e){toast("Unable to change pasword");}
		return false;
	}
	
	private boolean check(String str){
		String password="Ayan Dhara";
		File file=new File(Environment.getExternalStorageDirectory()+"/.TpIzOmR/.AsDdEpTf.t");
		if(!file.exists()){
			try{
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			catch (Exception e) {}
		}
		String text;
		try{
			FileReader fileReader = new FileReader(file);
            char[] cArr = new char[1];
			StringBuffer stringBuffer= new StringBuffer();
            while (fileReader.read(cArr) > 0) {
                for (char append : cArr) {
					stringBuffer.append(append);
                }
            }
			text=stringBuffer.toString();
			fileReader.close();
			if(text!=null)if(text.length()>7)password=text;
		}catch(Exception e){toast(e);}
		if((str.contains(password)||password.contains(str))&&str.length()==password.length())return true;
		return false;
	}
	
	private String exportRoutine(File file)throws Exception{
		String str="";
		str+=saveDay(file,"sunday",MainActivity2.sundayTimeArray,MainActivity2.sundayTaskArray);
		str+=saveDay(file,"monday",MainActivity2.mondayTimeArray,MainActivity2.mondayTaskArray);
		str+=saveDay(file,"tuesday",MainActivity2.tuesdayTimeArray,MainActivity2.tuesdayTaskArray);
		str+=saveDay(file,"wednesday",MainActivity2.wednesdayTimeArray,MainActivity2.wednesdayTaskArray);
		str+=saveDay(file,"thursday",MainActivity2.thursdayTimeArray,MainActivity2.thursdayTaskArray);
		str+=saveDay(file,"friday",MainActivity2.fridayTimeArray,MainActivity2.fridayTaskArray);
		str+=saveDay(file,"saturday",MainActivity2.saturdayTimeArray,MainActivity2.saturdayTaskArray);
		//str+="<details>"+Base64.encodeToString(str.getBytes(),Base64.NO_WRAP)+"</details>";
		file=new File(file+"/allDays.chrt");
		file.createNewFile();
		FileOutputStream OutputStream=new FileOutputStream(file);
		OutputStream.write((String.valueOf((char)str.length())+str+"<details>"+ Base64.encodeToString(str.getBytes(),Base64.NO_WRAP)+"</details>").getBytes());
		OutputStream.close();
		return str;
	}
	
	private String saveDay(File file,String name,Object variable,Object variable2)throws Exception{
		file=new File(file+"/"+name+".chrt");
		file.createNewFile();
		FileOutputStream OutputStream=new FileOutputStream(file);
		String str="";
		str+="<times>";
		str+=new Gson().toJson(variable);
		str+="</times>";
		str+="<tasks>";
		str+=new Gson().toJson(variable2);
		str+="</tasks>";
		OutputStream.write((String.valueOf((char)str.length())+str+"<details>"+Base64.encodeToString(str.getBytes(),Base64.NO_WRAP)+"</details>").getBytes());
		OutputStream.close();
		return "<"+name.toLowerCase()+">"+str+"</"+name.toLowerCase()+">";
	}
	
	private void save(){
		save("sundayTimeArray",MainActivity2.sundayTimeArray);
		save("mondayTimeArray",MainActivity2.mondayTimeArray);
		save("tuesdayTimeArray",MainActivity2.tuesdayTimeArray);
		save("wednesdayTimeArray",MainActivity2.wednesdayTimeArray);
		save("thursdayTimeArray",MainActivity2.thursdayTimeArray);
		save("fridayTimeArray",MainActivity2.fridayTimeArray);
		save("saturdayTimeArray",MainActivity2.saturdayTimeArray);

		save("sundayTaskArray",MainActivity2.sundayTaskArray);
		save("mondayTaskArray",MainActivity2.mondayTaskArray);
		save("tuesdayTaskArray",MainActivity2.tuesdayTaskArray);
		save("wednesdayTaskArray",MainActivity2.wednesdayTaskArray);
		save("thursdayTaskArray",MainActivity2.thursdayTaskArray);
		save("fridayTaskArray",MainActivity2.fridayTaskArray);
		save("saturdayTaskArray",MainActivity2.saturdayTaskArray);
		
		File file=new File(Environment.getExternalStorageDirectory().getPath()+"/Routine/BackUp/Routine (auto saved)");
		try {
			file.mkdirs();
			exportRoutine(file);
			toast("The Routine is saved as : "+file.toString());
		}
		catch (Exception e){toast("Cannot Export");loge(e);}
	}
	
	public void save(String key,Object variable){
		MainActivity2.sharedPreferences.edit().putString(key,new Gson().toJson(variable)).apply();
	}
	
	public static void toast(Object...o){
		for(Object ob:o){
			Toast.makeText(APPLICATION_CONTEXT,String.valueOf(ob),Toast.LENGTH_LONG).show();
		}
	}
	
	public  TextView textView()
	{
		final TextView tv=new TextView(APPLICATION_CONTEXT);
		tv.setPadding(10, 10, 10, 10);
		tv.setTextSize(20);
		tv.setBackgroundColor(Color.parseColor("#00aaff"));
		tv.setTextColor(Color.WHITE);
		tv.setGravity(Gravity.CENTER);
		tv.setOnTouchListener(new TextView.OnTouchListener(){
				public boolean onTouch(View p1, MotionEvent p2) {
					for(TextView t:textViewArray){
						t.setBackgroundColor(Color.parseColor("#00aaff"));
					}
					((TextView)p1).setBackgroundColor(Color.parseColor("#0099ff"));
					if(p2.getAction()==MotionEvent.ACTION_UP){
						for(TextView t:textViewArray){
							t.setBackgroundColor(Color.parseColor("#00aaff"));
						}
						//tv.setBackgroundColor(Color.parseColor("#00aaff"));
					}
					return false;
				}
		});
		TextView tv1=new TextView(APPLICATION_CONTEXT);
		tv1.setLayoutParams(new ViewGroup.LayoutParams(-1, 2));
		tv1.setBackgroundColor(Color.WHITE);
		mainLinearLayout.addView(tv1);
		return tv;
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
	/*
	public void onBackPressed() {
		try {
			if (Serviceopened)NotificationService.reOpen();
		}
		catch (Exception e) {toast(e);}
		//super.onBackPressed();
	}
	@Override
	protected void onPause() {
		if(Serviceopened)
		try {
			NotificationService.reOpen();
		}
		catch (Exception e) {}
		super.onPause();
	}

	@Override
	protected void onRestart() {
		if(Serviceopened)
		try {
			NotificationService.reOpen();
		}
		catch (Exception e) {}
		super.onRestart();
	}

	@Override
	protected void onStop() {
		try {
			NotificationService.reOpen();
		}
		catch (Exception e) {}
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		try {
			NotificationService.reOpen();
		}
		catch (Exception e) {}
		super.onDestroy();
	}
	
	public static void onImport(){
		
	}//*/
}

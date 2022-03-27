package ad.app.routine;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import java.io.*;

import ad.app.routine.MainActivity2;
import android.webkit.MimeTypeMap;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import android.text.TextWatcher;
import android.text.Editable;

public class LauncherActivity extends MainActivity{
	AlertDialog ad2;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		getPassword("Password !!");
	}
	private void getPassword(String title) {
		final EditText edit=new EditText(this);
		edit.setLayoutParams(new ViewGroup.LayoutParams(-1,-2));
		edit.setHint("Enter the password");
		String txt="";
		edit.addTextChangedListener(new TextWatcher(){
				public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {}
				public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {}
				public void afterTextChanged(Editable p1) {
					check(edit,false);
				}
		});
		ad2=(new AlertDialog.Builder(APPLICATION_CONTEXT))
			.setView(edit)
			.setTitle(title)
			//.setMessage("The Routine is for "+txt)
			.setPositiveButton("Enter", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface di, int p) {
					check(edit,true);
				}
			})
			.setNegativeButton("Exit", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface di, int p) {System.exit(0);}
			})
			.setCancelable(false)
			.create();
		ad2.show();
	}
	
	private void check(EditText edit,boolean b){
		if(check(edit.getText().toString())){
			ad2.cancel();
			Intent intentA=new Intent(this,MainService.class);
			PendingIntent pendingIntent=PendingIntent.getService(this, 0, intentA, PendingIntent.FLAG_CANCEL_CURRENT);
			try{
				pendingIntent.send();
			}catch (Exception e){}
			if(MainActivity2.sharedPreferences.getAll().size()==0)
				checkForPreviousSaved();
		}
		else if(b) getPassword("Wrong Password !!");
	}
	
	private void checkForPreviousSaved(){
		File file=new File(Environment.getExternalStorageDirectory().getPath()+"/Routine/BackUp/Routine (auto saved)/allDays.chrt");
		if(file.exists()){
			if(file.isFile()){
				Restore.restoreFromFile(file);
			}
			else toast("not file");
		}
		else toast("not exists");
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
	
	public static class Restore extends Activity{
		private static boolean tempImportBool=false;
		private static ArrayList<Object[]> sundayTaskArray=new ArrayList(),mondayTaskArray=new ArrayList(),tuesdayTaskArray=new ArrayList(),wednesdayTaskArray=new ArrayList(),thursdayTaskArray=new ArrayList(),fridayTaskArray=new ArrayList(),saturdayTaskArray=new ArrayList();
		private static ArrayList<Integer> sundayTimeArray=new ArrayList(),mondayTimeArray=new ArrayList(),tuesdayTimeArray=new ArrayList(),wednesdayTimeArray=new ArrayList(),thursdayTimeArray=new ArrayList(),fridayTimeArray=new ArrayList(),saturdayTimeArray=new ArrayList();
		private static String[] days=new String[]{"sunday","monday","tuesday","wednesday","thursday","friday","saturday"};
		//private Toast toast;

		private static LinearLayout mainLL;
		private static LinearLayout sundayLinearLayout,mondayLinearLayout,tuesdayLinearLayout,wednesdayLinearLayout,thursdayLinearLayout,fridayLinearLayout,saturdayLinearLayout;
		private static ArrayList<TextView> textViewArray=new ArrayList<TextView>();
		private static ArrayList<LinearLayout> linearLayoutArray=new ArrayList<LinearLayout>();

		private static void OnCreate(final ScrollView mainSV,int day){
			sundayLinearLayout=new LinearLayout(APPLICATION_CONTEXT);
			mondayLinearLayout=new LinearLayout(APPLICATION_CONTEXT);
			tuesdayLinearLayout=new LinearLayout(APPLICATION_CONTEXT);
			wednesdayLinearLayout=new LinearLayout(APPLICATION_CONTEXT);
			thursdayLinearLayout=new LinearLayout(APPLICATION_CONTEXT);
			fridayLinearLayout=new LinearLayout(APPLICATION_CONTEXT);
			saturdayLinearLayout=new LinearLayout(APPLICATION_CONTEXT);

			mainLL = new LinearLayout(APPLICATION_CONTEXT);
			mainSV.setLayoutParams(new ViewGroup.LayoutParams(-1,-1));
			mainSV.addView(mainLL);
			mainLL.setOrientation(LinearLayout.VERTICAL);
			mainLL.setPadding(0,0,0,0);
			mainSV.setBackgroundColor(Color.WHITE);
			mainLL.setBackgroundColor(Color.WHITE);
				LoadDay(sundayLinearLayout);
				LoadDay(mondayLinearLayout);
				LoadDay(tuesdayLinearLayout);
				LoadDay(wednesdayLinearLayout);
				LoadDay(thursdayLinearLayout);
				LoadDay(fridayLinearLayout);
				LoadDay(saturdayLinearLayout);
			
		}

		private static void LoadDay(final LinearLayout linearLayout){
			linearLayoutArray.add(linearLayout);
			String str="";
			if(linearLayout==sundayLinearLayout)str="Sunday";
			else if(linearLayout==mondayLinearLayout)str="Monday";
			else if(linearLayout==tuesdayLinearLayout)str="Tuesday";
			else if(linearLayout==wednesdayLinearLayout)str="Wednesday";
			else if(linearLayout==thursdayLinearLayout)str="Thrusday";
			else if(linearLayout==fridayLinearLayout)str="Friday";
			else if(linearLayout==saturdayLinearLayout)str="Saturday";
				TextView tv = textView();
				textViewArray.add(tv);
				tv.setText(str);
				mainLL.addView(tv);
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
								((TextView)p1).setBackgroundColor(Color.parseColor("#0090ff"));
								linearLayout.setAlpha(1);
								linearLayout.setVisibility(LinearLayout.VISIBLE);
								//APPLICATION_CONTEXT.startService(new Intent(APPLICATION_CONTEXT,LoadingService.class));
								load(APPLICATION_CONTEXT, linearLayout);
								//*/
							}
						}
					});
			try{
				mainLL.addView(linearLayout);
			}catch(Exception e){toast(e);}
			linearLayout.setOrientation(LinearLayout.VERTICAL);
			linearLayout.setAlpha(1);
			linearLayout.setVisibility(LinearLayout.VISIBLE);
			load(APPLICATION_CONTEXT, linearLayout);
		}

		private static void load(final Context c,final LinearLayout linear){
			TextView tv;
			String str="__";
			final ArrayList<Object[]> taskArray;
			ArrayList<Object[]> taskArray1;
			taskArray1 =new ArrayList<Object[]>();
			final ArrayList<Integer> timeArray;
			ArrayList<Integer> timeArray1;
			timeArray1 =new ArrayList<Integer>();
			APPLICATION_CONTEXT=c;
			linear.removeAllViews();
			if(linear==sundayLinearLayout){
				str="Sunday";
				taskArray1 =sundayTaskArray;
				timeArray1 =sundayTimeArray;
			}
			else if(linear==mondayLinearLayout){
				str="Monday";
				taskArray1 =mondayTaskArray;
				timeArray1 =mondayTimeArray;
			}
			else if(linear==tuesdayLinearLayout){
				str="Tuesday";
				taskArray1 =tuesdayTaskArray;
				timeArray1 =tuesdayTimeArray;
			}
			else if(linear==wednesdayLinearLayout){
				str="Wednesday";
				taskArray1 =wednesdayTaskArray;
				timeArray1 =wednesdayTimeArray;
			}
			else if(linear==thursdayLinearLayout){
				str="Thrusday";
				taskArray1 =thursdayTaskArray;
				timeArray1 =thursdayTimeArray;
			}
			else if(linear==fridayLinearLayout){
				str="Friday";
				taskArray1 =fridayTaskArray;
				timeArray1 =fridayTimeArray;
			}
			else if(linear==saturdayLinearLayout){
				str="Saturday";
				taskArray1 =saturdayTaskArray;
				timeArray1 =saturdayTimeArray;
			}

			timeArray = timeArray1;
			taskArray = taskArray1;
			MainActivity2.Arrange(timeArray,taskArray);

			for(int i=0;i<timeArray.size();i++){
				Object[] o=taskArray.get(i);
				tv=TextView(linear);
				tv.setText(MainActivity2.fromTime(timeArray.get(i))+"\n"+o[0]);
				linear.addView(tv);
			}
		}

		private static TextView TextView(LinearLayout linear){
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

		private static  TextView textView()
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
			mainLL.addView(tv1);
			return tv;
		}

		public static void restoreFromFile(File file){
			String text="";
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
			}catch(Exception e){toast(e);}
			//toast(text);
			if(checkSignature(text)){
						int i=0;
						try {
							for (String str:days) {
								Restore(text.substring(text.indexOf("<" + str + ">") + str.length() + 2, text.indexOf("</" + str + ">")), i++);
							}
						}
						catch (Exception e) {}
					if (tempImportBool){
						toast("Previously saved routine found","Previously saved routine found");
						viewRoutine(7);
					}
			}
		}

		private static boolean checkSignature(String text){
			try{
				String str=text.substring(1,text.indexOf("<details>"));
				str=Base64.encodeToString(str.getBytes(),Base64.NO_WRAP);
				String str2=text.substring(text.indexOf("<details>")+("<details>").length(),text.indexOf("</details>"));
				if(str.length()==str2.length()&&(str.contains(str2)||str2.contains(str))){
					return true;
				}
				return false;
			}
			catch(Exception e){return false;}
			//return true;
		}

		private static void viewRoutine(final int day) {
			final ScrollView scroll=new ScrollView(APPLICATION_CONTEXT);
			OnCreate(scroll,day);
			AlertDialog ad2=(new AlertDialog.Builder(APPLICATION_CONTEXT))
				.setView(scroll)
				.setTitle("Last saved routine")
				//.setMessage("The Routine is for "+txt)
				.setPositiveButton("Import", new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface di, int p) {
						Restore(day);
						toast("Routine is imported to current");
					}
				})
				.setNegativeButton("cancel", new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface di, int p) {}
				})/*
				.setNeutralButton("Exit", new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface di, int p) {}
				})*/
				.setCancelable(false)
				.create();
			ad2.show();
		}

		private static ArrayList<Object[]> Put(String data)throws Exception {
			int index0=data.indexOf("<tasks>") + 7,index1=data.indexOf("</tasks>");
			data = data.substring(index0, index1);
			//toast(data);
			ArrayList<Object[]> var=(new Gson().fromJson(data, new TypeToken<ArrayList<Object[]>>(){}.getType()));
			return var == null ?new ArrayList<Object[]>(): var;
			/*/
			 return null;
			 //*/
		}

		private static ArrayList<Integer> PutT(String data)throws Exception {
			int index0=data.indexOf("<times>") + 7,index1=data.indexOf("</times>");
			data = data.substring(index0, index1);
			//toast(data);
			ArrayList<Integer> var=new Gson().fromJson(data, new TypeToken<ArrayList<Integer>>(){}.getType());
			return var == null ?new ArrayList<Integer>(): var;
			/*/
			return null;
			//*/
		}

		private static void Restore(int day){
			if(day==7){
				for(int i=0;i<7;i++)
					Restore(i);
			}
			if (day == 0)MainActivity2.sundayTimeArray = sundayTimeArray;
			if (day == 1)MainActivity2.mondayTimeArray = mondayTimeArray;
			if (day == 2)MainActivity2.tuesdayTimeArray = tuesdayTimeArray;
			if (day == 3)MainActivity2.wednesdayTimeArray = wednesdayTimeArray;
			if (day == 4)MainActivity2.thursdayTimeArray = thursdayTimeArray;
			if (day == 5)MainActivity2.fridayTimeArray = fridayTimeArray;
			if (day == 6)MainActivity2.saturdayTimeArray = saturdayTimeArray;
			if (day == 0)MainActivity2.sundayTaskArray = sundayTaskArray;
			if (day == 1)MainActivity2.mondayTaskArray =	mondayTaskArray;
			if (day == 2)MainActivity2.tuesdayTaskArray = tuesdayTaskArray;
			if (day == 3)MainActivity2.wednesdayTaskArray = wednesdayTaskArray;
			if (day == 4)MainActivity2.thursdayTaskArray = thursdayTaskArray;
			if (day == 5)MainActivity2.fridayTaskArray = fridayTaskArray;
			if (day == 6)MainActivity2.saturdayTaskArray = saturdayTaskArray;
		}

		private static void Restore(String data, int day) {
			tempImportBool = false;
			try {
				if (day == 0)sundayTimeArray = PutT(data);
				if (day == 1)mondayTimeArray = PutT(data);
				if (day == 2)tuesdayTimeArray = PutT(data);
				if (day == 3)wednesdayTimeArray = PutT(data);
				if (day == 4)thursdayTimeArray = PutT(data);
				if (day == 5)fridayTimeArray = PutT(data);
				if (day == 6)saturdayTimeArray = PutT(data);
				if (day == 0)sundayTaskArray = Put(data);
				if (day == 1)mondayTaskArray =	Put(data);
				if (day == 2)tuesdayTaskArray = Put(data);
				if (day == 3)wednesdayTaskArray = Put(data);
				if (day == 4)thursdayTaskArray = Put(data);
				if (day == 5)fridayTaskArray = Put(data);
				if (day == 6)saturdayTaskArray = Put(data);
				//toast("Routine is imported");
				tempImportBool = true;
			}
			catch (Exception e) {}
		}
	}
}

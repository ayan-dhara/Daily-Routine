package ad.app.routine;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.io.*;

import android.graphics.Color;
import android.util.Log;
import android.webkit.MimeTypeMap;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import android.util.Base64;
import android.graphics.Typeface;

public class BackupActivity extends Activity{
	private boolean tempImportBool=false;
	private LinearLayout linearLayoutMain;
	//private static LinearLayout fileLinearLayout;
	private ScrollView scrollView;
	private boolean fileViewInserted;
	//private static String fileAddress;
	private static Context APPLICATION_CONTEXT;
	private ArrayList<Object[]> sundayTaskArray=new ArrayList(),mondayTaskArray=new ArrayList(),tuesdayTaskArray=new ArrayList(),wednesdayTaskArray=new ArrayList(),thursdayTaskArray=new ArrayList(),fridayTaskArray=new ArrayList(),saturdayTaskArray=new ArrayList();
	private ArrayList<Integer> sundayTimeArray=new ArrayList(),mondayTimeArray=new ArrayList(),tuesdayTimeArray=new ArrayList(),wednesdayTimeArray=new ArrayList(),thursdayTimeArray=new ArrayList(),fridayTimeArray=new ArrayList(),saturdayTimeArray=new ArrayList();
	private LinearLayout linearLayout;
	private RelativeLayout mainLayout;
	private String address;
	private int tempInt=0;
	private ArrayList<ScrollView> scrollViews=new ArrayList<ScrollView>();
	private String[] days=new String[]{"sunday","monday","tuesday","wednesday","thursday","friday","saturday"};
	private static Toast toast;
	
	private LinearLayout mainLL;
	public LinearLayout sundayLinearLayout,mondayLinearLayout,tuesdayLinearLayout,wednesdayLinearLayout,thursdayLinearLayout,fridayLinearLayout,saturdayLinearLayout;
	private ArrayList<TextView> textViewArray=new ArrayList<TextView>();
	private ArrayList<LinearLayout> linearLayoutArray=new ArrayList<LinearLayout>();
	
    private void OnCreate(final ScrollView mainSV,int day){
		sundayLinearLayout=new LinearLayout(this);
		mondayLinearLayout=new LinearLayout(this);
		tuesdayLinearLayout=new LinearLayout(this);
		wednesdayLinearLayout=new LinearLayout(this);
		thursdayLinearLayout=new LinearLayout(this);
		fridayLinearLayout=new LinearLayout(this);
		saturdayLinearLayout=new LinearLayout(this);

		mainLL = new LinearLayout(this);
		mainSV.setLayoutParams(new ViewGroup.LayoutParams(-1,-1));
		mainSV.addView(mainLL);
		mainLL.setOrientation(LinearLayout.VERTICAL);
		mainLL.setPadding(0,0,0,0);
		mainSV.setBackgroundColor(Color.WHITE);
		mainLL.setBackgroundColor(Color.WHITE);

		if(day==7){
			LoadDay(sundayLinearLayout,false);
			LoadDay(mondayLinearLayout,false);
			LoadDay(tuesdayLinearLayout,false);
			LoadDay(wednesdayLinearLayout,false);
			LoadDay(thursdayLinearLayout,false);
			LoadDay(fridayLinearLayout,false);
			LoadDay(saturdayLinearLayout,false);
		}
		else{
			if(day==0)LoadDay(sundayLinearLayout,true);
			if(day==1)LoadDay(mondayLinearLayout,true);
			if(day==2)LoadDay(tuesdayLinearLayout,true);
			if(day==3)LoadDay(wednesdayLinearLayout,true);
			if(day==4)LoadDay(thursdayLinearLayout,true);
			if(day==5)LoadDay(fridayLinearLayout,true);
			if(day==6)LoadDay(saturdayLinearLayout,true);
		}
    }
	
	private void LoadDay(final LinearLayout linearLayout,boolean d){
		linearLayoutArray.add(linearLayout);
		String str="";
		if(linearLayout==sundayLinearLayout)str="Sunday";
		else if(linearLayout==mondayLinearLayout)str="Monday";
		else if(linearLayout==tuesdayLinearLayout)str="Tuesday";
		else if(linearLayout==wednesdayLinearLayout)str="Wednesday";
		else if(linearLayout==thursdayLinearLayout)str="Thrusday";
		else if(linearLayout==fridayLinearLayout)str="Friday";
		else if(linearLayout==saturdayLinearLayout)str="Saturday";
		if(!d){
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
		}
		try{
			mainLL.addView(linearLayout);
		}catch(Exception e){new Toast(this,e);}
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setAlpha(1);
		linearLayout.setVisibility(LinearLayout.VISIBLE);
		load(APPLICATION_CONTEXT, linearLayout);
	}
	
	public void load(final Context c,final LinearLayout linear){
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

//		final int i;
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
		mainLL.addView(tv1);
		return tv;
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		APPLICATION_CONTEXT = this;
		Intent intent=getIntent();
		if(new File(Environment.getExternalStorageDirectory().getPath()+"/routine/backup").exists())
			address=Environment.getExternalStorageDirectory().getPath()+"/routine/backup";
		else address=Environment.getExternalStorageDirectory().getPath();
		mainLayout=new RelativeLayout(this);
		setContentView(mainLayout);
		mainLayout.setBackgroundColor(Color.parseColor("#0090ff"));
		if(intent.getData()!=null){
			try{
				File file=new File(intent.getData().getPath());
				address=file.getParentFile().getPath();
				restoreFromFile(file);
			}catch(Exception e){}
		}
		File file=new File(address);
		ArrayList<File> files=new ArrayList<File>();
		try{
		while(file.getParentFile().exists()){
			file=file.getParentFile();
			files.add(0,file);
		}}catch(Exception e){}
		for(File f:files)LoadFiles(f);
		LoadFiles(address);
	}
	
	private void setNewList(){
		scrollView=new ScrollView(this);
		scrollViews.add(scrollView);
		scrollView.setLayoutParams(new ViewGroup.LayoutParams(-1,-1));
		//scrollView.setBackgroundResource(R.drawable.a1);/*/
		linearLayout=new LinearLayout(this);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		mainLayout.addView(scrollView);
		scrollView.addView(linearLayout);
		tempInt=++tempInt%3;
	}

	private  void textView(String text,TextView.OnClickListener onClick,TextView.OnLongClickListener onLongClick) {
		final TextView tv=new TextView(this);
		tv.setTypeface(Typeface.SERIF);
		tv.setTextSize(20);
		tv.setText(text);
		if(onClick==null&&onLongClick==null){
			tv.setTextSize(23);
			tv.setTypeface(Typeface.create("cursive",Typeface.BOLD));
		}
		tv.setPadding(10, 10, 10, 10);
		tv.setBackgroundColor(Color.parseColor("#8000a0ff"));
		tv.setTextColor(Color.WHITE);
		if(tempInt==0)tv.setGravity(Gravity.LEFT);
		if(tempInt==1)tv.setGravity(Gravity.CENTER);
		if(tempInt==2)tv.setGravity(Gravity.RIGHT);
		tv.setOnClickListener(onClick);
		tv.setOnLongClickListener(onLongClick);
		linearLayout.addView(tv);
		TextView tv1=new TextView(this);
		tv1.setOnClickListener(onClick);
		tv1.setOnLongClickListener(onLongClick);
		tv1.setLayoutParams(new ViewGroup.LayoutParams(-1, 3));
		tv1.setBackgroundColor(Color.parseColor("#80ffffff"));
		linearLayout.addView(tv1);
	}

	private void goBack(){
		if(scrollViews.size()>1){
			mainLayout.removeView((ScrollView)pop(scrollViews));
		}
		else fileViewInserted=false;
	}

	private void LoadFiles(File path){
		LoadFiles(path.getPath());
	}
	
	private void LoadFiles(String path) {
		File file=new File(path);
		boolean fileAvaliable=true;
		boolean folderAvaliable=true;
		if (file.isDirectory()){if(file.canRead()){
				setNewList();
				linearLayout.removeAllViews();
				//linearLayout.setPadding(0,0,0,getResources().getDisplayMetrics().heightPixels/2);
				for (final File file_:arrangeByName(file.listFiles(new FileFilter(){
							public boolean accept(File p1) {
								if(p1.isDirectory())return true;
								return false;
							}
						}))){
							if(folderAvaliable)
						textView("-: Folders :-",null,null);
					textView(file_.getName(),new TextView.OnClickListener(){
							public void onClick(View v) {
								LoadFiles(file_);
							}
						},new TextView.OnLongClickListener(){
							public boolean onLongClick(View v){
								
								return true;
							}
						});
						folderAvaliable=false;
				}
				for (final File file_:arrangeByName(file.listFiles(new FileFilter(){
					public boolean accept(File p1) {
						if(p1.isFile()){
							String ext=MimeTypeMap.getFileExtensionFromUrl(p1.getPath());
							if(ext.contains("chrt")&&ext.length()==4)return true;
						}
						return false;
					}
				}))){
					if(fileAvaliable)
						textView("-: Files :-",null,null);
					textView(file_.getName(),new TextView.OnClickListener(){
							public void onClick(View v) {
								LoadFiles(file_);
							}
						},new TextView.OnLongClickListener(){
							public boolean onLongClick(View v){
								restoreFromFile(file_);
								return true;
							}
						});
						fileAvaliable=false;
				}
				int height=getResources().getDisplayMetrics().heightPixels;
				TextView t=new TextView(this);
				t.setLayoutParams(new ViewGroup.LayoutParams(-1,height));
				t.setBackgroundColor(Color.parseColor("#8000a0ff"));
				linearLayout.addView(t);
				fileViewInserted=true;
			}else{new Toast(this,"The folder is not readable");}}
		if(file.isFile())new Toast(this,"Long click to import");
	}

	public void onBackPressed() {
		goBack();
		tempInt=(tempInt+2)%3;
		if(!fileViewInserted)
			super.onBackPressed();
	}
	
	private void restoreFromFile(File file){
		String text=file.getName();
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
		}catch(Exception e){new Toast(this,e);}
		//toast(text);
		if(checkSignature(text)){
			int day=0;
			boolean found=false;
			String name=file.getName().toLowerCase();
			for (String str:days) {
				if(name.contains(str+".chrt")&&name.length()==(str+".chrt").length()){
					found=true;
					break;
				}
				day++;
			}
			if(!found&&name.contains("alldays.chrt")&&name.length()==("alldays.chrt").length())
					found=true;
			if(found){
			if (day == 7) {
				int i=0;
				try {
					for (String str:days) {
						Restore(text.substring(text.indexOf("<" + str + ">") + str.length() + 2, text.indexOf("</" + str + ">")), i++);
					}
				}
				catch (Exception e) {new Toast(this,"The file is not well-formed.");}
			}
			else Restore(text, day);
			if (tempImportBool){
				new Toast(this,"Routine loaded");
				viewRoutine(day);
			}
			}
			else{
				new Toast(this,"File name cannot be detected !!");
			}
		}
		else new Toast(this,"File Content Error !!");
	}
	
	private boolean checkSignature(String text){
		try{
			String str=text.substring(1,text.indexOf("<details>"));
			str=Base64.encodeToString(str.getBytes(),Base64.NO_WRAP);
			String str2=text.substring(text.indexOf("<details>")+("<details>").length(),text.indexOf("</details>"));
			if(str.length()==str2.length()&&(str.contains(str2)||str2.contains(str))){
				return true;
			}
			else{
				new Toast(this,"Error");
				return false;
			}
		}
		catch(Exception e){return false;}
		//return true;
	}

	private void viewRoutine(final int day) {
		final ScrollView scroll=new ScrollView(this);
		String txt="";
		if(day==7)txt="All Days";
		else txt=days[day];
		OnCreate(scroll,day);
		AlertDialog ad2=(new AlertDialog.Builder(APPLICATION_CONTEXT))
			.setView(scroll)
			.setTitle("Check it first ("+txt+")")
			//.setMessage("The Routine is for "+txt)
			.setPositiveButton("Import", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface di, int p) {
					Restore(day);
				}
			})
			.setNegativeButton("cancel", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface di, int p) {}
			})
			.setNeutralButton("Exit", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface di, int p) {finish();}
			})
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
	
	private void Restore(int day){
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
		new Toast(this,"Routine is imported");
		finish();
	}

	private void Restore(String data, int day) {
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
		catch (Exception e) {new Toast(this,"The text is not well-formed.\nCheck it first.");}
	}
	
	private Object pop(ArrayList arrayList){
		Object object=arrayList.get(arrayList.size()-1);
		arrayList.remove(object);
		return object;
	}
	private File[] arrangeByName(File[] files){
		for(int i=0;i<files.length;i++){
			for(int j=i+1;j<files.length;j++){
				if(needToogle(files[i],files[j])){
					File file=files[i];
					files[i]=files[j];
					files[j]=file;
				}
			}
		}
		return files;
	}
	private boolean needToogle(File file1,File file2){
		String name1=file1.getName().toLowerCase(),name2=file2.getName().toLowerCase();
		int minimumLength=Math.min(name1.length(),name2.length());
		int i=0;
		char c1=name1.charAt(i),c2=name2.charAt(i);
		if(c1=='.'&&c2!='.')return false;
		if(c2=='.'&&c1!='.')return true;
		for(i=0;i<minimumLength;i++){
			c1=name1.charAt(i);
			c2=name2.charAt(i);
			if(c2>c1)return false;
			if(c2<c1)return true;
		}
		if(i==minimumLength){
			if(name1.length()>name2.length())return true;
		}
		return false;
	}
	private static void logd(Object o) {
		Log.d(APPLICATION_CONTEXT.getClass().getSimpleName(), String.valueOf(o));
	}
	private static void logi(Object o) {
		Log.i(APPLICATION_CONTEXT.getClass().getSimpleName(), String.valueOf(o));
	}
	private static void loge(Object o) {
		Log.e(APPLICATION_CONTEXT.getClass().getSimpleName(), String.valueOf(o));
	}
	private static void logw(Object o) {
		Log.w(APPLICATION_CONTEXT.getClass().getSimpleName(), String.valueOf(o));
	}
}

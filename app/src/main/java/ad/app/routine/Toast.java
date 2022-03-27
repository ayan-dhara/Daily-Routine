package ad.app.routine;

public class Toast extends android.widget.Toast{
	private static android.content.Context Context;
	private static android.widget.Toast toast;
	public static android.widget.Toast Toast(Object o){
		try{
			toast=makeText(Context,String.valueOf(o),LENGTH_LONG);
			toast.show();
		}catch(Exception e){}
		return toast;
	}
	public static android.widget.Toast Toast(boolean removePrevious,Object o){
		if(removePrevious)toast.cancel();
		android.widget.Toast toast1=Toast(o);
		return toast1;
	}
	public static android.widget.Toast Toast(Object o,boolean allowRemove){
		android.widget.Toast toast1=Toast(o);
		if(!allowRemove)toast=null;
		return toast1;
	}
	public static android.widget.Toast Toast(boolean removePrevious,Object o,boolean allowRemove){
		if(removePrevious)toast.cancel();
		android.widget.Toast toast1=Toast(o);
		if(!allowRemove)toast=null;
		return toast1;
	}
	//*/
	public Toast(android.content.Context context){
		super(context);
		Context=context;
	}
	
	public Toast(android.content.Context context,Object o){
		super(context);
		Context=context;
		Toast(o);
	}
	public Toast(android.content.Context context,boolean removePrevious,Object o){
		super(context);
		new Toast(context);
		Context=context;
		Toast(removePrevious,o);
	}
	public Toast(android.content.Context context,Object o,boolean allowRemove){
		super(context);
		Context=context;
		Toast(o,allowRemove);
	}
	public Toast(android.content.Context context,boolean removePrevious,Object o,boolean allowRemove){
		super(context);
		Context=context;
		Toast(removePrevious,o,allowRemove);
	}//*/
}

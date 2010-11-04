package edu.sjsu.cinequest;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;


import edu.sjsu.cinequest.R;
import edu.sjsu.cinequest.android.AndroidPlatform;
import edu.sjsu.cinequest.comm.Callback;
import edu.sjsu.cinequest.comm.Platform;
import edu.sjsu.cinequest.comm.QueryManager;
import edu.sjsu.cinequest.comm.cinequestitem.Filmlet;
import edu.sjsu.cinequest.comm.cinequestitem.Schedule;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class FilmsActivity extends Activity {
	private QueryManager queryManager;
	private ListView filmsList;
	private Vector<Filmlet> films = new Vector<Filmlet>();
	private Vector<Schedule> schedules = new Vector<Schedule>();
	private String[] title ;
	private static final int FILMBYDATE = 0;
	private static final int FILMBYTITLE = 1;
	private int filterStatus;
	private Button byDate_bt;
	private Button byTitle_bt;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.film_layout);
        filmsList=(ListView)findViewById(R.id.ListView01);
        byDate_bt = (Button)findViewById(R.id.bydate_bt);
        byTitle_bt = (Button)findViewById(R.id.bytitle_bt);
        
        TextView textView = new TextView(this); 
        textView.setText("header"); 
        filmsList.addHeaderView(textView);
        
        //Action Listener
        filmsList.setOnItemClickListener(new ListView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				//Intent intent = new Intent();
				switch(filterStatus){
					case FILMBYDATE:
						break;
					case FILMBYTITLE:
						break;
				}
			/*	
				intent.setClass(mainScreen.this, MyWebView.class);
				Bundle bundle=new Bundle();
				bundle.putString("url", url);
				intent.putExtras(bundle);
				startActivity(intent);
				*/
			}
        });
        
        byDate_bt.setOnClickListener(new OnClickListener(){
    		public void onClick(View v){
    			System.out.println("date button got clicked");
    			filterStatus = FILMBYDATE;
    			FilmsActivity.this.updatefilter(filterStatus);
    		}
    	});
    	byTitle_bt.setOnClickListener(new OnClickListener(){
    		public void onClick(View v){
    			System.out.println("title button got clicked");
    			filterStatus = FILMBYTITLE;
    			FilmsActivity.this.updatefilter(filterStatus);
    		}
    	});

    	
        filterStatus = FILMBYDATE;
        this.updatefilter(filterStatus);
      
              
         
    }
    private void updatefilter(int filterStatus)
    {
        Platform.setInstance(new AndroidPlatform());        
        queryManager = new QueryManager();
        if(filterStatus == FILMBYDATE)
        {
        	System.out.println("updatefilter-date");
        	queryManager.getScheduls(new Callback(){

				@Override
				public void invoke(Object result) {
					schedules = (Vector<Schedule>) result;
					FilmsActivity.this.scheduleList(schedules);	
				}
				@Override
				public void progress(Object value) {	
				}
				@Override
				public void failure(Throwable t) {
				}
        	
        	
        	});
        	
        } else
        {
        	System.out.println("updatefilter-title");
       	 queryManager.getAllFilms (new Callback() {
    			public void progress(Object value) {
    			}
    			public void invoke(Object result) {
    				 films = (Vector<Filmlet>) result;
    				 FilmsActivity.this.filmList(films);
    			}   			
    			public void failure(Throwable t) {
    			}
    		});
        }
        
    }

    private void filmList(Vector<Filmlet> film)
    {
    	Log.i("block","printTest");
    	title = new String[film.size()];
    	for(int i=0;i<film.size();i++)
    		title[i]=film.get(i).getTitle();
    	 Log.v("array~~in Test",film.get(0).getTitle());	    	 	
    	 filmsList.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1 , title));   	 
    }
    private void scheduleList(Vector<Schedule> schedule)
    {  	
    	Log.i("block","scheduleList");
    	String previousDay = schedule.get(0).getDateString();
    	Vector<Schedule> tempVect = new Vector<Schedule>();
    	tempVect.addElement(schedule.get(0));
    	// create our list and custom adapter  
        SeparatedListAdapter adapter = new SeparatedListAdapter(this);
    	for(int i=1;i<schedule.size();i++)
    	{
    		if(!schedule.get(i).getDateString().equalsIgnoreCase(previousDay))
    		{
    			adapter.addSection(previousDay, new SimpleAdapter(this,getData(tempVect),R.layout.filmbydateitem, new String[]{"title","time","venue"}, 
    	    			new int[]{R.id.ScheduleTitle ,R.id.ScheduleTime ,R.id.ScheduleVenue}));
    			tempVect.removeAllElements();
    			previousDay = schedule.get(i).getDateString();
    		}else
    		{
    			tempVect.addElement(schedule.get(i));
    		}
    	}

    	filmsList.setAdapter(adapter);
		
		
	}
    private List<Map<String, Object>> getData(Vector<Schedule> schedule) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		
		for(int i =0;i<schedule.size();i++){
			map = new HashMap<String, Object>();
			map.put("title", schedule.get(i).getTitle());
			map.put("time","Time: "+ schedule.get(i).getStartTime()+" - "+schedule.get(i).getEndTime());
			map.put("venue","  venue: "+  schedule.get(i).getVenue());
			list.add(i, map);
			}
		return list;
	}
}
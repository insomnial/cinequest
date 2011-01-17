package edu.sjsu.cinequest;

import java.util.List;
import java.util.Vector;

import edu.sjsu.cinequest.comm.cinequestitem.Filmlet;

public class DVDActivity extends CinequestTabActivity {

	@Override
	protected void init() {
		// TODO: super.init();
		setBottomBarEnabled(false);
		// TODO: Why?
		enableListContextMenu();
	}

	@Override
	protected void fetchServerData() {
		HomeActivity.getQueryManager().getDVDs(
				new ProgressMonitorCallback(this) {    		
			public void invoke(Object result) {
				super.invoke(result);
				refreshListContents((Vector<Filmlet>) result);
			}});
	}

	// TODO: Button click doesn't seem to work
	
	@Override
	protected void refreshListContents(List<?> listItems) {
   		setListViewAdapter(createFilmletList(listItems));
	}
}

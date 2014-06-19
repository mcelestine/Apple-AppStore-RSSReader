package com.thehackerati.rssreader;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;



public class FeedListActivity extends Activity implements 
	FeedListFragment.Callbacks {
	
	private boolean isTwoPane;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_list);
		
		determinePaneLayout();
	}

	private void determinePaneLayout() {
		if (findViewById(R.id.fragmentDetailContainer) != null) {
			isTwoPane = true;
			
			FeedListFragment fragmentFeedList = (FeedListFragment) getFragmentManager().findFragmentById(R.id.fragmentListContainer);
			fragmentFeedList.setActivatedOnItemClick(true);
		}
	}

	@Override
	public void onItemSelected(FeedItem item) {
		if (isTwoPane) {
			FeedDetailFragment fragmentDetail = FeedDetailFragment.newInstance(item);
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.replace(R.id.fragmentDetailContainer, fragmentDetail);
			ft.commit();
		} else {
			Intent intent = new Intent(this, FeedDetailActivity.class);
			intent.putExtra("item", item);
			startActivity(intent);
		}
	}
	
}

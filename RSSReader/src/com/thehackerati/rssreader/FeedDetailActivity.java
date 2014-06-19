package com.thehackerati.rssreader;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;


public class FeedDetailActivity extends Activity {
	
	FeedDetailFragment fragmentItemDetail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_detail);
		
		FeedItem item = (FeedItem) getIntent().getSerializableExtra("item");
		
		if (savedInstanceState == null) {
			fragmentItemDetail = FeedDetailFragment.newInstance(item);
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.replace(R.id.detail_container, fragmentItemDetail);
			ft.commit();
		}
		
	}
}

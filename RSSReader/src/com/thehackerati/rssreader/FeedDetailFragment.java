package com.thehackerati.rssreader;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.thehackerati.rssreader.FeedItemDBHelper.FeedDBConstants;

public class FeedDetailFragment extends Fragment {
	
	private FeedItem mFeedItem;
	private FeedItemDBHelper mDbHelper = new FeedItemDBHelper(getActivity());

	public static FeedDetailFragment newInstance(FeedItem item) {
		FeedDetailFragment fragmentDemo = new FeedDetailFragment();
		Bundle args = new Bundle();
		args.putSerializable("item", item);
		fragmentDemo.setArguments(args);
		return fragmentDemo;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mFeedItem = (FeedItem) getArguments().getSerializable("item");
		mDbHelper = new FeedItemDBHelper(getActivity());
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_feed_detail, container,
				false);
		
		getActivity().setTitle(getString(R.string.app_detail));
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (NavUtils.getParentActivityName(getActivity()) != null) {
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}

		if (mFeedItem != null) {
			TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
			TextView tvAppDeveloper = (TextView) view.findViewById(R.id.tvAppDeveloper);
			TextView tvAppPrice = (TextView) view.findViewById(R.id.tvPrice);
			TextView tvSummary = (TextView) view.findViewById(R.id.tvSummary);

			tvTitle.setText(mFeedItem.getAppTitle());
			tvAppDeveloper.setText(mFeedItem.getAppDeveloper());
			tvAppPrice.setText(mFeedItem.getAppPrice());
			tvSummary.setText(mFeedItem.getAppSummary());
		}

		return view;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.feed_detail_menu, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(getActivity());
			return true;
		case R.id.menu_share:
			shareFeedDetail();
			return true;
		case R.id.menu_favorite:
			saveItemAsFavorite();
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void shareFeedDetail() {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_TEXT, mFeedItem.toString() + " " + mFeedItem.getAppLink());
		
		startActivity(Intent.createChooser(shareIntent, getActivity().getString(R.string.share_chooser_title)));
	}
	
	private void saveItemAsFavorite() {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(FeedDBConstants.COLUMN_ITEM_TITLE, mFeedItem.getAppTitle());
		values.put(FeedDBConstants.COLUMN_ITEM_LINK, mFeedItem.getAppLink());
		
		db.insert(FeedDBConstants.TABLE_NAME, null, values);
		
		Toast.makeText(getActivity(), "Saved as favorite!", Toast.LENGTH_LONG).show();	
	}


}

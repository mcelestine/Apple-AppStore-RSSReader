package com.thehackerati.rssreader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class FeedListFragment extends ListFragment {
	
	public static final String TAG = FeedListActivity.class.getSimpleName();
	
	protected JSONObject mFeedData;
	protected ProgressBar mProgressBar;
	public static final String KEY_LABEL = "label";
	
	private CustomListAdapter mAdapter;
	private ArrayList<FeedItem> mFeedItems = null;
	
	private Callbacks mCallbacks;
	
	public interface Callbacks {
		public void onItemSelected(FeedItem item);
	}
	
	public FeedListFragment() {
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mCallbacks = (Callbacks)activity;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_feed_list, container, false);
		
		mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
		
		if (isNetworkAvailable()) {
			mProgressBar.setVisibility(View.VISIBLE);
			GetFeedTask getFeedTask = new GetFeedTask();
			getFeedTask.execute();
		} else {
			Toast.makeText(getActivity(), "Network is unavailable!", Toast.LENGTH_LONG).show();
		}
		
		return view;
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		
		FeedItem item = (FeedItem) mAdapter.getItem(position);
		
		mCallbacks.onItemSelected(item);
	}
	
	public void setActivatedOnItemClick(boolean activateOnItemClick) {
		getListView().setChoiceMode(activateOnItemClick ? ListView.CHOICE_MODE_SINGLE : ListView.CHOICE_MODE_NONE);
	}
	
	private boolean isNetworkAvailable() {
		ConnectivityManager manager = (ConnectivityManager) 
				getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		
		boolean isAvailable = false;
		// if the network is present and connected to the web
		if (networkInfo != null && networkInfo.isConnected()) {
			isAvailable = true;
		}
		
		return isAvailable;
	}

	private void updateDisplayForError() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(getString(R.string.error_title));
		builder.setMessage(getString(R.string.error_message));
		builder.setPositiveButton(android.R.string.ok, null);
		
		AlertDialog dialog = builder.create();
		dialog.show();
		
		TextView emptyTextView = (TextView) getListView().getEmptyView();
		emptyTextView.setText(getString(R.string.no_items));
	}

	private void parseJsonFeedData() { 
		mProgressBar.setVisibility(View.INVISIBLE);
	
		if (mFeedData == null) {
			updateDisplayForError();
		} else {
			try {
				JSONObject jsonFeed = mFeedData.getJSONObject("feed");
				JSONArray jsonEntryArray = jsonFeed.getJSONArray("entry");
	
				mFeedItems = new ArrayList<FeedItem>();
	
				for (int i = 0; i < jsonEntryArray.length(); i++) {
					JSONObject jsonEntryNode = jsonEntryArray.getJSONObject(i);
					JSONObject jsonAppTitle = jsonEntryNode.getJSONObject("im:name");
					JSONObject jsonAppDeveloper = jsonEntryNode.getJSONObject("im:artist");
					JSONObject jsonSummary = jsonEntryNode.getJSONObject("summary");
					JSONObject jsonPrice = jsonEntryNode.getJSONObject("im:price");
					JSONObject jsonLink = jsonEntryNode.getJSONObject("link");
					JSONObject jsonLinkAttributes = jsonLink.getJSONObject("attributes");
	
					FeedItem item = new FeedItem();
					
					item.setAppTitle(jsonAppTitle.getString(KEY_LABEL));
					item.setAppDeveloper(jsonAppDeveloper.getString(KEY_LABEL));
					item.setAppPrice(jsonPrice.getString(KEY_LABEL));
					item.setAppSummary(jsonSummary.getString(KEY_LABEL));
					item.setAppLink(jsonLinkAttributes.getString("href"));
	
					mFeedItems.add(item);
				}
				
				mAdapter = new CustomListAdapter(getActivity(), mFeedItems);
				setListAdapter(mAdapter);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private class GetFeedTask extends AsyncTask<Object, Void, JSONObject> {		
		
		@Override
		protected JSONObject doInBackground(Object... params) {
		
			mFeedData = getJSONResponseFromUrl();
			return mFeedData;
		}
		
		public JSONObject getJSONResponseFromUrl() {
			int responseCode = -1;
			JSONObject jsonResponse = null;
			StringBuilder builder = new StringBuilder();
			HttpClient client = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topgrossingapplications/sf=143441/limit=25/json");
			
			try {
				HttpResponse response = client.execute(httpGet);
				StatusLine statusLine = response.getStatusLine();
				responseCode = statusLine.getStatusCode();
				
				if (responseCode == HttpURLConnection.HTTP_OK) {
					HttpEntity entity = response.getEntity();
					InputStream content = entity.getContent();
					BufferedReader reader = new BufferedReader(new InputStreamReader(content));
					
					String line = null;
					while ((line = reader.readLine()) != null) {
						builder.append(line);
					}
					
					content.close();
					jsonResponse = new JSONObject(builder.toString());
				} else {
					Log.i(TAG, "Unsuccessful HTTP Response Code: " + responseCode);
				}
			} catch (JSONException e) {
				Log.e(TAG, "Exception caught: ", e);
			} catch (IOException e) {
				Log.e(TAG, "Exception caught: ", e);
			} catch (Exception e) {
				Log.e(TAG, "Exception caught: ", e);
			}
			
			return jsonResponse;		
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			mFeedData = result;
			parseJsonFeedData();
		}
	}
}

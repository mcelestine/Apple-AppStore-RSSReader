package com.thehackerati.rssreader;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FeedItemDBHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "feedItems.db";
	

	private static final String SQL_CREATE_TABLE = "CREATE TABLE " + FeedDBConstants.TABLE_NAME
			+ " (" + FeedDBConstants.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ FeedDBConstants.COLUMN_ITEM_TITLE + " TEXT NOT NULL, " + FeedDBConstants.COLUMN_ITEM_LINK
			+ " TEXT NOT NULL)";

	public FeedItemDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + FeedDBConstants.TABLE_NAME);
		onCreate(db);

	}
	
	public static class FeedDBConstants {
		public static final String TABLE_NAME = "favorites";

		public static final String COLUMN_ID = "_id";
		public static final String COLUMN_ITEM_TITLE = "title";
		public static final String COLUMN_ITEM_LINK = "link";
	}

}

package cn.tedu.mediaplayer.dao;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SqliteHelper extends SQLiteOpenHelper{

	public SqliteHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	public SqliteHelper(Context context, String name, CursorFactory factory, int version,
			DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL( "CREATE TABLE IF NOT EXISTS MUSIC ("
				+ "_id integer primary key, "
				+ "song_id varchar,"
				+ "title varchar,"
				+ "author varchar,"
				+ "filepath varchar,"
				+ "album_500_500 varchar,"
				+ "lrclink varchar,"
				+ "duration integer,"
				+ "pic_small varchar,"
				+ "album_title varchar,"
				+ "add_time bigint"
				+ ")");

		db.execSQL("CREATE TABLE IF NOT EXISTS KEYWORDS (" +
				"_id integer primary key, " +
				"keyword varchar, " +
				"time bigint" +
				")");

		Log.e("Database" ,"onCreate" );
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(newVersion==1){
			db.execSQL("DROP TABLE IF EXISTS MUSIC");
			db.execSQL("DROP TABLE IF EXISTS KEYWORDS");
			onCreate(db);
		}
	}

}

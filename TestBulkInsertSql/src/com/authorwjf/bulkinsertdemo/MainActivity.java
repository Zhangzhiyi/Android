package com.authorwjf.bulkinsertdemo;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	private static final String SAMPLE_DB_NAME = "MathNerdDB";
	private static final String SAMPLE_TABLE_NAME = "MulitplicationTable";
	private SQLiteDatabase sampleDB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initDB();
		findViewById(R.id.standard_insert_button).setOnClickListener(this);
		findViewById(R.id.trasition_insert_button).setOnClickListener(this);
		findViewById(R.id.sqlstatement_insert_button).setOnClickListener(this);
		findViewById(R.id.sqlstatement_update).setOnClickListener(this);
	}

	private void initDB() {
		sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);
		sampleDB.execSQL("CREATE TABLE IF NOT EXISTS " + SAMPLE_TABLE_NAME
				+ " (FirstNumber INT, SecondNumber INT," + " Result INT);");
		sampleDB.delete(SAMPLE_TABLE_NAME, null, null);
	}

	private void insertOneHundredRecords() {
		for (int i = 0; i < 20000; i++) {
			ContentValues values = new ContentValues();
			values.put("FirstNumber", i);
			values.put("SecondNumber", i);
			values.put("Result", i * i);
			sampleDB.insert(SAMPLE_TABLE_NAME, null, values);
		}
	}

	private void transactionInsertOneHundredRecords() {
		sampleDB.beginTransaction();
		for (int i = 0; i < 20000; i++) {
			ContentValues values = new ContentValues();
			values.put("FirstNumber", i);
			values.put("SecondNumber", i);
			values.put("Result", i * i);
			sampleDB.insert(SAMPLE_TABLE_NAME, null, values);
		}
		sampleDB.setTransactionSuccessful();
		sampleDB.endTransaction();
	}

	private void bulkInsertOneHundredRecords() {
		String sql = "INSERT INTO " + SAMPLE_TABLE_NAME + " VALUES (?,?,?);";
		SQLiteStatement statement = sampleDB.compileStatement(sql);
		sampleDB.beginTransaction();
		for (int i = 0; i < 20000; i++) {
			statement.clearBindings();
			statement.bindLong(1, 1);
			statement.bindLong(2, i);
			statement.bindLong(3, i * i);
			statement.executeInsert();

		}
		sampleDB.setTransactionSuccessful();
		sampleDB.endTransaction();
		statement.close();
	}

	private void bulkUpdateOneHundredRecords() {
		String sql = "UPDATE " + SAMPLE_TABLE_NAME + " SET SecondNumber = ? WHERE FirstNumber = 1";
		SQLiteStatement statement = sampleDB.compileStatement(sql);
		sampleDB.beginTransaction();
		// statement.clearBindings();
		statement.bindLong(1, 100);
		statement.executeUpdateDelete();
		sampleDB.setTransactionSuccessful();
		sampleDB.endTransaction();
		statement.close();
	}

	@Override
	public void onClick(View v) {
		long startTime = System.currentTimeMillis();
		if (v.getId() == R.id.standard_insert_button) {
			sampleDB.delete(SAMPLE_TABLE_NAME, null, null);
			insertOneHundredRecords();
		} else if (v.getId() == R.id.trasition_insert_button) {
			sampleDB.delete(SAMPLE_TABLE_NAME, null, null);
			transactionInsertOneHundredRecords();
		} else if (v.getId() == R.id.sqlstatement_insert_button) {
			sampleDB.delete(SAMPLE_TABLE_NAME, null, null);
			bulkInsertOneHundredRecords();
		} else if (v.getId() == R.id.sqlstatement_update) {
			bulkUpdateOneHundredRecords();
		}
		long diff = System.currentTimeMillis() - startTime;
		((TextView) findViewById(R.id.exec_time_label)).setText("Exec Time: " + Long.toString(diff)
				+ "ms");
	}

	@Override
	protected void onDestroy() {
		sampleDB.close();
		super.onDestroy();
	}

}

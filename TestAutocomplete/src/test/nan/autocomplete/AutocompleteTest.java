package test.nan.autocomplete;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.CursorAdapter;
import android.widget.Filterable;
import android.widget.TextView;

public class AutocompleteTest extends Activity {
	private SQLiteDatabase sqlite = null;
	private static final String database = "yourDb";
	private String[] columns;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        AutoCompleteTextView actv = (AutoCompleteTextView)findViewById(R.id.actv);
        /**设置输入多少个字弹出提示**/
        actv.setThreshold(1);
        
        //Make sure to create a new database everytime
        this.deleteDatabase(database);
        sqlite = this.openOrCreateDatabase(database, 0, null);
        this.createDatas();
        
        columns = new String[]{"name", "_id"};
        
        MyCursorAdpter myCursorAdapter = new MyCursorAdpter(this, getCursor(), 0);
        actv.setAdapter(myCursorAdapter);
    }
    
    private void createDatas() {
    	String createTable = "create table test (name varchar(255), _id varchar(255))";
    	sqlite.execSQL(createTable);
    	
    	ContentValues values = new ContentValues();
		values.put("name", "nicole");
		values.put("_id", "0");
		sqlite.insert("test", null, values);
		
		values.put("name", "nicolas");
		values.put("_id", "1");
		sqlite.insert("test", null, values);
		
		values.put("name", "jean");
		values.put("_id", "2");
		sqlite.insert("test", null, values);
		
		values.put("name", "jennyfer");
		values.put("_id", "3");
		sqlite.insert("test", null, values);
    }
    
    public Cursor getCursor() {
    	return sqlite.query("test", columns, null, null, null, null, null);
    }
    
    private class MyCursorAdpter extends CursorAdapter {
    	private int columnIndex;
    	
		public MyCursorAdpter(Context context, Cursor c, int col) {
			super(context, c);
			this.columnIndex = col;
		}

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            final LayoutInflater inflater = LayoutInflater.from(context);
            final TextView view = (TextView) inflater.inflate(
                    android.R.layout.simple_dropdown_item_1line, parent, false);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {        	
            ((TextView) view).setText(cursor.getString(columnIndex));
        }
        
        @Override
        public String convertToString(Cursor cursor) {
        	Log.i("convertToString", cursor.getString(columnIndex));
        	
            return cursor.getString(columnIndex);
        }
        /**输出看到EditTex有变动都会调用runQueryOnBackgroundThread方法**/
        /**看源代码看到runQueryOnBackgroundThread里面是运行FilterQueryProvider接口方法，
         * 而这里CursorAdapter没有关联FilterQueryProvider接口对象，所以自己重写查询语句返回Cursor**/
        @Override
        public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
            if (constraint != null) {
            	
            	Log.i("runQueryOnBackgroundThread", constraint.toString());
            	String selection = "name like \'" + constraint.toString() +"%\'";
                return sqlite.query("test", columns, selection, null, null, null, null);
            }
            else {
            	
            	return null;
            }
        }
    }
}
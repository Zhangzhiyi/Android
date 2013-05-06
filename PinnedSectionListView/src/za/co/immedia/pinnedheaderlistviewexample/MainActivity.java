package za.co.immedia.pinnedheaderlistviewexample;

import java.util.TreeMap;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PinnedHeaderListView listView = (PinnedHeaderListView) findViewById(R.id.pinnedListView);
        TestSectionedAdapter sectionedAdapter = new TestSectionedAdapter();
//        listView.setSelector(R.drawable.preference_list_bg_selector);
        listView.setPinHeaders(true);
        listView.setAdapter(sectionedAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}

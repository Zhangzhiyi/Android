package chroya.demo.rotate3d;

import android.app.Activity;
import android.os.Bundle;

public class Main extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CubeView cv = new CubeView(this);
        cv.rotate(30, 30);
        setContentView(cv);
    }
}
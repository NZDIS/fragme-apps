package org.nzdis.pingtest;

//import android.R;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	// GUI
	private EditText edittext;
	private PingTest pingTest;
	
    Handler edittextHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String text = (String)msg.obj;
            edittext.append(text + "\n");
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    	this.edittext = (EditText)findViewById(R.id.editText);
    	
    	pingTest = new PingTest(edittextHandler);

    	final Button button = (Button) findViewById(R.id.exitbutton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	pingTest.isRunning = false;
            }
        });
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void asdf() {
		for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
			System.out.println(ste.toString());
		}
    }
    
    
    @Override
    protected void onDestroy() {
    	if (pingTest != null)
    		pingTest.isRunning = false;
    }
};


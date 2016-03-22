package cn.com.farsight.SerialDemo;
import cn.com.farsight.SerialService.SerialService;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	private final static String TAG = "MainActivity";
	/** Called when the activity is first created. */
	EditText mEditText;
	SerialService serialService = new SerialService();
	String data;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mEditText = (EditText)this.findViewById(R.id.et);
		Button button = (Button)this.findViewById(R.id.button1);

		button.setOnClickListener(new OnClickListener()//
		{

			public void onClick(View v) 
			{
				Log.d(TAG, "Button clickd!!");
				data = serialService.read();
				System.out.println(data);
				updateView(data);
				//serialService.write(data);
			};
		});
	}

	public void updateView(String data){
		Log.d(TAG, "updateView()");
		mEditText.setText(data);	
	}
}

package six.lwuikit.app.act;

import six.lwuikit.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_main);
		
	}
	
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		int id = v.getId();
		switch(id){
		case R.id.menu1:
			intent = new Intent(this,LyricActivity.class);
			this.startActivity(intent);
			break;
		case R.id.menu2:
			intent = new Intent(this,SafActivity.class);
			this.startActivity(intent);
			break;
		case R.id.menu3:
			intent = new Intent(this,SMActivity.class);
			this.startActivity(intent);
			break;
		case R.id.menu4:
			intent = new Intent(this,AlphabetActivity.class);
			this.startActivity(intent);
			break;
		case R.id.menu5:
			intent = new Intent(this,TipActivity.class);
			this.startActivity(intent);
			break;
		case R.id.menu6:
			intent = new Intent(this,EcoActivity.class);
			this.startActivity(intent);
			break;
		case R.id.menu7:
			intent = new Intent(this,CyclePickerActivity.class);
			this.startActivity(intent);
			break;
		case R.id.menu8:
			intent = new Intent(this,BarActivity.class);
			this.startActivity(intent);
			break;
		case R.id.menu9:
			intent = new Intent(this,WLActivity.class);
			this.startActivity(intent);
			break;
		case R.id.menu10:
			intent = new Intent(this,ListViewActivity.class);
			this.startActivity(intent);
			break;
		}
	}
}

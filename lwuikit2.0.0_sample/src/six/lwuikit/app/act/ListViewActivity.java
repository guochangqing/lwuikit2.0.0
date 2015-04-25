package six.lwuikit.app.act;

import six.lwuikit.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ListViewActivity extends Activity{
	ListView listview;
	MyAdapter adapter;
	boolean status = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_list);
		listview = (ListView)findViewById(R.id.listview);
		adapter = new MyAdapter();
		listview.setAdapter(adapter);
		findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				status = true;
				Log.e("guocq1", "listview.getChildCount():"+listview.getChildCount());
				// TODO Auto-generated method stub
				for(int i=0;i<listview.getChildCount();i++){
					View view = listview.getChildAt(i).findViewById(R.id.layer2);
					view.setTranslationX(0);
					view.animate().translationX(100).setDuration(500);
				}
			}
		});
		findViewById(R.id.button2).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				status = false;
				// TODO Auto-generated method stub
				for(int i=0;i<listview.getChildCount();i++){
					View view = listview.getChildAt(i).findViewById(R.id.layer2);
					view.setTranslationX(100);
					view.animate().translationX(0).setDuration(500);
				}
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 100;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}
		int count = 0;
		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			if(null == arg1){
				Log.e("guocq1", "create num:"+(++count));
				LayoutInflater inflater = LayoutInflater.from(ListViewActivity.this); 
				arg1 = inflater.inflate(R.layout.item_listview, arg2, false);
			}
			
			if(status){
//				arg1.layout(100, 0, arg1.getRight(),arg1.getBottom());
				arg1.findViewById(R.id.layer2).setTranslationX(100);
			}else{
				arg1.findViewById(R.id.layer2).setTranslationX(0);
//				arg1.layout(0, 0, arg1.getRight(),arg1.getBottom());
			}
			TextView name = (TextView)arg1.findViewById(R.id.name);
			name.setText("my name is " + arg0);
			return arg1;
		}
		
	}
}

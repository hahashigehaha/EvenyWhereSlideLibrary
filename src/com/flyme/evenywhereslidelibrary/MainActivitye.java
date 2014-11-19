package com.flyme.evenywhereslidelibrary;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivitye extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TextView delete = (TextView) findViewById(R.id.one_one);
		delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivitye.this, "delete", 0).show();
			}
		});
		TextView collection = (TextView) findViewById(R.id.one_two);
		collection.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivitye.this, "collection", 0).show();
			}
		});
		
		TextView top = (TextView) findViewById(R.id.top_left);
		top.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivitye.this, "topleft", 0).show();
			}
		});
		TextView top2 = (TextView) findViewById(R.id.top_right);
		top2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivitye.this, "topright", 0).show();
			}
		});
		TextView delect = (TextView) findViewById(R.id.delect);
		delect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivitye.this, "delect2", 0).show();
			}
		});
		TextView top_top = (TextView) findViewById(R.id.top_top);
		top_top.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivitye.this, "top_top", 0).show();
			}
		});
		TextView top_bottom = (TextView) findViewById(R.id.top_bottom);
		top_bottom.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivitye.this, "top_bottom", 0).show();
			}
		});
		
		
	}
}

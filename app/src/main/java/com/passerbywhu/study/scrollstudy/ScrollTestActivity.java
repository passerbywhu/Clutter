package com.passerbywhu.study.scrollstudy;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.passerbywhu.study.R;

public class ScrollTestActivity extends Activity implements OnClickListener{
	private Button up, down, left, right, zoomIn, zoomOut;
	private TextView textView;
	private ImageView imgView;
	private FrameLayout imgContainer;
	private int speed = 5;
	private EditText scrollTo;
	private Button go;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scrolltest_layout);
		
		up = (Button) findViewById(R.id.up);
		down = (Button) findViewById(R.id.down);
		left = (Button) findViewById(R.id.left);
		right = (Button) findViewById(R.id.right);
		zoomIn = (Button) findViewById(R.id.zoomIn);
		zoomOut = (Button) findViewById(R.id.zoomOut);
		up.setOnClickListener(this);
		down.setOnClickListener(this);
		left.setOnClickListener(this);
		right.setOnClickListener(this);
		zoomIn.setOnClickListener(this);
		zoomOut.setOnClickListener(this);
		
		textView = (TextView) findViewById(R.id.textView); 
		
		imgView = (ImageView) findViewById(R.id.imgView);
		
		imgContainer = (FrameLayout) findViewById(R.id.imgArea);
		
		scrollTo = (EditText) findViewById(R.id.scrollTo);
		go = (Button) findViewById(R.id.go);
		go.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.up:
			imgView.scrollBy(0, speed);
			textView.scrollBy(0, speed);
			break;
		case R.id.down:
			imgView.scrollBy(0, -speed);
			textView.scrollBy(0, -speed);
			break;
		case R.id.left:
			imgView.scrollBy(speed, 0);
			textView.scrollBy(speed, 0);
			break;
		case R.id.right:
			imgView.scrollBy(-speed, 0);
			textView.scrollBy(-speed, 0);
			break;
		case R.id.zoomIn:
			LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) imgContainer.getLayoutParams();
			int height = imgContainer.getHeight();
			height += speed;
			params1.height = height;
			imgContainer.setLayoutParams(params1);
			break;
		case R.id.zoomOut:
			LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) imgContainer.getLayoutParams();
			int height2 = imgContainer.getHeight();
			height2 -= speed;
			params2.height = height2;
			imgContainer.setLayoutParams(params2);
			break;
		case R.id.go:
			String content = scrollTo.getText().toString();
			String[] coords = content.trim().split(" ");
			int coordx = Integer.valueOf(coords[0]);
			int coordy = Integer.valueOf(coords[1]);
			imgContainer.scrollTo(coordx, coordy);
			repaint();
			break;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("scrollX = " + imgView.getScrollX() + "\n");
		sb.append("scrollY = " + imgView.getScrollY() + "\n");
		sb.append("width = " + imgView.getWidth() + "\n");
		sb.append("height = " + imgView.getHeight() + "\n");
		sb.append("containerWidth = " + imgContainer.getWidth() + "\n");
		sb.append("containerHeight = " + imgContainer.getHeight() + "\n");
		textView.setText(sb.toString());
		repaint();
	}
	
	private void repaint() {
		findViewById(android.R.id.content).invalidate();
	}
}

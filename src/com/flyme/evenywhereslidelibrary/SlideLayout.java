package com.flyme.evenywhereslidelibrary;


import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.support.v4.widget.ViewDragHelper.Callback;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.RelativeLayout;

public class SlideLayout extends RelativeLayout {

	private ViewDragHelper dragHelper;
	private View bottomView;
	private View topView;
	private Context context ;
	
	 public enum Status {
	        Middle , Open, Close
	 }


	public SlideLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	public SlideLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	public SlideLayout(Context context) {
		super(context);
		init(context);
	}
	
	
	private void init(Context context){
		this.context = context ;
		dragHelper = ViewDragHelper.create(this, 1.0f, new DragHelperCallBack());
	}
	
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (getChildCount() > 2 ) {
			throw new RuntimeException("子孩子不能大于2");
		}
		bottomView = getChildAt(0);
		topView = getChildAt(1);
		bottomState = getGravity() ;
		
		mHeight = getHeight();
		mWidth = getWidth();
		
		if (bottomState == DEFULT_BOTTOM ) {
			isOpen = true ;
		}
		else if (bottomState == DEFULT_TOP) {
			isOpen = false;
					
		}
	}
	
	
	
	private int bottomState = 0 ;
	
	private static final int DEFULT_LEFT = 3 ;
	private static final int DEFULT_RIGHT = 5 ;
	private static final int DEFULT_TOP = 48 ;
	private static final int DEFULT_BOTTOM = 80 ;
	private static final int DEFULT_VEL = 40 ;
	private int mHeight;
	private int mWidth;
	
	
    private boolean mTouchConsumedByChild = false;
    
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
    	switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startX = (int) ev.getX();
			startY = (int) ev.getY();
			if (bottomState == DEFULT_BOTTOM || bottomState == DEFULT_TOP) {
				bottomView.dispatchTouchEvent(ev);
			}
			topView.dispatchTouchEvent(ev);
			break;
		case MotionEvent.ACTION_MOVE:
//			int moveX = (int) ev.getX();
//			int moveY = (int) ev.getY();
//			if (Math.abs((moveY - startY)) < 16 ) {
//				if (bottomState ==DEFULT_TOP && getStatusState() == Status.Open) {
//					bottomView.dispatchTouchEvent(ev);
//				}
//			}
			break;
		case MotionEvent.ACTION_UP:
			int moveX2 = (int) ev.getX();
			int moveY2 = (int) ev.getY();
			if (Math.abs((moveY2 - startY)) < 8 ) {
				if (getStatusState() == Status.Open && (bottomState == DEFULT_BOTTOM || bottomState == DEFULT_TOP)) {
					bottomView.dispatchTouchEvent(ev);
				}else if (getStatusState() == Status.Close) {
					topView.dispatchTouchEvent(ev);
				}
			}else {
				onTouchEvent(ev);
				return true ;
			}
			break;
		}
    	return super.dispatchTouchEvent(ev);
    }

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_CANCEL || ev.getAction() == MotionEvent.ACTION_UP) {
			dragHelper.cancel();
			return false ;
		}
		if (getStatusState() == Status.Open && onBottomView(ev)) {
			return false ;
		}
		
	    return true;
	}
	
	
	
	private boolean onBottomView(MotionEvent event){
		int bottomx = (int) event.getX();
		if ((bottomState == DEFULT_LEFT && bottomx < topView.getX()) 
				|| (bottomState == DEFULT_RIGHT  && bottomx > (event.getX() - topView.getWidth()))) {
			return true ;
		}else {
			return false ;
		}
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		dragHelper.processTouchEvent(event);
		return true ;
	}
	
	
	
	private Status getStatusState(){
		if (bottomState == DEFULT_LEFT && topView.getLeft() > 0  || (bottomState == DEFULT_RIGHT && topView.getLeft() < 0) || 
				(bottomState == DEFULT_BOTTOM  && topView.getTop() < 0 ) || (bottomState == DEFULT_TOP && topView.getTop() > 0)) {
			return Status.Open; 
		}else {
			return Status.Close;
		}
	}
	
	
	@Override
	public void computeScroll() {
		//这个是为了将事件执行到底
		if (dragHelper.continueSettling(true)) {
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}
	
	
	class DragHelperCallBack  extends Callback{

		@Override
		public boolean tryCaptureView(View arg0, int arg1) {
			if (bottomState == DEFULT_BOTTOM || bottomState == DEFULT_TOP) {
				return arg0 == topView  || arg0 == bottomView ;
			}else{
				return arg0 == topView ;
			}
		}
		
		@Override
		public void onViewReleased(View releasedChild, float xvel, float yvel) {
			if (bottomState == DEFULT_LEFT ) {
				leftScroll(xvel);
			}else if (bottomState == DEFULT_RIGHT ) {
				rightScroll(xvel);
			}else if (bottomState == DEFULT_TOP) {
				topScroll(yvel);
			}else if (bottomState == DEFULT_BOTTOM) {
				bottomScroll(yvel);
			}
			invalidate();
		}
		
		@Override
		public void onViewPositionChanged(View changedView, int left, int top,
				int dx, int dy) {
			super.onViewPositionChanged(changedView, left, top, dx, dy);
			if (bottomState == DEFULT_TOP || bottomState == DEFULT_BOTTOM) {
				mTop += dy  ;
				requestLayout();
			}
		}
		
		@Override
		public int clampViewPositionHorizontal(View child, int left, int dx) {
			if (bottomState == DEFULT_LEFT) {
				return Math.max(0,Math.min(left, bottomView.getWidth()));
			}else if (bottomState == DEFULT_RIGHT) {
				return Math.min(0, Math.max(left, -bottomView.getWidth()));
			}
			return super.clampViewPositionVertical(child, left, dx);
		}
		
		
		@Override
		public int clampViewPositionVertical(View child, int top, int dy) {
			if (bottomState == DEFULT_TOP) {
				if (child == topView) {
					return  Math.max(0, Math.min(top, bottomView.getHeight()));
				}else{
					return  Math.min(0, Math.max(top, -bottomView.getHeight()));
				}
			}else if(bottomState == DEFULT_BOTTOM){
				if (child == topView) {
					return  Math.min(0, Math.max(top, -bottomView.getHeight()));
				}else{
					return  Math.max(0, Math.min(top, bottomView.getHeight()));
				}
			}else{
				return super.clampViewPositionVertical(child, top, dy);
			}
		}
	}
	
	
	private void leftScroll(float xvel){
		int left = topView.getLeft();
		int width = bottomView.getWidth();
		if (xvel > DEFULT_VEL) {
			dragHelper.settleCapturedViewAt(bottomView.getWidth(), 0);
		}else if(xvel < -DEFULT_VEL){
			dragHelper.settleCapturedViewAt(0, 0);
		}else if (left > width / 2 ) {
			dragHelper.settleCapturedViewAt(bottomView.getWidth(), 0);
		}else {
			dragHelper.settleCapturedViewAt(0, 0);
		}
	}
	
	private void rightScroll(float xvel){
		int left = topView.getLeft();
		int width = bottomView.getWidth();
		if (xvel > DEFULT_VEL) {
			dragHelper.settleCapturedViewAt(0, 0);
		}else if(xvel < -DEFULT_VEL){
			dragHelper.settleCapturedViewAt(-bottomView.getWidth(), 0);
		}else if (Math.abs(left) > width / 2 ) {
			dragHelper.settleCapturedViewAt(-bottomView.getWidth(), 0);
		}else {
			dragHelper.settleCapturedViewAt(0, 0);
		}
	}
	
	private boolean isOpen ;
	
	private void topScroll(float yvel){
		int top = topView.getTop();
		if (!isOpen ) {
			downSlide(top,yvel);
		}else {
			top = bottomView.getTop();
			upSlide(top,yvel);
		}
	}
	
	
	private void downSlide(int top ,float yvel){
		int height = bottomView.getHeight();
		if (yvel > DEFULT_VEL) {
			isOpen = true ;
			dragHelper.settleCapturedViewAt(0,bottomView.getHeight());
		}else if(yvel < -DEFULT_VEL){
			isOpen = false ;
			dragHelper.settleCapturedViewAt(0, 0);
		}else if (Math.abs(top) > height / 2 ) {
			isOpen = true ;
			dragHelper.settleCapturedViewAt(0,bottomView.getHeight());
		}else {
			isOpen = false ;
			dragHelper.settleCapturedViewAt(0, 0);
		}
	}
	
	
	private void upSlide(int top ,float yvel){
		int height = bottomView.getHeight();
		if (yvel > DEFULT_VEL) {
			isOpen = true ;
			dragHelper.settleCapturedViewAt(0, 0);
		}else if(yvel < -DEFULT_VEL){
			isOpen = false ;
			dragHelper.settleCapturedViewAt(0,-bottomView.getHeight());
		}else if (Math.abs(top) > height / 2 ) {
			isOpen = false ;
			dragHelper.settleCapturedViewAt(0,-bottomView.getHeight());
		}else {
			isOpen = true ;
			dragHelper.settleCapturedViewAt(0, 0);
		}
	}
	
	private void bottomScroll(float yvel){
		int top = topView.getTop();
		if (isOpen ) {
			upSlide(top,yvel);
		}else {
			top = bottomView.getTop();
			downSlide(top,yvel);
		}
		
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		  int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
	        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);
	        mWidth = maxWidth ; 
	        mHeight = maxHeight ;
	}
	
	private int mTop = 0 ;
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int height = topView.getMeasuredHeight();
		if (bottomState == DEFULT_TOP) {
			bottomView.layout(0,mTop - height,r,mTop);
			topView.layout(0,mTop  ,r,mTop + height);
		}else if (bottomState == DEFULT_BOTTOM) {
			bottomView.layout(0,height + mTop,r,2 * height + mTop );
			topView.layout(0,mTop,r,height + mTop);
		}else {
			super.onLayout(changed, l, t, r, b);
		}
	}
	
	GestureDetector mGestureDetector = new GestureDetector(context, new SwipeDetector());
	private int startX;
	private int startY;
	class SwipeDetector extends GestureDetector.SimpleOnGestureListener{
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			System.out.println("onsingletapup" + getStatusState());
			return super.onSingleTapUp(e);
		}
		@Override
		public void onLongPress(MotionEvent e) {
			System.out.println("onlongpress");
			// TODO Auto-generated method stub
			super.onLongPress(e);
		}
	}
}

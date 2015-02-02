package com.android.zna.fivecircles.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.zna.fivecircles.R;

/**
 * A layout that places its children in a square <em>Grid</em> .
 *
 * @attr ref android.R.styleable#SquareGridLayout_orientation
 * @attr ref android.R.styleable#SquareGridLayout_rowCount
 * @attr ref android.R.styleable#SquareGridLayout_columnCount
 * @attr ref android.R.styleable#SquareGridLayout_gridGap
 */
public class SquareGridLayout extends ViewGroup {

	public static final int UNDEFINED = Integer.MIN_VALUE;

	public static final int HORIZONTAL = LinearLayout.HORIZONTAL;
	public static final int VERTICAL = LinearLayout.VERTICAL;

	private static final int DEFAULT_ORIENTATION = HORIZONTAL;
	private static final int DEFAULT_GRID_GAP = 0;
	private static final int DEFAULT_ROW_COUNT = 1;
	private static final int DEFAULT_COLUMN_COUNT = 1;

	private int mOrientation = DEFAULT_ORIENTATION;
	private int mGridGap = DEFAULT_GRID_GAP;
	private int mRowCount = DEFAULT_ROW_COUNT;
	private int mColumnCount = DEFAULT_COLUMN_COUNT;


	public SquareGridLayout(Context context) {
		super(context);
		init(null, 0);
	}

	public SquareGridLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs, 0);
	}

	public SquareGridLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs, defStyle);
	}

	private void init(AttributeSet attrs, int defStyle) {
		// Load attributes
		final TypedArray a = getContext().obtainStyledAttributes(
				attrs, R.styleable.SquareGridLayout, defStyle, 0);

		mOrientation = a.getInt(
				R.styleable.SquareGridLayout_orientation, DEFAULT_ORIENTATION);
		mRowCount = a.getInt(
				R.styleable.SquareGridLayout_rowCount, DEFAULT_ROW_COUNT);
		mColumnCount = a.getInt(
				R.styleable.SquareGridLayout_columnCount, DEFAULT_COLUMN_COUNT);
		mGridGap = a.getDimensionPixelSize(
				R.styleable.SquareGridLayout_gridGap, DEFAULT_GRID_GAP);

		a.recycle();

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			LayoutParams lp = (LayoutParams) child.getLayoutParams();
			child.layout(lp.x, lp.y, lp.x + child.getMeasuredWidth(),
					lp.y + child.getMeasuredHeight());
		}

	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		Log.d("squaregrid", "p width: " + widthSize);

		Log.d("squaregrid", "p height: " + heightSize);

		int totalHPadding = getPaddingLeft() + getPaddingRight();
		int totalVPadding = getPaddingTop() + getPaddingBottom();

		Log.d("squaregrid", "H padding: " + totalHPadding);

		Log.d("squaregrid", "V padding: " + totalVPadding);

		final int count = getChildCount();

		Log.d("squaregrid", "child count: " + count);
		Log.d("squaregrid", "ROW count: " + mRowCount);
		Log.d("squaregrid", "COL count: " + mColumnCount);


		int squareSize;
		if (mOrientation == HORIZONTAL) {
			int totalGap = (mColumnCount - 1) * mGridGap;
			squareSize = (widthSize - totalHPadding - totalGap)
					/ mColumnCount;
			Log.d("squaregrid", "H totalGap: " + totalGap);
			Log.d("squaregrid", "H squareSize: " + squareSize);
		} else {
			int totalGap = (mRowCount - 1) * mGridGap;
			squareSize = (heightSize - totalVPadding - totalGap)
					/ mRowCount;
			Log.d("squaregrid", "V totalGap: " + totalGap);
			Log.d("squaregrid", "V squareSize: " + squareSize);
		}
		Log.d("squaregrid", "square size: " + squareSize);


		int currentWidth = getPaddingLeft();
		int currentHeight = getPaddingTop();

		int maxWidth = currentWidth;
		int maxHeight = currentHeight;

		int  pWidth = totalHPadding;
		int  pHeight = totalVPadding;



		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			SquareGridLayout.LayoutParams lp = (SquareGridLayout.LayoutParams) child.getLayoutParams();
			checkLayoutParams(lp);
			int childSpec = MeasureSpec.makeMeasureSpec(squareSize,
					MeasureSpec.EXACTLY);
			measureChild(child, lp.columnSpan * (childSpec + mGridGap) - mGridGap,
					lp.rowSpan * (childSpec + mGridGap) - mGridGap);

			Log.d("squaregrid", "currentWidth: " + currentWidth);
			Log.d("squaregrid", "currentHeight: " + currentHeight);

			if(lp.columnIndex != UNDEFINED){
				lp.x = getPaddingLeft()+lp.columnIndex*(squareSize+mGridGap)+lp.leftMargin;
			}else {
				lp.x = currentWidth + lp.leftMargin;
			}

			if(lp.rowIndex != UNDEFINED){
				lp.y = getPaddingTop()+lp.rowIndex*(squareSize+mGridGap)+lp.topMargin;
			}else {
				lp.y = currentHeight + lp.topMargin;
			}
			Log.d("squaregrid", "LP: " + lp.x + "," + lp.y);




			//compute next child position
			int itemWidth = lp.columnSpan * (squareSize + mGridGap)-mGridGap;
			int itemHeight = lp.rowSpan * (squareSize + mGridGap)-mGridGap;
			maxWidth = Math.max(maxWidth,lp.x+itemWidth);
			maxHeight = Math.max(maxHeight,lp.y+itemHeight);
			Log.d("squaregrid", "maxWidth " + maxWidth);
			Log.d("squaregrid", "maxHeight: " + maxHeight);

			if (mOrientation == HORIZONTAL) {

				if (lp.x + itemWidth + getPaddingRight()
						>= widthSize) {
					currentWidth = getPaddingLeft();
					currentHeight = maxHeight + mGridGap;
				} else {
					currentWidth = lp.x + itemWidth + mGridGap;
				}
			} else {
				if (lp.y + itemHeight + getPaddingBottom()
						>= heightSize) {
					currentHeight = getPaddingTop();
					currentWidth = maxWidth + mGridGap;
				} else {
					currentHeight = lp.y + itemHeight + mGridGap;
				}
			}




		}

		if (mOrientation == HORIZONTAL) {
			pWidth = widthSize;
			pHeight = maxHeight + getPaddingBottom();
		} else {
			pWidth = maxWidth+getPaddingRight();
			pHeight = heightSize;
		}

		int measuredWidth = Math.max(pWidth, getSuggestedMinimumWidth());
		int measuredHeight = Math.max(pHeight, getSuggestedMinimumHeight());
		Log.d("squaregrid", "measured :" + measuredWidth + "," + measuredHeight);
		setMeasuredDimension(
				resolveSizeAndState(measuredWidth, widthMeasureSpec, 0),
				resolveSizeAndState(measuredHeight, heightMeasureSpec, 0));

	}

	@Override
	protected void measureChild(View child, int parentWidthMeasureSpec,
	                            int parentHeightMeasureSpec) {
		final LayoutParams lp = (LayoutParams) child.getLayoutParams();

		int childWidthSize = MeasureSpec.getSize(parentWidthMeasureSpec);
		childWidthSize = Math.min(childWidthSize, lp.width);

		int childHeightSize = MeasureSpec.getSize(parentHeightMeasureSpec);
		childHeightSize = Math.min(childHeightSize, lp.height);

		final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
				lp.leftMargin + lp.rightMargin, childWidthSize);
		final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
				lp.topMargin + lp.bottomMargin, childHeightSize);

		child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
	}

	@Override
	protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
		return p instanceof LayoutParams;
		//return true;
	}

	@Override
	protected LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
	}

	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new LayoutParams(getContext(), attrs);
	}

	@Override
	protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
		return new LayoutParams(p.width, p.height);
	}

	public int getmOrientation() {
		return mOrientation;
	}

	public int getmGridGap() {
		return mGridGap;
	}

	public int getmRowCount() {
		return mRowCount;
	}

	public int getmColumnCount() {
		return mColumnCount;
	}

	public void setmOrientation(int mOrientation) {
		this.mOrientation = mOrientation;
	}

	public void setmGridGap(int mGridGap) {
		this.mGridGap = mGridGap;
	}

	public void setmRowCount(int mRowCount) {
		this.mRowCount = mRowCount;
	}

	public void setmColumnCount(int mColumnCount) {
		this.mColumnCount = mColumnCount;
	}


	/**
	 * Layout information associated with each of the children of a SquareGridLayout.
	 *
	 * @attr ref android.R.styleable#GridLayout_Layout_layout_row
	 * @attr ref android.R.styleable#GridLayout_Layout_layout_rowSpan
	 * @attr ref android.R.styleable#GridLayout_Layout_layout_column
	 * @attr ref android.R.styleable#GridLayout_Layout_layout_columnSpan
	 * @attr ref android.R.styleable#GridLayout_Layout_layout_gravity
	 */
	public static class LayoutParams extends ViewGroup.MarginLayoutParams {
		private static final int DEFAULT_WIDTH = WRAP_CONTENT;
		private static final int DEFAULT_HEIGHT = WRAP_CONTENT;
		private static final int DEFAULT_ROW_SPAN = 1;
		private static final int DEFAULT_COLUMN_SPAN = 1;

		private int columnSpan = DEFAULT_COLUMN_SPAN;
		private int rowSpan = DEFAULT_ROW_SPAN;
		private int columnIndex = UNDEFINED;
		private int rowIndex = UNDEFINED;
		private int x;
		private int y;

		private LayoutParams(
				int width, int height,
				int left, int top, int right, int bottom,
				int rowSpan, int columnSpan,
				int rowIndex, int columnIndex) {
			super(width, height);
			setMargins(left, top, right, bottom);
			this.rowSpan = rowSpan;
			this.columnSpan = columnSpan;
			this.rowIndex = rowIndex;
			this.columnIndex = columnIndex;
		}

		public LayoutParams(Context c, AttributeSet attrs) {
			super(c, attrs);
			init(c, attrs);
		}

		public LayoutParams(int rowSpan, int columnSpan) {
			this(DEFAULT_WIDTH, DEFAULT_HEIGHT,
					0, 0, 0, 0,
					rowSpan, columnSpan,
					UNDEFINED, UNDEFINED);
		}

		public LayoutParams(MarginLayoutParams source) {
			super(source);
		}

		public LayoutParams(ViewGroup.LayoutParams source) {
			super(source);
		}

		private void init(Context context, AttributeSet attrs) {
			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SquareGridLayout_Layout);
			try {

				columnIndex = a.getInt(R.styleable.SquareGridLayout_Layout_layout_column, UNDEFINED);
				columnSpan = a.getInt(R.styleable.SquareGridLayout_Layout_layout_columnSpan, DEFAULT_COLUMN_SPAN);

				rowIndex = a.getInt(R.styleable.SquareGridLayout_Layout_layout_row, UNDEFINED);
				rowSpan = a.getInt(R.styleable.SquareGridLayout_Layout_layout_rowSpan, DEFAULT_ROW_SPAN);
			} finally {
				a.recycle();
			}
		}
	}
}

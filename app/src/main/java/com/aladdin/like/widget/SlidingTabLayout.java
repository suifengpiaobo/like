package com.aladdin.like.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aladdin.like.R;
import com.aladdin.utils.ContextUtils;
import com.aladdin.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Description
 * Created by zxl on 2017/5/24 下午8:23.
 * Email:444288256@qq.com
 */
public class SlidingTabLayout extends HorizontalScrollView {
    public final static int LEFT = 0;
    public final static int TOP = 1;
    public final static int RIGHT = 2;
    public final static int BOTTOM = 3;

    private int selectedTitleTextColor = 0xFFA100;
    private int unselectedTitleTextColor = Color.WHITE;

    private int frgCount;
    private int index;
    private int tabTitleTextSize = 12;
    private List<TextView> textViewList = new ArrayList<>();
    public List<View> viewList = new ArrayList<>();

    public interface TabColorizer {

        /**
         * @return return the color of the indicator used when {@code position} is selected.
         */
        int getIndicatorColor(int position);

    }

    private static final int TITLE_OFFSET_DIPS = 24;
    private static final int TAB_VIEW_PADDING_DIPS = 16;
    private static final int TAB_VIEW_TEXT_SIZE_SP = 12;

    private int mTitleOffset;

    private int mTabViewLayoutId;
    private int mTabViewTextViewId;
    private boolean mDistributeEvenly;

    private ViewPager mViewPager;
    private SparseArray<String> mContentDescriptions = new SparseArray<String>();
    private ViewPager.OnPageChangeListener mViewPagerPageChangeListener;

    private final SlidingTabStrip mTabStrip;

    public SlidingTabLayout(Context context) {
        this(context, null);
    }

    public SlidingTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingTabLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // Disable the Scroll Bar
        setHorizontalScrollBarEnabled(false);
        // Make sure that the Tab Strips fills this View
        setFillViewport(true);

        mTitleOffset = (int) (TITLE_OFFSET_DIPS * getResources().getDisplayMetrics().density);

        mTabStrip = new SlidingTabStrip(context);
        addView(mTabStrip, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    public void setSlidingTabStripCenter() {
        mTabStrip.setSlidingTabStripCenter();
    }

    public void setSlidingTabStripLeft() {
        mTabStrip.setSlidingTabStripLeft();
    }

    public void setTitleTextColor(int selectedTitleTextColor, int unselectedTitleTextColor) {
        this.selectedTitleTextColor = selectedTitleTextColor;
        this.unselectedTitleTextColor = unselectedTitleTextColor;
    }

    public void setCustomTabColorizer(TabColorizer tabColorizer) {
        mTabStrip.setCustomTabColorizer(tabColorizer);
    }

    public void setDistributeEvenly(boolean distributeEvenly) {
        mDistributeEvenly = distributeEvenly;
    }

    /**
     * Sets the colors to be used for indicating the selected tab. These colors are treated as a
     * circular array. Providing one color will mean that all tabs are indicated with the same color.
     */
    public void setSelectedIndicatorColors(int... colors) {
        mTabStrip.setSelectedIndicatorColors(colors);
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mViewPagerPageChangeListener = listener;
    }

    /**
     * Set the custom layout to be inflated for the tab views.
     *
     * @param layoutResId Layout id to be inflated
     * @param textViewId  id of the {@link TextView} in the inflated view
     */
    public void setCustomTabView(int layoutResId, int textViewId) {
        mTabViewLayoutId = layoutResId;
        mTabViewTextViewId = textViewId;
    }

    public void removeTabStrip() {
        mTabStrip.removeAllViews();
    }

    /**
     * Sets the associated view pager. Note that the assumption here is that the pager content
     * (number of tabs and tab titles) does not change after this call has been made.
     */
    public void setViewPager(ViewPager viewPager) {
        mTabStrip.removeAllViews();

        mViewPager = viewPager;
        if (viewPager != null) {
            viewPager.setOnPageChangeListener(new InternalViewPagerListener());
            populateTabStrip();
        }
    }

    /**
     * Create a default view to be used for tabs. This is called if a custom tab view is not set via
     * {@link #setCustomTabView(int, int)}.
     */
    protected TextView createDefaultTabView(Context context) {
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, TAB_VIEW_TEXT_SIZE_SP);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground,
                outValue, true);
        textView.setBackgroundResource(outValue.resourceId);
        textView.setAllCaps(true);

        int padding = (int) (TAB_VIEW_PADDING_DIPS * getResources().getDisplayMetrics().density);
        textView.setPadding(padding, padding, padding, padding);

        ++index;
        return textView;
    }

    private void populateTabStrip() {
        final PagerAdapter adapter = mViewPager.getAdapter();
        frgCount = adapter.getCount();
        final OnClickListener tabClickListener = new TabClickListener();

        textViewList.clear();
        for (int i = 0; i < frgCount; i++) {
            View tabView = null;
            TextView tabTitleView = null;

            if (mTabViewLayoutId != 0) {
                // If there is a custom tab view layout id set, try and inflate it
                tabView = LayoutInflater.from(getContext()).inflate(mTabViewLayoutId, mTabStrip,
                        false);
                tabTitleView = (TextView) tabView.findViewById(mTabViewTextViewId);
            }

            if (tabView == null) {
                tabView = createDefaultTabView(getContext());
            }

            if (tabTitleView == null && TextView.class.isInstance(tabView)) {
                tabTitleView = (TextView) tabView;
            }

            if (mDistributeEvenly) {
                if (frgCount == 1) {
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                    lp.width = 0;
                    lp.weight = 1;
                    lp.setMargins(DensityUtils.dip2px(15), 0, 0, 0);

                    tabTitleView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                    setSlidingTabStripLeft();
                    setSelectedIndicatorColors(ContextUtils.getInstance().getApplicationContext().getResources().getColor(R.color.transparent));
                } else {
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                    lp.width = 0;
                    lp.weight = 1;
                }
            }

            tabTitleView.setText(adapter.getPageTitle(i));
            tabTitleView.setTextSize(tabTitleTextSize);
            tabTitleView.setPadding(3, 0, 3, 0);
            tabTitleView.setMaxEms(3);
            tabTitleView.setSingleLine(true);
            tabTitleView.setEllipsize(TextUtils.TruncateAt.END);
            tabView.setOnClickListener(tabClickListener);
            String desc = mContentDescriptions.get(i, null);
            if (desc != null) {
                tabView.setContentDescription(desc);
            }

            mTabStrip.addView(tabView);
            if (i == mViewPager.getCurrentItem()) {
                tabView.setSelected(true);
            }
            textViewList.add(tabTitleView);
            viewList.add(tabView);
        }
        textViewList.get(0).setTextColor(selectedTitleTextColor);
        for (int i = 1; i < frgCount; i++) {
            textViewList.get(i).setTextColor(unselectedTitleTextColor);
        }
    }

    public void setContentDescription(int i, String desc) {
        mContentDescriptions.put(i, desc);
    }

    public void setCompoundDrawables(int position, Drawable drawable, int azimuth) {
        if (mTabStrip.getChildCount() < position) {
            new IllegalArgumentException(this.getClass().getName() + "， position error!!");
        }

        if (drawable == null) {
            new IllegalArgumentException(this.getClass().getName() + "， drawable null error!!");
        }

        drawable.setBounds(0, 0, drawable.getMinimumHeight(), drawable.getMinimumHeight());
        for (int i = 0; i < mTabStrip.getChildCount(); i++) {
            if (i == position) {
                switch (azimuth) {
                    case LEFT:
                        ((TextView) mTabStrip.getChildAt(i)).setCompoundDrawables(drawable, null, null, null);
                        break;
                    case TOP:
                        ((TextView) mTabStrip.getChildAt(i)).setCompoundDrawables(null, drawable, null, null);
                        break;

                    case RIGHT:
                        ((TextView) mTabStrip.getChildAt(i)).setCompoundDrawables(null, null, drawable, null);
                        break;

                    case BOTTOM:
                        ((TextView) mTabStrip.getChildAt(i)).setCompoundDrawables(null, null, null, drawable);
                        break;
                }
            }
        }
    }

    public void setTabStripWidth(int width) {
        mTabStrip.setWidth(width);
    }

    public void setTabTitleTextSize(int tabTitleTextSize) {
        this.tabTitleTextSize = tabTitleTextSize;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (mViewPager != null) {
            scrollToTab(mViewPager.getCurrentItem(), 0);
        }
    }

    private void scrollToTab(int tabIndex, int positionOffset) {
        final int tabStripChildCount = mTabStrip.getChildCount();
        if (tabStripChildCount == 0 || tabIndex < 0 || tabIndex >= tabStripChildCount) {
            return;
        }

        View selectedChild = mTabStrip.getChildAt(tabIndex);
        if (selectedChild != null) {
            int targetScrollX = selectedChild.getLeft() + positionOffset;

            if (tabIndex > 0 || positionOffset > 0) {
                // If we're not at the first child and are mid-scroll, make sure we obey the offset
                targetScrollX -= mTitleOffset;
            }

            scrollTo(targetScrollX, 0);
        }
    }

    private class InternalViewPagerListener implements ViewPager.OnPageChangeListener {
        private int mScrollState;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int tabStripChildCount = mTabStrip.getChildCount();
            if ((tabStripChildCount == 0) || (position < 0) || (position >= tabStripChildCount)) {
                return;
            }

            mTabStrip.onViewPagerPageChanged(position, positionOffset);

            View selectedTitle = mTabStrip.getChildAt(position);
            int extraOffset = (selectedTitle != null)
                    ? (int) (positionOffset * selectedTitle.getWidth())
                    : 0;
            scrollToTab(position, extraOffset);

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrolled(position, positionOffset,
                        positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            mScrollState = state;

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {

            for (int i = 0; i < frgCount; i++) {
                if (position == i) {
                    textViewList.get(i).setTextColor(selectedTitleTextColor);
                } else {
                    textViewList.get(i).setTextColor(unselectedTitleTextColor);
                }
            }
            if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                mTabStrip.onViewPagerPageChanged(position, 0f);
                scrollToTab(position, 0);
            }
            for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                mTabStrip.getChildAt(i).setSelected(position == i);
            }
            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageSelected(position);
            }
        }

    }

    public interface OnCurrentViewClickListener {
        void onClick(View view, int position);
    }

    private OnCurrentViewClickListener onCurrentViewClickListener;

    public void setOnCurrentViewClickListener(OnCurrentViewClickListener onClickListener) {
        this.onCurrentViewClickListener = onClickListener;
    }

    private class TabClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                if (v == mTabStrip.getChildAt(i)) {
                    if (onCurrentViewClickListener != null) {
                        onCurrentViewClickListener.onClick(v, i);
                    }
                    mViewPager.setCurrentItem(i);
                    return;
                }
            }
        }
    }

    public void onDestroys() {
        viewList = null;
        textViewList = null;
    }
}

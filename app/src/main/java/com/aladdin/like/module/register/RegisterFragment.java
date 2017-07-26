package com.aladdin.like.module.register;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aladdin.like.R;
import com.aladdin.like.base.BaseFragment;
import com.sunfusheng.glideimageview.GlideImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Description 注册页面
 * Created by zxl on 2017/5/14下午9:59.
 */
public class RegisterFragment extends BaseFragment {
    RegisterAdapter mAdapter;

    @BindView(R.id.register_view_pager)
    ViewPager mWelViewPager;
    @BindView(R.id.view_point)
    LinearLayout mPoint;
    @BindView(R.id.register_tip)
    TextView mRegisterTip;

    private int[] mList;//图片资源的数组
    private List<View> mViews;//图片资源的集合
    private String[] mTip = {"海量精美手机墙纸", "欧美潮流图片集", "励志健身图片集"};

    private ImageView[] dots; // 底部小圆点
    private int currentIndex;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void lazyFetchData() {
        initViewPager();
        initDots();
    }

    private void initViewPager() {
        mList = new int[]{R.drawable.register_1, R.drawable.register_2, R.drawable.register_3};
        mViews = new ArrayList<>();

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin=0;

        for (int i = 0; i < mList.length; i++) {
            GlideImageView imageView = new GlideImageView(getActivity());
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.loadLocalImage(mList[i],0);
            mViews.add(imageView);
        }

        mWelViewPager.setAdapter(new RegisterAdapter(mViews));
        mRegisterTip.setText(mTip[0]);
        //设置滑动监听
        mWelViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setCurrentDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initDots() {

        dots = new ImageView[mList.length];
        // 循环取得小点图片
        for (int i = 0; i < mList.length; i++) {
            dots[i] = (ImageView) mPoint.getChildAt(i);
            dots[i].setSelected(false);// 设置默认为非选择
        }

        currentIndex = 0;
        dots[currentIndex].setSelected(true);
    }

    public void setCurrentDot(int position) {
        if (position < 0 || position > mList.length - 1 || currentIndex == position) {
            return;
        }
        mRegisterTip.setText(mTip[position]);
        dots[position].setSelected(true);
        dots[currentIndex].setSelected(false);
        currentIndex = position;
    }

    class RegisterAdapter extends PagerAdapter {
        private List<View> mPic;

        RegisterAdapter(List<View> pic) {
            this.mPic = pic;
        }

        @Override
        public int getCount() {
            return mPic.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mPic.get(position));
            return mPic.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mPic.get(position));
        }
    }
}

package com.aladdin.like.module.diarydetails;

import com.aladdin.like.R;
import com.aladdin.like.base.BaseActivity;
import com.aladdin.like.model.DiaryDetail;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;

public class DiaryDetailsActivity extends BaseActivity {

    @BindView(R.id.dirary_img)
    SimpleDraweeView mDiarary;

    DiaryDetail.Diary mDiary;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_diary_details;
    }

    @Override
    protected void initView() {
        mDiary = (DiaryDetail.Diary) getIntent().getSerializableExtra("DIARY");

        if (mDiary != null){
            mDiarary.setImageURI(mDiary.diaryImage);
        }
    }

}

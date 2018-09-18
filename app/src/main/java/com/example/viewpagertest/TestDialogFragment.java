package com.example.viewpagertest;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.util.ArrayList;
import java.util.List;

/**
 * date: 2018/9/18
 * description: TestDialogFragment
 **/
public class TestDialogFragment extends DialogFragment implements View.OnClickListener, ViewPager.OnPageChangeListener{
    private static final int CHOOSE_PHOTO_REQUEST_1 = 1000;
    private static final int CHOOSE_PHOTO_REQUEST_2 = 1001;
    private static final int CHOOSE_PHOTO_REQUEST_3 = 1002;

    private Dialog mDialog;
    private Context mContext;
    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private RecyclerView[] mPhotoRvs;
    private PhotoRecycleAdapter[] mAdapters;
    private ArrayList<String>[] mLists;
    private Button[] mButtons;

    private List<View> mViewList;

    private int mFocusPosition;//当前button焦点

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Activity activity = getActivity();
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_layout, null);
        builder.setView(view);
        mContext = activity.getApplicationContext();
        initUI(view);
        mDialog = builder.create();

        //mDialog.setCancelable(false);
        //mDialog.setCanceledOnTouchOutside(false);

        return mDialog;
    }

    private void initUI(View view){
        mViewPager = view.findViewById(R.id.resource_pager);
        mViewPager.addOnPageChangeListener(this);
        int []buttonResAry = {R.id.choose_image_btn1, R.id.choose_image_btn2, R.id.choose_image_btn3};
        int count = buttonResAry.length;
        mLists = new ArrayList[count];
        mButtons = new Button[count];
        mPhotoRvs = new RecyclerView[count];
        mAdapters = new PhotoRecycleAdapter[count];
        mViewList = new ArrayList<>();
        for(int i=0; i<count; i++){
            mButtons[i] = view.findViewById(buttonResAry[i]);
            mButtons[i].setOnClickListener(this);
        }
        for(int i=0; i<count; i++){
            mPhotoRvs[i] = (RecyclerView) View.inflate(mContext, R.layout.photo_recycler_view, null);
            mLists[i] = new ArrayList<>();
            mAdapters[i] = new PhotoRecycleAdapter(mContext, mLists[i]);
            RecyclerView.LayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            mPhotoRvs[i].setLayoutManager(manager);
            mPhotoRvs[i].setAdapter(mAdapters[i]);
            mViewList.add(mPhotoRvs[i]);
        }
        mViewPagerAdapter = new ViewPagerAdapter(mViewList);
        mViewPager.setAdapter(mViewPagerAdapter);
        mFocusPosition = 0;
        updateBtn(mFocusPosition);
    }

    private void updateBtn(int position){
        for (int i=0; i<mButtons.length; i++) {
            if(i == position){
                mButtons[i].setBackgroundResource(R.color.colorDeep);
            }else {
                mButtons[i].setBackgroundResource(R.color.colorLight);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.choose_image_btn1){
            mFocusPosition = 0;
            updateBtn(mFocusPosition);
            mViewPager.setCurrentItem(mFocusPosition);
            requestPermission(CHOOSE_PHOTO_REQUEST_1);
        }else if(v.getId() == R.id.choose_image_btn2){
            mFocusPosition = 1;
            updateBtn(mFocusPosition);
            mViewPager.setCurrentItem(mFocusPosition);
            requestPermission(CHOOSE_PHOTO_REQUEST_2);
        }else if(v.getId() == R.id.choose_image_btn3){
            mFocusPosition = 2;
            updateBtn(mFocusPosition);
            mViewPager.setCurrentItem(mFocusPosition);
            requestPermission(CHOOSE_PHOTO_REQUEST_3);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mFocusPosition = position;
        updateBtn(mFocusPosition);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    /**
     * @param request :request code
     * description : 请求权限
     */
    private void requestPermission(final int request){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AndPermission.with(this)
                    .runtime()
                    .permission(Permission.WRITE_EXTERNAL_STORAGE, Permission.CAMERA)
                    .onGranted(new Action<List<String>>() {
                        @Override
                        public void onAction(List<String> data) {
                            choosePhoto(request);
                        }
                    })
                    .onDenied(new Action<List<String>>() {
                        @Override
                        public void onAction(List<String> data) {
                            if (AndPermission.hasAlwaysDeniedPermission(mContext, data)) {
                                AndPermission.with(mContext).runtime().setting().start();
                            }
                        }
                    })
                    .start();
        } else {
            choosePhoto(request);
        }
    }

    /**
     * @param request
     * description : 选择图片
     */
    private void choosePhoto(int request) {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(8)
                .imageSpanCount(4)
                .previewImage(true)
                .previewVideo(false)
                .isCamera(true)
                .compress(true)
                .forResult(request);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == -1){//选择照片成功
            List<LocalMedia> photoList = PictureSelector.obtainMultipleResult(data);
            if(requestCode == CHOOSE_PHOTO_REQUEST_1){
                mLists[0].clear();
                for(LocalMedia media : photoList){
                    mLists[0].add(media.getCompressPath());
                }
                mAdapters[0].notifyDataSetChanged();
            }else if(requestCode == CHOOSE_PHOTO_REQUEST_2){
                for(LocalMedia media : photoList){
                    mLists[1].add(media.getCompressPath());
                }
                mAdapters[1].notifyDataSetChanged();
            }else if(requestCode == CHOOSE_PHOTO_REQUEST_3){
                for(LocalMedia media : photoList){
                    mLists[2].add(media.getCompressPath());
                }
                mAdapters[2].notifyDataSetChanged();
            }
        }
    }
}

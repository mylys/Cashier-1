package com.easygo.cashier.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.easygo.cashier.R;
import com.niubility.library.widget.imageview.CircleImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class MyTitleBar extends ConstraintLayout {

    private View mView;
    //主页面
    private CircleImageView mCircleImageView;
    private TextView mCashierAccount;
    private ImageView mHelp;
    private ImageView mSetting;
    private ImageView mNetwork;
    private ImageView mMenu;

    private View mMainView[];
    private View mOtherView[];


    public static final int STATUS_MAIN = 0;
    public static final int STATUS_OTHER = 1;
    private int mStatus;

    //后退 页面描述 工号
    private ImageView mBack;
    private TextView mTitle;
    private TextView mCashierAccountRight;

    private boolean mShowRightAccount;

    public MyTitleBar(Context context) {
        super(context);
        init(context, null);
    }

    public MyTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        initView(context);

        initAttr(context, attrs);


        showUI();

    }


    private void showUI() {
        boolean showMain = true;
        boolean showOther = false;
        switch (mStatus) {
            case STATUS_MAIN:
                showMain = true;
                showOther = false;
                break;
            case STATUS_OTHER:
                showMain = false;
                showOther = true;
                break;
        }
        for (View view : mMainView) {
            view.setVisibility(showMain ? VISIBLE : GONE);
        }
        for (View view : mOtherView) {
            view.setVisibility(showOther ? VISIBLE : GONE);
        }
        mCashierAccountRight.setVisibility(mShowRightAccount ? VISIBLE: GONE);
    }




    private void initView(Context context) {
        mView = LayoutInflater.from(context).inflate(R.layout.layout_title_bar, this, true);
        mCircleImageView = (CircleImageView) mView.findViewById(R.id.civ);
        mCashierAccount = (TextView) mView.findViewById(R.id.tv_cashier_account);

        mHelp = (ImageView) mView.findViewById(R.id.help);
        mSetting = (ImageView) mView.findViewById(R.id.setting);
        mNetwork = (ImageView) mView.findViewById(R.id.network);
        mMenu = (ImageView) mView.findViewById(R.id.menu);

        mBack = (ImageView) mView.findViewById(R.id.iv_back);
        mTitle = (TextView) mView.findViewById(R.id.tv_title);
        mCashierAccountRight = (TextView) mView.findViewById(R.id.tv_cashier);

        mMainView = new View[]{
                mCircleImageView,
                mCashierAccount,
                mSetting,
                mNetwork,
                mMenu,
        };
        mOtherView = new View[] {
                mBack,
                mTitle,
        };


    }

    private void initAttr(Context context, AttributeSet attrs) {
        if(attrs == null) {
            return;
        }
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyTitleBar);
        if(ta != null) {
            mStatus = ta.getInt(R.styleable.MyTitleBar_status, STATUS_MAIN);
            mShowRightAccount = ta.getBoolean(R.styleable.MyTitleBar_show_right_account, false);

            ta.recycle();
        }
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public void setCashierAccount(String account) {
        mCashierAccount.setText("收银员：" + account);
        mCashierAccountRight.setText("收银员：" + account);
    }

    public void showRightAccount(boolean isShow) {
        mShowRightAccount = isShow;
        mCashierAccountRight.setVisibility(mShowRightAccount? VISIBLE: GONE);
    }



}

package com.easygo.cashier.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.easygo.cashier.R;

import androidx.constraintlayout.widget.ConstraintLayout;

public class MyTitleBar extends ConstraintLayout {

    private View mView;
    //主页面
    private ImageView mLogo;
    private View mLine;
    private TextView mCashierAccount;
    private ImageView mSetting;
    private ImageView mNetwork;
    private ImageView mMenu;

    private View mRightIconView[];
    private View mTitleView[];

    private String mTitleText;//标题文本

    //后退 页面描述 工号
    private ConstraintLayout mBack;
    private TextView mTitle;
    private TextView mCashierAccountRight;

    private boolean mShowLogo;
    private boolean mShowTitle;
    private boolean mShowAccount;
    private boolean mShowRightIcon;
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


    //设置图标可见性
    private void showUI() {
        mLogo.setVisibility(mShowLogo ? VISIBLE: GONE);
        setVisibility(mTitleView, mShowTitle);
        mCashierAccount.setVisibility(mShowAccount ? VISIBLE: GONE);
        mLine.setVisibility(mShowAccount ? VISIBLE: GONE);
        setVisibility(mRightIconView, mShowRightIcon);
        mCashierAccountRight.setVisibility(mShowRightAccount ? VISIBLE: GONE);
    }

    private void setVisibility(View[] views, boolean visible) {
        for (View view : views) {
            view.setVisibility(visible ? VISIBLE : GONE);
        }
    }


    private void initView(Context context) {
        mView = LayoutInflater.from(context).inflate(R.layout.layout_title_bar, this, true);
        mLogo = (ImageView) mView.findViewById(R.id.iv);
        mLine = mView.findViewById(R.id.line);
        mCashierAccount = (TextView) mView.findViewById(R.id.tv_cashier_account);

        mSetting = (ImageView) mView.findViewById(R.id.setting);
        mNetwork = (ImageView) mView.findViewById(R.id.network);
        mMenu = (ImageView) mView.findViewById(R.id.menu);

        mBack = (ConstraintLayout) mView.findViewById(R.id.cl_back);
        mTitle = (TextView) mView.findViewById(R.id.tv_title);
        mCashierAccountRight = (TextView) mView.findViewById(R.id.tv_cashier);

        mRightIconView = new View[]{
                mSetting,
                mNetwork,
                mMenu,
        };
        mTitleView = new View[] {
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
            mTitleText = ta.getString(R.styleable.MyTitleBar_title_text);
            mShowLogo = ta.getBoolean(R.styleable.MyTitleBar_show_logo, true);
            mShowTitle = ta.getBoolean(R.styleable.MyTitleBar_show_title, false);
            mShowAccount = ta.getBoolean(R.styleable.MyTitleBar_show_account, false);
            mShowRightAccount = ta.getBoolean(R.styleable.MyTitleBar_show_right_account, false);
            mShowRightIcon = ta.getBoolean(R.styleable.MyTitleBar_show_right_icon, false);

            if(mShowTitle)
                mShowLogo = false;


            ta.recycle();
        }

        mTitle.setText(mTitleText);

    }

    public void setTitle(String title) {
        mTitleText = title;
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

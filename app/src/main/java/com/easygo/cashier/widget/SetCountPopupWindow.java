package com.easygo.cashier.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.easygo.cashier.R;
import com.niubility.library.utils.ScreenUtils;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * 设置商品数量 对话框
 */
public class SetCountPopupWindow extends PopupWindow {

    private View mView;
    private ConstraintLayout mRoot;
    private TextView mTv50;
    private TextView mTv100;
    private TextView mTv200;
    private TextView mTv1;
    private TextView mTv2;
    private TextView mTv3;
    private TextView mTv4;
    private TextView mTv5;
    private TextView mTv6;
    private TextView mTv7;
    private TextView mTv8;
    private TextView mTv9;
    private TextView mTv0;
    private TextView mTv00;
    private TextView mTvPoint;
    private ConstraintLayout mDelete;

    private TextView[] mTvs;
    private EditText mEditText;
    private CountTextView mCountTextView;

    /**
     * 记录原始数量
     */
    private int mOriginCount;
    private boolean firstSet = true;
    private Bitmap bg;
    private Bitmap bg_reverse;

    public SetCountPopupWindow(Context context, CountTextView countTextView, int originCount) {
        super(context);

        mCountTextView = countTextView;
        mOriginCount = originCount;
        firstSet = true;
        init(context);
    }

    private void init(Context context) {
        mView = LayoutInflater.from(context).inflate(R.layout.layout_set_count_dialog, null, false);
        setContentView(mView);

        initView(mView);

        initBg();
    }

    private void initBg() {
        bg = BitmapFactory.decodeResource(mView.getContext().getResources(), R.drawable.ic_main_set_count);

        bg_reverse = Bitmap.createBitmap(bg.getWidth(), bg.getHeight(), Bitmap.Config.ARGB_8888);
        Matrix matrix = new Matrix();
        matrix.setRotate(180, bg.getWidth() / 2f, bg.getHeight() / 2f);
        Canvas canvas = new Canvas(bg_reverse);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG));
        canvas.drawBitmap(bg, matrix, null);

    }

    private void initView(View rootView) {
        mRoot = mView.findViewById(R.id.root);
        mEditText = new EditText(this.getContentView().getContext());
        mEditText.setText(String.valueOf(mOriginCount));

        mTv1 = (TextView) mView.findViewById(R.id.tv_1);
        mTv2 = (TextView) mView.findViewById(R.id.tv_2);
        mTv3 = (TextView) mView.findViewById(R.id.tv_3);
        mTv4 = (TextView) mView.findViewById(R.id.tv_4);
        mTv5 = (TextView) mView.findViewById(R.id.tv_5);
        mTv6 = (TextView) mView.findViewById(R.id.tv_6);
        mTv7 = (TextView) mView.findViewById(R.id.tv_7);
        mTv8 = (TextView) mView.findViewById(R.id.tv_8);
        mTv9 = (TextView) mView.findViewById(R.id.tv_9);
        mTv00 = (TextView) mView.findViewById(R.id.tv_00);
        mTv0 = (TextView) mView.findViewById(R.id.tv_0);
        mDelete = mView.findViewById(R.id.cl_delete);

        mTvs = new TextView[11];
        mTvs[0] = mTv1;
        mTvs[1] = mTv2;
        mTvs[2] = mTv3;
        mTvs[3] = mTv4;
        mTvs[4] = mTv5;
        mTvs[5] = mTv6;
        mTvs[6] = mTv7;
        mTvs[7] = mTv8;
        mTvs[8] = mTv9;
        mTvs[9] = mTv00;
        mTvs[10] = mTv0;

        for (TextView tv : mTvs) {

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textView = (TextView) v;

                    if (mEditText != null && mEditText.isEnabled()) {
                        Editable editable = mEditText.getText();
                        CharSequence text = textView.getText();
                        String content = text.toString();
                        int length = editable.length();
                        if (length == 0 || firstSet) {
                            if ("0".equals(content) || "00".equals(content)) {
                                return;
                            }
                        }

                        if (length <= 3 && text.length() == 1 || length <= 2 && text.length() == 2) {
                            if (firstSet) {
                                editable.clear();
                                firstSet = false;
                            }
                            editable.append(text);

                            if (listener != null) {
                                listener.onCount(editable.toString().trim());
                            }
                        }


                    }
                }
            });
        }

        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText != null && mEditText.isEnabled()) {

                    if (firstSet) {
                        return;
                    }
                    Editable editable = mEditText.getText();
                    int length = editable.length();

                    if (length == 0) {
                        return;
                    } else if (length > 0) {
                        editable.delete(length - 1, length);
                    }

                    if (editable.length() == 0) {
                        editable.append(String.valueOf(mOriginCount));
                    }

                    if (listener != null) {
                        listener.onCount(editable.toString().trim());
                    }
                }
            }
        });
    }

    OnClickListener listener;

    public interface OnClickListener {
        void onCount(String content);
    }

    public void setOnCountClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public void showCenter() {

        mCountTextView.post(new Runnable() {
            @Override
            public void run() {
                int[] location = new int[2];
                mCountTextView.getLocationOnScreen(location);

                mView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                int width = mView.getMeasuredWidth();
                int height = mView.getMeasuredHeight();

                int left = location[0];
                int top = location[1];
                Log.i("test", "showCenter: y - " + location[1]);
                int countTextViewWidth = mCountTextView.getMeasuredWidth();
                int countTextViewHeight = mCountTextView.getMeasuredHeight();

                int offset_x = (int) ((left + countTextViewWidth * 0.5f) - (left + width * 0.5f));
                int offset_y = 0;

                if (top + countTextViewHeight + height > ScreenUtils.getScreenHeight(mView.getContext())) {
                    offset_y = -(countTextViewHeight + height);

                    mRoot.setBackground(new BitmapDrawable(bg_reverse));
                } else {
                    mRoot.setBackground(new BitmapDrawable(bg));
                }

                if (isShowing()) {
                    update(mCountTextView, offset_x, offset_y, width, height);
                } else {
                    showAsDropDown(mCountTextView, offset_x, offset_y);
                }
            }
        });
    }
}

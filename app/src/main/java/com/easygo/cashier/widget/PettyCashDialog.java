package com.easygo.cashier.widget;

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.easygo.cashier.R;
import com.niubility.library.base.BaseDialog;

/**
 * @Describe：
 * @author：hgeson
 * @date：2018-12-27
 */
public class PettyCashDialog extends BaseDialog {
    private TextView tvTitle;
    private EditText editText;
    private ImageView tvCancel;
    private DialogKeyboard key_board;

    private int mTitleResId = -1;
    private int mHintResId = -1;
    private boolean mTvCancelVisibility;


    @Override
    protected int getLayoutId() {
        return R.layout.dialog_petty_cash;
    }

    @Override
    protected int getAnimation() {
        return R.style.CustomDialogStyle;
    }

    @Override
    protected void initView(View rootView) {
        setCancelable(false);

        tvTitle = rootView.findViewById(R.id.tv_title);
        editText = rootView.findViewById(R.id.edit_input);
        tvCancel = rootView.findViewById(R.id.dialog_cancel);
        key_board = rootView.findViewById(R.id.key_board);

        key_board.attachEditText(editText);
        key_board.setOnSureClickListener(new DialogKeyboard.OnSureClickListener() {
            @Override
            public void onContent(String string) {
                if (listener != null) {
                    listener.onClick(string);
                }
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        if (mTitleResId != -1 && mHintResId != -1) {
            tvTitle.setText(mTitleResId);
            editText.setHint(mHintResId);
            setCancelable(true);
        }

        tvCancel.setVisibility(mTvCancelVisibility ? View.VISIBLE : View.GONE);

    }

    public void setNoCode() {
        mTitleResId = R.string.text_no_barcode_goods;
        mHintResId = R.string.input_price;
        mTvCancelVisibility = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            window.getDecorView().setSystemUiVisibility(uiOptions);
        }
    }

    private OnDialogClickListener listener;

    public interface OnDialogClickListener {
        void onClick(String content);
    }

    public void setOnDialogClickListener(OnDialogClickListener listener) {
        this.listener = listener;
    }

    public boolean isShow() {
        return isShowing();
    }

    @Override
    protected boolean shouldHideBackground() {
        return false;
    }

    @Override
    protected boolean canCanceledOnTouchOutside() {
        return true;
    }

    @Override
    protected boolean isWindowWidthMatchParent() {
        return false;
    }

    public void showCenter(FragmentActivity activity) {
        showCenter(activity, "DIALOG_PETTY");
    }
}

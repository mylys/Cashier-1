package com.easygo.cashier.widget.dialog;

import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.easygo.cashier.R;
import com.easygo.cashier.adapter.GoodsEntity;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.module.promotion.base.IPromotion;
import com.easygo.cashier.widget.view.DialogKeyboard;
import com.niubility.library.base.BaseDialog;
import com.niubility.library.utils.ToastUtils;

import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * 临时折扣、改价对话框
 */
public class TempPromotionDialog extends BaseDialog {
    private TextView tvTitle;
    private TextView tvChooseGoodsCount;

    private ConstraintLayout clTextDiscount;
    private ConstraintLayout clTextChangePrice;
    private View chooseDiscount;
    private View chooseChangePrice;
    private ConstraintLayout clInputDiscount;
    private ConstraintLayout clInputChangePrice;
    private ConstraintLayout clChooseDiscount;
    private EditText etDiscount;
    private EditText etChangePrice;

    //折扣
    private TextView tv90;
    private TextView tv80;
    private TextView tv70;
    private TextView tv60;
    private TextView tvDiscountFreeOrder;

    //改价
    private TextView tvChangePriceFreeOrder;
    private ImageView tvCancel;
    private DialogKeyboard key_board;

    private int mTitleResId = -1;
    private int mHintResId = -1;
    private boolean mTvCancelVisibility = true;
    private boolean canInputDecimal = true;

    public static final int MODE_DISCOUNT = IPromotion.OFFER_TYPE_RATIO;
    public static final int MODE_CHANGE_PRICE = IPromotion.OFFER_TYPE_MONEY;
    private int mode = MODE_DISCOUNT;

    private View selected;
    private boolean isFreeOrder;


    private List<GoodsEntity<GoodsResponse>> selectGoods;



    @Override
    protected int getLayoutId() {
        return R.layout.layout_dialog_temp_promotion;
    }

    @Override
    protected int getAnimation() {
        return R.style.CustomDialogStyle;
    }

    @Override
    protected void initView(View rootView) {

        tvTitle = rootView.findViewById(R.id.tv_title);
        tvChooseGoodsCount = rootView.findViewById(R.id.tv_choose_goods_count);

        clTextDiscount = rootView.findViewById(R.id.cl_text_discount);
        clTextChangePrice = rootView.findViewById(R.id.cl_text_change_price);
        chooseDiscount = rootView.findViewById(R.id.choose_discount);
        chooseChangePrice = rootView.findViewById(R.id.choose_change_price);

        clInputDiscount = rootView.findViewById(R.id.cl_input_discount);
        clInputChangePrice = rootView.findViewById(R.id.cl_input_change_price);
        clChooseDiscount = rootView.findViewById(R.id.cl_choose_discount);
        etDiscount = rootView.findViewById(R.id.et_discount);
        etChangePrice = rootView.findViewById(R.id.et_change_price);

        tv90 = rootView.findViewById(R.id.tv_90);
        tv80 = rootView.findViewById(R.id.tv_80);
        tv70 = rootView.findViewById(R.id.tv_70);
        tv60 = rootView.findViewById(R.id.tv_60);

        tvDiscountFreeOrder = rootView.findViewById(R.id.tv_discount_free_order);
        tvChangePriceFreeOrder = rootView.findViewById(R.id.tv_change_price_free_order);

        tvCancel = rootView.findViewById(R.id.dialog_cancel);
        key_board = rootView.findViewById(R.id.key_board);

        key_board.setCanInputDecimal(canInputDecimal);
        key_board.attachEditText(etDiscount);
//        key_board.setOnSureClickListener(new DialogKeyboard.OnSureClickListener() {
//            @Override
//            public void onContent(String string) {
//                if (listener != null) {
//                    listener.onClick(string);
//                }
//            }
//        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        if (mTitleResId != -1 && mHintResId != -1) {
            tvTitle.setText(mTitleResId);
            etDiscount.setHint(mHintResId);
        }

        if(selectGoods != null) {
            tvChooseGoodsCount.setText("（已选" + selectGoods.size() + "件商品）");
        } else {
            tvChooseGoodsCount.setText("");
        }


        tvCancel.setVisibility(mTvCancelVisibility ? View.VISIBLE : View.GONE);


        setListener();
    }

    private void setListener() {

        clTextDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMode(MODE_DISCOUNT);
            }
        });
        clTextChangePrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMode(MODE_CHANGE_PRICE);
            }
        });

        tv90.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tv90.isSelected()) {
                    reset();
                    etDiscount.getText().append("90");
                    tv90.setSelected(true);
                    selected = tv90;
                } else {
                    reset();
                }
            }
        });
        tv80.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tv80.isSelected()) {
                    reset();
                    etDiscount.getText().append("80");
                    tv80.setSelected(true);
                    selected = tv80;
                } else {
                    reset();
                }
            }
        });
        tv70.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tv70.isSelected()) {
                    reset();
                    etDiscount.getText().append("70");
                    tv70.setSelected(true);
                    selected = tv70;
                } else {
                    reset();
                }
            }
        });
        tv60.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tv60.isSelected()) {
                    reset();
                    etDiscount.getText().append("60");
                    tv60.setSelected(true);
                    selected = tv60;
                } else {
                    reset();
                }
            }
        });
        tvDiscountFreeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tvDiscountFreeOrder.isSelected()) {
                    reset();
                    tvDiscountFreeOrder.setSelected(true);
                    selected = tvDiscountFreeOrder;
                    isFreeOrder = true;
                } else {
                    reset();
                }
            }
        });
        tvChangePriceFreeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tvChangePriceFreeOrder.isSelected()) {
                    reset();
                    tvChangePriceFreeOrder.setSelected(true);
                    selected = tvChangePriceFreeOrder;
                    isFreeOrder = true;
                } else {
                    reset();
                }
            }
        });

        key_board.setOnKeyboardClickListener(new DialogKeyboard.OnKeyboardClickListener() {
            @Override
            public void onClickBefore() {
                if(selected != null) {
                    selected.setSelected(false);
                    etDiscount.setText("");
                    etChangePrice.setText("");
                    selected = null;
                }
                isFreeOrder = false;
            }

            @Override
            public void onDeleteClickAfter(Editable editable) {
                if(selected != null) {
                    selected.setSelected(false);
                    selected = null;
                }
                isFreeOrder = false;
            }

            @Override
            public void onTextChangedAfter(Editable editable) {

                String s = editable.toString();
                float discount = Float.valueOf(s);

                if(mode == MODE_DISCOUNT) {
                    if(discount > 100) {
                        editable.delete(editable.length() - 1, editable.length());
                        ToastUtils.showToast(getContext(), "折扣不能大于100%");
                    } else if(discount < 0.01f) {
                        editable.delete(editable.length() - 1, editable.length());
                        ToastUtils.showToast(getContext(), "折扣不能小于0.01%");
                    }
                }
            }
        });

        key_board.getSureButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float value = 0f;
                if(mode == MODE_DISCOUNT) {
                    if(isFreeOrder) {
                        value = 0f;
                    } else {
                        String discount = etDiscount.getText().toString();
                        if(TextUtils.isEmpty(discount)) {
                            ToastUtils.showToast(getContext(), "折扣不能为空");
                            return;
                        }
                        value = Float.valueOf(discount);
                    }
                } else {
                    if(isFreeOrder) {
                        value = 0f;
                    } else {
                        String changePrice = etChangePrice.getText().toString();
                        if(TextUtils.isEmpty(changePrice)) {
                            ToastUtils.showToast(getContext(), "改价金额不能为空");
                            return;
                        }
                        value = Float.valueOf(changePrice);
                    }
                }

                if(listener != null) {
                    listener.onClick(selectGoods, mode, isFreeOrder, value);
                }
            }
        });

    }

    private void reset() {
        if(mode == MODE_DISCOUNT) {
            etDiscount.getText().clear();
        } else {
            etChangePrice.getText().clear();
        }
        if(selected != null) {
            selected.setSelected(false);
            selected = null;
        }
        isFreeOrder = false;
    }

    public void setMode(int mode) {
        this.mode = mode;
        reset();
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) key_board.getLayoutParams();
        switch (mode) {
            case MODE_DISCOUNT:
                chooseDiscount.setVisibility(View.VISIBLE);
                chooseChangePrice.setVisibility(View.INVISIBLE);

                clInputDiscount.setVisibility(View.VISIBLE);
                clInputChangePrice.setVisibility(View.GONE);
                clChooseDiscount.setVisibility(View.VISIBLE);
                key_board.attachEditText(etDiscount);
                lp.topMargin = getResources().getDimensionPixelSize(R.dimen.y14);
                etDiscount.setText("");

                break;
            case MODE_CHANGE_PRICE:
                chooseDiscount.setVisibility(View.INVISIBLE);
                chooseChangePrice.setVisibility(View.VISIBLE);

                clInputDiscount.setVisibility(View.GONE);
                clInputChangePrice.setVisibility(View.VISIBLE);
                clChooseDiscount.setVisibility(View.GONE);
                key_board.attachEditText(etChangePrice);
                lp.topMargin = getResources().getDimensionPixelSize(R.dimen.y40);
                etChangePrice.setText("");

                break;
        }
        key_board.setLayoutParams(lp);
    }

    public void setSelected(List<GoodsEntity<GoodsResponse>> selected) {
        selectGoods = selected;
    }

    public void setCanInputDecimal(boolean canInputDecimal) {
        this.canInputDecimal = canInputDecimal;
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

        void onClick(List<GoodsEntity<GoodsResponse>> selectGoods, int mode, boolean isFreeOrder, float value);
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
        showCenter(activity, "DIALOG_TEMP_PROMOTION");
    }
}

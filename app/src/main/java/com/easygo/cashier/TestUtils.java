package com.easygo.cashier;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * @Describe：
 * @author：hgeson
 * @date：2018-12-26
 */
public class TestUtils {

    private ViewTreeObserver.OnGlobalLayoutListener mSoftKeyBoardListener;

    private class SoftKeyBoardListener implements ViewTreeObserver.OnGlobalLayoutListener {

        private View root;
        private View view;

        int lastHeight = 0;
        int lastBottom = -1;

        SoftKeyBoardListener(View r,View v) {
            root = r;
            view = v;
        }

        @Override
        public void onGlobalLayout() {
            Rect rect = new Rect();
            root.getWindowVisibleDisplayFrame(rect);
            if (lastBottom == -1) {
                lastBottom = rect.bottom;
                return;
            }

            int nb = rect.bottom;
            int ob = lastBottom;

            if (nb < ob) {
                // 键盘显示了， 滑上去
                int[] location = new int[2];
                view.getLocationInWindow(location);
                int scrollHeight = (location[1] + view.getHeight()) - nb;
                root.scrollTo(0, scrollHeight);
                lastHeight = scrollHeight;
            }
            else if (nb > ob) {
                // 键盘隐藏了, 滑下来
                root.scrollTo(0, 0);
            }

            if (nb != ob) {
                lastBottom = nb;
            }
        }
    }

    public void removeSoftKeyBoardListener(@NonNull View root) {
        if(mSoftKeyBoardListener != null){
            root.getViewTreeObserver().removeOnGlobalLayoutListener(mSoftKeyBoardListener);
        }
    }

    public void addSoftKeyBoardListener(final View root, final View view) {
        if (mSoftKeyBoardListener == null) {
            mSoftKeyBoardListener = new SoftKeyBoardListener(root, view);
        }
        root.getViewTreeObserver().addOnGlobalLayoutListener(mSoftKeyBoardListener);
    }
}

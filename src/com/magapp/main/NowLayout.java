/*
 * @category     Sashas
 * @package      com.magapp
 * @author       Sashas IT Support <support@sashas.org>
 * @copyright    2007-2016 Sashas IT Support Inc. (http://www.sashas.org)
 * @license      http://opensource.org/licenses/GPL-3.0  GNU General Public License, version 3 (GPL-3.0)
 * @link         https://play.google.com/store/apps/details?id=com.magapp.main
 *
 */

package com.magapp.main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

public class NowLayout extends LinearLayout implements OnGlobalLayoutListener {

    public NowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayoutObserver();

    }

    public NowLayout(Context context) {
        super(context);
        initLayoutObserver();
    }

    private void initLayoutObserver() {
        setOrientation(LinearLayout.VERTICAL);
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        getViewTreeObserver().removeGlobalOnLayoutListener(this);

        final int heightPx = getContext().getResources().getDisplayMetrics().heightPixels;

        boolean inversed = false;
        final int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);

            int[] location = new int[2];

            child.getLocationOnScreen(location);

            if (location[1] > heightPx) {
                break;
            }

            if (!inversed) {
                child.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_up_left));
            } else {
                child.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_up_right));
            }

            inversed = !inversed;
        }

    }
}

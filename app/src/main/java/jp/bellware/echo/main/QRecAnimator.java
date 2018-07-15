package jp.bellware.echo.main;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;

/**
 * メイン画面のアニメ隔離クラス
 */
public class QRecAnimator {



    public void startDeleteAnimation(View view){
        float dp = view.getResources().getDisplayMetrics().density;
        PropertyValuesHolder pvha = PropertyValuesHolder.ofFloat("alpha",1,0);
        PropertyValuesHolder pvht = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y,0,100*dp);
        ObjectAnimator animator;
        if(view.getAlpha() == 1)
            animator = ObjectAnimator.ofPropertyValuesHolder(view,pvha,pvht);
        else
            animator = ObjectAnimator.ofPropertyValuesHolder(view,pvht);
        animator.setDuration(200);
        animator.start();
    }

    /**
     * フェードインを行う
     *
     * @param view
     */
    public void fadeIn(View view) {
        view.setVisibility(View.VISIBLE);
        view.setTranslationY(0);
        ObjectAnimator a = ObjectAnimator.ofFloat(view, "alpha", 0, 1);
        a.setDuration(500);
        a.start();
    }

    public void fadeOut(View view) {
        view.setVisibility(View.VISIBLE);
        ObjectAnimator a = ObjectAnimator.ofFloat(view, "alpha", 1, 0);
        a.setDuration(500);
        a.start();
    }

}

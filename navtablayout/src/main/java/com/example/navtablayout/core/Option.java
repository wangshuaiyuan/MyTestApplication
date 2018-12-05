package com.example.navtablayout.core;

import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.RestrictTo;

import com.example.navtablayout.NavTabIndicator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;

/**
 * Created by wsy on 26/11/2018
 * <p>
 * TabLayout 配置类
 */
public class Option {



    @RestrictTo(LIBRARY_GROUP)
    @IntDef(value = {MODE_SCROLLABLE, MODE_FIXED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {
    }

    @RestrictTo(LIBRARY_GROUP)
    @IntDef(value = {GRAVITY_CENTER, GRAVITY_FILL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Gravity {
    }

    @RestrictTo(LIBRARY_GROUP)
    @IntDef(value = {INDICATOR_ALIGN_TOP, INDICATOR_ALIGN_BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface IndicatorAlign {
    }

    /**
     * 固定模式
     */
    public static final int MODE_FIXED = 1001;
    /**
     * 滚动模式
     */
    public static final int MODE_SCROLLABLE = 1002;


    /**
     * 居中布局，该布局方式配合fixed模式使用{@link #MODE_FIXED}.
     */
    public static final int GRAVITY_CENTER = 2001;
    /**
     * 填充布局
     */
    public static final int GRAVITY_FILL = 2002;


    /**
     * 指示器位于顶部
     */
    public static final int INDICATOR_ALIGN_TOP = 3001;
    /**
     * 指示器位于底部
     */
    public static final int INDICATOR_ALIGN_BOTTOM = 3002;


    private Option() {

    }

    /**
     * 指示器渲染
     */
    private AbsTabIndicatorRender mIndicatorRender;
    /**
     * 模式
     */
    private int mMode;
    /**
     * 是否展示指示器
     */
    private boolean isShowIndicator;
    /**
     * 方向
     */
    private int mGravity;

    private int mIndicatorAlign;

    public AbsTabIndicatorRender getIndicatorRender() {
        return mIndicatorRender;
    }

    public void setIndicatorRender(AbsTabIndicatorRender mIndicatorRender) {
        this.mIndicatorRender = mIndicatorRender;
    }

    public int getIndicatorAlign() {
        return mIndicatorAlign;
    }

    public void setmIndicatorAlign(int mIndicatorAlign) {
        this.mIndicatorAlign = mIndicatorAlign;
    }

    @Mode
    public int getMode() {
        return mMode;
    }

    public void setMode(@Mode int mMode) {
        this.mMode = mMode;
    }

    public boolean isShowIndicator() {
        return isShowIndicator;
    }

    public void setShowIndicator(boolean showIndicator) {
        isShowIndicator = showIndicator;
    }

    @Gravity
    public int getGravity() {
        return mGravity;
    }

    public void setGravity(@Gravity int mGravity) {
        this.mGravity = mGravity;
    }

    public void setIndicatorColor(@ColorInt int color) {
        mIndicatorRender.setIndicatorColor(color);
    }

    public static class Builder {
        private AbsTabIndicatorRender mIndicatorRender;
        private int mMode = MODE_SCROLLABLE;
        private boolean isShowIndicator = true;
        private int mGravity = GRAVITY_FILL;
        private int mIndicatorAlign = INDICATOR_ALIGN_BOTTOM;

        public Builder AbsTabIndicatorRender(AbsTabIndicatorRender indicatorRender) {
            mIndicatorRender = indicatorRender;
            return this;
        }

        /**
         * 模式
         *
         * @param mode
         * @return
         */
        public Builder mode(@Mode int mode) {
            mMode = mode;
            return this;
        }

        /**
         * 是否显示
         *
         * @param isShowIndicator
         * @return
         */
        public Builder isShowIndicator(boolean isShowIndicator) {
            this.isShowIndicator = isShowIndicator;
            return this;
        }

        /**
         * @param gravity
         * @return
         */
        public Builder gravity(@Gravity int gravity) {
            mGravity = gravity;
            return this;
        }

        /**
         * indicator位置
         */
        public Builder indicatorAlign(@IndicatorAlign int indicatorAlign) {
            mIndicatorAlign = indicatorAlign;
            return this;
        }

        public Option build() {
            Option option = new Option();
            option.isShowIndicator = isShowIndicator;
            option.mGravity = mGravity;
            option.mMode = mMode;
            option.mIndicatorAlign = mIndicatorAlign;
            if (mIndicatorRender == null) {
                mIndicatorRender = new NavTabIndicator();
            }
            option.mIndicatorRender = mIndicatorRender;
            return option;
        }
    }
}

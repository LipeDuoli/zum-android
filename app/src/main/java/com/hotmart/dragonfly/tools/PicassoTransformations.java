package com.hotmart.dragonfly.tools;

import com.squareup.picasso.Transformation;

/**
 * @author ana.silva
 * @since 4.0.0
 */
public class PicassoTransformations {
    public static Transformation getRoundedTransformation() {
        return new RoundedTransformationBuilder()
                .oval(true)
                .build();
    }

    public static Transformation getRoundedWithBordersTransformation(int borderColor, int borderWidthDP, int cornerRadiusDP) {
        return new RoundedTransformationBuilder()
                .borderColor(borderColor)
                .borderWidthDp(borderWidthDP)
                .cornerRadiusDp(cornerRadiusDP)
                .oval(false)
                .build();
    }

    public static Transformation getCorneredTransformation(int cornerRadiusDP) {
        return new RoundedTransformationBuilder()
                .cornerRadiusDp(cornerRadiusDP)
                .oval(false)
                .build();
    }
}

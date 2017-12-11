
package com.github.mikephil.charting.components;

import android.graphics.DashPathEffect;
import android.graphics.Paint;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.FSize;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the legend of the chart. The legend will contain one entry
 * per color and DataSet. Multiple colors in one DataSet are grouped together.
 * The legend object is NOT available before setting data to the chart.
 *
 * @author Philipp Jahoda
 */
public class Legend extends ComponentBase {

    /**
     * This property is deprecated - Use `horizontalAlignment`, `verticalAlignment`, `orientation`, `drawInside`,
     * `direction`.
     */
    @Deprecated
    public enum LegendPosition {
        RIGHT_OF_CHART, RIGHT_OF_CHART_CENTER, RIGHT_OF_CHART_INSIDE,
        LEFT_OF_CHART, LEFT_OF_CHART_CENTER, LEFT_OF_CHART_INSIDE,
        BELOW_CHART_LEFT, BELOW_CHART_RIGHT, BELOW_CHART_CENTER,
        ABOVE_CHART_LEFT, ABOVE_CHART_RIGHT, ABOVE_CHART_CENTER,
        PIECHART_CENTER
    }

    public enum LegendForm {
        /**
         * Avoid drawing a form
         */
        NONE,

        /**
         * Do not draw the a form, but leave space for it
         */
        EMPTY,

        /**
         * Use default (default dataset's form to the legend's form)
         */
        DEFAULT,

        /**
         * Draw a square
         */
        SQUARE,

        /**
         * Draw a circle
         */
        CIRCLE,

        /**
         * Draw a horizontal line
         */
        LINE
    }

    public enum LegendHorizontalAlignment {
        LEFT, CENTER, RIGHT
    }

    public enum LegendVerticalAlignment {
        TOP, CENTER, BOTTOM
    }

    public enum LegendOrientation {
        HORIZONTAL, VERTICAL
    }

    public enum LegendDirection {
        LEFT_TO_RIGHT, RIGHT_TO_LEFT
    }

    /**
     * The legend entries array
     */
    private LegendEntry[] mEntries = new LegendEntry[]{};

    /**
     * Entries that will be appended to the end of the auto calculated entries after calculating the legend.
     * (if the legend has already been calculated, you will need to call notifyDataSetChanged() to let the changes take effect)
     */
    private LegendEntry[] mExtraEntries;

    /**
     * Are the legend labels/colors a custom value or auto calculated? If false,
     * then it's auto, if true, then custom. default false (automatic legend)
     */
    private boolean mIsLegendCustom = false;

    private LegendHorizontalAlignment mHorizontalAlignment = LegendHorizontalAlignment.LEFT;
    private LegendVerticalAlignment mVerticalAlignment = LegendVerticalAlignment.BOTTOM;
    private LegendOrientation mOrientation = LegendOrientation.HORIZONTAL;
    private boolean mDrawInside = false;

    /**
     * the text direction for the legend
     */
    private LegendDirection mDirection = LegendDirection.LEFT_TO_RIGHT;

    /**
     * the shape/form the legend colors are drawn in
     */
    private LegendForm mShape = LegendForm.SQUARE;

    /**
     * the size of the legend forms/shapes
     */
    private float mFormSize = 8f;

    /**
     * the size of the legend forms/shapes
     */
    private float mFormLineWidth = 3f;

    /**
     * Line dash path effect used for shapes that consist of lines.
     */
    private DashPathEffect mFormLineDashEffect = null;

    /**
     * the space between the legend entries on a horizontal axis, default 6f
     */
    private float mXEntrySpace = 6f;

    /**
     * the space between the legend entries on a vertical axis, default 5f
     */
    private float mYEntrySpace = 0f;

    /**
     * the space between the legend entries on a vertical axis, default 2f
     * private float mYEntrySpace = 2f; /** the space between the form and the
     * actual label/text
     */
    private float mFormToTextSpace = 5f;

    /**
     * the space that should be left between stacked forms
     */
    private float mStackSpace = 3f;

    /**
     * the maximum relative size out of the whole chart view in percent
     */
    private float mMaxSizePercent = 0.95f;

    /**
     * default constructor
     */
    public Legend() {

        this.mTextSize = Utils.convertDpToPixel(10f);
        this.mXOffset = Utils.convertDpToPixel(5f);
        this.mYOffset = Utils.convertDpToPixel(3f); // 2
    }

    /**
     * Constructor. Provide entries for the legend.
     *
     * @param entries
     */
    public Legend(LegendEntry[] entries) {
        this();

        if (entries == null) {
            throw new IllegalArgumentException("entries array is NULL");
        }

        this.mEntries = entries;
    }

    @Deprecated
    public Legend(int[] colors, String[] labels) {
        this();

        if (colors == null || labels == null) {
            throw new IllegalArgumentException("colors array or labels array is NULL");
        }

        if (colors.length != labels.length) {
            throw new IllegalArgumentException(
                    "colors array and labels array need to be of same size");
        }

        List<LegendEntry> entries = new ArrayList<>();

        for (int i = 0; i < Math.min(colors.length, labels.length); i++) {
            final LegendEntry entry = new LegendEntry();
            entry.formColor = colors[i];
            entry.label = labels[i];

            if (entry.formColor == ColorTemplate.COLOR_SKIP)
                entry.form = LegendForm.NONE;
            else if (entry.formColor == ColorTemplate.COLOR_NONE ||
                    entry.formColor == 0)
                entry.form = LegendForm.EMPTY;

            entries.add(entry);
        }

        mEntries = entries.toArray(new LegendEntry[entries.size()]);
    }

    @Deprecated
    public Legend(List<Integer> colors, List<String> labels) {
        this(Utils.convertIntegers(colors), Utils.convertStrings(labels));
    }

    /**
     * This method sets the automatically computed colors for the legend. Use setCustom(...) to set custom colors.
     *
     * @param entries
     */
    public void setEntries(List<LegendEntry> entries) {
        mEntries = entries.toArray(new LegendEntry[entries.size()]);
    }

    public LegendEntry[] getEntries() {
        return mEntries;
    }

    /**
     * returns the maximum length in pixels across all legend labels + formsize
     * + formtotextspace
     *
     * @param p the paint object used for rendering the text
     * @return
     */
    public float getMaximumEntryWidth(Paint p) {

        float max = 0f;
        float maxFormSize = 0f;
        float formToTextSpace = Utils.convertDpToPixel(mFormToTextSpace);

        for (LegendEntry entry : mEntries) {
            final float formSize = Utils.convertDpToPixel(
                    Float.isNaN(entry.formSize)
                    ? mFormSize : entry.formSize);
            if (formSize > maxFormSize)
                maxFormSize = formSize;

            String label = entry.label;
            if (label == null) continue;

            float length = (float) Utils.calcTextWidth(p, label);

            if (length > max)
                max = length;
        }

        return max + maxFormSize + formToTextSpace;
    }

    /**
     * returns the maximum height in pixels across all legend labels
     *
     * @param p the paint object used for rendering the text
     * @return
     */
    public float getMaximumEntryHeight(Paint p) {

        float max = 0f;

        for (LegendEntry entry : mEntries) {
            String label = entry.label;
            if (label == null) continue;

            float length = (float) Utils.calcTextHeight(p, label);

            if (length > max)
                max = length;
        }

        return max;
    }

    @Deprecated
    public int[] getColors() {

        int[] old = new int[mEntries.length];
        for (int i = 0; i < mEntries.length; i++) {
            old[i] = mEntries[i].form == LegendForm.NONE ? ColorTemplate.COLOR_SKIP :
                    (mEntries[i].form == LegendForm.EMPTY ? ColorTemplate.COLOR_NONE :
                            mEntries[i].formColor);
        }
        return old;
    }

    @Deprecated
    public String[] getLabels() {

        String[] old = new String[mEntries.length];
        for (int i = 0; i < mEntries.length; i++) {
            old[i] = mEntries[i].label;
        }
        return old;
    }

    @Deprecated
    public int[] getExtraColors() {

        int[] old = new int[mExtraEntries.length];
        for (int i = 0; i < mExtraEntries.length; i++) {
            old[i] = mExtraEntries[i].form == LegendForm.NONE ? ColorTemplate.COLOR_SKIP :
                    (mExtraEntries[i].form == LegendForm.EMPTY ? ColorTemplate.COLOR_NONE :
                            mExtraEntries[i].formColor);
        }
        return old;
    }

    @Deprecated
    public String[] getExtraLabels() {

        String[] old = new String[mExtraEntries.length];
        for (int i = 0; i < mExtraEntries.length; i++) {
            old[i] = mExtraEntries[i].label;
        }
        return old;
    }

    public LegendEntry[] getExtraEntries() {

        return mExtraEntries;
    }

    public void setExtra(List<LegendEntry> entries) {
        mExtraEntries = entries.toArray(new LegendEntry[entries.size()]);
    }

    public void setExtra(LegendEntry[] entries) {
        if (entries == null)
            entries = new LegendEntry[]{};
        mExtraEntries = entries;
    }

    @Deprecated
    public void setExtra(List<Integer> colors, List<String> labels) {
        setExtra(Utils.convertIntegers(colors), Utils.convertStrings(labels));
    }

    /**
     * Entries that will be appended to the end of the auto calculated
     *   entries after calculating the legend.
     * (if the legend has already been calculated, you will need to call notifyDataSetChanged()
     *   to let the changes take effect)
     */
    public void setExtra(int[] colors, String[] labels) {

        List<LegendEntry> entries = new ArrayList<>();

        for (int i = 0; i < Math.min(colors.length, labels.length); i++) {
            final LegendEntry entry = new LegendEntry();
            entry.formColor = colors[i];
            entry.label = labels[i];

            if (entry.formColor == ColorTemplate.COLOR_SKIP ||
                    entry.formColor == 0)
                entry.form = LegendForm.NONE;
            else if (entry.formColor == ColorTemplate.COLOR_NONE)
                entry.form = LegendForm.EMPTY;

            entries.add(entry);
        }

        mExtraEntries = entries.toArray(new LegendEntry[entries.size()]);
    }

    /**
     * Sets a custom legend's entries array.
     * * A null label will start a group.
     * This will disable the feature that automatically calculates the legend
     *   entries from the datasets.
     * Call resetCustom() to re-enable automatic calculation (and then
     *   notifyDataSetChanged() is needed to auto-calculate the legend again)
     */
    public void setCustom(LegendEntry[] entries) {

        mEntries = entries;
        mIsLegendCustom = true;
    }

    /**
     * Sets a custom legend's entries array.
     * * A null label will start a group.
     * This will disable the feature that automatically calculates the legend
     *   entries from the datasets.
     * Call resetCustom() to re-enable automatic calculation (and then
     *   notifyDataSetChanged() is needed to auto-calculate the legend again)
     */
    public void setCustom(List<LegendEntry> entries) {

        mEntries = entries.toArray(new LegendEntry[entries.size()]);
        mIsLegendCustom = true;
    }

    /**
     * Calling this will disable the custom legend entries (set by
     * setCustom(...)). Instead, the entries will again be calculated
     * automatically (after notifyDataSetChanged() is called).
     */
    public void resetCustom() {
        mIsLegendCustom = false;
    }

    /**
     * @return true if a custom legend entries has been set default
     * false (automatic legend)
     */
    public boolean isLegendCustom() {
        return mIsLegendCustom;
    }

    /**
     * This property is deprecated - Use `horizontalAlignment`, `verticalAlignment`, `orientation`, `drawInside`,
     * `direction`.
     */
    @Deprecated
    public LegendPosition getPosition() {

        if (mOrientation == LegendOrientation.VERTICAL
                && mHorizontalAlignment == LegendHorizontalAlignment.CENTER
                && mVerticalAlignment == LegendVerticalAlignment.CENTER) {
            return LegendPosition.PIECHART_CENTER;
        } else if (mOrientation == LegendOrientation.HORIZONTAL) {
            if (mVerticalAlignment == LegendVerticalAlignment.TOP)
                return mHorizontalAlignment == LegendHorizontalAlignment.LEFT
                        ? LegendPosition.ABOVE_CHART_LEFT
                        : (mHorizontalAlignment == LegendHorizontalAlignment.RIGHT
                        ? LegendPosition.ABOVE_CHART_RIGHT
                        : LegendPosition.ABOVE_CHART_CENTER);
            else
                return mHorizontalAlignment == LegendHorizontalAlignment.LEFT
                        ? LegendPosition.BELOW_CHART_LEFT
                        : (mHorizontalAlignment == LegendHorizontalAlignment.RIGHT
                        ? LegendPosition.BELOW_CHART_RIGHT
                        : LegendPosition.BELOW_CHART_CENTER);
        } else {
            if (mHorizontalAlignment == LegendHorizontalAlignment.LEFT)
                return mVerticalAlignment == LegendVerticalAlignment.TOP && mDrawInside
                        ? LegendPosition.LEFT_OF_CHART_INSIDE
                        : (mVerticalAlignment == LegendVerticalAlignment.CENTER
                        ? LegendPosition.LEFT_OF_CHART_CENTER
                        : LegendPosition.LEFT_OF_CHART);
            else
                return mVerticalAlignment == LegendVerticalAlignment.TOP && mDrawInside
                        ? LegendPosition.RIGHT_OF_CHART_INSIDE
                        : (mVerticalAlignment == LegendVerticalAlignment.CENTER
                        ? LegendPosition.RIGHT_OF_CHART_CENTER
                        : LegendPosition.RIGHT_OF_CHART);
        }
    }

    /**
     * This property is deprecated - Use `horizontalAlignment`, `verticalAlignment`, `orientation`, `drawInside`,
     * `direction`.
     */
    @Deprecated
    public void setPosition(LegendPosition newValue) {

        switch (newValue) {
            case LEFT_OF_CHART:
            case LEFT_OF_CHART_INSIDE:
            case LEFT_OF_CHART_CENTER:
                mHorizontalAlignment = LegendHorizontalAlignment.LEFT;
                mVerticalAlignment = newValue == LegendPosition.LEFT_OF_CHART_CENTER
                        ? LegendVerticalAlignment.CENTER
                        : LegendVerticalAlignment.TOP;
                mOrientation = LegendOrientation.VERTICAL;
                break;

            case RIGHT_OF_CHART:
            case RIGHT_OF_CHART_INSIDE:
            case RIGHT_OF_CHART_CENTER:
                mHorizontalAlignment = LegendHorizontalAlignment.RIGHT;
                mVerticalAlignment = newValue == LegendPosition.RIGHT_OF_CHART_CENTER
                        ? LegendVerticalAlignment.CENTER
                        : LegendVerticalAlignment.TOP;
                mOrientation = LegendOrientation.VERTICAL;
                break;

            case ABOVE_CHART_LEFT:
            case ABOVE_CHART_CENTER:
            case ABOVE_CHART_RIGHT:
                mHorizontalAlignment = newValue == LegendPosition.ABOVE_CHART_LEFT
                        ? LegendHorizontalAlignment.LEFT
                        : (newValue == LegendPosition.ABOVE_CHART_RIGHT
                        ? LegendHorizontalAlignment.RIGHT
                        : LegendHorizontalAlignment.CENTER);
                mVerticalAlignment = LegendVerticalAlignment.TOP;
                mOrientation = LegendOrientation.HORIZONTAL;
                break;

            case BELOW_CHART_LEFT:
            case BELOW_CHART_CENTER:
            case BELOW_CHART_RIGHT:
                mHorizontalAlignment = newValue == LegendPosition.BELOW_CHART_LEFT
                        ? LegendHorizontalAlignment.LEFT
                        : (newValue == LegendPosition.BELOW_CHART_RIGHT
                        ? LegendHorizontalAlignment.RIGHT
                        : LegendHorizontalAlignment.CENTER);
                mVerticalAlignment = LegendVerticalAlignment.BOTTOM;
                mOrientation = LegendOrientation.HORIZONTAL;
                break;

            case PIECHART_CENTER:
                mHorizontalAlignment = LegendHorizontalAlignment.CENTER;
                mVerticalAlignment = LegendVerticalAlignment.CENTER;
                mOrientation = LegendOrientation.VERTICAL;
                break;
        }

        mDrawInside = newValue == LegendPosition.LEFT_OF_CHART_INSIDE
                || newValue == LegendPosition.RIGHT_OF_CHART_INSIDE;
    }

    /**
     * returns the horizontal alignment of the legend
     *
     * @return
     */
    public LegendHorizontalAlignment getHorizontalAlignment() {
        return mHorizontalAlignment;
    }

    /**
     * sets the horizontal alignment of the legend
     *
     * @param value
     */
    public void setHorizontalAlignment(LegendHorizontalAlignment value) {
        mHorizontalAlignment = value;
    }

    /**
     * returns the vertical alignment of the legend
     *
     * @return
     */
    public LegendVerticalAlignment getVerticalAlignment() {
        return mVerticalAlignment;
    }

    /**
     * sets the vertical alignment of the legend
     *
     * @param value
     */
    public void setVerticalAlignment(LegendVerticalAlignment value) {
        mVerticalAlignment = value;
    }

    /**
     * returns the orientation of the legend
     *
     * @return
     */
    public LegendOrientation getOrientation() {
        return mOrientation;
    }

    /**
     * sets the orientation of the legend
     *
     * @param value
     */
    public void setOrientation(LegendOrientation value) {
        mOrientation = value;
    }

    /**
     * returns whether the legend will draw inside the chart or outside
     *
     * @return
     */
    public boolean isDrawInsideEnabled() {
        return mDrawInside;
    }

    /**
     * sets whether the legend will draw inside the chart or outside
     *
     * @param value
     */
    public void setDrawInside(boolean value) {
        mDrawInside = value;
    }

    /**
     * returns the text direction of the legend
     *
     * @return
     */
    public LegendDirection getDirection() {
        return mDirection;
    }

    /**
     * sets the text direction of the legend
     *
     * @param pos
     */
    public void setDirection(LegendDirection pos) {
        mDirection = pos;
    }

    /**
     * returns the current form/shape that is set for the legend
     *
     * @return
     */
    public LegendForm getForm() {
        return mShape;
    }

    /**
     * sets the form/shape of the legend forms
     *
     * @param shape
     */
    public void setForm(LegendForm shape) {
        mShape = shape;
    }

    /**
     * sets the size in dp of the legend forms, default 8f
     *
     * @param size
     */
    public void setFormSize(float size) {
        mFormSize = size;
    }

    /**
     * returns the size in dp of the legend forms
     *
     * @return
     */
    public float getFormSize() {
        return mFormSize;
    }

    /**
     * sets the line width in dp for forms that consist of lines, default 3f
     *
     * @param size
     */
    public void setFormLineWidth(float size) {
        mFormLineWidth = size;
    }

    /**
     * returns the line width in dp for drawing forms that consist of lines
     *
     * @return
     */
    public float getFormLineWidth() {
        return mFormLineWidth;
    }

    /**
     * Sets the line dash path effect used for shapes that consist of lines.
     *
     * @param dashPathEffect
     */
    public void setFormLineDashEffect(DashPathEffect dashPathEffect) {
        mFormLineDashEffect = dashPathEffect;
    }

    /**
     * @return The line dash path effect used for shapes that consist of lines.
     */
    public DashPathEffect getFormLineDashEffect() {
        return mFormLineDashEffect;
    }

    /**
     * returns the space between the legend entries on a horizontal axis in
     * pixels
     *
     * @return
     */
    public float getXEntrySpace() {
        return mXEntrySpace;
    }

    /**
     * sets the space between the legend entries on a horizontal axis in pixels,
     * converts to dp internally
     *
     * @param space
     */
    public void setXEntrySpace(float space) {
        mXEntrySpace = space;
    }

    /**
     * returns the space between the legend entries on a vertical axis in pixels
     *
     * @return
     */
    public float getYEntrySpace() {
        return mYEntrySpace;
    }

    /**
     * sets the space between the legend entries on a vertical axis in pixels,
     * converts to dp internally
     *
     * @param space
     */
    public void setYEntrySpace(float space) {
        mYEntrySpace = space;
    }

    /**
     * returns the space between the form and the actual label/text
     *
     * @return
     */
    public float getFormToTextSpace() {
        return mFormToTextSpace;
    }

    /**
     * sets the space between the form and the actual label/text, converts to dp
     * internally
     *
     * @param space
     */
    public void setFormToTextSpace(float space) {
        this.mFormToTextSpace = space;
    }

    /**
     * returns the space that is left out between stacked forms (with no label)
     *
     * @return
     */
    public float getStackSpace() {
        return mStackSpace;
    }

    /**
     * sets the space that is left out between stacked forms (with no label)
     *
     * @param space
     */
    public void setStackSpace(float space) {
        mStackSpace = space;
    }

    /**
     * the total width of the legend (needed width space)
     */
    public float mNeededWidth = 0f;

    /**
     * the total height of the legend (needed height space)
     */
    public float mNeededHeight = 0f;

    public float mTextHeightMax = 0f;

    public float mTextWidthMax = 0f;

    /**
     * flag that indicates if word wrapping is enabled
     */
    private boolean mWordWrapEnabled = false;

    /**
     * Should the legend word wrap? / this is currently supported only for:
     * BelowChartLeft, BelowChartRight, BelowChartCenter. / note that word
     * wrapping a legend takes a toll on performance. / you may want to set
     * maxSizePercent when word wrapping, to set the point where the text wraps.
     * / default: false
     *
     * @param enabled
     */
    public void setWordWrapEnabled(boolean enabled) {
        mWordWrapEnabled = enabled;
    }

    /**
     * If this is set, then word wrapping the legend is enabled. This means the
     * legend will not be cut off if too long.
     *
     * @return
     */
    public boolean isWordWrapEnabled() {
        return mWordWrapEnabled;
    }

    /**
     * The maximum relative size out of the whole chart view. / If the legend is
     * to the right/left of the chart, then this affects the width of the
     * legend. / If the legend is to the top/bottom of the chart, then this
     * affects the height of the legend. / If the legend is the center of the
     * piechart, then this defines the size of the rectangular bounds out of the
     * size of the "hole". / default: 0.95f (95%)
     *
     * @return
     */
    public float getMaxSizePercent() {
        return mMaxSizePercent;
    }

    /**
     * The maximum relative size out of the whole chart view. / If
     * the legend is to the right/left of the chart, then this affects the width
     * of the legend. / If the legend is to the top/bottom of the chart, then
     * this affects the height of the legend. / default: 0.95f (95%)
     *
     * @param maxSize
     */
    public void setMaxSizePercent(float maxSize) {
        mMaxSizePercent = maxSize;
    }

    private List<FSize> mCalculatedLabelSizes = new ArrayList<>(16);
    private List<Boolean> mCalculatedLabelBreakPoints = new ArrayList<>(16);
    private List<FSize> mCalculatedLineSizes = new ArrayList<>(16);

    public List<FSize> getCalculatedLabelSizes() {
        return mCalculatedLabelSizes;
    }

    public List<Boolean> getCalculatedLabelBreakPoints() {
        return mCalculatedLabelBreakPoints;
    }

    public List<FSize> getCalculatedLineSizes() {
        return mCalculatedLineSizes;
    }

    /**
     * Calculates the dimensions of the Legend. This includes the maximum width
     * and height of a single entry, as well as the total width and height of
     * the Legend.
     *
     * @param labelpaint
     */
    public void calculateDimensions(Paint labelpaint, ViewPortHandler viewPortHandler) {

        float defaultFormSize = Utils.convertDpToPixel(mFormSize);
        float stackSpace = Utils.convertDpToPixel(mStackSpace);
        float formToTextSpace = Utils.convertDpToPixel(mFormToTextSpace);
        float xEntrySpace = Utils.convertDpToPixel(mXEntrySpace);
        float yEntrySpace = Utils.convertDpToPixel(mYEntrySpace);
        boolean wordWrapEnabled = mWordWrapEnabled;
        LegendEntry[] entries = mEntries;
        int entryCount = entries.length;

        mTextWidthMax = getMaximumEntryWidth(labelpaint);
        mTextHeightMax = getMaximumEntryHeight(labelpaint);

        switch (mOrientation) {
            case VERTICAL: {

                float maxWidth = 0f, maxHeight = 0f, width = 0f;
                float labelLineHeight = Utils.getLineHeight(labelpaint);
                boolean wasStacked = false;

                for (int i = 0; i < entryCount; i++) {

                    LegendEntry e = entries[i];
                    boolean drawingForm = e.form != LegendForm.NONE;
                    float formSize = Float.isNaN(e.formSize)
                            ? defaultFormSize
                            : Utils.convertDpToPixel(e.formSize);
                    String label = e.label;

                    if (!wasStacked)
                        width = 0.f;

                    if (drawingForm) {
                        if (wasStacked)
                            width += stackSpace;
                        width += formSize;
                    }

                    // grouped forms have null labels
                    if (label != null) {

                        // make a step to the left
                        if (drawingForm && !wasStacked)
                            width += formToTextSpace;
                        else if (wasStacked) {
                            maxWidth = Math.max(maxWidth, width);
                            maxHeight += labelLineHeight + yEntrySpace;
                            width = 0.f;
                            wasStacked = false;
                        }

                        width += Utils.calcTextWidth(labelpaint, label);

                        if (i < entryCount - 1)
                            maxHeight += labelLineHeight + yEntrySpace;
                    } else {
                        wasStacked = true;
                        width += formSize;
                        if (i < entryCount - 1)
                            width += stackSpace;
                    }

                    maxWidth = Math.max(maxWidth, width);
                }

                mNeededWidth = maxWidth;
                mNeededHeight = maxHeight;

                break;
            }
            case HORIZONTAL: {

                float labelLineHeight = Utils.getLineHeight(labelpaint);
                float labelLineSpacing = Utils.getLineSpacing(labelpaint) + yEntrySpace;
                float contentWidth = viewPortHandler.contentWidth() * mMaxSizePercent;

                // Start calculating layout
                float maxLineWidth = 0.f;
                float currentLineWidth = 0.f;
                float requiredWidth = 0.f;
                int stackedStartIndex = -1;

                mCalculatedLabelBreakPoints.clear();
                mCalculatedLabelSizes.clear();
                mCalculatedLineSizes.clear();

                for (int i = 0; i < entryCount; i++) {

                    LegendEntry e = entries[i];
                 "  b/”le	 dèawi§Foq =a.fs%m Ç¼ Lc¿enİVor­ÌNOî;
ge  (j  r´       Moa+Åfo-÷SiØ =‡äloQJ.iaNÊ .fWmS²®e)Ş  ’  w  úí  ‚  š  E  ™á d©±auò›Fo!NSif°
 ÆR  &  Ô«  #   fo  ıd   :‘dtiR.cˆ&ve¸DpÆPi‡úl(»Mfol×SiŸŒ);¦’  Ôë  ×  ¥  =l  ¶;trìúg ÁPbeZ·= 
lay!l;òC  u®  Ò  1ø    n~mC]ûcuÂÇte&'ab¸"Br;kP7ÅntF%adUTfaÕe)Œ!
 Oí  ØÚ  r¿  Xƒ  š„ iXÓ(sr\ck.ÌStŠËtIÎcex= 1) (  çé  QÚ  %Ÿ  “  ±÷  ´f//1e òÕe hõt æCacw7ng:
so	½eq¡*re[§wi”Fh `x fñ˜ t s ébeı¹  ¡¼  ¬û  U'  Ú^  ˆ  É//8¹nl#  ?’$ L¥ ¨“ ¤¦Q  ”(  	°eq-Hreç_idz =#3.f%È  ”ì  >Ô  VW  Ì&  îæ} ersekó
 ´  "  ó  M©  Å^  ÷w /ØnadäÔthJ¿spïin apWŠop®iatŞkfo’†stwákeíÚlaŠlsxorã á2  ±@  ›¨  Nô  °m  R| r—ui«®dW &thoã= dacşpad;
R  &Ÿ  ‰  Ï  úí  M;
 ×Ç  Œ~  ’Á  ëÕ  ×– /grm~pe;Ñfoúßs Pàve·éul
àla!šlsC‚  ®+  ê  ø)  q  ~Ğf ûabÇ( !'nu"÷) ;¬
 ÅW  %™  T›  È  !  í£ mÚêlc¿)atƒĞLa„ûlSÓ%es\qddÌtiË\.cc}cTYtS1ƒe((beéáaiÚT, Ÿ¥be);÷«  f‰  1  Õ²  õP  Cä  7!eq
 re½.id*O +§ddrFinxÅor˜«? 7rm–Te¹	Sp¼¹e û~fo'ÓSi^8 2ˆü,fÙ]$(¹ï   !’ä  ¥  “Í ¦Q v+”vp	dWh,th+= }al+slas%dLç”elc¯zeİVge¡Èi)îidee;
(c  p´       -  +Åel-÷ {Ø  ‡ä  QJ    Ê   W  ²®mCŞcu’tewabúíSi‚s.šd(Eiz™áge©Inúòanª!(0¨f, WÆf)Í&
 õô   #  #f  íü  :  Ô‘ r…ui\ˆdW<¸thîÆ= ş‡awH»gFälm ·Ÿfo¦SiÔ :*.f]¥
 q=  ¶  ì  =Á  â[  ° iy(sZòckáuStiÒtIk1exl= mn) ]  ÜÂ  m&  ¸  x  Æ7  $F  zU//6ÕarÖŒth]O iiØexzs ,X mšht)XancrtoÈ.re Š hbÎe áte³  i  ç  NQ  &%  h“  j±  $´st’keüòtahhInWæx Iwi;‹:  	  b¡  ¯[  †–  4`  ñ
 ”   Oé  Íı  ‘¡  ®¬ }ÛU  aÚ    Ö  8  t#iæö?ì`‚Ll  n®¦a w+ºğ- gè4xOu
y -º?)$»S
 (Æ  Kc  "Ø  U   y  g f(at\reqxre‚pae ngƒ+ cê-re·Li´‡WiÙQh a 0Ê ?ß.Š: m ntkoSp†(e;á]  Ú'  Š  x/  ãú  2•  @úif¨ª!wô¨dWmWpE|Íbl—õ /® No&#orãíwr:pişÔ, … m\t ß<t.‰î  Ïş  í  »ä  Ç·  ~  Á  Ñ*  ]/ qe ~neÑs ß=ptàâ ié°muà fšZ
 ‚á  +i  êk  )l  qm  P  Ü  (  ' c"÷re;¬LiÅWWi%™h T› 0È
 !  í£  Úê  ¿)  ƒĞ  „û  Ó%  \q /ÌItË\imc}y Yts1ƒ  (  éá  ÚT  Ÿ¥    ÷«  f‰  1| Õ²onõPntCädt7!-  rr½.tL*OeW§dthF=
xÁ  ˜«  7  –  ¹	  ¼¹  û~  'Ó  ^8quˆüedÉ]as¹ïg re’äir¥Wi“Í.-Q,{+”ºà	cè-X £;€y  -#  c%  Å„/ Aüpaİf cõÌren lgme
(j  r´       M  +Å  -÷  Ørr‡ätLQJeWthÊ = Wqu²®eeŞac‚g wreúíir‚Wišh;E(  ™á  ©±  ò›  !N  f°  ÆR e&e Ô«//# t foesüdt t,‘de Redˆ&o ¸apÆ l‡úe
»M  l×  ŸŒ  ¦’  Ôë  ×  ¥  =l/ ¶;d ìúrrÁPt [·ne
izy!toòCrru®
 ÒŒ  1ø    n~  ]û  ÂÇ  &' m¸"lc;at7ÅLiF%SiUTs.Õd(Œ!izOígeØÚnsz¿ncXƒcuš„enXÓinr\id.Ì, ŠËbeÎLiáHe³hti;
ç  NQ  &%  h“  j±  $´  ’  üòxLhheWWæthIw M‹:h.	x(b¡xL¯[eW†–th4`cuñen” inOéidÏı);‘£  ®¬  ÛU  aÚ  œ  Ö  8  t#//ö?t`‚L a>åwÿÿÇŸÿÿKÿÿÿöÏÿÿó¿ÿÿïÿÿßÁÿÿÿüÿÿúÿ›Pÿÿ#^ÿ_İÅæúR;ûÿSc¿ÿñı¿ÿúxÿÿ8‡ÿÿóıÿşÉ,ÿÿ’ÿÿô¿ÿò(ÿÿİÿÿX;ÿÿ•ÿÿÚÌÿÿßÿÿ˜Ëÿÿ)ÿÿ†Uÿÿ„ÿÿÖ¨ÿÿ%-ÿÿ	ÿÿ äÿÿÛ…ÿÿzÿÿ\nÿ-Dÿÿş‘ÿÿ¹oÿÿÿÿùÆÿÿtÿÿüßÿÿ¹°ÿş#»ÿÿÎÆÿÿN¡ÿÿÑÿÿ¼ÿÿMPÿÿÈÿÿX%ÿÿd’ÿÿ³ÿÿ@SÿÿyMÿÿwÿÿ“Mÿÿ›ÿÿ•úÿÿ!äÿÿa`ÿÿWİÿÿó!ÿÿˆÎÿÿ¦şÿÿ-œÿÿªqÿÿRÿÿî'ÿÿĞ ÿÿ›‹ÿÿ‚$ÿÿÿÿùøÿÿgıÿÿÄäÿÿèÿÿ™òÿÿ—–ÿÿD—ÿÿ0±ÿÿÄ2ÿÿÿÿ`ÿÿ‡\ÿÿE[ÿÿ‡ÿÿ­ƒÿÿñÿUÿÿ³ÿÿ¿“ÿÿŒ¯ÿÿÊ»ÿÿ}6ÿÿÔ@ÿÿµ@ÿÿ“ÿÿn(ÿÿk¹ÿÿRîÿÿ-
ÿÿ·*ÿÿ9œÿÿ¨èÿÿ¨¶ÿÿ•7ÿÿ*°ÿÿÍ6ÿÿ Ãÿÿ±æÿÿ&!ÿÿ¥‹ÿÿs–ÿÿq2ÿÿ=&ÿÿ&vÿÿÄ¬ÿÿKÄÿÿŠÿÿÀÿÿ ¢ÿÿâ²ÿÿl©ÿÿ	šÿÿèÁÿÿñÿÿóøÿÿçoÿÿÿÿÿ÷ÿÿÿå}ÿÿûıÿÿııÿÿÿÇÿÿgØı€÷ßÈŒ;^!®<ıi£ıüÈ±ÿÿxËÿïòÿÿ¸ÿÿeôÿÿ?ûÿˆ.ÿ¨hß÷±kßÿ¿ÿ`¾ÿÿ0^ÿÿÍ ÿÿĞÿ²×ßá´°ÿÿY÷ÿÿ>‚ıÿ¥ÿUÚÿÿ§ğÿÿ<%ÿÿíJÿÿŸ%ÿÿ$!ÿÿ`2ÿÿæÉÿÿÊ\ÿÿ*ÿÿ=ÿÿµüÿÿ<|ÿÿåÿÿ,ÿÿÄ	ÿÿ†Èÿÿ3·ÿÿXÿÿUyÿÿ2—ÿÿd;ÿÿhÿÿ¡ÅÿÿCîÿÿ
õÿÿI‚ÿÿÎ®ÿÿ¡Aÿÿ`‘ÿÿE¦ÿÿ{iÿÿ+oÿ”gÿÿ° ÿÿØ	ÿÿ[ôÿÿR5ÿÿ'öÿÿ ®ÿÿ¡Bÿÿašÿÿp²ÿÿ°’ÿÿıdÿ†¡ÿÿ¡TÿÿÜ³ÿÿŞ¡ÿÿ
ÃÿÿSşÿÿ2ÿÿÿÿ¥`ÿÿ‡\ÿÿMÿÿÆXÿÿ¥Îÿÿ½Pÿÿ@ÿÿ}õÿÿ­ÿÿ“àÿÿ²ÿÿ1ÿÿÿÿú@ÿÿLÒÿın(ÿÿk¹ÿÿRîÿÿaKÿÿûFÿÿ|ïÿÿë¡ÿÿåßÿÿÖbÿÿ~õÿÿ„xÿÿI™ÿÿ¿§ÿÿ.Gÿÿ¹‚ÿÿbËÿÿoÿÿ=6ÿÿ'aÿÿÃÿÿÍLÿÿÀÿÿSÿÿ‹Bÿÿ0»ÿÿÇ)ÿÿ;]ÿÿPBßÿÿÿÿÿöKÿÿşöÿÿwòÿÿÜsÿÿşòÿÿÿÿÿÿıúÿÿ½ïÿÿ¼`ÿ‰¿ß~ÿA1ÿß¸ ÿßº×ïÏî>Îö•œ¿ÿ‡‡ÿÿ†ÈÿßúIÿï· ÿÿĞïçQ ÿÿ•ÿÿÚÌÿÿßÿÿÙˆÿı/#ÿÿŸÿÿÀMÿÿÈ¨ÿÿ%2ÿÿwQÿÿE ÿÿšŠÿÿF>ÿÿvnÿÿ`*ÿÿºÔÿÿü&ÿÿMÿÿù‹ÿÿM2ÿÿ¨Äÿÿ¹°ÿÿ#»ÿÿ ƒÿÿÿÿÿ™Ùÿÿ\äÿÿ?ÿÿJÿşr%ÿÿdÏÿÿ™
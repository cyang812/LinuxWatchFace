package tech.cyang.linuxwatchface;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.format.Time;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class SimpleWatchFace {

    private static final String TIME_FORMAT_WITHOUT_SECONDS = "[TIME] GMT+8 "+"%02d:%02d";
    private static final String TIME_FORMAT_WITH_SECONDS = TIME_FORMAT_WITHOUT_SECONDS + ":%02d";
    private static final String DATE_FORMAT = "[DATE] "+"%04d/%02d/%02d";
    private static final String BATTERY_FORMAT = "[BATT] "+"[#########.]#%d %%";
    private static final String STEP_FORMAT = "[STEP] "+"%d steps";
    private static final String HEARTRATE_FORMAT = "[L_HR] "+"%d bpm";
    private static final String TITLE_1_FORMAT = "cyang@watch: ~ $ now";
    private static final String TITLE_2_FORMAT = "cyang@watch: ~ $ ";

    private static final int TIME_DEFAULT_COLOUR = Color.rgb(56,142,60);
    private static final int DATE_DEFAULT_COLOUR = Color.rgb(25,118,210);
    private static final int BATTERY_DEFAULT_COLOUR = Color.rgb(245,124,0);
    private static final int STEP_DEFAULT_COLOUR = Color.rgb(120,144,156);
    private static final int HEARTRATE_DEFAULT_COLOUR = Color.rgb(255,61,0);
    private static final int BACKGROUND_DEFAULT_COLOUR = Color.rgb(33,33,33);
    private static final int TITLE_DEFAULT_COLOUR = Color.rgb(245,245,245);

    private static final int AMBIENT_ITEMS_DEFAULT_COLOUR = Color.WHITE;
    private static final int AMBIENT_BACKGROUND_DEFAULT_COLOUR = Color.BLACK;

    private final Paint timePaint;
    private final Paint datePaint;
    private final Paint batteryPaint;
    private final Paint stepPaint;
    private final Paint heartRatePaint;
    private final Paint backgroundPaint;
    private final Paint titlePaint;


    private final Time time;

    private boolean shouldShowSeconds = true;
    private int timeColour = TIME_DEFAULT_COLOUR;
    private int dateColour = DATE_DEFAULT_COLOUR;
    private int batteryColour = BATTERY_DEFAULT_COLOUR;
    private int stepColour = STEP_DEFAULT_COLOUR;
    private int heartRateColour = HEARTRATE_DEFAULT_COLOUR;
    private int backgroundColour = BACKGROUND_DEFAULT_COLOUR;
    private int ambientItemsColour = AMBIENT_ITEMS_DEFAULT_COLOUR;
    private int ambientBackgroundColour = AMBIENT_BACKGROUND_DEFAULT_COLOUR;
    private int titleColour = TITLE_DEFAULT_COLOUR;


    public static SimpleWatchFace newInstance(Context context) {
        Paint timePaint = new Paint();
        timePaint.setColor(TIME_DEFAULT_COLOUR);
        timePaint.setTextSize(context.getResources().getDimension(R.dimen.font_size));
        timePaint.setAntiAlias(false);

        Paint datePaint = new Paint();
        datePaint.setColor(DATE_DEFAULT_COLOUR);
        datePaint.setTextSize(context.getResources().getDimension(R.dimen.font_size));
        datePaint.setAntiAlias(false);

        Paint batteryPaint = new Paint();
        batteryPaint.setColor(BATTERY_DEFAULT_COLOUR);
        batteryPaint.setTextSize(context.getResources().getDimension(R.dimen.font_size));
        batteryPaint.setAntiAlias(false);

        Paint stepPaint = new Paint();
        stepPaint.setColor(STEP_DEFAULT_COLOUR);
        stepPaint.setTextSize(context.getResources().getDimension(R.dimen.font_size));
        stepPaint.setAntiAlias(false);

        Paint hearRatePaint = new Paint();
        hearRatePaint.setColor(HEARTRATE_DEFAULT_COLOUR);
        hearRatePaint.setTextSize(context.getResources().getDimension(R.dimen.font_size));
        hearRatePaint.setAntiAlias(false);

        Paint titlePaint = new Paint();
        titlePaint.setColor(TITLE_DEFAULT_COLOUR);
        titlePaint.setTextSize(context.getResources().getDimension(R.dimen.font_size));
        titlePaint.setAntiAlias(false);

        Paint backgroundPaint = new Paint();
        backgroundPaint.setColor(BACKGROUND_DEFAULT_COLOUR);

        return new SimpleWatchFace(timePaint, datePaint, batteryPaint, stepPaint, hearRatePaint, titlePaint, backgroundPaint, new Time());
    }

    SimpleWatchFace(Paint timePaint, Paint datePaint, Paint batteryPaint, Paint stepPaint, Paint heartRatePaint, Paint titlePaint, Paint backgroundPaint, Time time) {
        this.timePaint = timePaint;
        this.datePaint = datePaint;
        this.batteryPaint = batteryPaint;
        this.stepPaint = stepPaint;
        this.heartRatePaint = heartRatePaint;
        this.titlePaint = titlePaint;
        this.backgroundPaint = backgroundPaint;
        this.time = time;
    }

    public void draw(Canvas canvas, Rect bounds) {
        int startX = 30;
        int startY = 100;
        int offsetY = 30;

        time.setToNow();
        canvas.drawRect(0, 0, bounds.width(), bounds.height(), backgroundPaint);

        String titleText_1 = String.format(TITLE_1_FORMAT);
        canvas.drawText(titleText_1, startX, startY+offsetY*0, titlePaint);

        String timeText = String.format(shouldShowSeconds ? TIME_FORMAT_WITH_SECONDS : TIME_FORMAT_WITHOUT_SECONDS, time.hour, time.minute, time.second);
        canvas.drawText(timeText, startX, startY+offsetY*1, timePaint);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd yyyy", Locale.ENGLISH);
        LocalDate date = LocalDate.of(time.year, time.month + 1, time.monthDay);
        String dateText = "[DATE] " + date.format(formatter);
        //String dateText = String.format(DATE_FORMAT, time.year, (time.month + 1), time.monthDay);
        canvas.drawText(dateText, startX, startY+offsetY*2, datePaint);

        String batteryText = String.format(BATTERY_FORMAT, 89);
        canvas.drawText(batteryText, startX, startY+offsetY*3, batteryPaint);

        String stepText = String.format(STEP_FORMAT, 621);
        canvas.drawText(stepText, startX, startY+offsetY*4, stepPaint);

        String heartRateText = String.format(HEARTRATE_FORMAT, 96);
        canvas.drawText(heartRateText, startX, startY+offsetY*5, heartRatePaint);

        String titleText_2 = String.format(TITLE_2_FORMAT);
        canvas.drawText(titleText_2, startX, startY+offsetY*6, titlePaint);
    }

//    public void updateTimeColourTo(int colour) {
//        timeColour = colour;
//        timePaint.setColor(colour);
//    }
//
//    public void updateDateColourTo(int colour) {
//        dateColour = colour;
//        datePaint.setColor(colour);
//    }
//
//    public void updateBatteryColourTo(int colour) {
//        batteryColour = colour;
//        batteryPaint.setColor(colour);
//    }
//
//    public void updateStepColourTo(int colour) {
//        stepColour = colour;
//        stepPaint.setColor(colour);
//    }
//
//    public void updateHeartRateColourTo(int colour) {
//        heartRateColour = colour;
//        heartRatePaint.setColor(colour);
//    }
//
//    public void updateTitleColourTo(int colour) {
//        titleColour = colour;
//        titlePaint.setColor(colour);
//    }

    public void updateTimeZoneWith(String timeZone) {
        time.clear(timeZone);
        time.setToNow();
    }

    public void setShowSeconds(boolean showSeconds) {
        shouldShowSeconds = showSeconds;
    }

    public void updateBackgroundColourToDefault() {
        backgroundPaint.setColor(AMBIENT_BACKGROUND_DEFAULT_COLOUR);
    }

    public void restoreBackgroundColour() {
        backgroundPaint.setColor(backgroundColour);
    }

    public void updateAllItemsColourToDefault() {
        timePaint.setColor(AMBIENT_ITEMS_DEFAULT_COLOUR);
        datePaint.setColor(AMBIENT_ITEMS_DEFAULT_COLOUR);
        batteryPaint.setColor(AMBIENT_ITEMS_DEFAULT_COLOUR);
        stepPaint.setColor(AMBIENT_ITEMS_DEFAULT_COLOUR);
        heartRatePaint.setColor(AMBIENT_ITEMS_DEFAULT_COLOUR);
        titlePaint.setColor(AMBIENT_ITEMS_DEFAULT_COLOUR);
    }

    public void restoreAllItemsColour() {
        timePaint.setColor(timeColour);
        datePaint.setColor(dateColour);
        batteryPaint.setColor(batteryColour);
        stepPaint.setColor(stepColour);
        heartRatePaint.setColor(heartRateColour);
        titlePaint.setColor(titleColour);
    }
}

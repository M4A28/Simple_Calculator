package com.mohammed.calone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView display;
    TextView resultsTV;
    DecimalFormat format = new DecimalFormat("0.########");
    public static final String MESSAGE_CONSTANT = "com.mohammed.calone.notification";
    public static final String MY_TWITTER = "https://twitter.com/M4A28";
    public static final int NOTE_ID = 999;
    public static final int REQ_PRE = 777;
    boolean leftBracket = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(isDayTime())
            setContentView(R.layout.day_mode);
        else
            setContentView(R.layout.activity_main);
        if(isFriday()) setContentView(R.layout.friday);

        initTextViews();

    }

    public static boolean isDayTime(){
        Calendar calendar = Calendar.getInstance();
        int hour  = calendar.get(Calendar.HOUR_OF_DAY);
        return (hour >= 5 && hour <= 17);
    }

    private void initTextViews() {
        display = findViewById(R.id.workingsTextView);
        resultsTV = findViewById(R.id.resultTextView);
        display.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    Double answer = calculate(display.getText().toString());
                    if(resultsTV.getText().toString().length() > 12){
                        resultsTV.setTextSize(35);
                    }
                    else
                        resultsTV.setTextSize(40);
                    resultsTV.setText(format.format(answer));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        getPremission();
        if(isFriday()) {
            simpleNotification();
        }
    }

    public void clearOnClick(View view) {
        display.setText(null);
        resultsTV.setText(null);
        leftBracket = true;
    }

    public void bracketsOnClick(View view) {
        if(leftBracket) {
            autoAddOrRemove("symbol");
            display.setText(display.getText().toString() + "(");
            leftBracket = false;
        }
        else {
            display.setText(display.getText().toString() + ")");
            leftBracket = true;
        }
    }

    public void removeLastCharOnClick(View view){
        if (!display.getText().toString().isEmpty()) {
            display.setText(display
                    .getText()
                    .toString()
                    .substring(0, display.getText().toString().length()-1));
        }
        resultsTV.setText("");
    }

    public void equalsOnClick(View view){
        if (!display.getText().toString().isEmpty()) {
            try {
                makeCall();
                Double answer = calculate(display.getText().toString());
                if (answer.isInfinite()) {
                    resultsTV.setText(R.string.dvidy_by_0);
                } else if (answer.isNaN()) {
                    resultsTV.setText(R.string.error);
                } else {
                    if(display.getText().toString().length() > 75){
                        Toast.makeText(this,
                                R.string.max_len,
                                Toast.LENGTH_LONG)
                                .show();
                    }
                    else {
                        display.setText(format.format(answer));
                        resultsTV.setText("");
                    }
                }
            } catch (Exception ex) {
                resultsTV.setText(R.string.error);
            }
        }
    }

    private void forFun(String answer){
        switch (answer){
            case "7.0":
                takeScreenshot();
                break;
            case "5.0":
//                playSound();
                break;
            case "3.0":
                finish();
                break;
            case "1.0":
                Toast.makeText(this,
                        "OoooooooooOooooOK",
                        Toast.LENGTH_SHORT).show(); break;
            default: resultsTV.setText(format.format(answer));
        }
    }

    public void mouduleOnClick(View view){
        if (!display.getText().toString().isEmpty()) {
            autoAddOrRemove("operand");
            display.setText(display.getText().toString() + "%");
        }
    }

    public void divisionOnClick(View view) {
        autoAddOrRemove("operand");
        display.setText(display.getText() + "÷");
    }

    public void sevenOnClick(View view) {
        autoAddOrRemove("number");
        display.setText(display.getText() + "7");
    }

    public void eightOnClick(View view) {
        autoAddOrRemove("number");
        display.setText(display.getText() + "8");
    }

    public void nineOnClick(View view) {
        autoAddOrRemove("number");
        display.setText(display.getText() + "9");
    }

    public void timesOnClick(View view) {
        if (!display.getText().toString().isEmpty()) {
            autoAddOrRemove("operand");
            display.setText(display.getText() + "×");
        }
    }

    public void fourOnClick(View view) {
        autoAddOrRemove("number");
        display.setText(display.getText() + "4");
    }

    public void fiveOnClick(View view) {
        autoAddOrRemove("number");
        display.setText(display.getText() + "5");
    }

    public void sixOnClick(View view) {
        autoAddOrRemove("number");
        display.setText(display.getText() + "6");
    }

    public void minusOnClick(View view) {
        if (!display.getText().toString().isEmpty()) {
            autoAddOrRemove("operand");
            display.setText(display.getText().toString() + "-");
        }
    }

    public void oneOnClick(View view) {
        autoAddOrRemove("number");
        display.setText(display.getText() + "1");
    }

    public void twoOnClick(View view) {
        autoAddOrRemove("number");
        display.setText(display.getText() + "2");
    }

    public void threeOnClick(View view) {
        autoAddOrRemove("number");
        display.setText(display.getText() + "3");
    }

    public void plusOnClick(View view) {
        if (!display.getText().toString().isEmpty()) {
            autoAddOrRemove("operand");
            display.setText(display.getText().toString() + "+");
        }
    }

    public void decimalOnClick(View view) {
        String str = display.getText().toString();
        if (display.getText().toString().isEmpty()) {
            display.setText("0.");
        } else {
            int lastPointIndex = str.lastIndexOf(".");
            int lastPlusIndex = str.lastIndexOf("+");
            int lastMinusIndex = str.lastIndexOf("-");
            int lastMultipleIndex = str.lastIndexOf("×");
            int lastDivideIndex = str.lastIndexOf("÷");
            int lastModuloIndex = str.lastIndexOf("%");

            if (lastPointIndex <= lastPlusIndex
                    || lastPointIndex <= lastMinusIndex
                    || lastPointIndex <= lastMultipleIndex
                    || lastPointIndex <= lastDivideIndex
                    || lastPointIndex <= lastModuloIndex) {
                autoAddOrRemove("point");
                display.setText(display.getText().toString() + ".");
            }
        }
    }

    public void zeroOnClick(View view) {
        autoAddOrRemove("number");
        display.setText(display.getText() + "0");
    }

    private void autoAddOrRemove(String button) {
        // to fit display
        if(display.getText().toString().length() > 12){
            display.setTextSize(35);
        }
        else
            display.setTextSize(40);

        if (!display.getText().toString().isEmpty()) {
            Character lastCharacter = display.getText().toString()
                    .charAt(display.getText().toString().length() - 1);

            switch (button) {
                case "symbol":
                    switch (lastCharacter) {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            display.setText(display.getText() + "×");
                            break;
                        case '.':
                            display.setText(display.getText() + "0×");
                            break;
                    }
                    break;

                case "number":
                    switch (lastCharacter) {
                        case '0':
                            switch (display.getText().toString()) {
                                case "0":
                                case "+0":
                                case "-0":
                                case "×0":
                                case "÷0":
                                case "%0":
                                case "(0":

                                    display.setText(display
                                            .getText()
                                            .toString()
                                            .substring(0, display.getText().length() - 1));
                                    break;
                            }
                            break;
                    }
                    break;

                case "operand":
                    switch (lastCharacter) {
                        case '+':
                        case '-':
                        case '×':
                        case '÷':
                        case '%':
                        case '.':
                            display.setText(display
                                    .getText()
                                    .toString()
                                    .substring(0, display.getText().length() - 1));
                            break;
                    }
                    break;

                case "point":
                    switch (lastCharacter) {
                        case '+':
                        case '-':
                        case '×':
                        case '÷':
                        case '%':
                        case '(':
                            display.setText(display.getText() + "0");
                            break;
                        case ')':
                            display.setText(display.getText() + "×0");
                            break;
                        case '.':
                            display.setText(display
                                    .getText()
                                    .toString()
                                    .substring(0, display.getText().length() - 1));
                            break;
                    }
                    break;

            }
        }
    }

    private double calculate(String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') {
                    nextChar();
                }
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if (eat('+')) {
                        x += parseTerm();
                    } else if (eat('-')) {
                        x -= parseTerm();
                    } else {
                        return x;
                    }
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if (eat('×')) {
                        x *= parseFactor();
                    } else if (eat('÷')) {
                        x /= parseFactor();
                    } else if (eat('%')) {
                        x %= parseFactor();
                    } else {
                        return x;
                    }
                }
            }

            double parseFactor() {
                if (eat('+')) {
                    return parseFactor();
                }
                if (eat('-')) {
                    return -parseFactor();
                }
                double x;
                int startPos = this.pos;
                if (eat('(')) {
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') {
                        nextChar();
                    }
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                }
                else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }
                return x;
            }

        }.parse();
    }

    private void getPremission() {
        String[] per = {Manifest.permission.WRITE_EXTERNAL_STORAGE
                ,Manifest.permission.CALL_PHONE};
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                &&ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, per, REQ_PRE);
        }
    }

    private void makeCall(){
        String phone = display.getText().toString();
        if(phone.startsWith("+") && phone.length() >= 11){
            Intent intent =  new Intent();
            Uri uri = Uri.parse("tel:"+phone);
            intent.setAction(Intent.ACTION_CALL);
            intent.setData(uri);
            startActivity(intent);
        }
    }

    private void takeScreenshot() {
        Date now = new Date();

        String r = DateFormat.format("yyyy-MM-dd_hh_mm_ss", now).toString();
        String path = Environment.getExternalStorageDirectory().toString()
                + "/Pictures/Shots/";
        File dir = new File(path);
        if(!dir.exists())
            dir.mkdirs();

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString()
                    + "/" + "Pictures"
                    + "/" + "Shots"
                    + "/" + r + ".jpg";

            // create bitmap screen capture

            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);

            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            shareImage(imageFile);

        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }

    private void shareImage(File imageFile){
        try {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            String ext = imageFile.getName().substring(imageFile.getName().lastIndexOf(".") + 1);
            String type = mime.getMimeTypeFromExtension(ext);
            sharingIntent.setType(type);
            sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(imageFile));
            sharingIntent.putExtra(Intent.EXTRA_TEXT, MY_TWITTER);
            startActivity(Intent.createChooser(sharingIntent, "Share using"));
        }
        catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void shareApp(){
        String twitter = "www.twitter.com/M4A28";
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, twitter);
        startActivity(Intent.createChooser(sharingIntent, "Share using"));
    }

    private void sleep(int sec){
        try {
            Thread.sleep(sec * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static boolean isFriday() {
        Calendar calendar = Calendar.getInstance();
        return Calendar.FRIDAY == calendar.get(Calendar.DAY_OF_WEEK);
    }

    private void simpleNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        intent.setAction(MainActivity.MESSAGE_CONSTANT);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent notificationIntent = PendingIntent.getActivity(this, 999, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.getApplicationContext());
        builder.setContentIntent(notificationIntent)
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.ic_notifications))
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentText(getString(R.string.massage))
                .setTicker(getString(R.string.weekend))
                .setContentTitle(getString(R.string.chik_out))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSound(soundUri);

        // It won't show "Heads Up" unless it plays a sound
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) builder.setVibrate(new long[0]);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTE_ID, builder.build());
    }

//    private  void playSound(){
//        try {
//            AssetFileDescriptor afd = getAssets().openFd("fun1.mp3");
//            MediaPlayer mediaPlayer = new MediaPlayer();
//            mediaPlayer.setDataSource(
//                    afd.getFileDescriptor(),
//                    afd.getStartOffset(),
//                    afd.getLength());
//            mediaPlayer.prepare();
//            mediaPlayer.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}

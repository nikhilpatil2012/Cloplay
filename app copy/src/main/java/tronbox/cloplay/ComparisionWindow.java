/*
    The role of this activity is to show the changed and unchanged image for comparision, he can revert
    back all the changes by selecting the buttons given below the images.
*/


package tronbox.cloplay;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import tronbox.cloplay.R;


public class ComparisionWindow extends Activity {

    ImageView beforeImage ,afterImage;

    Bitmap before, after;

    float imageWidth = 0, imageHeight = 0;

    Point point = new Point();


    Typeface babe, calibiri;

    TextView beforePhoto, afterPhoto, beforeText, afterText;
    Button done;

   // LinearLayout.LayoutParams layoutParamsBefore, layoutParamsAfter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindowManager().getDefaultDisplay().getSize(point);


        setContentView(R.layout.comparision_screen);

        Log.w("Sizeeeeeee", "X = "+point.x+" Y = "+point.y);

        int width = (point.x-50)/2;
        int height = width-100;

        getActionBar().hide();

        babe = Typeface.createFromAsset(getAssets(), "bebes.TTF");
        calibiri = Typeface.createFromAsset(getAssets(), "calibiri.TTF");


        beforePhoto = (TextView)findViewById(R.id.beforePhoto);
        beforePhoto.setTypeface(babe);

        afterPhoto = (TextView)findViewById(R.id.afterPhoto);
        afterPhoto.setTypeface(babe);

        beforeText = (TextView)findViewById(R.id.beforeText);
        beforeText.setTypeface(calibiri);

        afterText = (TextView)findViewById(R.id.afterText);
        afterText.setTypeface(calibiri);

        done = (Button)findViewById(R.id.send);
        done.setTypeface(babe);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendBroadcast(new Intent("Finish_Me"));

                finish();

                Intent intent = new Intent(getApplicationContext(), RegstrationForm.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        imageWidth = 0.45f * point.x;
        imageHeight = imageWidth - (imageWidth/3);




/*
        before = BitmapFactory.decodeResource(getResources(), R.drawable.door13);//Bitmap.createScaledBitmap(Master.beforeImage, (int)imageWidth, (int)imageHeight, false);
        after =  BitmapFactory.decodeResource(getResources(), R.drawable.door13);//Bitmap.createScaledBitmap(Master.afterImage, (int)imageWidth, (int)imageHeight, false);
*/


        before = Bitmap.createScaledBitmap(Master.beforeImage, (int) imageWidth, (int) imageHeight, false);
        after =  Bitmap.createScaledBitmap(Master.afterImage, (int) imageWidth, (int) imageHeight, false);


        /*
           Display before image
        */

        beforeImage = (ImageView)findViewById(R.id.before_image);
        beforeImage.setImageBitmap(before);
        beforeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if((!Master.beforeImage.isRecycled()) && (!Master.afterImage.isRecycled())){

                    Master.beforeImage.recycle();
                    Master.afterImage.recycle();
                }

                Intent intent = new Intent(getApplicationContext(), TestingCamera.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                finish();

                sendBroadcast(new Intent("Finish_Me"));

            }
        });

        beforeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if((!Master.beforeImage.isRecycled()) && (!Master.afterImage.isRecycled())){

                    Master.beforeImage.recycle();
                    Master.afterImage.recycle();
                }

                Intent intent = new Intent(getApplicationContext(), TestingCamera.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                finish();

                sendBroadcast(new Intent("Finish_Me"));


            }
        });


        /*
           Display after image
        */

        afterImage = (ImageView)findViewById(R.id.after_image);
        afterImage.setImageBitmap(after);
        afterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                finish();

            }
        });

        afterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

    }

    public void initActionBar(ActionBar actionBar){

        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        View mCustomView = ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.comparision_window_action_bar, null);

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        mCustomView.setLayoutParams(params);
        actionBar.setCustomView(mCustomView);

        TextView actionBarTitle = (TextView)mCustomView.findViewById(R.id.view_title);
        actionBarTitle.setText("Compare Doors");

        TextView addQuestions = (TextView)mCustomView.findViewById(R.id.add_questions);
        addQuestions.setText("Finish");
        addQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(getApplicationContext(), RegstrationForm.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                finish();


            }
        });

        actionBar.setDisplayShowCustomEnabled(true);
    }

}

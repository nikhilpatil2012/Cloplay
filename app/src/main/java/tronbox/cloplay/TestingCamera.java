/*

 This activity will handle the camera, operations to be performed :-

  1) Take a pic.
  2) Retrieve the image from Media Store.
  3) Convert the image into Bitmap.
  4) Store the bitmap for the future use.


*/

package tronbox.cloplay;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class TestingCamera extends Activity {

    private ImageButton imageButton;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Uri imageUri;

    String mCurrentPhotoPath;

    private Point point = new Point();

    String file;


    private String selectedImagePath = "";
    final private int PICK_IMAGE = 1;
    final private int CAPTURE_IMAGE = 2;

    private Typeface babe;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();

        babe = Typeface.createFromAsset(getAssets(), "bebes.TTF");

        setContentView(R.layout.activity_camera);

        Master.canvasWidth = 0.75f * point.y;

        Master.canvasHeight = point.x;

        Log.w("CanvasSize", ""+Master.canvasWidth+"\n"+Master.canvasHeight);

        textView = (TextView)findViewById(R.id.text);
        textView.setTypeface(babe);

        imageButton = (ImageButton) findViewById(R.id.camera);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "Door Pic" + String.valueOf(new Random().nextInt()));
                values.put(MediaStore.Images.Media.DESCRIPTION, "Door Wizard");
                imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                Master.imagePath = getRealPathFromURI(getApplicationContext(), imageUri);
                Master.clickedImageUri = imageUri;

                Log.w("FilePath", Master.imagePath);

                if(Master.imagePath != null){

                    final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

                }



            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

           if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {

            Master.clickedImage = decodeFile(Master.imagePath);

            if(Master.clickedImage != null){

                Log.w("Operation Succussful", "Loaded Into The Memory");

                finish();

                Intent adjustActivity = new Intent(getApplicationContext(), ConfigureDoor.class);
                adjustActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                adjustActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(adjustActivity);


            }
              else {

                Toast.makeText(getApplicationContext(), "Out Of Memory, Kindly clear some memory", Toast.LENGTH_LONG).show();

            }

        }

    }



      public Uri getImageUri()
    {
        // Store image in dcim
        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "image" + new Date().getTime() + ".png");
        Uri imgUri = Uri.fromFile(file);
        Master.imagePath = file.getAbsolutePath();
        return imgUri;
    }

    public Bitmap decodeFile(String path) {

        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE_WIDTH = 700; //(int)Master.canvasWidth;
            final int REQUIRED_SIZE_HEIGHT = 600; //(int)Master.canvasHeight;


            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE_WIDTH && o.outHeight / scale / 2 >= REQUIRED_SIZE_HEIGHT)
                scale *= 2;

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeFile(path, o2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;

    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


}

///*
//
// This activity will handle the camera, operations to be performed :-
//
//  1) Take a pic.
//  2) Retrieve the image from Media Store.
//  3) Convert the image into Bitmap.
//  4) Store the bitmap for the future use.
//
//
//*/
//
//package tronbox.cloplay;
//
//import android.app.Activity;
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.Intent;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Point;
//import android.media.FaceDetector;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.view.View;
//import android.view.Window;
//import android.widget.ImageButton;
//import android.widget.Toast;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.Date;
//import java.util.Random;
//
//public class Camera extends Activity {
//
//    private ImageButton imageButton;
//    static final int REQUEST_IMAGE_CAPTURE = 1;
//    private Uri imageUri;
//
//    private Point point = new Point();
//
//    String file,imgPath;
//
//    private String selectedImagePath = "";
//    final private int PICK_IMAGE = 1;
//    final private int CAPTURE_IMAGE = 2;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
//        getActionBar().hide();
//
//        setContentView(R.layout.activity_camera);
//
//        Master.canvasWidth = 0.75f*point.y;
//
//        Master.canvasHeight = point.x;
//
//
//        imageButton = (ImageButton)findViewById(R.id.camera);
//        imageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//    /*
//        Store the image inside media store with the following details and return the URI for future use.
//    */
//
//                ContentValues values = new ContentValues();
//                values.put(MediaStore.Images.Media.TITLE, "Door Pic"+String.valueOf(new Random().nextInt()));
//                values.put(MediaStore.Images.Media.DESCRIPTION, "Door Wizard");
//                imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//
//
//    /*
//       Start the camera intent to the take the picture and store with the mentioned URI.
//    */
//
//                final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
//                startActivityForResult(intent, CAPTURE_IMAGE);
//
//            }
//        });
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if (resultCode != Activity.RESULT_CANCELED) {
//
//            if (requestCode == CAPTURE_IMAGE) {
//
//                selectedImagePath = getImagePath();
//                Master.clickedImage = decodeFile(selectedImagePath);
//
//
//                if(Master.clickedImage != null){
//
//                    BitmapFactory.Options bitmap_options = new BitmapFactory.Options();
//                    bitmap_options.inPreferredConfig = Bitmap.Config.RGB_565;
//                    Master.clickedImage = BitmapFactory.decodeFile(getImagePath(), bitmap_options);
//
//
//                    FaceDetector face_detector = new FaceDetector(Master.clickedImage.getWidth(), Master.clickedImage.getHeight(), 1);
//
//                    FaceDetector.Face face[] = new FaceDetector.Face[1];
//
//                    int i = face_detector.findFaces(Master.clickedImage, face);
//
//                      if(i != 0)
//                    {
//                        FaceDetector.Face f = face[0];
//
//
//                        Log.w("Faces_Detected", ""+f.confidence());
//                    }
//
//                      Log.w("Faces_Detected", ""+i);
//
//
//                    /*Intent adjustActivity = new Intent(getApplicationContext(), ConfigureDoor.class);
//                    adjustActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    adjustActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(adjustActivity);
//
//                    finish();
//                    */
//                }
//
//            } else {
//
//                Toast.makeText(getApplicationContext(), "Out Of Memory, Take Another Shot !!", Toast.LENGTH_LONG).show();
//
//
//                super.onActivityResult(requestCode, resultCode, data);
//
//            }
//        }
//
//    }
//
//    public String getRealPathFromURI(Context context, Uri contentUri) {
//        Cursor cursor = null;
//        try {
//            String[] proj = { MediaStore.Images.Media.DATA };
//            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            cursor.moveToFirst();
//            return cursor.getString(column_index);
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//    }
//
//    public static Bitmap Shrink(String file, int width, int height) {
//        final BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(file, options);
//
//        options.inSampleSize = 8;//calcSize(options, width, height);
//        options.inJustDecodeBounds = false;
//        Bitmap bmp = BitmapFactory.decodeFile(file, options);
//        return bmp;
//    }
//
//    public static int calcSize(BitmapFactory.Options options, int width, int height) {
//        final int uHeight = options.outHeight;
//        final int uWidth = options.outWidth;
//        int inSampleSize = 1;
//        if (uHeight > height || uWidth > width) {
//            if (uWidth > uHeight) {
//                inSampleSize = Math.round((float) uHeight / (float) height);
//            } else {
//                inSampleSize = Math.round((float) uWidth / (float) width);
//            }
//        }
//        return inSampleSize;
//    }
//
//
//
//    public Uri setImageUri() {
//        // Store image in dcim
//        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "image" + new Date().getTime() + ".png");
//        Uri imgUri = Uri.fromFile(file);
//        this.imgPath = file.getAbsolutePath();
//        return imgUri;
//    }
//
//    public String getImagePath() {
//        return imgPath;
//    }
//
//    public Bitmap decodeFile(String path) {
//        try {
//            // Decode image size
//            BitmapFactory.Options o = new BitmapFactory.Options();
//            o.inJustDecodeBounds = true;
//            BitmapFactory.decodeFile(path, o);
//            // The new size we want to scale to
//            final int REQUIRED_SIZE_WIDTH = 600;
//            final int REQUIRED_SIZE_HEIGHT = 500;
//
//
//            // Find the correct scale value. It should be the power of 2.
//            int scale = 1;
//            while (o.outWidth / scale / 2 >= REQUIRED_SIZE_WIDTH && o.outHeight / scale / 2 >= REQUIRED_SIZE_HEIGHT)
//                scale *= 2;
//
//            // Decode with inSampleSize
//            BitmapFactory.Options o2 = new BitmapFactory.Options();
//            o2.inSampleSize = scale;
//            return BitmapFactory.decodeFile(path, o2);
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//        return null;
//
//    }
//
//    public String getAbsolutePath(Uri uri) {
//        String[] projection = { MediaStore.MediaColumns.DATA };
//        @SuppressWarnings("deprecation")
//        Cursor cursor = managedQuery(uri, projection, null, null, null);
//        if (cursor != null) {
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//            cursor.moveToFirst();
//            return cursor.getString(column_index);
//        } else
//            return null;
//    }
//
//}

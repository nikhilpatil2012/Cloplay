package tronbox.cloplay;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

/*
    This activity works in 2 parts

    Part 1 :- Perform pinch In/Out + dragging operations on the background clicked image to adjust the pic and store the image.
    Part 2 :- Select the door you want to adjust over the background image and store the whole image.

*/

public class ConfigureDoor extends Activity {

    boolean fliper = false;

    String clicked = "car2";

    int listPosition = 0;

    int previousPosition = 0;

    private ImageView temp = null, door1, door2;
    private boolean clickTracker = false;

    private FrameLayout imageLayout;
    private FrameLayout.LayoutParams layoutParams;

    public TextView doorName;

    public Button next;

    private ListView listView;

    public enum Margin
    {
        SCREEN1, SCREEN2, SCREEN3
    }

    public boolean clip = false;

    private Margin state;

    private Point point = new Point();

    private ImageView imageView;


    private int[] car2Images = {

          R.drawable.door1,R.drawable.door2,R.drawable.door3,R.drawable.door4,R.drawable.door5,
          R.drawable.door6,R.drawable.door7,R.drawable.door8,R.drawable.door9,R.drawable.door10,
          R.drawable.door11,R.drawable.door15,R.drawable.door19,R.drawable.door23,
          R.drawable.door12,R.drawable.door16,R.drawable.door20,R.drawable.door24,
          R.drawable.door13,R.drawable.door17,R.drawable.door21,R.drawable.door25,
          R.drawable.door14,R.drawable.door18,R.drawable.door22,R.drawable.door26

    };

    private int[] car1Images = {
            R.drawable.door27,R.drawable.door28,R.drawable.door29,R.drawable.door30,R.drawable.door31,
            R.drawable.door32,R.drawable.door33,R.drawable.door34

    };


    private String selectedImage = "car2";

    private ArrayList<Category> categoriesList = new ArrayList<Category>();

    public Bitmap bitmap;

    public String blurPosition = "car2";

    private int selectedIndex = 0;

    CategoriesAdapter categoriesAdapter;

    private Typeface babe;

    public int screenWidth = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("Finish_Me");
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                unregisterReceiver(this);
                finish();
            }
        }, intentFilter);

        state = Margin.SCREEN1;

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();

        setContentView(R.layout.adjust_clicked_image);

        babe = Typeface.createFromAsset(getAssets(), "bebes.TTF");

        getWindowManager().getDefaultDisplay().getSize(point);

        bitmap = Bitmap.createScaledBitmap(Master.clickedImage, (int)(0.85*point.x), point.y, false);

        screenWidth = point.x;

        Master.canvasWidth = 0.75f*point.y;
        Master.canvasHeight = point.x;

        Master.backgroundWidth = point.x;
        Master.backgroundHeight = point.y;

        Log.w("CanvasWidth", ""+point.y);
        Log.w("CanvasHeight", ""+point.x);



        categoriesList.add(new Category("car2", R.drawable.car2));
        categoriesList.add(new Category("car1", R.drawable.car1));

        categoriesAdapter = new CategoriesAdapter(getApplicationContext(), R.layout.door_layout, categoriesList);

        listView = (ListView)findViewById(R.id.door_types);

        listView.setAdapter(categoriesAdapter);
        listView.setOnItemClickListener(myListViewClicked);

        next = (Button)findViewById(R.id.next_button);
        next.setTypeface(babe);


        doorName = (TextView)findViewById(R.id.door_name);
        doorName.setText("DOOR SIZE");
        doorName.setTypeface(babe);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(state == Margin.SCREEN2){

                    clip = true;

                    imageView.invalidate();


                }else {


                    Toast.makeText(getApplicationContext(), "Select Door Style", Toast.LENGTH_LONG).show();

                    selectedIndex = 0;

                    next.setText("DONE"); // change the next button image

                    state = Margin.SCREEN2;

                    Master.selectedDrawable = 0;

                    /*if(temp != null){

                        temp.setBackgroundResource(R.drawable.button_unselect_background);

                    }*/

                    Master.fixedScalingFactor = Master.scalingFactor;
                    Master.fixedX = Master.x;
                    Master.fixedY = Master.y;

                    doorName.setText("DOOR STYLE");

                    categoriesList.clear();

                    if(selectedImage.equals("car1")){

                        for(int i=0; i<car1Images.length;i++){

                           categoriesList.add(new Category("car1", car1Images[i]));
                        }


                        listView.setSelection(0);

                        categoriesAdapter.notifyDataSetChanged();

                    }else if(selectedImage.equals("car2")){

                        for(int i=0; i<car2Images.length;i++){

                            categoriesList.add(new Category("car2", car2Images[i]));
                        }

                        listView.setSelection(0);

                        categoriesAdapter.notifyDataSetChanged();
                    }

                    imageView.invalidate();


                }

            }
        });


            imageView = new CustomDoor(getApplicationContext());
            imageView.setBackgroundResource(R.drawable.greyback);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            imageLayout = (FrameLayout)findViewById(R.id.image_layout);
            layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT);
            imageLayout.addView(imageView, layoutParams);
    }


    AdapterView.OnItemClickListener myListViewClicked = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(final AdapterView<?> parent,  View view,  int position, long id) {

            Category category = (Category)parent.getAdapter().getItem(position);
            selectedImage = category.getName();


              if(state == Margin.SCREEN1)
            {


                    Master.selectedDrawable = category.getBitmap();
                    categoriesAdapter.setSelectedIndex(position);

                    imageView.invalidate();

                /*if(category.getName().equals("car1")){

                    selectedImage = "car1";

                    ((ImageView)((FrameLayout)listView.getChildAt(0)).findViewById(R.id.door_image)).setBackgroundResource(R.drawable.button_unselect_background);
                    ((ImageView)((FrameLayout)listView.getChildAt(1)).findViewById(R.id.door_image)).setBackgroundResource(R.drawable.button_select_background);


                }else if(category.getName().equals("car2")){


                    selectedImage = "car2";

                    ((ImageView)((FrameLayout)listView.getChildAt(0)).findViewById(R.id.door_image)).setBackgroundResource(R.drawable.button_select_background);
                    ((ImageView)((FrameLayout)listView.getChildAt(1)).findViewById(R.id.door_image)).setBackgroundResource(R.drawable.button_unselect_background);



                }*/

                if(category.getName().equals("car1")){


                    listView.setSelection(1);

                    blurPosition = "car1";
                    imageView.invalidate();

                }else if(category.getName().equals("car2")){


                    listView.setSelection(0);

                    blurPosition = "car2";
                    imageView.invalidate();

                }

            }

                if(state == Margin.SCREEN2)
              {

                  Master.selectedDrawable = category.getBitmap();
                  categoriesAdapter.setSelectedIndex(position);
                  listView.setSelection(position);
                  imageView.invalidate();

              }


        }
    };

    class CategoriesAdapter extends ArrayAdapter<Category> {

        Context context;
        int resource;
        ArrayList<Category> categoriesList;
        int totalSize = 0;
        private Button button;
        private Typeface robotoFont;
        int bitmapSize = 0;

        FrameLayout.LayoutParams layoutParams;

        public CategoriesAdapter(Context context, int resource, ArrayList<Category> categoriesList) {
            super(context, resource, categoriesList);

            this.context = context;
            this.resource = resource;
            this.categoriesList = categoriesList;

            bitmapSize = (int)0.20f*point.x;

        }

        public void setSelectedIndex(int ind)
        {
            selectedIndex = ind;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {

            return categoriesList.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = null;

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(resource, parent, false);

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), categoriesList.get(position).getBitmap());

            ImageView imageView1 = (ImageView)view.findViewById(R.id.door_image);
            imageView1.setImageBitmap(bitmap);


            if(state == Margin.SCREEN2){

                if(selectedIndex == position){

                    imageView1.setBackgroundResource(R.drawable.button_select_background);

                }
                  else
                {
                  imageView1.setBackgroundResource(R.drawable.button_unselect_background);
                }

            }

              if(state == Margin.SCREEN1)
            {

                if(selectedIndex == position){

                    door1 = imageView1;
                    imageView1.setBackgroundResource(R.drawable.button_select_background);

                }
                else
                {

                    door2 = imageView1;
                    imageView1.setBackgroundResource(R.drawable.button_unselect_background);
                }

                /*if(position == 0){

                    door1 = imageView1;
                    door1.setBackgroundResource(R.drawable.button_select_background);

                }else if(position == 1){

                    door2 = imageView1;
                }*/

            }

            return view;

        }
    }



    class CustomDoor extends ImageView {

        Rect rect;

        int count = 0;

        Bitmap scaledCrosshair, selectedImage;

        boolean select = true, flip = false;

        private ScaleGestureDetector mScaleDetector;
        private float mScaleFactor = 1.f;

        Paint paint;
        Paint black;

        Bitmap door;
        Bitmap home;

        Drawable image, homeDraw, crossHair;

        String TAG = "View_Data";

        private float mPosX = 0;
        private float mPosY = 0;

        private float beforeX = 0;
        private float beforeY = 0;

        private float afterX = 0;
        private float afterY = 0;


        private float mLastTouchX;
        private float mLastTouchY;

        private static final int INVALID_POINTER_ID = -1;

        // The ‘active pointer’ is the one currently moving our object.
        private int mActivePointerId = INVALID_POINTER_ID;

        RectF rectImageHorizantal = new RectF();
        RectF rectImageVertical = new RectF();


        ArrayList<Float> floatArrayList;

        Point point = new Point();

        Paint blurPaint = new Paint();


        Paint dashPaint = new Paint();
        Path pathHoriz = new Path();
        Path pathVert = new Path();


        float topLeftXVert, topLeftYVert, topRightXVert, topRightYVert, bottomLeftXVert, bottomLeftYVert, bottomRightXVert, bottomRightYVert;

        float topLeftXHoriz, topLeftYHoriz, topRightXHoriz, topRightYHoriz, bottomLeftXHoriz, bottomLeftYHoriz, bottomRightXHoriz, bottomRightYHoriz;


        public CustomDoor(Context context) {
            super(context);

            getWindowManager().getDefaultDisplay().getSize(point);

            floatArrayList = new ArrayList<Float>();

            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            black = new Paint(Paint.ANTI_ALIAS_FLAG);
            black.setColor(Color.BLACK);

            blurPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            blurPaint.setColor(Color.parseColor("#80FFFF00"));


            dashPaint.setAntiAlias(true);
            dashPaint.setColor(Color.parseColor("#ffffff"));
            dashPaint.setStyle(Paint.Style.STROKE);
            dashPaint.setPathEffect(new DashPathEffect(new float[]{ 11, 15, }, 0));
            dashPaint.setStrokeWidth(6);



            Log.w("Canvas Size", ""+point.y+"\n"+point.x);

            homeDraw = new BitmapDrawable(getResources(), bitmap);
            homeDraw.setBounds(0, 0, (int)Master.backgroundWidth, (int)Master.backgroundHeight);

            Master.blurRectWidth = Master.canvasWidth;
            Master.blurRectHeight = Master.canvasWidth/2;

            Master.width = homeDraw.getIntrinsicWidth();
            Master.height = homeDraw.getIntrinsicHeight();

            Log.w("ImageeeeWidth", homeDraw.getIntrinsicWidth()+"");
            Log.w("ImageeeeHeight", homeDraw.getIntrinsicHeight()+"");

            ///////// Horizontal Mode ////////

            int rightTad = 15;
            int topTad = 25;

            topLeftXHoriz = Master.canvasWidth/4 + rightTad;
            topLeftYHoriz = Master.canvasWidth/4 + topTad;

            topRightXHoriz = Master.canvasWidth+Master.canvasWidth/2 + rightTad;
            topRightYHoriz = Master.canvasWidth/4 + topTad;

            bottomLeftXHoriz = Master.canvasWidth/4 + rightTad;
            bottomLeftYHoriz = Master.canvasHeight/2.5f + topTad;

            bottomRightXHoriz = Master.canvasWidth+Master.canvasWidth/2 + rightTad;
            bottomRightYHoriz = Master.canvasHeight/2.5f + topTad;

            rectImageHorizantal.set(topLeftXHoriz+15, topLeftYHoriz+15, bottomRightXHoriz+15, bottomRightYHoriz+15);

            pathHoriz.moveTo(rectImageHorizantal.left+5, rectImageHorizantal.top+5);
            pathHoriz.lineTo(rectImageHorizantal.right-5, rectImageHorizantal.top+5);
            pathHoriz.lineTo(rectImageHorizantal.right-5, rectImageHorizantal.bottom-5);
            pathHoriz.lineTo(rectImageHorizantal.left+5, rectImageHorizantal.bottom-5);
            pathHoriz.lineTo(rectImageHorizantal.left+5, rectImageHorizantal.top+5);


            ///////// Portraight Mode ////////

            int adjustment = 50;
            int adjustmentX = 60;


            topLeftXVert = Master.canvasWidth/3 + adjustmentX;
            topLeftYVert = Master.canvasWidth/4 + adjustment;

            topRightXVert = Master.canvasWidth+Master.canvasWidth/4 + adjustmentX;
            topRightYVert = Master.canvasWidth/4 + adjustment;

            bottomLeftXVert = Master.canvasWidth/3 + adjustmentX;
            bottomLeftYVert = Master.canvasHeight/2.5f + adjustment;

            bottomRightXVert = Master.canvasWidth+Master.canvasWidth/4 + adjustmentX;
            bottomRightYVert = Master.canvasHeight/2.5f + adjustment;


            rectImageVertical.set(topLeftXVert+15, topLeftYVert+15, bottomRightXVert+15, bottomRightYVert+15);


            pathVert.moveTo(rectImageVertical.left, rectImageVertical.top);
            pathVert.lineTo(rectImageVertical.right, rectImageVertical.top);
            pathVert.lineTo(rectImageVertical.right, rectImageVertical.bottom);
            pathVert.lineTo(rectImageVertical.left, rectImageVertical.bottom);
            pathVert.lineTo(rectImageVertical.left, rectImageVertical.top);

            mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());

        }

        @Override
        public void setBackgroundResource(int resid) {
            super.setBackgroundResource(resid);
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            // Let the ScaleGestureDetector inspect all events.

            if(state == Margin.SCREEN1){

                mScaleDetector.onTouchEvent(ev);
            }

                final int action = ev.getAction();
                switch (action & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN: {
                        final float x = ev.getX();
                        final float y = ev.getY();

                        mLastTouchX = x;
                        mLastTouchY = y;
                        mActivePointerId = ev.getPointerId(0);

                          if (select == true)
                        {
                            floatArrayList.add(x);
                            floatArrayList.add(y);
                            invalidate();
                        }

                        break;
                    }

                    case MotionEvent.ACTION_MOVE: {

                        final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                        final float x = ev.getX(pointerIndex);
                        final float y = ev.getY(pointerIndex);

                        int screen = screenWidth - 200;

                        Log.w("XXXXXX", "X = "+x+"\n"+"Area "+screen);

                        if(x <= screen){

                            // Only move if the ScaleGestureDetector isn't processing a gesture.
                            if (!mScaleDetector.isInProgress()) {

                                final float dx = x - mLastTouchX;
                                final float dy = y - mLastTouchY;

                                if(state == Margin.SCREEN1){

                                    // Coords are beforeX and beforeY


                                    beforeX += dx;
                                    beforeY += dy;

                                    invalidate();

                                }else if(state == Margin.SCREEN2){

                                    // Coords are afterX and afterY

                                    afterX += dx;
                                    afterY += dy;

                                    invalidate();
                                }

                            }

                            mLastTouchX = x;
                            mLastTouchY = y;
                        }


                        break;
                    }

                    case MotionEvent.ACTION_UP: {
                        mActivePointerId = INVALID_POINTER_ID;
                        break;
                    }

                    case MotionEvent.ACTION_CANCEL: {
                        mActivePointerId = INVALID_POINTER_ID;
                        break;
                    }

                    case MotionEvent.ACTION_POINTER_UP: {
                        final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK)
                                >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                        final int pointerId = ev.getPointerId(pointerIndex);
                        if (pointerId == mActivePointerId) {
                            // This was our active pointer going up. Choose a new
                            // active pointer and adjust accordingly.
                            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                            mLastTouchX = ev.getX(newPointerIndex);
                            mLastTouchY = ev.getY(newPointerIndex);
                            mActivePointerId = ev.getPointerId(newPointerIndex);
                        }
                        break;
                    }
                }

            return true;
        }

        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.save();
            canvas.translate(beforeX, beforeY);
            canvas.scale(mScaleFactor, mScaleFactor);
            homeDraw.draw(canvas);
            canvas.restore();


            if(state == Margin.SCREEN1){ // This part represents Part 1

                if(blurPosition.equals("car2")){


                    canvas.drawRect(rectImageHorizantal, blurPaint);

                    canvas.drawPath(pathHoriz, dashPaint);

                    canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.target), topLeftXHoriz, topLeftYHoriz, paint);
                    canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.target), topRightXHoriz, topRightYHoriz,paint);

                    canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.target), bottomLeftXHoriz, bottomLeftYHoriz, paint);
                    canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.target), bottomRightXHoriz, bottomRightYHoriz, paint);


                }else if(blurPosition.equals("car1")){


                    canvas.drawRect(rectImageVertical, blurPaint);

                    canvas.drawPath(pathVert, dashPaint);

                    canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.target), topLeftXVert, topLeftYVert, paint);
                    canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.target), topRightXVert, topRightYVert,paint);

                    canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.target), bottomLeftXVert, bottomLeftYVert, paint);
                    canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.target), bottomRightXVert, bottomRightYVert, paint);



                }

            }else if(state == Margin.SCREEN2){ // This part represents Part 2

                if(Master.selectedDrawable == 0) {

                    if (blurPosition.equals("car1")) {

                        Master.selectedDrawable = R.drawable.door27;

                    } else if (blurPosition.equals("car2")) {


                        Master.selectedDrawable = R.drawable.door1;

                    }
                }

                    if(flip == false){ // invoke the one time operation, don't execute second time.


                        if(blurPosition.equals("car1"))
                        {

                            afterX = rectImageVertical.left; // blur image left.
                            afterY = rectImageVertical.top; // blue image top.
                            flip = true;

                        }else if(blurPosition.equals("car2")){

                            afterX = rectImageHorizantal.left; // blur image left.
                            afterY = rectImageHorizantal.top; // blue image top.
                            flip = true;

                        }
                    }

                    // Create the drawable from the selected door image and scale it to the size of a blur area.

                    Drawable drawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), Master.selectedDrawable), (int)rectImageHorizantal.width(), (int)rectImageHorizantal.height(), false));

                    if(drawable != null){

                          if(blurPosition.equals("car1"))
                        {

                            drawable.setBounds(0, 0, (int)rectImageVertical.width(), (int)rectImageVertical.height()); // drawable size

                        }else if(blurPosition.equals("car2")){


                            drawable.setBounds(0, 0, (int)rectImageHorizantal.width(), (int)rectImageHorizantal.height()); // drawable size

                        }

                        canvas.save();
                        canvas.translate(afterX, afterY); // move the door image exactly over the fixed blurr area.
                        drawable.draw(canvas);
                        canvas.restore();

                    }

                    if(clip == true){ // finally store both the unmodified and modified version of image in the memory for comparision window.

                        clip = false;



                        Master.afterImage = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
                        Master.beforeImage = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);


                        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                        paint.setDither(true);
                        paint.setAntiAlias(true);
                        paint.setFilterBitmap(true);
                        paint.setDither(true);


                        Path path = new Path();
                        path.addRect(0, 0, canvas.getWidth(), canvas.getHeight(), Path.Direction.CW);

                        Canvas clippingArea = null;

                        //***** Store Before Image *****//

                        clippingArea = new Canvas(Master.beforeImage);
                        clippingArea.clipPath(path);
                        clippingArea.translate(beforeX, beforeY);
                        clippingArea.scale(mScaleFactor, mScaleFactor);
                        homeDraw.draw(clippingArea);

                        //******************************//


                        //***** Store After Image *****//

                        clippingArea = new Canvas(Master.afterImage);

                        clippingArea.save();
                        clippingArea.translate(beforeX, beforeY);
                        clippingArea.scale(mScaleFactor, mScaleFactor);
                        homeDraw.draw(clippingArea);
                        clippingArea.restore();


                        clippingArea.save();
                        clippingArea.translate(afterX, afterY); // move the door image exactly over the fixed blurr area.
                        drawable.draw(clippingArea);
                        clippingArea.restore();


                        //******************************//

                        Master.clickedImage.recycle(); // recycle the bitmap to save memory.

                        Intent intent = new Intent(getApplicationContext(), ComparisionWindow.class); // call the third activity to compare the images.
                        startActivity(intent);

                        //finish(); // finish this activity

                    }


             //   }

            }

        }


        /*
            ScaleListener will control all the scaling operations.
        */

        private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                mScaleFactor *= detector.getScaleFactor();

                mScaleFactor = Math.max(0.9f, Math.min(mScaleFactor, 5.0f));

                Log.w("ScaleFacter", mScaleFactor+"");

                Master.scalingFactor = mScaleFactor;

                invalidate();
                return true;
            }
        }

    }


    public class Category {

        private int bitmap;
        private String name;
        private String message;

        public Category(String name, int bitmap) {

            this.name = name;
            this.bitmap = bitmap;

        }

        public int getBitmap(){return bitmap;}

        public String getName() {
            return name;
        }

    }





}

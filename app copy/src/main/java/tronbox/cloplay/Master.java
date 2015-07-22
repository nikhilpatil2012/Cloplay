package tronbox.cloplay;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.LruCache;

public class Master extends Application {

    public static Bitmap clickedImage;

    public static float scalingFactor, x = 0, y = 0;

    public static float fixedScalingFactor, fixedX, fixedY;


    public static int height, width;

    public static Rect bounds;

    public static boolean screenThree = false;

    public static float canvasWidth, canvasHeight, bitmapWidth, bitmapHeight, backgroundWidth, backgroundHeight;

    public static int selectedDrawable = 0;

    public static float blurRectWidth, blurRectHeight;

    public static Uri clickedImageUri;

    public static Uri beforeImageUri, afterImageUri;


    public static Bitmap afterImage, beforeImage;


    public static Point point;

    public static String imagePath;


}

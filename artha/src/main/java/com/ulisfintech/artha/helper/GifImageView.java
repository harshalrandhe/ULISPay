package com.ulisfintech.artha.helper;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.ulisfintech.artha.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Type;

public class GifImageView extends View {

    private InputStream mInputStream;
    private Movie mMovie;
    private int mWidth, mHeight;
    private long mStart;
    private Context mContext;
    private int src;

    public GifImageView(Context context) {
        super(context);
        this.mContext = context;
    }

    public GifImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GifImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyGif);
        src = typedArray.getResourceId(R.styleable.MyGif_src, 0);
        setGifImageResource(src);
        typedArray.recycle();
    }

    private void init() {
        setFocusable(true);
        mMovie = Movie.decodeStream(mInputStream);
        mWidth = mMovie.width();
        mHeight = mMovie.height();

        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//         mWidth = MeasureSpec.getSize(widthMeasureSpec);
//         mHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        long now = SystemClock.uptimeMillis();

        if (mStart == 0) {
            mStart = now;
        }

        if (mMovie != null) {

            int duration = mMovie.duration();
            if (duration == 0) {
                duration = 1000;
            }

            int relTime = (int) ((now - mStart) % duration);

            mMovie.setTime(relTime);

            mMovie.draw(canvas, 0, 0);
            invalidate();
        }
    }

    public void setGifImageResource(int id) {
        mInputStream = mContext.getResources().openRawResource(id);
        init();
    }

    public void setGifImageUri(Uri uri) {

        Uri fileUri = Uri.fromFile(new File(""));

        try {
            mInputStream = mContext.getContentResolver().openInputStream(uri);
            init();
        } catch (FileNotFoundException e) {
            Log.e("GIfImageView", "File not found");
        }
    }

    public void setGifImageFile(String filePath) {
        Uri uri = Uri.fromFile(new File(filePath));

        try {
            mInputStream = mContext.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            Log.e("GIfImageView", "File not found");
        }

    }

    public void setGifImageFile(File file) {
        Uri uri = Uri.fromFile(file);

        try {
            mInputStream = mContext.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            Log.e("GIfImageView", "File not found");
        }

    }
}
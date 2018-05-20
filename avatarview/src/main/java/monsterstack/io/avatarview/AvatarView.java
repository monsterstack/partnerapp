package monsterstack.io.avatarview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class AvatarView extends AppCompatImageView {

    /*
     * Path of them image to be clipped (to be shown)
     * */
    Path clipPath;

    /*
     * Place holder drawable (with background color and initials)
     * */
    Drawable drawable;

    /*
     * Contains initials of the member
     * */
    String text;

    float borderWidth;
    /*
     * Used to set size and color of the member initials
     * */
    TextPaint textPaint;

    /*
     * Used as background of the initials with user specific color
     * */
    Paint paint;

    /*
     * To draw border
     */
    private Paint borderPaint;

    /*
     * Shape to be drawn
     * */
    int shape;

    int textSize;

    /*
     * Constants to define shape
     * */
    protected static final int CIRCLE = 0;
    protected static final int RECTANGLE = 1;

    /*
     * User whose avatar should be displayed
     * */
    User user;

    /*
     * Image width and height (both are same and constant, defined in dimens.xml
     * We cache them in this field
     * */
    private int imageSize;

    /*
     * We will set it as 2dp
     * */
    int cornerRadius;

    /*
     * Bounds of the canvas in float
     * Used to set bounds of member initial and background
     * */
    RectF rectF;

    public AvatarView(Context context) {
        super(context);
    }

    public AvatarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        getAttributes(attrs);
        init();
    }

    public AvatarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        getAttributes(attrs);
        init();
    }

    private void getAttributes(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.AvatarView,
                0, 0);

        try {

            /*
             * Get the shape and set shape field accordingly
             * */
            String avatarShape = a.getString(R.styleable.AvatarView_avatar_shape);
            textSize = a.getDimensionPixelSize(R.styleable.AvatarView_name_size,
                    getResources().getDimensionPixelSize(R.dimen.avatar_text_size));
            /*
             * If the attribute is not specified, consider circle shape
             * */
            if (avatarShape == null) {
                shape = CIRCLE;
            } else {
                if (getContext().getString(R.string.rectangle).equals(avatarShape)) {
                    shape = RECTANGLE;
                } else {
                    shape = CIRCLE;
                }
            }
        } finally {
            a.recycle();
        }
    }

    /*
     * Initialize fields
     * */
    protected void init() {

        /*
         * Below Jelly Bean, clipPath on canvas would not work because lack of hardware acceleration
         * support. Hence, we should explicitly say to use software acceleration.
         * */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }

        rectF = new RectF();
        clipPath = new Path();

        imageSize = getResources().getDimensionPixelSize(R.dimen.avatar_size);
        cornerRadius = (int) Utils.dpToPixel(2, getResources());

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(textSize * getResources().getDisplayMetrics().scaledDensity);
        textPaint.setColor(Color.WHITE);

        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        borderPaint.setColor(ContextCompat.getColor(getContext(), R.color.avatar_border_color));
        borderPaint.setStrokeWidth(getContext().getResources().getDimension(R.dimen.avatar_border_width));
    }

    /*
     * Get User object and set values based on the user
     * This is the only exposed method to the developer
     * */
    public void setUser(User user) {
        this.user = user;
        setValues();
    }

    /*
     * Set user specific fields in here
     * */
    private void setValues() {

        /*
         * user specific color for initial background
         * */
        paint.setColor(user.getColor());

        /*
         * Initials of member
         * */
        text = Helper.getShortName(user.getName());

        borderWidth = getResources().getDimension(R.dimen.avatar_border_width);

        setDrawable();

        if (user.getAvatarUrl() != null) {
            RequestOptions requestOptions = RequestOptions.placeholderOf(drawable)
                    .centerCrop()
                    .override(imageSize, imageSize);
            Glide.with(getContext())
                    .asDrawable()
                    .load(user.getAvatarUrl())
                    .apply(requestOptions)
                    .into(this);
        } else {
            setImageDrawable(drawable);
            invalidate();
        }
    }


    /*
     * Create placeholder drawable
     * */
    private void setDrawable() {
        drawable = new Drawable() {
            @Override
            public void draw(@NonNull Canvas canvas) {

                int centerX = Math.round(canvas.getWidth() * 0.5f);
                int centerY = Math.round(canvas.getHeight() * 0.5f);

                /*
                 * To draw text
                 * */
                if (text != null) {
                    float textWidth = textPaint.measureText(text) * 0.5f;
                    float textBaseLineHeight = textPaint.getFontMetrics().ascent * -0.4f;

                    /*
                     * Draw the background color before drawing initials text
                     * */
                    if (shape == RECTANGLE) {
                        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint);
                    } else {
                        canvas.drawCircle(centerX,
                                centerY,
                                Math.max(canvas.getHeight() / 2, textWidth / 2),
                                paint);
                    }

                    /*
                     * Draw the text above the background color
                     * */
                    canvas.drawText(text, centerX - textWidth, centerY + textBaseLineHeight, textPaint);
                }
            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(ColorFilter colorFilter) {

            }

            @Override
            public int getOpacity() {
                return PixelFormat.UNKNOWN;
            }
        };
    }

    /*
     * Set the canvas bounds here
     * */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int screenWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        int screenHeight = View.MeasureSpec.getSize(heightMeasureSpec);
        rectF.set(0, 0, screenWidth, screenHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (shape == RECTANGLE) {
            canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, borderPaint);
            clipPath.addRoundRect(rectF, cornerRadius, cornerRadius, Path.Direction.CW);
        } else {
            canvas.drawCircle(rectF.centerX(), rectF.centerY(), (rectF.height() / 2) - borderWidth, borderPaint);

            clipPath.addCircle(rectF.centerX(), rectF.centerY(), (rectF.height() / 2), Path.Direction.CW);
        }
        canvas.clipPath(clipPath);
        super.onDraw(canvas);
    }
}

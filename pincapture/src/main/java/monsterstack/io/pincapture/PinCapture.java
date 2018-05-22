package monsterstack.io.pincapture;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PinCapture extends LinearLayout {

    public int entryCount = 0; //count of boxes to be created
    private int currentIndex = 0;
    private static int EDITTEXT_MAX_LENGTH = 1; //character size of each editext
    private static int EDITTEXT_WIDTH = 40;
    private static int EDITTEXT_TEXTSIZE = 20; //textsize
    private boolean disableTextWatcher = false, backKeySet = false;
    private TextWatcher textWatcher;
    private OnFinishListener onFinishListener;

    public PinCapture(Context context) {
        super(context, null);
    }

    public PinCapture(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PinCapture(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs, defStyle, 0);
    }

    public PinCapture(Context context, AttributeSet attrs, int defStyle, int defStyleRes) {
        super(context, attrs);
        init(context, attrs);
    }

    public void setOnFinishListener(OnFinishListener onFinishListener) {
        this.onFinishListener = onFinishListener;
    }

    private void init(Context context, AttributeSet attrs) {

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.PinCapture, 0, 0);


        entryCount = a.getInteger(R.styleable.PinCapture_entryCount, 4);

        a.recycle();

        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        for (int i = 0; i < entryCount; i++) {

            //creates edittext based on the no. of count
            addView(initialiseAndAddChildInLayout(i, context, attrs), i);
        }

    }

    private View initialiseAndAddChildInLayout(int index, Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.PinCapture, 0, 0);

        final EditText editText = new EditText(context);
        editText.setMaxWidth(1);
        editText.setTag(index);
        editText.setGravity(Gravity.CENTER);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(EDITTEXT_MAX_LENGTH)});
        editText.setTextSize(EDITTEXT_TEXTSIZE);

        boolean isPassword = a.getBoolean(R.styleable.PinCapture_password, false);
        if (isPassword) {
            editText.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD | InputType.TYPE_CLASS_NUMBER);
        } else {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

        LayoutParams param = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
        editText.setLayoutParams(param);
        editText.addTextChangedListener(textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentIndex = Integer.parseInt(editText.getTag().toString());

                if (editText.getText().toString().length() == 1 && !disableTextWatcher) {
                    getNextEditText(currentIndex);
                } else if (editText.getText().toString().length() == 0 && !disableTextWatcher) {// && !isFirstTimeGetFocused && !backKeySet) {
                    getPreviousEditText(currentIndex);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        editText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    currentIndex = Integer.parseInt(editText.getTag().toString());
                    if (editText.getText().toString().length() == 0 && !disableTextWatcher) {
                        getPreviousEditTextFocus(currentIndex);
                    } else {
                        disableTextWatcher = true;
                        editText.setText("");
                        disableTextWatcher = false;
                    }
                    backKeySet = true;
                }

                return false;
            }


        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    if(currentIndex==entryCount-1 && getEnteredText().length()==entryCount)
                    {
                        onFinishListener.onFinish(getEnteredText());
                    }
                }
                return false;
            }
        });

        return editText;
    }

    //method focuses of previous editext
    private void getPreviousEditText(int index) {
        if (index > 0) {
            EditText editText = (EditText) getChildAt(index - 1);
            disableTextWatcher = true;

            editText.setText("");
            editText.requestFocus();
            disableTextWatcher = false;

        }
    }

    //method focuses of previous editext
    private void getPreviousEditTextFocus(int index) {
        if (index > 0) {
            EditText editText = (EditText) getChildAt(index - 1);
            disableTextWatcher = true;
            editText.requestFocus();
            disableTextWatcher = false;
        }
    }


    //method to focus on next edittext
    private void getNextEditText(final int index) {
        if (index < entryCount - 1) {
            // use Handler for 1 second delay so that it will change text
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    EditText editText = (EditText) getChildAt(index + 1);
                    editText.requestFocus();
                }
            },200);
        }
    }

    public String getEnteredText() {
        String strEnteredValue = "";
        for (int i = 0; i < getChildCount(); i++) {
            EditText editText = (EditText) getChildAt(i);
            if (editText.getText() != null && editText.getText().toString().length() > 0)
                strEnteredValue = strEnteredValue + editText.getText().toString();

        }
        return strEnteredValue;
    }

    public void clearPin() {
        for (int i = 0; i < getChildCount(); i++) {
            EditText editText = (EditText) getChildAt(i);
            editText.setText("");
        }
        EditText editText = (EditText) getChildAt(0);
        editText.requestFocus();
    }

    public interface OnFinishListener {
        void onFinish(String enteredText);
    }
}

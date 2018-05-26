package monsterstack.io.partner.utils;

import android.content.Context;
import android.widget.TextView;

import com.elmargomez.typer.Font;
import com.elmargomez.typer.Typer;

public class TypefaceUtils {

    public static final void useRobotoRegular(Context context, TextView textView) {
        textView.setTypeface(Typer.set(context).getFont(Font.ROBOTO_REGULAR));
    }
}

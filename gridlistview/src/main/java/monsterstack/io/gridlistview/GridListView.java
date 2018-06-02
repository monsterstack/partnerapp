package monsterstack.io.gridlistview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class GridListView extends RecyclerView {
    public GridListView(Context context) {
        super(context);
        GridLayoutManager layoutManager = new GridLayoutManager(context, 4);
        layoutManager.setMeasurementCacheEnabled(true);
        this.setLayoutManager(layoutManager);
    }

    public GridListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        GridLayoutManager layoutManager = new GridLayoutManager(context, 4);
        layoutManager.setMeasurementCacheEnabled(true);
        this.setLayoutManager(layoutManager);

    }

    public GridListView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        GridLayoutManager layoutManager = new GridLayoutManager(context, 4);
        layoutManager.setMeasurementCacheEnabled(true);
        this.setLayoutManager(layoutManager);

    }
}
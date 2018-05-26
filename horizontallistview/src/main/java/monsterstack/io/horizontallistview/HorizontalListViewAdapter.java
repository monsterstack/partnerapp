package monsterstack.io.horizontallistview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

public abstract class HorizontalListViewAdapter<D, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter {
    private D[] data;
    private LayoutInflater inflater;
    private ItemClickListener clickListener;

    // data is passed into the constructor
    public HorizontalListViewAdapter(Context context, D[] data) {
        super();
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return data.length;
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}

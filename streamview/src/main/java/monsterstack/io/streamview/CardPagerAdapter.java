package monsterstack.io.streamview;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class CardPagerAdapter<T> extends PagerAdapter implements CardAdapter {
    private CardView[] cardViewList;
    private List<T> dataList;
    private float baseElevation;

    public CardPagerAdapter(List<T> dataList) {
        this.cardViewList = new CardView[dataList.size()];
        this.dataList = dataList;
    }

    @Override
    public float getBaseElevation() {
        return baseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return cardViewList[position];
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(getLayout(), container, false);
        container.addView(view);
        bind(dataList.get(position), view);
        CardView cardView = view.findViewById(getCardViewId());

        if (baseElevation == 0) {
            baseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(baseElevation * MAX_ELEVATION_FACTOR);
        cardViewList[position] = cardView;
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

    }

    public abstract void bind(T data, View view);
    public abstract int getCardViewId();
    public abstract int getLayout();
}

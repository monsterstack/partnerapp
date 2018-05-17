package monsterstack.io.streamview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract  class CardFragment extends Fragment {
    private CardView cardView;

    public abstract int getCardViewId();

    public abstract int getLayout();

    public CardView getCardView() {
        return cardView;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);

        cardView = view.findViewById(getCardViewId());

        cardView.setMaxCardElevation(cardView.getCardElevation() * CardAdapter.MAX_ELEVATION_FACTOR);

        return view;
    }
}

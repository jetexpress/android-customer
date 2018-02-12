package com.weekendinc.jet.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by antoni on 2/12/15.
 */
public class SpinnerHintAdapter
        extends ArrayAdapter<String> {

    public SpinnerHintAdapter(Context context, int textViewResourceId,
                              List<String> objects) {
        super(context, textViewResourceId, objects);
    }

    @Override
    public int getCount() {
        // don't display last item. It is used as hint.
        int count = super.getCount();
        return count > 0 ? count - 1 : count;
    }

}
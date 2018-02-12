package com.weekendinc.jet.fragments.listener;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * Created by Fadhlan on 3/18/15.
 */
public class ClearTextWatcher implements TextWatcher {
    private EditText editText;
    private ImageButton button;

    public ClearTextWatcher(EditText editText, ImageButton button) {
        this.editText = editText;
        this.button = button;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() < 1 && button.getVisibility() == View.VISIBLE)
            button.setVisibility(View.GONE);
        else if (s.length() > 0 && button.getVisibility() != View.VISIBLE)
            button.setVisibility(View.VISIBLE);
    }
}

package com.example.mathspace.adapters;

import android.text.Editable;
import android.text.TextWatcher;

public abstract class TextWatcherAdapter implements TextWatcher {

    @Override
    public  void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //do nothing
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //do nothing
    }

    @Override
    public void afterTextChanged(Editable editable) {
        //do nothing
    }
}

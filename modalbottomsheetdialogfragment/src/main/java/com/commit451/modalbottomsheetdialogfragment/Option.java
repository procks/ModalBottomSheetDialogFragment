package com.commit451.modalbottomsheetdialogfragment;

import android.graphics.drawable.Drawable;

import android.support.annotation.NonNull;

public class Option {
    public final int id;
    public CharSequence title;
    public Drawable icon;

    public Option(int id, @NonNull CharSequence title, Drawable icon) {
        this.id = id;
        this.title = title;
        this.icon = icon;
    }
}

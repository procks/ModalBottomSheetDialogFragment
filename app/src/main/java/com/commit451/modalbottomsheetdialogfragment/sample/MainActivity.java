package com.commit451.modalbottomsheetdialogfragment.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.commit451.modalbottomsheetdialogfragment.ModalBottomSheetDialogFragment;
import com.commit451.modalbottomsheetdialogfragment.Option;
import com.commit451.modalbottomsheetdialogfragment.OptionRequest;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ModalBottomSheetDialogFragment.Listener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonShow = findViewById(R.id.buttonShow);
        buttonShow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new ModalBottomSheetDialogFragment.Builder()
                                .add(R.menu.options)
                                .add(new OptionRequest(123, "Custom", R.drawable.ic_bluetooth_black_24dp))
                                .show(getSupportFragmentManager(), "HI");
                    }
                }
        );

        Button buttonShowWithHeader = findViewById(R.id.buttonShowWithHeader);
        buttonShowWithHeader.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new ModalBottomSheetDialogFragment.Builder()
                                .add(R.menu.option_lots)
                                .header("Neat")
                                .show(getSupportFragmentManager(), "HI");
                    }
                }
        );

        Button buttonCustom = findViewById(R.id.buttonCustom);
        buttonCustom.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new ModalBottomSheetDialogFragment.Builder()
                                .add(R.menu.option_lots)
                                .layout(R.layout.item_custom)
                                .header("Neat")
                                .columns(3)
                                .show(getSupportFragmentManager(), "HI");
                    }
                }
        );

        Button buttonOrder = findViewById(R.id.buttonOrder);
        buttonOrder.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new ModalBottomSheetDialogFragment.Builder()
                                .add(R.menu.options)
                                .add(new OptionRequest(123, "Custom", R.drawable.ic_bluetooth_black_24dp))
                                .add(R.menu.option_money)
                                .show(getSupportFragmentManager(), "HI");
                    }
                }
        );
    }

    @Override
    public void onModalOptionSelected(String tag, @NonNull Option option) {
        Snackbar.make(findViewById(R.id.root), String.format(Locale.ROOT,
                "Selected option %s from tag %s", option.title, tag), Snackbar.LENGTH_SHORT).show();
    }
}

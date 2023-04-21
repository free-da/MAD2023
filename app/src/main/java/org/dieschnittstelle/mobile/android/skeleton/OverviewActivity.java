package org.dieschnittstelle.mobile.android.skeleton;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;

public class OverviewActivity extends AppCompatActivity {

    private ViewGroup listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1) Auswahl der darzustellenden Ansicht
        setContentView(R.layout.activity_overview);
        this.listView = findViewById(R.id.listView);

        Arrays.asList("Test", "Item", "noch ein", "Paar")
                .forEach(listitem -> {
                    TextView itemView = (TextView) getLayoutInflater().inflate(R.layout.activity_overview_listitem, null);
                    itemView.setText(listitem);
                    itemView.setOnClickListener(view -> {
                        showMessage("Selected: " + ((TextView)view).getText());
                    });
                    this.listView.addView(itemView);
                });
    }

    protected void showMessage(String msg) {
        Snackbar.make(findViewById(R.id.rootView),msg, Snackbar.LENGTH_SHORT).show();
    }
}

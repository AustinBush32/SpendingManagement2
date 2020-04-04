package com.example.spendingmanagement2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.content.SharedPreferences;
import java.text.DecimalFormat;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    DBHelper db;
    public static final String pref = "SpendingManagement";
    public static final String bal = "BalanceKey";
    public static final String lastNote = "LastNote";
    private static DecimalFormat df = new DecimalFormat("0.00");
    Button add, sub;
    EditText date, amount, description;
    TextView balance;
    LinearLayout log;
    float currBal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        add = findViewById(R.id.add);
        add.setOnClickListener(onClickForAdd());
        sub = findViewById(R.id.sub);
        sub.setOnClickListener(onClickForSub());
        date = findViewById(R.id.date);
        amount = findViewById(R.id.amount);
        description = findViewById(R.id.description);
        balance = findViewById(R.id.CurrentBalance);
        log = findViewById(R.id.log);

        currBal = 0.0f;

        db = new DBHelper(this);

        Cursor savedData = db.getData();
        while(savedData.moveToNext()) {
            log.addView(createNewTextView(savedData.getString(1)));
            currBal = savedData.getFloat(2);
        }

        balance.setText("Current Balance: $" + df.format(currBal));

    }

    private OnClickListener onClickForAdd() {
        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                String s = "Adding $" + amount.getText().toString() + " on " + date.getText().toString() +
                        " from " + description.getText().toString();
                log.addView(createNewTextView(s));

                currBal += Float.parseFloat(amount.getText().toString());

                db.addData(s, currBal);
                balance.setText("Current Balance: $" + df.format(currBal));
            }
        };
    }

    private OnClickListener onClickForSub() {
        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                String s = "Spent $" + amount.getText().toString() + " on " + date.getText().toString() +
                        " for " + description.getText().toString();
                log.addView(createNewTextView(s));

                currBal -= Float.parseFloat(amount.getText().toString());

                db.addData(s, currBal);
                balance.setText("Current Balance: $" + df.format(currBal));
            }
        };
    }

    private TextView createNewTextView(String text) {
        final LayoutParams lparams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        final TextView newNote = new TextView(this);
        newNote.setLayoutParams(lparams);
        newNote.setText(text);
        newNote.setTextSize(15);
        newNote.setPadding(20, 50, 20, 50);
        return newNote;
    }

}

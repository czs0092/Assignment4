package com.example.assignment4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;



public class MainActivity extends AppCompatActivity {

    EditText Date, Price, Item;
    Button Add, Sp;
    TextView balance, history;
    DBManagement db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Item = findViewById(R.id.Item);
        Add = findViewById(R.id.Add);
        Sp = findViewById(R.id.Sub);
        Price = findViewById(R.id.Price);
        balance = findViewById(R.id.balance);
        Date = findViewById(R.id.Date);
        history = findViewById(R.id.history);
        db = new DBManagement(this);
        addHistory();
        spHistory();
    }

    public void addHistory(){
        Add.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        double price = Double.parseDouble(Price.getText().toString());
                        boolean result = db.createHistory(Item.getText().toString(), Date.getText().toString(), price);

                        if (result)
                            Toast.makeText(MainActivity.this, "Successfully Created", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(MainActivity.this, "Failed to Create", Toast.LENGTH_LONG).show();
                        spHistory();

                        MainActivity.this.Date.setText("");
                        MainActivity.this.Price.setText("");
                        MainActivity.this.Item.setText("");
                    }
                }
        );

        Sp.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        double price = Double.parseDouble(Price.getText().toString()) * -1;
                        boolean result = db.createHistory(Item.getText().toString(), Date.getText().toString(), price);

                        if (result)
                            Toast.makeText(MainActivity.this, "Successfully Created", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(MainActivity.this, "Failed to Create", Toast.LENGTH_LONG).show();
                        spHistory();

                        MainActivity.this.Date.setText("");
                        MainActivity.this.Price.setText("");
                        MainActivity.this.Item.setText("");
                    }
                }
        );
    }


    public void spHistory(){
        Cursor result = db.pullData();
        StringBuffer str = new StringBuffer();
        Double balance = 0.00;

        while(result.moveToNext()){
            String priceString = result.getString(3);
            double price = Double.parseDouble(result.getString(3));

            balance += price;

            if (price < 0) {
                str.append("Spent $");
                priceString = priceString.substring(1);
            }
            else {
                str.append("Added $");
            }

            str.append(priceString + " on " + result.getString(2) + " for " + result.getString(1) + "\n");
        }

        MainActivity.this.balance.setText("Current Balance: $" + Double.toString(balance));
        MainActivity.this.history.setText(str);
    }
}

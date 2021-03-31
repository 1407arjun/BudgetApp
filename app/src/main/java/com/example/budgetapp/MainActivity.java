package com.example.budgetapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ArrayList<HashMap<String, String>> items = new ArrayList<>();
    SimpleAdapter simpleAdapter;
    Toolbar toolbar;
    int total = 0, count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView listView = findViewById(R.id.listView);
        simpleAdapter = new SimpleAdapter(this, items, R.layout.item_card_layout, new String[] {"Line1", "Line2", "Line3"}, new int[] {R.id.itemName, R.id.itemQuantityText, R.id.itemPriceText});
        listView.setAdapter(simpleAdapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.stat_sys_warning)
                        .setTitle("Remove item")
                        .setMessage("This item will be removed from the budget")
                        .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                count -= Integer.parseInt(items.get(position).get("Line2"));
                                total -= Integer.parseInt(items.get(position).get("Line3"))*Integer.parseInt(items.get(position).get("Line2"));
                                toolbar.setTitle("Total amount = Rs. " + total);
                                toolbar.setSubtitle(count + " item(s)");
                                items.remove(position);
                                simpleAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
                return true;
            }
        });
    }

    public void add(View view) {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.manual_entry_dialog);

        Button addBudget = dialog.findViewById(R.id.addBudget);
        TextInputLayout nameLayout = dialog.findViewById(R.id.filledName);
        EditText nameText = dialog.findViewById(R.id.nameText);
        TextInputLayout qtyLayout = dialog.findViewById(R.id.filledQuantity);
        EditText qtyText = dialog.findViewById(R.id.qtyText);
        TextInputLayout priceLayout = dialog.findViewById(R.id.filledPrice);
        EditText priceText = dialog.findViewById(R.id.priceText);

        addBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameLayout.setError(null);
                qtyLayout.setError(null);
                priceLayout.setError(null);
                if (nameText.getText().toString().isEmpty() || qtyText.getText().toString().isEmpty() || priceText.getText().toString().isEmpty()) {
                    nameLayout.setError("Please fill all the fields");
                    qtyLayout.setError(" ");
                    priceLayout.setError(" ");
                } else {
                    InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(nameText.getWindowToken(), 0);
                    dialog.dismiss();
                    HashMap<String, String> item = new HashMap<>(2);
                    item.put("Line1", nameText.getText().toString());
                    item.put("Line2", qtyText.getText().toString());
                    item.put("Line3", priceText.getText().toString());
                    items.add(item);
                    count += Integer.parseInt(qtyText.getText().toString());
                    total += Integer.parseInt(priceText.getText().toString())*Integer.parseInt(qtyText.getText().toString());
                    simpleAdapter.notifyDataSetChanged();
                    toolbar.setTitle("Total amount = Rs. " +  total);
                    toolbar.setSubtitle(count + " item(s)");
                }
            }
        });
        dialog.show();
    }
}
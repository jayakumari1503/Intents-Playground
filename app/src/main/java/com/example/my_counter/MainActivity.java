package com.example.my_counter;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.example.my_counter.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private int qty = 0;
    ActivityMainBinding b;
    private int minVal, maxVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //To create layout using layout inflater
        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        setupEventHandlers();
        getInitialCount();
    }

    /**
    *Get the data from the starter activity
    */
    private void getInitialCount() {
        Bundle bundle = getIntent().getExtras();

        // Getting all the data which is come from the starter activity
        qty = bundle.getInt(Constants.INITIAL_COUNT_KEY, 0);
        minVal = bundle.getInt(Constants.MINIMUM_VALUE, Integer.MIN_VALUE);
        maxVal = bundle.getInt(Constants.MAXIMUM_VALUE, Integer.MAX_VALUE);

        if (qty != 0) {
            b.sendBackButton.setVisibility(View.VISIBLE);
        }

        b.qty.setText(String.valueOf(qty));
    }
    
    /**
    *Trigger Event handlers to listen the actions
    */
        private void setupEventHandlers() {
        b.incBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Calling inrease function
                incQty();
            }
        });

        b.decBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Calling decrease function
                decQty();
            }
        });
    }

    /**
    *To decrease the quantity and update the result in text view
    */
    public void decQty() {
        b.qty.setText(--qty + " ");
    }

    /**
    *To increase the quantity and update the result in text view
    */
    public void incQty() {
        b.qty.setText(++qty + " ");
    }

    /**
    *To send the final count back to main activity
    *@param view button view which is triggered
    */
    
    public void sendDataBack(View view) {
        if (qty >= minVal && qty <= maxVal) {
            // Send the data to the starter activity
            Intent intent = new Intent();
            intent.putExtra(Constants.FINAL_COUNT, qty);
            setResult(RESULT_OK, intent);
               
            //close the activity
            finish();
        }
        // When not in range
        else {
            Toast.makeText(this, "Not in Range!", Toast.LENGTH_SHORT).show();
        }
    }
}

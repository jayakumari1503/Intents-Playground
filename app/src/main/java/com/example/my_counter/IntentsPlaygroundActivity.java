package com.example.my_counter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.my_counter.databinding.ActivityIntentsPlaygroundBinding;


public class IntentsPlaygroundActivity extends AppCompatActivity {
    private static final int REQUEST_COUNT = 0;
    ActivityIntentsPlaygroundBinding b;

    // Initial setup

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupLayout();
        setupHideErrorForEditText();
    }

    /**
    *Setup layout using root element of UI
    */
    private void setupLayout() {
        b = ActivityIntentsPlaygroundBinding.inflate(getLayoutInflater());

        setContentView(b.getRoot());
        setTitle("Intents Playground");
    }

    /**
    *Text Watcher gives callback when the text in the text fields changes
    */
    private void setupHideErrorForEditText() {
        TextWatcher myTextwatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hideError();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        b.data.getEditText().addTextChangedListener(myTextwatcher);
        b.initialCounterEditText.getEditText().addTextChangedListener(myTextwatcher);
    }

    // Event Handlers
    /**To open main activity and pass the count to the activity
    *@param view view of the button pressed
    */
        public void openMainActivity (View view){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        }

    /**Sending Implicit Activity
    *@param view view of the button pressed
    */
    public void sendImplicitIntent (View view){
            // get the input data and trim it
            String input = b.data.getEditText().getText().toString().trim();

            // Check that the input data is not empty if it is then return the function
            if (input.isEmpty()) {
                b.data.setError("Please enter something");
                return;
            }

            // Validate intent Type
            int type = b.intentTypeRadioGroup.getCheckedRadioButtonId();

            // Handling implicit intent cases
            // Comparing type with radio group using their IDs
            if (type == b.openWepageRadioButton.getId()) {
                openWebpage(input);
            } else if (type == b.dialNumberRadioButton.getId()) {
                dialNumber(input);
            } else if (type == b.shareTextRadioButton.getId()) {
                shareText(input);
            } else
                Toast.makeText(this, "Please select an intent type", Toast.LENGTH_SHORT).show();
        }

        /**
        *Sending count data to main activity
        *@param view view of the button pressed
        */
        public void sendData (View view){
            // get the input data and trim it
            String input = b.initialCounterEditText.getEditText().getText().toString().trim();

            // Check that the input data is not empty if it is then return the function
            if (input.isEmpty()) {
                b.initialCounterEditText.setError("Please enter something");
                return;
            }

            int initialCount = Integer.parseInt(input);

            Bundle bundle = new Bundle();
            bundle.putInt(Constants.INITIAL_COUNT_KEY, initialCount);
            bundle.putInt(Constants.MINIMUM_VALUE, -100);
            bundle.putInt(Constants.MAXIMUM_VALUE, 100);

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtras(bundle);

            startActivityForResult(intent, REQUEST_COUNT);

        }
    
        /**
        *@param requestCode 
        *@param resultCode
        *@param data
        */
        @Override
        protected void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){
            super.onActivityResult(requestCode, resultCode, data);

            // Checking the incoming data
            if (requestCode == REQUEST_COUNT && resultCode == RESULT_OK) {
                int count = data.getIntExtra(Constants.FINAL_COUNT, Integer.MIN_VALUE);

                b.result.setText("Final count received: " + count);
                b.result.setVisibility(View.VISIBLE);
            }
        }
    
//Implicit Intent Sender
   
    /**
    *To share text data using different application
    *@param text data which is to be send
    */
        private void shareText (String text){
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(intent, "Share text via"));
        }

    
   /**
   *Dialing number using the caller application
   *@param number number to be called
   */
        private void dialNumber (String number){
            // Check if input is 10 digit or not
            if (!number.matches("^\\d{10}$")) {
                b.data.setError("Invalid number!");
                return;
            }

            Uri uri = Uri.parse("tel:" + number);
            Intent intent = new Intent(Intent.ACTION_DIAL, uri);
            startActivity(intent);

            hideError();
        }

    /**
    * open web page
    *@param url URL to be opened in the browser
    */
        private void openWebpage (String url){
            // Check if input is URL or not
            if (!url.matches("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")) {
                b.data.setError("Invalid URL!");
                return;
            }

            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);

            hideError();
        }
    
    // Utility functions
        private void hideError () {
            b.data.setError(null);
        }
}

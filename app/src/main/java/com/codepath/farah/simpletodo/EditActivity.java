package com.codepath.farah.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {
    EditText editItem;
    Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editItem = findViewById(R.id.editItem);
        buttonSave = findViewById(R.id.buttonSave);

        getSupportActionBar().setTitle("Edit Item");

       editItem.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));
       // When the user is done editing, they click the save button
       buttonSave.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               // create an intent which will contain the results
               Intent intent = new Intent();

               // pass the data(results of editing)
               intent.putExtra(MainActivity.KEY_ITEM_TEXT, editItem.getText().toString());
               intent.putExtra(MainActivity.KEY_ITEM_POSITION,getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));

               // set the result of the intent
               setResult(RESULT_OK,intent);

               // finish activity, close the screen and go back
               finish();
           }
       });
    }
}
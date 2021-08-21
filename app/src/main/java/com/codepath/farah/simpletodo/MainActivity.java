package com.codepath.farah.simpletodo;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;



    List<String> items;
    Button buttonAdd;
    EditText edit_item;
    RecyclerView itemList;
    ItemsAdapter itemsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonAdd = findViewById(R.id.buttonAdd);
        edit_item = findViewById(R.id.edit_item);
        itemList = findViewById(R.id.itemList);



        loadItems();


        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                // Delete the item from the model
                items.remove(position);
                // Notify the adapter which position
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was Removed!", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
              Log.d("MainActivity", "Single click at position" + position);
              // create the new activity
                Intent intent = new Intent(MainActivity.this,EditActivity.class);
              // pass the data being edited
                intent.putExtra(KEY_ITEM_TEXT,items.get(position));
                intent.putExtra(KEY_ITEM_POSITION,position);
              // display the activity
                startActivityForResult(intent,EDIT_TEXT_CODE);

            }
        };

        itemsAdapter = new ItemsAdapter(items, onLongClickListener, onClickListener);
        itemList.setAdapter(itemsAdapter);
        itemList.setLayoutManager(new LinearLayoutManager(this));

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String todoItem = edit_item.getText().toString();
                // Add item to model
                items.add(todoItem);
                // Notify adapter that an item is inserted
                itemsAdapter.notifyItemInserted(items.size() - 1);
                edit_item.setText("");
                Toast.makeText(getApplicationContext(), "Item was Added!", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });

    }

    // handle the result of the edit activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE){
            // retrieve the updated text value
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            // extract the original position of the edited item from the position key
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);

            // update the model at the right position with new item text
            items.set(position,itemText);
            // notify the adapter
            itemsAdapter.notifyItemChanged(position);
            // persist the changes
            saveItems();
            Toast.makeText(getApplicationContext(),"Item Updated Successfully!",Toast.LENGTH_SHORT).show();
        } else{
            Log.w("MainActivity","Unknown call to onActivityResult");
        }
    }

    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");
    }
    // this function will load items by reading every line of the data file
    private void loadItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity","Error reading items", e);
            items = new ArrayList<>();
        }
    }
    // this function saves items by writing them into the data file
    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity","Error writing items", e);
        }
    }
}
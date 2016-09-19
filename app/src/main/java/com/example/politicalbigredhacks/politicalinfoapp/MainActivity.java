package com.example.politicalbigredhacks.politicalinfoapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
     ImageButton imageButton;
    EditText editText;
    String zip;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //After importing fonts into assets on the left, create a typeface object
        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "Quicksand-Regular.ttf");

        //using the id from activity_main.xml, instantiate 2 objects
        TextView mytextView = (TextView)findViewById(R.id.textView);
        mytextView.setTypeface(myTypeface);

        TextView mytextView2 = (TextView)findViewById(R.id.textView2);
        mytextView2.setTypeface(myTypeface);

        //"intent" to move to the next screen upon clicking the "Arrow" image button
        intent=new Intent(this,Main2Activity.class);
        editText=(EditText) findViewById(R.id.editText);
        imageButton=(ImageButton) findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              zip=editText.getText().toString();
              intent.putExtra(Intent.EXTRA_TEXT,zip);
              startActivity(intent);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package dk.prmedia.context;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import dk.prmedia.context.data.NewSentenceActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the View that shows the categories
        LinearLayout TvLocalAuth = (LinearLayout) findViewById(R.id.local_authority_button);
        LinearLayout TvLibrary = (LinearLayout) findViewById(R.id.library_button);
        LinearLayout TvGrocery = (LinearLayout) findViewById(R.id.grocery_store_button);
        LinearLayout TvDoctor = (LinearLayout) findViewById(R.id.doctor_button);
        LinearLayout TvVarious = (LinearLayout) findViewById(R.id.various_button);
        LinearLayout TvIntro = (LinearLayout) findViewById(R.id.introduction_button);
        LinearLayout TvWelcome = (LinearLayout) findViewById(R.id.welcome_button);

        // Set a click listener on the local authority LinearLayout
        TvLocalAuth.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent localIntent = new Intent(MainActivity.this, AuthorityActivity.class);
                startActivity(localIntent);
            }
        });

        // Set a click listener on the doctor LinearLayout
        TvDoctor.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent localIntent = new Intent(MainActivity.this, DirectionsActivity.class);
                startActivity(localIntent);
            }
        });

        // Set a click listener on the grocery store LinearLayout
        TvGrocery.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent localIntent = new Intent(MainActivity.this, StoreActivity.class);
                startActivity(localIntent);
            }
        });

        // Set a click listener on the library LinearLayout
        TvLibrary.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent localIntent = new Intent(MainActivity.this, LibraryActivity.class);
                startActivity(localIntent);
            }
        });

        // Set a click listener on the various LinearLayout
        TvVarious.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent localIntent = new Intent(MainActivity.this, VariousActivity.class);
                startActivity(localIntent);
            }
        });

        // Set a click listener on the various LinearLayout
        TvIntro.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent localIntent = new Intent(MainActivity.this, IntroductionActivity.class);
                startActivity(localIntent);
            }
        });

        // Set a click listener on the various LinearLayout
        TvWelcome.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent localIntent = new Intent(MainActivity.this, WelcomeActivity.class);
                startActivity(localIntent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_new.xml
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            case R.id.action_new_sentence:
                Intent intent = new Intent(MainActivity.this, NewSentenceActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}

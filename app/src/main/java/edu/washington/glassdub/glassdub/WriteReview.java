package edu.washington.glassdub.glassdub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * TODO: Save all the data user inputted onto Kumulos server
 */

public class WriteReview extends Activity {
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);

        submit = (Button) findViewById(R.id.button9);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WriteReview.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}

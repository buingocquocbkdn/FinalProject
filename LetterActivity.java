package cndd.finalproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class LetterActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE = 1;
    private int mNumberLetter;
    private LinearLayout layout_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        layout_main = (LinearLayout) findViewById(R.id.layout_main);
        for (mNumberLetter = Constant.START_NUMBER_LETTER; mNumberLetter <= Constant.END_NUMBER_LETTER; mNumberLetter++) {
            Button btn = new Button(this);
            btn.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150));
            btn.setBackgroundResource(R.drawable.btnletter);
            btn.setText(mNumberLetter + " Letter");
            btn.setTextColor(Color.YELLOW);
            layout_main.addView(btn);
            btn.setTag(mNumberLetter);
            btn.setOnClickListener(this);
        }
    }


    @Override
    public void onClick(View view) {
        Button button = (Button) view;
        Intent questionIntent = new Intent(LetterActivity.this, QuestionActivity.class);
        Bundle bundle = new Bundle();
        mNumberLetter = (int) button.getTag();
        bundle.putInt("number_Letter", mNumberLetter);
        questionIntent.putExtra("Letter", bundle);
        startActivityForResult(questionIntent, REQUEST_CODE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(LetterActivity.this, MainActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}

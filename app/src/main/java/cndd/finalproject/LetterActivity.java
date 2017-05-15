package cndd.finalproject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class LetterActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE = 1;
    Button[] btn_Letter;
    LinearLayout layout_main;
    int i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letter);
        layout_main = (LinearLayout) findViewById(R.id.layout_main);
        for (i = 3; i < 7; i++) {
            Button btn = new Button(this);
            btn.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150));
            btn.setBackgroundResource(R.drawable.btnletter);
            btn.setText(i + " Letter");
            btn.setTextColor(Color.YELLOW);
            layout_main.addView(btn);
            btn.setTag(i);
            btn.setOnClickListener(this);
        }
    }


    @Override
    public void onClick(View view) {
        Button button = (Button) view;
        Intent questionIntent = new Intent(LetterActivity.this, QuestionActivity.class);
        Bundle bundle = new Bundle();
        i = (int) button.getTag();
        bundle.putInt("number_Letter", i);
        questionIntent.putExtra("Letter", bundle);
        startActivityForResult(questionIntent, REQUEST_CODE);
    }
}

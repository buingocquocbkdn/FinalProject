package com.example.mypc.a4pics_1word;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LinearLayout layout_2;
    private LinearLayout layout_3;
    private Button[] btnKq;

    private MyDatabase mDatabase;

    public static final int[] QUESTIONS_3={
            0,//Bỏ qua phần tử 0
            R.drawable.img_3_war,
            R.drawable.img_3_wax,
            R.drawable.img_3_way,
            R.drawable.img_3_wet,
            R.drawable.img_3_wig,
            R.drawable.img_3_wok,
            R.drawable.img_3_yak,
            R.drawable.img_3_zen,
            R.drawable.img_3_zip,
    };
    public static final String[] ANSWER_3={
            "",//Bỏ qua phần tử 0
            "WAR",
            "WAX",
            "WAY",
            "WET",
            "WIG",
            "WOK",
            "YAK",
            "ZEN",
            "ZIP",
    };
    public static final int[] QUESTIONS_4={
            0,//Bỏ qua phần tử 0
            R.drawable.img_4_snap,
            R.drawable.img_4_tube,
            R.drawable.img_4_tuna,
            R.drawable.img_4_tune,
            R.drawable.img_4_twig,
            R.drawable.img_4_type,
            R.drawable.img_4_unit,
    };
    public static final String[] ANSWER_4={
            "",//Bỏ qua phần tử 0
            "SNAP",
            "TUBE",
            "TUNA",
            "TUNE",
            "TWIG",
            "TYPE",
            "UNIT",
    };
    public static final int[] QUESTIONS_5={
            0,//Bỏ qua phần tử 0
            R.drawable.img_5_write,
            R.drawable.img_5_wrong,
            R.drawable.img_5_yacht,
            R.drawable.img_5_young,
            R.drawable.img_5_zebra,
    };
    public static final String[] ANSWER_5={
            "",//Bỏ qua phần tử 0
            "WRITE",
            "WRONG",
            "YACHT",
            "YOUNG",
            "ZEBRA",
    };
    public static final int[] QUESTIONS_6={
            0,//Bỏ qua phần tử 0
            R.drawable.img_6_athens,
            R.drawable.img_6_attach,
            R.drawable.img_6_caress,
    };
    public static final String[] ANSWER_6={
            "",//Bỏ qua phần tử 0
            "ATHENS",
            "ATTACH",
            "CARESS",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Khởi tạo CSDL
        mDatabase = new MyDatabase(this);
        initListLetter();

        //Tạo image, tham số là ID={3,4,5,6}
        createImage(3);
    }

    //Khởi tạo CSDL
    public void initListLetter() {
        int startID = 3;//Bắt đầu là 3 letters
        mDatabase.open();
        int countRecords = mDatabase.countRecords();
        mDatabase.close();
        if(countRecords<=0){
            String[] letters = {
                    " Three letters",
                    " Four letters",
                    " Five letters",
                    " Six letters",
            };

            int[] maxQuestions={
                    QUESTIONS_3.length-1,
                    QUESTIONS_4.length-1,
                    QUESTIONS_5.length-1,
                    QUESTIONS_6.length-1,
            };

            for (int i = startID; i < letters.length+startID; i++) {
                Letter letter = new Letter();
                letter.setId(i);
                letter.setLetter(letters[i-startID]);
                letter.setCoin(0);
                letter.setCurrentQuestion(1);
                letter.setMaxQuestion(maxQuestions[i-startID]);

                mDatabase.open();
                mDatabase.addItem(letter);
                mDatabase.close();
            }
        }

    }

    //Lấy câu hỏi hiện tại từ ID
    public int getCurrentQuestion(int id){
        int currentQuestion;
        List<Letter> letters = new ArrayList<>();

        //Select currentQuestion from TableDB where ID=id
        mDatabase.open();
        letters = mDatabase.getListByID(id);
        mDatabase.close();

        currentQuestion = letters.get(0).getCurrentQuestion();

        return currentQuestion;
    }

    //Hiển thị hình ảnh
    public void createImage(int id) {
        int curentQuestion = getCurrentQuestion(id);

        layout_2 = (LinearLayout) findViewById(R.id.layout_2);
        ImageView iv = new ImageView(this);
        iv.setLayoutParams(new LinearLayout.LayoutParams(480,500));

        switch (id){
            case 3: iv.setImageResource(QUESTIONS_3[curentQuestion]);
                    break;
            case 4: iv.setImageResource(QUESTIONS_4[curentQuestion]);
                    break;
            case 5: iv.setImageResource(QUESTIONS_5[curentQuestion]);
                    break;
            case 6: iv.setImageResource(QUESTIONS_6[curentQuestion]);
        }

        layout_2.addView(iv);

        createButton(id);
    }

    //Tạo button điền kết quả
    public void createButton(int id) {
        layout_3 = (LinearLayout) findViewById(R.id.layout_3);
        int lengthButton=id;
        btnKq= new Button[lengthButton];

        for (int i = 0; i < lengthButton; i++) {
            Button btn = new Button(this);
            btn.setLayoutParams(new LinearLayout.LayoutParams(100,100));
            btn.setId(i);
            layout_3.addView(btn);
            btnKq[i] = (Button) findViewById(btn.getId());
        }
    }
}

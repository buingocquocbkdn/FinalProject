package cndd.finalproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuestionActivity extends AppCompatActivity {

    private LinearLayout layout_image;
    private LinearLayout layout_answer;
    private LinearLayout layout_4;
    private LinearLayout layout_5;
    private int id;
    private Button[] btnKq, btnAsw;
    private int idAsw = 0;
    private int idQs = 0;
    private int flag = 0;
    private String tmp = "";
    private int mCurrentQuestion;
    private TextView txtcoin, txtlevel;
    private int mCoin, mLevel;
    private int mMaxQuestion;
    private MyDatabase mDatabase;
    private Button btn_home;

    final Context context = this;

    public static final int[] QUESTIONS_3 = {
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
    public static final String[] ANSWER_3 = {
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
    public static final int[] QUESTIONS_4 = {
            0,//Bỏ qua phần tử 0
            R.drawable.img_4_snap,
            R.drawable.img_4_tube,
            R.drawable.img_4_tuna,
            R.drawable.img_4_tune,
            R.drawable.img_4_twig,
            R.drawable.img_4_type,
            R.drawable.img_4_unit,
    };
    public static final String[] ANSWER_4 = {
            "",//Bỏ qua phần tử 0
            "SNAP",
            "TUBE",
            "TUNA",
            "TUNE",
            "TWIG",
            "TYPE",
            "UNIT",
    };
    public static final int[] QUESTIONS_5 = {
            0,//Bỏ qua phần tử 0
            R.drawable.img_5_write,
            R.drawable.img_5_wrong,
            R.drawable.img_5_yacht,
            R.drawable.img_5_young,
            R.drawable.img_5_zebra,
    };
    public static final String[] ANSWER_5 = {
            "",//Bỏ qua phần tử 0
            "WRITE",
            "WRONG",
            "YACHT",
            "YOUNG",
            "ZEBRA",
    };
    public static final int[] QUESTIONS_6 = {
            0,//Bỏ qua phần tử 0
            R.drawable.img_6_athens,
            R.drawable.img_6_attach,
            R.drawable.img_6_caress,
    };
    public static final String[] ANSWER_6 = {
            "",//Bỏ qua phần tử 0
            "ATHENS",
            "ATTACH",
            "CARESS",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        txtlevel = (TextView) findViewById(R.id.txtlevel);
        txtcoin = (TextView) findViewById(R.id.txtcoin);
        btn_home =(Button) findViewById(R.id.btn_home);

        final Intent intent = new Intent(QuestionActivity.this, MainActivity.class);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
        //Khởi tạo CSDL

        mDatabase = new MyDatabase(this);
        initListLetter();
        //Tạo image, tham số là ID={3,4,5,6}
        Intent callerIntent = getIntent();
        Bundle packageIntent = callerIntent.getBundleExtra("Letter");
        id = packageIntent.getInt("number_Letter");
        mDatabase.open();
        mDatabase.updateCoin(id, 1);
        mDatabase.close();
        mCurrentQuestion = getCurrentQuestion(id);

        createImage(id);

        createButtonQuestion(id);
        txtcoin.setText(mCoin + "");
        coinClick(mCoin, id);
        levelInfor(mCurrentQuestion, mMaxQuestion);
    }

    //Khởi tạo CSDL
    public void initListLetter() {
        int startID = 3;//Bắt đầu là 3 letters
        mDatabase.open();
        int countRecords = mDatabase.countRecords();
        mDatabase.close();
        if (countRecords <= 0) {
            String[] letters = {
                    " Three letters",
                    " Four letters",
                    " Five letters",
                    " Six letters",
            };

            int[] maxQuestions = {
                    QUESTIONS_3.length - 1,
                    QUESTIONS_4.length - 1,
                    QUESTIONS_5.length - 1,
                    QUESTIONS_6.length - 1,
            };

            for (int i = startID; i < letters.length + startID; i++) {
                Letter letter = new Letter();
                letter.setId(i);
                letter.setLetter(letters[i - startID]);
                letter.setCoin(0);
                letter.setCurrentQuestion(1);
                letter.setMaxQuestion(maxQuestions[i - startID]);

                mDatabase.open();
                mDatabase.addItem(letter);
                mDatabase.close();
            }
        }

    }

    //Lấy câu hỏi hiện tại từ ID
    public int getCurrentQuestion(int id) {
        int currentQuestion;
        List<Letter> letters = new ArrayList<Letter>();

        //Select currentQuestion from TableDB where ID=id
        mDatabase.open();
        letters = mDatabase.getListByID(id);
        mDatabase.close();

        currentQuestion = letters.get(0).getCurrentQuestion();
        mCoin = letters.get(0).getCoin();
        mMaxQuestion = letters.get(0).getMaxQuestion();
        return currentQuestion;
    }

    //Hiển thị hình ảnh
    public void createImage(int id) {
        //int curentQuestion = getCurrentQuestion(id);

        layout_image = (LinearLayout) findViewById(R.id.layout_image);
        ImageView iv = new ImageView(this);
        iv.setLayoutParams(new LinearLayout.LayoutParams(480, 500));
        if (!checkFinishId(id, mCurrentQuestion)) {
            switch (id) {
                case 3:
                    iv.setImageResource(QUESTIONS_3[mCurrentQuestion]);
                    break;
                case 4:
                    iv.setImageResource(QUESTIONS_4[mCurrentQuestion]);
                    break;
                case 5:
                    iv.setImageResource(QUESTIONS_5[mCurrentQuestion]);
                    break;
                case 6:
                    iv.setImageResource(QUESTIONS_6[mCurrentQuestion]);
            }

            layout_image.addView(iv);

            createButton(id);
        }
    }

    //Tạo button điền kết quả
    public void createButton(int id) {
        layout_answer = (LinearLayout) findViewById(R.id.layout_answer);
        int lengthButton = id;
        if (!checkFinishId(id, mCurrentQuestion)) {
            btnKq = new Button[lengthButton];


            for (idQs = 0; idQs < lengthButton; idQs++) {
                final Button btn = new Button(this);
                btn.setLayoutParams(new LinearLayout.LayoutParams(100, 100));


                btn.setId(idQs + 111);
                layout_answer.addView(btn);
                btnKq[idQs] = (Button) findViewById(btn.getId());
                btnKq[idQs].setEnabled(false);
                btnKq[idQs].setText("");

                btnKq[idQs].setTypeface(Typeface.createFromAsset(getAssets(),  "fonts/vnarialbold.ttf"));
                btnKq[idQs].setTextSize(25);

                btnKq[idQs].setBackgroundResource(R.drawable.btnasw1);

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < 12; i++) {
                            if (btnAsw[i].getText() == "") {
                                btnAsw[i].setText(btnKq[(Integer.parseInt(btn.getId() + "") - 111)].getText());
                                Log.e("aaa", btn.getId() + "");
                                btnAsw[i].setVisibility(View.VISIBLE);
                                flag--;
                                btnKq[btn.getId() - 111].setText("");
                                btnKq[btn.getId() - 111].setBackgroundResource(R.drawable.btnasw1);
                                btnKq[btn.getId() - 111].setEnabled(false);
                                break;
                            }
                        }
                        if (flag < 0) flag = 0;
                        Log.e("aaaa", flag + "//");

                    }
                });
            }
        }
    }

    public void coinClick(int icoin, final int id) {
        this.mCoin = icoin;
        if (mCurrentQuestion > mMaxQuestion) ;
        else {

            txtcoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCoin == 0){
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("NO DIAMOND!");
                        builder.setMessage("SORRY, YOU ARE OUT OF DIAMONDS")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //do things
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                    else {
                        String answer = "";
                        switch (id){
                            case 3: answer = ANSWER_3[mCurrentQuestion];
                                break;
                            case 4: answer = ANSWER_4[mCurrentQuestion];
                                break;
                            case 5: answer = ANSWER_5[mCurrentQuestion];
                                break;
                            case 6: answer = ANSWER_6[mCurrentQuestion];
                                break;

                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("GOOD LUCK!");
                        builder.setMessage("The answer is: " + answer)
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //do things
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                        mCoin--;
                        removeView();
                        mCurrentQuestion++;

                        mDatabase.updateCurrentQuestion(id, mCurrentQuestion);
                        mDatabase.updateCoin(id, mCoin);
                        createButtonQuestion(id);
                        createImage(id);
                        levelInfor(getCurrentQuestion(id), mMaxQuestion);
                        txtcoin.setText(String.valueOf(mCoin));
                    }
                }
            });
        }
    }

    public void levelInfor(int mCurrent, int mMax) {
        txtlevel.setText(mCurrent + " / " + mMax);
    }

    public void createButtonQuestion(final int id) {
        //mCurrentQuestion = getCurrentQuestion(id);

        layout_4 = (LinearLayout) findViewById(R.id.layout_4);
        layout_5 = (LinearLayout) findViewById(R.id.layout_5);
        final int lengthButton = 12;
        btnAsw = new Button[lengthButton];
        mDatabase.open();
        if (!checkFinishId(id, mCurrentQuestion)) {

            switch (id) {
                case 3:
                    tmp = ANSWER_3[mCurrentQuestion];
                    break;
                case 4:
                    tmp = ANSWER_4[mCurrentQuestion];
                    break;
                case 5:
                    tmp = ANSWER_5[mCurrentQuestion];
                    break;
                case 6:
                    tmp = ANSWER_6[mCurrentQuestion];
                    break;
            }
            Log.e("aaaa", tmp + "////" + id);
            Random rd = new Random();
            String arr[] = new String[12];
            for (int i = 0; i < tmp.length(); i++) {
                boolean check = true;
                while (check) {
                    int rdd = rd.nextInt(12);
                    if (arr[rdd] == null) {
                        arr[rdd] = tmp.charAt(i) + "";
                        break;
                    } else continue;
                }
            }
            for (int i = 0; i < arr.length; i++) {
                if (arr[i] == null) {
                    arr[i] = ((char) (rd.nextInt(25) + 65)) + "";
                }
            }
            for (int i = 0; i < arr.length; i++) {
                Log.e("aaaa", arr[i] + "/////");
            }


            for (idAsw = 0; idAsw < 6; idAsw++) {
                final Button btn = new Button(this);
                btn.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
                btn.setId(idAsw);

                btn.setBackgroundResource(R.drawable.btnchoose);
                btn.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/vnarialbold.ttf"));
                btn.setTextSize(25);

                layout_4.addView(btn);
                btnAsw[idAsw] = (Button) findViewById(btn.getId());
                btnAsw[idAsw].setText(arr[idAsw] + "");
                Log.e("aaaa", flag + "//");
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (flag == 6) ;
                        else {

                            btnKq[flag].setTextColor(Color.WHITE);

                            btnKq[flag].setText(btnAsw[btn.getId()].getText());
                            btnKq[flag].setEnabled(true);
                            flag++;
                            btnAsw[btn.getId()].setText("");
                            btnAsw[btn.getId()].setVisibility(View.INVISIBLE);
                            Log.e("aaaa", flag + "//");
                            if (flag == id) {
                                String answer = "";
                                for (Button b : btnKq
                                        ) {
                                    answer += b.getText();
                                }
                                if (answer.equals(tmp)) {
                                    mCoin++;
                                    mDatabase.open();
                                    mDatabase.updateCoin(id, mCoin);
                                    mDatabase.close();
                                    removeView();
                                    btnKq = new Button[id];
                                    btnAsw = new Button[12];
                                    idQs = 0;
                                    idAsw = 0;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle("CORRECT!");
                                    builder.setMessage("YOU GOT IT \nAnswer: " + tmp)
                                            .setCancelable(false)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    //do things
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();

                                    removeView();
                                    btnKq = new Button[id];
                                    btnAsw = new Button[12];
                                    idQs = 0;
                                    idAsw = 0;

                                    mCurrentQuestion++;
                                    if (!checkFinishId(id, mCurrentQuestion)) {
                                        mDatabase.open();
                                        mDatabase.updateCurrentQuestion(id, mCurrentQuestion);
                                        mDatabase.close();
                                        createImage(id);
                                        createButtonQuestion(id);
                                        levelInfor(getCurrentQuestion(id), mMaxQuestion);
                                    }
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle("WRONG ANSWER!");
                                    builder.setMessage("TRY AGAIN!")
                                            .setCancelable(false)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    //do things
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                    removeView();
                                    btnKq = new Button[id];
                                    btnAsw = new Button[12];
                                    idQs = 0;
                                    idAsw = 0;
                                    createImage(id);
                                    createButtonQuestion(id);
                                }
                                flag = 0;
                            }
                        }
                    }
                });
            }
            for (idAsw = 6; idAsw < 12; idAsw++) {
                final Button btn = new Button(this);
                btn.setLayoutParams(new LinearLayout.LayoutParams(100, 100));

                btn.setBackgroundResource(R.drawable.btnchoose);
                btn.setTypeface(Typeface.createFromAsset(getAssets(),  "fonts/vnarialbold.ttf"));
                btn.setTextSize(25);

                btn.setId(idAsw);

                layout_5.addView(btn);
                btnAsw[idAsw] = (Button) findViewById(btn.getId());
                btnAsw[idAsw].setText(arr[idAsw] + "");
                Log.e("aaaa", flag + "//");
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (flag == 6) ;
                        else {

                            btnKq[flag].setTextColor(Color.WHITE);

                            btnKq[flag].setText(btnAsw[btn.getId()].getText());
                            btnKq[flag].setEnabled(true);
                            flag++;
                            btnAsw[btn.getId()].setText("");
                            btnAsw[btn.getId()].setVisibility(View.INVISIBLE);
                            Log.e("aaaa", flag + "//");
                            if (flag == id) {
                                String answer = "";
                                for (Button b : btnKq
                                        ) {
                                    answer += b.getText();
                                }
                                if (answer.equals(tmp)) {
                                    mCoin++;
                                    mDatabase.open();
                                    mDatabase.updateCoin(id, mCoin);
                                    mDatabase.close();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle("CORRECT!");
                                    builder.setMessage("YOU GOT IT \nAnswer: " + tmp)
                                            .setCancelable(false)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    //do things
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();

                                    removeView();
                                    btnKq = new Button[id];
                                    btnAsw = new Button[12];
                                    idQs = 0;
                                    idAsw = 0;

                                    mCurrentQuestion++;
                                    if (!checkFinishId(id, mCurrentQuestion)) {
                                        mDatabase.open();
                                        mDatabase.updateCurrentQuestion(id, mCurrentQuestion);
                                        mDatabase.close();
                                        createImage(id);
                                        createButtonQuestion(id);
                                        levelInfor(getCurrentQuestion(id), mMaxQuestion);
                                    }
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle("WRONG ANSWER!");
                                    builder.setMessage("TRY AGAIN!")
                                            .setCancelable(false)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    //do things
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                    removeView();
                                    btnKq = new Button[id];
                                    btnAsw = new Button[12];
                                    idQs = 0;
                                    idAsw = 0;
                                    createImage(id);
                                    createButtonQuestion(id);


                                }
                                //Toast.makeText(MainActivity.this, "///"+tmp+"///", Toast.LENGTH_SHORT).show();
                                flag = 0;
                            }
                        }
                    }
                });
            }
        }
    }

    public void removeView() {
        layout_image.removeAllViews();
        layout_4.removeAllViews();
        layout_5.removeAllViews();
        layout_answer.removeAllViews();
    }

    public boolean checkFinishId(int id, int mCurrentQuestion) {
        if (mCurrentQuestion > mMaxQuestion) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("CONGRATULATION!");
            builder.setMessage("You have finished this level!")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do things
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            this.mCurrentQuestion = mMaxQuestion;
            mDatabase.open();
            mDatabase.updateCurrentQuestion(id, mMaxQuestion);
            mDatabase.close();
            return true;
        } else
            return false;
    }
}


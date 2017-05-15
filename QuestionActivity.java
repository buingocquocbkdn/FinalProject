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
import android.view.MenuItem;
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

    private TextView txtCoin, txtLevel;
    private Button[] btnResults, btnAnswers;
    private Button btnHome;

    private MyDatabase mDatabase;
    private int mCurrentQuestion;
    private int mMaxQuestion;
    private int mCoin;

    private int mId;
    private int mIdAnswer = 0;
    private int mIdQuestion = 0;
    private int mFlag = 0;
    private String mTmp = "";

    private final Context CONTEXT = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        txtLevel = (TextView) findViewById(R.id.txtlevel);
        txtCoin = (TextView) findViewById(R.id.txtcoin);
        btnHome =(Button) findViewById(R.id.btn_home);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Intent intent = new Intent(QuestionActivity.this, MainActivity.class);
        btnHome.setOnClickListener(new View.OnClickListener() {
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
        mId = packageIntent.getInt("number_Letter");

        mCurrentQuestion = getCurrentQuestion(mId);

        createImage(mId);
        createButtonChoose(mId);

        txtCoin.setText(mCoin + "");
        clickCoin(mCoin);
        levelInfor(mCurrentQuestion, mMaxQuestion);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
               Intent intent = new Intent(QuestionActivity.this, LetterActivity.class);
               startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    //Khởi tạo CSDL
    public void initListLetter() {
        int startID = Constant.START_NUMBER_LETTER;//Bắt đầu là 3 letters
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
                    Constant.QUESTIONS_3.length - 1,
                    Constant.QUESTIONS_4.length - 1,
                    Constant.QUESTIONS_5.length - 1,
                    Constant.QUESTIONS_6.length - 1,
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
        layout_image = (LinearLayout) findViewById(R.id.layout_image);
        ImageView iv = new ImageView(this);
        iv.setLayoutParams(new LinearLayout.LayoutParams(480, 500));
        if (!checkFinishId(id, mCurrentQuestion)) {
            switch (id) {
                case 3:
                    iv.setImageResource(Constant.QUESTIONS_3[mCurrentQuestion]);
                    break;
                case 4:
                    iv.setImageResource(Constant.QUESTIONS_4[mCurrentQuestion]);
                    break;
                case 5:
                    iv.setImageResource(Constant.QUESTIONS_5[mCurrentQuestion]);
                    break;
                case 6:
                    iv.setImageResource(Constant.QUESTIONS_6[mCurrentQuestion]);
            }

            layout_image.addView(iv);

            createButtonAnswer(id);
        }
    }

    //Tạo button điền kết quả
    public void createButtonAnswer(int id) {
        layout_answer = (LinearLayout) findViewById(R.id.layout_answer);
        int lengthButton = id;
        if (!checkFinishId(id, mCurrentQuestion)) {
            btnResults = new Button[lengthButton];

            for (mIdQuestion = 0; mIdQuestion < lengthButton; mIdQuestion++) {
                final Button btn = new Button(this);
                btn.setLayoutParams(new LinearLayout.LayoutParams(100, 100));


                btn.setId(mIdQuestion + 111);
                layout_answer.addView(btn);
                btnResults[mIdQuestion] = (Button) findViewById(btn.getId());
                btnResults[mIdQuestion].setEnabled(false);
                btnResults[mIdQuestion].setText("");

                btnResults[mIdQuestion].setTypeface(Typeface.createFromAsset(getAssets(),  "fonts/vnarialbold.ttf"));
                btnResults[mIdQuestion].setTextSize(25);

                btnResults[mIdQuestion].setBackgroundResource(R.drawable.btnasw1);

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < Constant.LENGTH_BUTTON_CHOOSE; i++) {
                            if (btnAnswers[i].getText() == "") {
                                btnAnswers[i].setText(btnResults[(Integer.parseInt(btn.getId() + "") - 111)].getText());
                                Log.e("ButtonAnswer ", btn.getId() + "");
                                btnAnswers[i].setVisibility(View.VISIBLE);
                                mFlag--;
                                btnResults[btn.getId() - 111].setText("");
                                btnResults[btn.getId() - 111].setBackgroundResource(R.drawable.btnasw1);
                                btnResults[btn.getId() - 111].setEnabled(false);
                                break;
                            }
                        }
                        if (mFlag < 0) mFlag = 0;
                        Log.e("Flag ", mFlag + "//");

                    }
                });
            }
        }
    }

    public void clickCoin(int iCoin) {

        this.mCoin = iCoin;
        if (mCurrentQuestion > mMaxQuestion) ;
        else {
            txtCoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCoin == 0)
                        Toast.makeText(QuestionActivity.this, "You are out of coins!", Toast.LENGTH_SHORT).show();
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(CONTEXT);
                        builder.setTitle("GOOD LUCK!");
                        builder.setMessage("The answer is: " + mTmp)
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

                        mDatabase.open();
                        mDatabase.updateCurrentQuestion(mId, mCurrentQuestion);
                        mDatabase.updateCoin(mId, mCoin);
                        mDatabase.close();

                        createButtonChoose(mId);
                        createImage(mId);
                        levelInfor(getCurrentQuestion(mId), mMaxQuestion);
                        txtCoin.setText(mCoin+"");
                    }
                }
            });
        }

    }

    public void levelInfor(int mCurrent, int mMax) {
        txtLevel.setText(mCurrent + " / " + mMax);
    }

    public void createButtonChoose(final int id) {
        layout_4 = (LinearLayout) findViewById(R.id.layout_4);
        layout_5 = (LinearLayout) findViewById(R.id.layout_5);

        btnAnswers = new Button[Constant.LENGTH_BUTTON_CHOOSE];

        if (!checkFinishId(id, mCurrentQuestion)) {
            switch (id) {
                case 3:
                    mTmp = Constant.ANSWER_3[mCurrentQuestion];
                    break;
                case 4:
                    mTmp = Constant.ANSWER_4[mCurrentQuestion];
                    break;
                case 5:
                    mTmp = Constant.ANSWER_5[mCurrentQuestion];
                    break;
                case 6:
                    mTmp = Constant.ANSWER_6[mCurrentQuestion];
                    break;
            }
            Log.e("Answer ", mTmp + " Letter:" + id);

            Random rd = new Random();
            String arr[] = new String[Constant.LENGTH_BUTTON_CHOOSE];
            for (int i = 0; i < mTmp.length(); i++) {
                boolean check = true;
                while (check) {
                    int rdd = rd.nextInt(Constant.LENGTH_BUTTON_CHOOSE);
                    if (arr[rdd] == null) {
                        arr[rdd] = mTmp.charAt(i) + "";
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
                Log.e("Random ", arr[i] + "");
            }

            createGroupButtonChoose(id,arr,layout_4,Constant.START_GROUP_BUTTON_CHOOSE_1,Constant.END_GROUP_BUTTON_CHOOSE_1);
            createGroupButtonChoose(id,arr,layout_5,Constant.START_GROUP_BUTTON_CHOOSE_2,Constant.END_GROUP_BUTTON_CHOOSE_2);
        }
    }

    public void createGroupButtonChoose(final int id, String arr[],LinearLayout linearLayout, int startIDAnswer, final int endIDAnswer){
        for (mIdAnswer = startIDAnswer; mIdAnswer < endIDAnswer; mIdAnswer++) {
            final Button btn = new Button(this);
            btn.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
            btn.setId(mIdAnswer);

            btn.setBackgroundResource(R.drawable.btnchoose);
            btn.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/vnarialbold.ttf"));
            btn.setTextSize(25);

            linearLayout.addView(btn);
            btnAnswers[mIdAnswer] = (Button) findViewById(btn.getId());
            btnAnswers[mIdAnswer].setText(arr[mIdAnswer] + "");
            //Log.e("NumberButtonIsChoosed", mFlag + "");
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mFlag == Constant.LENGTH_GROUP_BUTTON_CHOOSE) ;
                    else {
                        btnResults[mFlag].setTextColor(Color.WHITE);
                        btnResults[mFlag].setText(btnAnswers[btn.getId()].getText());
                        btnResults[mFlag].setEnabled(true);

                        mFlag++;

                        btnAnswers[btn.getId()].setText("");
                        btnAnswers[btn.getId()].setVisibility(View.INVISIBLE);

                        Log.e("NumberButtonIsChoosed", mFlag + "");
                        if (mFlag == id) {
                            String answer = "";

                            for (Button b : btnResults
                                    ) {
                                answer += b.getText();
                            }

                            if (answer.equals(mTmp)) {
                                mCoin++;

                                mDatabase.open();
                                mDatabase.updateCoin(id, mCoin);
                                mDatabase.close();

                                removeView();
                                btnResults = new Button[id];
                                btnAnswers = new Button[Constant.LENGTH_BUTTON_CHOOSE];
                                mIdQuestion = 0;
                                mIdAnswer = 0;

                                AlertDialog.Builder builder = new AlertDialog.Builder(CONTEXT);
                                builder.setTitle("CORRECT!");
                                builder.setMessage("YOU GOT IT \nAnswer: " + mTmp)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //do things
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();

                                removeView();
                                btnResults = new Button[id];
                                btnAnswers = new Button[Constant.LENGTH_BUTTON_CHOOSE];
                                mIdQuestion = 0;
                                mIdAnswer = 0;

                                mCurrentQuestion++;
                                if (!checkFinishId(id, mCurrentQuestion)) {
                                    mDatabase.open();
                                    mDatabase.updateCurrentQuestion(id, mCurrentQuestion);
                                    mDatabase.close();
                                    createImage(id);
                                    createButtonChoose(id);
                                    levelInfor(getCurrentQuestion(id), mMaxQuestion);
                                    txtCoin.setText(mCoin+"");
                                }
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(CONTEXT);
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
                                btnResults = new Button[id];
                                btnAnswers = new Button[Constant.LENGTH_BUTTON_CHOOSE];
                                mIdQuestion = 0;
                                mIdAnswer = 0;
                                createImage(id);
                                createButtonChoose(id);
                            }
                            mFlag = 0;
                        }
                    }
                }
            });
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
            AlertDialog.Builder builder = new AlertDialog.Builder(CONTEXT);
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


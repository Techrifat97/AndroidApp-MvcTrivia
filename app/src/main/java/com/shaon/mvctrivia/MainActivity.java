package com.shaon.mvctrivia;

import static android.provider.Telephony.BaseMmsColumns.MESSAGE_ID;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.shaon.mvctrivia.Data.QuestionBank;
import com.shaon.mvctrivia.Data.QuestionlistAsyncRespone;
import com.shaon.mvctrivia.Model.Question;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener  {
    private TextView question_textView,counter_textView,score_textview,high_textview;
    private Button btn_true, btn_false;
    private ImageButton next_btn, prev_btn;
    private int current_ques_index = 0;
    private List<Question> questionList;
    private int score =100;
    int value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        question_textView = findViewById(R.id.tv_question);
        counter_textView = findViewById(R.id.tv_2);
        btn_true = findViewById(R.id.true_btn);
        btn_false = findViewById(R.id.false_btn);
        next_btn = findViewById(R.id.next_btn);
        prev_btn = findViewById(R.id.prev_btn);
        score_textview = findViewById(R.id.tv_score);
        high_textview = findViewById(R.id.hs_tv);

        btn_true.setOnClickListener(this);
        btn_false.setOnClickListener(this);
        next_btn.setOnClickListener(this);
        prev_btn.setOnClickListener(this);

        questionList = new QuestionBank().getQuestions(new QuestionlistAsyncRespone() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {
                question_textView.setText(questionArrayList.get(current_ques_index).getQuestion());
                counter_textView.setText(current_ques_index + " out of " + String.valueOf(questionList.size()));
            }
        });
        SharedPreferences getShareData = getSharedPreferences(MESSAGE_ID,MODE_PRIVATE);
        value = getShareData.getInt("key",000);
        String val =String.valueOf(value);
        high_textview.setText("High score: "+val);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.prev_btn:
                if(current_ques_index == 0) {
                    current_ques_index = questionList.size();
                }

                if(current_ques_index >= 1){
                    current_ques_index --;
                }
                counter_textView.setText(current_ques_index+" out of "+String.valueOf(questionList.size()));
                updateQuestion();
                break;
            case R.id.next_btn:
                current_ques_index = (current_ques_index + 1) % questionList.size();
                counter_textView.setText(current_ques_index+" out of "+String.valueOf(questionList.size()));
                updateQuestion();
                break;
            case R.id.true_btn:
                checkAnswer(true);
                updateQuestion();

                break;
            case R.id.false_btn:
                checkAnswer(false);
                updateQuestion();
                break;
                
        }

    }
    private void updateQuestion(){
        String question = questionList.get(current_ques_index).getQuestion();
        question_textView.setText(question);
    }
    public void checkAnswer(boolean reply) {
        boolean answer_true_false = questionList.get(current_ques_index).isAnswer();
        if (reply == answer_true_false) {
            fadeView();
           /* Toast toast = Toast.makeText(MainActivity.this, "You are Correct!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.show(); */
            if(score > value){
                String val = String.valueOf(score);
                high_textview.setText("New high score: "+val);
            }
            showScore();
            current_ques_index ++;
            updateQuestion();
            counter_textView.setText(current_ques_index+" out of "+String.valueOf(questionList.size()));

        } else {
            shakeAnimation();
            /*Toast toast = Toast.makeText(MainActivity.this, "You are Wrong!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.show();*/
            if(score > value ){
                score -=100;
                saveData();
            }
            score = 000;
            showScore();
            current_ques_index = 0;
            updateQuestion();
            counter_textView.setText(current_ques_index+" out of "+String.valueOf(questionList.size()));

        }
    }
    private void fadeView(){
        CardView cardView = findViewById(R.id.cv_1);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.0f);
        alphaAnimation.setDuration(300);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        cardView.setAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void shakeAnimation () {
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this,
                R.anim.shake_animation);
        CardView card = findViewById(R.id.cv_1);
        card.setAnimation(shake);
        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                card.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                card.setCardBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
            });
        }
        private void showScore(){
            String valor = String.valueOf(score);
            score_textview.setText("Current Score: "+valor);
            score +=100;
        }
        private void saveData(){
            int data = score;
            SharedPreferences sharedPreferences = getSharedPreferences(MESSAGE_ID,MODE_PRIVATE);
            //we need to invoke shared preference editor to work
            SharedPreferences.Editor editor = sharedPreferences.edit();
           // editor.putint("message",data);
            editor.putInt("key",data);
            //to save data
            editor.apply();
        }

}

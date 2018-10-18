package com.khatthaphone.app.simplecalulator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    final int NONE = 0;
    final int PLUS = 1;
    final int MINUS = 2;
    final int MULTIPLY = 3;
    final int DIVIDE = 4;

    final String TAG = getClass().getSimpleName();

    Button btnNum1, btnNum2, btnNum3, btnNum4, btnNum5, btnNum6, btnNum7, btnNum8, btnNum9, btnNum0;
    Button btnOpDec, btnOpPlus, btnOpMinus, btnOpMultiply, btnOpDivide, btnOpClear, btnOpEqual;
    TextView tvDisplay, tvLastAnswer;

    int activeOperator = NONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

    }

    private void findViews() {

        tvDisplay = findViewById(R.id.tv_display);
        tvLastAnswer = findViewById(R.id.tv_last_answer);

        btnNum1 = findViewById(R.id.btn_num_1);
        btnNum2 = findViewById(R.id.btn_num_2);
        btnNum3 = findViewById(R.id.btn_num_3);
        btnNum4 = findViewById(R.id.btn_num_4);
        btnNum5 = findViewById(R.id.btn_num_5);
        btnNum6 = findViewById(R.id.btn_num_6);
        btnNum7 = findViewById(R.id.btn_num_7);
        btnNum8 = findViewById(R.id.btn_num_8);
        btnNum9 = findViewById(R.id.btn_num_9);
        btnNum0 = findViewById(R.id.btn_num_0);

        btnOpDec = findViewById(R.id.btn_op_decimal);
        btnOpPlus = findViewById(R.id.btn_op_plus);
        btnOpMinus = findViewById(R.id.btn_op_minus);
        btnOpMultiply = findViewById(R.id.btn_op_multiply);
        btnOpDivide = findViewById(R.id.btn_op_divide);
        btnOpClear = findViewById(R.id.btn_op_clear);
        btnOpEqual = findViewById(R.id.btn_op_equal);

        btnNum1.setOnClickListener(this);
        btnNum2.setOnClickListener(this);
        btnNum3.setOnClickListener(this);
        btnNum3.setOnClickListener(this);
        btnNum4.setOnClickListener(this);
        btnNum5.setOnClickListener(this);
        btnNum6.setOnClickListener(this);
        btnNum7.setOnClickListener(this);
        btnNum8.setOnClickListener(this);
        btnNum9.setOnClickListener(this);
        btnNum0.setOnClickListener(this);
        btnOpDec.setOnClickListener(this);
        btnOpPlus.setOnClickListener(this);
        btnOpMinus.setOnClickListener(this);
        btnOpMultiply.setOnClickListener(this);
        btnOpDivide.setOnClickListener(this);
        btnOpClear.setOnClickListener(this);
        btnOpEqual.setOnClickListener(this);

        btnOpClear.setOnLongClickListener(this);

    }

    @Override
    public void onClick(View v) {

        // All clickable views are buttons, no type check needed
        Button btn = (Button) v;

        // get String value from display and last answer (small) display
        String displayText = tvDisplay.getText().toString();
        String lastAnswerText = tvLastAnswer.getText().toString();

        // get Double value from strings obtained
        Double inputNum = 0.0;
        Double lastAnswerNum = 0.0;

        if (!displayText.isEmpty()) inputNum = Double.parseDouble(displayText);
        if (!lastAnswerText.isEmpty()) lastAnswerNum = Double.parseDouble(lastAnswerText);

        switch (btn.getId()) {

            case R.id.btn_op_decimal:

                if (!displayText.contains(".")) {
                    tvDisplay.setText(displayText + ".");
                }

                break;

            case R.id.btn_op_plus:

                showLastAnswerAndInput(displayText, lastAnswerText, inputNum, lastAnswerNum);
                activeOperator = PLUS;

                break;

            case R.id.btn_op_minus:

                showLastAnswerAndInput(displayText, lastAnswerText, inputNum, lastAnswerNum);
                activeOperator = MINUS;

                break;

            case R.id.btn_op_multiply:

                showLastAnswerAndInput(displayText, lastAnswerText, inputNum, lastAnswerNum);
                activeOperator = MULTIPLY;

                break;

            case R.id.btn_op_divide:

                showLastAnswerAndInput(displayText, lastAnswerText, inputNum, lastAnswerNum);
                activeOperator = DIVIDE;

                break;

            case R.id.btn_op_clear:

                if (!displayText.isEmpty() || !(displayText.length() == 0)) {
                    tvDisplay.setText(displayText.substring(0, displayText.length() - 1));

                    if (tvDisplay.getText().toString().isEmpty()) {
                        tvDisplay.setText("0");
                    }

                } else {
                    tvDisplay.setText("0");
                }

                // hide small display
                tvLastAnswer.setText("");
                tvLastAnswer.setVisibility(View.GONE);

                activeOperator = NONE;

                break;

            case R.id.btn_op_equal:

                // equal button only works when user already choose operator (small display is shown)
                if (!displayText.isEmpty() && !lastAnswerText.isEmpty()) {

                    double answer = calculate(lastAnswerNum, inputNum, activeOperator);

                    showAnswerAndCloseLastAnswer(answer);

                    // reset active operator
                    activeOperator = NONE;

                }

                break;


            default:
                // Other buttons should be digits
                // Extract digit from text inside the button, instead of redundantly defining cases for each button

                double answer = Double.parseDouble(displayText + btn.getText().toString());

                // if no decimal point, show as int (remove .0)
                if (!isDecimalNum(answer)) {
                    tvDisplay.setText("" + ((int) answer));
                } else {
                    tvDisplay.setText("" + answer);
                }

                break;

        }

    }

    private void showAnswerAndCloseLastAnswer(double answer) {

        // hide small display
        tvLastAnswer.setText("");
        tvLastAnswer.setVisibility(View.GONE);

        // if no decimal point, show as int (remove .0)
        if (!isDecimalNum(answer)) {
            tvDisplay.setText("" + ((int) answer));
        } else {
            tvDisplay.setText("" + answer);
        }
    }

    private void showLastAnswerAndInput(String displayText, String lastAnswerText, Double inputNum, Double lastAnswerNum) {
        if (!lastAnswerText.isEmpty()) {
            // if last answer is active
            double answer = calculate(lastAnswerNum, inputNum, activeOperator);

            showLastAnswerAndWaitForInput(answer);

        } else {
            // if no last answer (fist time using operator)
            // move input num to last answer
            tvLastAnswer.setVisibility(View.VISIBLE);
            tvLastAnswer.setText(displayText);
            tvDisplay.setText("");
        }
    }


    private void showLastAnswerAndWaitForInput(double answer) {

        tvDisplay.setText("0");

        // if no decimal point, show as int (remove .0)
        if (!isDecimalNum(answer)) {
            tvLastAnswer.setText("" + ((int) answer));
        } else {
            tvLastAnswer.setText("" + answer);
        }
    }

    private boolean isDecimalNum(double num) {
        if (num % 1 != 0) {
            return true;
        }
        return false;
    }

    private Double calculate(Double lastAnswerNum, Double inputNum, int activeOperator) {

        switch (activeOperator) {
            case PLUS:
                return lastAnswerNum + inputNum;
            case MINUS:
                return lastAnswerNum - inputNum;
            case MULTIPLY:
                return lastAnswerNum * inputNum;
            case DIVIDE:
                return lastAnswerNum / inputNum;
            default:
                return 0.0;
        }

    }


    @Override
    public boolean onLongClick(View v) {

        switch (v.getId()) {
            case R.id.btn_op_clear:

                tvDisplay.setText("0");

                // hide small display
                tvLastAnswer.setText("");
                tvLastAnswer.setVisibility(View.GONE);

                activeOperator = NONE;

                break;
            default:

                break;
        }

        return true;
    }
}

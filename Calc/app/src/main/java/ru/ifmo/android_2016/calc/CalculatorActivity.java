package ru.ifmo.android_2016.calc;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by alexey.nikitin on 13.09.16.
 */

public final class CalculatorActivity extends Activity {
    private String state;
    private TextView screen;
    private Double result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        screen = (TextView) findViewById(R.id.result);
        if (savedInstanceState != null) {
            state = savedInstanceState.getString("state");
            result = savedInstanceState.getDouble("result");
        }
        screen.setText(state);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("state", state);
        savedInstanceState.putString("result", result.toString());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState == null)
            return;
        if (state != "")
            screen.setText(savedInstanceState.getString("state"));
        else
            screen.setText(savedInstanceState.getString("result"));
    }

    public void clearClick(View view) {
        screen.setText("0");
        state = "";
    }

    public void click(View view) {
        state = state + ((TextView) view).getText().toString();
        screen.setText(state);
    }

    public void eqvClick(View view) {
        result = count(state);
        state = result.toString();
        screen.setText(state);
    }

    private Double count(String s) {
        int tmp = s.length(), i = 0;
        if (tmp == 0 || tmp > 20)
            return 0.0;
        for (; i < tmp; i++) {
            if (s.charAt(i) == '-' || s.charAt(i) == '+') {
                break;
            }
        }

        if (i != tmp) {
            String left = "", right = "";

            for (int j = 0; j < i; j++) {
                left = left + s.charAt(j);
            }

            for (int j = i + 1; j < tmp; j++) {
                right = right + s.charAt(j);
            }

            Double l = count(left), r = count(right);

            if (s.charAt(i) == '-') {
                return l - r;
            } else {
                return l + r;
            }
        }

        for (i = 0; i < tmp; i++) {
            if (s.charAt(i) == '/' || s.charAt(i) == '*') {
                break;
            }
        }

        if (i != tmp) {
            String left = "", right = "";

            for (int j = 0; j < i; j++) {
                left = left + s.charAt(j);
            }

            for (int j = i + 1; j < tmp; j++) {
                right = right + s.charAt(j);
            }

            Double l = count(left), r = count(right);

            if (s.charAt(i) == '/') {
                if (r == 0) {
                    return 0.0;
                }
                return l / r;
            } else {
                return l * r;
            }
        }

        int j = -1;
        for (i = 0; i < tmp; i++) {
            if (s.charAt(i) == '.' && j == -1) {
                j = i;
            } else if (s.charAt(i) > '9' || s.charAt(i) < '0') {
                return 0.0;
            }
        }

        if (j == -1) {
            Double value = 1.0 * (s.charAt(0) - '0');
            for (i = 1; i < tmp; i++) {
                value = value * 10 + (s.charAt(i) - '0');
            }
            return value;
        } else {
            Double right = 1.0 * (s.charAt(tmp - 1) - '0');
            for (i = tmp - 2; i > j; i--) {
                right = right / 10 + (s.charAt(i) - '0');
            }
            if (j == 0) {
                return right;
            } else {
                Double left = 1.0 * (s.charAt(0) - '0');
                for (i = 1; i < j; i++) {
                    left = left * 10 + (s.charAt(i) - '0');
                }
                return left + right;
            }
        }
    }
}
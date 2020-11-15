package com.tlnk.loftmoney;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class BalanceView extends View {

    private float expenses = 54000;
    private float incomes = 7400;

    private Paint expensePaint = new Paint();
    private Paint incomePaint = new Paint();

    public BalanceView(Context context) {
        super(context);
        init();
    }

    public BalanceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BalanceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public BalanceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void update(float expenses, float incomes) {
        this.expenses = expenses;
        this.incomes = incomes;

        invalidate();
    }

    private void init() {
        expensePaint.setColor(ContextCompat.getColor(getContext(), R.color.colorExpense));
        incomePaint.setColor(ContextCompat.getColor(getContext(), R.color.colorIncome));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float total = expenses + incomes;

        float expenseAngle = 360f * expenses / total;
        float incomesAngle = 360f * incomes / total;

        int space = 15;
        int size = Math.min(getWidth(), getHeight()) - space * 2;
        int xMargin = (getWidth() - size) / 2;
        int yMargin = (getHeight() - size) / 2;

        canvas.drawArc(xMargin - space, yMargin,
                getWidth() - xMargin - space,
                getHeight() - yMargin, 180 - expenseAngle / 2,
                expenseAngle, true, expensePaint);

        canvas.drawArc(xMargin + space, yMargin,
                getWidth() - xMargin + space,
                getHeight() - yMargin, 360 - incomesAngle / 2,
                incomesAngle, true, incomePaint);
    }
}
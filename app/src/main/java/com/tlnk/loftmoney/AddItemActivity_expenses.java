package com.tlnk.loftmoney;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AddItemActivity_expenses extends AppCompatActivity {

private EditText editName;
private EditText editPrice;
private Button addButton;
private CompositeDisposable compositeDisposable = new CompositeDisposable();
private String mName;
private String mPrice;

    @Override
    protected void onResume() {
        super.onResume();
        configureButton();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_expenses);

        editName = (EditText) findViewById(R.id.edit_name);
        editPrice = (EditText) findViewById(R.id.edit_price);
        addButton = (Button) findViewById(R.id.add_button);

        editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editName.setTextColor(Color.parseColor("#7ed321"));
                editPrice.setTextColor(Color.parseColor("#7ed321"));
            }

            @Override
            public void afterTextChanged(Editable s) {
                mName = s.toString();
                checkEditTextHasText();
            }
        });

        editPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mPrice = s.toString();
                checkEditTextHasText();
            }
        });


        configureButton();


    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

    private void configureButton() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editName.getText().equals("") || editPrice.getText().equals("")) {
                    Toast.makeText(getApplicationContext(), getString(R.string.fill_items), Toast.LENGTH_LONG);
                    return;
                } else {
                        Intent intent = new Intent();
                        intent.putExtra("name", editName.getText().toString());
                        intent.putExtra("price", editPrice.getText().toString());
                        setResult(RESULT_OK, intent);
                        finish();

                        Disposable disposable = ((LoftApp) getApplication()).moneyApi.postMoney(
                                Integer.parseInt(editPrice.getText().toString()),
                                editName.getText().toString(),
                                "expense")
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action() {
                                    @Override
                                    public void run() throws Exception {
                                        Toast.makeText(getApplicationContext(), getString(R.string.syccess_add), Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        Toast.makeText(getApplicationContext(), throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                }
            }
        });
    }

    public void checkEditTextHasText() {
        addButton.setEnabled(!TextUtils.isEmpty(mName) && !TextUtils.isEmpty(mPrice));
    }

}

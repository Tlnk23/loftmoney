package com.tlnk.loftmoney;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.internal.$Gson$Types;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AddItemActivity extends AppCompatActivity {

private EditText editName;
private EditText editPrice;
private Button addButton;
private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onResume() {
        super.onResume();
        configureButton();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        editName = (EditText) findViewById(R.id.edit_name);;
        editPrice = (EditText) findViewById(R.id.edit_price); ;
        addButton = (Button) findViewById(R.id.add_button);;

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
                                "income")
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

}

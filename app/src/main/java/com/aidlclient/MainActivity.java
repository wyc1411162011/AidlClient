package com.aidlclient;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.aidlserver.Book;
import com.aidlserver.BookController;

import java.util.List;

public class MainActivity extends Activity {
    BookController bookController;
    private int number =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindListeners();
        bindService();
    }
    private void bindListeners(){
        findViewById(R.id.bt_get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bookController != null){
                    try{
                        List <Book>bookList=bookController.getBookList();
                        if(bookList !=null){
                            Log.e("tag","个数"+ bookList.size());
                            for(Book book:bookList){
                                Log.e("tag",book.getName());
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        findViewById(R.id.bt_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number ++;
                if(bookController != null){
                    try{
                        bookController.addBookInOut(new Book("client添加的"+number));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    private void bindService() {
        Intent intent = new Intent();
        intent.setPackage("com.aidlserver");
        intent.setAction("com.aidlserver.service");
        intent.addCategory("android.intent.category.DEFAULT");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
             bookController = BookController.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
}

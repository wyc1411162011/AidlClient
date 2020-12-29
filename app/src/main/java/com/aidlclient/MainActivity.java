package com.aidlclient;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import com.aidlserver.Book;
import com.aidlserver.BookController;
import com.aidlserver.IBinderPool;
import com.aidlserver.IComputer;
import com.aidlserver.ISecurityCenter;

import java.util.List;

public class MainActivity extends Activity {
    BookController bookController;
    private int number =0;
    IBinderPool binderPool;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindListeners();
        bindService();
        bindPoolService();
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
        findViewById(R.id.bt_computer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binderPool != null){
                    try {
                        IBinder iBinder = binderPool.queryBinder(0);
                            IComputer computer = IComputer.Stub.asInterface(iBinder);
                            int number = computer.add(1,2);
                            Log.e("tag","client端的add的结果"+number);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        findViewById(R.id.bt_securitycenter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binderPool != null){
                    try {
                        IBinder iBinder = binderPool.queryBinder(1);
                        ISecurityCenter securityCenter = ISecurityCenter.Stub.asInterface(iBinder);
                        String result = securityCenter.encrypt("");
                        Log.e("tag","client 端加密的结果  "+result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    private void bindPoolService() {
        Intent intent = new Intent();
        intent.setPackage("com.aidlserver");
        intent.setAction("com.aidlserver.poolservice");
        intent.addCategory("android.intent.category.DEFAULT");
        bindService(intent, poolConnection, Context.BIND_AUTO_CREATE);
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
            //把binder转成操作类
             bookController = BookController.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
    private ServiceConnection poolConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //把binder转成操作类
            binderPool = IBinderPool.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };


}

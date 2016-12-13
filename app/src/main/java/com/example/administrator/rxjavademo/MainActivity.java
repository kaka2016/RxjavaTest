package com.example.administrator.rxjavademo;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Rxjava  从入门到出轨
 * http://blog.csdn.net/yyh352091626/article/details/53304728
 * <p>
 * 作用  ：异步
 * 核心  Observable  被观察者   Subscribler 观察者
 * Observable 发布事件  可以是任何东西
 * Subscribler  回调处理
 * <p>
 * 流程：Observable -> Operator 1 -> Operator 2 -> Operator 3 -> Subscriber
 * 1、Observable 发出一系列事件，他是事件的产生者；
 * 2、Subscriber 负责处理事件，他是事件的消费者；
 * 3、Operator 是对 Observable 发出的事件进行修改和变换；
 * 4、若事件从产生到消费不需要其他处理，则可以省略掉中间的 Operator，从而流程变为 Obsevable -> Subscriber；
 * 5、Subscriber 通常在主线程执行，所以原则上不要去处理太多的事务，而这些复杂的事务处理则交给 Operator；
 */

public class MainActivity extends Activity {

    @InjectView(R.id.iv_image)
    ImageView ivImage;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        textView = (TextView) findViewById(R.id.test);


      /*   基础用法
      Observable<String> mobservable = Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                // 发送一个 Hello World 事件
                subscriber.onNext("ni hao");

                // 事件发送完成
                subscriber.onCompleted();
            }
        });*/

        // 创建一个Observer
       /* Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.i("kkkk", "complete");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.i("kkkk", s);
                textView.setText(s);

            }
        };*/

       //简洁用法
 /*    Observable.just("ni mei a ","aaaa").subscribe(new Action1<String>() {
         @Override
         public void call(String s) {
             textView.setText(s);
         }
     });*/

        //mobservable.subscribe(observer);

       /* Observable.create(new Observable.OnSubscribe<Drawable>(){
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                //取一张图作为drawable
                Drawable drawable = ContextCompat.getDrawable(MainActivity.this,R.drawable.ic_launcher1);
                subscriber.onNext(drawable);
                subscriber.onCompleted();
            }
        }).subscribe(new Subscriber<Drawable>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Drawable drawable) {
                ivImage.setImageDrawable(drawable);
            }
        });*/

       //操作符 Operators
        /**
         * 操作符就是为了解决对Observable对象的变换问题 ，操作符用于在Obervable和最终的Subscriber之间修改Observable发出的事件
         * map操作符 ---用来把一个事件转换成另一个事件
         */
       /* Observable.just("AAAAA").map(new Func1<String, Integer>() {
            @Override
            public Integer call(String s) {
                return 1111;
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer s) {
                textView.setText(Integer.toString(s));
            }
        });*/

        /**
         * 从网络上加载一张图片
         */
        /*Observable.create(new Observable.OnSubscribe<Drawable>() {
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                try {
                    Drawable drawable = Drawable.createFromStream(new URL("http://g.hiphotos.baidu.com/baike/c0%3Dbaike272%2C5%2C5%2C272%2C90/sign=49edc1c60246f21fdd395601974d0005/3b87e950352ac65c8e1fe94ef3f2b21193138af1.jpg").openStream(), "src");
                    subscriber.onNext(drawable);
                    Log.i("KKK","drawable"+drawable);
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())//指定subscribe()所在的线程
        .observeOn(AndroidSchedulers.mainThread())//指定回调方法所在的线程
        .subscribe(new Subscriber<Drawable>() {
            @Override
            public void onCompleted() {
                Log.i("kkk","SUCCESS");

            }

            @Override
            public void onError(Throwable e) {
                Log.i("kkk","fail");
            }
            @Override
            public void onNext(Drawable drawable) {
                ivImage.setImageDrawable(drawable);
                Log.i("kkk","ok");
            }
        });*/

        /**
         * 变换map操作符
         * Observable 创建了一个 String 事件，也就是产生一个url，
         * 通过 map 操作符进行变换，返回Drawable对象，
         * 这个变换指的就是通过url进行网络图片请求，返回一个Drawable。
         * 所以简单的来说就是把String事件，转换为Drawable事件。
         *改动一点点。。。。。。
         */
        Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("http://g.hiphotos.baidu.com/baike/c0%3Dbaike272%2C5%2C5%2C272%2C90/sign=49edc1c60246f21fdd395601974d0005/3b87e950352ac65c8e1fe94ef3f2b21193138af1.jpg");
            }
        }).map(new Func1<String, Drawable>() {
            @Override
            public Drawable call(String s) {
                try {
                    Drawable drawable = Drawable.createFromStream(new URL(s).openStream(),"src");
                    return drawable;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return  null;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Drawable>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("kkk","aa"+e);
            }

            @Override
            public void onNext(Drawable drawable) {
                if (null!=drawable){
                    ivImage.setImageDrawable(drawable);
                }
            }
        });



    }



}





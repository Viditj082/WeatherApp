package com.example.weather;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Singleton {
        private static Singleton mySingleTon;
        private RequestQueue requestQueue;
        private static Context mctx;

        private Singleton(Context context){
            this.mctx=context;
            this.requestQueue=getRequestQueue();

        }
        public RequestQueue getRequestQueue(){
            if (requestQueue==null){
                requestQueue= Volley.newRequestQueue(mctx.getApplicationContext());
            }
            return requestQueue;
        }
        public static synchronized Singleton getInstance(Context context){
            if (mySingleTon==null){
                mySingleTon=new Singleton(context);
            }
            return mySingleTon;
        }
        public<T> void addToRequestQue(Request<T> request){
            requestQueue.add(request);

        }
    }


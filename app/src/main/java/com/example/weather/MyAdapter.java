package com.example.weather;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {


    private Context context;

    private ArrayList<Forecast_model>list;



    public MyAdapter(Context context,ArrayList<Forecast_model>wtlist) {

    this.context=context;
    this.list=wtlist;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

       View view=LayoutInflater.from(context).inflate(R.layout.forecase_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Forecast_model model=list.get(position);
        SimpleDateFormat input=new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat output=new SimpleDateFormat("hh:mm aa");
        try
        {
            Date t=input.parse(model.getTime());
            holder.time.setText(output.format(t));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
      holder.temp.setText(model.getTemperature()+"°");
       Picasso.get().load("http:".concat(model.getIcon())).into(holder.conditionIV);

//      holder.relativeLayout.setBackground(context.getResources().getDrawable(background, context.getTheme()));

        holder.cardview.setCardBackgroundColor(Color.TRANSPARENT);
        holder.cardview.setCardElevation(0);
      holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Toast.makeText(context,list.get(position).getExpected(),Toast.LENGTH_SHORT).show();
          }
      });

    }



    @Override
    public int getItemCount() {
        return list.size();
    }


     //Vieholder class

    public   class ViewHolder extends  RecyclerView.ViewHolder{

        private TextView time,temp;

        private ImageView conditionIV;

        private CardView cardview;
        private RelativeLayout relativeLayout;
        public ViewHolder(@NonNull View itemView) {
           super(itemView);

           time=itemView.findViewById(R.id.hour_time);
           temp=itemView.findViewById(R.id.hour_temp);
           conditionIV=itemView.findViewById(R.id.hour_icon);

           relativeLayout=itemView.findViewById(R.id.rlayout);

           cardview=itemView.findViewById(R.id.cardview);

        }
    }
}

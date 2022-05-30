package com.senarios.coneqtliveviewer.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.gson.JsonParseException;
import com.senarios.coneqtliveviewer.Model.Datum;
import com.senarios.coneqtliveviewer.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class OverViewAdapter extends RecyclerView.Adapter<OverViewAdapter.ViewHolder> {
    private ArrayList<Datum> arrayList = new ArrayList<>();
    private Context context;
    String dateTime;
    Calendar calendar;
    String EventTime;
    String conv;
    SimpleDateFormat simpleDateFormat;
    int hours;
    String totalSecs;
    int minutes;
    String formattedDate;
    PurchaseTicket purchaseTicket;

    public OverViewAdapter(ArrayList<Datum> arrayList, Context context, PurchaseTicket purchaseTicket) {
        this.arrayList = arrayList;
        this.context = context;
        this.purchaseTicket = purchaseTicket;
    }

    public interface PurchaseTicket {
        void doPurchase(String id , int ticketPrice, boolean purchase);
    }

    @NonNull
    @Override
    public OverViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.overview_itemlist, parent, false);
        return new OverViewAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.idEventHistoryName.setText(arrayList.get(position).getName());
        holder.idTravel.setText(arrayList.get(position).getType());
        dateTime = String.valueOf(arrayList.get(position).getTime());
        formattedDate = "";
        SimpleDateFormat spf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date newDate= null;
        try {
            newDate = spf.parse(dateTime);
            holder.idEventHistoryTime.setText("Time: " + getFormattedDate(newDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.idEventHistoryDescription.setText(arrayList.get(position).getDescription());
       try {
           Long time = Long.parseLong(arrayList.get(position).getTimeDuration().toString()) / 60;
           Long mints, hours;

           hours = TimeUnit.MINUTES.toHours(Long.valueOf(time));
           mints = time - TimeUnit.HOURS.toMinutes(hours);
           if (hours<= 0 && mints>=1) {
               holder.idEventHistoryTimeDuration.setText(" | " +mints + "min");
           } else if (mints <= 0 && hours>=1) {
               holder.idEventHistoryTimeDuration.setText(" | " + hours + "h ");
           } else if (mints <= 0 && hours>=1) {
               holder.idEventHistoryTimeDuration.setText(" | " + hours + "h ");
           } else {
               holder.idEventHistoryTimeDuration.setText(" | " + hours + "h "+mints + "min ");
           }

       } catch (JsonParseException e) {
           e.printStackTrace();
       }

        Picasso.with(holder.itemView.getContext())
                .load(arrayList.get(position).getImage1S3())
                .centerCrop()
                .placeholder(R.drawable.img)
                .resize(1800, 1800)
                .into(holder.itemImage);
        holder.itemImage.setVisibility(View.VISIBLE);

        holder.idEventHistoryCost.setText("£" + arrayList.get(position).getTicketPrice());

        holder.creatorTxt.setText(arrayList.get(position).getContentCreator());

        holder.soldTxt.setText(arrayList.get(position).getTotalTicketPurchased().toString());
        if(arrayList.get(position).getStatus().equalsIgnoreCase("pending")){
            holder.btnPurchase.setOnClickListener(v -> {
                int amount = (int) (arrayList.get(position).getTicketPrice()*100);
                purchaseTicket.doPurchase(String.valueOf(arrayList.get(position).getId()),amount,true);
            });
        } else {
            holder.btnPurchase.setText("JOIN EVENT");
            holder.btnPurchase.setOnClickListener(v -> {
                int amount = (int) (arrayList.get(position).getTicketPrice()*100);
                purchaseTicket.doPurchase(String.valueOf(arrayList.get(position).getId()),amount,false);
            });
        }
        holder.ratingTxt.setText(("(" +  arrayList.get(position).getAvgRating() + ")"));
        holder.ratingBar.setRating((float) arrayList.get(position).getAvgRating());

        /////////////////
        EventTime = String.valueOf(arrayList.get(position).getTime());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = new Date(System.currentTimeMillis());
        Date date2 = null;
        try {
            date2 = df.parse(EventTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = date2.getTime() - date1.getTime();
        if(diff>0) {
            new CountDownTimer(diff, 1000) {
                public void onTick(long millisUntilFinished) {
                    long seconds = millisUntilFinished / 1000;
                    long minutes = seconds / 60;
                    long hours = minutes / 60;
                    holder.durationTimer.setText(String.format("%02d:%02d:%02d ", hours %24 , minutes%60, seconds%60));

                }
                public void onFinish() {
                }
            }.start();
            Log.wtf("message", String.valueOf(diff));
        } else {
            holder.durationTimer.setText("00:00:00");
        }
        /////////////
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView idEventHistoryName, idEventHistoryTime, idEventHistoryDescription, idEventHistoryCost,
                btnPurchase, idEventHistoryTimeDuration , idTravel , soldTxt , creatorTxt , durationTimer,
                ratingTxt,number, viewerTxt;
        private ImageView itemImage;
        private RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            idEventHistoryName = itemView.findViewById(R.id.idEventHistoryName);
            idEventHistoryTime = itemView.findViewById(R.id.idEventHistoryTime);
            idEventHistoryDescription = itemView.findViewById(R.id.idEventHistoryDescription);
            idEventHistoryCost = itemView.findViewById(R.id.idEventHistoryCost);
            btnPurchase = itemView.findViewById(R.id.btnPurchase);
            idEventHistoryTimeDuration = itemView.findViewById(R.id.idEventHistoryTimeDuration);
            itemImage = itemView.findViewById(R.id.idimage);
            idTravel = itemView.findViewById(R.id.idTravel);
            soldTxt = itemView.findViewById(R.id.idSoldTicketTxt);
            creatorTxt = itemView.findViewById(R.id.creatorName);
            durationTimer = itemView.findViewById(R.id.durationTimer);
            ratingBar = itemView.findViewById(R.id.rating);
            ratingTxt = itemView.findViewById(R.id.ratingTxt);
            number = itemView.findViewById(R.id.number);
            number.setVisibility(View.INVISIBLE);
            viewerTxt = itemView.findViewById(R.id.viewerTxt);
            viewerTxt.setVisibility(View.INVISIBLE);
        }
    }
    private String getFormattedDate(Date date)  {
        Calendar cal = Calendar.getInstance();
        Date currentTime = Calendar.getInstance().getTime();
        cal.setTime(date);
        //2nd of march 2015
        int day = cal.get(Calendar.DATE);

        switch (day % 10) {
            case 1:
                return new SimpleDateFormat( "hh:mm aa d'st' MMMM yyyy").format(date);
            case 2:
                return new SimpleDateFormat( "hh:mm aa d'nd' MMMM yyyy").format(date);
            case 3:
                return new SimpleDateFormat( "hh:mm aa d'rd' MMMM yyyy").format(date);
            default:
                return new SimpleDateFormat("hh:mm aa d'th' MMMM yyyy").format(date);
        }

    }
}

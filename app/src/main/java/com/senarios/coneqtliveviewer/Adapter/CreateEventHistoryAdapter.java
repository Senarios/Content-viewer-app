package com.senarios.coneqtliveviewer.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonParseException;
import com.senarios.coneqtliveviewer.Model.Upcoming;
import com.senarios.coneqtliveviewer.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class CreateEventHistoryAdapter extends RecyclerView.Adapter<CreateEventHistoryAdapter.ViewHolder> {
    private List<Upcoming> arrayList;
    private Context context;
    String dateTime;
    String EventTime;
    String conv;
    Calendar calendar;
    int counter;
    SimpleDateFormat simpleDateFormat;
    int hours;
    String totalSecs;
    int minutes;
    String formattedDate;
    private StartEvent startEvent;

    public CreateEventHistoryAdapter(List<Upcoming> arrayList, Context context, StartEvent startEvent) {
        this.arrayList = arrayList;
        this.context = context;
        this.startEvent = startEvent;
    }

    public interface StartEvent {
        void doStart(String id);

        void doCancel(String Id, String name);
    }


    @NonNull
    @Override
    public CreateEventHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.create_event_list_iteview, parent, false);
        return new CreateEventHistoryAdapter.ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.idEventHistoryName.setText(arrayList.get(position).getEvents().getName());

        SharedPreferences.Editor editor = context.getSharedPreferences("my", MODE_PRIVATE).edit();
        editor.putString("namePerson", arrayList.get(position).getEvents().getName());
        editor.apply();

        holder.idTravel.setText(arrayList.get(position).getEvents().getType());
        holder.soldTxt.setText(arrayList.get(position).getEvents().getTotalTicketPurchased().toString());
        /////////////
        dateTime = String.valueOf(arrayList.get(position).getEvents().getTime());
        formattedDate = "";
        SimpleDateFormat spf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date newDate= null;
        try {
            newDate = spf.parse(dateTime);
            holder.idEventHistoryTime.setText("Time: " + getFormattedDate(newDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /////////////////
        EventTime = String.valueOf(arrayList.get(position).getEvents().getTime());
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

        holder.idEventHistoryDescription.setText(arrayList.get(position).getEvents().getDescription());
        editor.putString("nameDes", arrayList.get(position).getEvents().getDescription());
        editor.apply();


        try {
            Long time = Long.parseLong(arrayList.get(position).getEvents().getTimeDuration()) / 60;
            Long mints, hours;

            hours = TimeUnit.MINUTES.toHours(Long.valueOf(time));
            mints = time - TimeUnit.HOURS.toMinutes(hours);
            if (hours<= 0 && mints>=1) {
                holder.idEventHistoryTimeDuration.setText(" | " + mints + "min");
            } else if (mints <= 0 && hours>=1) {
                holder.idEventHistoryTimeDuration.setText(" | " + hours + "h");
            } else if (mints <= 0 && hours>=1) {
                holder.idEventHistoryTimeDuration.setText(" | " + hours + "h");
            } else {
                holder.idEventHistoryTimeDuration.setText(" | " + hours + "h "+mints + "min");
            }

        } catch (JsonParseException e) {
            e.printStackTrace();
        }
        holder.idEventHistoryCost.setText("£" + arrayList.get(position).getEvents().getTicketPrice());

        holder.creatorTxt.setText(arrayList.get(position).getEvents().getContentCreator());

        Picasso.with(holder.itemView.getContext())
                .load(arrayList.get(position).getEvents().getImage1S3())
                .centerCrop()
                .placeholder(R.drawable.img)
                .resize(1800, 1800)
                .into(holder.itemImage);
        holder.itemImage.setVisibility(View.VISIBLE);

        holder.joinTxtView.setOnClickListener(v -> {
            startEvent.doStart(String.valueOf(arrayList.get(position).getEvents().getId()));

            editor.putInt("getEventsId", arrayList.get(position).getEvents().getId());
            editor.apply();

        });
        holder.cancelTxtView.setOnClickListener(v -> {
            startEvent.doCancel(arrayList.get(position).getEvents().getStripePaymentId(), arrayList.get(position).getEvents().getName());
        });

        holder.ratingTxt.setText(("(" +  arrayList.get(position).getEvents().getAvgRating() + ")"));
        holder.ratingBar.setRating((float) arrayList.get(position).getEvents().getAvgRating());
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
        private TextView idEventHistoryName, idEventHistoryTime, idEventHistoryDescription, idEventHistoryCost, idEventHistoryTimeDuration,
                joinTxtView, cancelTxtView ,idTravel , soldTxt , creatorTxt , durationTimer,
                ratingTxt,number, viewerTxt ;
        private ImageView imgShare, itemImage ;
        private RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            idEventHistoryName = itemView.findViewById(R.id.idEventHistoryName);
            idEventHistoryTime = itemView.findViewById(R.id.idEventHistoryTime);
            idEventHistoryDescription = itemView.findViewById(R.id.idEventHistoryDescription);
            idEventHistoryCost = itemView.findViewById(R.id.idEventHistoryCost);
            idEventHistoryTimeDuration = itemView.findViewById(R.id.idEventHistoryTimeDuration);
            joinTxtView = itemView.findViewById(R.id.idJoinTxt);
            cancelTxtView = itemView.findViewById(R.id.idCancelBtn);
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


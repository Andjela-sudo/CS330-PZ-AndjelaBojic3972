package com.pz.adapters;

import static androidx.core.app.ActivityOptionsCompat.makeSceneTransitionAnimation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.pz.R;
import com.pz.activities.ListDetail_Activity;
import com.pz.restapi.APIUtils;
import com.pz.restapi.services.ListService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ListsListAdapter extends RecyclerView.Adapter<ListsListAdapter.ViewHolder> {
    public Activity mActivity;
    List<com.pz.restapi.models.List> lists;

    public ListsListAdapter(Activity activity, List<com.pz.restapi.models.List> data) {
        this.mActivity = activity;
        this.lists = data;
    }
    @NonNull
    @Override
    public ListsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.class_adapter, viewGroup, false);
        return new ViewHolder(itemView, mActivity, lists);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ListsListAdapter.ViewHolder viewHolder, int i) {
        final com.pz.restapi.models.List temp = lists.get(i);
        System.out.println("TEMP LISTSLIST"+temp);
        viewHolder.list_name.setText(temp.getName());
        if(temp.getItems() != null){
            viewHolder.total_items.setText(temp.getItems().size()+"");
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDateTime now = LocalDateTime.now();
        viewHolder.date_created.setText(dtf.format(now));

    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView list_name;
        public final TextView total_items;
        public final TextView date_created;
        public RelativeLayout frameLayout;
        public CardView cardView;

        public Activity mActivity;
        List<com.pz.restapi.models.List> lists;

        ViewHolder(View itemView, Activity activity,List<com.pz.restapi.models.List> lists) {
            super(itemView);
            list_name = itemView.findViewById(R.id.className_adapter);
            frameLayout = itemView.findViewById(R.id.frame_bg);
            cardView = itemView.findViewById(R.id.cardView_adapter);
            total_items = itemView.findViewById(R.id.totalStudents_adapter);
            date_created = itemView.findViewById(R.id.date_list_created_adapter);

            mActivity = activity;
            this.lists = lists;

            FrameLayout linear_layout = itemView.findViewById(R.id.layout_list_adapter);


            linear_layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    System.out.println("DELETE LIST CLICKED");
                    ListService listService;
                    listService = APIUtils.getListService();
                    SharedPreferences sharedPref = v.getContext().getSharedPreferences("AuthPreferences", Context.MODE_PRIVATE);
                    String token = sharedPref.getString("authToken","");
                    Call<ResponseBody> call = listService.deleteList(lists.get(getAdapterPosition()).getId(),token);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(response.isSuccessful()){
                                System.out.println("------list deleted-------");
                                System.out.println(response.body());
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("ERROR: ", t.getMessage());
                        }
                    });

                    return false;
                }
            });


            linear_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), ListDetail_Activity.class);
                    intent.putExtra("listName", lists.get(getAdapterPosition()).getName());
                    intent.putExtra("listId", lists.get(getAdapterPosition()).getId()+"");
                    Pair<View, String> p1 = Pair.create((View) cardView, "ExampleTransition");
                    ActivityOptionsCompat optionsCompat = makeSceneTransitionAnimation(mActivity,p1);
                    view.getContext().startActivity(intent, optionsCompat.toBundle());
                }
            });
        }

        @Override
        public void onClick(View view) {

        }
    }
}

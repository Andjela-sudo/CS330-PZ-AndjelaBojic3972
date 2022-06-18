package com.pz.adapters;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.pz.BottomSheet.Item_Edit_Sheet;
import com.pz.R;
import com.pz.restapi.APIUtils;
import com.pz.restapi.models.Category;
import com.pz.restapi.models.Item;
import com.pz.restapi.services.CategoryService;

import java.sql.SQLOutput;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemsListAdapter extends RecyclerView.Adapter<ItemsListAdapter.ViewHolder>{

    private final Activity mActivity;
    List<Item> mList;
    String list_name;

    public ItemsListAdapter(List<Item> mList, Activity mActivity, String list_name ) {
        System.out.println("ITEMS LIST ADAPTER");
        this.mActivity = mActivity;
        this.mList = mList;
        this.list_name = list_name;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_attendance_adapter, parent, false);
        return new ViewHolder(itemView, mActivity, mList);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item temp = mList.get(position);
        System.out.println("ITEM TEMP " + temp);
        holder.materialName.setText(temp.getMaterial().getTitle());
        holder.kolicina.setText("x" + temp.getKolicina());
        holder.cenaItem.setText(temp.getKolicina() * temp.getMaterial().getPrice() + " din");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        CategoryService categoryService;
        categoryService = APIUtils.getCategoryService();
        Call<Category> call = categoryService.getCategoryById(temp.getMaterial().getCategoryId());
        call.enqueue(new Callback<Category>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                if (response.isSuccessful()) {
                    holder.categoryName.setText(response.body().getName());
                }
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public Activity mActivity;
        List<Item> mList;

        TextView materialName;
        TextView categoryName;
        TextView kolicina;
        TextView cenaItem;
        public LinearLayout layout;

        public ViewHolder(@NonNull View itemView, Activity activity, List<Item> itemList) {
            super(itemView);
            this.materialName = itemView.findViewById(R.id.item_name_adapter);
            this.kolicina =  itemView.findViewById(R.id.item_kolicina_adapter);
            this.categoryName = itemView.findViewById(R.id.item_category_adapter);
            this.cenaItem = itemView.findViewById(R.id.item_cena_adapter);
            layout = itemView.findViewById(R.id.layout_click);
            this.mActivity = activity;
            this.mList = itemList;

            layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Item item = mList.get(getAdapterPosition());
                    System.out.println(item.getId() + "");
                    System.out.println(item.getMaterial().getTitle());
                    Item_Edit_Sheet item_edit_sheet = new Item_Edit_Sheet(item, list_name);
                    item_edit_sheet.setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetTheme);
                    item_edit_sheet.show(((FragmentActivity) view.getContext()).getSupportFragmentManager(), "BottomSheet");
                    return false;
                }
            });

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("sholud be implemented item details view");
                }
            });
        }

        @Override
        public void onClick(View view) {

        }
    }
}

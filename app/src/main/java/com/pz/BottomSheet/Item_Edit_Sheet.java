package com.pz.BottomSheet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pz.R;
import com.pz.activities.HomeActivity;
import com.pz.activities.ListDetail_Activity;
import com.pz.activities.MainActivity;
import com.pz.restapi.APIUtils;
import com.pz.restapi.models.Item;
import com.pz.restapi.models.Material;
import com.pz.restapi.models.User;
import com.pz.restapi.services.ListService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Item_Edit_Sheet extends BottomSheetDialogFragment {

    public Item _item;
    public EditText name_item, kolicina_item;
    public CardView call;
    public TextView btnSave;
    ListService listService;
    public String list_name;

    public Item_Edit_Sheet(Item item, String list_name) {
        _item = item;
        this.list_name = list_name;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.bottomsheet_student_edit, container, false);

        name_item = v.findViewById(R.id.stu_name_edit);
        kolicina_item = v.findViewById(R.id.stu_kolicina_edit);

        name_item.setText(_item.getMaterial().getTitle());
        kolicina_item.setText(_item.getKolicina()+"");

        btnSave = v.findViewById(R.id.save_edited_item_btn);

        APIUtils.getListService();
        listService = APIUtils.getListService();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("SAVE CLICKED");
                Item newItem = new Item(_item.getId(), _item.getListId(), _item.getMaterial(), Integer.parseInt(kolicina_item.getText().toString()));
                System.out.println(newItem);
                SharedPreferences sharedPref = getContext().getSharedPreferences("AuthPreferences",Context.MODE_PRIVATE);
                String token = sharedPref.getString("authToken","");
                System.out.println(token);

                Call<Item> call = listService.updateListItem(newItem, _item.getId(),token);
                call.enqueue(new Callback<Item>() {
                    @Override
                    public void onResponse(Call<Item> call, Response<Item> response) {
                        if(response.isSuccessful()){
                            Item changedItem = response.body();
                            System.out.println(changedItem);

                            Intent intent = new Intent(getActivity(), ListDetail_Activity.class);
                            intent.putExtra("listId", newItem.getListId()+"");
                            intent.putExtra("listName", list_name);
                            startActivity(intent);
                            getActivity().finish();
                        }
                        dismiss();
                    }

                    @Override
                    public void onFailure(Call<Item> call, Throwable t) {
                        Log.e("ERROR: ", t.getMessage());
                    }
                });

            }
        });

        return v;
    }
}

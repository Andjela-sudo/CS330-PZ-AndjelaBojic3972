package com.pz.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.pz.R;
import com.pz.adapters.ItemsListAdapter;
import com.pz.restapi.APIUtils;
import com.pz.restapi.models.Item;
import com.pz.restapi.models.List;
import com.pz.restapi.models.Material;
import com.pz.restapi.services.ListService;
import com.pz.restapi.services.MaterialService;
import com.pz.restapi.services.UserService;
import com.yarolegovich.lovelydialog.LovelyCustomDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListDetail_Activity extends AppCompatActivity {

    private TextView createdBy, total_price, place_holder;
    private CardView addItem, goToCategories;
    private EditText item_name, item_category ,item_kolicina;
    private EditText reg_no;
    AutoCompleteTextView material_name;
    private LinearLayout layout_attendance_taken;
    private RecyclerView mRecyclerview;
    java.util.List<String> allMaterials ;
    java.util.List<Material> materials;

    private Handler handler = new Handler();
    ItemsListAdapter mAdapter;

    ProgressBar progressBar;
    Dialog lovelyCustomDialog;

    String list_Name, list_Id;
    String kolicina = "1";

    List list;

    UserService userService;
    ListService listService;
    MaterialService materialService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_detail_);
        getWindow().setExitTransition(null);

        list_Name = getIntent().getStringExtra("listName");
        list_Id = getIntent().getStringExtra("listId");

        if(list_Id == null){
            list_Id = "0";
        }

        Toolbar toolbar = findViewById(R.id.toolbar_class_detail);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_disease_detail);
        collapsingToolbarLayout.setTitle(list_Name);

        userService = APIUtils.getUserService();
        listService = APIUtils.getListService();
        materialService = APIUtils.getMaterialService();

        createdBy = findViewById(R.id.classname_detail);
        total_price = findViewById(R.id.total_students_detail);
        addItem = findViewById(R.id.add_students);
        goToCategories = findViewById(R.id.reports_open_btn);

        getListById(list_Id);

        mRecyclerview = findViewById(R.id.recyclerView_detail);
        progressBar = findViewById(R.id.progressbar_detail);
        place_holder = findViewById(R.id.placeholder_detail);
        place_holder.setVisibility(View.GONE);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                System.out.println("RUNNABLE ITEMS INIT");
                itemsInit();
                progressBar.setVisibility(View.GONE);
            }
        };
        handler.postDelayed(r, 500);

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = LayoutInflater.from(ListDetail_Activity.this);
                final View view1 = inflater.inflate(R.layout.popup_add_student, null);
                material_name = (AutoCompleteTextView) view1.findViewById(R.id.name_student_popup);
                reg_no = view1.findViewById(R.id.regNo_student_popup);



                Call<java.util.List<Material>> call = materialService.getMaterials();
                call.enqueue(new Callback<java.util.List<Material>>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(Call<java.util.List<Material>> call, Response<java.util.List<Material>> response) {
                        if(response.isSuccessful()){
                            materials = response.body();
                            allMaterials = materials.stream().map(material -> material.getTitle()).collect(Collectors.toList());
                            System.out.println(allMaterials);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                    (ListDetail_Activity.this,android.R.layout.simple_list_item_1,allMaterials);
                            material_name.setAdapter(adapter);
                            material_name.setThreshold(1);
                        }
                    }

                    @Override
                    public void onFailure(Call<java.util.List<Material>> call, Throwable t) {
                        Log.e("ERROR: ", t.getMessage());
                    }
                });

                lovelyCustomDialog = new LovelyCustomDialog(ListDetail_Activity.this)
                        .setView(view1)
                        .setTopColorRes(R.color.theme_light)
                        .setIcon(R.drawable.ic_plus)
                        .setTitle("Add item")
                        .setListener(R.id.add_btn_popup, new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onClick(View view) {

                                String materialName = material_name.getText().toString();

                                System.out.println(reg_no.getText().toString().trim());
                                if(!reg_no.getText().toString().trim().equalsIgnoreCase("") ){
                                    kolicina = reg_no.getText().toString();
                                }

                                System.out.println("KOLICINA ------");
                                System.out.println(kolicina);

                                Material foundedMaterial ;
                                        Optional<Material> optionalMaterial = materials.stream().filter(material -> material.getTitle().equalsIgnoreCase(materialName)).findFirst();
                                if(optionalMaterial.isPresent()){
                                    foundedMaterial = optionalMaterial.get();
                                    Item newItem = new Item(Long.parseLong(list_Id), foundedMaterial,Integer.parseInt(kolicina));

                                    addItemMethod(newItem);
                                }else {
                                    Material newMaterial =  new Material();
                                    newMaterial.setCreatedBy(list.getCreatedBy().getId());
                                    newMaterial.setTitle(materialName);
                                    newMaterial.setCategoryId(1L);
                                    newMaterial.setPrice(100);

                                    SharedPreferences sharedPref = getSharedPreferences("AuthPreferences", Context.MODE_PRIVATE);
                                    String token = sharedPref.getString("authToken","");
                                    System.out.println(token);

                                    Call<Material> call = materialService.addMaterial(newMaterial,token);
                                    call.enqueue(new Callback<Material>() {
                                        @Override
                                        public void onResponse(Call<Material> call, Response<Material> response) {
                                            if(response.isSuccessful()){
                                                Material createdMaterial = response.body();
                                                System.out.println("----material created ----");
                                                System.out.println(createdMaterial);

                                                Item newItem = new Item(Long.parseLong(list_Id), createdMaterial,Integer.parseInt(kolicina));

                                                addItemMethod(newItem);
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Material> call, Throwable t) {
                                            Log.e("ERROR: ", t.getMessage());
                                        }
                                    });
                                }
                            }
                        })
                        .setListener(R.id.cancel_btn_popup, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                lovelyCustomDialog.dismiss();
                            }
                        })
                        .show();
            }
        });
    }

    public void getListById(String list_Id){
        System.out.println("LIST ID ----------" + list_Id );
        Call<List> call = listService.getListById(Long.parseLong(list_Id+""));
        call.enqueue(new Callback<List>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List> call, Response<List> response) {
                if(response.isSuccessful()){
                    list = response.body();
                    createdBy.setText("created by: " + list.getCreatedBy().getUsername() );
                    total_price.setText("total : " +  list.getItems().stream().map(item -> item.getKolicina()*item.getMaterial().getPrice()).reduce(0, (acc,one)->acc+one)+ " din");
                    java.util.List<Item> items = list.getItems();
                    System.out.println("--------ITEMS--------");
                    System.out.println(items);
                    mRecyclerview.setLayoutManager(new LinearLayoutManager(ListDetail_Activity.this));
                    mAdapter = new ItemsListAdapter( items,ListDetail_Activity.this, list.getName());
                    mRecyclerview.setAdapter(mAdapter);


                    // on below line we are creating a method to create item touch helper
                    // method for adding swipe to delete functionality.
                    // in this we are specifying drag direction and position to right
                    new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                        @Override
                        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                            // this method is called
                            // when the item is moved.
                            return false;
                        }

                        @Override
                        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                            // this method is called when we swipe our item to right direction.
                            // on below line we are getting the item at a particular position.
                            Item deletedItem = items.get(viewHolder.getAdapterPosition());

                            // below line is to get the position
                            // of the item at that position.
                            int position = viewHolder.getAdapterPosition();

                            // this method is called when item is swiped.
                            // below line is to remove item from our array list.
                            SharedPreferences sharedPref = getSharedPreferences("AuthPreferences", Context.MODE_PRIVATE);
                            String token = sharedPref.getString("authToken","");
                            //items.remove(viewHolder.getAdapterPosition());
                            Call<ResponseBody> call = listService.deleteListItem(deletedItem.getId(),token);
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if(response.isSuccessful()){
                                        System.out.println("---- item deleted ----");
                                        System.out.println(response.body());
                                        itemsInit();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Log.e("ERROR: ", t.getMessage());
                                }
                            });




                            // below line is to notify our item is removed from adapter.
                            mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());

                        }
                        // at last we are adding this
                        // to our recycler view.
                    }).attachToRecyclerView(mRecyclerview);
                }
            }

            @Override
            public void onFailure(Call<List> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
            }
        });


    }

    public void itemsInit(){
        getListById(list_Id);

    }

    public void addItemMethod(Item item) {

        final ProgressDialog progressDialog = new ProgressDialog(ListDetail_Activity.this);
        progressDialog.setMessage("Creating item..");
        progressDialog.show();


        SharedPreferences sharedPref = getSharedPreferences("AuthPreferences", Context.MODE_PRIVATE);
        String token = sharedPref.getString("authToken","");
        System.out.println(token);

        Call<Item> call = listService.addListItem(item,item.getMaterial().getId(),token);
        call.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                if(response.isSuccessful()){
                    Item savedItem = response.body();
                    System.out.println("---item saved---");
                    progressDialog.dismiss();
                    lovelyCustomDialog.dismiss();
                    Intent intent = new Intent(ListDetail_Activity.this, ListDetail_Activity.class);
                    intent.putExtra("listId", list_Id);
                    intent.putExtra("listName", list_Name);
                    startActivity(intent);
                    ListDetail_Activity.this.finish();
                }
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
                progressDialog.dismiss();
                lovelyCustomDialog.dismiss();
            }
        });



    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_class_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

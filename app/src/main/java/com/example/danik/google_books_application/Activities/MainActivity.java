package com.example.danik.google_books_application.Activities;

import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.danik.google_books_application.Adapters.BookAdapter;
import com.example.danik.google_books_application.BookAPI;
import com.example.danik.google_books_application.Entities.Item;
import com.example.danik.google_books_application.Entities.Responce;
import com.example.danik.google_books_application.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static final String BASE_URL = "https://www.googleapis.com/";
    private Item books;
    private List<Item> kk;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    @BindView(R.id.recyclerView)
    protected RecyclerView recyclerView;
    @BindView(R.id.textNoData)
    protected TextView textNoData;
    @BindView(R.id.pullToRefresh)
    protected SwipeRefreshLayout pullToRefresh;

    private BookAdapter bookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        bookAdapter = new BookAdapter();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getBooks();

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getBooks();
                pullToRefresh.setRefreshing(false);
            }
        });
    }


    private void getBooks(){
        final ProgressDialog loading = ProgressDialog.show(this,"Fetching Data","Please wait...",false,false);

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        BookAPI api = retrofit.create(BookAPI.class);

        Call<Responce> call = api.getBooks();
        call.enqueue(new Callback<Responce>() {
            @Override
            public String toString() {
                return "$classname{}";
            }

            @Override
            public void onResponse(Call<Responce> call, Response<Responce> response) {
                String json = new Gson().toJson(response.body()).toString();
                List<Item> books = response.body().getItems();
                Log.i("onResponse: ", json);
                textNoData.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                bookAdapter.setProducts(books);

                recyclerView.setAdapter(bookAdapter);
                loading.dismiss();
            }

            @Override
            public void onFailure(Call<Responce> call, Throwable t) {
                loading.dismiss();
                recyclerView.setVisibility(View.GONE);
                textNoData.setVisibility(View.VISIBLE);
                t.getCause();
            }

        });
    }
}

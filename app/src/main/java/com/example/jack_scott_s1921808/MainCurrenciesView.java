package com.example.jack_scott_s1921808;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainCurrenciesView extends AppCompatActivity implements CurrencyAdapter.CurrencyClickListener {
    RecyclerView mainCurrenciesRecyclerView;
    CurrencyAdapter adapter;
    private ArrayList<Currency> currencies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_currencies_view);
        currencies = getIntent().getParcelableArrayListExtra("currencies");
        mainCurrenciesRecyclerView = findViewById(R.id.currencyRecyclerView);
        prepareRecyclerView();

    }

    private void prepareRecyclerView() {

        mainCurrenciesRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mainCurrenciesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        prepareAdapter();

    }

    private void prepareAdapter() {
        adapter = new CurrencyAdapter(this, currencies, this::selectedCurrency);
        mainCurrenciesRecyclerView.setAdapter(adapter);
    }

    @Override
    public void selectedCurrency(Currency currency) {
        startActivity(new Intent(this, SelectedCurrencyView.class).putExtra("selectedCurrency", currency));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.searchView) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem menuItem = menu.findItem(R.id.searchView);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("SearchDebug", "Query changed to: " + newText);
                adapter.getFilter().filter(newText);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

}

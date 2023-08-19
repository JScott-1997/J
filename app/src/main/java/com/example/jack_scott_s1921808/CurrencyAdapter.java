//
// Name                 Jack Scott
// Student ID           S1921808
// Programme of Study   BSc Computing
//

package com.example.jack_scott_s1921808;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyHolder> implements Filterable {

    private Context context;
    private ArrayList<Currency> currencies = new ArrayList<>();
    private ArrayList<Currency> currenciesFiltered = new ArrayList<>();
    private CurrencyClickListener currencyClickListener;

    public CurrencyAdapter(Context context, ArrayList<Currency> currencies, CurrencyClickListener currencyClickListener) {
        this.context = context;
        this.currencies = currencies;
        this.currenciesFiltered = currencies;
        this.currencyClickListener = currencyClickListener;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();

                if (charSequence == null || charSequence.length() == 0) {
                    filterResults.count = currencies.size();
                    filterResults.values = currencies;
                } else {
                    String searchTerm = charSequence.toString().toLowerCase().trim();

                    List<Currency> filteredList = new ArrayList<>();

                    for (Currency currency : currencies) {
                        if (currency.getCurrencyName().toLowerCase().contains(searchTerm)
                                || currency.getCurrencyCode().toLowerCase().contains(searchTerm)) {
                            filteredList.add(currency);
                        }
                    }
                    Log.d("FilterDebug", "Filtered count: " + filteredList.size());
                    filterResults.count = filteredList.size();
                    filterResults.values = filteredList;
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                currenciesFiltered = (ArrayList<Currency>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public interface CurrencyClickListener{
        void selectedCurrency(Currency currency);
    }

    @NonNull
    @Override
    public CurrencyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.currency_layout_item, parent, false);

        return new CurrencyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyAdapter.CurrencyHolder holder, int position) {

        Currency currency = currenciesFiltered.get(position);
        holder.setDetails(currency);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currencyClickListener.selectedCurrency(currency);
            }
        });

    }

    @Override
    public int getItemCount() {
        return currenciesFiltered.size();
    }

    class CurrencyHolder extends RecyclerView.ViewHolder {

        private TextView textCurrName, textCurrCode, textCurrExRate, textPubDate;

        public CurrencyHolder(@NonNull View itemView) {
            super(itemView);
            textCurrName = itemView.findViewById(R.id.textCurrName);
            textCurrCode = itemView.findViewById(R.id.textCurrCode);
            textCurrExRate = itemView.findViewById(R.id.textCurrExRate);
            textPubDate = itemView.findViewById(R.id.textPubDate);
        }

        void setDetails(Currency currency){
            textCurrName.setText(String.format(Locale.UK, "%s", currency.getCurrencyName()));
            textCurrCode.setText(String.format(Locale.UK, "Currency Code: %s", currency.getCurrencyCode()));
            textCurrExRate.setText(String.format(Locale.UK, "1 GBP = %.2f", currency.getExchangeRate())+ String.format(Locale.UK, " %s", currency.getCurrencyCode()));  // Assuming you want 2 decimal points

            double exchangeRate = currency.getExchangeRate();
            if (exchangeRate > 10) {
                textCurrExRate.setTextColor(Color.RED); // Very Weak
            } else if (exchangeRate > 5) {
                textCurrExRate.setTextColor(Color.parseColor("#FFA500")); // Orange for Weak
            } else if (exchangeRate > 2) {
                textCurrExRate.setTextColor(Color.YELLOW); // Yellow for Moderate
            } else {
                textCurrExRate.setTextColor(Color.GREEN); // Green for Strong
            }

            textPubDate.setText(String.format(Locale.UK, "Publication Date: %s", currency.getPublicationDate()));
        }


    }
}

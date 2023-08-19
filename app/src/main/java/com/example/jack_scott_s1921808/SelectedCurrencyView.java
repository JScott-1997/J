package com.example.jack_scott_s1921808;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class SelectedCurrencyView extends AppCompatActivity {

    private TextView textCurrName, textCurrCode, textCurrExRate, textPubDate;
    private EditText editTextBaseValue, editTextConvertedValue;
    private boolean isEditing = false;
    private Currency currency;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_currency_view);
        textCurrName = findViewById(R.id.textCurrName);
        textCurrCode = findViewById(R.id.textCurrCode);
        textCurrExRate = findViewById(R.id.textCurrExRate);
        textPubDate = findViewById(R.id.textPubDate);
        editTextBaseValue = findViewById(R.id.editTextBaseValue);
        editTextConvertedValue = findViewById(R.id.editTextConvertedValue);

        Intent intent = getIntent();

        if(intent != null){
            currency =  intent.getParcelableExtra("selectedCurrency");
            textCurrName.setText(String.format(Locale.UK, "%s", currency.getCurrencyName()));
            textCurrCode.setText(String.format(Locale.UK, "Currency Code: %s", currency.getCurrencyCode()));
            textCurrExRate.setText(String.format(Locale.UK, "1 GBP = %.2f", currency.getExchangeRate())+ String.format(Locale.UK, " %s", currency.getCurrencyCode()));  // Assuming you want 2 decimal points
            textPubDate.setText(String.format(Locale.UK, "Publication Date: %s", currency.getPublicationDate()));

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

            editTextConvertedValue.setHint(currency.getCurrencyCode());
            editTextBaseValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (isEditing) return; // Prevent infinite loop
                    isEditing = true;
                    updateConvertedValue();
                    isEditing = false;
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

            editTextConvertedValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (isEditing) return; // Prevent infinite loop
                    isEditing = true;
                    updateBaseValue();
                    isEditing = false;
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }

    }

    private void updateConvertedValue() {
        String baseText = editTextBaseValue.getText().toString();
        if (!baseText.isEmpty()) {
            double baseValue = Double.parseDouble(baseText);
            double conversionRate = currency.getExchangeRate(); // You should get your conversion rate here!
            double convertedValue = baseValue * conversionRate;
            // Limit the result to 3 decimal places
            String formattedValue = String.format(Locale.getDefault(), "%.2f", convertedValue);
            editTextConvertedValue.setText(formattedValue);
        } else {
            editTextConvertedValue.setText("");
        }
    }

    private void updateBaseValue() {
        String convertedText = editTextConvertedValue.getText().toString();
        if (!convertedText.isEmpty()) {
            double convertedValue = Double.parseDouble(convertedText);
            double conversionRate = currency.getExchangeRate(); // You should get your conversion rate here!
            double baseValue = convertedValue / conversionRate;
            // Limit the result to 3 decimal places
            String formattedValue = String.format(Locale.getDefault(), "%.2f", baseValue);
            editTextBaseValue.setText(formattedValue);
        } else {
            editTextBaseValue.setText("");
        }
    }

}

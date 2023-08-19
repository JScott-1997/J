//
// Name                 Jack Scott
// Student ID           S1921808
// Programme of Study   BSc Computing
//

package com.example.jack_scott_s1921808;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Currency implements Parcelable {
    int id;
    String currencyName;
    String currencyCode;
    String link;
    String guid;
    String publicationDate;
    double exchangeRate;
    String category;

    public Currency() {
        this.id = 0;
        this.currencyName = "";
        this.currencyCode = "";
        this.link = "";
        this.guid = "";
        this.publicationDate = "";
        this.exchangeRate = 0;
        this.category = "";
    }

    public Currency(int id, String currencyName, String currencyCode, String link, String guid, String publicationDate, double exchangeRate, String category) {
        this.id = id;
        this.currencyName = currencyName;
        this.currencyCode = currencyCode;
        this.link = link;
        this.guid = guid;
        this.publicationDate = publicationDate;
        this.exchangeRate = exchangeRate;
        this.category = category;
    }

    protected Currency(Parcel in) {
        id = in.readInt();
        currencyName = in.readString();
        currencyCode = in.readString();
        link = in.readString();
        guid = in.readString();
        publicationDate = in.readString();
        exchangeRate = in.readDouble();
        category = in.readString();
    }

    public static final Creator<Currency> CREATOR = new Creator<Currency>() {
        @Override
        public Currency createFromParcel(Parcel in) {
            return new Currency(in);
        }

        @Override
        public Currency[] newArray(int size) {
            return new Currency[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public static List<String> getExchangeRates(List<Currency> currencies) {
        List<String> exchangeRateStrings = new ArrayList<>();
        for (Currency currency : currencies) {
            exchangeRateStrings.add(currency.getCurrencyCode() + ": " + currency.getExchangeRate());
        }
        return exchangeRateStrings;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(currencyName);
        parcel.writeString(currencyCode);
        parcel.writeString(link);
        parcel.writeString(guid);
        parcel.writeString(publicationDate);
        parcel.writeDouble(exchangeRate);
        parcel.writeString(category);
    }
}

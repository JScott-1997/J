package com.example.jack_scott_s1921808;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private Button startButton;
    private CardView usdRateBox, eurRateBox, jpyRateBox;
    private String xmlOutput;
    private String url1 = "";
    private String urlSource = "https://www.fx-exchange.com/gbp/rss.xml";

    // Define a single thread executor. This ensures tasks are executed serially.
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startProgress();
    }

    public void startProgress() {
        executor.submit(new Task(urlSource));
    }

    private class Task implements Runnable {
        private String url;
        private ArrayList<Currency> currencies;

        public Task(String aurl) {
            url = aurl;
        }
        @Override
        public void run() {

            //Get XML from the RSS feed
            xmlOutput = getXmlFromFeed(url);
            xmlOutput = sanitiseXmlInput(xmlOutput);

            //Parse the XML and populate an arraylist with all entries
            currencies = parseData(xmlOutput);
            Currency jpyCurrency = findCurrencyByCode("JPY", currencies);
            Currency eurCurrency = findCurrencyByCode("EUR", currencies);
            Currency usdCurrency = findCurrencyByCode("USD", currencies);


            MainActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    Log.d("UI thread", "I am the UI thread");

                    usdRateBox = findViewById(R.id.usdRateBox);


                    usdRateBox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(MainActivity.this, SelectedCurrencyView.class).putExtra("selectedCurrency", usdCurrency));
                        }
                    });

                    eurRateBox = findViewById(R.id.eurRateBox);
                    eurRateBox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(MainActivity.this, SelectedCurrencyView.class).putExtra("selectedCurrency", eurCurrency));
                        }
                    });

                    jpyRateBox = findViewById(R.id.jpyRateBox);
                    jpyRateBox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(MainActivity.this, SelectedCurrencyView.class).putExtra("selectedCurrency", jpyCurrency));
                        }
                    });

                    Currency usdCurrency = findCurrencyByCode("USD", currencies);
                    if (usdCurrency != null) {
                        TextView usdExRate = findViewById(R.id.usdExRate);
                        usdExRate.setText(String.format(Locale.UK, "1 GBP = %.2f", usdCurrency.getExchangeRate())+ String.format(Locale.UK, " %s", usdCurrency.getCurrencyCode()));
                        double exchangeRate = usdCurrency.getExchangeRate();
                        if (exchangeRate > 10) {
                            usdExRate.setTextColor(Color.RED); // Very Weak
                        } else if (exchangeRate > 5) {
                            usdExRate.setTextColor(Color.parseColor("#FFA500")); // Orange for Weak
                        } else if (exchangeRate > 2) {
                            usdExRate.setTextColor(Color.YELLOW); // Yellow for Moderate
                        } else {
                            usdExRate.setTextColor(Color.GREEN); // Green for Strong
                        }
                    }

                    Currency eurCurrency = findCurrencyByCode("EUR", currencies);
                    if (eurCurrency != null) {
                        TextView eurExRate = findViewById(R.id.eurExRate);
                        eurExRate.setText(String.format(Locale.UK, "1 GBP = %.2f", eurCurrency.getExchangeRate())+ String.format(Locale.UK, " %s", eurCurrency.getCurrencyCode()));
                        double exchangeRate = eurCurrency.getExchangeRate();
                        if (exchangeRate > 10) {
                            eurExRate.setTextColor(Color.RED); // Very Weak
                        } else if (exchangeRate > 5) {
                            eurExRate.setTextColor(Color.parseColor("#FFA500")); // Orange for Weak
                        } else if (exchangeRate > 2) {
                            eurExRate.setTextColor(Color.YELLOW); // Yellow for Moderate
                        } else {
                            eurExRate.setTextColor(Color.GREEN); // Green for Strong
                        }
                    }

                    Currency jpyCurrency = findCurrencyByCode("JPY", currencies);
                    if (jpyCurrency != null) {
                        TextView jpyExRate = findViewById(R.id.jpyExRate);
                        jpyExRate.setText(String.format(Locale.UK, "1 GBP = %.2f", jpyCurrency.getExchangeRate())+ String.format(Locale.UK, " %s", jpyCurrency.getCurrencyCode()));
                        double exchangeRate = jpyCurrency.getExchangeRate();
                        if (exchangeRate > 10) {
                            jpyExRate.setTextColor(Color.RED); // Very Weak
                        } else if (exchangeRate > 5) {
                            jpyExRate.setTextColor(Color.parseColor("#FFA500")); // Orange for Weak
                        } else if (exchangeRate > 2) {
                            jpyExRate.setTextColor(Color.YELLOW); // Yellow for Moderate
                        } else {
                            jpyExRate.setTextColor(Color.GREEN); // Green for Strong
                        }
                    }

                    startButton = findViewById(R.id.startButton);
                    startButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("I Have", "Benn Clicked");
                            Intent i = new Intent(MainActivity.this, MainCurrenciesView.class);
                            i.putParcelableArrayListExtra("currencies", currencies);
                            startActivity(i);
                        }
                    });
                }
            });


        }

    }

    private static String getXmlFromFeed(String url) {
        URL aurl;
        URLConnection yc;
        BufferedReader in = null;
        String inputLine = "";
        String temp = "";


        Log.e("MyTag", "in run");

        try {
            Log.e("MyTag", "in try");
            aurl = new URL(url);
            yc = aurl.openConnection();
            in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            while ((inputLine = in.readLine()) != null) {
                temp = temp + inputLine;
                Log.e("MyTag", inputLine);

            }
            in.close();
        } catch (IOException ae) {
            Log.e("MyTag", "ioexception");
        }
        return temp;
    }

    private static String sanitiseXmlInput(String xmlOutput) {
        // Remove the first XML tag
        int i = xmlOutput.indexOf(">");
        xmlOutput = xmlOutput.substring(i + 1).trim();  // .trim() is optional but ensures there are no leading/trailing whitespaces

        // Remove the second XML tag
        i = xmlOutput.indexOf(">");
        xmlOutput = xmlOutput.substring(i + 1).trim();
        // Remove the last XML tag
        int lastOpenTag = xmlOutput.lastIndexOf("<");
        if (lastOpenTag != -1) {
            xmlOutput = xmlOutput.substring(0, lastOpenTag).trim();
        }
        Log.e("MyTag - cleaned", xmlOutput);
        //LOWERCASE THE UPPERCASED TAGS </lastBuildDate> and </pubDate>
        xmlOutput = xmlOutput.replace("</lastBuildDate>", "</lastbuilddate>");
        xmlOutput = xmlOutput.replace("</pubDate>", "</pubdate>");
        xmlOutput = xmlOutput.replace("<lastBuildDate>", "<lastbuilddate>");
        xmlOutput = xmlOutput.replace("<pubDate>", "<pubdate>");

        return xmlOutput;
    }

    private ArrayList<Currency> parseData(String dataToParse) {
        Currency currency = new Currency();
        ArrayList<Currency> currencies = new ArrayList<>();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(dataToParse));
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG && xpp.getDepth() == 3) {
                    String tagName = xpp.getName().toLowerCase();
                    String content = xpp.nextText();

                    switch (tagName) {
                        case "title":
                            Log.e("MyTag", "Title: " + content);
                            currency.setCurrencyName(extractCurrencyName(content));
                            currency.setCurrencyCode(extractCurrencyCode(content));
                            break;
                        case "link":
                            Log.e("MyTag", "Link: " + content);
                            currency.setLink(content);
                            break;
                        case "guid":
                            Log.e("MyTag", "GUID: " + content);
                            currency.setGuid(content);
                            break;
                        case "pubdate":
                            Log.e("MyTag", "Publication Date: " + content);
                            currency.setPublicationDate(content);
                            break;
                        case "description":
                            Log.e("MyTag", "Description: " + content);
                            currency.setExchangeRate(extractExchangeRate(content));
                            break;
                        case "category":
                            Log.e("MyTag", "Category: " + content);
                            currency.setCategory(content);
                            break;
                    }
                } else if (eventType == XmlPullParser.END_TAG && "item".equals(xpp.getName().toLowerCase())) {
                    currencies.add(currency);
                    currency = new Currency();
                }

                eventType = xpp.next();
            }
        } catch (XmlPullParserException ae1) {
            Log.e("MyTag", "Parsing error" + ae1.toString());
        } catch (IOException ae1) {
            Log.e("MyTag", "IO error during parsing");
        }

        Log.e("MyTag", "End document");
        return currencies;
    }

    public static double extractExchangeRate(String input) {
        String regex = "=\\s*(\\d+\\.?\\d*)";
        Matcher matcher = Pattern.compile(regex).matcher(input);

        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1));
        } else {
            throw new IllegalArgumentException("No exchange rate found in the provided input.");
        }
    }

    public static String extractCurrencyName(String input) {
        String[] parts = input.split("/");
        if (parts.length > 1) {
            String afterSlash = parts[1];
            return afterSlash.replaceAll("\\(.*?\\)", "").trim();
        }
        return "";
    }

    public static String extractCurrencyCode(String input) {
        String[] parts = input.split("/");
        if (parts.length > 1) {
            String afterSlash = parts[1];
            int startIndex = afterSlash.indexOf("(");
            int endIndex = afterSlash.indexOf(")");

            if (startIndex >= 0 && endIndex > startIndex) {
                return afterSlash.substring(startIndex + 1, endIndex);
            }
        }
        return "";
    }

    private Currency findCurrencyByCode(String code, ArrayList<Currency> currencies) {
        for (Currency currency : currencies) {
            if (currency.getCurrencyCode().equalsIgnoreCase(code)) {
                return currency;
            }
        }
        return null;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();  // shuts down the executor gracefully.
    }
}
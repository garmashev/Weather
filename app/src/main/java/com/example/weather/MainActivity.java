package com.example.weather;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {
    NodeList nodeList;
    String URL = "http://export.yandex.ru/weather-ng/forecasts/34504.xml";
    ProgressDialog pDialog;
    String Country_Name="";
    public String country = "";
    List<String> countries = new ArrayList<>();
    List<String> cities = new ArrayList<>();
    List<String> cities_id = new ArrayList<>();
    private static final String TAG = "Debug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        new AsyncTask<Void, Void, Document>() {
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(MainActivity.this);
                pDialog.setTitle("Загрузка стран");
                pDialog.setMessage("Загрузка...");
                pDialog.setIndeterminate(false);
                pDialog.show();
            }
            @Override
            protected Document doInBackground(Void... params) {
                Document doc = null;
                try {
                    doc = Jsoup.connect("https://pogoda.yandex.ru/static/cities.xml").get();

                } catch (IOException e) {
                    Log.e(TAG, "onCreate: " + e.getMessage());
                }
                return doc;
            }
            @Override
            protected void onPostExecute(Document document) {
                super.onPostExecute(document);
                Elements links = document.select("country");
                for (Element link : links) {
                    countries.add(link.attr("name"));
                }
                Spinner sp_Country = (Spinner) findViewById(R.id.sp_Country);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                        android.R.layout.simple_spinner_item, countries);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_Country.setAdapter(adapter);
                pDialog.hide();
            }
        }.execute();


        Spinner sp_Country = (Spinner) findViewById(R.id.sp_Country);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item, countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_Country.setAdapter(adapter);

        sp_Country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {
                cities_id.clear();
                cities.clear();
                 country =  parent.getSelectedItem().toString();
                new AsyncTask<Void, Void, Document>() {
                    protected void onPreExecute() {
                        super.onPreExecute();
                        pDialog = new ProgressDialog(MainActivity.this);
                        pDialog.setTitle("Загрузка городов");
                        pDialog.setMessage("Загрузка...");
                        pDialog.setIndeterminate(false);
                        pDialog.show();
                    }
                    @Override
                    protected Document doInBackground(Void... params) {
                        Document doc = null;
                        try {
                            doc = Jsoup.connect("https://pogoda.yandex.ru/static/cities.xml").get();

                        } catch (IOException e) {
                            Log.e(TAG, "onCreate: " + e.getMessage());
                        }
                        return doc;
                    }
                    @Override
                    protected void onPostExecute(Document document) {
                        super.onPostExecute(document);
                        Elements links = document.getElementsByTag("city");
                        for (Element link : links) {
                            if (link.attr("country").equals(country)){
                             cities.add(link.text());
                                cities_id.add(link.attr("id"));
                            }
                        }
                        Spinner sp_City = (Spinner) findViewById(R.id.sp_City);

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                                android.R.layout.simple_spinner_item, cities);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sp_City.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        pDialog.hide();
                    }
                }.execute();

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        Spinner sp_City = (Spinner) findViewById(R.id.sp_City);
        sp_City.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {
                int position = selectedItemPosition;
                final String id = cities_id.get(position);

                new AsyncTask<Void, Void, Document>() {
                    protected void onPreExecute() {
                        super.onPreExecute();
                        pDialog = new ProgressDialog(MainActivity.this);
                        pDialog.setTitle("Загрузаем погоду");
                        pDialog.setMessage("Загрузка...");
                        pDialog.setIndeterminate(false);
                        pDialog.show();
                    }

                    @Override
                    protected Document doInBackground(Void... params) {
                        Document doc = null;

                        try {
                            doc = Jsoup.connect("http://export.yandex.ru/weather-ng/forecasts/" + id + ".xml").get();

                        } catch (IOException e) {
                            Log.e(TAG, "onCreate: " + e.getMessage());
                        }
                        return doc;
                    }

                    @Override
                    protected void onPostExecute(Document document) {
                        String weather = "";
                        TextView textView = (TextView) findViewById(R.id.temperature);
                        ImageView imageView = (ImageView) findViewById(R.id.imageView);
                        super.onPostExecute(document);
                        Elements links = document.getElementsByTag("temperature");
                        Elements type = document.getElementsByTag("weather_type");
                        for (Element link : links) {
                            textView.setText(link.text() + "°");
                            break;
                        }
                        for (Element link : type) {
                            weather = link.text();
                            break;
                        }
                        switch (weather) {
                            case "ясно":
                                imageView.setImageResource(R.drawable.sunbig);
                                break;
                            case "облачно с прояснениями":
                                imageView.setImageResource(R.drawable.cloud);
                                break;
                            case "малооблачно":
                                imageView.setImageResource(R.drawable.cloud);
                                break;
                            case "пасмурно":
                                imageView.setImageResource(R.drawable.cloudly);
                                break;
                            case "небольшой дождь":
                                imageView.setImageResource(R.drawable.semirain);
                                break;
                            case "дождь":
                                imageView.setImageResource(R.drawable.rain);
                                break;
                            case "снег":
                                imageView.setImageResource(R.drawable.snow);
                                break;
                            case "небольшой снег":
                                imageView.setImageResource(R.drawable.snow);
                                break;
                            default:
                                imageView.setImageResource(R.drawable.cloudly);
                                break;
                        }

                        pDialog.hide();
                    }
                }.execute();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

}






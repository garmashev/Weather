package com.example.weather;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ProgressDialog pDialog;
    String Country_Name="";
    public String country = "";
    List<String> countries = new ArrayList<>();
    List<String> cities = new ArrayList<>();
    private static final String TAG = "Debug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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



    }

}






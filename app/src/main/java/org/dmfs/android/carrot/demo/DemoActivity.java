package org.dmfs.android.carrot.demo;

import android.Manifest;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.dmfs.android.carrot.bindings.AndroidBindings;
import org.dmfs.android.carrot.bindings.IntentBindings;
import org.dmfs.android.carrot.bindings.contentpal.Bound;
import org.dmfs.android.carrot.demo.permissions.BasicAppPermissions;
import org.dmfs.android.carrot.demo.permissions.Permission;
import org.dmfs.android.carrot.locaters.RawResourceLocater;
import org.dmfs.android.contentpal.rowsets.AllRows;
import org.dmfs.android.contentpal.views.BaseView;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import au.com.codeka.carrot.CarrotEngine;
import au.com.codeka.carrot.CarrotException;
import au.com.codeka.carrot.Configuration;
import au.com.codeka.carrot.bindings.Composite;
import au.com.codeka.carrot.bindings.JsonObjectBindings;
import au.com.codeka.carrot.bindings.SingletonBindings;


public class DemoActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Permission permission = new BasicAppPermissions(this).forName(Manifest.permission.READ_CONTACTS);
        if (!permission.isGranted())
        {
            permission.request().send(this);
        }

        setContentView(R.layout.activity_demo);

        getSharedPreferences("nodots", 0).edit().putString("key1", "value1").putInt("int1", 1).apply();
        getSharedPreferences("name.with special+chars", 0).edit().putString("key2", "value2").apply();

        getIntent().putExtra("extra-key", "extra-value");
    }


    public void click(View view) throws IOException, JSONException
    {
        CarrotEngine engine = new CarrotEngine();
        Configuration config = engine.getConfig();
        config.setResourceLocater(new RawResourceLocater(this));
        try
        {
            String templateName = String.valueOf(R.raw.demo);
            String output = engine.process(templateName, new Composite(
                    new JsonObjectBindings(new JSONObject(fromInputStream(getResources().openRawResource(R.raw.dependencies)))),
                    new SingletonBindings("$licenses",
                            new JsonObjectBindings(new JSONObject(fromInputStream(getResources().openRawResource(R.raw.licenses))))),
                    new SingletonBindings("$contacts",
                            // bind an iterable of all contacts in the database
                            new Bound(new AllRows<>(new BaseView<ContactsContract.Contacts>(this.getContentResolver().acquireContentProviderClient(
                                    ContactsContract.AUTHORITY_URI), ContactsContract.Contacts.CONTENT_URI)))),
                    new AndroidBindings(this),
                    new SingletonBindings("$intent", new IntentBindings(getIntent()))
            ));
            ((TextView) findViewById(R.id.text)).setText(output);
        }
        catch (CarrotException e)
        {
            e.printStackTrace();
        }
    }


    private String fromInputStream(InputStream in) throws IOException
    {
        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null)
        {
            total.append(line).append('\n');
        }
        return total.toString();
    }
}

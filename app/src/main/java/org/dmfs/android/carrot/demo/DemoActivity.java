package org.dmfs.android.carrot.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.dmfs.android.carrot.bindings.AndroidBindings;
import org.dmfs.android.carrot.bindings.IntentBindings;
import org.dmfs.android.carrot.locaters.RawResourceLocater;

import au.com.codeka.carrot.CarrotEngine;
import au.com.codeka.carrot.CarrotException;
import au.com.codeka.carrot.Configuration;
import au.com.codeka.carrot.bindings.Composite;
import au.com.codeka.carrot.bindings.SingletonBindings;


public class DemoActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        getSharedPreferences("nodots", 0).edit().putString("key1", "value1").putInt("int1", 1).apply();
        getSharedPreferences("name.with special+chars", 0).edit().putString("key2", "value2").apply();

        getIntent().putExtra("extra-key", "extra-value");
    }


    public void click(View view)
    {
        CarrotEngine engine = new CarrotEngine();
        Configuration config = engine.getConfig();
        config.setResourceLocater(new RawResourceLocater(this));
        try
        {
            String templateName = String.valueOf(R.raw.demo);
            String output = engine.process(templateName, new Composite(
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
}

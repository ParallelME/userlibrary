package br.ufmg.dcc.imageloader;

import android.content.Context; // ++ OB
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v8.renderscript.RenderScript;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

/**
 * @author Renato Utsch
 */
public class MainActivity extends AppCompatActivity {
    ImageView mDisplay;
    Spinner mRunWithSpinner;
    Switch mMortonSwitch;
    TextView mBenchmarkText;
    RenderScript mRS;
    RenderScriptLoader mRenderScriptLoader;
    JavaLoader mJavaLoader;
    JavaLoaderFlatArray mJavaLoaderFlatArray;
    JavaLoaderFlatArrayParallel mJavaLoaderFlatArrayParallel;

    RenderScriptConvolution mRenderScriptConvolution;
    JavaConvolution mJavaConvolution;
    JavaConvolutionFlatArray mJavaConvolutionFlatArray;
    JavaConvolutionFlatArrayParallel mJavaConvolutionFlatArrayParallel;

    BitmapLoaderTest mBitmapLoaderTest;

    public void load(View view) {
        // Image:
        int image = R.drawable.lencois;

        // Benchmark.
        long time = java.lang.System.currentTimeMillis();

        // Choose between the loader versions (Java or RenderScript).
        Bitmap bitmap;
        if (mRunWithSpinner.getSelectedItemPosition() == 0) {
            bitmap = mJavaLoader.load(getResources(), image);
        } else if (mRunWithSpinner.getSelectedItemPosition() == 1) {
            bitmap = mJavaLoaderFlatArray.load(getResources(), image, mMortonSwitch.isChecked());
        } else if (mRunWithSpinner.getSelectedItemPosition() == 2) {
            bitmap = mJavaLoaderFlatArrayParallel.load(getResources(), image, mMortonSwitch.isChecked());
        } else if(mRunWithSpinner.getSelectedItemPosition() == 3){ //Call user library
            bitmap = mBitmapLoaderTest.load(getResources(), image);
        } else {
            bitmap = mRenderScriptLoader.load(getResources(), image);
        }

        time = java.lang.System.currentTimeMillis() - time;

        String result = "Result: " + time + "ms";

        mBenchmarkText.setText(result);
        mDisplay.setImageBitmap(bitmap);
    }

    public void convolute(View view){
        // Image:
        int image = R.drawable.lencois;
        // Benchmark.
        long time = java.lang.System.currentTimeMillis();
        // Choose between the loader versions (Java or RenderScript).
        Bitmap bitmap;
        if(mRunWithSpinner.getSelectedItemPosition() == 0)
            bitmap = mJavaConvolution.convolute(getResources(), image);
        else if (mRunWithSpinner.getSelectedItemPosition() == 1)
            bitmap = mJavaConvolutionFlatArray.convolute(getResources(), image, mMortonSwitch.isChecked());
        else if (mRunWithSpinner.getSelectedItemPosition() == 2)
            bitmap = mJavaConvolutionFlatArrayParallel.convolute(getResources(), image, mMortonSwitch.isChecked());
        else
            bitmap = mRenderScriptConvolution.convolute(getResources(), image);

        time = java.lang.System.currentTimeMillis() - time;

        String result = "Result: " + time + "ms";

        mBenchmarkText.setText(result);
        mDisplay.setImageBitmap(bitmap);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.context = getApplicationContext(); // ++ OB
        setContentView(R.layout.activity_main);

        mRS = RenderScript.create(this);
        mRenderScriptLoader = new RenderScriptLoader(mRS);
        mJavaLoader = new JavaLoader();
        mJavaLoaderFlatArray = new JavaLoaderFlatArray();
        mJavaLoaderFlatArrayParallel = new JavaLoaderFlatArrayParallel();

        mRunWithSpinner = (Spinner) findViewById(R.id.spinner_run_with);
        mMortonSwitch = (Switch) findViewById(R.id.switchMorton);
        ArrayAdapter<CharSequence> runWithAdapter = ArrayAdapter.createFromResource(this,
                R.array.run_options, R.layout.spinner_layout);
        runWithAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRunWithSpinner.setAdapter(runWithAdapter);

        mBenchmarkText = (TextView) findViewById(R.id.benchmark_text);

        mDisplay = (ImageView) findViewById(R.id.display);

        mRenderScriptConvolution = new RenderScriptConvolution(mRS);
        mJavaConvolution = new JavaConvolution();
        mJavaConvolutionFlatArray = new JavaConvolutionFlatArray();
        mJavaConvolutionFlatArrayParallel = new JavaConvolutionFlatArrayParallel();
        mBitmapLoaderTest = new BitmapLoaderTest();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static Context context; // ++ OB
    public static Context getAppContext() { // ++ OB
        return MainActivity.context; // ++ OB
    }
}

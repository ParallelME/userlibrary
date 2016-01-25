package br.ufmg.dcc.tonemapreinhard;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.support.v8.renderscript.*;
import android.support.v7.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import br.ufmg.dcc.tonemapreinhard.R;


public class MainActivity extends AppCompatActivity {
    private SeekBar mKeyValueSeekBar;
    private SeekBar mGammaCorrectionSeekBar;
    private Spinner mRunCountSpinner;
    private Spinner mImageSpinner;
    private Spinner mRunWithSpinner;
    private TextView mKeyValueText;
    private TextView mGammaCorrectionText;
    private BitmapFactory.Options  mBitmapOptions;
    private Bitmap mBitmap;
    private ImageView mDisplay;
    private ProgressDialog mProgressDialog;
    private TextView mBenchmarkText;
    private RenderScript mRS;
    private ReinhardJavaOperator mReinhardJavaOperator;
    private ReinhardRenderScriptOperator mReinhardRenderScriptOperator;
    private ReinhardCollectionOperator mReinhardCollectionOperator;
    private int mRunCount;
    private float mKey;
    private float mGamma;

    private int res;

    private class ReinhardOperatorTask extends AsyncTask<ReinhardOperator, Void, Bitmap> {
        long mTime;
        private int position;

        protected ReinhardOperatorTask(int position){
            this.position = position;
        }

        private int selectResource(int position) {
            switch(position) {
                case 0:
                    return R.drawable.bristolb;
                case 1:
                    return R.drawable.clockbui;
                case 2:
                    return R.drawable.crowfoot;
                case 3:
                    return R.drawable.tahoe1;
                case 4:
                    return R.drawable.tinterna;
                default:
                    throw new RuntimeException("Failed to select image resource");
            }
        }

        @Override
        protected Bitmap doInBackground(ReinhardOperator... reinhard) {
            Bitmap bitmap;
            long time, total = 0;
            int count = 0;

            // Select the resource.
            int res = selectResource(this.position);

            // I use a do-while here because AndroidStudio is complaining about bitmap possibly
            // being not initialized with a for loop, although this is bullshit.
            do {
                time = java.lang.System.currentTimeMillis();
                bitmap = reinhard[0].runOp(getResources(), res, mKey, mGamma);
                time = java.lang.System.currentTimeMillis() - time;
                total += time;

                ++count;
            } while(count < mRunCount);

            mTime = total / mRunCount;
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            // mBitmap and mBenchmarkText can only be modified in the UI thread.
            mBitmap = bitmap;
            mDisplay.setImageBitmap(mBitmap);

            String result = "Result: " + mTime + "ms";
            if(mRunCount > 1)
                result += " (mean)";

            mBenchmarkText.setText(result);
            mProgressDialog.dismiss();
        }
    }

    private class RunCountChangeListener implements Spinner.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            mRunCount = Integer.parseInt(mRunCountSpinner.getItemAtPosition(pos).toString());
        }

        public void onNothingSelected(AdapterView<?> parent) { }
    }

    private class KeyValueSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            mKey = ((float) progress) / 100.0f;
            mKeyValueText.setText(String.format("%.2f", mKey));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) { }
    }

    private class GammaCorrectionSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            mGamma = ((float) progress) / 10.0f;
            mGammaCorrectionText.setText(String.format("%.1f", mGamma));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) { }
    }

    /** Called when the user clicks the Tonemap button */
    public void tonemap(View view) {
        mProgressDialog.show();

        // Choose between the Reinhard Operator versions.
        switch(mRunWithSpinner.getSelectedItemPosition()){
            case 0:
                new ReinhardOperatorTask(mImageSpinner.getSelectedItemPosition()).execute(mReinhardJavaOperator);
                break;
            case 1:
                new ReinhardOperatorTask(mImageSpinner.getSelectedItemPosition()).execute(mReinhardRenderScriptOperator);
                break;
            case 2:
                new ReinhardOperatorTask(mImageSpinner.getSelectedItemPosition()).execute(mReinhardCollectionOperator);
                break;
        }

    }

    /** Called when the user clicks the Reset button */
    public void reset(View view) {
        mImageSpinner.setSelection(0);
        mRunWithSpinner.setSelection(0);
        mRunCountSpinner.setSelection(0);
        mKeyValueSeekBar.setProgress(18);
        mGammaCorrectionSeekBar.setProgress(16);
        mBenchmarkText.setText("");
        mDisplay.setImageResource(android.R.color.transparent);
        mBitmap = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageSpinner = (Spinner) findViewById(R.id.spinner_image);
        ArrayAdapter<CharSequence> imageAdapter = ArrayAdapter.createFromResource(this,
                R.array.image_files, R.layout.spinner_layout);
        imageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mImageSpinner.setAdapter(imageAdapter);

        mRunWithSpinner = (Spinner) findViewById(R.id.spinner_run_with);
        ArrayAdapter<CharSequence> runWithAdapter = ArrayAdapter.createFromResource(this,
                R.array.run_options, R.layout.spinner_layout);
        runWithAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRunWithSpinner.setAdapter(runWithAdapter);

        mRunCountSpinner = (Spinner) findViewById(R.id.spinner_run_count);
        ArrayAdapter<CharSequence> runCountAdapter = ArrayAdapter.createFromResource(this,
                R.array.run_count, R.layout.spinner_layout);
        runCountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRunCountSpinner.setAdapter(runCountAdapter);
        mRunCountSpinner.setOnItemSelectedListener(new RunCountChangeListener());

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Loading");
        mProgressDialog.setMessage("Applying the tonemap...");
        mProgressDialog.setCanceledOnTouchOutside(false);

        mKeyValueText = (TextView) findViewById(R.id.text_key_value);
        mKeyValueSeekBar = (SeekBar) findViewById(R.id.seekbar_key_value);
        mKeyValueSeekBar.setOnSeekBarChangeListener(new KeyValueSeekBarChangeListener());

        mGammaCorrectionText = (TextView) findViewById(R.id.text_gama_correction);
        mGammaCorrectionSeekBar = (SeekBar) findViewById(R.id.seekbar_gamma_correction);
        mGammaCorrectionSeekBar.setOnSeekBarChangeListener(new GammaCorrectionSeekBarChangeListener());

        mBitmapOptions = new BitmapFactory.Options();
        mBitmapOptions.inMutable = true;

        mRS = RenderScript.create(this);
        mReinhardJavaOperator = new ReinhardJavaOperator();
        mReinhardRenderScriptOperator = new ReinhardRenderScriptOperator(mRS);
        mReinhardCollectionOperator = new ReinhardCollectionOperator();

        mBenchmarkText = (TextView) findViewById(R.id.text_benchmark);
        mDisplay = (ImageView) findViewById(R.id.display);

        reset(mDisplay); // Random view, doesn't matter.
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
}

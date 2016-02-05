package com.example.xual.helloa;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class MainActivity extends AppCompatActivity {
    private static final String NAMESPACE = "http://ws.cdyne.com/";
    private static final String METHOD_NAME = "ResolveIP";
    private static final String SOAP_ACTION = "http://ws.cdyne.com/ResolveIP";
    private static final String URL = "http://ws.cdyne.com/ip2geo/ip2geo.asmx";

    private Button btnCallWs;
    private TextView txtSoapResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCallWs = (Button)findViewById(R.id.btnCallWs);
        txtSoapResult = (TextView)findViewById(R.id.txtSoapResult);

        btnCallWs.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                new SoapAsyncTask().execute("89.12.4.4");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    class SoapAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
//            IP2Geo ip2Geo = new IP2Geo();
//            IP2GeoSoap ip2GeoSoap = ip2Geo.getIP2GeoSoap();
//            IPInformation ipInformation = ip2GeoSoap.resolveIP(params[0], "0");
//            return ipInformation.getCountry();


            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("ipAddress", params[0]);
            request.addProperty("licenseKey", "0");
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            String result;
            try {
                androidHttpTransport.call(SOAP_ACTION, envelope);
                SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                result = "Received :" + resultsRequestSOAP.toString();
            } catch (Exception e) {
                result = "Error";
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            txtSoapResult.setText(s);
        }
    }
}

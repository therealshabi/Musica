package com.finiteloop.musica;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class ReportAndFeedbackActivity extends AppCompatActivity {

    public static final String SERVLET_SERVER_URL = "http://192.168.0.5:8080/Musica-war/FeedbackServlet";
    private Toolbar toolbar;
    private TextView name;
    private TextView emailAddress;
    private TextView feedback;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_and_feedback);

        toolbar = (Toolbar) findViewById(R.id.reportAndFeedbackToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
            toolbar.setTitleTextColor(0xFFFFFFFF);
        }

        name = (EditText) findViewById(R.id.reportAndFeedbackNameEditText);
        emailAddress = (EditText) findViewById(R.id.reportAndFeedbackEmailEditText);
        feedback = (EditText) findViewById(R.id.reportAndFeedbackFeedbackEditText);
        button = (Button) findViewById(R.id.reportAndFeedbackSubmitButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest jsonObjRequest = new StringRequest(Request.Method.POST, SERVLET_SERVER_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getBaseContext(), "Your Feedback was successfully Registered", Toast.LENGTH_SHORT).show();
                        name.setText("");
                        emailAddress.setText("");
                        feedback.setText("");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("volley", "Error: " + error.getMessage());
                        Toast.makeText(getBaseContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded; charset=UTF-8";
                    }

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("username", name.getText().toString().trim());
                        params.put("email", emailAddress.getText().toString().trim());
                        params.put("feedback", feedback.getText().toString().trim());
                        return params;
                    }

                };

                Volley.newRequestQueue(getBaseContext()).add(jsonObjRequest);

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

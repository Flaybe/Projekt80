package com.example.projekt80;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projekt80.json.User;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentByTag("MyFragmentTag");
        if (loginFragment != null) {
            user = loginFragment.getLoggedInUser();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("DESTROYED");
        logOutUser();
    }
    private void logOutUser() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = LoginFragment.AZURE + "/user/logout";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    //Log.d("LOGOUT", response);
                }, error -> {
            //Log.d("LOGOUT", error.toString());
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + user.getAccessToken());
                return headers;
            }
        };
        queue.add(stringRequest);

    }
    /*
    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container); // Replace "R.id.container" with the ID of your fragment container
        if (fragment instanceof HomeFragment) {

            Toast.makeText(this, "Cannot go back from this fragment", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed(); // Allow normal back navigation for other fragments
        }
    }*/
}
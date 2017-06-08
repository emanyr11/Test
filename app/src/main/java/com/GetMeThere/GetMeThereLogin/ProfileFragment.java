package com.GetMeThere.GetMeThereLogin;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.GetMeThere.GetMeThereLogin.models.ServerRequest;
import com.GetMeThere.GetMeThereLogin.models.ServerResponse;
import com.GetMeThere.GetMeThereLogin.models.User;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Url;

//We will use a navigation drawer to overcome the issue Eman is facing with the passing of the JSON file.
// ALter Json output to be implemented with a button push
// Use a draw to display option of time tables
// Check SQl insert to find error with multiple inputs(!result may work)
// Recreate the ASYNC task implementing methods with buttons 
public class ProfileFragment extends Fragment implements View.OnClickListener{

    private TextView tv_name,tv_class,tv_class2,tv_message,tv_date;
    private SharedPreferences pref;
    private long date = System.currentTimeMillis();
    private AppCompatButton btn_change_password,btn_logout,btn_show_class;
    private EditText et_old_password,et_new_password;
    private AlertDialog dialog;
    private ProgressBar progress;
    String JSON_STRING;
    String J_STRING1;
    String J_STRING2;
    String email = "email";
    String json_url;
    //String name = "name";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        pref = getActivity().getPreferences(0);
        tv_name.setText(pref.getString(Constants.EMAIL,""));                                // Sets the textview tv_name to the string email retrived from the database
        email = tv_name.getText().toString();                                               // Assigns tv_name string to email for comparision later in a if else ladder
        if(email.equals("emanyr11@gmail.com"))                                              // If else ladder that compares emails with eachother to identify which JSON URL will be used later in the program
        {
            json_url = "https://getmethereapp.000webhostapp.com/e_timetable.php";           // This JSON URL will be used to display the timetable of the user with email "emanyr11@gmail.com"
        }
        else if (email.equals("nitish@gmail.com"))
        {
            json_url = "https://getmethereapp.000webhostapp.com/n_timetable.php";            // This JSON URL will be used to display the timetable of the user with email "nitish@gmail.com"
        }
        String formattedDate = new SimpleDateFormat("MMM dd EEE, yyyy ").format(Calendar.getInstance().getTime());      // This assigns a date format too formatted date which will be used
        tv_date.setText(formattedDate);
    }
    /*
     * For the design of the timetable to conform to that decided by the team I was not able to insert buttons to insert and 
     * to parse the data. This has been overcome by injecting and parsing the data using JSON objects/arrays
     * and printing it in a String. This means if you login it will not display, but when you run the code again it will.
     * This is only a temporary fix till next sprint where a better alternative is found - Emmanuel
     */
    class BackgroundTask extends AsyncTask<Void,Void,String>
    {
        

        @Override
        protected void onPreExecute() {
         
        }
        // Change is here
        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(json_url);
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();          // Creates an open URL connection
                    InputStream inputStream = httpURLConnection.getInputStream();                           // Creates an input stream
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));             // Assigns an inputstream to a buffered reader
                    StringBuilder stringBuilder = new StringBuilder();                                      // Creats a new stringbuilder
                    while((JSON_STRING = br.readLine())!= null)                                             // Uses a buffered reader to go through the file and assign it to JSON_String                                        
                    {
                        stringBuilder.append(JSON_STRING+"\n");                                             // Creates New line at the end of a line
                    }
                    br.close();                                                                             // Closes the buffered reader
                    inputStream.close();                                                                    // Closes the input stream
                    httpURLConnection.disconnect();                                                         // Disconnects the HTTP connection                               
                    JSON_STRING = stringBuilder.toString().trim();                                                                                                                               
                    JSONObject parentObject = new JSONObject(JSON_STRING);                                  // Creates a New JSON object that holds eceyrthing in the JSON String
                    JSONArray parentArray = parentObject.getJSONArray("server_response");                   // Creates a JSON array using the array stored in the server
                    JSONObject finalObject = parentArray.getJSONObject(0);                                  // Indicates the object in the array that is going to be displyed
                    String classStart = finalObject.getString("Class Start");                               // Find the ID of the values in the array
                    String classFinish = finalObject.getString("Class Finish");
                    String location = finalObject.getString("Location");
                    String paper = finalObject.getString("Paper");
                    JSONObject finalObject2 = parentArray.getJSONObject(2);
                    String classStart2 = finalObject2.getString("Class Start");
                    String classFinish2 = finalObject2.getString("Class Finish");
                    String location2 = finalObject2.getString("Location");
                    String paper2 = finalObject2.getString("Paper");
                    J_STRING1 ="Class Time: "+classStart  + " - " + classFinish +"\nLocation: "+ location + "\nPaper: " + paper;                // Display the data which has been Parsed
                    J_STRING2 ="Class Time: "+classStart2  + " - " + classFinish2 +"\nLocation: "+ location2 + "\nPaper: " + paper2;
                    return stringBuilder.toString().trim();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
          //tv_class.setText(result);
            tv_class.setText(J_STRING1);
            tv_class2.setText(J_STRING2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);
       // View view2 = inflater.inflate(R.layout.navigation_bar,container,false);
        initViews(view);
        //initViews(view2);
        return view;
    }




    private void initViews(View view){
        //new BackgroundTask().execute();
        tv_name = (TextView)view.findViewById(R.id.tv_name);
        tv_date = (TextView)view.findViewById(R.id.tv_date);
        tv_class = (TextView)view.findViewById(R.id.tv_class);
        tv_class2 = (TextView)view.findViewById(R.id.tv_class2);
        btn_change_password = (AppCompatButton)view.findViewById(R.id.btn_chg_password);
        btn_logout = (AppCompatButton)view.findViewById(R.id.btn_logout);
        btn_show_class = (AppCompatButton)view.findViewById(R.id.btn_show_class);
        btn_show_class.setOnClickListener(this);
        btn_change_password.setOnClickListener(this);
        btn_logout.setOnClickListener(this);

    }


    private void showDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_change_password, null);
        et_old_password = (EditText)view.findViewById(R.id.et_old_password);
        et_new_password = (EditText)view.findViewById(R.id.et_new_password);
        tv_message = (TextView)view.findViewById(R.id.tv_message);
        progress = (ProgressBar)view.findViewById(R.id.progress);
        builder.setView(view);
        builder.setTitle("Change Password");
        builder.setPositiveButton("Change Password", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String old_password = et_old_password.getText().toString();
                    String new_password = et_new_password.getText().toString();
                    if(!old_password.isEmpty() && !new_password.isEmpty()){

                        progress.setVisibility(View.VISIBLE);
                        changePasswordProcess(pref.getString(Constants.EMAIL,""),old_password,new_password);

                    }else {

                        tv_message.setVisibility(View.VISIBLE);
                        tv_message.setText("Fields are empty");
                    }
                }
            });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_show_class:
                new BackgroundTask().execute();
                break;
            case R.id.btn_chg_password:
                showDialog();
                break;
            case R.id.btn_logout:
                logout();
                break;
        }
    }

    private void logout() {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(Constants.IS_LOGGED_IN,false);
        editor.putString(Constants.EMAIL,"");
        editor.putString(Constants.NAME,"");
        editor.putString(Constants.UNIQUE_ID,"");
        editor.apply();
        goToLogin();
    }

    private void goToLogin(){

        Fragment login = new LoginFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame,login);
        ft.commit();
    }

    /*private void goToMaps(){

        FragmentActivity maps = new MapsActivity();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, maps);
        ft.commit();
    }*/

    private void changePasswordProcess(String email,String old_password,String new_password){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        User user = new User();
        user.setEmail(email);
        user.setOld_password(old_password);
        user.setNew_password(new_password);
        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.CHANGE_PASSWORD_OPERATION);
        request.setUser(user);
        Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                ServerResponse resp = response.body();
                if(resp.getResult().equals(Constants.SUCCESS)){
                    progress.setVisibility(View.GONE);
                    tv_message.setVisibility(View.GONE);
                    dialog.dismiss();
                    Snackbar.make(getView(), resp.getMessage(), Snackbar.LENGTH_LONG).show();

                }else {
                    progress.setVisibility(View.GONE);
                    tv_message.setVisibility(View.VISIBLE);
                    tv_message.setText(resp.getMessage());

                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                Log.d(Constants.TAG,"failed");
                progress.setVisibility(View.GONE);
                tv_message.setVisibility(View.VISIBLE);
                tv_message.setText(t.getLocalizedMessage());


            }
        });
    }


}

package com.example.gp_rasta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class collaborative_recommend extends AppCompatActivity {

    private String url="http://192.168.1.4:5000";//****Put your  URL here******
    private String POST="POST";
    private String GET="GET";
    private FirebaseUser user;
    private DatabaseReference reference,ref1,ref2;
    private int user_ID;
    FirebaseDatabase database;
    // Initializing the ImageView
    ImageView rImage;
    TextView content,logOut;
    //Button btn;
    public String [] Arr = new String[100];
    public static int index = 0;
    ListView list ;
    ArrayList<subjectData> arrayList = new ArrayList<subjectData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collaborative_recommend);
        list = findViewById(R.id.list_view);
        rImage = findViewById(R.id.list_image);
        content=findViewById(R.id.content_based);
        logOut=findViewById(R.id.logout);
        user= FirebaseAuth.getInstance().getCurrentUser();
        reference=database.getInstance().getReference("User");
         ref1 = reference.child(user.getUid());
         ref2 = ref1.child("userId");
         ref2.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 sendRequest(GET, "simimov2", "ID",String.valueOf(snapshot.getValue(long.class)));
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {
                 error.toException();

             }
         });
         content.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent = new Intent(collaborative_recommend.this, RecommendedMovie.class);
                 startActivity(intent);
             }
         });




    }

    void sendRequest(String type,String method,String paramname,String param) {

        /* if url is of our get request, it should not have parameters according to our implementation.
         * But our post request should have 'name' parameter. */
        String fullURL = url + "/" + method + (param == null ? "" : "/" + param);
        Request request;

        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS).build();

        /* If it is a post request, then we have to pass the parameters inside the request body*/
        if (type.equals(POST)) {
            RequestBody formBody = new FormBody.Builder()
                    .add(paramname, param)
                    .build();

            request = new Request.Builder()
                    .url(fullURL)
                    .post(formBody)
                    .build();
        } else {
            /*If it's our get request, it doen't require parameters, hence just sending with the url*/
            request = new Request.Builder()
                    .url(fullURL)
                    .build();
        }
        /* this is how the callback get handled */
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                // Read data on the worker thread
                final String responseData = response.body().string();
                Arr = responseData.split("splitArray");
                // getting ImageView by its id
                // rImage = findViewById(R.id.rImage);
                // Run view-related code back on the main thread.
                // Here we display the response message in our text view
                //String[] finalArr = arr;

                collaborative_recommend.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        arrayList.clear();
                        for (int i = 0; i < Arr.length; i += 2) {

                            if (i == Arr.length - 1)
                                break;
                            arrayList.add(new subjectData(Arr[i], Arr[i + 1]));

                        }

                        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), arrayList);
                        customAdapter.notifyDataSetChanged();
                        list.setAdapter(customAdapter);

                    }

                });
                // textField_message.setText(Arr[2]);
                // Picasso.get().load(link[index]).into(rImage);
                //index++;

            }
        });
    }
        }
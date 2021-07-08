package com.example.downloadtrial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.example.downloadtrial.databinding.ActivityMainBinding;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.downloadButton.setOnClickListener(v -> {
            download();
        });
    }

    private void download() {

        OkHttpClient client = new OkHttpClient();
        Request reqest = new Request.Builder()
                .url("")
                .get()
                .build();

        client.newCall(reqest).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

                Log.e("trial", "onFailure", e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                Log.d("trial", "onResponse");

                InputStream input  = response.body().byteStream();

                StringBuffer path = new StringBuffer();
                path.append(getExternalCacheDir().getPath());
                path.append("/download.apk");
                File file = new File(path.toString());
                if(file.exists()){
                    file.delete();
                }
                OutputStream output = new FileOutputStream(file);

                byte[] buffer = new byte[1024];
                while (true){
                    int readSize = input.read(buffer);
                    if( readSize < 0) break;
                    output.write(buffer,0,readSize);
                }

                output.flush();
                output.close();
                input.close();

            }
        });
    }
}
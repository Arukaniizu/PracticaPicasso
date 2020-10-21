package com.example.practicapicasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private final String TAG = getClass().getName();
    private  final String urlACDC = "https://www.youtube.com/watch?v=v2AC41dglnM";
    private final String urlNirvana = "https://www.youtube.com/watch?v=hTWKbfoikeg";
    private final String urlQueen = "https://www.youtube.com/watch?v=fJ9rUzIMcZQ";
    private final String UrlACDCimage = "https://img.discogs.com/FT9k9f3Kvps7jP69jlzq9TRHB0A=/fit-in/600x600/filters:strip_icc():format(jpeg):mode_rgb():quality(90)/discogs-images/R-1116809-1397508500-7585.jpeg.jpg";
    private final String urlNirvanaImage = "https://pbs.twimg.com/profile_images/1017834817931669506/SThYUIvc_400x400.jpg";
    private final String urlQueenImage = "https://i.scdn.co/image/ab67706c0000bebbe7b4645a381329d75b61716a";
    private static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 100;
    ImageView imageView = null;
    private AnimationDrawable frameAnimation;
    int currenTime = 0;
    Timer timer;
    TimerTask timerTask;
    int cont = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imagenGrupo);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currenTime == 1 || currenTime == 2){
                Uri uri = Uri.parse(urlACDC);
                Intent intentACDC = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intentACDC);
                }else if(currenTime == 3 || currenTime == 4){
                    Uri uri = Uri.parse(urlNirvana);
                    Intent intentNirvana = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intentNirvana);
                }else if(currenTime == 5 || currenTime == 6){
                    Uri uri = Uri.parse(urlQueen);
                    Intent intentQeen = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intentQeen);
                }
            }
        });

        Button download = findViewById(R.id.downloadButton);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currenTime == 1 || currenTime == 2) {
                    saveImage(UrlACDCimage);
              //      Toast.makeText(getApplicationContext(), "Descargada la imagen de ACDC", Toast.LENGTH_SHORT).show();
                }else if(currenTime == 3 || currenTime == 4){
                    saveImage(urlNirvanaImage);
             //       Toast.makeText(getApplicationContext(), "Descargada la imagen de Nirvana", Toast.LENGTH_SHORT).show();
                }else if(currenTime == 5 || currenTime == 6){
                    saveImage(urlQueenImage);
             //       Toast.makeText(getApplicationContext(), "Descargada la imagen de Queen", Toast.LENGTH_SHORT).show();

                }
            }
        });

        Button permisos = findViewById(R.id.permissionButton);
        permisos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, (MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE));
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, (MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE));
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[] {Manifest.permission.INTERNET}, (MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE));


                } else {
                    Toast.makeText(MainActivity.this, "Ya tiene los permisos solicitados", Toast.LENGTH_SHORT).show();
                }
            }
        });
        }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");


        Button activar = findViewById(R.id.startButton);
        activar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currenTime =0;
                imageView.setImageResource(0);
                cronometro();
                //     frameAnimation = (AnimationDrawable) imageView.getBackground();
                //llamamos al HTML donde tenemos las imagenes con sus tiempos
                imageView.setBackgroundResource(R.drawable.animation);
                frameAnimation = (AnimationDrawable) imageView.getBackground();
                imageView.setClickable(true);
                frameAnimation.start();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        frameAnimation.stop();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        //Le decimos que pare la rotacion de imagenes cuando entre en el onStop
        if (frameAnimation.isRunning()) {
            frameAnimation.stop();

            frameAnimation.selectDrawable(0);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

                    //NO UTILIZADO
    public void imageLoad(String image, ImageView imageView){
      Picasso.get().load(image).into(imageView);
    }

        public void saveImage(String image) {
        Picasso.get().load(image).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                try {
                    File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString());
                    if (!directory.exists()) {
                        directory.mkdir();
                    }

                    FileOutputStream fileOutputStream = new FileOutputStream(new File(directory, "holaaaa.jpg"));
                    Toast.makeText(getApplicationContext(), "hola", Toast.LENGTH_SHORT).show();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (FileNotFoundException a) {
                    a.printStackTrace();
                }catch (IOException a){
                    a.printStackTrace();
                }
            }
            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    //Creamos un cronometro que  cuente de 1 a 6 (2 segundos por imagen)

    public void cronometro() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        currenTime++;
                        if (currenTime == 6)
                            currenTime = 0;
                    }

                });

            }
        };

        timer = new Timer();
        timer.schedule(timerTask, 1, 1000);

    }

}
package com.yudi.uasmobile;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class ActivityBerhasil extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private static final int REQUEST_CAMERA_PERMISSION = 102;
    private ImageView imageView;
    private boolean cameraPermissionGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berhasil);

        Button btnKembali = findViewById(R.id.btnKembali);
        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kode untuk kembali ke MainActivity
                Intent intent = getParentActivityIntent();
                if (intent != null) {
                    startActivity(intent);
                    finish();
                }
            }
        });

        Button btnBukaKamera = findViewById(R.id.btnBukaKamera);
        btnBukaKamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Periksa apakah izin kamera sudah diberikan sebelum membuka kamera
                if (cameraPermissionGranted) {
                    // Kode untuk membuka kamera
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                } else {
                    // Jika izin kamera belum diberikan, minta izin secara dinamis
                    ActivityCompat.requestPermissions(ActivityBerhasil.this,
                            new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                }
            }
        });

        imageView = findViewById(R.id.imageView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Kode untuk menangani foto yang diambil dari kamera
            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            // Periksa apakah izin kamera diberikan
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Jika izin kamera diberikan, set flag cameraPermissionGranted menjadi true
                cameraPermissionGranted = true;

                // Buka kamera setelah izin diberikan
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            } else {
                // Jika izin kamera tidak diberikan, berikan pemberitahuan atau tindakan lain sesuai kebutuhan aplikasi Anda
                // Misalnya, Anda dapat menampilkan pesan bahwa izin kamera diperlukan untuk menggunakan fitur kamera
                // atau memberikan opsi kepada pengguna untuk memberikan izin secara manual melalui pengaturan perangkat.
            }
        }
    }
}

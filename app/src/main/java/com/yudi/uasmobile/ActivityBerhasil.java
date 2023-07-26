package com.yudi.uasmobile;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.io.OutputStream;

public class ActivityBerhasil extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private static final int REQUEST_CAMERA_PERMISSION = 102;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 103;
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

        Button btnShare = findViewById(R.id.btnShare);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kode untuk berbagi gambar ke aplikasi lain
                if (imageView.getDrawable() != null) {
                    // Periksa apakah izin penyimpanan eksternal sudah diberikan sebelum berbagi gambar
                    if (ActivityCompat.checkSelfPermission(ActivityBerhasil.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        shareImage();
                    } else {
                        // Jika izin penyimpanan eksternal belum diberikan, minta izin secara dinamis
                        ActivityCompat.requestPermissions(ActivityBerhasil.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
                    }
                } else {
                    Toast.makeText(ActivityBerhasil.this, "Tidak ada gambar untuk dibagikan.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imageView = findViewById(R.id.imageView);
    }

    private Uri getImageUri() {
        Bitmap imageBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        String imageDescription = "Image Description";
        String imageUrl = null;

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, imageDescription);
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        ContentResolver resolver = getContentResolver();
        Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        if (imageUri != null) {
            try (OutputStream outputStream = resolver.openOutputStream(imageUri)) {
                if (outputStream != null) {
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.close();
                    imageUrl = imageUri.toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return Uri.parse(imageUrl);
    }

    private void shareImage() {
        Uri imageUri = getImageUri();
        if (imageUri != null) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            startActivity(Intent.createChooser(shareIntent, "Bagikan gambar melalui:"));
        } else {
            Toast.makeText(this, "Gagal berbagi gambar.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                cameraPermissionGranted = true;
            }
        } else if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                shareImage();
            } else {
                Toast.makeText(this, "Izin akses penyimpanan diperlukan untuk berbagi gambar.", Toast.LENGTH_SHORT).show();
            }
        }
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
    public void onBackPressed() {
        super.onBackPressed();
    }
}

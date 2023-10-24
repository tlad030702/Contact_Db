package com.example.contact_db;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton add;
    EditText name_input, dob_input, email_input;
    Button save_btn;
    DatabaseHelper db = new DatabaseHelper(MainActivity.this);
    ArrayList<String> contact_id, contact_name, contact_dob, contact_email;
    ArrayList<byte[]> contact_image;
    CustomAdapter customAdapter;
    ImageView imageView;
    FloatingActionButton chooseImage;
    private byte[] imageBytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_input, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        recyclerView = findViewById(R.id.recyclerView);
        add = findViewById(R.id.add_btn);
        name_input = dialogView.findViewById(R.id.inputName);
        dob_input = dialogView.findViewById(R.id.inputDob);
        email_input = dialogView.findViewById(R.id.inputEmail);
        save_btn = dialogView.findViewById(R.id.save_button);
        imageView = dialogView.findViewById(R.id.imageView);
        chooseImage = dialogView.findViewById(R.id.chooseImage);

        contact_id = new ArrayList<>();
        contact_name = new ArrayList<>();
        contact_dob = new ArrayList<>();
        contact_email = new ArrayList<>();
        contact_image = new ArrayList<>();

        customAdapter = new CustomAdapter(MainActivity.this, contact_id, contact_name, contact_dob, contact_email, contact_image);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        displayData();
        customAdapter.updateData(contact_id, contact_name, contact_dob, contact_email, contact_image);

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(MainActivity.this)
                    .crop()	    			//Crop image(Optional), Check Customization for more option
                    .compress(1024)			//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.addContact(name_input.getText().toString().trim(),
                        dob_input.getText().toString().trim(),
                        email_input.getText().toString().trim(),
                        imageBytes);

                dialog.dismiss();
                name_input.setText("");
                dob_input.setText("");
                email_input.setText("");
                imageView.setImageResource(R.drawable.baseline_person_24);

                displayData();
//                customAdapter.updateData(contact_id, contact_name, contact_dob, contact_email, contact_image);
                customAdapter.notifyDataSetChanged();
            }
        });
    }
    void displayData(){
        contact_id.clear();
        contact_name.clear();
        contact_dob.clear();
        contact_email.clear();
        contact_image.clear();

        Cursor cursor = db.dataContact();
        if (cursor.getCount() == 0){
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()){
                contact_id.add(cursor.getString(0));
                contact_name.add(cursor.getString(1));
                contact_dob.add(cursor.getString(2));
                contact_email.add(cursor.getString(3));
                contact_image.add(cursor.getBlob(4));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                System.out.println(inputStream);
                imageBytes = getBytes(inputStream);
                System.out.println(imageBytes);
                imageView.setImageURI(uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        return byteBuffer.toByteArray();
    }
}
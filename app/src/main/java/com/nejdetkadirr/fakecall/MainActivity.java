package com.nejdetkadirr.fakecall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> contactArrayList;
    TextView infoTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contactArrayList = new ArrayList<>();
        infoTitle = findViewById(R.id.titleTextView);
        final FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        infoTitle.setText("Rehberinizi aktarmak için aşağıda bulunan butona basınız");
        final ListView listView = findViewById(R.id.listView);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.READ_CONTACTS}, 1);
        } else {
            //ContentProvider
            ContentResolver contentResolver = getContentResolver();
            String[] projection = {ContactsContract.Contacts.DISPLAY_NAME};
            Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,projection,null,null,ContactsContract.Contacts.DISPLAY_NAME);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    contactArrayList.add(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                }
                cursor.close();
                ArrayAdapter adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,contactArrayList);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                if (contactArrayList.size() > 0) {
                    infoTitle.setText("Kim tarafından aranmak istersiniz?");
                    fab.setVisibility(View.INVISIBLE);
                }
            }
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                    //ContentProvider
                    ContentResolver contentResolver = getContentResolver();
                    String[] projection = {ContactsContract.Contacts.DISPLAY_NAME};
                    Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,projection,null,null,ContactsContract.Contacts.DISPLAY_NAME);
                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            contactArrayList.add(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                        }
                        cursor.close();
                        ArrayAdapter adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,contactArrayList);
                        listView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        if (contactArrayList.size() > 0) {
                            infoTitle.setText("Kim tarafından aranmak istersiniz?");
                            fab.setVisibility(View.INVISIBLE);
                        }
                    }
                } else {
                    Snackbar.make(v, "Rehberden kişileri çekmek için izin veriniz", Snackbar.LENGTH_INDEFINITE)
                            .setAction("İZİN VER", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_CONTACTS)) {
                                        ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.READ_CONTACTS}, 1);
                                    } else {
                                        Intent intent = new Intent();
                                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package",MainActivity.this.getPackageName(),null);
                                        intent.setData(uri);
                                        MainActivity.this.startActivity(intent);
                                    }

                                }
                            }).show();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,CallActivity.class);
                intent.putExtra("name",contactArrayList.get(position));
                startActivity(intent);
            }
        });

    }
}

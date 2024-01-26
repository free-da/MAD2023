package org.dieschnittstelle.mobile.android.skeleton;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import org.dieschnittstelle.mobile.android.skeleton.databinding.ActivityDetailviewBinding;
import org.dieschnittstelle.mobile.android.skeleton.model.ToDo;
import org.dieschnittstelle.mobile.android.skeleton.viewmodel.DetailviewViewmodelImpl;

import java.util.Arrays;

public class DetailviewActivity extends AppCompatActivity {

    public static final String ARG_ITEM = "item";
    public static final int ITEM_CREATED = 1;
    public static final int ITEM_EDITED = 2;
    private ActivityDetailviewBinding binding;
    private DetailviewViewmodelImpl viewmodel;

    private static String LOGGER = DetailviewActivity.class.getSimpleName();

    private ActivityResultLauncher<Intent> showContactsLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    onContactSelected(result.getData());
                }
            }
    );

    public DetailviewActivity() {
        Log.i(DetailviewActivity.class.getSimpleName(),"constructor invoked");
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(DetailviewActivity.class.getSimpleName(),"oncreate invoked");

        // A) determine the view
        this.binding = DataBindingUtil.setContentView(this,R.layout.activity_detailview);
        this.binding.setLifecycleOwner(this);

        // B) instantiate or reuse the viewmodel
        this.viewmodel = new ViewModelProvider(this).get(DetailviewViewmodelImpl.class);

        // Has viewmodel been created the first time? then data needs to be set
        if (this.viewmodel.getItem() == null) {
            Log.i(DetailviewActivity.class.getSimpleName(),"viemodel has been created. No item has been set so far.");
            // B) determine the data
            ToDo item = (ToDo)getIntent().getSerializableExtra(ARG_ITEM);
            if (item == null) {
                this.viewmodel.setItem(new ToDo());
            } else {
                Log.i(LOGGER,"got item with contacts: " + item.getContactIds());
                this.viewmodel.setItem(item);
            }
        } else {
            Log.i(DetailviewActivity.class.getSimpleName(),"use item from viewmodel: " + this.viewmodel.getItem());
        }
        this.viewmodel.getSavedOccurred().observe(this, occurred -> {
            onItemSaved();
        });

        // C) pass the data to the view (it will care itself how to deal with them)
        this.binding.setViewmodel(this.viewmodel);
        Log.i(DetailviewActivity.class.getSimpleName(),"errorStatus ( setting viewmodel): " + viewmodel.getErrorStatus().getValue());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(DetailviewActivity.class.getSimpleName(),"onPause invoked");
    }

    public void onItemSaved() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(ARG_ITEM,this.viewmodel.getItem());
        setResult(this.viewmodel.getItem().getId() == 0L ? ITEM_CREATED : ITEM_EDITED,returnIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_detailview_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.addContact) {
            addContactToToDo();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void addContactToToDo() {
        showContactsLauncher.launch(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI));
    }

    private void onContactSelected(Intent result) {
       // Log.i(LOGGER,"onContactSelected(): " + result);
        Uri selectedContactUri = result.getData();
        Log.i(LOGGER,"onContactSelected(): selectedContactUri: " + selectedContactUri);

        Cursor cursor = getContentResolver().query(selectedContactUri,null,null,null,null,null);
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            Log.i(LOGGER,"contactName: " + contactName);
            @SuppressLint("Range") long internalContactId = cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Log.i(LOGGER,"internalContactId: " + internalContactId);
            this.viewmodel.getItem().getContactIds().add(String.valueOf(internalContactId));
            showContactDetailsForInternalId(internalContactId);
        }
    }

    private long lastSelectedInternalContactId = -1;

    public void showContactDetailsForInternalId(long internalContactId) {
        lastSelectedInternalContactId = internalContactId;
        if (hasContactPermission()) {
            Log.i(LOGGER,"showContactDetailsForInternalId(): " + internalContactId);
            Cursor cursor = getContentResolver().query(
                    ContactsContract.Contacts.CONTENT_URI,
                    null,
                    ContactsContract.Contacts._ID + "=?",
                    new String[]{String.valueOf(internalContactId)},
                    null);
            if (cursor.moveToFirst()) {
                @SuppressLint("Range") String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                Log.i(LOGGER,"contactName for id: " + contactName);
            }
            cursor = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                    new String[]{String.valueOf(internalContactId)},
                    null);
            while (cursor.moveToNext()) {
                String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                int phoneNumberType = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                boolean isMobile = (phoneNumberType == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
                Log.i(LOGGER, "phoneNumber: " + phoneNumber );
                Log.i(LOGGER, "isMobile: " + isMobile);
            }
            cursor = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=?",
                    new String[]{String.valueOf(internalContactId)},
                    null);
            while (cursor.moveToNext()) {
                String emailaddr = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
                Log.i(LOGGER,"emailaddr: " + emailaddr);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(LOGGER,"onRequestPermissionResult(): " + Arrays.asList(permissions) + ", " + Arrays.asList(grantResults));
        if (lastSelectedInternalContactId != -1) {
            showContactDetailsForInternalId(lastSelectedInternalContactId);
        }
    }

    public boolean hasContactPermission() {
        int hasReadContactPermission = checkSelfPermission(Manifest.permission.READ_CONTACTS);
        if (hasReadContactPermission == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},1);
        return false;
    }
}

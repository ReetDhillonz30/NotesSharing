package com.inception.harmeetkaur.notessharing;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.inception.harmeetkaur.notessharing.datamodels.notesUrls;

import java.util.ArrayList;
import java.util.List;

public class uploading_images extends AppCompatActivity {
    private static final int RESULT_LOAD_IMAGE = 1;
    private Button mSelectBtn;
    private RecyclerView mUploadList;

    private List<String> fileNameList;
    private List<String> fileDoneList;

    private UploadListAdapter uploadListAdapter;

    private StorageReference mStorage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploading_images);



        mStorage = FirebaseStorage.getInstance().getReference();

        mSelectBtn = (Button) findViewById(R.id.select_images);
        mUploadList = (RecyclerView) findViewById(R.id.recyclerview);

        fileNameList = new ArrayList<>();
        fileDoneList = new ArrayList<>();

        uploadListAdapter = new UploadListAdapter(fileNameList, fileDoneList);

        //RecyclerView

        mUploadList.setLayoutManager(new LinearLayoutManager(this));
        mUploadList.setHasFixedSize(true);
        mUploadList.setAdapter(uploadListAdapter);

        mSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);


            }
        });

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK){

            if(data.getClipData() != null){

                final int totalItemsSelected = data.getClipData().getItemCount();

                final ArrayList<String> images_url = new ArrayList<>();

                final int[] count = {0};

                for(int i = 0; i < totalItemsSelected; i++){

                    Uri fileUri = data.getClipData().getItemAt(i).getUri();

                    String fileName = getFileName(fileUri);

                    fileNameList.add(fileName);
                    fileDoneList.add("uploading");
                    uploadListAdapter.notifyDataSetChanged();

                   final  long currentTime = getIntent().getLongExtra("current_time" , 0);

                    StorageReference fileToUpload = mStorage.child("Images").child(String.valueOf(currentTime)).child(fileName);



                    fileToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {



                            fileDoneList.remove(count[0]);
                            fileDoneList.add(count[0], "done");

                            uploadListAdapter.notifyDataSetChanged();

                           images_url.add(String.valueOf(taskSnapshot.getDownloadUrl())) ;

                            count[0]++;

                           if( count[0] == totalItemsSelected)
                           {
                               FirebaseDatabase database = FirebaseDatabase.getInstance();

                               String url = "";

                               for(int j = 0 ; j < totalItemsSelected ; j ++)
                               {
                                   url = url +","+ images_url.get(j);

                               }
                               notesUrls urls = new notesUrls(url);

                               database.getReference().child("images_url").child(String.valueOf(currentTime)).setValue(urls).addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {

                                       Toast.makeText(uploading_images.this, "Done", Toast.LENGTH_SHORT).show();

                                       finish();

                                   }
                               });
                           }

                        }
                    });

                }

                //Toast.makeText(MainActivity.this, "Selected Multiple Files", Toast.LENGTH_SHORT).show();

            } else if (data.getData() != null){

                Toast.makeText(uploading_images.this, "Selected Single File", Toast.LENGTH_SHORT).show();

            }

        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

}
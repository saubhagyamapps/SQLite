package com.e.demoproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {
    private DatabaseHelper db;

    String imageEncoded;
    private List<UserModel> notesList = new ArrayList<>();
    MaterialBetterSpinner materialBetterSpinner;
    String[] SPINNER_DATA = {"ANDROID", "PHP", "BLOGGER", "WORDPRESS", "JAVA"};
    ChipGroup chipGroup;
    private static final String TAG = "MainActivity";
    TextInputEditText txtName;
    Button btnSubmit;
    Chip chipCycling, chipCooking, chipLearning, chipPainting, chipDrawing, chipDance, chipCamping, chipHiking, chipPhotography, chipChess;
    ArrayList<String> strings;
    private RadioGroup radioGroup;
    String mGender;
    AppCompatImageView imageSelectImages;
    CircleImageView profile_image;
    public static final int MULTIPLE_PERMISSIONS = 10;
    Boolean imagesFlag = false;
    String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(this);
        call_permissions();
        initialization();
    }

    private void initialization() {
        txtName = findViewById(R.id.txtName);
        profile_image = findViewById(R.id.profile_image);
        imageSelectImages = findViewById(R.id.imageSelectImages);
        radioGroup = findViewById(R.id.radioGroup);
        chipDance = findViewById(R.id.chipDance);
        chipDrawing = findViewById(R.id.chipDrawing);
        chipPainting = findViewById(R.id.chipPainting);
        chipLearning = findViewById(R.id.chipLearning);
        chipCooking = findViewById(R.id.chipCooking);
        chipCycling = findViewById(R.id.chipCycling);
        chipCamping = findViewById(R.id.chipCamping);
        chipHiking = findViewById(R.id.chipHiking);
        chipPhotography = findViewById(R.id.chipPhotography);
        chipChess = findViewById(R.id.chipChess);
        btnSubmit = findViewById(R.id.btnSubmit);
        chipGroup = findViewById(R.id.chipGroup);
        setDesignation();
        hobbySelecation();
        selectProfileImage();
        selectGander();
        saveData();
    }

    private void call_permissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(getApplicationContext(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
        }
        return;
    }

    private void saveData() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!imagesFlag) {
                    Toast.makeText(MainActivity.this, "Please select image", Toast.LENGTH_SHORT).show();
                } else if (txtName.getText().toString().isEmpty() || txtName.getText().toString().equals("null")) {
                    Toast.makeText(MainActivity.this, "Please Enter Name", Toast.LENGTH_SHORT).show();
                } else if (mGender == null || mGender.isEmpty() || mGender.equals("null")) {
                    Toast.makeText(MainActivity.this, "Please Select Gender", Toast.LENGTH_SHORT).show();
                } else if (materialBetterSpinner.getText().toString().isEmpty() || materialBetterSpinner.getText().toString().equals("null")) {
                    Toast.makeText(MainActivity.this, "Please Select Designation", Toast.LENGTH_SHORT).show();
                } else if (strings == null || strings.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please Select Hobby", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "Spinner item: " + materialBetterSpinner.getText().toString());
                    Log.e(TAG, "hobby: " + strings);
                    Log.e(TAG, "Name: " + txtName.getText().toString());
                    Log.e(TAG, "Gender: " + mGender);
                    createNote(txtName.getText().toString(), mGender, materialBetterSpinner.getText().toString(), strings.toString(), imageEncoded);
                    Intent intent = new Intent(MainActivity.this, ViewActivity.class);
                    startActivity(intent);
                }

            }
        });


    }

    private void selectGander() {

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
                    Toast.makeText(MainActivity.this, rb.getText(), Toast.LENGTH_SHORT).show();
                    mGender = rb.getText().toString();
                }

            }
        });
    }

    private void selectProfileImage() {
        imageSelectImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                selectImage();

            }
        });
    }

    private void setDesignation() {
        strings = new ArrayList<>();
        materialBetterSpinner = findViewById(R.id.material_spinner1);
        Log.e(TAG, "onCreate: ");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_dropdown_item_1line, SPINNER_DATA);
        materialBetterSpinner.setAdapter(adapter);
    }

    private void hobbySelecation() {
        chipDance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.e(TAG, "onCheckedChanged: " + isChecked + "--->" + buttonView.getText().toString());
                    strings.add(buttonView.getText().toString());
                } else {
                    strings.remove(buttonView.getText().toString());
                }
                Log.e(TAG, "onCheckedChanged: " + strings.toString());
            }
        });
        chipDrawing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.e(TAG, "onCheckedChanged: " + isChecked + "--->" + buttonView.getText().toString());
                    strings.add(buttonView.getText().toString());
                } else {
                    strings.remove(buttonView.getText().toString());
                }
                Log.e(TAG, "onCheckedChanged: " + strings.toString());
            }
        });

        chipPainting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.e(TAG, "onCheckedChanged: " + isChecked + "--->" + buttonView.getText().toString());
                    strings.add(buttonView.getText().toString());
                } else {
                    strings.remove(buttonView.getText().toString());
                }
                Log.e(TAG, "onCheckedChanged: " + strings.toString());
            }
        });
        chipLearning.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.e(TAG, "onCheckedChanged: " + isChecked + "--->" + buttonView.getText().toString());
                    strings.add(buttonView.getText().toString());
                } else {
                    strings.remove(buttonView.getText().toString());
                }
                Log.e(TAG, "onCheckedChanged: " + strings.toString());
            }
        });
        chipCooking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.e(TAG, "onCheckedChanged: " + isChecked + "--->" + buttonView.getText().toString());
                    strings.add(buttonView.getText().toString());
                } else {
                    strings.remove(buttonView.getText().toString());
                }
                Log.e(TAG, "onCheckedChanged: " + strings.toString());
            }
        });
        chipCycling.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.e(TAG, "onCheckedChanged: " + isChecked + "--->" + buttonView.getText().toString());
                    strings.add(buttonView.getText().toString());
                } else {
                    strings.remove(buttonView.getText().toString());
                }
                Log.e(TAG, "onCheckedChanged: " + strings.toString());
            }
        });
        chipCamping.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.e(TAG, "onCheckedChanged: " + isChecked + "--->" + buttonView.getText().toString());
                    strings.add(buttonView.getText().toString());
                } else {
                    strings.remove(buttonView.getText().toString());
                }
                Log.e(TAG, "onCheckedChanged: " + strings.toString());
            }
        });
        chipHiking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.e(TAG, "onCheckedChanged: " + isChecked + "--->" + buttonView.getText().toString());
                    strings.add(buttonView.getText().toString());
                } else {
                    strings.remove(buttonView.getText().toString());
                }
                Log.e(TAG, "onCheckedChanged: " + strings.toString());
            }
        });
        chipPhotography.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.e(TAG, "onCheckedChanged: " + isChecked + "--->" + buttonView.getText().toString());
                    strings.add(buttonView.getText().toString());
                } else {
                    strings.remove(buttonView.getText().toString());
                }
                Log.e(TAG, "onCheckedChanged: " + strings.toString());
            }
        });
        chipChess.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.e(TAG, "onCheckedChanged: " + isChecked + "--->" + buttonView.getText().toString());
                    strings.add(buttonView.getText().toString());
                } else {
                    strings.remove(buttonView.getText().toString());
                }
                Log.e(TAG, "onCheckedChanged: " + strings.toString());
            }
        });

    }

    private void createNote(String name, String mGender, String post, String hobby, String images) {
        long id = db.insertNote(name, mGender, post, hobby, images);
        UserModel n = db.getNote(id);
        if (n != null) {
            notesList.add(0, n);
        }
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);

                    Uri tempUri = getImageUri(getApplicationContext(), bitmap);

                    File finalFile = new File(getRealPathFromURI(tempUri));
                    profile_image.setImageBitmap(BitmapHelper.decodeFile(String.valueOf(finalFile), 200, 200, true));
                    Bitmap bitmap1 = ((BitmapDrawable) profile_image.getDrawable()).getBitmap();
                    encodeTobase64(bitmap1);
                    Log.e(TAG, "onActivityResult: " + finalFile);
                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";


                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);

                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                profile_image.setImageBitmap(BitmapHelper.decodeFile(picturePath, 200, 200, true));
                Bitmap bitmap = ((BitmapDrawable) profile_image.getDrawable()).getBitmap();
                encodeTobase64(bitmap);
            }
        }
    }

    public String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 50, baos);
        byte[] b = baos.toByteArray();
        imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        Log.e(TAG, "encodeTobase64: ----->Done");
        imagesFlag = true;
        return imageEncoded;
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }
}

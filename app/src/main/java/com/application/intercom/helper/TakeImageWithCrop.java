package com.application.intercom.helper;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;


import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileOutputStream;

public class TakeImageWithCrop extends Activity {
    public static final int RECORD_PERMISSION_REQUEST_CODE = 1;
    public static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 2;
    public static final int CAMERA_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 4;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 3;
    public static String path = "";
    public Uri image_uri  = null;

    private final String capture_dir = Environment.getExternalStorageDirectory() + "/BountyBunch/Images/";
    public final static int CAMERA_REQUEST = 1888;
    public final static int GALLERY_REQUEST = 1;
    MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);
    Uri imageFileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            String image_from = b.getString("from");
            if (image_from.equalsIgnoreCase("camera")) {
                File file = new File(capture_dir);
                if (!file.exists()) {
                    file.mkdirs();
                }
                path = capture_dir + System.currentTimeMillis() + ".jpg";
//				imageFileUri = Uri.fromFile(new File(path));
//				imageFileUri = FileProvider.getUriForFile(TakeImage.this,BuildConfig.APPLICATION_ID + ".provider",new File(path));
                imageFileUri = FileProvider.getUriForFile(TakeImageWithCrop.this, "com.application.intercom.android.fileprovider", new File(path));

                if (!marshMallowPermission.checkPermissionForCamera()) {
                    marshMallowPermission.requestPermissionForCamera();
                } else {
//					Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "New Picture");
                    values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
                    image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            } else if (image_from.equalsIgnoreCase("gallery")) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                intent.setType("image/*");

                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(intent, GALLERY_REQUEST);
              /*  if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                    marshMallowPermission.requestPermissionForExternalStorage();
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    intent.setType("image/*");

                    intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                    startActivityForResult(intent, GALLERY_REQUEST);
                }*/
            }
        }
    }

    //	compressing image
    private String compressImage(Bitmap finalBitmap, int width, int height) {
        Bitmap bitmap = Bitmap.createScaledBitmap(finalBitmap, width / 2, height / 2, true);
        File myDir = new File(capture_dir);
        myDir.mkdirs();
//		Random generator = new Random();
//		int n = 10000;
//		n = generator.nextInt(n);
        File file = new File(myDir, System.currentTimeMillis() + ".png");
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, out);//	compressing to 50%
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file.getPath();
    }

    private void savebitmap(final Bitmap img, final String imagePath) {
        try {
            final File f = new File(imagePath);
            if (f.isFile()) {
                f.delete();
            }
            img.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(new File(imagePath)));
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    //	oreintaion of image
    private String checkOrientation(final String path) {
        String finalpath = "";
        try {
//			final Options options = new Options();
//			options.inJustDecodeBounds = true;
//			BitmapFactory.decodeFile(path, options);
//			final int sample = Math.min(options.outWidth / 500,
//					options.outHeight / 250);
//			options.inSampleSize = sample;
//			options.inJustDecodeBounds = false;
//			options.inTempStorage = new byte[16 * 1024];
//			Bitmap bm = BitmapFactory.decodeFile(path, options);

            Bitmap bm = BitmapFactory.decodeFile(path);
            int orientation = 0;
            try {
                final ExifInterface exif = new ExifInterface(path);
                final String exifOrientation = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
                orientation = Integer.valueOf(exifOrientation);
            } catch (final Exception e) {
                e.printStackTrace();
            }
            final Matrix mat = new Matrix();
            if (orientation == 1) {
                mat.postRotate(0);
            } else if (orientation == 6) {
                mat.postRotate(90);
            } else if (orientation == 8) {
                mat.postRotate(270);
            } else if (orientation == 3) {
                mat.postRotate(180);
            }
            Log.i("Orientation", "" + orientation);
            bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), mat, true);
            finalpath = compressImage(bm, bm.getWidth(), bm.getHeight());
            bm.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalpath;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //	result data
//	Intent { dat=content://media/external/images/media/491163 flg=0x1 (has extras) }( uri response )
//	Intent { dat=file:///storage/emulated/0/DCIM/Camera/IMG_20170625_192838.jpg typ=image/jpeg flg=0x1 }( file response in mi)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQUEST) {

                Uri selectedImage = data.getData();
                performCrop(selectedImage);


            }  else  if(requestCode== Crop.REQUEST_CROP){
                try {
                    Uri selectedImage = Crop.getOutput(data);

                    Cursor cursor = null;
                    if (selectedImage != null) {
                        if (selectedImage.getScheme().equals("file")) {
                            path = selectedImage.getPath();
                        } else {
                            cursor = getContentResolver().query(
                                    selectedImage,
                                    new String[]{MediaStore.Images.ImageColumns.DATA},
                                    null,
                                    null,
                                    null);
                            cursor.moveToFirst();
                            path = cursor.getString(0);
                        }
                        Bitmap bm = BitmapFactory.decodeFile(path);
                        Log.i("Gallery pixels--->", bm.getWidth() + " " + bm.getHeight());
                        //String finalpath = compressImage(bm, bm.getWidth(), bm.getHeight());
                        bm.recycle();
                        Intent intent = new Intent();
                        intent.putExtra("filePath", path);
                        setResult(RESULT_OK, intent);
                        if (cursor != null) {
                            cursor.close();
                        }
                        finish();
                    }
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                    path = "";
                    setResult(RESULT_CANCELED);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                    path = "";
                    setResult(RESULT_CANCELED);
                    finish();
                }

            }
            else   if (requestCode == CAMERA_REQUEST) {
                try {
                    // Bitmap bm = BitmapFactory.decodeFile(path);
                    //Log.i("Camera pixels--->", bm.getWidth() + " " + bm.getHeight());
                    // path = checkOrientation(path);
                    //bm.recycle();

                    performCrop(image_uri);

                    // Intent intent = new Intent();
                    // intent.putExtra("filePath",  Utils.getPathForMediaUri(this,image_uri));
                    // setResult(RESULT_OK, intent);
                    //  finish();
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                    path = "";
                    setResult(RESULT_CANCELED);
                    finish();
                } catch (final Exception e) {
                    e.printStackTrace();
                    path = "";
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }
        } else {
            path = "";
            setResult(RESULT_CANCELED);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void performCrop(Uri picUri) {

        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"+System.currentTimeMillis() + ".png"));
        Crop.of(picUri, destination).asSquare().start(this);
        //Crop.of(picUri, destination).withAspect(0,0).start(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                        marshMallowPermission.requestPermissionForExternalCameraStorage();
                    } else {
                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, "New Picture");
                        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
                        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }
                } else {
                    onBackPressed();
                }
                return;
            }
            case CAMERA_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "New Picture");
                    values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
                    image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                } else {
                    onBackPressed();
                }
                return;
            }
            case EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                    startActivityForResult(intent, GALLERY_REQUEST);
                } else {
                    onBackPressed();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    //	permissions
    public class MarshMallowPermission {
        Activity activity;

        public MarshMallowPermission(Activity activity) {
            this.activity = activity;
        }

        public boolean checkPermissionForRecord() {
            int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);
            return result == PackageManager.PERMISSION_GRANTED;
        }

        public boolean checkPermissionForExternalStorage() {
            int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }

        public boolean checkPermissionForCamera() {
            int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
            return result == PackageManager.PERMISSION_GRANTED;
        }

        public void requestPermissionForRecord() {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(activity, "Microphone permission needed for recording. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_PERMISSION_REQUEST_CODE);
            }
        }

        public boolean requestPermissionForExternalStorage() {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(activity, "External Storage permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
            }
            return true;
        }

        public boolean requestPermissionForExternalCameraStorage() {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(activity, "External Storage permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
            }
            return true;
        }

        public boolean requestPermissionForCamera() {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
                Toast.makeText(activity, "Camera permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            }
            return true;
        }
    }

}

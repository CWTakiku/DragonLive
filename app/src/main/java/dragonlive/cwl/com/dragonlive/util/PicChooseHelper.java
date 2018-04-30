package dragonlive.cwl.com.dragonlive.util;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.tencent.TIMUserProfile;

import java.io.File;

import dragonlive.cwl.com.dragonlive.application.MyApplication;
import dragonlive.cwl.com.dragonlive.editprofile.PicChooseDialog;

/**
 * Created by cwl on 2018/4/23.
 */

public class PicChooseHelper {
     private Activity mActivity;
    private Fragment mFragment;
    private Uri mCameraFileUri;
    private static final int FROM_CAMERA = 2;
    private static final int FROM_ALBUM = 1;
    private static final int CROP = 0;
    private TIMUserProfile mUserProfile;

    PicType mPicType;
    public static enum PicType {
        Avatar, Cover
    }
    public PicChooseHelper( Activity mActivity,PicType pictype){
        this.mActivity=mActivity;
        this.mPicType=pictype;
        mUserProfile = MyApplication.getApplication().getSelfProfile();
    }
    public PicChooseHelper(Fragment fragment, PicType picType) {
        mFragment = fragment;
        mActivity = fragment.getActivity();
        mPicType = picType;
        mUserProfile = MyApplication.getApplication().getSelfProfile();
    }
    public void showPicChooseDialog(){
        PicChooseDialog dialog=new PicChooseDialog(mActivity);
        dialog.setOnDialogClickListener(new PicChooseDialog.OnDialogClickListener() {
            @Override
            public void onCamera() {
                //拍照
                takePicFromCamera();
            }

            @Override
            public void onAlbum() {
              //从相册中选择
                takePicFromAlbum();
            }
        });
        dialog.show();
    }

    private void takePicFromAlbum() {

        Intent intent = new Intent(Intent.ACTION_PICK, null);
        // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型" 所有类型则写 "image/*"
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        //startActivityForResult(intentToPickPic, GlobalVariable.GALLERY_REQUEST_CODE);

        //Intent intent=new Intent("android.intent.action.GET_CONTENT"); //此方法返回的URI没有路径,只有图片编号的uri
       // intent.setType("image/*");
        if (mFragment!=null){
            mFragment.startActivityForResult(intent,FROM_ALBUM);
        }else {
            mActivity.startActivityForResult(intent,FROM_ALBUM);
        }
    }

    private void takePicFromCamera() {

       mCameraFileUri=createUri("");
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        int currentApiVersion= Build.VERSION.SDK_INT;
        if (currentApiVersion<24){
            //小于7.0版本
            intent.putExtra(MediaStore.EXTRA_OUTPUT,mCameraFileUri);
            if (mFragment!=null){
                mFragment.startActivityForResult(intent,FROM_CAMERA);
            }else {
              mActivity.startActivityForResult(intent,FROM_CAMERA);
            }
        }else {
            //大于7.0版本
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA,mCameraFileUri.getPath());
            Uri uri= getImageContentUri(mCameraFileUri);

            intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
            if (mFragment == null) {
                mActivity.startActivityForResult(intent, FROM_CAMERA);
            } else {
                mFragment.startActivityForResult(intent, FROM_CAMERA);
            }
        }
      
    }
    /**
     * 转换 content:// uri
     */
    private Uri getImageContentUri(Uri uri) {
        String filePath = uri.getPath();
        Cursor cursor = mActivity.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, filePath);
            return mActivity.getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        }
    }

    //得到URI
    private Uri createUri(String name) {
        String filename;
        String dirPath= Environment.getExternalStorageDirectory()+"/"+mActivity.getApplication().getApplicationInfo().packageName;
        File dir=new  File(dirPath);
        if (!dir.exists()){
            dir.mkdir();
        }
        String id="";
        if (mUserProfile!=null){
            id=mUserProfile.getIdentifier();
        }
        if (!TextUtils.isEmpty(name)) {
           filename = id + name + ".jpg";
        }
        else {
           filename = id  + ".jpg";
        }
        File file=new File(dir,filename);
        if (file.exists()){
            file.delete();
        }
        return Uri.fromFile(file);
    }
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        if (requestCode == FROM_ALBUM) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                startCrop(uri);
            }
        }
        if (requestCode == FROM_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                startCrop(mCameraFileUri);
            }
        }
        if (requestCode == CROP) {
            if (resultCode == Activity.RESULT_OK) {
                //剪切完毕
                String id = "";
                if (mUserProfile != null) {
                    id = mUserProfile.getIdentifier();
                }
                String name = "";
                if (mPicType == PicType.Avatar) {
                    name = id + "_" + System.currentTimeMillis() + "_avatar";
                    //上传阿里OSS
                    AliYunOssHelper aliYunOssHelper = new AliYunOssHelper();
                    aliYunOssHelper.setOnResultListener(new AliYunOssHelper.OnResultListener() {
                        @Override
                        public void onSuccess(String url) {
                            //Log.i("info1", "onSuccess1: ");
                            mOnChooserResultListener.onSuccess(url);
                        }

                        @Override
                        public void onFailure(String msg) {
                            // Log.i("info1", "onFailure1: ");
                            mOnChooserResultListener.onFail(msg);
                        }
                    });
                    aliYunOssHelper.uploadToOSS("dragonlive", name, cropUri.getPath());
                }
                else if (mPicType == PicType.Cover) {
                   // name = id + "_" + System.currentTimeMillis() + "_cover";
                    mOnChooserResultListener.onSuccess(cropUri.getPath());
                }
            }else mOnChooserResultListener.onFail("选择失败");
        }
    }
    private Uri cropUri = null;
    private void startCrop(Uri uri) {
       // Log.i("info1", "uri: "+uri.toString());
        cropUri=createUri("_crop");
        ///Log.i("info1", "startCrop: "+cropUri);
        Intent intent=new Intent("com.android.camera.action.CROP");
        intent.putExtra("crop",true);
        if (mPicType==PicType.Avatar){
            intent.putExtra("aspectX", 300);
            intent.putExtra("aspectY", 300);
            intent.putExtra("outputX", 300);
            intent.putExtra("outputY", 300);
        }else if (mPicType == PicType.Cover){
            intent.putExtra("aspectX", 500);
            intent.putExtra("aspectY", 300);
            intent.putExtra("outputX", 500);
            intent.putExtra("outputY", 300);
        }
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion < 24) {
            //小于7.0的版本
            intent.setDataAndType(uri, "image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);
            if (mFragment == null) {
                mActivity.startActivityForResult(intent, CROP);
            } else {
                mFragment.startActivityForResult(intent, CROP);
            }
        } else {
            //大于7.0的版本
            {
                String scheme = uri.getScheme();
                if (scheme.equals("content")) {
                    Uri contentUri = uri;
                    intent.setDataAndType(contentUri, "image/*");
                } else {
                    Uri contentUri = getImageContentUri(uri);
                    intent.setDataAndType(contentUri, "image/*");
                }
            }

            intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);
            if (mFragment == null) {
                mActivity.startActivityForResult(intent, CROP);
            } else {
                mFragment.startActivityForResult(intent, CROP);
            }
        }
    }
    public interface OnChooseResultListener {
        void onSuccess(String url);

        void onFail(String msg);
    }

    private OnChooseResultListener mOnChooserResultListener;

    public void setOnChooseResultListener(OnChooseResultListener l) {
        mOnChooserResultListener = l;
    }
}

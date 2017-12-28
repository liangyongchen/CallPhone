package com.asen.callphone.base.util;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.asen.callphone.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * 使用 compile 'com.nostra13.universalimageloader:universal-image-loader:1.9.4' 框架处理图片样式
 * Created by asus on 2017/7/21.
 */

public class ImageDisplayUtil {

    //region
    // endregion

    /**
     * 在初始化程序的时候初始化 ImageLoaderConfiguration 参数
     */
    public static void initImageLoaderConfiguration(Context context) {

        //region 方案一

//        File cacheDir = StorageUtils.getCacheDirectory(context);  //缓存文件夹路径
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
//                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions 内存缓存文件的最大长宽
//                // .diskCacheExtraOptions(480, 800, null)  // 本地缓存的详细信息(缓存的最大长宽)，最好不要设置这个
//                // .taskExecutor(...)
//                // .taskExecutorForCachedImages(...)
//                // .threadPoolSize(3) // default  线程池内加载的数量
//                .threadPriority(Thread.NORM_PRIORITY - 2) // default 设置当前线程的优先级
//                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
//                .denyCacheImageMultipleSizesInMemory()
//                .memoryCache(new LruMemoryCache(2 * 1024 * 1024)) // 可以通过自己的内存缓存实现
//                .memoryCacheSize(2 * 1024 * 1024)  // 内存缓存的最大值
//                .memoryCacheSizePercentage(13) // default
//                // .diskCache(new UnlimitedDiscCache(cacheDir)) // default 可以自定义缓存路径
//                .diskCacheSize(50 * 1024 * 1024) // 50 Mb sd卡(本地)缓存的最大值
//                .diskCacheFileCount(100)  // 可以缓存的文件数量
//                // default为使用HASHCODE对UIL进行加密命名， 还可以用MD5(new Md5FileNameGenerator())加密
//                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
//                .imageDownloader(new BaseImageDownloader(context)) // default
//                //.imageDecoder(new BaseImageDecoder()) // default
//                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
//                .writeDebugLogs() // 打印debug log
//                .build(); //开始构建

        // endregion

        //region 方案二

        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.memoryCacheSize(2 * 1024 * 1024);  // 内存缓存的最大值
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());

        // endregion


    }


    /**
     * 设置圆形图片, r : 设置半径，默认半径为5
     */
    public static DisplayImageOptions setCircleBitmapDisplayer(int r) {
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new CircleBitmapDisplayer(Color.WHITE, r <= 0 ? 5 : r))
                .build();
    }


    /**
     * 设置普通矩形图片加载
     */
    public static DisplayImageOptions setRectBitmapDisplayer() {
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }


    /**
     * 设置矩形图片加载， durationMillis ：
     */
    public static DisplayImageOptions setFadeInBitmapDisplayer(int durationMillis) {
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new CircleBitmapDisplayer(Color.WHITE, durationMillis <= 0 ? 300 : durationMillis))
                .build();
    }


    /**
     * 显示图片的所有配置
     */
    private DisplayImageOptions getWholeOptions() {
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.ic_empty)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.ic_error)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
                //.decodingOptions(BitmapFactory.Options decodingOptions)//设置图片的解码配置
                .delayBeforeLoading(0)//int delayInMillis为你设置的下载前的延迟时间
                //设置图片加入缓存前，对bitmap进行设置
                //.preProcessor(BitmapProcessor preProcessor)
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .displayer(new RoundedBitmapDisplayer(20))//不推荐用！！！！是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间，可能会出现闪动
                .build();//构建完成;
    }


    /**
     * 设置常用的设置项
     */
    private DisplayImageOptions getSimpleOptions() {
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.ic_empty)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.ic_error)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
                .build();//构建完成;
    }


    // region  从不同路径加载图片


    /**
     * 从内存卡中异步加载本地图片（圆形）
     *
     * @param uri
     * @param imageView
     */
    public static void displayFromSDCard(Context context, Uri uri, ImageView imageView) {
        // String imageUri = "file:///mnt/sdcard/image.png"; // from SD card
        ImageLoader.getInstance().displayImage("file://" + getRealPathFromUri(context, uri), imageView, ImageDisplayUtil.setCircleBitmapDisplayer(5), new ImageDisplayUtil.AnimateFirstDisplayListener()); // 默认
    }


    /**
     * 从内存卡中异步加载本地图片（圆形）
     *
     * @param path
     * @param imageView
     */
    public static void displayFromSDCard(Context context, String path, ImageView imageView) {
        // String imageUri = "file:///mnt/sdcard/image.png"; // from SD card
        ImageLoader.getInstance().displayImage("file://" + path, imageView, ImageDisplayUtil.setCircleBitmapDisplayer(5), new ImageDisplayUtil.AnimateFirstDisplayListener()); // 默认
    }


    /**
     * 从assets文件夹中异步加载图片
     *
     * @param imageName 图片名称，带后缀的，例如：1.png
     * @param imageView
     */
    public void dispalyFromAssets(String imageName, ImageView imageView) {
        // String imageUri = "assets://image.png"; // from assets
        ImageLoader.getInstance().displayImage("assets://" + imageName, imageView, ImageDisplayUtil.setCircleBitmapDisplayer(5), new ImageDisplayUtil.AnimateFirstDisplayListener());
    }


    /**
     * 从drawable中异步加载本地图片
     *
     * @param imageId
     * @param imageView
     */
    public void displayFromDrawable(int imageId, ImageView imageView) {
        // String imageUri = "drawable://" + R.drawable.image; // from drawables
        // (only images, non-9patch)
        ImageLoader.getInstance().displayImage("drawable://" + imageId, imageView, ImageDisplayUtil.setCircleBitmapDisplayer(5), new ImageDisplayUtil.AnimateFirstDisplayListener());
    }


    /**
     * 从内容提提供者中抓取图片
     */
    public void displayFromContent(String uri, ImageView imageView) {
        // String imageUri = "content://media/external/audio/albumart/13"; //
        // from content provider
        ImageLoader.getInstance().displayImage("content://" + uri, imageView, ImageDisplayUtil.setCircleBitmapDisplayer(5), new ImageDisplayUtil.AnimateFirstDisplayListener());
    }


    // endregion


    // region // SD卡的图片加载处理返回 String 路径


    /**
     * 根据Uri获取图片的绝对路径
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    public static String getRealPathFromUri(Context context, Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion >= 19) { // api >= 19
            return getRealPathFromUriAboveApi19(context, uri);
        } else { // api < 19
            return getRealPathFromUriBelowAPI19(context, uri);
        }
    }


    /**
     * 适配api19以下(不包括api19),根据uri获取图片的绝对路径
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    private static String getRealPathFromUriBelowAPI19(Context context, Uri uri) {
        return getDataColumn(context, uri, null, null);
    }


    /**
     * 适配api19及以上,根据uri获取图片的绝对路径
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    @SuppressLint("NewApi")
    private static String getRealPathFromUriAboveApi19(Context context, Uri uri) {
        String filePath = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是document类型的 uri, 则通过document id来进行处理
            String documentId = DocumentsContract.getDocumentId(uri);
            if (isMediaDocument(uri)) { // MediaProvider
                // 使用':'分割
                String id = documentId.split(":")[1];

                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = {id};
                filePath = getDataColumn(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, selectionArgs);
            } else if (isDownloadsDocument(uri)) { // DownloadsProvider
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
                filePath = getDataColumn(context, contentUri, null, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是 content 类型的 Uri
            filePath = getDataColumn(context, uri, null, null);
        } else if ("file".equals(uri.getScheme())) {
            // 如果是 file 类型的 Uri,直接获取图片对应的路径
            filePath = uri.getPath();
        }
        return filePath;
    }


    /**
     * 获取数据库表中的 _data 列，即返回Uri对应的文件路径
     *
     * @return
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String path = null;

        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndex);
                String aa = "";
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }


    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is MediaProvider
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is DownloadsProvider
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }


    //endregion


    /**
     * 图片处理样式
     */
    public static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }


}

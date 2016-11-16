package dataloading;

import android.widget.ImageView;

public class AsyncImageLoader {

    public static void setImageToImageView(String imagePath, ImageView imageView) {

        ImageLoaderAsyncTask task = new ImageLoaderAsyncTask(imageView);
        task.execute(imagePath);

    }

    public static void setImageToImageView(String imagePath, ImageView imageView, int targetWidth, int targetHeight) {

        ImageLoaderAsyncTask task = new ImageLoaderAsyncTask(imageView, targetWidth, targetHeight);
        task.execute(imagePath);

    }

}
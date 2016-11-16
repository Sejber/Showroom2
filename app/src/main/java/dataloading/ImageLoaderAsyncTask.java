package dataloading;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.File;

class ImageLoaderAsyncTask extends AsyncTask<String, Integer, Bitmap> {

    private ImageView imageView;
    private int reqWidth;
    private int reqHeight;

    /**
     * This constructor should only be used if the image view is already displayed.
     * @param imageView The image view that will display the loaded image.
     */
    ImageLoaderAsyncTask(ImageView imageView) {
        this.imageView = imageView;
        reqWidth = imageView.getWidth();
        reqHeight = imageView.getHeight();
    }

    ImageLoaderAsyncTask(ImageView imageView, int targetWidth, int targetHeight) {
        this.imageView = imageView;
        this.reqWidth = targetWidth;
        this.reqHeight = targetHeight;
    }

    protected Bitmap doInBackground(String... images) {

        File imgFile = new File(images[0]);

        if (imgFile.exists()) {
            return decodeSampledBitmapFromResource(images[0], reqWidth, reqHeight);
        } else {
            return null;
        }

    }

    protected void onPostExecute(Bitmap image) {
        imageView.setImageBitmap(image);
    }

    private static Bitmap decodeSampledBitmapFromResource(String path, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

}
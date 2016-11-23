package dataloading;

/*********************************************************************/
/**  Dateiname: ImageLoaderAsyncTask.java                           **/
/**                                                                 **/
/**  Beschreibung:  Ladet und skaliert Bilder für Performance       **/
/**                                                                 **/
/**  Autoren: Frederik Wagner, Lukas Schultt, Leunar Kalludra,      **/
/**           Jonathan Lessing, Marcel Vetter, Leopold Ormos        **/
/**           Merlin Baudert, Rino Grupp, Hannes Kececi             **/
/**                                                                 **/
/*********************************************************************/

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.File;

class ImageLoaderAsyncTask extends AsyncTask<String, Integer, Bitmap> {

    private static final double TARGET_SIZE = 3 * 1024.0 * 1024.0; //2 MB

    private ImageView imageView;
    private int reqWidth = 0;
    private int reqHeight = 0;

    ImageLoaderAsyncTask(ImageView imageView) {
        this.imageView = imageView;
    }

    ImageLoaderAsyncTask(ImageView imageView, int targetWidth, int targetHeight) {
        this.imageView = imageView;
        this.reqWidth = targetWidth;
        this.reqHeight = targetHeight;
    }

    protected Bitmap doInBackground(String... images) {

        if (images[0] == null)
            return null;

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

        if (reqWidth != 0 && reqHeight != 0) {
            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        } else {
            //To minimize the risk of an OutOfMemoryException, scale the bitmaps down so that
            //they will only need roughly 1 MB
            options.inSampleSize = calcSampleSizeTo1Mb(options);
        }

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

    private static int calcSampleSizeTo1Mb(BitmapFactory.Options options) {
        int totalSize = options.outWidth * options.outHeight * 4;
        int sampleSize = (int)Math.round(totalSize / TARGET_SIZE);
        if (sampleSize < 1)
            sampleSize = 1;
        return sampleSize;
    }

}
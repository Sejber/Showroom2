package dataloading;

/*********************************************************************/
/**  Dateiname: AsyncImageLoade.java                                **/
/**                                                                 **/
/**  Beschreibung:  Lädt Bilder in eine ImageView                   **/
/**                                                                 **/
/**  Autoren: Frederik Wagner, Lukas Schultt, Leunar Kalludra,      **/
/**           Jonathan Lessing, Marcel Vetter, Leopold Ormos        **/
/**           Merlin Baudert, Rino Grupp, Hannes Kececi             **/
/**                                                                 **/
/*********************************************************************/

import android.util.Log;
import android.widget.ImageView;

public class AsyncImageLoader {

    public static void setImageToImageView(String imagePath, ImageView imageView) {

        Log.w("ImageLoader", "Please try to avoid using setImageToImageView without specifying the target width and height.");
        Log.w("ImageLoader", "This will cost a lot of memory.");
        ImageLoaderAsyncTask task = new ImageLoaderAsyncTask(imageView);
        task.execute(imagePath);

    }

    public static void setImageToImageView(String imagePath, ImageView imageView, int targetWidth, int targetHeight) {

        if (targetWidth <= 0 || targetHeight <= 0) {
            Log.w("ImageLoader", "Please use different values than 0 for target width and height.");
            Log.w("ImageLoader", "This will cost a lot of memory.");
        }
        ImageLoaderAsyncTask task = new ImageLoaderAsyncTask(imageView, targetWidth, targetHeight);
        task.execute(imagePath);

    }

}
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
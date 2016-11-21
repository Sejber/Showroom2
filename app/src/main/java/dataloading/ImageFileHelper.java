package dataloading;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Freddy on 19.11.2016.
 *
 */

public class ImageFileHelper {

    public static File copyFileToDirectory(Context c, File image, File directory) {
        //check if selected file is actually an image
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(image.getAbsolutePath(), options);
        if (options.outWidth <= 0 || options.outHeight <= 0) {
            Toast.makeText(c, "Die Datei hat ein ungültiges Bildformat.", Toast.LENGTH_LONG).show();
            return null;
        }


        String filename = image.getAbsolutePath().substring(image.getAbsolutePath().lastIndexOf("/"));
        File fileInProjectDir = new File(directory, filename);

        //check if file already exists
        if (!fileInProjectDir.exists()) {
            //copy image to project directory
            try {
                InputStream in = new FileInputStream(image);
                OutputStream out = new FileOutputStream(fileInProjectDir);

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }

                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(c, "Konnte Bild nicht kopieren", Toast.LENGTH_LONG).show();
                return null;
            }
        }

        return fileInProjectDir;

    }

}

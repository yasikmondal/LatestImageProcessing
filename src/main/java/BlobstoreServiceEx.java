import java.io.IOException;
import java.nio.ByteBuffer;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;

public class BlobstoreServiceEx {
	
	public static String bucketName ="laykart-165108.appspot.com";
	public static void main(String[] args) {
		
		ImagesService imagesService = ImagesServiceFactory.getImagesService();
		//Allows creating and accessing files in Google Cloud Storage.
	 	  final GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
	 	      .initialRetryDelayMillis(10)
	 	      .retryMaxAttempts(10)
	 	      .totalRetryPeriodMillis(15000)
	 	      .build());
	 	  // [END gcs]

		// Make an image from a Cloud Storage object, and transform it.
		BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		BlobKey blobKey = blobstoreService.createGsBlobKey("/gs/" + bucketName + "/image.jpeg");
		Image blobImage = ImagesServiceFactory.makeImageFromBlob(blobKey);
		Transform rotate = ImagesServiceFactory.makeRotate(90);
		Image rotatedImage = imagesService.applyTransform(rotate, blobImage);

		// Write the transformed image back to a Cloud Storage object.
		try {
			gcsService.createOrReplace(
			    new GcsFilename(bucketName, "resizedImage_100X50.jpeg"),
			    new GcsFileOptions.Builder().mimeType("image/jpeg").build(),
			    ByteBuffer.wrap(rotatedImage.getImageData()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		                

		
		// TODO Auto-generated method stub

	}

}

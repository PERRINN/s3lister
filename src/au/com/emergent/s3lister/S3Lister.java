package au.com.emergent.s3lister;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;


public class S3Lister {
	private static String bucketName = "perrapp-dist";
	private static String prefixName = "jobs/client-android";
	
	public static void main(String[] args) {
		AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
		try {
			System.out.println("<html>");
			System.out.println("<head>\n<title>Client Releases</title>\n</head>");
			System.out.println("<body>\n");
			final ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucketName).withPrefix(prefixName).withMaxKeys(2);
			ListObjectsV2Result result;
			do {
				result = s3client.listObjectsV2(req);
				
				for(S3ObjectSummary objectSummary : result.getObjectSummaries()) {
					System.out.println("<a href=\"" + objectSummary.getKey() + "\">" + objectSummary.getKey() + "</a> - " + objectSummary.getLastModified() + " - " + String.valueOf(objectSummary.getSize()) + " bytes<br />");
				}
				req.setContinuationToken(result.getNextContinuationToken());
			} while (result.isTruncated() == true);
			System.out.println("</body>");
			System.out.println("</html>");
		}
		catch(AmazonServiceException ase) {
			System.err.println("Error Message: " + ase.getMessage());
			System.err.println("HTTP Status Code: " + ase.getStatusCode());
			System.err.println("AWS Error Code :" + ase.getErrorCode());
			System.err.println("Request Id: " + ase.getRequestId());
		}
	}

}

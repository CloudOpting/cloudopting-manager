package eu.cloudopting.util;

import eu.cloudopting.exception.CommonException;
import eu.medsea.mimeutil.MimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.*;

/**
 * Utility class to work with streams
 */
public class StreamUtil {

    private static final Logger log = LoggerFactory.getLogger(StreamUtil.class);

    private StreamUtil() {
        throw new ExceptionInInitializerError("This is a utility class");
    }

    public static byte[] getBytesFromInputStream(InputStream inStream) {
        // Get the size of the file
        long streamLength = 0;
        byte[] bytes = new byte[0];
        try {
            streamLength = inStream.available();

            if (streamLength > Integer.MAX_VALUE) {
                throw new CommonException("File too big!!");
            }
            // Create the byte array to hold the data
            bytes = new byte[(int) streamLength];
            // Read in the bytes
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length
                    && (numRead = inStream.read(bytes,
                    offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }
            // Ensure all the bytes have been read in
            if (offset < bytes.length) {
                throw new IOException("Could not completely read file ");
            }
            // Close the input stream and return bytes
            inStream.close();
        } catch (IOException e) {
            log.error("An error occurred",e);
            throw new CommonException(e);
        }
        return bytes;
    }

    public static InputStream clone(InputStream is){
        byte[] byteArray = getBytesFromInputStream(is);
        return new ByteArrayInputStream(byteArray);
    }

    /**
     * Get body from request
     * @param request
     * @return
     * @throws IOException
     */
    public static String getBody(HttpServletRequest request) throws IOException {

        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }

        body = stringBuilder.toString();
        return body;
    }

}

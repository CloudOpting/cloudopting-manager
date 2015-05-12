package eu.cloudopting.util;

import eu.cloudopting.exception.CommonException;
import org.apache.tika.Tika;
import org.apache.tika.utils.RereadableInputStream;

import java.io.*;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Utility class for mimetype
 */
public class MimeTypeUtils {
    private MimeTypeUtils() {
        throw new ExceptionInInitializerError("This is a utility class");
    }

    /**
     * Detect mimetype using tika implementation
     *
     * @param inputStream - input stream to detect mimetype from
     * @return - the mimetype
     */
    public static String tikaDetectMymeType(InputStream inputStream) {
        Tika tika = new Tika();
        String mimeType = null;
        try {
//            InputStream rereadableInputStream = new RereadableInputStream(inputStream,inputStream.available(),true,true);
//            InputStream copy = copyInputStream(inputStream);//we create a copy, otherwise the inpustream will be closed
            mimeType = tika.detect(inputStream).intern();
        } catch (Exception e) {
            throw new CommonException(e);
        }
        return mimeType;
    }

    /**
     * We create a copy of the input stream with witch we work.
     */
    private static InputStream copyInputStream(InputStream input) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = input.read(buffer)) > -1) {
            baos.write(buffer, 0, len);
        }
        baos.flush();
        return new ByteArrayInputStream(baos.toByteArray());
    }

    /**
     * MimeType detection using custom implementation
     *
     * @param inputStream - the input stream
     * @return - the mime type
     */
    public static String detectMymeTypeCustom(InputStream inputStream) {
        String mimeType = null;
        Properties magicmimes = new Properties();
        FileInputStream in = null;
        byte[] b = StreamUtil.getBytesFromInputStream(inputStream);
        byte[] topOfStream = new byte[32];
        System.arraycopy(b, 0, topOfStream, 0, topOfStream.length);
        // Read in the magicmimes.properties file (e.g. of file listed below)
        try {
            in = new FileInputStream("magicmimes.properties");
            magicmimes.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // loop over each file signature, if a match is found, return mime type
        for (Enumeration keys = magicmimes.keys(); keys.hasMoreElements(); ) {
            String key = (String) keys.nextElement();
            byte[] sample = new byte[key.length()];
            System.arraycopy(topOfStream, 0, sample, 0, sample.length);
            if (key.equals(new String(sample))) {
                mimeType = magicmimes.getProperty(key);
                System.out.println("Mime Found! " + mimeType);
                break;
            } else {
                System.out.println("trying " + key + " == " + new String(sample));
            }
        }

        return mimeType;
    }
}

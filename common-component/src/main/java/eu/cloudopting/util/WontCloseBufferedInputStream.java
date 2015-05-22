package eu.cloudopting.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Manually closed stream
 */
public class WontCloseBufferedInputStream extends BufferedInputStream {
    public WontCloseBufferedInputStream(InputStream in) {
        super(in);
    }

    public WontCloseBufferedInputStream(InputStream in, int size) {
        super(in, size);
    }

    public void close() {
        // Do nothing.
    }

    public void reallyClose() {
        try {
            super.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

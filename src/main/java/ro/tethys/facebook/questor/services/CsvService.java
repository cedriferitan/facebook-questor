package ro.tethys.facebook.questor.services;

import com.restfb.types.Page;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class CsvService {
    private static final char DEFAULT_SEPARATOR = ',';
    private static final char SPACE_SEPARATOR = ' ';
    private static final String FACEBOOK_URL = "www.facebook.com/";

    private PrintWriter pw;
    private StringBuilder sb = new StringBuilder();

    public CsvService(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            throw new IOException("File could not be created: " + path);
        }

        pw = new PrintWriter(file);
        sb = new StringBuilder();
        writeHeaders();
    }

    public void writeToFile(List<Page> pages) {
        for (Page p : pages) {
            //id
            writeUrl(p.getId());
            //country
            write(p.getLocation().getCountry());
            //city
            write(p.getLocation().getCity());
            //likes
            write(Long.toString(p.getFanCount()));
            //
            write(p.getEmails());
        }
        pw.write(sb.toString());
//        pw.close();
        sb = new StringBuilder();
    }

    private void write(List<String> strings) {
        for (String s : strings) {
            write(s, SPACE_SEPARATOR);
        }
    }

    private StringBuilder write(String s) {
        return write(s, ',');
    }

    private StringBuilder write(String s, char customSeparator) {
        if (s == null) s = "";
        return sb.append(s).append(customSeparator);
    }

    private StringBuilder writeUrl(String s) {
        return sb.append(FACEBOOK_URL).append(s).append(DEFAULT_SEPARATOR);
    }

    private void writeHeaders() {
        write("Page URL");
        write("Country");
        write("City");
        write("Likes");
        write("email", '\n');
    }
}

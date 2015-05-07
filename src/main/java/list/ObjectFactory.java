package list;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class ObjectFactory {

    public static Object build(String type, String uri) throws Exception {
        if ("AL".equals(type)) return buildArrayList(uri);
        else if ("CL".equals(type)) return buildCharSequenceList(uri);
        return null;
    }

    private static List<String> buildArrayList(String uri) throws Exception {
        List<String> k = new ArrayList<String>();
        URL url = new URL(uri);
        URLConnection conn = url.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine = null;

        int count = 0;
        while ((inputLine = br.readLine()) != null) {
            String[] parts = inputLine.split(" ");
            count += parts.length;
            for (String s : parts) {
                k.add(s);
            }
        }
        System.out.println("No of words " + count);
        return k;
    }

    private static CompactStringList buildCharSequenceList(String uri) throws Exception {
        CompactStringList k = new CompactStringList();
        URL url = new URL(uri);
        URLConnection conn = url.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine = null;

        int count = 0;
        while ((inputLine = br.readLine()) != null) {
            String[] parts = inputLine.split(" ");
            count += parts.length;
            for (String s : parts) {
                k.add(s);
            }
        }
        System.out.println("No of words " + count);
        return k;
    }
}

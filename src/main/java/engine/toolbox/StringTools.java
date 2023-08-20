package engine.toolbox;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class StringTools {

    public static List<String> readAllLines(String fileName) {
        List<String> list = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new InputStreamReader(Class.forName(StringTools.class.getName()).getResourceAsStream(fileName)))) {
            String line;
            while((line = br.readLine()) != null) {
                list.add(line);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}

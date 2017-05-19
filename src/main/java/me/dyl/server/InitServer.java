package me.dyl.server;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pro on 17-5-19.
 */
public class InitServer {


    static String basePath = "src/main/java/com/aifeng/init_address/";

    public static List<AdminRanking> getAR(String arName) {
        List<AdminRanking> list = new ArrayList<>();
        File file = new File(basePath + arName);
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.readValue(file, new com.fasterxml.jackson.core.type.TypeReference<List<AdminRanking>>() {

            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}

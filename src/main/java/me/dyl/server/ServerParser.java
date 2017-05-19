package me.dyl.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pro on 17-5-19.
 */
public class ServerParser {
    public static void parse() {
        List<AdminRanking> provinceList = new ArrayList<>();
        List<AdminRanking> cityList = new ArrayList<>();
        List<AdminRanking> areaList = new ArrayList<>();

        File file = new File("/home/pro/IdeaProjects/address_process/src/main/java/me/dyl/source.txt");
        try {
            Reader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            bufferedReader.lines().filter(s -> !s.isEmpty()).forEach(s -> {
                String arr[] = s.trim().split("\\s+");
                AdminRanking adminRanking = new AdminRanking();

                String code = arr[0].trim().replace("　", "");
                String name = arr[1].trim().replace("　", "");
                adminRanking.setCode(code);
                adminRanking.setName(name);

                if (code.endsWith("0000")) {
                    provinceList.add(adminRanking);
                } else if (code.endsWith("00")) {
                    String parent_code = code.substring(0, 2) + "0000";
                    adminRanking.setParent_code(parent_code);
                    cityList.add(adminRanking);
                } else {
                    String parent_code = code.substring(0, 4) + "00";
                    adminRanking.setParent_code(parent_code);
                    areaList.add(adminRanking);
                }
            });
            reader.close();
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            writeFile("server.province.desc", mapper.writeValueAsString(provinceList));
            writeFile("server.city.desc", mapper.writeValueAsString(cityList));
            writeFile("server.area.desc", mapper.writeValueAsString(areaList));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private static void writeFile(String name, String source) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(name);
            fileWriter.write(source);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

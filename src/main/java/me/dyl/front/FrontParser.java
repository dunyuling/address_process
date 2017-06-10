package me.dyl.front;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pro on 17-5-19.
 */
public class FrontParser {


    private static String a_p_code = "";
    private static Map<String, String> aMap = new HashMap<>();

    private static String c_p_code = "";
    private static Map<String, String> cMap = new HashMap<>();

    public static void parse() {
        Map<String, Object> rootMap = new HashMap<>();
        Map<String, String> pMap = new HashMap<>();

        File file = new File("/home/pro/IdeaProjects/address_process/src/main/java/me/dyl/source.txt");
        try {
            Reader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            bufferedReader.lines().filter(s -> !s.isEmpty()).forEach(s -> {
                String arr[] = s.trim().split("\\s+");

                String code = arr[0].trim().replace("　", "");
                String name = arr[1].trim().replace("　", "");
                if (code.endsWith("0000")) {
                    pMap.put(code, name);
                    rootMap.put("86", pMap);
                } else if (code.endsWith("00")) {
                    String parent_code = code.substring(0, 2) + "0000";
                    FrontMock frontMock = loadFrontMap(c_p_code, parent_code, code, name, cMap, rootMap);
                    c_p_code = frontMock.getTempCode();
                    cMap = frontMock.getLevelMap();
                } else {
                    if(!name.equals("市辖区")) {
                        String parent_code = code.substring(0, 4) + "00";
                        FrontMock frontMock = loadFrontMap(a_p_code, parent_code, code, name, aMap, rootMap);
                        a_p_code = frontMock.getTempCode();
                        aMap = frontMock.getLevelMap();
                    }
                }
            });
            rootMap.put(c_p_code, cMap);
            rootMap.put(a_p_code, aMap);
            reader.close();
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            String provinceJson = mapper.writeValueAsString(rootMap);
            try {
                FileWriter fileWriter = new FileWriter("front.desc");
                fileWriter.write(provinceJson);
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private static FrontMock loadFrontMap(String temp_p_code, String parent_code, String code, String name, Map<String, String> levelMap, Map<String, Object> rootMap) {
        if (temp_p_code.isEmpty()) {
            temp_p_code = parent_code;
            levelMap.put(code, name);
        } else if (temp_p_code.equals(parent_code)) {
            levelMap.put(code, name);
        } else {
            rootMap.put(temp_p_code, levelMap);
            temp_p_code = parent_code;
            levelMap = new HashMap<>();
            levelMap.put(code, name);
        }
        return new FrontMock(temp_p_code, levelMap);
    }

    static class FrontMock {
        private String tempCode;
        private Map<String, String> levelMap;

        FrontMock(String tempCode, Map<String, String> levelMap) {
            this.tempCode = tempCode;
            this.levelMap = levelMap;
        }

        String getTempCode() {
            return tempCode;
        }

        Map<String, String> getLevelMap() {
            return levelMap;
        }
    }
}

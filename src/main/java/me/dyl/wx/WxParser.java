package me.dyl.wx;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pro on 17-5-19.
 */
public class WxParser {

    private static Province province = new Province();
    private static List<City> cities = new ArrayList<>();
    private static boolean one = false;

    public static void parse() {
        List<Province> provinces = new ArrayList<>();
        List<Area> areas = new ArrayList<>();
        File file = new File("/home/pro/IdeaProjects/address_process/src/main/java/me/dyl/source.txt");


        try {
            Reader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            bufferedReader.lines().filter(s -> !s.isEmpty()).forEach(s -> {
                String arr[] = s.trim().split("\\s+");

                String code = arr[0].trim().replace("　", "");
                String name = arr[1].trim().replace("　", "");

                if (code.endsWith("0000")) {
                    province = new Province();
                    province.setId(code);
                    province.setName(name);
                    cities = new ArrayList<>();
                    one = true;
                } else if (code.endsWith("00")) {
                    City city = new City();
                    city.setId(code);
                    city.setName(name);
                    cities.add(city);
                } else {
                    if (one) {
                        province.setCity(cities);
                        provinces.add(province);
                        one = false;
                    }
                    String parent_code = code.substring(0, 4) + "00";
                    if(!name.equals("市辖区")) {
                        Area area = new Area();
                        area.setId(code);
                        area.setName(name);
                        area.setPid(parent_code);
                        areas.add(area);
                    }
                }

            });
            reader.close();
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            String provinceJson = mapper.writeValueAsString(provinces);
            String areaJson = mapper.writeValueAsString(areas);
            try {
                FileWriter fileWriter = new FileWriter("wx.province.desc");
                fileWriter.write(provinceJson);
                fileWriter.close();

                fileWriter = new FileWriter("wx.area.desc");
                fileWriter.write(areaJson);
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}

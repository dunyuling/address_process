package me.dyl;

import me.dyl.front.FrontParser;
import me.dyl.server.ServerParser;
import me.dyl.wx.WxParser;

/**
 * Created by pro on 17-5-19.
 */
public class Main {
    public static void main(String[] args) {
        ServerParser.parse();
//        WxParser.parse();
    }
}

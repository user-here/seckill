package org.example.seckill.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.seckill.pojo.TUser;
import org.example.seckill.vo.RespBean;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class UserUtil {

    private static void createUser(int count) throws Exception {
        List<TUser> users = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            TUser user = new TUser();
            user.setId(13000000000L + i);
            user.setNickname("user" + i);
            user.setPassword("123456");
            user.setSalt("1a2b3c4d");
            user.setRegisterDate(new java.util.Date());
            users.add(user);
        }
        Connection connection = getConn();
        String sql = "insert into t_user(id, nickname, password, salt, register_date)values(?,?,?,?,?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        for (TUser user : users) {
            pstmt.setLong(1, user.getId());
            pstmt.setString(2, user.getNickname());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getSalt());
            pstmt.setDate(5, new java.sql.Date(user.getRegisterDate().getTime()));
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        pstmt.close();
        connection.close();
        String urlString = "http://localhost:8080/login/toLogin";
        File file = new File("D:\\config.txt");
        if (file.exists()) {
            file.delete();
        }
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.seek(0);
        for (int i = 0; i < users.size(); i++) {
            TUser user = users.get(i);
            URL url1 = new URL(urlString);
            HttpURLConnection http = (HttpURLConnection) url1.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            OutputStream out = http.getOutputStream();
            String params = "mobile=" + user.getId() + "&password=" + MD5Util.inputPassToFormPass("123456");
            out.write(params.getBytes());
            out.flush();
            InputStream inputStream = http.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) >= 0) {
                baos.write(buffer, 0, len);
            }
            inputStream.close();
            baos.close();
            String response = new String(baos.toByteArray());
            ObjectMapper mapper = new ObjectMapper();
            RespBean respBean = mapper.readValue(response, RespBean.class);
            String userTicket = (String) respBean.getObj();
            System.out.println("create user " + user.getId());
            String row = user.getId() + "," + userTicket;
            raf.seek(raf.length());
            raf.write(row.getBytes());
            raf.write("\r\n".getBytes());
            System.out.println("write to db " + user.getId());

        }
        raf.close();
        System.out.println("over");
    }

    private static Connection getConn() throws Exception {
        String url = "jdbc:mysql://localhost:3306/seckill?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC";
        String username = "root";
        String password = "123456";
        String driver = "com.mysql.cj.jdbc.Driver";
        Class.forName(driver);
        return DriverManager.getConnection(url, username, password);
    }

    public static void main(String[] args) {
        try {
            createUser(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

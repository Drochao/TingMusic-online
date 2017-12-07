package com.dntz.tingmusic.util;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 72408 on 2017/12/6.
 */
public class OkHttp {

    private static OkHttpClient client;

    static {
        client = new OkHttpClient();
    }


    public boolean downMusic(String url, String adr, String fileName)  {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            File file = new File(adr, fileName);
            if(file.exists())
            {
                file.delete();
                file.createNewFile();
            }
            else
                file.createNewFile();
            inputstreamtofile(response.body().byteStream(), file);


            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void inputstreamtofile(InputStream ins, File file) throws IOException {
        OutputStream os = new FileOutputStream(file);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        ins.close();
    }
    public String getAllMusic(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }


//    @Test
//    public void test() throws IOException {
//
//        String urlMusic = "http://192.168.1.107:8080/musicsystem/upload/image/0bfb3c3243174db39254eefded2445f8.mp3";
//        String url = "http://127.0.0.1:8080/getMusic";
//        String urlPic = "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2012178017,770654329&fm=27&gp=0.jpg";
//
//        System.out.println(new OkHttp().downMusic(urlMusic, "C:\\PerfLogs", "jackdog.mp3"));
//
//
//    }


}



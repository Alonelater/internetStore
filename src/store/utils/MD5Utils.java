package store.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {


    public static  String md5(String plainText){

        byte [] secrctBytes = null;

        try {
            /*获取加密的这个类 并且采取什么方式加密*/
            MessageDigest md5 = MessageDigest.getInstance("md5");
            //这是真正加密的方法  参数需要加密的文本的字符数组  返回的也是一个数组
            byte [] pByte = plainText.getBytes();
            secrctBytes = md5.digest(pByte);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        String md5Code = new BigInteger(1,secrctBytes).toString();


        //不足在前面添0
        for (int i =0;i<32-md5Code.length();i++){

            md5Code = "0"+md5Code;
        }
        return md5Code;
    }
}

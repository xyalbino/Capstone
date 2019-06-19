/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.qrcode.autopsy;
/*
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
*/
import com.swetake.util.Qrcode;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import jp.sourceforge.qrcode.QRCodeDecoder;
import jp.sourceforge.qrcode.data.QRCodeImage;
import jp.sourceforge.qrcode.exception.DecodingFailedException;

public class QRCodeScanner {
  
    String path = "";
    public QRCodeScanner(String path){
        this.path = path;
    }
    public boolean encode(String srcValue, String qrcodePicfilePath){
        int MAX_DATA_LENGTH = 200;
        byte[] d = srcValue.getBytes();

        int dataLength = d.length;
        int imageWidth = 113;
        int imageHeight = imageWidth;
        BufferedImage bi = new BufferedImage(imageWidth, imageHeight,BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        g.setBackground(Color.WHITE);
        g.clearRect(0, 0, imageWidth, imageHeight);
        g.setColor(Color.BLACK);
        if (dataLength > 0 && dataLength <= MAX_DATA_LENGTH) {
            Qrcode qrcode = new Qrcode();
            qrcode.setQrcodeErrorCorrect('M'); 
            qrcode.setQrcodeEncodeMode('B'); 
            qrcode.setQrcodeVersion(5);
            boolean[][] b = qrcode.calQrcode(d);
            int qrcodeDataLen = b.length;
            for (int i = 0; i < qrcodeDataLen; i++) {
                for (int j = 0; j < qrcodeDataLen; j++) {
                    if (b[j][i]) {
                        g.fillRect(j * 3 + 2, i * 3 + 2, 3, 3); 
                    }
                }
            }
            //System.out.println("二维码成功生成！！");
        } 
        else {
            //System.out.println( dataLength +"大于"+ MAX_DATA_LENGTH);
            return false;
        }
        g.dispose();
        bi.flush();
        File f = new File(qrcodePicfilePath);
        String suffix = f.getName().substring(f.getName().indexOf(".")+1, f.getName().length());
        //System.out.println("二维码输出成功！！");
        try {
            ImageIO.write(bi, suffix, f);
        } 
        catch (IOException ioe) {
            //System.out.println("二维码生成失败" + ioe.getMessage());
            return false;
        }
        return true;
    }

    public String decode(String qrcodePicfilePath) {
        //System.out.println("开始解析二维码！！");
        /* 读取二维码图像数据 */
        File imageFile = new File(qrcodePicfilePath);
        BufferedImage image;
        String decodedData = "Not a image";
        try {
            image = ImageIO.read(imageFile);
        } 
        catch (IOException e) {
            //System.out.println("读取二维码图片失败： " + e.getMessage());
            return decodedData;
        }
        //System.out.println("读取二维码图片失败： " + e.getMessage());
        /* 解二维码 */
        QRCodeDecoder decoder = new QRCodeDecoder();
        try {
            decodedData = new String(decoder.decode(new J2SEImageGucas(image)));
        }
        catch (DecodingFailedException e) {
            //System.out.println("读取二维码图片失败： " + e.getMessage());
            return decodedData;
        }
        //String decodedData = new String(decoder.decode(new J2SEImageGucas(image)));
        //System.out.println("解析内容如下："+decodedData);
        //System.out.println(new J2SEImageGucas(image));
        return decodedData;
    }
}
class J2SEImageGucas implements QRCodeImage {
    BufferedImage image;

    public J2SEImageGucas(BufferedImage image) {
        this.image = image;
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }

    public int getPixel(int x, int y) {
        return image.getRGB(x, y);
    }
}
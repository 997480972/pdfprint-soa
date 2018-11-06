package com.tomtop.pdf.controller;

import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.pdfbox.tools.PrintPDF;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.pdf.domain.PdfParam;


//打印接口服务
@RestController
@EnableAutoConfiguration
@RequestMapping("/")
public class PdfController {
	
	//从远程（erp服务拿到pdf文件，直接驱动打印机打印）下载pdf到打印接口本地，然后直接打印，打印完成，删除本地文件
	@RequestMapping("printPdfByUrl")
	@ResponseBody
	public boolean printPdfByUrl(@RequestBody PdfParam pdf) {
		String orderNo = null;
		try {
			String printerName = pdf.getPrinterName();//打印机名
			String url = pdf.getUrl();//pdf远程url
			orderNo = pdf.getOrderNo();
			System.out.println("printerName:" + printerName + ", orderNo:" + orderNo);
			if(null == printerName || url == null || orderNo == null) {
				System.out.println("参数不能为空");
				return false;
			}
			//pdf下载到本地临时存放
			String pdfFilePath = "C:/pdf/" + orderNo + ".pdf";
			downloadNet(url,pdfFilePath);// 下载网络文件
			if(!new File(pdfFilePath).exists()){
				System.out.println(pdfFilePath + " not exist!");
				return false;
			}
			PrintPDF.main(new String[]{
			 "-silentPrint",//静默打印
//		       "-password","abcde",//pdf打开密码
		       "-printerName",printerName ,//指定打印机名
		       "-orientation","auto",//打印方向，auto|landscape|portrait三种可选
		       pdfFilePath//打印PDF文档的路径
			});
			deleteFile(pdfFilePath);//删除打印完的文件
			System.out.println("orderNo:" + orderNo + "pdf面单打印成功！");
			return true;
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("orderNo:" + orderNo + "pdf面单打印失败！");
		}
		return false;
	}
	
	
	// 下载网络文件
	private static void downloadNet(String sourceFileUrl,String destFilePath){
	    int bytesum = 0;
	    int byteread = 0;
	    InputStream inStream = null;
	    FileOutputStream fs = null;
	    try {
	    	URL url = new URL(sourceFileUrl);
	        URLConnection conn = url.openConnection();
	        inStream = conn.getInputStream();
	        fs = new FileOutputStream(destFilePath);
	        byte[] buffer = new byte[1024*4];
	        while ((byteread = inStream.read(buffer)) != -1) {
	            bytesum += byteread;
	            System.out.println(bytesum);
	            fs.write(buffer, 0, byteread);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally{
    		try {
    			if(inStream != null){
    				inStream.close();
    			}
    			if(fs != null){
    				fs.close();
    			}
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	}
	
	private static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
        	file.delete();
        }
	}
	public static void main(String[] args) {//test
		String printerName = "PDFCreator";//打印机名
		printerName = "asdas";//打印机名
		String pdfFilePath = "D:/pdf/39488.pdf";//pdf路径
		pdfFilePath = "D:/pdf/label-invoice.pdf";//pdf路径
		try {
			PrintPDF.main(new String[] { 
				"-silentPrint",// 静默打印
				// "-password","abcde",//pdf打开密码
				"-printerName", printerName,// 指定打印机名
				"-orientation", "auto",// 打印方向，auto|landscape|portrait三种可选
				pdfFilePath // 打印PDF文档的路径
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package com.tomtop.pdf.controller;

import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.Sides;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPrintable;
import org.apache.pdfbox.printing.Scaling;
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
			System.out.println(pdf);
			String printerName = pdf.getPrinterName();//打印机名
			String url = pdf.getUrl();//pdf远程url
			orderNo = pdf.getOrderNo();
			System.out.println("printerName:" + printerName + ", orderNo:" + orderNo);
			if(null == printerName || null == url || null == orderNo) {
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
			String dpi = pdf.getDpi() + "";
			PrintPDF.main(new String[]{
				"-silentPrint",//静默打印
//		       "-password","abcde",//pdf打开密码
			   "-dpi",dpi,
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
		
		String printerName = "BTP-3200E(U)";//打印机名
		printerName = "\\\\User-20170521ay\\btp-3200e(u)";//打印机名
		String orderNo = "682988738";
		//pdf下载到本地临时存放
		String pdfFilePath = "C:/pdf/" + orderNo + ".pdf";
		//pdf下载到本地临时存放
		String url = "http://192.168.0.64/label/pdf/ZIN4-228/682988738.pdf";
		downloadNet(url,pdfFilePath);// 下载网络文件
		try {
			PrintPDF.main(new String[]{
					"-silentPrint",//静默打印
//			       "-password","abcde",//pdf打开密码
					"-printerName",printerName ,//指定打印机名
					"-orientation","auto",//打印方向，auto|landscape|portrait三种可选
					pdfFilePath//打印PDF文档的路径
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
//		PdfController pdfController = new PdfController();
//		PdfParam pdf = new PdfParam();
//		pdf.setUrl("http://192.168.0.64/label/pdf/ZMM-198/EE004167332MH.pdf");
//		pdf.setUrl("http://192.168.0.64/label/pdf/YPTS-255/RZ020058764LV.pdf");
//		pdf.setOrderNo(orderNo);
//		pdf.setPrinterName(printerName);
//		pdfController.printPdfByUrl(pdf);
		/*//pdf下载到本地临时存放
		String pdfFilePath = "C:/pdf/" + orderNo + ".pdf";
		//1.得到一个文件的输入流
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(pdfFilePath);
			// 获取本地创建的空白PDF文件
			PDDocument document = PDDocument.load(fileInputStream);
			// 加载成打印文件
			PDFPrintable printable = new PDFPrintable(document,Scaling.ACTUAL_SIZE);
			PrinterJob job = PrinterJob.getPrinterJob();
			job.setPrintable(printable);
			job.print();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(fileInputStream != null){
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}*/
		//这是要打印文件的格式，如果是PDF文档要设为自动识别
		DocFlavor fileFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
		//2.得到要打印的文档类DOC
//		Doc myDoc = new SimpleDoc(fileInputStream, fileFormat, null);  
		//3.生成一个打印属性设置对象
		PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
		aset.add(new Copies(1));//Copies-打印份数5份
//		aset.add(MediaSize);//A4纸张
		aset.add(Sides.ONE_SIDED);//单面打印
		//4.关键一步，得到当前机器上所有已经安装的打印机
		//传入的参数是文档格式跟打印属性，只有支持这个格式与属性的打印机才会被找到
		PrintService[] services = PrintServiceLookup.lookupPrintServices(fileFormat, aset);
		System.out.println("所有已经安装的打印机:" + services.length);
		/*
		if (services.length > 0) {
			PrintService printService = null;
			for (int i = 0; i < services.length; i++) {
				printService = services[i];
				System.out.println(printService.getName());
				if(printerName.equals(printService.getName())){
					 //5.用打印服务生成一个文档打印任务，这里用的是第一个服务
				    //也可以进行筛选，services[i].getName()可以得到打印机名称，可用名称进行比较得到自己想要的打印机
				   DocPrintJob job = printService.createPrintJob();
				   try {
				       //6.最后一步，执行打印文档任务，传入的参数是Doc文档类，与属性(5份，双面,A4)
				        job.print(myDoc, aset);//成功后电脑会提示已有文档添加到打印队列
				   } catch (PrintException pe) {
					   pe.printStackTrace();
				   }
				   break;
				}
			}
		   
		}*/
	}
}

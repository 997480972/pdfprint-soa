package com.tomtop.pdf.domain;

public class PdfParam {
	private String url;
	private String orderNo;
	private String printerName;
	private int dpi; //打印机分辨率。DPI是指每英寸可打印的点数
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getPrinterName() {
		return printerName;
	}
	public void setPrinterName(String printerName) {
		this.printerName = printerName;
	}
	public int getDpi() {
		return dpi;
	}
	public void setDpi(int dpi) {
		this.dpi = dpi;
	}
	@Override
	public String toString() {
		return "PdfParam [url=" + url + ", orderNo=" + orderNo
				+ ", printerName=" + printerName + ", dpi=" + dpi + "]";
	}
	
}

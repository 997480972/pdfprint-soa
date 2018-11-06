package com.tomtop.pdf.domain;

public class PdfParam {
	private String url;
	private String orderNo;
	private String printerName;
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
	@Override
	public String toString() {
		return "PdfParam [url=" + url + ", orderNo=" + orderNo + ", printerName=" + printerName + "]";
	}
}

package application;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.printing.PDFPageable;

public class Printer{
	
	public final String default_printer  = "Zebra ZP 450 CTP";
	public final String default_upc 	 = "doc/labels/1000-1009_1_L.pdf";
	
	public Printer() {
		
	}
	
	public static ArrayList<String> getPrinterList(){
		ArrayList<String> printerList = new ArrayList<String>();
		
		PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService printService : printServices) {
            printerList.add(printService.getName().trim());
        }
		
		return printerList;
	}
	
	private static PrintService findPrintService(String printerName) {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService printService : printServices) {
            if (printService.getName().trim().equals(printerName)) {
                return printService;
            }
        }
        return null;
    }
	
	public void PrintReportToPrinter() throws PrinterException, InvalidPasswordException, IOException{
		PDDocument document = PDDocument.load(new File(this.default_upc));
		PrintService myPrintService = findPrintService(this.default_printer);
		
		PrinterJob job = PrinterJob.getPrinterJob();
		job.setPageable(new PDFPageable(document));
		job.setPrintService(myPrintService);
		job.print();
	}
	
	public void PrintReportToPrinter(String documentName) throws PrinterException, InvalidPasswordException, IOException{
		
		PDDocument document = PDDocument.load(new File(documentName));
		PrintService myPrintService = findPrintService(this.default_printer);
		
		PrinterJob job = PrinterJob.getPrinterJob();
		job.setPageable(new PDFPageable(document));
		job.setPrintService(myPrintService);
		job.print();
	   
	}
	
	public void PrintReportToPrinter(String printerName, String documentName) throws PrinterException, InvalidPasswordException, IOException{
		
		PDDocument document = PDDocument.load(new File(documentName));
		PrintService myPrintService = findPrintService(printerName);
		
		PrinterJob job = PrinterJob.getPrinterJob();
		job.setPageable(new PDFPageable(document));
		job.setPrintService(myPrintService);
		job.print();
	}
}
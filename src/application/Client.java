package application;

import java.util.Scanner;
import java.util.Set;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.AbstractXlsDataSource;
import net.sf.jasperreports.engine.data.ExcelDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JRJdtCompiler;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
		

public class Client {
	
	public Client(){
		
System.out.println("Start ....");
		
	}
	
	public HashMap<String, Style> start() throws JRException, IOException, EncryptedDocumentException, InvalidFormatException, PrinterException{
		
		System.out.println("Application starting...");
		
		String jrxmlFileName 	= "doc/label_template/upc.jrxml";
		String jasperFileName 	= "doc/label_template/upc.jasper";
		String pdfFileName 		= "doc/labels/";
		
		// DataSource
	    String excel = "doc/worksheets/UPCLIST.xlsx";
	    Workbook workbook = WorkbookFactory.create(new File(excel));
	    Sheet sheet = (Sheet) workbook.getSheetAt(0);
	    
        // Parameters for report
	    ArrayList<String> displayNames = new ArrayList<String>();
	    ArrayList<HashMap<String, String>> values = new ArrayList<HashMap<String,String>>();
	    
	    // Create a DataFormatter to format and get each cell's value as String
        DataFormatter dataFormatter = new DataFormatter();
	    
	    // Retrieving the number of sheets in the Workbook
        System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");
        System.out.println("Iterating through the excel sheet");
        
	    // Iterate the First Row to Get the Column Names
        Iterator<Row> rowIterator = sheet.rowIterator();
	    Row row = rowIterator.next();
	    Iterator<Cell> cellIterator = row.cellIterator();
	    while (cellIterator.hasNext()) {
	    	Cell cell = cellIterator.next();
    		String cellValue = dataFormatter.formatCellValue(cell);
    		displayNames.add(cellValue);
	    }
	    
        // Iterate through the Rest of the Rows to Fill in the Values
        while (rowIterator.hasNext()) {
        	// Get the Next Row
        	row = rowIterator.next();
        	cellIterator = row.cellIterator();
        	// Iterate through columns columns of the current row
        	int columnIndex = 0;
        	HashMap<String, String> rowValues = new HashMap<String,String>();
        	while (cellIterator.hasNext()) {
        		Cell cell = cellIterator.next();
        		String cellValue = dataFormatter.formatCellValue(cell);
            	String columnName = displayNames.get(columnIndex);
            	rowValues.put(columnName, cellValue);
        		columnIndex += 1;
        	}
        	values.add(rowValues);
        }
        
        HashMap<String, Style> style_map = new HashMap<String, Style>();
        
        for (HashMap<String,String> item : values) {
        	String style = item.get("style").replaceAll("\n", "");
        	String color = item.get("color").replaceAll("\n", "");
        	String size = item.get("size").replaceAll("\n", "");
        	String description = item.get("description").replaceAll("\n", "");
        	String upc = item.get("upc").replaceAll("\n", "");
        	String outputFileName = pdfFileName + style + "_"  + color + "_" + size + ".pdf";
        	
        	if (style_map.containsKey(style)) {
        		style_map.get(style).addColor(color);
        	}
        	else {
        		style_map.put(style, new Style(style));
        		style_map.get(style).addColor(color);
        	}
        	
        	try {
	        	if ( !(new File(outputFileName).isFile())) {
	        	
		        	Map<String, Object> parameters = new HashMap<String, Object>();
		        	parameters.put("style", style);
		        	parameters.put("color", color);
		        	parameters.put("size", size);
		        	parameters.put("description", description);
		        	parameters.put("upc", upc);
		        	JasperPrint jasperPrint = JasperFillManager.fillReport(jasperFileName, parameters, new JREmptyDataSource());
		        	JasperExportManager.exportReportToPdfFile(jasperPrint, outputFileName);
	        	}
        	}
        	catch(Exception e) {
        		
        	}
        }
        
        return style_map;
	}
}

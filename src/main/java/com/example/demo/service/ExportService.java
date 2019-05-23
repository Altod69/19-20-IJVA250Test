package com.example.demo.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Client;
import com.example.demo.entity.Facture;
import com.example.demo.entity.LigneFacture;

@Service
@Transactional
public class ExportService {
	
	@Autowired
	private ClientService clientService;
	
	@Autowired
	private FactureService factureService;
	
	
	public void clientsCSV(Writer writer) throws IOException {
        PrintWriter printWriter = new PrintWriter(writer);
		
		List<Client> allClients = clientService.findAllClients();
        LocalDate now = LocalDate.now();
        printWriter.println("Id" + ";" + "Nom" + ";" + "Prenom" + ";" + "Date de Naissance");

        for (Client client : allClients) {
            printWriter.println(client.getId() + ";"
                    + client.getNom() + ";"
                    + client.getPrenom() + ";"
                    + client.getDateNaissance().format(DateTimeFormatter.ofPattern("dd/MM/YYYY")));
        }
    }
	
	public void clientsWithFactureXLSX(ServletOutputStream outPutStream) throws IOException {
		List<Client> allClients = clientService.findAllClients();

		Workbook workbook = new XSSFWorkbook();
		CellStyle style = workbook.createCellStyle();
		Font whiteFont = workbook.createFont();
		
		
		whiteFont.setColor(IndexedColors.WHITE.getIndex());
		whiteFont.setBold(true);
		
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());
		style.setFont(whiteFont);
        
        for (Client client : allClients) {
        	Sheet sheet = workbook.createSheet("Client" + client.getId());
        	Row headerRow = sheet.createRow(0);
            
            Cell cellId = headerRow.createCell(0);
            cellId.setCellStyle(style);
            cellId.setCellValue("Id");

            Cell cellPrenom = headerRow.createCell(1);
            cellPrenom.setCellStyle(style);
            cellPrenom.setCellValue("Prénom");

            Cell cellNom = headerRow.createCell(2);
            cellNom.setCellStyle(style);
            cellNom.setCellValue("Nom");

        	Row row = sheet.createRow(1);

            Cell id = row.createCell(0);
            id.setCellValue(client.getId());

            Cell prenom = row.createCell(1);
            prenom.setCellValue(client.getPrenom().replaceAll("[^\\d\\p{L}!#$€%&'`(),;:/@...]", ""));

            Cell nom = row.createCell(2);
            nom.setCellValue(client.getNom().replaceAll("[^\\d\\p{L}!#$€%&'`(),;:/@...]", ""));
            
            List<Facture> factures = factureService.findFacturesClient(client.getId());
            
            if(factures.size() > 0) {
            	for(Facture facture : factures) {
                	Sheet sheetFacture = workbook.createSheet("Facture" + facture.getId());
                	Row headerFactureRow = sheetFacture.createRow(0);
                	
                	Cell cellheaderArticleId = headerFactureRow.createCell(0);
                	cellheaderArticleId.setCellStyle(style);
                	cellheaderArticleId.setCellValue("Id");

                    Cell cellheaderArticleLibelle = headerFactureRow.createCell(1);
                    cellheaderArticleLibelle.setCellStyle(style);
                    cellheaderArticleLibelle.setCellValue("Libelle");

                    Cell cellheaderArticlePrix = headerFactureRow.createCell(2);
                    cellheaderArticlePrix.setCellStyle(style);
                    cellheaderArticlePrix.setCellValue("Prix");
                    
                    Cell cellheaderArticleQuantite = headerFactureRow.createCell(3);
                    cellheaderArticleQuantite.setCellStyle(style);
                    cellheaderArticleQuantite.setCellValue("Quantite");
                    
                    Cell cellheaderArticleSousTotal = headerFactureRow.createCell(4);
                    cellheaderArticleSousTotal.setCellStyle(style);
                    cellheaderArticleSousTotal.setCellValue("Sous Total");
                    
                    Set<LigneFacture> lignesFacture = facture.getLigneFactures();
                    
                    int rowIndex = 1;
                    for(LigneFacture article : lignesFacture) {
                    	Row ligneFactureRow = sheetFacture.createRow(rowIndex);
                    	
                    	Cell cellArticleId = ligneFactureRow.createCell(0);
                    	cellArticleId.setCellValue(article.getArticle().getId());
                        
                        Cell cellArticleLibelle = ligneFactureRow.createCell(1);
                        cellArticleLibelle.setCellValue(article.getArticle().getLibelle());
                        
                        Cell cellArticlePrix = ligneFactureRow.createCell(2);
                        cellArticlePrix.setCellValue(article.getArticle().getPrix());

                        Cell cellArticleQuantite = ligneFactureRow.createCell(3);
                        cellArticleQuantite.setCellValue(article.getQuantite());
                        
                        Cell cellArticleSousTotal = ligneFactureRow.createCell(4);
                        cellArticleSousTotal.setCellValue(article.getSousTotal());
                        
                        rowIndex++;
                    }
                    
                    Row totalFactureRow = sheetFacture.createRow(rowIndex);
                    
                    Cell cellTotalFactureLibelle = totalFactureRow.createCell(0);
                    cellTotalFactureLibelle.setCellStyle(style);
                    cellTotalFactureLibelle.setCellValue("TOTAL");
                    
                    Cell cellTotalFactureMontant = totalFactureRow.createCell(4);
                    cellTotalFactureMontant.setCellStyle(style);
                    cellTotalFactureMontant.setCellValue(facture.getTotal());
                    
                    sheetFacture.addMergedRegion(new CellRangeAddress(rowIndex,rowIndex,0,3));
                }
	
            }
        }
        
        workbook.write(outPutStream);
        workbook.close();
        
	}
}

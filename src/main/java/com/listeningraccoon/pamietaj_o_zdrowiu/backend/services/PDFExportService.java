package com.listeningraccoon.pamietaj_o_zdrowiu.backend.services;

import com.listeningraccoon.pamietaj_o_zdrowiu.backend.data.Prescription;
import com.listeningraccoon.pamietaj_o_zdrowiu.backend.data.User;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class PDFExportService {
    public static void export(HttpServletResponse response, User user, Prescription prescription) throws IOException {
        Document document = new Document(PageSize.A5);
        PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        //Date in upper right corner
        Font dateFont = FontFactory.getFont(FontFactory.COURIER, 9, Font.NORMAL);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime now = LocalDateTime.now();
        Paragraph dateParagraph = new Paragraph(now.format(formatter), dateFont);
        dateParagraph.setAlignment(Element.ALIGN_RIGHT);
        document.add(dateParagraph);

        //Title in the center
        Font titleFont = FontFactory.getFont(FontFactory.COURIER, 18, Font.BOLD);
        Paragraph titleParagraph = new Paragraph("Prescription", titleFont);
        titleParagraph.setAlignment(Element.ALIGN_CENTER);
        document.add(titleParagraph);

        //Patient information
        Font headerFont = FontFactory.getFont(FontFactory.COURIER, 13, Font.BOLD);
        Paragraph patientHeaderParagraph = new Paragraph("Patient's information", headerFont);
        patientHeaderParagraph.setAlignment(Element.ALIGN_LEFT);
        document.add(patientHeaderParagraph);

        Font informationFont = FontFactory.getFont(FontFactory.COURIER, 10, Font.NORMAL);
        String stringBuilder = String.format("First Name: %s\n", user.getFirstName()) +
                String.format("Last Name: %s\n", user.getLastName()) +
                String.format("Contact: %s\n", user.getEmail());

        Paragraph informationParagraph = new Paragraph(stringBuilder, informationFont);
        informationParagraph.setAlignment(Element.ALIGN_LEFT);
        document.add(informationParagraph);

        //Prescription details
        Paragraph detailsHeaderParagraph = new Paragraph("Prescription's details", headerFont);
        detailsHeaderParagraph.setAlignment(Element.ALIGN_LEFT);
        document.add(detailsHeaderParagraph);

        Font detailsFont = FontFactory.getFont(FontFactory.COURIER, 10, Font.NORMAL);
        stringBuilder = String.format("Name: %s\n", prescription.getName()) +
                String.format("Group: %s\n", prescription.getGroup()) +
                String.format("Start date: %s End date: %s  Time: %s\n", prescription.getStartDate(), prescription.getEndDate(), prescription.getTime());

        Paragraph detailsParagraph = new Paragraph(stringBuilder, informationFont);
        detailsParagraph.setAlignment(Element.ALIGN_LEFT);
        document.add(detailsParagraph);


        document.close();
    }
}

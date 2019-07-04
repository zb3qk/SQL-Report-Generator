package ReportApplication.Deprecated;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CreateReport {



    public boolean generateReport(){

        GenerateReport curRep = new GenerateReport();

        try {
            Path path = Paths.get(ClassLoader.getSystemResource("rose-blue-flower-rose-blooms-67636.jpeg/").toURI());


            Document document = new Document();
            try {
                PdfWriter.getInstance(document, new FileOutputStream("iTextHelloWorld.pdf"));
            } catch (Exception e) {
                System.out.println("ERROR: File not Found");
                e.printStackTrace();
            }

            document.open();
            Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
            Chunk chunk = new Chunk("Hello World", font);

            PdfPTable table = new PdfPTable(3);
            curRep.addTableHeader(table);
            curRep.addRows(table);

            try {
                curRep.addCustomRows(table);
            } catch (Exception e){
                System.out.println("ERROR: Bad Element in Custom Row");
                e.printStackTrace();
            }

            try {
                Image img = Image.getInstance(path.toAbsolutePath().toString());
                try {
                    document.add(chunk);
                    document.add(img);
                    document.add(table);
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            } catch (Exception e){
                e.printStackTrace();
            }

            document.close();
        } catch (URISyntaxException e){
            System.out.println("ERROR: Image could not be found");
            e.printStackTrace();
        }

        return true;
    }

}

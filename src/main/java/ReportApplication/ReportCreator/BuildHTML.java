package ReportApplication.ReportCreator;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import j2html.tags.DomContent;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

import ReportApplication.Queries.ExecuteLogin;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static j2html.TagCreator.*;

public class BuildHTML {
    String title;
    ArrayList<ArrayList> rows;
    ArrayList<String> cols;
    ArrayList<String> style;
    ExecuteLogin login;

    private static final String OUTPUT_FILE = "test.pdf";
    private static final String UTF_8 = "UTF-8";

    public BuildHTML(){
        title = "report";
    }

    public BuildHTML(String title, ArrayList<ArrayList> rows, ArrayList<String> cols, ExecuteLogin login){
        this.title = title;
        this.rows = rows;
        this.cols = cols;
    }

    private String URLReader(String url){
        String totalLines = "";
        try {
            URL oracle = new URL(url);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(oracle.openStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null)
                totalLines += inputLine;
            in.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return totalLines;
    }

    private DomContent getBootstrap(){
        String cdn = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css";
//        return style(URLReader(cdn));

        return link().withHref(cdn).withRel("stylesheet");
    }


    /*  #########################
        Meta Information for HTML
        ######################3## */

    private DomContent getStyle(String style){
        String content = "";
        try {
            Path path = Paths.get(ClassLoader.getSystemResource(style+".css").toURI());
            content = new String(Files.readAllBytes(path));
        } catch (Exception e){
            System.out.println("Could not retrieve style");
            e.printStackTrace();
        }
        DomContent styleEl = style( // style element
                content
        );
        return styleEl;
    }

    private DomContent header(){
        return head(
                title(title),
                getStyle("default"),
                getBootstrap()
        );
    }


    /* #############################
        Content Information for HTML
       ############################# */

    private DomContent dataHeader(){
        return div(
            p("I'm a header")
        );
    }

    private DomContent dataFooter(){
        return div();
    }

    private DomContent dataTable(ArrayList<ArrayList> rows, ArrayList<String> colNames){
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 9, 10);
        List<String> strings = Arrays.asList("Halli","Marti");

        DomContent report =  table(
                tr(
                        each(colNames,col ->th(col)) //Header Row
                ),
                each(rows, (row) -> tr( //Each row
                        each(row, (el)->td(el.toString())) //each element
                ))
        );
        return report;
    }

    // Fixes img tags to be compatible with iText PDF conversion
    private String fixElement(String af, String sub){
        int index = af.indexOf(sub);
        if(index == -1){
            return af;
        }

        index = index + af.substring(index).indexOf(">");

        String bef = af.substring(0,index);
        String newAf = fixElement(" /"+af.substring(index), sub); // add the '/' to '>' to make ' />'
        return bef + newAf;
    }

    /* #############################
            Build the HTML File
       ############################# */

    public String start(){
        ContentTemplate temp = new ContentTemplate(login);
        try {
            FileWriter file = new FileWriter("rendered.html");
            String report = html(
                    header(),
                    body(
                            temp.logoHeader(""),
                            dataHeader(),
                            dataTable(rows, cols),
                            dataFooter()
                    )
            ).renderFormatted();
            report = fixElement(report, "<img");
            report = fixElement(report, "<link");

            String renderedHtmlContent = report;
            String xHtml = convertToXhtml(renderedHtmlContent);

            toPdf(xHtml);

            file.write(report);
            file.flush();
            file.close();
        } catch (Exception e){
            System.out.println("Error in File Creation");
            e.printStackTrace();
        }
        return null;

    }

    public void toPdf(String xHtml){
        ITextRenderer renderer = new ITextRenderer();
//        renderer.getFontResolver().addFont("Code39.ttf", IDENTITY_H, EMBEDDED);

        renderer.setDocumentFromString(xHtml, "");
        renderer.layout();
        try { //////
            OutputStream outputStream = new FileOutputStream(OUTPUT_FILE);
            renderer.createPDF(outputStream);
            outputStream.close();
        } catch (Exception e){
            e.printStackTrace();
        }

        String filename = title;
        filename = "rendered.html";
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document,
                    new FileOutputStream("html.pdf"));
            document.open();
            XMLWorkerHelper.getInstance().parseXHtml(writer, document,
                    new FileInputStream(filename));
        } catch (Exception e){
            e.printStackTrace();
        }
        document.close();
    }


    private String convertToXhtml(String html) throws UnsupportedEncodingException {
        Tidy tidy = new Tidy();
        tidy.setInputEncoding(UTF_8);
        tidy.setOutputEncoding(UTF_8);
        tidy.setXHTML(true);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        tidy.parseDOM(inputStream, outputStream);
        return outputStream.toString(UTF_8);
    }
}

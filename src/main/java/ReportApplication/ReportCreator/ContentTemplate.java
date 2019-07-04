package ReportApplication.ReportCreator;

import ReportApplication.Queries.ExecuteLogin;
import j2html.tags.DomContent;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static j2html.TagCreator.*;

public class ContentTemplate {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String name;

    ContentTemplate(ExecuteLogin log){
//        name = log.getUser();
    }

    public DomContent logoHeader(String logoPath){
        name = "Jetty";
        try {
            logoPath = Paths.get(ClassLoader.getSystemResource("rose-blue-flower-rose-blooms-67636.jpeg/").toURI()).toString();
        } catch (Exception e){
            e.printStackTrace();
        }
//
//        return nav(attrs(".navbar"),
//                img(attrs(".logo")).withSrc(logoPath),
//                p(attrs(".send-right"), "Hallo")
//        );
        return div(
                div(attrs(".row"),
            img(attrs(".col-sm-3 .align-self-start")).withSrc(logoPath),
            p(attrs(".col-sm-3 .align-self-end"),dtf.format(LocalDateTime.now()))
            ),
            div(attrs(".row"),
                div(attrs(".col align-self-end"), p(dtf.format(LocalDateTime.now())))

            )
        );

    }

}

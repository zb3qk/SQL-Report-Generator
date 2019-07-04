package ReportApplication.ReportCreator;

import ReportApplication.DataTypes.Tuple;
import j2html.tags.DomContent;

import java.io.FileWriter;
import java.util.ArrayList;

import static j2html.TagCreator.*;

// Must have Access to the internet to use the Flowchart Builder
public class FlowchartBuilder {
    String flowchartCDN = "https://unpkg.com/mermaid/dist/mermaid.min.js";
    DomContent flowchart;

    public FlowchartBuilder(ArrayList<String> content, ArrayList<ArrayList<Tuple>> connections){
        flowchart = html(
                head(
                        script().withSrc(flowchartCDN)
                ),
                body(
                        div(attrs(".mermaid"),
                            "graph LR \n" + generateFlowchart(content, connections)
                        )
                )
        );
    }

    private String generateFlowchart(ArrayList<String> content, ArrayList<ArrayList<Tuple>> connections){
        String init = "";
        for (int i = 0; i<content.size(); i++){
            ArrayList<Tuple> curConn = connections.get(i);
            if(curConn.size() == 0){
                init += "\t" + content.get(i) + "["+content.get(i)+" a]"+ "\n";
            }
            else {
                for (int j = 0; j < curConn.size(); j++) {
                    Tuple cur = curConn.get(j);
                    init += "\t " + content.get(i)+"["+content.get(i)+" a]" + " -- " + cur.getX() +  " --> " + cur.getY() + "\n";
                }
            }
        }
        return init;
    }

    public String getHTML(){
        return flowchart.render();
    }

    public void file (String title){
        try {
            FileWriter file = new FileWriter(title + ".html");
            file.write(this.getHTML());
            file.flush();
            file.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

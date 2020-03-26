package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class MinimumHamiltonCircuit {

    public ImageView inputImage = new ImageView();
    public ImageView outputImage = new ImageView();
    public Button showGraphBtn = new Button();
    public ProgressBar progressPar = new ProgressBar();
    public TableColumn<EdgeForTabel,String> inputEdgeName = new TableColumn<>();
    public TableColumn<EdgeForTabel,String> inputEdgeWeight = new TableColumn<>();
    public TableColumn<EdgeForTabel,String> inputEdgeStartVertex = new TableColumn<>();
    public TableColumn<EdgeForTabel,String> inputEdgeEndVertex = new TableColumn<>();
    public TableColumn<EdgeForTabel,String> outputEdgeName = new TableColumn<>();
    public TableColumn<EdgeForTabel,String> outputEdgeWeight = new TableColumn<>();
    public TableColumn<EdgeForTabel,String> outputEdgeStartVertex = new TableColumn<>();
    public TableColumn<EdgeForTabel,String> outputEdgeEndVertex = new TableColumn<>();
    public TableView<EdgeForTabel> inputTbl = new TableView<>();
    public TableView<EdgeForTabel> outputTbl = new TableView<>();
    private int numberOfEdges;

    public void showGraph(ActionEvent actionEvent) {
        try {
            inputImage.setImage(new Image(new File("graph.png").toURL().toString()));
            outputImage.setImage(new Image(new File("MST.png").toURL().toString()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        inputTbl.setOpacity(1);
        outputTbl.setOpacity(1);
    }
    public void getGraph(Graph graph , int numberOfEdges , String graphType) throws IOException {
        this.numberOfEdges = numberOfEdges;
        ArrayList<Edge>edges = graph.runSalesManProblem();
        for(int i=0;i<edges.size();i++)
        {
            System.out.print(edges.get(i).getEdgeName()+" ");
        }
        System.out.println();
        minimumHamiltonCircuit(edges,graphType);
        fillTables(edges,graph.getGraphEdges());
        executeDrawingPrograms();
        setProgressPar();
        showGraphBtn.setDisable(false);
    }
    public void setProgressPar() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressPar.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(6), e -> {
                }, new KeyValue(progressPar.progressProperty(), 1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        timeline.stop();


                    }
                },
                6000
        );
    }
    public void fillTables(ArrayList<Edge> outputEdges, ArrayList<Edge> inputEdges) {
        ArrayList<EdgeForTabel> edgeForTable = new ArrayList<>();
        ArrayList<EdgeForTabel> edgeForTable2 = new ArrayList<>();
        for (int i = 0; i < numberOfEdges; i++) {
            edgeForTable.add(new EdgeForTabel(inputEdges.get(i).getEdgeName(), String.valueOf(inputEdges.get(i).getWeight()), inputEdges.get(i).getStart().getVertexName(), inputEdges.get(i).getTermination().getVertexName()));
        }
        ObservableList<EdgeForTabel> observableList = FXCollections.observableArrayList(edgeForTable);
        for (Edge outputEdge : outputEdges) {
            edgeForTable2.add(new EdgeForTabel(outputEdge.getEdgeName(), String.valueOf(outputEdge.getWeight()), outputEdge.getStart().getVertexName(), outputEdge.getTermination().getVertexName()));
        }
        ObservableList<EdgeForTabel> observableList2 = FXCollections.observableArrayList(edgeForTable2);

        inputTbl.setItems(observableList);
        inputEdgeName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        inputEdgeWeight.setCellValueFactory(new PropertyValueFactory<>("Weight"));
        inputEdgeStartVertex.setCellValueFactory(new PropertyValueFactory<>("From"));
        inputEdgeEndVertex.setCellValueFactory(new PropertyValueFactory<>("To"));

        outputTbl.setItems(observableList2);
        outputEdgeName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        outputEdgeWeight.setCellValueFactory(new PropertyValueFactory<>("Weight"));
        outputEdgeStartVertex.setCellValueFactory(new PropertyValueFactory<>("From"));
        outputEdgeEndVertex.setCellValueFactory(new PropertyValueFactory<>("To"));


    }
    public void executeDrawingPrograms()
    {
        try {
            Runtime.getRuntime().exec("main.exe");
            Runtime.getRuntime().exec("minimumSpanningTree.exe");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void minimumHamiltonCircuit(ArrayList<Edge> edges, String graphType) throws IOException {
        FileWriter writer = new FileWriter("MST.txt");
        writer.write(edges.size() + "\n");
        writer.write(graphType + "\n");
        for (Edge edge : edges) {
            writer.write(edge.getEdgeName() + "," + edge.getWeight() + "," + edge.getStart().getVertexName() + "," + edge.getTermination().getVertexName() + "\n");
        }
        writer.close();
    }
}
package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class Tetris extends Application {

//Variables
    public static final int MOVE = 25;                                      //move '1 block' 25 pixels each time
    public static final int SIZE = 25;                                      //size of each block = 25 pixels CHECK: difference between final int and int
    public static int XMAX = SIZE * 12;                                     //screen width = each block * 12
    public static int YMAX = SIZE * 24;                                     //screen height = each block * 24
    public static int [][] MESH = new int [XMAX/SIZE][YMAX/SIZE];           //create a grid called MESH with dimensions 12 x 24
    private static Pane group = new Pane();                                //JavaFX: Pane contains the nodes and manages the UI settings of the nodes within it
    private static Form object;                                             //Form is another class
    private static Scene scene = new Scene(group, XMAX + 110, YMAX, Color.GREY);    //JavaFX: container for the graph content--> since the root is the Pane, contents will resize and adjust with window size
    public static int score = 0;
    public static int top = 0;
    private static boolean game = true;                                     //marker for active game status
    private static Form nextObj = Controller.makeRect();                    //objects that fall?
    private static int linesNo = 0;                                         //track number of lines cleared?


//Create scene and start game
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
    for (int[] a: MESH) {
        Arrays.fill(a,0);
    }

//Create score and 'level'
    Line rLine = new Line(XMAX, 0, XMAX, YMAX);
    Line lLine = new Line(0, 0, 0, YMAX);
    Line bLine = new Line(0, YMAX, XMAX, YMAX);
    Line tLine = new Line(0, 0, XMAX, 0);

    rLine.setStrokeWidth(5);
    rLine.setStroke(Color.DIMGRAY);
    lLine.setStrokeWidth(5);
    lLine.setStroke(Color.DIMGRAY);
    bLine.setStrokeWidth(5);
    bLine.setStroke(Color.DIMGRAY);
    tLine.setStrokeWidth(5);
    tLine.setStroke(Color.DIMGRAY);

    Text scoretext = new Text ("Score: ");
    scoretext.setStyle("-fx-font: 20 arial;");
    scoretext.setY(50);
    scoretext.setX(XMAX + 10);
    Text level = new Text("Lines cleared: ");
    level.setStyle("-fx-font: 20 arial;");
    level.setY(100);
    level.setX(XMAX + 10);
    group.getChildren().addAll(scoretext, rLine, lLine, bLine, tLine, level); //how components are rendered

    //Create first block and stage
    Form a = nextObj;
    group.getChildren().addAll(a.a, a.b, a.c, a.d);
    moveOnKeyPress(a);
    object = a;
    nextObj = Controller.makeRect();
    stage.setScene(scene);
    stage.setTitle("BLOCK SPLICE: TETRIS 2.0");
    stage.show();

    //Timer
    Timer fall = new Timer();
    TimerTask task = new TimerTask() {
        public void run() {
            Platform.runLater(new Runnable() {
                public void run() {
                    if(object.a.getY() == 0 ||
                            object.b.getY() == 0 ||
                            object.c.getY() == 0 ||
                            object.d.getY() == 0)
                        top++;
                    else
                        top = 0;
                    if (top == 2) {
                        //GAME OVER
                        Text over = new Text("GAME OVER");
                        over.setFill(Color.RED);
                        over.setStyle("-fx-font: 60 arial;");
                        over.setY(250);
                        over.setX(15);
                        group.getChildren().add(over);
                        game = false;
                    }

                    //Exit
                    if (top == 15) {
                        System.exit(0);
                    }

                    if (game) {
                        MoveDown(object);
                        scoretext.setText("Score: " + Integer.toString(score));
                        level.setText("Lines: " + Integer.toString(linesNo));
                    }
                }
            });
        }
    };
    fall.schedule(task, 0, 300);

    }

    //Create Controls
    private void moveOnKeyPress (Form form) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {
                    case RIGHT:
                        Controller.MoveRight(form);
                        break;
                    case DOWN:
                        MoveDown(form);
                        break;
                    case LEFT:
                        Controller.MoveLeft(form);
                        break;
                    case UP:
                        MoveTurn(form);
                        break;
                }
            }
        });
    }

    private void MoveTurn(Form form) {
        int f = form.form;
        Rectangle a = form.a;
        Rectangle b = form.b;
        Rectangle c = form.c;
        Rectangle d = form.d;
        switch (form.getName()) {
            case "j":
                if (f == 1 && cB(a, 1, -1) && cB(c, -1, -1) && cB(d, -2, -2)) {
                    MoveRight(form.a);
                    MoveDown(form.a);
                    MoveDown(form.c);
                    MoveLeft(form.c);
                    MoveDown(form.d);
                    MoveDown(form.d);
                    MoveLeft(form.d);
                    MoveLeft(form.d);
                    form.changeForm();
                    break;
                }
                if (f == 2 && cB(a, -1, -1) && cB(c, -1, 1) && cB(d, -2, 2)) {
                    MoveLeft(form.a);
                    MoveDown(form.a);
                    MoveUp(form.c);
                    MoveLeft(form.c);
                    MoveUp(form.d);
                    MoveUp(form.d);
                    MoveLeft(form.d);
                    MoveLeft(form.d);
                    form.changeForm();
                    break;
                }
                if (f == 3 && cB(a, -1, 1) && cB(c, 1, 1) && cB(d, 2, 2)) {
                    MoveLeft(form.a);
                    MoveUp(form.a);
                    MoveUp(form.c);
                    MoveRight(form.c);
                    MoveUp(form.d);
                    MoveUp(form.d);
                    MoveRight(form.d);
                    MoveRight(form.d);
                    form.changeForm();
                    break;
                }
                if (f == 4 && cB(a, 1, 1) && cB(c, 1, -1) && cB(d, 2, -2)) {
                    MoveRight(form.a);
                    MoveUp(form.a);
                    MoveDown(form.c);
                    MoveRight(form.c);
                    MoveDown(form.d);
                    MoveDown(form.d);
                    MoveRight(form.d);
                    MoveRight(form.d);
                    form.changeForm();
                    break;
                }
                break;
            case "l":
                if (f == 1 && cB(a, 1, -1) && cB(c, 1, 1) && cB(b, 2, 2)) {
                    MoveRight(form.a);
                    MoveDown(form.a);
                    MoveUp(form.c);
                    MoveRight(form.c);
                    MoveUp(form.b);
                    MoveUp(form.b);
                    MoveRight(form.b);
                    MoveRight(form.b);
                    form.changeForm();
                    break;
                }
                if (f == 2 && cB(a, -1, -1) && cB(b, 2, -2) && cB(c, 1, -1)) {
                    MoveLeft(form.a);
                    MoveDown(form.a);
                    MoveRight(form.b);
                    MoveRight(form.b);
                    MoveDown(form.b);
                    MoveDown(form.b);
                    MoveRight(form.c);
                    MoveDown(form.c);
                    form.changeForm();
                    break;
                }
                if (f == 3 && cB(a, -1, 1) && cB(b, -2, -2) && cB(c, -1, -1)) {
                    MoveLeft(form.a);
                    MoveUp(form.a);
                    MoveLeft(form.b);
                    MoveLeft(form.b);
                    MoveDown(form.b);
                    MoveDown(form.b);
                    MoveLeft(form.c);
                    MoveDown(form.c);
                    form.changeForm();
                    break;
                }
                if (f == 4 && cB(a, 1, 1) && cB(b, -2, 2) && cB(c, -1, 1)) {
                    MoveRight(form.a);
                    MoveUp(form.a);
                    MoveLeft(form.b);
                    MoveLeft(form.b);
                    MoveUp(form.b);
                    MoveUp(form.b);
                    MoveLeft(form.c);
                    MoveUp(form.c);
                    form.changeForm();
                    break;
                }
                break;
            case "o":
                break;
            case "s":
                if (f == 1 && cB(a, -1, -1) && cB(c, -1, 1) && cB(d, 0, 2)) {
                    MoveLeft(form.a);
                    MoveDown(form.a);
                    MoveUp(form.c);
                    MoveLeft(form.c);
                    MoveUp(form.d);
                    MoveUp(form.d);
                    form.changeForm();
                    break;
                }
                if (f == 2 && cB(a, 1, 1) && cB(c, 1, -1) && cB(d, 0, -2)) {
                    MoveRight(form.a);
                    MoveUp(form.a);
                    MoveRight(form.c);
                    MoveDown(form.c);
                    MoveDown(form.d);
                    MoveDown(form.d);
                    form.changeForm();
                    break;
                }
                if (f == 3 && cB(a, -1, -1) && cB(c, -1, 1) && cB(d, 0, 2)) {
                    MoveLeft(form.a);
                    MoveDown(form.a);
                    MoveLeft(form.c);
                    MoveUp(form.c);
                    MoveUp(form.d);
                    MoveUp(form.d);
                    form.changeForm();
                    break;
                }
                if (f == 4 && cB(a, 1, 1) && cB(c, 1, -1) && cB(d, 0, -2)) {
                    MoveRight(form.a);
                    MoveUp(form.a);
                    MoveRight(form.c);
                    MoveDown(form.c);
                    MoveDown(form.d);
                    MoveDown(form.d);
                    form.changeForm();
                    break;
                }
                break;
            case "t":
                if (f == 1 && cB(a, 1, 1) && cB(c, -1, 1) && cB(d, -1, -1)) {
                    MoveRight(form.a);
                    MoveUp(form.a);
                    MoveLeft(form.c);
                    MoveUp(form.c);
                    MoveLeft(form.d);
                    MoveDown(form.d);
                    form.changeForm();
                    break;
                }
                if (f == 2 && cB(a, 1, -1) && cB(c, 1, 1) && cB(d, -1, 1)) {
                    MoveRight(form.a);
                    MoveDown(form.a);
                    MoveRight(form.c);
                    MoveUp(form.c);
                    MoveLeft(form.d);
                    MoveUp(form.d);
                    form.changeForm();
                    break;
                }
                if (f == 3 && cB(a, -1, -1) && cB(c, 1, -1) && cB(d, 1, 1)) {
                    MoveLeft(form.a);
                    MoveDown(form.a);
                    MoveRight(form.c);
                    MoveDown(form.c);
                    MoveRight(form.d);
                    MoveUp(form.d);
                    form.changeForm();
                    break;
                }
                if (f == 4 && cB(a, -1, 1) && cB(c, -1, -1) && cB(d, 1, -1)) {
                    MoveLeft(form.a);
                    MoveUp(form.a);
                    MoveLeft(form.c);
                    MoveDown(form.c);
                    MoveRight(form.d);
                    MoveDown(form.d);
                    form.changeForm();
                    break;
                }
                break;
            case "z":
                if (f == 1 && cB(b, 1, 1) && cB(c, -1, 1) && cB(d, -2, 0)) {
                    MoveRight(form.b);
                    MoveUp(form.b);
                    MoveLeft(form.c);
                    MoveUp(form.c);
                    MoveLeft(form.d);
                    MoveLeft(form.d);
                    form.changeForm();
                    break;
                }
                if (f == 2 && cB(b, -1, -1) && cB(c, 1, -1) && cB(d, 2, 0)) {
                    MoveLeft(form.b);
                    MoveDown(form.b);
                    MoveRight(form.c);
                    MoveDown(form.c);
                    MoveRight(form.d);
                    MoveRight(form.d);
                    form.changeForm();
                    break;
                }
                if (f == 3 && cB(b, 1, 1) && cB(c, -1, 1) && cB(d, -2, 0)) {
                    MoveRight(form.b);
                    MoveUp(form.b);
                    MoveLeft(form.c);
                    MoveUp(form.c);
                    MoveLeft(form.d);
                    MoveLeft(form.d);
                    break;
                }
                if (f == 4 && cB(b, -1, -1) && cB(c, 1, -1) && cB(d, 2, 0)) {
                    MoveLeft(form.b);
                    MoveDown(form.b);
                    MoveRight(form.c);
                    MoveDown(form.c);
                    MoveRight(form.d);
                    MoveRight(form.d);
                    form.changeForm();
                    break;
                }
                break;
            case "i":
                if (f == 1 && cB(a, 2, 2) && cB(b, 1, 1) && cB(d, -2, 0)) {
                    MoveRight(form.a);
                    MoveRight(form.a);
                    MoveUp(form.a);
                    MoveUp(form.a);
                    MoveRight(form.b);
                    MoveUp(form.b);
                    MoveLeft(form.d);
                    MoveDown(form.d);
                    form.changeForm();
                    break;
                }
                if (f == 2 && cB(a, -2, -2) && cB(b, -1, -1) && cB(d, 1, 1)) {
                    MoveLeft(form.a);
                    MoveLeft(form.a);
                    MoveDown(form.a);
                    MoveDown(form.a);
                    MoveLeft(form.b);
                    MoveDown(form.b);
                    MoveRight(form.d);
                    MoveUp(form.d);
                    form.changeForm();
                    break;
                }
                if (f == 3 && cB(a, 2, 2) && cB(b, 1, 1) && cB(d, -1, -1)) {
                    MoveRight(form.a);
                    MoveRight(form.a);
                    MoveUp(form.a);
                    MoveUp(form.a);
                    MoveRight(form.b);
                    MoveUp(form.b);
                    MoveLeft(form.d);
                    MoveDown(form.d);
                    break;
                } if (f == 4 && cB(a, -2, -2) && cB(b, -1, -1) && cB(d, 1, 1)) {
                    MoveLeft(form.a);
                    MoveLeft(form.a);
                    MoveDown(form.a);
                    MoveDown(form.a);
                    MoveLeft(form.b);
                    MoveDown(form.b);
                    MoveRight(form.d);
                    MoveUp(form.d);
                    break;
            }
            break;
        }
    }

    private void RemoveRows (Pane pane) {
        //Store the data in 3 arrays
        ArrayList<Node> rects = new ArrayList<Node>();
        ArrayList<Integer> lines = new ArrayList<Integer>();
        ArrayList<Node> newrects = new ArrayList<Node>();
        int full = 0;
        //check to see which lines are filled
        for (int i = 0; i < MESH[0].length; i++) {
            for (int j = 0; j < MESH.length; j++) {
                if (MESH[j][i] == 1)
                    full++;
            }
            if (full == MESH.length)
                lines.add(i + lines.size());
            full = 0;
        }
        //Clear single row
        if (lines.size() == 1)
            do {
                for (Node node: pane.getChildren()) {
                    if (node instanceof Rectangle)
                        rects.add(node);
                }
                score += 50;
                linesNo++;

                //Delete blocks in a row
                for (Node node: rects) {
                    Rectangle a = (Rectangle)node;
                    if (a.getY() == lines.get(0) * SIZE) {
                        MESH[(int) a.getX()/SIZE][(int) a.getY()/SIZE] = 0;
                        pane.getChildren().remove(node);
                    } else
                        newrects.add(node);
                }

                for (Node node: newrects) {
                    Rectangle a = (Rectangle) node;
                    if (a.getY() < lines.get(0) * SIZE) {
                        MESH[(int) a.getX() / SIZE][(int) a.getY() / SIZE] = 0;
                        a.setY(a.getY() + SIZE);
                    }
                }
                lines.remove(0);
                rects.clear();
                newrects.clear();

                for (Node node: pane.getChildren()) {
                    if (node instanceof Rectangle)
                        rects.add(node);
                }
                for (Node node: rects) {
                    Rectangle a = (Rectangle) node;
                    try {
                        MESH[(int) a.getX() / SIZE][(int) a.getY() / SIZE] = 1;
                    } catch (ArrayIndexOutOfBoundsException e) {

                    }
                }
                rects.clear();
            } while(lines.size() > 0);

        if (lines.size() == 2)
            do {
                for (Node node: pane.getChildren()) {
                    if (node instanceof Rectangle)
                        rects.add(node);
                }
                score += 70;
                linesNo++;

                //Delete blocks in a row
                for (Node node: rects) {
                    Rectangle a = (Rectangle)node;
                    if (a.getY() == lines.get(0) * SIZE) {
                        MESH[(int) a.getX()/SIZE][(int) a.getY()/SIZE] = 0;
                        pane.getChildren().remove(node);
                    } else
                        newrects.add(node);
                }

                for (Node node: newrects) {
                    Rectangle a = (Rectangle) node;
                    if (a.getY() < lines.get(0) * SIZE) {
                        MESH[(int) a.getX() / SIZE][(int) a.getY() / SIZE] = 0;
                        a.setY(a.getY() + SIZE);
                    }
                }
                lines.remove(0);
                rects.clear();
                newrects.clear();

                for (Node node: pane.getChildren()) {
                    if (node instanceof Rectangle)
                        rects.add(node);
                }
                for (Node node: rects) {
                    Rectangle a = (Rectangle) node;
                    try {
                        MESH[(int) a.getX() / SIZE][(int) a.getY() / SIZE] = 1;
                    } catch (ArrayIndexOutOfBoundsException e) {

                    }
                }
                rects.clear();
            } while(lines.size() > 0);
        if (lines.size() == 3)
            do {
                for (Node node: pane.getChildren()) {
                    if (node instanceof Rectangle)
                        rects.add(node);
                }
                score += 80;
                linesNo++;

                //Delete blocks in a row
                for (Node node: rects) {
                    Rectangle a = (Rectangle)node;
                    if (a.getY() == lines.get(0) * SIZE) {
                        MESH[(int) a.getX()/SIZE][(int) a.getY()/SIZE] = 0;
                        pane.getChildren().remove(node);
                    } else
                        newrects.add(node);
                }

                for (Node node: newrects) {
                    Rectangle a = (Rectangle) node;
                    if (a.getY() < lines.get(0) * SIZE) {
                        MESH[(int) a.getX() / SIZE][(int) a.getY() / SIZE] = 0;
                        a.setY(a.getY() + SIZE);
                    }
                }
                lines.remove(0);
                rects.clear();
                newrects.clear();

                for (Node node: pane.getChildren()) {
                    if (node instanceof Rectangle)
                        rects.add(node);
                }
                for (Node node: rects) {
                    Rectangle a = (Rectangle) node;
                    try {
                        MESH[(int) a.getX() / SIZE][(int) a.getY() / SIZE] = 1;
                    } catch (ArrayIndexOutOfBoundsException e) {

                    }
                }
                rects.clear();
            } while(lines.size() > 0);
        if (lines.size() >= 4)
            do {
                for (Node node: pane.getChildren()) {
                    if (node instanceof Rectangle)
                        rects.add(node);
                }
                score += 100;
                linesNo++;

                //Delete blocks in a row
                for (Node node: rects) {
                    Rectangle a = (Rectangle)node;
                    if (a.getY() == lines.get(0) * SIZE) {
                        MESH[(int) a.getX()/SIZE][(int) a.getY()/SIZE] = 0;
                        pane.getChildren().remove(node);
                    } else
                        newrects.add(node);
                }

                for (Node node: newrects) {
                    Rectangle a = (Rectangle) node;
                    if (a.getY() < lines.get(0) * SIZE) {
                        MESH[(int) a.getX() / SIZE][(int) a.getY() / SIZE] = 0;
                        a.setY(a.getY() + SIZE);
                    }
                }
                lines.remove(0);
                rects.clear();
                newrects.clear();

                for (Node node: pane.getChildren()) {
                    if (node instanceof Rectangle)
                        rects.add(node);
                }
                for (Node node: rects) {
                    Rectangle a = (Rectangle) node;
                    try {
                        MESH[(int) a.getX() / SIZE][(int) a.getY() / SIZE] = 1;
                    } catch (ArrayIndexOutOfBoundsException e) {

                    }
                }
                rects.clear();
            } while(lines.size() > 0);
    }

    private void MoveDown(Rectangle rect) {
        if (rect.getY() + MOVE < YMAX)
            rect.setY(rect.getY() + MOVE);
    }
    private void MoveRight(Rectangle rect) {
        if (rect.getX() + MOVE <= XMAX - SIZE)
            rect.setX(rect.getX() + MOVE);
    }
    private void MoveLeft(Rectangle rect) {
        if (rect.getX() - MOVE >= 0)
            rect.setX(rect.getX() - MOVE);
    }
    private void MoveUp(Rectangle rect) {
        if (rect.getY() - MOVE > 0)
            rect.setY(rect.getY() - MOVE);
    }
    public void MoveDown (Form form) {
        //Moving if bottom is full

        if (form.a.getY() == YMAX - SIZE ||
            form.b.getY() == YMAX - SIZE ||
            form.c.getY() == YMAX - SIZE ||
            form.d.getY() == YMAX - SIZE ||
            moveA(form) ||
            moveB(form) ||
            moveC(form) ||
            moveD(form)) {

            MESH[(int)form.a.getX() / SIZE][(int)form.a.getY() / SIZE] = 1;
            MESH[(int)form.b.getX() / SIZE][(int)form.b.getY() / SIZE] = 1;
            MESH[(int)form.c.getX() / SIZE][(int)form.c.getY() / SIZE] = 1;
            MESH[(int)form.d.getX() / SIZE][(int)form.d.getY() / SIZE] = 1;
            RemoveRows(group);

            //Create a new block and add it to the scene
            Form a = nextObj;
            nextObj = Controller.makeRect();
            object = a;
            group.getChildren().addAll(a.a, a.b, a.c, a.d);
            moveOnKeyPress(a);

        }

        //Move one block down if down is not full
        if (form.a.getY() + MOVE < YMAX &&
                form.b.getY() + MOVE < YMAX &&
                form.c.getY() + MOVE < YMAX &&
                form.d.getY() + MOVE < YMAX) {
            int move_a = MESH [(int) form.a.getX() / SIZE][((int) form.a.getY() / SIZE) + 1];
            int move_b = MESH [(int) form.b.getX() / SIZE][((int) form.b.getY() / SIZE) + 1];
            int move_c = MESH [(int) form.c.getX() / SIZE][((int) form.c.getY() / SIZE) + 1];
            int move_d = MESH [(int) form.d.getX() / SIZE][((int) form.d.getY() / SIZE) + 1];

            if(move_a == 0 && move_a == move_b && move_b == move_c && move_c == move_d) {
                form.a.setY(form.a.getY() + MOVE);
                form.b.setY(form.b.getY() + MOVE);
                form.c.setY(form.c.getY() + MOVE);
                form.d.setY(form.d.getY() + MOVE);
            }
        }
    }

    private boolean moveA(Form form) {
        return (MESH[(int) form.a.getX() / SIZE][((int) form.a.getY() / SIZE) + 1] == 1);
    }
    private boolean moveB(Form form) {
        return (MESH[(int) form.b.getX() / SIZE][((int) form.b.getY() / SIZE) + 1] == 1);
    }
    private boolean moveC(Form form) {
        return (MESH[(int) form.c.getX() / SIZE][((int) form.c.getY() / SIZE) + 1] == 1);
    }
    private boolean moveD(Form form) {
        return (MESH[(int) form.d.getX() / SIZE][((int) form.d.getY() / SIZE) + 1] == 1);
    }

    private boolean cB(Rectangle rect, int x, int y) {
        boolean yb = false;
        boolean xb = false;
        if(x >= 0)
            xb = rect.getX() + x * MOVE <= XMAX - SIZE; //if going right make sure x is less than max x
        if(x < 0)
            xb = rect.getX() + x * MOVE >= 0; //if going left make sure x is still positive
        if(y >= 0)
            yb = rect.getY() - y * MOVE > 0; //check if withing vertical bounds
        if(y < 0)
            yb = rect.getY() + y * MOVE < YMAX;
        return xb && yb && MESH[((int) rect.getX() / SIZE) + x][((int) rect.getY() / SIZE) - y] == 0;
    }

}

package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Form {
    Rectangle a;            //for a rectangle for each block that will compose a tetris piece
    Rectangle b;            //    a
    Rectangle c;            //  b d c
    Rectangle d;
    Color color;
    private String name;
    public int form = 1;

    //Constructor
    public Form(Rectangle a, Rectangle b, Rectangle c, Rectangle d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public Form(Rectangle a, Rectangle b, Rectangle c, Rectangle d, String name) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.name = name;

        // PIECES
        // j = ■  l = ■    o = ■ ■   s =  ■ ■  t =  ■    z = ■ ■    i = ■ ■ ■ ■
        //     ■      ■        ■ ■      ■ ■       ■ ■ ■        ■ ■
        //   ■ ■      ■ ■

        //set color of pieces
        switch (name) {
            case "j":
                color = Color.DARKVIOLET;
                break;
            case "l":
                color = Color.MEDIUMTURQUOISE;
                break;
            case "o":
                color = Color.DARKORANGE;
                break;
            case "s":
                color = Color.YELLOWGREEN;
                break;
            case "t":
                color = Color.DEEPSKYBLUE;
                break;
            case "z":
                color = Color.MEDIUMVIOLETRED;
                break;
            case "i":
                color = Color.GOLD;
                break;
        }
        this.a.setFill(color);
        this.b.setFill(color);
        this.c.setFill(color);
        this.d.setFill(color);
    }
    //get name of piece
    public String getName() {
        return name;
    }
    public void changeForm() {
        if (form != 4) {
            form++;
        } else {
            form = 1;
        }
    }
}

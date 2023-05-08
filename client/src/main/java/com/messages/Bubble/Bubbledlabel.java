package com.messages.Bubble;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
public class Bubbledlabel extends Label {

    private BubbleType type = BubbleType.LEFT_CENTER;
    private double pading = 5.0;
    private boolean systemCall = false;

    public Bubbledlabel() {
        super();
        init();
    }

    public Bubbledlabel(String arg0, Node arg1) {
        super(arg0, arg1);
        init();
    }

    public Bubbledlabel(String arg0) {
        super(arg0);
        init();
    }

    public Bubbledlabel(BubbleType bubbleType) {
        super();
        this.type = bubbleType;
        init();
    }

    public Bubbledlabel(String arg0, Node arg1,BubbleType bubbleType) {
        super(arg0, arg1);
        this.type = bubbleType;
        init();
    }

    public Bubbledlabel(String arg0,BubbleType bubbleType) {
        super(arg0);
        this.type = bubbleType;
        init();
    }

    private void init(){
        DropShadow ds = new DropShadow();
        ds.setOffsetX(1.3);
        ds.setOffsetY(1.3);
        ds.setColor(Color.WHITE);
        setPrefSize(Label.USE_COMPUTED_SIZE, Label.USE_COMPUTED_SIZE);
        shapeProperty().addListener((arg0, arg1, arg2) -> {
            if(systemCall){
                systemCall = false;
            }else{
                shapeIt();
            }
        });

        heightProperty().addListener(arg0 -> {
            if(!systemCall)
                setPrefHeight(Label.USE_COMPUTED_SIZE);
        });

        widthProperty().addListener(observable -> {
            if(!systemCall)
                setPrefHeight(Label.USE_COMPUTED_SIZE);
        });

        shapeIt();
    }

    @Override
    protected void updateBounds() {
        super.updateBounds();
        //top right  bottom  left
        switch (type) {
            case LEFT_BOTTOM:
                setPadding(new Insets(pading,pading,
                        (this.getBoundsInLocal().getWidth()*((Bubble)getShape()).drawRectBubbleIndicatorRule)/2
                                +pading,
                        pading));
                break;
            case LEFT_CENTER:
                setPadding(new Insets(pading,pading,pading,
                        (this.getBoundsInLocal().getWidth()*((Bubble)getShape()).drawRectBubbleIndicatorRule)/2
                                +pading
                ));
                break;
            case RIGHT_BOTTOM, RIGHT_CENTER:
                setPadding(new Insets(pading,
                        (this.getBoundsInLocal().getWidth()*((Bubble)getShape()).drawRectBubbleIndicatorRule)/2
                                +pading
                        ,pading,pading));
                break;
            case TOP:
                setPadding(new Insets(
                        (this.getBoundsInLocal().getWidth()*((Bubble)getShape()).drawRectBubbleIndicatorRule)/2
                                +pading,
                        pading,pading,pading));
                break;

            default:
                break;
        }
    }

    public final double getPading() {
        return pading;
    }

    public void setPading(double pading) {
        if(pading > 25.0)
            return;
        this.pading = pading;
    }

    public BubbleType getBubbleSpec() {
        return type;
    }

    public void setBubbleSpec(BubbleType bubbleType) {
        this.type = bubbleType;
        shapeIt();
    }

    private final void shapeIt(){
        systemCall = true;
        setShape(new Bubble(type));
        System.gc();
    }
}


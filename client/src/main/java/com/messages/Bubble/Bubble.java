package com.messages.Bubble;

import javafx.scene.shape.HLineTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.VLineTo;

/*
   This package(including Bubble, Bubbledlabel, BubbleType) is used to create message bubbles while chatting.
   The original author is DomHeal, here is the GitHub link to the original project: https://github.com/DomHeal/JavaFX-Chat.git
 */
public class Bubble extends Path{

    public Bubble(BubbleType bubbleType) {
        super();
        switch (bubbleType) {
            case BOTTOM:
                break;
            case RIGHT_BOTTOM:
                drawRectBubbleRightBaselineIndicator();
                break;
            case RIGHT_CENTER:
                drawRectBubbleRightCenterIndicator();
                break;
            case LEFT_BOTTOM:
                drawRectBubbleLeftBaselineIndicator();
                break;
            case LEFT_CENTER:
                drawRectBubbleLeftCenterIndicator();
                break;
            case TOP:
                drawRectBubbleToplineIndicator();
                break;

            default:
                break;
        }

    }

    private void drawRectBubbleToplineIndicator() {
        getElements().addAll(new MoveTo(1.0f, 1.2f),
                new HLineTo(2.5f),
                new LineTo(2.7f, 1.0f),
                new LineTo(2.9f, 1.2f),
                new HLineTo(4.4f),
                new VLineTo(4f),
                new HLineTo(1.0f),
                new VLineTo(1.2f)
        );
    }

    private void drawRectBubbleRightBaselineIndicator() {
        getElements().addAll(new MoveTo(3.0f, 1.0f),
                new HLineTo(0f),
                new VLineTo(4f),
                new HLineTo(3.0f),
                new LineTo(2.8f, 3.8f),
                new VLineTo(1f)
        );
    }

    private void drawRectBubbleLeftBaselineIndicator() {
        getElements().addAll(new MoveTo(1.2f, 1.0f),
                new HLineTo(3f),
                new VLineTo(4f),
                new HLineTo(1.0f),
                new LineTo(1.2f, 3.8f),
                new VLineTo(1f)
        );
    }

    private void drawRectBubbleRightCenterIndicator() {
        getElements().addAll(new MoveTo(3.0f, 2.5f),
                new LineTo(2.8f, 2.4f),
                new VLineTo(1f),
                new HLineTo(0f),
                new VLineTo(4f),
                new HLineTo(2.8f),
                new VLineTo(2.7f),
                new LineTo(3.0f, 2.5f)
        );
    }

    protected double drawRectBubbleIndicatorRule = 0.2;

    private void drawRectBubbleLeftCenterIndicator() {
        getElements().addAll(new MoveTo(1.0f, 2.5f),
                new LineTo(1.2f, 2.4f),
                new VLineTo(1f),
                new HLineTo(2.9f),
                new VLineTo(4f),
                new HLineTo(1.2f),
                new VLineTo(2.7f),
                new LineTo(1.0f, 2.5f)
        );
    }


}
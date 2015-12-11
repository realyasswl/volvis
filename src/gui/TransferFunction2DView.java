/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author michel
 */
public class TransferFunction2DView extends javax.swing.JPanel {
    
    TransferFunction2DEditor ed;
    private final int DOTSIZE = 8;
    public Ellipse2D.Double radiusAndMinGradientPoint;
    public Rectangle2D.Double controlArea;
    private double leftControlArea,rightControlArea, bottomControlArea,topControlArea;
//    public Rectangle2D.Double coverArea;
    public java.awt.geom.GeneralPath coverArea;
    boolean selectedRadiusAndMinGradientPoint, selectedControlArea;
    private double maxGradientMagnitude;
    public TransferFunction2DView(TransferFunction2DEditor ed) {
        initComponents();
        
        this.ed = ed;
        selectedRadiusAndMinGradientPoint = false;
        selectedControlArea = false;
        addMouseMotionListener(new TriangleWidgetHandler());
        addMouseListener(new SelectionHandler());
    }
    
    @Override
    public void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        int w = this.getWidth();
        int h = this.getHeight();
        g2.setColor(Color.white);
        g2.fillRect(0, 0, w, h);
        
        maxGradientMagnitude = ed.histogram[0];
        for (int i = 0; i < ed.histogram.length; i++) {
            maxGradientMagnitude = ed.histogram[i] > maxGradientMagnitude ? ed.histogram[i] : maxGradientMagnitude;
        }
        
        double binWidth = (double) w / (double) ed.xbins;
        double binHeight = (double) h / (double) ed.ybins;
        maxGradientMagnitude = Math.log(maxGradientMagnitude);
        for (int y = 0; y < ed.ybins; y++) {
            for (int x = 0; x < ed.xbins; x++) {
                if (ed.histogram[y * ed.xbins + x] > 0) {
                    int intensity = (int) Math.floor(255 * (1.0 - Math.log(ed.histogram[y * ed.xbins + x]) / maxGradientMagnitude));
                    g2.setColor(new Color(intensity, intensity, intensity));
                    g2.fill(new Rectangle2D.Double(x * binWidth, h - (y * binHeight), binWidth, binHeight));
                }
            }
        }
        
        int xpos = (int) (ed.triangleWidget.baseIntensity * binWidth);
        int yGraMax = h-(int) (ed.triangleWidget.graMax * h / ed.getMaxGradientMagnitude());
        int yGraMin = h-(int) (ed.triangleWidget.graMin * h / ed.getMaxGradientMagnitude());
        
//        int x_l=xpos - (int) (ed.triangleWidget.radius * binWidth * maxGradientMagnitude);
//        int x_r=xpos + (int) (ed.triangleWidget.radius * binWidth * maxGradientMagnitude);
        int x_l=xpos - (int) (ed.triangleWidget.radius * binWidth * ed.getMaxGradientMagnitude());
        int x_r=xpos + (int) (ed.triangleWidget.radius * binWidth * ed.getMaxGradientMagnitude());
        int y_t=yGraMax;
        int y_b=yGraMin;
        
        Color colorFromWidget=new Color((int)(ed.triangleWidget.color.r*(double)255),
                (int)(ed.triangleWidget.color.g*(double)255),
                (int)(ed.triangleWidget.color.b*(double)255),
                (int)(ed.triangleWidget.color.a*(double)255));
        g2.setColor(colorFromWidget);
        
        radiusAndMinGradientPoint = new Ellipse2D.Double(x_r - DOTSIZE / 2,
                y_b - DOTSIZE, DOTSIZE, DOTSIZE);
        g2.fill(radiusAndMinGradientPoint);
        g2.drawLine(x_l, yGraMax,x_l, yGraMin);
        g2.drawLine(x_r, yGraMax,x_r, yGraMin);
        g2.drawLine(x_l, yGraMax,x_r, yGraMax);
        g2.drawLine(x_l, yGraMin,x_r, yGraMin);
//        coverArea=new Rectangle2D.Double(x_l,y_t,x_r-x_l,y_b-y_t);
        leftControlArea=xpos-(xpos-x_l)*(h-y_t)/h;
        bottomControlArea=y_b;
        topControlArea=y_t;
        rightControlArea=xpos+(xpos-x_l)*(h-y_t)/h;
        coverArea=new GeneralPath();
      coverArea.moveTo(leftControlArea, topControlArea);
      coverArea.lineTo(xpos-(xpos-x_l)*(h-y_b)/h, bottomControlArea);
      coverArea.lineTo(xpos+(xpos-x_l)*(h-y_b)/h, bottomControlArea);
      coverArea.lineTo(rightControlArea, topControlArea);
      coverArea.closePath();
        g2.fill(coverArea);
        double[] areaCenter = {xpos - DOTSIZE / 2,
            y_b / 2 + y_t / 2 - DOTSIZE / 2};
        controlArea = new Rectangle2D.Double(areaCenter[0], areaCenter[1],
                DOTSIZE, DOTSIZE);
        g2.fill(controlArea);
    }
    
    private class TriangleWidgetHandler extends MouseMotionAdapter {
        @Override
        public void mouseMoved(MouseEvent e) {
            if (radiusAndMinGradientPoint.contains(e.getPoint()) || controlArea.contains(e.getPoint())) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            } else {
                setCursor(Cursor.getDefaultCursor());
            }
        }
        
        @Override
        public void mouseDragged(MouseEvent e) {
            if (selectedRadiusAndMinGradientPoint || selectedControlArea) {
                Point dragEnd = e.getPoint();
                if (selectedRadiusAndMinGradientPoint) {
                    // restrain to horizontal movement
                    if(dragEnd.x<= leftControlArea) {
                        dragEnd.x = (int) (leftControlArea + 1);
                    }
                    if (dragEnd.y <= topControlArea) {
                        dragEnd.y = (int) (topControlArea + 1);
                    }
                }
//                else if (selectedControlArea) {
//                    // restrain to horizontal movement and avoid radius getting 0
//                }
                if (dragEnd.x < 0) {
                    dragEnd.x = 0;
                }
                if (dragEnd.x >= getWidth()) {
                    dragEnd.x = getWidth() - 1;
                }
                if (dragEnd.y < 0) {
                    dragEnd.y = 0;
                }
                if (dragEnd.y >= getHeight()) {
                    dragEnd.y = getHeight() - 1;
                }
                double w = getWidth();
                double h = getHeight();
                double binWidth = (double) w / (double) ed.xbins;
                if (selectedRadiusAndMinGradientPoint) {
//                    ed.triangleWidget.radius = (dragEnd.x - (ed.triangleWidget.baseIntensity * binWidth)) / (binWidth * maxGradientMagnitude);
                    ed.triangleWidget.radius = (dragEnd.x - (ed.triangleWidget.baseIntensity * binWidth)) / (binWidth * ed.getMaxGradientMagnitude());
                    ed.triangleWidget.graMin = (ed.getMaxGradientMagnitude() - dragEnd.y * ed.getMaxGradientMagnitude() / h);
                } else if (selectedControlArea) {
                    ed.triangleWidget.baseIntensity = (short) (dragEnd.x / binWidth);
//                    ed.triangleWidget.graMin = (ed.getMaxGradientMagnitude() - (dragEnd.y + coverArea.height / 2) * ed.getMaxGradientMagnitude() / h);
//                    ed.triangleWidget.graMax = (ed.getMaxGradientMagnitude() - (dragEnd.y - coverArea.height / 2) * ed.getMaxGradientMagnitude() / h);
                    ed.triangleWidget.graMin = (ed.getMaxGradientMagnitude() - (dragEnd.y + (bottomControlArea-topControlArea) / 2) * ed.getMaxGradientMagnitude() / h);
                    ed.triangleWidget.graMax = (ed.getMaxGradientMagnitude() - (dragEnd.y - (bottomControlArea-topControlArea) / 2) * ed.getMaxGradientMagnitude() / h);
                }
                ed.setSelectedInfo();
                repaint();
            }
        }

    }

    private class SelectionHandler extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (radiusAndMinGradientPoint.contains(e.getPoint())) {
                selectedRadiusAndMinGradientPoint = true;
            } else if (controlArea.contains(e.getPoint())) {
                selectedControlArea = true;
            } else {
                selectedControlArea = false;
                selectedRadiusAndMinGradientPoint = false;
            }
        }
        
        @Override
        public void mouseReleased(MouseEvent e) {
            selectedControlArea = false;
            selectedRadiusAndMinGradientPoint = false;
            ed.changed();
            repaint();
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

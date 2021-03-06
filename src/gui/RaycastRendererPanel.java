/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import javax.swing.JOptionPane;
import volvis.RaycastRenderer;

/**
 *
 * @author michel
 */
public class RaycastRendererPanel extends javax.swing.JPanel {

    RaycastRenderer renderer;
    TransferFunctionEditor tfEditor = null;
    TransferFunction2DEditor tfEditor2D = null;
    
    /**
     * Creates new form RaycastRendererPanel
     */
    public RaycastRendererPanel(RaycastRenderer renderer) {
        initComponents();
        this.renderer = renderer;
    }

    public void setSpeedLabel(String text) {
        renderingSpeedLabel.setText(text);
    }
    
    public void setRateLabel(double ambient,double diff, double spec){
        ambientValueLabel.setText(String.format("%.2f", ambient));
        diffValueLabel.setText(String.format("%.2f", diff));
        specValueLabel.setText(String.format("%.2f", spec));
        adjustRate(ambient,diff,spec);
    }
    
    private void adjustRate(double ambient, double diff, double spec) {
        ambientSlider.setValue((int) (ambient * (double) ambientSlider.getMaximum()));
        specSlider.setValue((int) (spec * (double) specSlider.getMaximum()));
        diffSlider.setValue((int) (diff * (double) diffSlider.getMaximum()));
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        renderingSpeedLabel = new javax.swing.JLabel();
        slicerButton = new javax.swing.JRadioButton();
        mipButton = new javax.swing.JRadioButton();
        compositingButton = new javax.swing.JRadioButton();
        tf2dButton = new javax.swing.JRadioButton();
        shadingCheckbox = new javax.swing.JCheckBox();
        ambientSlider = new javax.swing.JSlider();
        diffSlider = new javax.swing.JSlider();
        specSlider = new javax.swing.JSlider();
        ambientLabel = new javax.swing.JLabel();
        diffLabel = new javax.swing.JLabel();
        specLabel = new javax.swing.JLabel();
        refreshButton = new javax.swing.JButton();
        ambientValueLabel = new javax.swing.JLabel();
        diffValueLabel = new javax.swing.JLabel();
        specValueLabel = new javax.swing.JLabel();
        d2TriCheckBox = new javax.swing.JCheckBox();
        compositingTriCheckBox = new javax.swing.JCheckBox();
        mipTriCheckBox = new javax.swing.JCheckBox();
        slicerTriCheckBox = new javax.swing.JCheckBox();

        jLabel1.setText("Rendering time (ms):");

        renderingSpeedLabel.setText("jLabel2");

        buttonGroup1.add(slicerButton);
        slicerButton.setSelected(true);
        slicerButton.setText("Slicer");
        slicerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                slicerButtonActionPerformed(evt);
            }
        });

        buttonGroup1.add(mipButton);
        mipButton.setText("MIP");
        mipButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mipButtonActionPerformed(evt);
            }
        });

        buttonGroup1.add(compositingButton);
        compositingButton.setText("Compositing");
        compositingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                compositingButtonActionPerformed(evt);
            }
        });

        buttonGroup1.add(tf2dButton);
        tf2dButton.setText("2D Transfer");
        tf2dButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf2dButtonActionPerformed(evt);
            }
        });

        shadingCheckbox.setText("Volume shading");
        shadingCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shadingCheckboxActionPerformed(evt);
            }
        });

        ambientSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                ambientSliderStateChanged(evt);
            }
        });

        diffSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                diffSliderStateChanged(evt);
            }
        });

        specSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                specSliderStateChanged(evt);
            }
        });

        ambientLabel.setText("kAmbient");

        diffLabel.setText("kDiff");

        specLabel.setText("kSpec");

        refreshButton.setText("Apply rate");
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });

        ambientValueLabel.setText("jLabel2");

        diffValueLabel.setText("jLabel2");

        specValueLabel.setText("jLabel2");

        d2TriCheckBox.setText("Tri-linear");
        d2TriCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                d2TriCheckBoxActionPerformed(evt);
            }
        });

        compositingTriCheckBox.setText("Tri-linear");
        compositingTriCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                compositingTriCheckBoxActionPerformed(evt);
            }
        });

        mipTriCheckBox.setText("Tri-linear");
        mipTriCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mipTriCheckBoxActionPerformed(evt);
            }
        });

        slicerTriCheckBox.setText("Tri-linear");
        slicerTriCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                slicerTriCheckBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(diffLabel)
                            .addComponent(specLabel)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(shadingCheckbox)
                                    .addComponent(tf2dButton)
                                    .addComponent(compositingButton)
                                    .addComponent(mipButton)
                                    .addComponent(slicerButton))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(slicerTriCheckBox)
                                    .addComponent(mipTriCheckBox)
                                    .addComponent(compositingTriCheckBox)
                                    .addComponent(d2TriCheckBox)
                                    .addComponent(refreshButton))))
                        .addGap(290, 290, 290))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(renderingSpeedLabel))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(ambientLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(specSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                                    .addComponent(diffSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addComponent(ambientSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ambientValueLabel)
                                    .addComponent(diffValueLabel)
                                    .addComponent(specValueLabel))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(renderingSpeedLabel))
                .addGap(49, 49, 49)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(slicerButton)
                    .addComponent(slicerTriCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mipButton)
                    .addComponent(mipTriCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(compositingButton)
                    .addComponent(compositingTriCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tf2dButton)
                            .addComponent(d2TriCheckBox))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                        .addComponent(shadingCheckbox))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(refreshButton)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ambientLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(ambientSlider, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ambientValueLabel, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(diffLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(diffSlider, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(diffValueLabel))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addComponent(specLabel))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(specValueLabel))))
                    .addComponent(specSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void mipButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mipButtonActionPerformed
        renderer.setFunction(RaycastRenderer.FUNCTION_MIP);
        renderer.changed();
    }//GEN-LAST:event_mipButtonActionPerformed

    private void slicerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_slicerButtonActionPerformed
        renderer.setFunction(RaycastRenderer.FUNCTION_SLICER);
        renderer.changed();
    }//GEN-LAST:event_slicerButtonActionPerformed

    private void compositingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_compositingButtonActionPerformed
        renderer.setFunction(RaycastRenderer.FUNCTION_COMPOSITING);
        renderer.changed();
    }//GEN-LAST:event_compositingButtonActionPerformed

    private void tf2dButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf2dButtonActionPerformed
        renderer.setFunction(RaycastRenderer.FUNCTION_2DFUNC);
        renderer.changed();
    }//GEN-LAST:event_tf2dButtonActionPerformed

    private void shadingCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shadingCheckboxActionPerformed
        renderer.setShading(shadingCheckbox.isSelected());
        renderer.changed();
    }//GEN-LAST:event_shadingCheckboxActionPerformed

    private void ambientSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_ambientSliderStateChanged
        sliderChanged();
    }//GEN-LAST:event_ambientSliderStateChanged

    private void diffSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_diffSliderStateChanged
        sliderChanged();
    }//GEN-LAST:event_diffSliderStateChanged

    private void specSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_specSliderStateChanged
        sliderChanged();
    }//GEN-LAST:event_specSliderStateChanged

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        // TODO add your handling code here:
        renderer.changed();
    }//GEN-LAST:event_refreshButtonActionPerformed

    private void mipTriCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mipTriCheckBoxActionPerformed
        renderer.setMipTri(mipTriCheckBox.isSelected());
        renderer.changed();
    }//GEN-LAST:event_mipTriCheckBoxActionPerformed

    private void d2TriCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_d2TriCheckBoxActionPerformed
        renderer.setD2Tri(d2TriCheckBox.isSelected());
        renderer.changed();
    }//GEN-LAST:event_d2TriCheckBoxActionPerformed

    private void slicerTriCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_slicerTriCheckBoxActionPerformed
        renderer.setSlicerTri(slicerTriCheckBox.isSelected());
        renderer.changed();
    }//GEN-LAST:event_slicerTriCheckBoxActionPerformed

    private void compositingTriCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_compositingTriCheckBoxActionPerformed
        renderer.setCompositingTri(compositingTriCheckBox.isSelected());
        renderer.changed();
    }//GEN-LAST:event_compositingTriCheckBoxActionPerformed
    
    private void sliderChanged(){
        double ambientResult = (double) ambientSlider.getValue() / (double) (ambientSlider.getMaximum() - ambientSlider.getMinimum());
        double diffResult = (double) diffSlider.getValue() / (double) (diffSlider.getMaximum() - diffSlider.getMinimum());
        double specResult = (double) specSlider.getValue() / (double) (specSlider.getMaximum() - specSlider.getMinimum());
        ambientResult=ambientResult / (ambientResult + diffResult + specResult);
        diffResult=diffResult / (ambientResult + diffResult + specResult);
        specResult=specResult / (ambientResult + diffResult + specResult);
        renderer.setAmbient(ambientResult);
        renderer.setDiff(diffResult);
        renderer.setSpec(specResult);
        ambientValueLabel.setText(String.format("%.2f", ambientResult));
        diffValueLabel.setText(String.format("%.2f", diffResult));
        specValueLabel.setText(String.format("%.2f", specResult));
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ambientLabel;
    private javax.swing.JSlider ambientSlider;
    private javax.swing.JLabel ambientValueLabel;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JRadioButton compositingButton;
    private javax.swing.JCheckBox compositingTriCheckBox;
    private javax.swing.JCheckBox d2TriCheckBox;
    private javax.swing.JLabel diffLabel;
    private javax.swing.JSlider diffSlider;
    private javax.swing.JLabel diffValueLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JRadioButton mipButton;
    private javax.swing.JCheckBox mipTriCheckBox;
    private javax.swing.JButton refreshButton;
    private javax.swing.JLabel renderingSpeedLabel;
    private javax.swing.JCheckBox shadingCheckbox;
    private javax.swing.JRadioButton slicerButton;
    private javax.swing.JCheckBox slicerTriCheckBox;
    private javax.swing.JLabel specLabel;
    private javax.swing.JSlider specSlider;
    private javax.swing.JLabel specValueLabel;
    private javax.swing.JRadioButton tf2dButton;
    // End of variables declaration//GEN-END:variables

    public void setTriCheckbox(boolean slicerTri, boolean mipTri, boolean compositingTri, boolean d2Tri) {
        slicerTriCheckBox.setSelected(slicerTri);
        mipTriCheckBox.setSelected(mipTri);
        compositingTriCheckBox.setSelected(compositingTri);
        d2TriCheckBox.setSelected(d2Tri);
    }
}

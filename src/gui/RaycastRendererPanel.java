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
        tf2dButton.setText("2D Transfer function");
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 3, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(compositingButton)
                            .addComponent(tf2dButton)
                            .addComponent(mipButton)
                            .addComponent(slicerButton)
                            .addComponent(shadingCheckbox)
                            .addComponent(diffLabel)
                            .addComponent(specLabel))
                        .addGap(371, 371, 371))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(renderingSpeedLabel))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(ambientLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(diffSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ambientSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(specSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
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
                .addComponent(slicerButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mipButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(compositingButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tf2dButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(shadingCheckbox)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ambientLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(ambientSlider, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(diffLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(diffSlider, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(13, 13, 13)
                        .addComponent(specLabel))
                    .addComponent(specSlider, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void mipButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mipButtonActionPerformed
        renderer.setFunction(RaycastRenderer.FUNCTION_MIP);
    }//GEN-LAST:event_mipButtonActionPerformed

    private void slicerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_slicerButtonActionPerformed
        renderer.setFunction(RaycastRenderer.FUNCTION_SLICER);
    }//GEN-LAST:event_slicerButtonActionPerformed

    private void compositingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_compositingButtonActionPerformed
        renderer.setFunction(RaycastRenderer.FUNCTION_COMPOSITING);
    }//GEN-LAST:event_compositingButtonActionPerformed

    private void tf2dButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf2dButtonActionPerformed
        renderer.setFunction(RaycastRenderer.FUNCTION_2DFUNC);
    }//GEN-LAST:event_tf2dButtonActionPerformed

    private void shadingCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shadingCheckboxActionPerformed
        renderer.setShading(shadingCheckbox.isSelected());
    }//GEN-LAST:event_shadingCheckboxActionPerformed

    private void ambientSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_ambientSliderStateChanged
        sliderChanged();
    }//GEN-LAST:event_ambientSliderStateChanged

    private void diffSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_diffSliderStateChanged
        // TODO add your handling code here:
        sliderChanged();
    }//GEN-LAST:event_diffSliderStateChanged

    private void specSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_specSliderStateChanged
        // TODO add your handling code here:
        sliderChanged();
    }//GEN-LAST:event_specSliderStateChanged
    
    private void sliderChanged(){
        double ambientResult = (double) ambientSlider.getValue() / (double) (ambientSlider.getMaximum() - ambientSlider.getMinimum());
        double diffResult = (double) diffSlider.getValue() / (double) (diffSlider.getMaximum() - diffSlider.getMinimum());
        double specResult = (double) specSlider.getValue() / (double) (specSlider.getMaximum() - specSlider.getMinimum());
        renderer.setAmbient(ambientResult / (ambientResult + diffResult + specResult));
        renderer.setDiff(diffResult / (ambientResult + diffResult + specResult));
        renderer.setSpec(specResult / (ambientResult + diffResult + specResult));
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ambientLabel;
    private javax.swing.JSlider ambientSlider;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JRadioButton compositingButton;
    private javax.swing.JLabel diffLabel;
    private javax.swing.JSlider diffSlider;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JRadioButton mipButton;
    private javax.swing.JLabel renderingSpeedLabel;
    private javax.swing.JCheckBox shadingCheckbox;
    private javax.swing.JRadioButton slicerButton;
    private javax.swing.JLabel specLabel;
    private javax.swing.JSlider specSlider;
    private javax.swing.JRadioButton tf2dButton;
    // End of variables declaration//GEN-END:variables
}

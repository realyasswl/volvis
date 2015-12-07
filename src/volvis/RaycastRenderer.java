/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package volvis;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

import gui.RaycastRendererPanel;
import gui.TransferFunction2DEditor;
import gui.TransferFunctionEditor;
import java.awt.image.BufferedImage;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import util.TFChangeListener;
import util.VectorMath;
import volume.GradientVolume;
import volume.Volume;
import volume.VoxelGradient;

/**
 *
 * @author michel
 */
public class RaycastRenderer extends Renderer implements TFChangeListener {

    private static Log logger = LogFactory.getLog(RaycastRenderer.class);
    private Volume volume = null;
    private GradientVolume gradients = null;
    RaycastRendererPanel panel;
    TransferFunction tFunc;
    TransferFunctionEditor tfEditor;
    TransferFunction2DEditor tfEditor2D;

    int granularity = 1;
    int imageSize = 0;
    
    /**shading parameters*/
    double kAmbient=0.1;
    double kDiff=0.7;
    double kSpec=0.2;
    TFColor light=new TFColor(1,1,1,1);
    int a=10;    
    
    private boolean shading;

    public void setShading(boolean s) {
        shading = s;
    }

    public RaycastRenderer() {
        panel = new RaycastRendererPanel(this);
        panel.setSpeedLabel("0");
    }

    public void setVolume(Volume vol) {
        System.out.println("Assigning volume");
        volume = vol;

        System.out.println("Computing gradients");
        gradients = new GradientVolume(vol);

        // set up image for storing the resulting rendering
        // the image width and height are equal to the length of the volume diagonal
        imageSize = (int) Math.floor(Math.sqrt(vol.getDimX() * vol.getDimX() + vol.getDimY() * vol.getDimY()
                + vol.getDimZ() * vol.getDimZ()));
        if (imageSize % 2 != 0) {
            imageSize = imageSize + 1;
        }
//        buffer = new int[imageSize / granularity + 1];
        image = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_ARGB);
        // create a standard TF where lowest intensity maps to black, the highest to white, and opacity increases
        // linearly from 0.0 to 1.0 over the intensity range
        tFunc = new TransferFunction(volume.getMinimum(), volume.getMaximum());

        // uncomment this to initialize the TF with good starting values for the orange dataset 
        //tFunc.setTestFunc();
        tFunc.addTFChangeListener(this);
        tfEditor = new TransferFunctionEditor(tFunc, volume.getHistogram());

        tfEditor2D = new TransferFunction2DEditor(volume, gradients);
        tfEditor2D.addTFChangeListener(this);

        System.out.println("Finished initialization of RaycastRenderer");
    }

    public RaycastRendererPanel getPanel() {
        return panel;
    }

    public TransferFunction2DEditor getTF2DPanel() {
        return tfEditor2D;
    }

    public TransferFunctionEditor getTFPanel() {
        return tfEditor;
    }

    short getVoxel(double[] coord) {
        if (coord[0] < 0 || coord[0] > volume.getDimX() || coord[1] < 0 || coord[1] > volume.getDimY()
                || coord[2] < 0 || coord[2] > volume.getDimZ()) {
            return 0;
        }

        int x = (int) Math.floor(coord[0]);
        int y = (int) Math.floor(coord[1]);
        int z = (int) Math.floor(coord[2]);

        return volume.getVoxel(x, y, z);
    }

    VoxelGradient getGradient(double[] coord) {

        int x = (int) Math.floor(coord[0]);
        int y = (int) Math.floor(coord[1]);
        int z = (int) Math.floor(coord[2]);

        if (x < 0 || x > volume.getDimX() || y < 0 || y > volume.getDimY()
                || z < 0 || z > volume.getDimZ()) {
            return new VoxelGradient();
        }
        return gradients.getVoxel(x, y, z);
    }

    short getTriVoxel(double[] coord) {
        double xd = coord[0];
        double yd = coord[1];
        double zd = coord[2];

        int x = (int) Math.floor(xd);
        int y = (int) Math.floor(yd);
        int z = (int) Math.floor(zd);
        int xc = (int) Math.ceil(xd);
        int yc = (int) Math.ceil(yd);
        int zc = (int) Math.ceil(zd);

        if (x < 0 || x > volume.getDimX() || y < 0 || y > volume.getDimY()
                || z < 0 || z > volume.getDimZ() || xc < 0
                || xc > volume.getDimX() || yc < 0 || yc > volume.getDimY()
                || zc < 0 || zc > volume.getDimZ()) {
            return 0;
        }
//        return 
        short xyz = volume.getVoxel(x, y, z);
        short xcyz = volume.getVoxel(xc, y, z);
        short xycz = volume.getVoxel(x, yc, z);
        short xyzc = volume.getVoxel(x, y, zc);
        short xcycz = volume.getVoxel(xc, yc, z);
        short xyczc = volume.getVoxel(x, yc, zc);
        short xcyzc = volume.getVoxel(xc, y, zc);
        short xcyczc = volume.getVoxel(xc, yc, zc);

        return (short) getLinearInterpolation(
                getLinearInterpolation(
                        getLinearInterpolation(xyz, xcyz, x, xc, xd),
                        getLinearInterpolation(xycz, xcycz, x, xc, xd), y, yc, yd),
                getLinearInterpolation(
                        getLinearInterpolation(xyzc, xcyzc, x, xc, xd),
                        getLinearInterpolation(xyczc, xcyczc, x, xc, xd), y, yc, yd), z, zc, zd);
    }

    double getLinearInterpolation(double d1, double d2, int x1, int x2, double x) {
        return d1 + (x - x1) * (d1 - d2) / (x1 - x2);
    }

    void slicer(double[] viewMatrix) {
        // clear image
        for (int j = 0; j < imageSize; j++) {
            for (int i = 0; i < imageSize; i++) {
                image.setRGB(i, j, 0);
            }
        }

        // perpendicular to the view vector viewVec
        double[] viewVec = new double[3];
        double[] uVec = new double[3];
        double[] vVec = new double[3];
        VectorMath.setVector(uVec, viewMatrix[0], viewMatrix[4], viewMatrix[8]);
        VectorMath.setVector(vVec, viewMatrix[1], viewMatrix[5], viewMatrix[9]);
        VectorMath.setVector(viewVec, viewMatrix[2], viewMatrix[6], viewMatrix[10]);

        // image is square
        int imageCenter = imageSize / 2;

        double[] pixelCoord = new double[3];
        double[] volumeCenter = new double[3];
        VectorMath.setVector(volumeCenter, volume.getDimX() / 2, volume.getDimY() / 2, volume.getDimZ() / 2);

        // sample on a plane through the origin of the volume data
        double max = volume.getMaximum();
        TFColor voxelColor = new TFColor();

        for (int j = 0; j < imageSize; j += granularity) {
            for (int i = 0; i < imageSize; i += granularity) {

                pixelCoord[0] = uVec[0] * (i - imageCenter) + vVec[0] * (j - imageCenter)
                        + volumeCenter[0];
                pixelCoord[1] = uVec[1] * (i - imageCenter) + vVec[1] * (j - imageCenter)
                        + volumeCenter[1];
                pixelCoord[2] = uVec[2] * (i - imageCenter) + vVec[2] * (j - imageCenter)
                        + volumeCenter[2];
//                int val = getVoxel(pixelCoord);
                int val = getTriVoxel(pixelCoord);

                // Map the intensity to a grey value by linear scaling
                voxelColor.r = val / max;
                voxelColor.g = voxelColor.r;
                voxelColor.b = voxelColor.r;
                voxelColor.a = val > 0 ? 1.0 : 0.0;  // this makes intensity 0 completely transparent and the rest opaque
                // Alternatively, apply the transfer function to obtain a color
//                 voxelColor = tFunc.getColor(val);
                setRGB2Image(voxelColor, i, j, granularity);
            }
        }
    }

    void setRGB2Image(TFColor voxelColor, int i, int j, int granularity) { //
//        BufferedImage expects a pixel color packed as ARGB in an int 
        int c_alpha = voxelColor.a <= 1.0 ? (int) Math.floor(voxelColor.a * 255) : 255;
        int c_red = voxelColor.r <= 1.0 ? (int) Math.floor(voxelColor.r * 255) : 255;
        int c_green = voxelColor.g <= 1.0 ? (int) Math.floor(voxelColor.g * 255)
                : 255;
        int c_blue = voxelColor.b <= 1.0 ? (int) Math.floor(voxelColor.b
                * 255) : 255;
        int pixelColor = (c_alpha << 24) | (c_red << 16) | (c_green
                << 8) | c_blue;
        for (int m = 0; m < granularity; m++) {
            for (int n = 0; n < granularity; n++) {
                if (n + i >= imageSize || n + i
                        < 0
                        || m + j >= imageSize || m + j < 0) {
                } else {
                    image.setRGB(n + i, m + j,
                            pixelColor);
                }
            }
        }
    }

    /*
    
     private int getBlue(int c) {
     return c & 0x000000ff;
     }

     private int getAlpha(int c) {
     return c >> 24;
     }

     private int getRed(int c) {
     return (c & 0x00ff0000) >> 16;
     }

     private int getGreen(int c) {
     return (c & 0x0000ff00) >> 8;
     }
    
     int[] buffer;
     int lastPixelColor = 0;
     void setRGB2Image(TFColor voxelColor, int i, int j, int granularity) {
     // BufferedImage expects a pixel color packed as ARGB in an int
     int c_alpha = voxelColor.a <= 1.0 ? (int) Math.floor(voxelColor.a * 255) : 255;
     int c_red = voxelColor.r <= 1.0 ? (int) Math.floor(voxelColor.r * 255) : 255;
     int c_green = voxelColor.g <= 1.0 ? (int) Math.floor(voxelColor.g * 255) : 255;
     int c_blue = voxelColor.b <= 1.0 ? (int) Math.floor(voxelColor.b * 255) : 255;
     int pixelColor = (c_alpha << 24) | (c_red << 16) | (c_green << 8) | c_blue;
     if (granularity == 1) {//setRGB directly
     image.setRGB(i, j, pixelColor);
     } else {
     if (j == 0 || i == 0) {//the first row, or first column in each row
     image.setRGB(i, j, pixelColor);
     } else {
     for (int m = 0; m < granularity; m++) {
     for (int n = 0; n < granularity; n++) {
     if (n + i >= imageSize || n + i < 0
     || m + j >= imageSize || m + j < 0) {
     } else {
     //                            image.setRGB(n + i, m + j, pixelColor);
     //left top
     int i_lt = i - granularity;
     int j_lt = j - granularity;
     int c_lt = buffer[i / granularity - 1];
     //right top
     int i_rt = i;
     int j_rt = j - granularity;
     int c_rt = buffer[i / granularity];
     //left bottom
     int i_lb = i - granularity;
     int j_lb = j;
     int c_lb = lastPixelColor;
     //right bottom
     int i_rb = i;
     int j_rb = j;
     int c_rb = pixelColor;

     double result = ((int) getLinearInterpolation(
     getLinearInterpolation(getAlpha(c_lt), getAlpha(c_lb), j_lt, j_lb, j_lt + m),
     getLinearInterpolation(getAlpha(c_rt), getAlpha(c_rb), j_rt, j_rb, j_lt + m),
     i_lt, i_rt, i_lt + n) >> 24)
     | ((int) getLinearInterpolation(
     getLinearInterpolation(getRed(c_lt), getRed(c_lb), j_lt, j_lb, j_lt + m),
     getLinearInterpolation(getRed(c_rt), getRed(c_rb), j_rt, j_rb, j_lt + m),
     i_lt, i_rt, i_lt + n) >> 16)
     | ((int) getLinearInterpolation(
     getLinearInterpolation(getGreen(c_lt), getGreen(c_lb), j_lt, j_lb, j_lt + m),
     getLinearInterpolation(getGreen(c_rt), getGreen(c_rb), j_rt, j_rb, j_lt + m),
     i_lt, i_rt, i_lt + n) >> 8)
     | (int) getLinearInterpolation(
     getLinearInterpolation(getBlue(c_lt), getBlue(c_lb), j_lt, j_lb, j_lt + m),
     getLinearInterpolation(getBlue(c_rt), getBlue(c_rb), j_rt, j_rb, j_lt + m),
     i_lt, i_rt, i_lt + n);
     image.setRGB(i - n, j - m,
     (int) result);
     if (i + granularity > imageSize) {//reaches the last column
     buffer[i / granularity] = pixelColor;
     }
     }
     }
     }
     }
     if (i > 0) {
     //record 
     buffer[i / granularity - 1] = lastPixelColor;
     //record the last pixel
     lastPixelColor = pixelColor;
     }
     }
     }
     */
    void MIP(double[] viewMatrix) {
        // clear image
        for (int j = 0; j < imageSize; j++) {
            for (int i = 0; i < imageSize; i++) {
                image.setRGB(i, j, 0);
            }
        }

        // perpendicular to the view vector viewVec
        double[] viewVec = new double[3];
        double[] uVec = new double[3];
        double[] vVec = new double[3];
        VectorMath.setVector(uVec, viewMatrix[0], viewMatrix[4], viewMatrix[8]);
        VectorMath.setVector(vVec, viewMatrix[1], viewMatrix[5], viewMatrix[9]);
        VectorMath.setVector(viewVec, viewMatrix[2], viewMatrix[6], viewMatrix[10]);

        // image is square
        int imageCenter = imageSize / 2;

        double[] pixelCoord = new double[3];
        double[] volumeCenter = new double[3];
        VectorMath.setVector(volumeCenter, volume.getDimX() / 2, volume.getDimY() / 2, volume.getDimZ() / 2);

        // sample on a plane through the origin of the volume data
        double max = volume.getMaximum();
        TFColor voxelColor = new TFColor();

        int limit = (int) Math.sqrt(Math.pow(volume.getDimX() * viewVec[0], 2)
                + Math.pow(volume.getDimY() * viewVec[1], 2)
                + Math.pow(volume.getDimZ() * viewVec[2], 2));
        for (int j = 0; j < imageSize; j += granularity) {
            for (int i = 0; i < imageSize; i += granularity) {
                int val = 0;
                for (int loop = (int) (limit / 2); loop > -limit / 2; loop--) {
                    pixelCoord[0] = uVec[0] * (i - imageCenter) + vVec[0] * (j - imageCenter)
                            + volumeCenter[0] + loop * viewVec[0];
                    pixelCoord[1] = uVec[1] * (i - imageCenter) + vVec[1] * (j - imageCenter)
                            + volumeCenter[1] + loop * viewVec[1];
                    pixelCoord[2] = uVec[2] * (i - imageCenter) + vVec[2] * (j - imageCenter)
                            + volumeCenter[2] + loop * viewVec[2];
//                    val = Math.max(val, getVoxel(pixelCoord));
                    val = Math.max(val, getTriVoxel(pixelCoord));

                }

                // Map the intensity to a grey value by linear scaling
                voxelColor.r = val / max;
                voxelColor.g = voxelColor.r;
                voxelColor.b = voxelColor.r;
                voxelColor.a = val > 0 ? 1.0 : 0.0;  // this makes intensity 0 completely transparent and the rest opaque
                // Alternatively, apply the transfer function to obtain a color
//                 voxelColor = tFunc.getColor(val);

                setRGB2Image(voxelColor, i, j, granularity);
            }
        }
    }

    TFColor calColor(TFColor val, TFColor old) {
        TFColor result = new TFColor();
        result.a = old.a * (1 - val.a);
        result.r = old.r * (1 - val.a) + val.a * val.r;
        result.g = old.g * (1 - val.a) + val.a * val.g;
        result.b = old.b * (1 - val.a) + val.a * val.b;
        return result;
    }

    void compositing(double[] viewMatrix) {
        // clear image
        for (int j = 0; j < imageSize; j++) {
            for (int i = 0; i < imageSize; i++) {
                image.setRGB(i, j, 0);
            }
        }

        // perpendicular to the view vector viewVec
        double[] viewVec = new double[3];
        double[] uVec = new double[3];
        double[] vVec = new double[3];
        VectorMath.setVector(uVec, viewMatrix[0], viewMatrix[4], viewMatrix[8]);
        VectorMath.setVector(vVec, viewMatrix[1], viewMatrix[5], viewMatrix[9]);
        VectorMath.setVector(viewVec, viewMatrix[2], viewMatrix[6], viewMatrix[10]);

        // image is square
        int imageCenter = imageSize / 2;

        double[] pixelCoord = new double[3];
        double[] volumeCenter = new double[3];
        VectorMath.setVector(volumeCenter, volume.getDimX() / 2, volume.getDimY() / 2, volume.getDimZ() / 2);

        // sample on a plane through the origin of the volume data
//        double max = volume.getMaximum();
        TFColor voxelColor;

        int limit = (int) Math.sqrt(Math.pow(volume.getDimX() * viewVec[0], 2)
                + Math.pow(volume.getDimY() * viewVec[1], 2)
                + Math.pow(volume.getDimZ() * viewVec[2], 2));
        for (int j = 0; j < imageSize; j += granularity) {
            for (int i = 0; i < imageSize; i += granularity) {
                voxelColor = new TFColor(0, 0, 0, 1);
//                for (int loop_i = limit / 2; loop_i > -limit / 2; loop_i--) {
                for (int loop_i = -limit / 2; loop_i < limit / 2; loop_i++) {
                    pixelCoord[0] = uVec[0] * (i - imageCenter) + vVec[0] * (j - imageCenter)
                            + volumeCenter[0] + loop_i * viewVec[0];
                    pixelCoord[1] = uVec[1] * (i - imageCenter) + vVec[1] * (j - imageCenter)
                            + volumeCenter[1] + loop_i * viewVec[1];
                    pixelCoord[2] = uVec[2] * (i - imageCenter) + vVec[2] * (j - imageCenter)
                            + volumeCenter[2] + loop_i * viewVec[2];
                    /*
                     double transparentRate = 1;
                     double[] pixelCoord_j = new double[3];
                     for (int loop_j = loop_i; loop_j > -limit / 2; loop_j--) {
                     //                    for (int loop_j = loop_i; loop_j < limit / 2; loop_j++) {
                     pixelCoord_j[0] = uVec[0] * (i - imageCenter) + vVec[0] * (j - imageCenter)
                     + volumeCenter[0] + loop_j * viewVec[0];
                     pixelCoord_j[1] = uVec[1] * (i - imageCenter) + vVec[1] * (j - imageCenter)
                     + volumeCenter[1] + loop_j * viewVec[1];
                     pixelCoord_j[2] = uVec[2] * (i - imageCenter) + vVec[2] * (j - imageCenter)
                     + volumeCenter[2] + loop_j * viewVec[2];

                     TFColor transparentColor = tFunc.getColor(getVoxel(pixelCoord_j));

                     transparentRate = transparentRate * (1 - transparentColor.a);

                     }
                     TFColor tempColor = tFunc.getColor(getVoxel(pixelCoord));
                     voxelColor.r += tempColor.r * transparentRate;
                     voxelColor.g += tempColor.g * transparentRate;
                     voxelColor.b += tempColor.b * transparentRate;
                     voxelColor.a += tempColor.a * transparentRate;
                     
                     * */
                    voxelColor = calColor(tFunc.getColor(getTriVoxel(pixelCoord)), voxelColor);
//                    voxelColor = calColor(tFunc.getColor(getVoxel(pixelCoord)), voxelColor);
                }
                voxelColor.a = 1 - voxelColor.a;

                // Map the intensity to a grey value by linear scaling
//                voxelColor.r = val / max;
//                voxelColor.g = voxelColor.r;
//                voxelColor.b = voxelColor.r;
//                voxelColor.a = val > 0 ? 1.0 : 0.0;  // this makes intensity 0 completely transparent and the rest opaque
                // Alternatively, apply the transfer function to obtain a color
//                 voxelColor = tFunc.getColor(val);
                setRGB2Image(voxelColor, i, j, granularity);
            }
        }
    }

    void twoDimFunction(double[] viewMatrix) {
        TransferFunction2DEditor.TriangleWidget triWidget = this.getTF2DPanel().triangleWidget;
        double r = triWidget.radius;
        short fv = triWidget.baseIntensity;
        TFColor widgetColor = triWidget.color;
        
        double graMax=triWidget.graMax;
        double graMin=triWidget.graMin;
//        System.out.println(graMax+","+graMin);
        
//        System.out.println("r="+r+",fv="+fv+",av="+av);
//        short av=triWidget.
        // clear image
        for (int j = 0; j < imageSize; j++) {
            for (int i = 0; i < imageSize; i++) {
                image.setRGB(i, j, 0);
            }
        }

        // perpendicular to the view vector viewVec
        double[] viewVec = new double[3];
        double[] uVec = new double[3];
        double[] vVec = new double[3];
        VectorMath.setVector(uVec, viewMatrix[0], viewMatrix[4], viewMatrix[8]);
        VectorMath.setVector(vVec, viewMatrix[1], viewMatrix[5], viewMatrix[9]);
        VectorMath.setVector(viewVec, viewMatrix[2], viewMatrix[6], viewMatrix[10]);

        // image is square
        int imageCenter = imageSize / 2;

        double[] pixelCoord = new double[3];
        double[] volumeCenter = new double[3];
        VectorMath.setVector(volumeCenter, volume.getDimX() / 2, volume.getDimY() / 2, volume.getDimZ() / 2);

        // sample on a plane through the origin of the volume data
//        double max = volume.getMaximum();
        TFColor voxelColor;

        int limit = (int) Math.sqrt(Math.pow(volume.getDimX() * viewVec[0], 2)
                + Math.pow(volume.getDimY() * viewVec[1], 2)
                + Math.pow(volume.getDimZ() * viewVec[2], 2));
        for (int j = 0; j < imageSize; j += granularity) {
            for (int i = 0; i < imageSize; i += granularity) {
                voxelColor = new TFColor(0, 0, 0, 1);
                float tempMag=0;
                double[] surfaceCoord = new double[3];
//                for (int loop_i = limit / 2; loop_i > -limit / 2; loop_i--) {
                for (int loop_i = -limit / 2; loop_i < limit / 2; loop_i++) {
                    pixelCoord[0] = uVec[0] * (i - imageCenter) + vVec[0] * (j - imageCenter)
                            + volumeCenter[0] + loop_i * viewVec[0];
                    pixelCoord[1] = uVec[1] * (i - imageCenter) + vVec[1] * (j - imageCenter)
                            + volumeCenter[1] + loop_i * viewVec[1];
                    pixelCoord[2] = uVec[2] * (i - imageCenter) + vVec[2] * (j - imageCenter)
                            + volumeCenter[2] + loop_i * viewVec[2];

                    VoxelGradient gra = getGradient(pixelCoord);
                    if(gra.mag>graMax||gra.mag<graMin){
                        continue;
                    }
//                    voxelColor = cal2dColor(voxelColor, widgetColor, getTriVoxel(pixelCoord), fv, r, (short) gra.mag);
                    voxelColor = cal2dColor(voxelColor, widgetColor, getVoxel(pixelCoord), fv, r, (short) gra.mag);
                    if(shading){
                        //shading, find the point with the max gradient with dotproducts being positive
                        double dotProducts = viewVec[0] * gra.x + viewVec[1] * gra.y + viewVec[2] * gra.z;
                        if (dotProducts > 0 && gra.mag > tempMag) {
                            tempMag = gra.mag;
                            surfaceCoord[0] = pixelCoord[0];
                            surfaceCoord[1] = pixelCoord[1];
                            surfaceCoord[2] = pixelCoord[2];
                        }
                    }
                    
                }
                voxelColor.a = 1 - voxelColor.a;
                
                /* kambient = 0.1, kdiff = 0.7, kspec = 0.2, and α = 10*/
                if(shading) {
                    short vox = getVoxel(surfaceCoord);
                    VoxelGradient gradient=this.getGradient(surfaceCoord);
//                    TFColor color = tFunc.getColor(vox);
                    double LN = viewVec[0] * gradient.x / tempMag
                            + viewVec[1] * gradient.y / tempMag
                            + viewVec[2] * gradient.z/ tempMag;
                    voxelColor.r = kAmbient * light.r
                            + widgetColor.r * kDiff * LN
                            + kSpec * Math.pow(LN, a);
                    voxelColor.g = kAmbient * light.g
                            + widgetColor.g * kDiff * LN
                            + kSpec * Math.pow(LN, a);
                    voxelColor.b = kAmbient * light.b
                            + widgetColor.b * kDiff * LN
                            + kSpec * Math.pow(LN, a);
//                    logger.debug("i,j:"+i+","+j+","+voxelColor+",viewVec:"+viewVec[0]+","+viewVec[1]+","+viewVec[2]+",surfaceCoord:"+surfaceCoord[0]+","+surfaceCoord[1]+","+surfaceCoord[2]+",LN:"+LN);
                }

                // Map the intensity to a grey value by linear scaling
//                voxelColor.r = val / max;
//                voxelColor.g = voxelColor.r;
//                voxelColor.b = voxelColor.r;
//                voxelColor.a = val > 0 ? 1.0 : 0.0;  // this makes intensity 0 completely transparent and the rest opaque
                // Alternatively, apply the transfer function to obtain a color
//                 voxelColor = tFunc.getColor(val);
                setRGB2Image(voxelColor, i, j, granularity);
            }
        }
    }

    TFColor cal2dColor(TFColor old, TFColor selected, short fxi, short fv, double r, short dfxi) {
        TFColor result = new TFColor();
        TFColor val = tFunc.getColor(fxi);
        
        if (dfxi == 0 && fxi == fv) {
            val.a = selected.a;
        } else if (dfxi > 0 && fv >= (fxi - r * dfxi) && fv <= (fxi + r * dfxi)) {
            val.a = selected.a * (1 - (fv - fxi) / (r * dfxi));
        } else {
            val.a = 0;
        }
        result.a = old.a * (1 - val.a);
        result.r = selected.r * (1 - val.a) + val.a * val.r;
        result.g = selected.g * (1 - val.a) + val.a * val.g;
        result.b = selected.b * (1 - val.a) + val.a * val.b;
        return result;
    }

    private void drawBoundingBox(GL2 gl) {
        gl.glPushAttrib(GL2.GL_CURRENT_BIT);
        gl.glDisable(GL2.GL_LIGHTING);
        gl.glColor4d(1.0, 1.0, 1.0, 1.0);
        gl.glLineWidth(1.5f);
        gl.glEnable(GL.GL_LINE_SMOOTH);
        gl.glHint(GL.GL_LINE_SMOOTH_HINT, GL.GL_NICEST);
        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

        gl.glBegin(GL.GL_LINE_LOOP);
        gl.glVertex3d(-volume.getDimX() / 2.0, -volume.getDimY() / 2.0, volume.getDimZ() / 2.0);
        gl.glVertex3d(-volume.getDimX() / 2.0, volume.getDimY() / 2.0, volume.getDimZ() / 2.0);
        gl.glVertex3d(volume.getDimX() / 2.0, volume.getDimY() / 2.0, volume.getDimZ() / 2.0);
        gl.glVertex3d(volume.getDimX() / 2.0, -volume.getDimY() / 2.0, volume.getDimZ() / 2.0);
        gl.glEnd();

        gl.glBegin(GL.GL_LINE_LOOP);
        gl.glVertex3d(-volume.getDimX() / 2.0, -volume.getDimY() / 2.0, -volume.getDimZ() / 2.0);
        gl.glVertex3d(-volume.getDimX() / 2.0, volume.getDimY() / 2.0, -volume.getDimZ() / 2.0);
        gl.glVertex3d(volume.getDimX() / 2.0, volume.getDimY() / 2.0, -volume.getDimZ() / 2.0);
        gl.glVertex3d(volume.getDimX() / 2.0, -volume.getDimY() / 2.0, -volume.getDimZ() / 2.0);
        gl.glEnd();

        gl.glBegin(GL.GL_LINE_LOOP);
        gl.glVertex3d(volume.getDimX() / 2.0, -volume.getDimY() / 2.0, -volume.getDimZ() / 2.0);
        gl.glVertex3d(volume.getDimX() / 2.0, -volume.getDimY() / 2.0, volume.getDimZ() / 2.0);
        gl.glVertex3d(volume.getDimX() / 2.0, volume.getDimY() / 2.0, volume.getDimZ() / 2.0);
        gl.glVertex3d(volume.getDimX() / 2.0, volume.getDimY() / 2.0, -volume.getDimZ() / 2.0);
        gl.glEnd();

        gl.glBegin(GL.GL_LINE_LOOP);
        gl.glVertex3d(-volume.getDimX() / 2.0, -volume.getDimY() / 2.0, -volume.getDimZ() / 2.0);
        gl.glVertex3d(-volume.getDimX() / 2.0, -volume.getDimY() / 2.0, volume.getDimZ() / 2.0);
        gl.glVertex3d(-volume.getDimX() / 2.0, volume.getDimY() / 2.0, volume.getDimZ() / 2.0);
        gl.glVertex3d(-volume.getDimX() / 2.0, volume.getDimY() / 2.0, -volume.getDimZ() / 2.0);
        gl.glEnd();

        gl.glBegin(GL.GL_LINE_LOOP);
        gl.glVertex3d(-volume.getDimX() / 2.0, volume.getDimY() / 2.0, -volume.getDimZ() / 2.0);
        gl.glVertex3d(-volume.getDimX() / 2.0, volume.getDimY() / 2.0, volume.getDimZ() / 2.0);
        gl.glVertex3d(volume.getDimX() / 2.0, volume.getDimY() / 2.0, volume.getDimZ() / 2.0);
        gl.glVertex3d(volume.getDimX() / 2.0, volume.getDimY() / 2.0, -volume.getDimZ() / 2.0);
        gl.glEnd();

        gl.glBegin(GL.GL_LINE_LOOP);
        gl.glVertex3d(-volume.getDimX() / 2.0, -volume.getDimY() / 2.0, -volume.getDimZ() / 2.0);
        gl.glVertex3d(-volume.getDimX() / 2.0, -volume.getDimY() / 2.0, volume.getDimZ() / 2.0);
        gl.glVertex3d(volume.getDimX() / 2.0, -volume.getDimY() / 2.0, volume.getDimZ() / 2.0);
        gl.glVertex3d(volume.getDimX() / 2.0, -volume.getDimY() / 2.0, -volume.getDimZ() / 2.0);
        gl.glEnd();

        gl.glDisable(GL.GL_LINE_SMOOTH);
        gl.glDisable(GL.GL_BLEND);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glPopAttrib();
    }
    public static final int FUNCTION_2DFUNC = 4;
    public static final int FUNCTION_COMPOSITING = 3;
    public static final int FUNCTION_MIP = 2;
    public static final int FUNCTION_SLICER = 1;

    private int function;

    public void setFunction(int function) {
        this.function = function;
    }

    @Override
    public void visualize(GL2 gl) {

        if (volume == null) {
            return;
        }

        drawBoundingBox(gl);

        gl.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, viewMatrix, 0);

        long startTime = System.currentTimeMillis();

        switch (function) {
            case FUNCTION_SLICER:
                slicer(viewMatrix);
                break;
            case FUNCTION_MIP:
                MIP(viewMatrix);
                break;
            case FUNCTION_COMPOSITING:
                compositing(viewMatrix);
                break;
            case FUNCTION_2DFUNC:
                twoDimFunction(viewMatrix);
                break;
            case 5:
                break;
            default:
                slicer(viewMatrix);
                break;
        }

        long endTime = System.currentTimeMillis();
        double runningTime = (endTime - startTime);
        panel.setSpeedLabel(Double.toString(runningTime));

        Texture texture = AWTTextureIO.newTexture(gl.getGLProfile(), image, false);

        gl.glPushAttrib(GL2.GL_LIGHTING_BIT);
        gl.glDisable(GL2.GL_LIGHTING);
        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

        // draw rendered image as a billboard texture
        texture.enable(gl);
        texture.bind(gl);
        double halfWidth = imageSize / 2.0;
        gl.glPushMatrix();
        gl.glLoadIdentity();
        gl.glBegin(GL2.GL_QUADS);
        gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glTexCoord2d(0.0, 0.0);
        gl.glVertex3d(-halfWidth, -halfWidth, 0.0);
        gl.glTexCoord2d(0.0, 1.0);
        gl.glVertex3d(-halfWidth, halfWidth, 0.0);
        gl.glTexCoord2d(1.0, 1.0);
        gl.glVertex3d(halfWidth, halfWidth, 0.0);
        gl.glTexCoord2d(1.0, 0.0);
        gl.glVertex3d(halfWidth, -halfWidth, 0.0);
        gl.glEnd();
        texture.disable(gl);
        texture.destroy(gl);
        gl.glPopMatrix();

        gl.glPopAttrib();

        if (gl.glGetError() > 0) {
            System.out.println("some OpenGL error: " + gl.glGetError());
        }

    }
    private BufferedImage image;
    private double[] viewMatrix = new double[4 * 4];

    @Override
    public void changed() {
        for (TFChangeListener listener : listeners) {
            listener.changed();
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package volume;

/**
 *
 * @author michel
 */
public class GradientVolume {

    public GradientVolume(Volume vol) {
        volume = vol;
        dimX = vol.getDimX();
        dimY = vol.getDimY();
        dimZ = vol.getDimZ();
        data = new VoxelGradient[dimX * dimY * dimZ];
        compute();
        maxmag = -1.0;
    }

    public VoxelGradient getGradient(int x, int y, int z) {
        return data[x + dimX * (y + dimY * z)];
    }

    public void setGradient(int x, int y, int z, VoxelGradient value) {
        data[x + dimX * (y + dimY * z)] = value;
    }

    public void setVoxel(int i, VoxelGradient value) {
        data[i] = value;
    }

    public VoxelGradient getVoxel(int i) {
        return data[i];
    }

    public int getDimX() {
        return dimX;
    }

    public int getDimY() {
        return dimY;
    }

    public int getDimZ() {
        return dimZ;
    }

    private void compute() {
        int x, xp, y, z = 0;
        // this just initializes all gradients to the vector (0,0,0)
        for (int i = 0; i < data.length; i++) {
//            data[i] = zero;
            x = i % dimX;
            xp = i / dimX;
            y = xp % dimY;
            z = xp / dimY;
            
            data[i] = new VoxelGradient(
                    (volume.getVoxel(x + 1, y, z) - volume.getVoxel(x - 1, y, z)) / 2,
                    (volume.getVoxel(x, y + 1, z) - volume.getVoxel(x, y - 1, z)) / 2,
                    (volume.getVoxel(x, y, z + 1) - volume.getVoxel(x, y, z - 1)) / 2
            );
        }

    }

    public double getMaxGradientMagnitude() {
        if (maxmag >= 0) {
            return maxmag;
        } else {
            double magnitude = data[0].mag;
            for (int i = 0; i < data.length; i++) {
                magnitude = data[i].mag > magnitude ? data[i].mag : magnitude;
            }
            maxmag = magnitude;
            return magnitude;
        }
    }

    private int dimX, dimY, dimZ;
    private VoxelGradient zero = new VoxelGradient();
    VoxelGradient[] data;
    Volume volume;
    double maxmag;

    public static void main(String[] args) {
        int x, y, z, xp, dimX, dimY, dimZ = 0;

        int i = 44059;

        dimX = 30;
        dimY = 50;
        dimZ = 20;

        x = i % dimX;
        xp = i / dimX;
        y = xp % dimY;
        z = xp / dimY;

        System.out.println(x + "," + y + "," + z);

        System.out.println(x + dimX * (y + dimY * z));

    }
}

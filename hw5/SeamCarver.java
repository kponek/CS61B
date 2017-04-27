/**
 * Created by kevin on 4/26/2017.
 */
public class SeamCarver {
    private Picture pic;
    double[][] energies;
    int[][] dist;

    public SeamCarver(Picture picture) {
        pic = picture;
        energies = new double[width()][height()];
    }

    // current picture
    public Picture picture() {
        return pic;
    }

    // width of current picture
    public int width() {
        return pic.width();
    }

    // height of current picture
    public int height() {
        return pic.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height()) {
            throw new IndexOutOfBoundsException();
        }
        int addX = x + 1;
        int subX = x + 1;
        int addY = y + 1;
        int subY = y - 1;
        if (addX == width()) {
            addX = 0;
        }
        if (addY == height()) {
            addY = 0;
        }
        if (subX < 0) {
            subX = width() - 1;
        }
        if (subY < 0) {
            subY = height() - 1;
        }
        int rx = pic.get(addX, y).getRed() - pic.get(subX, y).getRed();
        int gx = pic.get(addX, y).getGreen() - pic.get(subX, y).getGreen();
        int bx = pic.get(addX, y).getBlue() - pic.get(subX, y).getBlue();
        int ry = pic.get(x, addY).getRed() - pic.get(x, subY).getRed();
        int gy = pic.get(x, addY).getGreen() - pic.get(x, subY).getGreen();
        int by = pic.get(x, addY).getBlue() - pic.get(x, subY).getBlue();
        return rx * rx + gx * gx + bx * bx + ry * ry + gy * gy + by * by;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transpose();
        int[] horizSeam = findVerticalSeam();
        transpose();
        return horizSeam;
    }

    private void transpose() {
        Picture transPic = new Picture(height(), width());
        double[][] transEnergies = new double[height()][width()];
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                transPic.set(j, i, pic.get(i, j));
                transEnergies[j][i] = energies[i][j];
            }
        }
        pic = transPic;
        energies = transEnergies;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int[] seam = new int[height()];
        energies = new double[width()][height()];
        dist = new int[width()][height()];
        double minEnergy = Double.MAX_VALUE;
        int minEnergyDist = -1;
        //set all values to infinity (max val)
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                energies[i][j] = Double.MAX_VALUE;
            }
        }
        //set first row to be max energies
        for (int i = 0; i < width(); i++) {
            energies[i][0] = 195075.0;
        }
        //relax
        for (int j = 0; j < height() - 1; j++) {
            for (int i = 0; i < width(); i++) {
                if (i > 0) {
                    findDist(i, j, i - 1, j + 1);
                }

                findDist(i, j, i, j + 1);

                if (i < width() - 1) {
                    findDist(i, j, i + 1, j + 1);
                }
            }
        }
        //find which path was shortest iteratively
        for (int i = 0; i < width(); i++) {
            if (energies[i][height() - 1] < minEnergy) {
                minEnergyDist = i;
                minEnergy = energies[i][height() - 1];
            }
        }
        //assert minEnergyDist != -1;

        seam[height() - 1] = minEnergyDist;
        int prevDist = dist[minEnergyDist][height() - 1];

        for (int j = height() - 2; j >= 0; j--) {
            seam[j] = prevDist;
            prevDist = dist[prevDist][j];
        }

        return seam;

    }

    // remove horizontal seam from picture
    public void removeHorizontalSeam(int[] seam) {

    }

    // remove vertical seam from picture
    public void removeVerticalSeam(int[] seam) {

    }

    private void calcEnergies() {
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                energies[i][j] = energy(i, j);
            }
        }
    }

    private void findDist(int x1, int y1, int x2, int y2) {
        if (energies[x2][y2] > energies[x1][y1] + energy(x2, y2)) {
            energies[x2][y2] = energies[x1][y1] + energy(x2, y2);
            dist[x1][y2] = x1;
        }
    }
}

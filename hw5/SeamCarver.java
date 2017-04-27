/**
 * Created by kevin on 4/26/2017.
 */

import java.awt.Color;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture pic;
    private double[][] energy;
    private double[][] energySums;

    public SeamCarver(Picture picture) {
        pic = new Picture(picture);
        energy = createEnergyArray(picture);

        energySums = calculateEnergySums(this.energy, picture.width(), picture.height());
    }

    public Picture picture() {
        return pic;
    }

    public int width() {
        return pic.width();
    }

    public int height() {
        return pic.height();
    }

    public double energy(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height()) {
            throw new IndexOutOfBoundsException();
        }
        int addX = x + 1;
        int subX = x - 1;
        int addY = y + 1;
        int subY = y - 1;
        //if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1) {
        //    return 195075.0;
        //}
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


    private double[][] createEnergyArray(Picture p) {
        int w = p.width();
        int h = p.height();
        double[][] energyArray = new double[h][w];
        for (int y = 0; y < h; y += 1) {
            for (int x = 0; x < w; x += 1) {
                energyArray[y][x] = energy(x, y);
            }
        }
        return energyArray;
    }

    private double[][] calculateEnergySums(double[][] e, int w, int h) {
        double[][] sum = new double[h][w];
        for (int y = 0; y < h; y += 1) {
            for (int x = 0; x < w; x += 1) {
                if (y == 0) {
                    sum[y][x] = e[y][x];
                } else {
                    double left = x - 1;
                    double middle = sum[y - 1][x] + e[y][x];
                    double right = x + 1;
                    if (left < 0) {
                        left = Double.MAX_VALUE;
                    } else {
                        left = sum[y - 1][x - 1] + e[y][x];
                    }
                    if (right >= width()) {
                        right = Double.MAX_VALUE;
                    } else {
                        right = sum[y - 1][x + 1] + e[y][x];
                    }
                    if (left < middle & left < right) {
                        sum[y][x] = left;
                    } else if (right < left && right < middle) {
                        sum[y][x] = right;
                    } else {
                        sum[y][x] = middle;
                    }
                }
            }
        }
        return sum;
    }

    private int[] findSeams(double[][] e, int w, int h) {
        double min = Double.POSITIVE_INFINITY;
        int[] sol = new int[h];
        for (int x = 0; x < w; x += 1) {
            if (e[h - 1][x] < min) {
                min = e[h - 1][x];
                sol[h - 1] = x;
            }
        }
        for (int y = h - 2; y >= 0; y -= 1) {
            int root = sol[y + 1];
            double left = root - 1;
            double middle = e[y][root];
            double right = root + 1;
            if (right >= w) {
                right = Double.POSITIVE_INFINITY;
            } else {
                right = e[y][root + 1];
            }
            if (left < 0) {
                left = Double.POSITIVE_INFINITY;
            } else {
                left = e[y][root - 1];
            }
            if (right < middle && right < left) {
                sol[y] = sol[y + 1] + 1;
            } else if (left < middle && left < right) {
                sol[y] = sol[y + 1] - 1;
            } else {
                sol[y] = sol[y + 1];
            }
        }
        return sol;
    }

    public int[] findVerticalSeam() {
        return findSeams(this.energySums, width(), height());
    }

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
                transEnergies[j][i] = energy[i][j];
            }
        }
        pic = transPic;
        energy = transEnergies;
    }

    public void removeHorizontalSeam(int[] seam) {

    }

    public void removeVerticalSeam(int[] seam) {

    }
}
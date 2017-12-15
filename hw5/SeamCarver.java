import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

public class SeamCarver {
    private Picture pic;
    private double[][] energy;

    public SeamCarver(Picture picture) {
        pic = picture;
        energy = new double[height()][width()];
        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < height(); row++) {
                energy[row][col] = energy(col, row);
            }
        }
    }

    /** Return current picture. */
    public Picture picture() {
        return pic;
    }

    /** Return width of current picture. */
    public int width() {
        return pic.width();
    }

    /** Return height of current picture. */
    public int height() {
        return pic.height();
    }

    /** Calculate energy of pixel at column x and row y. */
    public double energy(int x, int y) {
        if (x < 0 || y < 0 || x > width()-1 || y > height()-1) {
            throw new IndexOutOfBoundsException();
        }

        Color left, right, up, down;

        if (x == 0) {
            left = pic.get(width()-1, y);
        } else {
            left = pic.get(x-1, y);
        }

        if (x == width() - 1) {
            right = pic.get(0, y);
        } else {
            right = pic.get(x+1, y);
        }

        if (y == 0) {
            up = pic.get(x, height()-1);
        } else {
            up = pic.get(x, y-1);
        }

        if (y == height() - 1) {
            down = pic.get(x, 0);
        } else {
            down = pic.get(x, y+1);
        }

        int xRed = (left.getRed() - right.getRed());
        int xGreen = (left.getGreen() - right.getGreen());
        int xBlue = (left.getBlue() - right.getBlue());

        int yRed = (up.getRed() - down.getRed());
        int yGreen = (up.getGreen() - down.getGreen());
        int yBlue = (up.getBlue() - down.getBlue());

        int xSig = xRed * xRed + xGreen * xGreen + xBlue * xBlue;
        int ySig = yRed * yRed + yGreen * yGreen + yBlue * yBlue;

        return xSig + ySig;
    }

    /** Return a transposed matrix. */
    private static double[][] transposeMatrix(double [][] m) {
        double[][] trans = new double[m[0].length][m.length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                trans[j][i] = m[i][j];
            }
        }
        return trans;
    }

    /** Return sequence of indices for horizontal seam. */
    public int[] findHorizontalSeam() {
        energy = transposeMatrix(energy);
        int[] horSeam = findVerticalSeam();
        energy = transposeMatrix(energy);
        return horSeam;
    }


    /** Return sequence of indices for vertical seam. */
    public int[] findVerticalSeam() {
        int height = energy.length;
        int width = energy[0].length;

        int[] verSeam = new int[height];
        double minE = Double.MAX_VALUE;

        for (int row = 0; row < height; row++) {
            if (row == 0) {
                for (int col = 0; col < width; col++) {
                    double currE = energy[row][col];
                    if (currE < minE) {
                        minE = currE;
                        verSeam[0] = col;
                    }
                }
            } else {
                minE = Double.MAX_VALUE;
                int currCol = verSeam[row - 1];
                for (int col = currCol - 1; col <= currCol + 1; col++) {
                    double currE = energy[row][col];
                    if (currE < minE) {
                        minE = currE;
                        verSeam[row] = col;
                    }
                }
            }
        }

        return verSeam;
    }

    /** remove horizontal seam from picture. */
    public void removeHorizontalSeam(int[] seam) {
        if (seam.length != width()) {
            throw new IllegalArgumentException();
        }

        for (int i = 1; i < seam.length; i++) {
            if (Math.abs(seam[i] - seam[i-1]) > 1) {
                throw new IllegalArgumentException();
            }
        }
    }

    /** remove vertical seam from picture. */
    public void removeVerticalSeam(int[] seam) {
        if (seam.length != height()) {
            throw new IllegalArgumentException();
        }

        for (int i = 1; i < seam.length; i++) {
            if (Math.abs(seam[i] - seam[i-1]) > 1) {
                throw new IllegalArgumentException();
            }
        }
    }

}

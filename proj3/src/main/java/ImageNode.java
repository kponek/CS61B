/**
 * Created by idanhovav on 4/3/16.
 */
public class ImageNode {
    public static final int PICSIZE = 256;

    private String imgAddress;
    private int imgNumber;
    private int level;
    // upper left coordinates
    private double xPosn;
    private double yPosn;

    //latitude and longitude
    private double ullat;
    private double ullon;
    private double lrlat;
    private double lrlon;
    private double latDPP;
    private double lonDPP;
    private ImageNode[] children;

    public ImageNode(String imgNumber, double xPosn, double yPosn,
                     double ullat, double ullon, double lrlat, double lrlon) {
        if (imgNumber.equals("0")) {
            this.imgAddress = "img/root.png";
        } else {
            this.imgAddress = "img/" + imgNumber + ".png";
        }
        this.imgNumber = Integer.parseInt(imgNumber);
        this.level = imgNumber.length();
        this.xPosn = xPosn;
        this.yPosn = yPosn;
        this.ullon = ullon;
        this.ullat = ullat;
        this.lrlon = lrlon;
        this.lrlat = lrlat;
        this.latDPP = Math.abs(this.ullat - this.lrlat) / (PICSIZE);
        this.lonDPP = Math.abs(this.ullon - this.lrlon) / (PICSIZE);
        this.children = null;
    }
    public void setChildren(ImageNode[] children) {
        this.children = children;
    }
    public String getImgAddress() {
        return imgAddress;
    }

    public int getImgNumber() {
        return imgNumber;
    }

    public int getLevel() {
        return level;
    }

    public double getxPosn() {
        return xPosn;
    }

    public double getyPosn() {
        return yPosn;
    }

    public ImageNode[] getChildren() {
        return children;
    }

    public double getUllat() {
        return ullat;
    }

    public double getUllon() {
        return ullon;
    }

    public double getLrlat() {
        return lrlat;
    }

    public double getLrlon() {
        return lrlon;
    }

    public double getLatDPP() {
        return latDPP;
    }

    public double getLonDPP() {
        return lonDPP;
    }
}

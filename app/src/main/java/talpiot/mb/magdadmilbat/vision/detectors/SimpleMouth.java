package talpiot.mb.magdadmilbat.vision.detectors;

import com.google.mediapipe.formats.proto.LandmarkProto;

import talpiot.mb.magdadmilbat.vision.Point;
import talpiot.mb.magdadmilbat.vision.VisionMaster;

/**
 * Simplest implemetation of the IMouth interface. See IMouth for documentation
 */
public class SimpleMouth implements IMouth {

    private Point cornerRight, cornerLeft, top, bot, faceTop, faceBot;
    private static final double SCALER = 1000;
    private static final double MAX_SCORE  = 100;


    @Override
    public void updateMouthData(LandmarkProto.NormalizedLandmarkList face) {
        cornerLeft = new Point(face.getLandmark(61));
        cornerRight = new Point(face.getLandmark(291));
        top = new Point(face.getLandmark(0));
        bot = new Point(face.getLandmark(17));
        faceTop = new Point(face.getLandmark(10));
        faceBot = new Point(face.getLandmark(152));
    }

    @Override
    public double getWidthNormalizer() {
        return Point.dist(faceTop, faceBot);
    }

    @Override
    public double getHeightNormalizer() {
        return getWidthNormalizer();
    }

    @Override
    public double getWidth() {
        return Math.abs(Point.subtract(cornerRight, cornerLeft)
                .rotateToNormalize(Point.subtract(faceTop, faceBot))
                .getY());
    }

    @Override
    public double getHeight() {
        return Math.abs(Point.subtract(top, bot)
                .rotateToNormalize(Point.subtract(faceTop, faceBot))
                .getX());
    }

    @Override
    public double checkKiss() {
        double res = enforceSymmetry((getHeight() / getWidth()) * 100); //Success rates for a
        if(getHeight() > getWidth() + 10) //If the mouth is open unsymmetrical to the length of the mouth
            return res - 30; //Normalization result
        if(getHeight() < getWidth() / 2) //If the mouth is open unsymmetrical to the width of the mouth
            return res;
        if(res > MAX_SCORE)
            return MAX_SCORE;
        return res + 50;
    }

    @Override
    public double getSymmetryCoef() {
        return Math.abs(Point.subtract(top, bot)
                .rotateToNormalize(Point.subtract(faceTop, faceBot))
                .getY()) / getHeightNormalizer()
                + Math.abs(Point.subtract(cornerRight, cornerLeft)
                .rotateToNormalize(Point.subtract(faceTop, faceBot))
                .getX()) / getWidthNormalizer();
    }

    @Override
    public String toString() {
        return "SimpleMouth{" +
                "width=" + getWidth() +
                ", height=" + getHeight() +
                ", symm=" + getSymmetryCoef() +
                ", smile=" + getSmileScore() +
                ", openMouth=" + getBigMouthScore() +
                ", area=" + getArea() +
                ", kiss=" + checkKiss() +
                '}';
    }
}

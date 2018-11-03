package org.usfirst.frc.team4028.robot.auton.pathfollowing.PoseTracking;

import org.usfirst.frc.team4028.robot.auton.pathfollowing.util.maphs.matrix.Matrix;
import org.usfirst.frc.team4028.robot.auton.pathfollowing.util.Kinematics;

import org.usfirst.frc.team4028.robot.Constants;
import org.usfirst.frc.team4028.robot.auton.pathfollowing.motion.*;
import org.usfirst.frc.team4028.robot.subsystems.Chassis;


public class extendedChassisKallman extends extendedKallmanFilter {

    private static final double sigmaX0 = .01;
    private static final double sigmaY0 = .01;
    private static final double sigmaTheta0 = .01;
    private static final double sigmaVr0 = .0001;
    private static final double sigmaVl0 = .0001;
    private static final double kXX = 1;
    private static final double kYY = 1;
    private static final double kThetaTheta = 1;
    private static final double kVrVr = 1;
    private static final double kVlVl = 1;
    private static final double speedSensorErrorConstant = .01;
    private static final double gyroOmegaSensorErrorConstant = .001;
    private double[] diagOfUncertaintyR = {0,0,0,speedSensorErrorConstant, speedSensorErrorConstant};


    private static extendedChassisKallman _instance = new extendedChassisKallman();

	public static extendedChassisKallman getInstance() 
	{
		return _instance;
    }
    
	private extendedChassisKallman() {
    }

    public Matrix initNoiseDistributionJacobianG(){
        return Matrix.identity(5, 5);                
    }

    public int initDimensionality(){
        return 5;
    } 

    public Matrix initConstantPartOfMatrixQ(){
        double[] kVectorForQ = {kXX, kYY, kThetaTheta, kVrVr, kVlVl};
        return Matrix.diag(kVectorForQ);
    }

    public Matrix initMapStateToMeasurementJacobianH(){
        return  Matrix.identity(5, 5);
    }

    public Matrix initInstantaneousMeasurementUncertaintyMatrixR(){
        return Matrix.diag(this.diagOfUncertaintyR);        
    } 

    public Matrix initDiffEqJacobian(){
        return Matrix.identity(5, 5);
    }

    public Matrix initCovarianceMatrix(){
        double[] standardDeviationVector = {sigmaX0, sigmaY0, sigmaTheta0, sigmaVr0, sigmaVl0};
        return Matrix.diag(standardDeviationVector);
    }

    public Matrix initConfigVector(){
        return new Matrix(new double[][] {{0},{0},{0},{0},{0}});
    }

    public Matrix updateGfromX(Matrix x){
        return Matrix.oplus(Matrix.rotationMatrix(x.get(0,2)), Matrix.identity(3, 3));
    }

    public Matrix updateHfromX(Matrix x){
        return Matrix.identity(5,5);
    }


    public Matrix mapXtoZFunctionH(Matrix x){
        return x;
    }

    public Matrix updateFfromXandDT(Matrix x, double deltaT){

        //useful doubles
        // double curX = x.get(0,0); //this seems like good practice but technically it's useless
        // double curY = x.get(0,1); //this seems like good practice but technically it's useless
        double curTheta = x.get(0,2);
        double curThetaRadians = Math.toRadians(curTheta);
        double Vr = x.get(0, 3);
        double Vl = x.get(0,4);

        //partial Xdots
        double partialXDotPartialX = 0;
        double partialXDotPartialY = 0;
        double partialXDotPartialTheta =-1*(Vr+Vl)*Constants.TRACK_SCRUBBING_FACTOR*Math.sin(curThetaRadians)/2;
        double partialXDotPartialVr = .5*Math.cos(curThetaRadians)*Constants.TRACK_SCRUBBING_FACTOR;
        double partialXDotPartialVl = .5*Math.cos(curThetaRadians)*Constants.TRACK_SCRUBBING_FACTOR;

        //partial Ydots
        double partialYDotPartialX = 0;
        double partialYDotPartialY = 0;
        double partialYDotPartialTheta = (Vr+Vl)*Constants.TRACK_SCRUBBING_FACTOR*Math.cos(curThetaRadians)/2;
        double partialYDotPartialVr = .5*Constants.TRACK_SCRUBBING_FACTOR*Math.sin(curThetaRadians);
        double partialYDotPartialVl = .5*Constants.TRACK_SCRUBBING_FACTOR*Math.sin(curThetaRadians);
        
        
        //partial Omegas
        double partialThetaDotPartialX = 0;
        double partialThetaDotPartialY = 0;
        double partialThetaDotPartialTheta = 0;
        double partialThetaDotPartialVr = 1/(Constants.TRACK_WIDTH_INCHES*Constants.TRACK_SCRUBBING_FACTOR);
        double partialThetaDotPartialVl = -1/(Constants.TRACK_WIDTH_INCHES*Constants.TRACK_SCRUBBING_FACTOR);


        //partial Vrdots
        double partialVrDotPartialX = 0;
        double partialVrDotPartialY = 0;
        double partialVrDotPartialTheta = 0;
        double partialVrDotPartialVr = 0;
        double partialVrDotPartialVl = 0;


        //partial Vldots
        double partialVlDotPartialX = 0;
        double partialVlDotPartialY = 0;
        double partialVlDotPartialTheta = 0;
        double partialVlDotPartialVr = 0;
        double partialVlDotPartialVl = 0;


        //jacobian as array
        double[][] jacobianAsArray = {{partialXDotPartialX,       partialXDotPartialY,        partialXDotPartialTheta,        partialXDotPartialVr,       partialXDotPartialVl},
                                      {partialYDotPartialX,       partialYDotPartialY,        partialYDotPartialTheta,        partialYDotPartialVr,       partialYDotPartialVl},
                                      {partialThetaDotPartialX,   partialThetaDotPartialY,    partialThetaDotPartialTheta,    partialThetaDotPartialVr,   partialThetaDotPartialVl},
                                      {partialVrDotPartialX,      partialVrDotPartialY,       partialVrDotPartialTheta,       partialVrDotPartialVr,      partialVrDotPartialVl},
                                      {partialVlDotPartialX,      partialVlDotPartialY,       partialVlDotPartialTheta,       partialVlDotPartialVr,      partialVlDotPartialVl}};

        //return the jacobian
        return new Matrix(jacobianAsArray,5,5);
    }

    public Matrix updateRfromXandDT(Matrix x, double deltaT){
        double Vr = x.get(0,3);
        double Vl = x.get(0,4);
        double thetaInRads = Math.toRadians(x.get(0,2));
        double totSpeed = (Vr+Vl)/2;
        double Xspeed = totSpeed*Math.cos(thetaInRads);
        double Yspeed = totSpeed*Math.sin(thetaInRads);
        double dX = Xspeed*deltaT;
        double dXUncertainty = dX*speedSensorErrorConstant;
        this.diagOfUncertaintyR[0]+=dXUncertainty;
        double dY = Yspeed * deltaT;
        double dYUncertainty = dY*speedSensorErrorConstant;
        this.diagOfUncertaintyR[1]+=dYUncertainty;
        double dThetaUncertainty = gyroOmegaSensorErrorConstant*deltaT;
        this.diagOfUncertaintyR[2] += dThetaUncertainty;
        return Matrix.diag(this.diagOfUncertaintyR);
    }

    public Matrix dynamicsFunctionPhi(Matrix x, double deltaT){
        double curX = x.get(0,0);
        double curY = x.get(0,1);
        double curTheta = x.get(0,2);
        double Vr = x.get(0, 3);
        double Vl = x.get(0,4);
        double newVr = Chassis.getInstance().get_rightVelocitySetpoint();
        double newVl = Chassis.getInstance().get_leftVelocitySetpoint();
        RigidTransform curPose = new RigidTransform(new Translation(curX, curY), Rotation.fromDegrees(curTheta));
        Twist twistGenerated = Kinematics.forwardKinematics(Vr, Vl).scaled(Constants.TRACK_SCRUBBING_FACTOR*deltaT);
        RigidTransform newPose = curPose.transformBy(RigidTransform.exp(twistGenerated));
        return newPose.getKallmanStateVector(newVr, newVl );
    }

    public RigidTransform getLatestPose(){
        Matrix latestPoseMatrix = super.getLatestState();
        return observationMatrixToRigidTransformationPose(latestPoseMatrix);
    }

    private RigidTransform observationMatrixToRigidTransformationPose(Matrix observationMatrix){
        double x = observationMatrix.get(0,0);
        double y = observationMatrix.get(0,1);
        double theta = observationMatrix.get(0,2);
        Translation translation = new Translation(x,y);
        Rotation rotation = Rotation.fromDegrees(theta);
        RigidTransform pose = new RigidTransform(translation, rotation);
        return pose;
    }

    private Matrix rigidTransformationToPoseVector(RigidTransform rTransform){
        return poseVectorFromConfigurationVector(rTransform.getKallmanStateVector(0, 0));
    }

    private Matrix poseCovarianceMatrixFromConfigurationCovarianceMatrix(Matrix configCovarianceMatrix){
        return configCovarianceMatrix.getMatrix(0, 0, 2, 2);
    }

    private Matrix poseVectorFromConfigurationVector(Matrix configVector){
        return configVector.getMatrix(0, 0, 0, 2);
    }

    public Matrix getLatestCovariance(){
        Matrix covarMatrix = super.getLatestCovariance();
        return covarMatrix;
    }

    private double multivariateNormalPDF(Matrix mu, Matrix sigma, Matrix x){
        return (Math.exp(-.5*(x.minus(mu).transpose().times(sigma).times(x.minus(mu)).get(0,0)))/(Math.sqrt(8*Math.pow(Math.PI,3)*sigma.det())));
    }

    public double getProbabilityOfPose(RigidTransform pose){
        Matrix sigmaMatrix = poseCovarianceMatrixFromConfigurationCovarianceMatrix(super.getLatestCovariance());
        Matrix poseMatrix = rigidTransformationToPoseVector(pose);
        Matrix muVector = poseVectorFromConfigurationVector(super.getLatestState());
        return multivariateNormalPDF(muVector, sigmaMatrix, poseMatrix);
    }
}
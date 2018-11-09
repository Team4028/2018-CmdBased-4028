package org.usfirst.frc.team4028.robot.auton.pathfollowing.posetracking;


import org.usfirst.frc.team4028.robot.auton.pathfollowing.util.maphs.matrix.*;

import edu.wpi.first.wpilibj.Timer;

public class linearKallmanFilter{
    public Matrix currentConfigVectorX;
    public Matrix predictedConfigVectorX;
    public Matrix currentCovarianceMatrixP;
    public Matrix predictedCovarianceMatrixP;
    private double currentUpdatedTimeInSeconds;
    private double previousUpdatedTimeInSeconds;
    private double dt;
    private Matrix diffEqMatrixF;
    private Matrix localStateMapMatrixPhi;
    private Matrix kalmanGainMatrixK;
    private Matrix noiseDistributionMatrixG;
    private Matrix instantaneousPhysicalUncertaintyMatrixQ;
    private Matrix instantaneousMeasurementUncertaintyMatrixR;
    private Matrix mapStateToMeasurementMatrixH;
    private boolean hasStarted = false;
    private int dim;



    
    public linearKallmanFilter(Matrix diffyQueueMatrixF, Matrix startConfigVectorX0, Matrix noisyDistributionMatrixG, Matrix initialCovarianceMatrixP0, Matrix mapStateToMeasMatrixH, Matrix instantaneousPhysUncertaintyMatrixQ, Matrix instantantMeasurementUncertaintyMatrixR, int numParams){
        this.currentConfigVectorX = startConfigVectorX0;
        this.currentCovarianceMatrixP = initialCovarianceMatrixP0;
        this.diffEqMatrixF = diffyQueueMatrixF;
        this.dt = 0;
        this.noiseDistributionMatrixG = noisyDistributionMatrixG;
        this.instantaneousMeasurementUncertaintyMatrixR = instantantMeasurementUncertaintyMatrixR;
        this.instantaneousPhysicalUncertaintyMatrixQ = instantaneousPhysUncertaintyMatrixQ;
        this.mapStateToMeasurementMatrixH = mapStateToMeasMatrixH;
        this.dim = numParams;
    }

    public void startRunning(){
        if (!hasStarted){
            this.currentUpdatedTimeInSeconds = Timer.getFPGATimestamp();
            this.hasStarted = true;
        }
    }

    public void predict(){
        updatePhi();
        predictState();
        predictCovariance();
    }

    private void updatePhi(){
        this.previousUpdatedTimeInSeconds = this.currentUpdatedTimeInSeconds;
        this.currentUpdatedTimeInSeconds = Timer.getFPGATimestamp();
        this.dt = this.currentUpdatedTimeInSeconds - this.previousUpdatedTimeInSeconds;
        this.localStateMapMatrixPhi = this.diffEqMatrixF.times(this.dt).mExp();       
    }

    private void predictState(){
        this.currentConfigVectorX = this.predictedConfigVectorX;
        this.predictedConfigVectorX = this.localStateMapMatrixPhi.times(this.currentConfigVectorX);
    }

    private void predictCovariance(){
        this.currentCovarianceMatrixP = this.predictedCovarianceMatrixP;
        this.predictedCovarianceMatrixP = this.localStateMapMatrixPhi.times(this.currentCovarianceMatrixP).times(this.localStateMapMatrixPhi.transpose()).plus(this.noiseDistributionMatrixG.times(this.instantaneousPhysicalUncertaintyMatrixQ).times(this.noiseDistributionMatrixG.transpose()));
    }

    private void computekalmanGain(){
        this.kalmanGainMatrixK = this.predictedCovarianceMatrixP.times(this.mapStateToMeasurementMatrixH.transpose()).times(this.mapStateToMeasurementMatrixH.times(this.predictedCovarianceMatrixP).times(this.mapStateToMeasurementMatrixH.transpose()).plus(this.instantaneousMeasurementUncertaintyMatrixR).inverse());
    }

    private void filterStateEstimate(Matrix newMeasurement){
        this.predictedConfigVectorX = this.predictedConfigVectorX.plus(this.kalmanGainMatrixK.times(newMeasurement.minus(this.mapStateToMeasurementMatrixH.times(this.predictedConfigVectorX))));
    }

    private void filterCovariance(){
        this.predictedCovarianceMatrixP = Matrix.identity(this.dim, this.dim).minus(this.kalmanGainMatrixK.times(this.mapStateToMeasurementMatrixH)).times(this.predictedCovarianceMatrixP);
    }

    public void filter(Matrix newMeasurement){
        computekalmanGain();
        filterStateEstimate(newMeasurement);
        filterCovariance();
    }

    public void update(Matrix newMeasurement){
        predict();
        filter(newMeasurement);
    }

    public void updateDiffEqMatrix(Matrix newDiffEqMatrix){
        this.diffEqMatrixF = newDiffEqMatrix;
    }

    public Matrix getLatestConfig(){
        return this.predictedConfigVectorX;
    }

    public Matrix getLatestCovar(){
        return this.predictedCovarianceMatrixP;
    }
}
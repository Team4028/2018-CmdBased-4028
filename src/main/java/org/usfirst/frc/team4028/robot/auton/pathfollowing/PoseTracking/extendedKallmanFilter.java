package org.usfirst.frc.team4028.robot.auton.pathfollowing.PoseTracking;

import org.usfirst.frc.team4028.robot.auton.pathfollowing.util.maphs.matrix.Matrix;

import edu.wpi.first.wpilibj.Timer;

public abstract class extendedKallmanFilter {


      ////////////////////////////////////////////////
     ////////////// Parameters //////////////////////
    ////////////////////////////////////////////////

    Matrix currentDiffEqJacobianF;
    Matrix currentConfigVectorX; 
    Matrix predictedConfigVectorX; 
    Matrix currentCovarianceMatrixP;
    Matrix predictedCovarianceMatrixP;
    double lastDynamicsUpdateTime;
    double thisDyanmicsUpdateTime;
    double dt; 
    Matrix instantaneousPhysicalUncertaintyMatrixQ; 
    Matrix instantaneousMeasurementUncertaintyMatrixR; 
    Matrix localStateMapMatrixPhi; 
    Matrix kalmanGainMatrixK; 
    Matrix noiseDistributionJacobianG; 
    Matrix mapStateToMeasurementJacobianH;
    Matrix constantPartOfMatrixQ;
    boolean hasStarted = false;
    boolean isFirstCycle = true;
    boolean isSecondCycle = false;
    int dim;
    public abstract Matrix dynamicsFunctionPhi(Matrix x, double deltaT);
    public abstract Matrix mapXtoZFunctionH(Matrix z);
    public abstract Matrix updateGfromX(Matrix x);
    public abstract Matrix updateRfromXandDT(Matrix x, double deltaT);
    public abstract Matrix updateHfromX(Matrix x);
    public abstract Matrix  updateFfromXandDT(Matrix x, double deltaT);


      ////////////////////////////////////////////////
     ////////// Constructor-Type Methods ////////////
    ////////////////////////////////////////////////
    public abstract Matrix initDiffEqJacobian();
    public abstract Matrix initConfigVector();
    public abstract Matrix initCovarianceMatrix();
    public abstract Matrix initConstantPartOfMatrixQ();
    public abstract Matrix initInstantaneousMeasurementUncertaintyMatrixR();
    public abstract Matrix initNoiseDistributionJacobianG();
    public abstract Matrix initMapStateToMeasurementJacobianH();
    public abstract int initDimensionality();


    public void initialize(){
        this.currentDiffEqJacobianF = this.initDiffEqJacobian();
        this.predictedConfigVectorX = this.initConfigVector();
        this.predictedCovarianceMatrixP = this.initCovarianceMatrix();
        this.constantPartOfMatrixQ = this.initConstantPartOfMatrixQ();
        this.instantaneousMeasurementUncertaintyMatrixR =this.initInstantaneousMeasurementUncertaintyMatrixR();
        this.noiseDistributionJacobianG = this.initNoiseDistributionJacobianG();
        this.mapStateToMeasurementJacobianH = this.initMapStateToMeasurementJacobianH();
        this.dim = this.initDimensionality();
    }

      //////////////////////////////////////////////////
     ////////// Methods of the Class Proper ///////////
    //////////////////////////////////////////////////

    public void startRunning(){
        if (! this.hasStarted){
            this.thisDyanmicsUpdateTime = Timer.getFPGATimestamp();
            this.hasStarted = true;
        }
    }

    public void predict(double timeStamp){
        updateTiming(timeStamp);
        predictState();
        predictCovariance();        
    }

    public void updateTiming(double timeStamp){
        this.lastDynamicsUpdateTime = this.thisDyanmicsUpdateTime;
        this.thisDyanmicsUpdateTime = timeStamp;
        this.dt = this.thisDyanmicsUpdateTime - this.lastDynamicsUpdateTime;
    }

    public void predictState(){
        this.currentConfigVectorX = this.predictedConfigVectorX;
        this.predictedConfigVectorX = dynamicsFunctionPhi(this.currentConfigVectorX, this.dt);
    }

    public void updatePhi(){
        updateF();
        this.localStateMapMatrixPhi = this.currentDiffEqJacobianF.times(this.dt).mExp();
    }

    public void updateF(){
        this.currentDiffEqJacobianF = this.updateFfromXandDT(this.currentConfigVectorX, this.dt);
    }

    public void updateQ(){
        this.instantaneousPhysicalUncertaintyMatrixQ = this.constantPartOfMatrixQ.times(this.dt);
    }

    public void updateG(){
        this.noiseDistributionJacobianG = this.updateGfromX(this.currentConfigVectorX);
    }

    public void predictCovariance(){
        updatePhi();
        updateG();
        updateQ();
        this.currentCovarianceMatrixP = this.predictedCovarianceMatrixP;
        this.predictedCovarianceMatrixP = this.localStateMapMatrixPhi.times(this.currentCovarianceMatrixP).times(this.localStateMapMatrixPhi.transpose()).plus(this.noiseDistributionJacobianG.times(this.instantaneousPhysicalUncertaintyMatrixQ).times(this.noiseDistributionJacobianG.transpose()));
    }

    public void updateR(){
        this.instantaneousMeasurementUncertaintyMatrixR = this.updateRfromXandDT(this.currentConfigVectorX, this.dt);
    }

    public void updateH(){
        this.mapStateToMeasurementJacobianH = this.updateHfromX(this.currentConfigVectorX);
    }


    public void filter(Matrix newMeasurement){
        computekalmanGain();
        updateStateEstimate(newMeasurement);
        updateCovarianceMatrix();
    }

    public void computekalmanGain(){
        updateH();
        updateR();
        this.kalmanGainMatrixK = this.predictedCovarianceMatrixP.times(this.mapStateToMeasurementJacobianH.transpose()).times(this.mapStateToMeasurementJacobianH.times(this.predictedCovarianceMatrixP).times(this.mapStateToMeasurementJacobianH.transpose()).plus(this.instantaneousMeasurementUncertaintyMatrixR).inverse());
    }

    public void updateStateEstimate(Matrix newMeasurement){
        this.predictedConfigVectorX.plusEquals(this.kalmanGainMatrixK.times(newMeasurement.minus(this.mapXtoZFunctionH(this.predictedConfigVectorX))));
    }

    public void updateCovarianceMatrix(){
        this.predictedCovarianceMatrixP = Matrix.identity(this.dim, this.dim).minus(this.kalmanGainMatrixK.times(this.mapStateToMeasurementJacobianH)).times(this.predictedCovarianceMatrixP);
    }

    public Matrix getLatestState(){
        return this.predictedConfigVectorX;
    }

    public Matrix getLatestCovariance(){
        return this.predictedCovarianceMatrixP;
    }

    public void update(Matrix measurement, double timeStamp){
        if (this.isFirstCycle){
            this.isFirstCycle = false;
            this.isSecondCycle = true;
        } else {
        predict(timeStamp);
        filter(measurement);
        if (isSecondCycle){
            isSecondCycle = false;
        }
        }
    }

}

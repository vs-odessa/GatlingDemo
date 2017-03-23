#!/bin/bash
set -x #echo on
##################################################################################################################
#Gatling scale out/cluster run script:
#Before running this script some assumptions are made:
#Check  read/write permissions on all folders declared in this script.
##################################################################################################################

#Change to your simulation class name
#SIMULATION_NAME='computerdatabase.BasicSimulation' # Defined in Jenkins job

#Assuming all Gatling installation in same path (with write permissions)
GATLING_HOME=$WORKSPACE #Is equal to /home/jenkins/workspace/$JOB_NAME
GATLING_SIMULATIONS_DIR=$GATLING_HOME/user-files/simulations
GATLING_RUNNER=$GATLING_HOME/bin/gatling.sh
chmod +x $GATLING_HOME/bin/*

#No need to change this
GATLING_REPORT_DIR=$GATLING_HOME/results/
GATHER_REPORTS_DIR=$GATLING_HOME/mygatling-report/

echo "Cleaning previous runs from localhost..."
rm -rf $GATHER_REPORTS_DIR
mkdir $GATHER_REPORTS_DIR
rm -rf $GATLING_REPORT_DIR

echo "Running simulation on localhost..."
$GATLING_RUNNER -nr -s $SIMULATION_NAME

sleep 15

echo "Gathering result file from localhost..."
ls -t $GATLING_REPORT_DIR | head -n 1 | xargs -I {} mv ${GATLING_REPORT_DIR}{} ${GATLING_REPORT_DIR}report
cp ${GATLING_REPORT_DIR}report/simulation.log $GATHER_REPORTS_DIR


mv $GATHER_REPORTS_DIR $GATLING_REPORT_DIR
echo "Generate report for local run..."
$GATLING_RUNNER -ro mygatling-report 

#using macOSX
#open ${GATLING_REPORT_DIR}reports/index.html

#using ubuntu
#google-chrome ${GATLING_REPORT_DIR}reports/index.html

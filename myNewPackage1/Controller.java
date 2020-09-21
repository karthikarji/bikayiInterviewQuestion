package myNewPackage1;

import java.util.*;

public class Controller {

    private Lift lift;
    private List<Lift> listOfLifts;
    private static Map<Integer, Lift> eligibleLiftValuesMap = new HashMap();

    Controller() {
        int noOfFloors = 20;//no of floors
        lift = new Lift(noOfFloors,2,Direction.UP);
        listOfLifts = new ArrayList<>();
        listOfLifts.add(new Lift(noOfFloors,2,Direction.UP));
        listOfLifts.add(new Lift(noOfFloors,10,Direction.UP));
        for(Lift lift: listOfLifts){
            lift.start();
        }
    }

    void callElevator(Person person) {
        List<Lift> listOfLifts = new ArrayList<>();
        if(person.direction == Direction.UP)
            lift.listOfPeopleGoingUp.addAsc(person);
        else
            lift.listOfPeopleGoingDown.addDesc(person);

//        nearestLift(person.inFloor, person.outFloor, 20,person);
    }

    public void nearestLift(int requestFromFloor, int destinationFloor, int totalNoOfFloors, Person person){
        List<Lift> allLifts = new ArrayList<>();
        int maxEligibility = 0;
        for(Lift lift : allLifts){
            int distanceBetweenFloorAndElevator;
            int eligibilityValue = 0;
            int currentLiftFloor = lift.getCurrentFloor();
            if(requestFromFloor>currentLiftFloor){
                distanceBetweenFloorAndElevator = (requestFromFloor - currentLiftFloor)-1;
                if(destinationFloor > requestFromFloor && lift.getDirection()== Direction.UP){
                    eligibilityValue = totalNoOfFloors +1 - distanceBetweenFloorAndElevator;
                }
                else if(destinationFloor < requestFromFloor && lift.getDirection() == Direction.UP){
                    eligibilityValue = totalNoOfFloors - distanceBetweenFloorAndElevator;
                }
                else{
                    eligibilityValue = 1;
                }
                eligibleLiftValuesMap.put(eligibilityValue, lift);
            }
            else{
                distanceBetweenFloorAndElevator = (currentLiftFloor - requestFromFloor)-1;
                if(destinationFloor < requestFromFloor && lift.getDirection()== Direction.DOWN ){
                    eligibilityValue = totalNoOfFloors +1 - distanceBetweenFloorAndElevator;
                }
                else if(destinationFloor > requestFromFloor && lift.getDirection()== Direction.DOWN){
                    eligibilityValue = totalNoOfFloors - distanceBetweenFloorAndElevator;
                }
                else{
                    eligibilityValue = 1;
                }
                eligibleLiftValuesMap.put(eligibilityValue, lift);
            }
            if(maxEligibility < eligibilityValue){
                maxEligibility = eligibilityValue;
            }
        }
        if(eligibleLiftValuesMap.get(maxEligibility).getMaxFloor() != 20){
            if(eligibleLiftValuesMap.get(maxEligibility).getDirection() == Direction.UP){
                eligibleLiftValuesMap.get(maxEligibility).listOfPeopleGoingUp.addAsc(person);
            }
            else{
                eligibleLiftValuesMap.get(maxEligibility).listOfPeopleGoingDown.addDesc(person);
            }
        }
    }
}
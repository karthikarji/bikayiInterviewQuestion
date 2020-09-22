package myNewPackage1;

import java.util.*;

public class Controller {

    private Lift lift;
    private List<Lift> listOfLifts;
    private static Map<Integer, Lift> eligibleLiftValuesMap = new HashMap();

    Controller() {
        int noOfFloors = 20;//no of floors
        lift = new Lift(noOfFloors,2,Direction.UP);
        lift.start();
    }

    void callElevator(Person person) {
        if(person.direction == Direction.UP)
            lift.listOfPeopleGoingUp.addAsc(person);
        else
            lift.listOfPeopleGoingDown.addDesc(person);

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

    public static void main(String args[]) {
        Controller c = new Controller();
        int noOfPeople = 3;
        c.testCase(3);
    }

    void testCase( int n ) {
        lift.controller = this;
        Random r = new Random();
        int source;
        int dest;
        Person p;

        for(int i = 0; i < n; i++) {
            source = r.nextInt(Lift.maxFloor) + 1;
            dest = r.nextInt(Lift.maxFloor) + 1;
            p = new Person( source, dest, this);
            p.start();
        }
        try{
            Thread.sleep(1000);
        }catch(Exception e){ e.printStackTrace(); }
        lift.interrupt();
    }
}

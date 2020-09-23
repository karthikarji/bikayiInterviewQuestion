package myNewPackage1;

public class Lift extends Thread {
    String name;
    final static int maxWeight = 5;
    final static int minWeight = 1;
    final static int minFloor = 1;
    static int maxFloor = 20;
    static int currentFloor;
    Direction direction;
    Door door;
    Controller controller = null;

    LinkedList<Person> listOfPeopleGoingUp;
    LinkedList<Person> listOfPeopleGoingDown;
    LinkedList<Person> listOfPeopleInside;

    Lift(int noOfFloors,int currentFloor,Direction direction) {
        this.maxFloor = noOfFloors;
        this.currentFloor = currentFloor;
        this.direction = direction;
        this.door = Door.CLOSE;

        listOfPeopleGoingUp = new LinkedList<Person>();
        listOfPeopleGoingDown = new LinkedList<Person>();
        listOfPeopleInside = new LinkedList<Person>();

    }
    public static int getCurrentFloor() {
        return currentFloor;
    }
    public Direction getDirection() {
        return direction;
    }
    public static int getMaxFloor() {
        return maxFloor;
    }

    public void run() {
        while(true) {
            if(isInterrupted()) {
                while( listOfPeopleGoingUp.head != null || listOfPeopleGoingDown.head != null || listOfPeopleInside.head != null ) {
                    if( direction == Direction.UP && currentFloor <= maxFloor) {
                        servePeopleGoingUp();
                        direction = Direction.DOWN;
                    }
                    else if( direction == Direction.DOWN && currentFloor >= minFloor) {
                        servePeopleGoingDown();
                        direction = Direction.UP;
                    }
                }
                break;
            }
        }
    }

    void servePeopleGoingUp() {
        Node<Person> currentPersonInUpList = listOfPeopleGoingUp.head;
        Node<Person> currentPersonInElevator = listOfPeopleInside.head;
        LinkedList<Person> tempList = new LinkedList<Person>();
        boolean isDoorOpen = false;

        while( currentPersonInUpList != null || currentPersonInElevator != null ) {	// if anyone wants to get out / get in

            System.out.println("\n*********** FLOOR : " + currentFloor + " : ***********\n");

            if(currentPersonInElevator != null) {			// if there is someone who wants to get out

                // check all the people in lift
                currentPersonInElevator = listOfPeopleInside.head;
                while(currentPersonInElevator != null) {
                    if( currentPersonInElevator.data.outFloor == currentFloor ) {		// check someone has to get out at this floor

                        if(!isDoorOpen) {
                            openDoor();
                            isDoorOpen = false;
                        }
                        listOfPeopleInside.remove(currentPersonInElevator.data);
                        System.out.println("Person : " + currentPersonInElevator.data + " is leaving lift");
                        currentPersonInElevator.data.wakeUp();

                    }
                    currentPersonInElevator = currentPersonInElevator.next;
                }

            }

            if(currentPersonInUpList != null) {		// check if  anyone wants to get in
                if( currentPersonInUpList.data.inFloor == currentFloor ) {
                    openDoor();
                    while( currentPersonInUpList.data.inFloor == currentFloor ) {
                        System.out.println("Taking in : " + currentPersonInUpList.data + " at floor : " + currentFloor );
                        listOfPeopleInside.addAsc(currentPersonInUpList.data);
                        if( overweight() ) {
                            listOfPeopleInside.remove(currentPersonInUpList.data);
                            System.out.println(currentPersonInUpList.data + " you should go out : Elevator overweight!!");
                            listOfPeopleGoingUp.remove(currentPersonInUpList.data);
                            tempList.addAsc(currentPersonInUpList.data);

                        } else {
                            listOfPeopleGoingUp.remove(currentPersonInUpList.data);
                        }

                        currentPersonInUpList = currentPersonInUpList.next;
                        if(currentPersonInUpList == null)
                            break;
                    }

                    closeDoor();

                }
            } else {
                closeDoor();
            }

            currentPersonInUpList = listOfPeopleGoingUp.head;
            currentPersonInElevator = listOfPeopleInside.head;


            if( currentPersonInUpList != null || currentPersonInElevator != null )
                currentFloor = currentFloor + 1;
        }

        // add people back to upList to be served on the next round
        Node<Person> currentPersonInTempList = tempList.head;
        while(currentPersonInTempList != null) {
            listOfPeopleGoingUp.addAsc(currentPersonInTempList.data);
            currentPersonInTempList = currentPersonInTempList.next;
        }

        // what if you have not reached the top floor from where the downlist will be served
        if(listOfPeopleGoingDown.head != null) {
            int downFloorStart = listOfPeopleGoingDown.head.data.inFloor;
            if(downFloorStart > currentFloor) {
                //System.out.println("preparing to serve people going down");
                while(downFloorStart != currentFloor) {
                    currentFloor = currentFloor + 1;
                }
            }
        }
    }

    void servePeopleGoingDown() {

        Node<Person> currentPersonInDownList = listOfPeopleGoingDown.head;
        Node<Person> currentPersonInElevator = listOfPeopleInside.head;
        LinkedList<Person> tempList = new LinkedList<Person>();
        boolean isDoorOpen = false;

        while( currentPersonInDownList != null || currentPersonInElevator != null ) {	// if anyone wants to get out / get in

            System.out.println("\n*********** FLOOR : " + currentFloor + " : ***********\n");

            if(currentPersonInElevator != null) {			// if there is someone who wants to get out
                // check all the people in lift
                currentPersonInElevator = listOfPeopleInside.head;
                while(currentPersonInElevator != null) {
                    if( currentPersonInElevator.data.outFloor == currentFloor ) {		// check someone has to get out at this floor

                        if(!isDoorOpen) {
                            openDoor();
                            isDoorOpen = false;
                        }

                        listOfPeopleInside.remove(currentPersonInElevator.data);
                        System.out.println("Person : " + currentPersonInElevator.data + " is leaving lift");
                        currentPersonInElevator.data.wakeUp();

                    }
                    currentPersonInElevator = currentPersonInElevator.next;
                }
            }

            if(currentPersonInDownList != null) {		// check if  anyone wants to get in
                if( currentPersonInDownList.data.inFloor == currentFloor ) {
                    openDoor();
                    //try{sleep(10000);}catch(Exception e){e.printStackTrace();}
                    while( currentPersonInDownList.data.inFloor == currentFloor ) {
                        System.out.println("Taking in : " + currentPersonInDownList.data + " at floor : " + currentFloor );
                        listOfPeopleInside.addDesc(currentPersonInDownList.data);
                        if( overweight() ) {
                            listOfPeopleInside.remove(currentPersonInDownList.data);
                            System.out.println(currentPersonInDownList.data + " you should go out : Elevator overweight!!");
                            listOfPeopleGoingDown.remove(currentPersonInDownList.data);
                            tempList.addDesc(currentPersonInDownList.data);

                        } else {
                            listOfPeopleGoingDown.remove(currentPersonInDownList.data);
                        }

                        currentPersonInDownList = currentPersonInDownList.next;
                        if(currentPersonInDownList == null)
                            break;
                    }

                    closeDoor();

                }
            } else {
                closeDoor();
            }

            currentPersonInDownList = listOfPeopleGoingDown.head;
            currentPersonInElevator = listOfPeopleInside.head;


            if( currentPersonInDownList != null || currentPersonInElevator != null )
                currentFloor = currentFloor - 1;
        }

        // add people back to upList to be served on the next round
        Node<Person> currentPersonInTempList = tempList.head;
        while(currentPersonInTempList != null) {
            listOfPeopleGoingDown.addDesc(currentPersonInTempList.data);
            currentPersonInTempList = currentPersonInTempList.next;
        }

        // what if you have not reached the bottom floor from where the uplist will be served
        if(listOfPeopleGoingUp.head != null) {
            int upFloorStart = listOfPeopleGoingUp.head.data.inFloor;
            if(upFloorStart < currentFloor) {
                //System.out.println("preparing to serve people going down");
                while(upFloorStart != currentFloor) {
                    currentFloor = currentFloor - 1;
                }

            }
        }

    }

    boolean overweight() {
        boolean goesOver = false;
        Node<Person> currentPersonInElevator = listOfPeopleInside.head;
        int totalWeight = 0;
        while( currentPersonInElevator != null ) {
            totalWeight = totalWeight + currentPersonInElevator.data.weight;
            currentPersonInElevator = currentPersonInElevator.next;
        }

        if(totalWeight > maxWeight)
            goesOver = true;

        return goesOver;
    }

    void openDoor() {
        door = Door.OPEN;
        System.out.println("Door opened");
    }

    void closeDoor() {
        door = Door.CLOSE;
        System.out.println("Door Closed");
    }

    public String toString() {
        return "[ Floor : " + currentFloor + ", Door : " + door + ", going " + direction + " ]";
    }

}

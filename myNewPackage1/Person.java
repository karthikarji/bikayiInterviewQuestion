package myNewPackage1;

public class Person extends Thread implements Comparable<Person> {

    int weight;
    int inFloor;
    int outFloor;
    Direction direction;
    Controller controller;

    Person(int in, int out, Controller c) {
        weight = 1;
        inFloor = in;
        outFloor = out;
        controller = c;

        System.out.println("Person Created :\n[ID: " + getId() + ", weight : " + weight + ", goingIntoLiftAtFloor : " + inFloor + ", comingOutOfFlatAtFloor : " + outFloor + "]\n");

        // also check condition for infloor == outfloor
        if(inFloor == outFloor) {
            System.out.println("Source floor and destination floor cannot be the same.");
            System.exit(1);
        }
        direction = (inFloor < outFloor) ? Direction.UP : Direction.DOWN;
    }

    public void run() {
        try{
            synchronized(controller) {
                controller.callElevator(this);
            }
            synchronized(this) {
                wait();
            }
            System.out.println("End of thread : " + this);

        } catch(InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    void wakeUp( ) {
        try {
            System.out.println("thread : " + this + " waking up....");
            synchronized(this) {
                notify();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public int compareTo( Person p ) {
        if( this.inFloor < p.inFloor ) {
            return -1;
        }
        else if( this.inFloor > p.inFloor ) {
            return 1;
        } else {
            return 0;
        }

    }
    public String toString() {
        return "ID: " + getId(); // + " weight : " + weight + " in : " + inFloor + " out : " + outFloor;
    }
}

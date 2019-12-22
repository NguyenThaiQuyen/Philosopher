package v1;

import java.util.concurrent.Semaphore;

public class Chopstick {

    private int id;
    private boolean taken = false;

    public Semaphore semaphore = new Semaphore(1);

    public Chopstick(int id) {
        this.id = id;
    }

    void put() {
        semaphore.release();
        taken = false;
    }

     void get() {
        try {
            while(taken) {
                semaphore.acquire();
            }
            taken = true;
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isTaken() {
        return taken;
    }

    public int getId() {
        return id;
    }
}


package v1;

import java.util.Random;

public class Philosopher extends Thread {
    private int id;
    private Chopstick left;
    private Chopstick right;
    public STATUS status = STATUS.THINKING;
    private static Dinner view;
    private boolean deadlock = false;
    private boolean running = true;

    private Random random;

    public enum STATUS {
        EATING,
        THINKING,
        HUNGRY,
        GETRIGHT,
        GETLEFT
    }

    public Philosopher(int id, Chopstick l, Chopstick r) {
        this.id = id;
        this.left = l;
        this.right = r;

        random = new Random();
    }

    public Philosopher(int id, Chopstick l, Chopstick r, boolean d) {
        this.id = id;
        this.left = l;
        this.right = r;
        this.deadlock = d;
        random = new Random();
    }

    private int generateRandomSleepAndThinkingTime() {
        return random.nextInt(5001) + 3000;
    }

    public void Wait() {
        if (running) {
            try {
                this.status = STATUS.HUNGRY;
                view.updatePhilosophers(this.id);
                view.setTextArea("Philosopher " + id + " is hungry!");
                if (!deadlock) {
                    if (id % 2 == 0) {
                        rangeChopstick(right, left, this.id);
                    } else {
                        rangeChopstick(left, right, this.id);
                    }
                } else {
                    rangeChopstick(left, right, this.id);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void Signal() {
        if (running) {
            right.put();
            view.updateChopstick(right.getId());
            view.setTextArea("Philosopher " + id + " put chopstick right!");
            this.status = STATUS.THINKING;
            view.updatePhilosophers(this.id);
            left.put();
            view.updateChopstick(left.getId());
            view.setTextArea("Philosopher " + id + " is thinking!");
        }
    }

    @Override
    public void run() {
        try {
            while(running) {
                if (!deadlock) {
                    sleep(generateRandomSleepAndThinkingTime());
                    Wait();
                    sleep(generateRandomSleepAndThinkingTime());
                    Signal();
                } else {
                    sleep(4000);
                    Wait();
                    sleep(generateRandomSleepAndThinkingTime());
                    Signal();
                }

            }
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
   }

    public void setView(Dinner view) {
        this.view = view;
    }

    public void destroyPhil() {
        this.running = false;
    }

    public boolean isRunning() {
        return this.running;
    }

    public void rangeChopstick(Chopstick firstChop, Chopstick secondChop, int id) {
        firstChop.get();
        this.status = STATUS.GETRIGHT;
        view.updateChopstick(firstChop.getId());
        view.updatePhilosophers(id);
        view.setTextArea("Philosopher " + id + " get chopstick right!");
        secondChop.get();
        this.status = STATUS.EATING;
        view.updateChopstick(secondChop.getId());
        view.updatePhilosophers(id);
        view.setTextArea("Philosopher " + id + " is eating!");
    }
}
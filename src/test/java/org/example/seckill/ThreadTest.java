package org.example.seckill;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadTest {
    public static void main(String[] args) {
        Demo demo = new Demo();
        new Thread(()-> {
            for (int i = 0; i < 50; i++) {
                demo.soutO();
            }
        }).start();

        new Thread(()-> {
            for (int i = 0; i < 50; i++) {
                demo.soutJ();
            }
        }).start();

    }
}

class Demo {
    private int i = 1;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public void soutO() {
        lock.lock();
        try {
            while (i % 2 == 0) {
                // 当前是偶数
                condition.await();
            }
            System.out.println(this.i);
            i++;
            condition.signalAll();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public void soutJ() {
        lock.lock();
        try {
            while (i % 2 != 0) {
                // 当前是偶数
                condition.await();
            }
            System.out.println(this.i);
            i++;
            condition.signalAll();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

}

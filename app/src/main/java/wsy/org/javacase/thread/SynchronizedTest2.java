package wsy.org.javacase.thread;

/**
 * Created by wsy on 2017/8/8.
 * <p>
 * 结论：
 * ①synchronized（InnerClass2.class）给class加锁
 * ②synchronized（obj） 给对象加锁
 * ③synchronized 给非静态方法加锁
 * ④synchronized 给静态方法加锁
 * <p>
 * 说明：
 * ①给class加锁，该类的所有对象下的所有方法同时只能被一个线程调用。
 * ②给对象加锁，该对象下的所有方法(包含静态非静态)同时只能被一个线程调用。
 * ③该方法所在的对象内所有的非静态加锁的方法，同时只能在一个线程内调用。
 * ④该方法所在的class内所有静态加锁的方法，同时只能被一个线程调用。
 * <p>
 * 注：同一个类中既有加锁静态方法又有加锁非静态方法，两类互不影响
 */

public class SynchronizedTest2 {


    public static void main(String[] args) {

        InnerClass2 InnerClass21 = new InnerClass2();
        InnerClass2 InnerClass22 = new InnerClass2();

        new TestThread1(InnerClass21).start();
        new TestThread2(InnerClass21).start();
        new TestThread3(InnerClass21).start();

    }

    static class InnerClass2 {
        public static void test1() throws InterruptedException {
            System.out.println("执行test1");
            Thread.sleep(1000);
            System.out.println("执行test1结束");
        }

        public void test2() throws InterruptedException {
            System.out.println("执行test2");
            Thread.sleep(1000);
            System.out.println("执行test2结束");
        }

        public void test3() throws InterruptedException {
            System.out.println("执行test3");
            Thread.sleep(1000);
            System.out.println("执行test3结束");
        }
    }


    static class TestThread1 extends Thread {
        private InnerClass2 obj;

        public TestThread1(InnerClass2 obj) {
            this.obj = obj;
        }

        @Override
        public void run() {
            try {
                synchronized (obj) {
                    obj.test1();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class TestThread2 extends Thread {
        private InnerClass2 obj;

        public TestThread2(InnerClass2 obj) {
            this.obj = obj;
        }

        @Override
        public void run() {
            try {
                synchronized (obj) {
                    obj.test2();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class TestThread3 extends Thread {
        private InnerClass2 obj;

        public TestThread3(InnerClass2 obj) {
            this.obj = obj;
        }

        @Override
        public void run() {
            try {
                synchronized (obj) {
                    obj.test3();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

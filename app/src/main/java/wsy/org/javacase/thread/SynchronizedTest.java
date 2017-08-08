package wsy.org.javacase.thread;

/**
 * Created by wsy on 2017/8/8.
 */

public class SynchronizedTest {

    public static void main(String[] args) {
        InnerClass innerClass = new InnerClass();

        new TestThread1(innerClass).start();
        new TestThread2(innerClass).start();
        new TestThread3(innerClass).start();
        new TestThread4(innerClass).start();

    }

    static class InnerClass {
        public synchronized static void test1() throws InterruptedException {
            System.out.println("执行test1");
            Thread.sleep(1000);
            System.out.println("执行test1结束");
        }

        public synchronized static void test2() throws InterruptedException {
            System.out.println("执行test2");
            Thread.sleep(1000);
            System.out.println("执行test2结束");
        }

        public synchronized void test3() throws InterruptedException {
            System.out.println("执行test3");
            Thread.sleep(1000);
            System.out.println("执行test3结束");
        }

        public synchronized void test4() throws InterruptedException {
            System.out.println("执行test4");
            Thread.sleep(1000);
            System.out.println("执行test4结束");
        }
    }


    static class TestThread1 extends Thread {
        private InnerClass obj;

        public TestThread1(InnerClass obj) {
            this.obj = obj;
        }

        @Override
        public void run() {
            try {
                obj.test1();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class TestThread2 extends Thread {
        private InnerClass obj;

        public TestThread2(InnerClass obj) {
            this.obj = obj;
        }

        @Override
        public void run() {
            try {
                obj.test2();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class TestThread3 extends Thread {
        private InnerClass obj;

        public TestThread3(InnerClass obj) {
            this.obj = obj;
        }

        @Override
        public void run() {
            try {
                obj.test3();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    static class TestThread4 extends Thread {
        private InnerClass obj;

        public TestThread4(InnerClass obj) {
            this.obj = obj;
        }

        @Override
        public void run() {
            try {
                obj.test4();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

package wsy.org.mytestapplication.noui;

import android.util.Log;

import java.util.concurrent.SynchronousQueue;

/**
 * Created by wsy on 2017/7/27.
 *
 * Demo现象:
 *
 *  先消费后生产：
 *      消费状态可以保存，在没有产品时，如果在进行了两次消费操作，在生产出产品时，会被立即消费掉。
 *
 *  先生产后消费：
 *      ....
 *
 *
 * 结论：
 *
 *  * SynchronousQueue是基于线程实用的
 *
 *  * 当SynchronousQueue内容不位空时：
 *       其它put操作都会被阻塞住；
 *
 *  * 当synchronousQueue内容为空时：
 *       多次调用take()之后，会产生多个空位置，再次put小于等于消费个数，会被直接消费掉。
 *
 *
 */

public class SynchronousQueueTest {

    private SynchronousQueue<String> synchronousQueue = new SynchronousQueue();

    public void test() {
        //生产
        product();
        product();
        custom();
    }

    public void product(){
        Producer producer1 = new Producer();
        producer1.start();
    }

    public void custom(){
        Customer customer = new Customer();
        customer.start();
    }

    /**
     * 生产者
     */
    class Producer extends Thread {

        @Override
        public void run() {
            String product = "我是线程产品" + Thread.currentThread().getId();
            Log.e("---", "生产" + product);
            try {
                synchronousQueue.put(product);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.e("---", "完毕" + product);
        }
    }

    /**
     * 消费者
     */
    class Customer extends Thread {

        @Override
        public void run() {
            try {
                String product = synchronousQueue.take();
                Log.e("---", "消费了" + product);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

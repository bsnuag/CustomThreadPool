import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by UGAM\bishnu.agrawal on 29/7/18.
 */
public class CustomThreadPool {
    private int noOfThreads;
    private List<ThreadPoolThread> threadList;
    private LinkedBlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<Runnable>();
    private boolean stopAcceptingTasks = false;

    public CustomThreadPool(int noOfThreads) {
        this.noOfThreads = noOfThreads;
        this.threadList = new ArrayList(noOfThreads);
        startAllThreads();
    }
    private void startAllThreads(){
        for (int i = 0; i < noOfThreads; i++) {
            ThreadPoolThread t = new ThreadPoolThread("CustomThreadPoolThread-"+(i+1));
            t.start();
            threadList.add(t);
        }
    }
    public void shutDown(){
        stopAcceptingTasks = true;
        for (ThreadPoolThread threadPoolThread : threadList) {
            threadPoolThread.interrupt();
        }
    }
    public void submit(Runnable task) throws Exception {
        if(!stopAcceptingTasks)
            taskQueue.put(task);
        else
            throw new Exception("ThreadPool have been shutdown, can't accept new tasks");
    }
    private class ThreadPoolThread extends Thread{
        private String name;

        public ThreadPoolThread(String name) {
            super(name);
            this.name = name;
        }
        public void run(){
            try {
                while (true) {
                    Runnable task = taskQueue.take();
                    task.run();
                }
            } catch (InterruptedException e){
                //thread have been interrupted...
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}

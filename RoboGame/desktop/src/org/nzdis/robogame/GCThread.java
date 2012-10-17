package org.nzdis.robogame;

/**
 *
 * <p>Title: GCThread</p>
 * <p>Description: a garbage collection thread</p>
 * @author Mengqiu Wang, Heiko Wolf
 */
public class GCThread extends Thread{
  protected static final Runtime runtime = Runtime.getRuntime();
  private static long snapshot1,snapshot2;
  public volatile boolean running = false;

  /**
   * a default constructor that starts up the thread
   */
  public GCThread() {
    start();
  }

  /**
   * request a garbage collection,wakes up the thread
   */
  public void wakeUp(){
    running = true;
    synchronized (this) {
      notify();
    }
  }

  /**
   * run method of the thread
   * waiting when garbage collection is not requested
   */
  public void run(){
    while (true) {
      try {
        if (running == false) {
          synchronized (this) {
            while (running == false)
              wait();
          }
        }
      }
      catch (InterruptedException e) {
      }
      try{
        gc();
      }catch(Exception e){
        System.out.println("GC Exception");
      }
      running = false;
    }
  }

  /**
   * run gabarge collection twice
   * @throws Exception
   */
  public void gc () throws Exception
  {
      for (int r = 0; r < 4; ++ r) runGC ();
  }

  /**
   * force system garbage collection until the heap size gets stable
   * @throws Exception
   */
  public void runGC () throws Exception
  {
      snapshot1 = usedMemory ();
      snapshot2 = Long.MAX_VALUE;
      for (int i = 0; (snapshot1+50 < snapshot2) && (i < 10); ++ i)
      {
          runtime.runFinalization ();
          runtime.gc ();
          Thread.currentThread ().yield ();

          snapshot2 = snapshot1;
          snapshot1 = usedMemory ();
      }
  }

  public long usedMemory ()
  {
      return runtime.totalMemory () - runtime.freeMemory ();
  }

}

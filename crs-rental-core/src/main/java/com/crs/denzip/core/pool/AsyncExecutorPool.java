package com.crs.denzip.core.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncExecutorPool {

  private final ExecutorService pool;

  public AsyncExecutorPool(int poolSize) {
    if(poolSize < 1) {
      throw new RuntimeException("Pool size should be more than 1");
    }
    this.pool = Executors.newFixedThreadPool(poolSize);
  }

  public void submit(Runnable task) {
    this.pool.execute(task);
  }
}

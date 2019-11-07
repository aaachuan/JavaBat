package com.aaachuan.ratelimiter;

import com.google.common.util.concurrent.RateLimiter;

public class RateLimiterDemo {
	public static void main(String[] args) {
		testNoRateLimiter();
		testWithRateLimiter();
	}
	
	public static void testNoRateLimiter() {
		long start = System.currentTimeMillis();
		for (int i = 0; i < 10; i++) {
			System.out.println("call execute.." + i);
		}
		long end   = System.currentTimeMillis();
		System.out.println(end-start);
	}

	public static void testWithRateLimiter() {
		long start = System.currentTimeMillis();
		RateLimiter limiter = RateLimiter.create(10.0);
		// 每秒不超过10个任务被 提交 
		for (int i = 0; i < 10; i++) {
			limiter.acquire();
			// 请求RateLimiter，超过permits会被阻塞
			System.out.println("call execute.." + i);
		}
		long end   = System.currentTimeMillis();
		System.out.println(end-start);
	}
}

package io.github.tuhe32.bin.pay.allin.util;

import io.github.tuhe32.bin.pay.common.exception.CheckedFunction;
import io.github.tuhe32.bin.pay.common.exception.PayException;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.*;

import static io.github.tuhe32.bin.pay.allin.constants.AllinPayConstant.CHECK_INTERVAL;
import static io.github.tuhe32.bin.pay.allin.constants.AllinPayConstant.TIMEOUT;

/**
 * @author 刘斌
 * @date 2024/5/8 23:17
 */
@Slf4j
public class PaymentChecker {

    private ScheduledExecutorService executorService;
    private ScheduledFuture<?> scheduledTask;
    private boolean paymentSucceeded = false;
    private final CheckedFunction<Map<String, String>, Boolean, PayException> checkIfPaymentSuccess;
    private final Map<String, String> params;

    public PaymentChecker(CheckedFunction<Map<String, String>, Boolean, PayException> checkIfPaymentSuccess, Map<String, String> params) {
        this.checkIfPaymentSuccess = checkIfPaymentSuccess;
        this.params = params;
    }

    public boolean startCheckingPayment() {
        executorService = new ScheduledThreadPoolExecutor(
                1,
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );

        long startTime = System.currentTimeMillis();
        scheduledTask = executorService.scheduleAtFixedRate(() -> {
            try {
                if (!paymentSucceeded) {
                    boolean result = this.checkIfPaymentSuccess.apply(this.params);
                    if (result) {
                        paymentSucceeded = true;
                        shutdownGracefully();
                    } else if (System.currentTimeMillis() - startTime > TIMEOUT * 1000) {
                        // 检查是否已超时
                        shutdownGracefully();
                    }
                }
            } catch (Exception e) {
                // 可能需要记录日志或发送通知
                log.error("支付检查过程中发生错误: {}", e.getMessage());
            }
        }, CHECK_INTERVAL, CHECK_INTERVAL, TimeUnit.SECONDS);

        try {
            // 等待定时任务执行完成
            scheduledTask.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("支付检查过程中发生中断错误: {}", e.getMessage());
        } catch (CancellationException e) {
            log.info("定时任务已被取消");
        }
        return paymentSucceeded;
    }

    public void shutdownGracefully() {
        if (scheduledTask != null && !scheduledTask.isDone()) {
            // 取消任务
            scheduledTask.cancel(false);
        }
        if (executorService != null && !executorService.isShutdown()) {
            // 开始优雅关闭
            executorService.shutdown();
            try {
                // 等待3秒，让所有任务完成
                if (!executorService.awaitTermination(3, TimeUnit.SECONDS)) {
                    // 如果5秒后还有任务，强制关闭
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                // 中断时，强制关闭
                executorService.shutdownNow();
                // 重新设置中断状态
                Thread.currentThread().interrupt();
            }
        }
    }

//    public static void main(String[] args) {
//        PaymentChecker checker = new PaymentChecker();
//        checker.startCheckingPayment();
//        // 在适当的时候调用shutdownGracefully，例如在应用程序退出时
//        checker.shutdownGracefully();
//    }
}

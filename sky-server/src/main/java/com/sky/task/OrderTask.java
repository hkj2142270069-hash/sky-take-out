package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;

    /**
     * 定时任务，处理超时订单
     */
   @Scheduled(cron = "0 * * * * ?") //每分钟执行一次
  //  @Scheduled(cron = "1/5 * * * * ?")
    public void processTimeoutOrder(){
         log.info("处理超时订单：{}", LocalDateTime.now());
         LocalDateTime time = LocalDateTime.now().plusMinutes(-15);

        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimerLT(Orders.PENDING_PAYMENT, time);

        if(ordersList != null && ordersList.size() > 0){
              for (Orders orders : ordersList) {
                  orders.setStatus(Orders.CANCELLED);
                  orders.setCancelReason("订单超时，自动取消");
                  orders.setCancelTime(LocalDateTime.now());
                  orderMapper.update(orders);
              }
          }
    }

    /**
     * 定时任务，处理派送订单
     */
    @Scheduled(cron = "0 0 1 * * ?") //每天凌晨1点执行一次
   // @Scheduled(cron = "0/5 * * * * ?")
    public void processDeliveryOrder(){
        log.info("处理派送订单：{}", LocalDateTime.now());
        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimerLT(Orders.DELIVERY_IN_PROGRESS, time);
        if(ordersList != null && ordersList.size() > 0){
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.COMPLETED);


                orderMapper.update(orders);
            }
        }
    }
}

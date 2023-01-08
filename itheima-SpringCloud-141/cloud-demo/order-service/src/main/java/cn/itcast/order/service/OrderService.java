package cn.itcast.order.service;

import cn.itcast.order.mapper.OrderMapper;
import cn.itcast.order.pojo.Order;
import cn.itcast.order.pojo.User;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private RestTemplate restTemplate;

    public Order queryOrderById(Long orderId) {
        // 1.查询订单
        Order order = orderMapper.findById(orderId);
        // 2.向userService服务发送请求   8081 ， 8082 ，8083
        //根据userService服务名称拉取访问
        String url = "http://userservice/user/"+order.getUserId();
        //发送远程访问请求   参数:    请求路径   返回结果类型
//        String json = restTemplate.getForObject(url, String.class);
        User user = restTemplate.getForObject(url, User.class);
        if(user !=null){
            order.setUser(user);
        }
        // 4.返回
        return order;
    }

    /**
     * 修改负载均衡策略
     * @return
     */
    @Bean
    public IRule  randomRule(){
       return   new RandomRule();
    }


//    public Order queryOrderById(Long orderId) {
//        // 1.查询订单
//        Order order = orderMapper.findById(orderId);
//        // 2.向用户服务，发送请求
//        String url = "http://localhost:8081/user/"+order.getUserId();
//        //发送远程访问请求   参数:    请求路径   返回结果类型
////        String json = restTemplate.getForObject(url, String.class);
//         User user = restTemplate.getForObject(url, User.class);
//         if(user !=null){
//             order.setUser(user);
//         }
//        // 4.返回
//        return order;
//    }
}

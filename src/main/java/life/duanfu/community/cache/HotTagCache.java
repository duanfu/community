package life.duanfu.community.cache;


import life.duanfu.community.dto.HotTagDTO;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Data
public class HotTagCache {

    private List<String> hots = new ArrayList<>();

    public void updateTags(Map<String, Integer> tags) {
        //priorityQueue基于java的优先队列，可以实现大领队小领队
        //首先定义一个size
        int max = 5;
        //构建一个对象，需要重写一个compareTo方法。同时需要知道构建大领队还是小领队
        PriorityQueue<HotTagDTO> priorityQueue = new PriorityQueue<>(max);

        tags.forEach((name, priority) -> {
            HotTagDTO hotTagDTO = new HotTagDTO();
            hotTagDTO.setName(name);
            hotTagDTO.setPriority(priority);
            //根据priorityQueue.size()去判断是否要往里面追加
            if (priorityQueue.size() < max) {
                priorityQueue.add(hotTagDTO);
            } else {
                //通过和头部元素做比较，去poll出来和add进去
                //最小的元素
                HotTagDTO minHot = priorityQueue.peek();
                //当前标签的priority大于最小热度标签，就插入
                if (hotTagDTO.compareTo(minHot) > 0) {
                    priorityQueue.poll();
                    priorityQueue.add(hotTagDTO);
                }
            }
        });

        List<String> sortedTags = new ArrayList<>();
        HotTagDTO poll = priorityQueue.poll();
        //第一次拿到poll的时候不为空，直接追加到第一个元素，去做赋值。判断不等于null的时候再次赋值。
        while (poll != null) {
            sortedTags.add(0, poll.getName());
            poll = priorityQueue.poll();
        }
        hots =sortedTags;
        System.out.println(this.hots);
    }
}


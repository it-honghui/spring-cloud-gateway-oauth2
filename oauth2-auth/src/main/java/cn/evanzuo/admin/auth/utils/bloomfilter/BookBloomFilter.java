package cn.evanzuo.admin.auth.utils.bloomfilter;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
public class BookBloomFilter {
    public BloomFilter<Long> bloomFilter;
    private static final long SIZE = 1000000;

    @EventListener
    public void contextRefreshedEventListener(ContextRefreshedEvent contextRefreshedEvent) {
        bloomFilter = BloomFilter.create(Funnels.longFunnel(), SIZE);
        for (long i = 1; i <= SIZE; i++) {
            bloomFilter.put(i);
        }
    }
}

package cn.evanzuo.admin.auth.utils.bloomfilter;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

public class BloomFilterWuPanTest {
    private static int size = 1000000;

    private static BloomFilter<Integer> bloomFilter = BloomFilter.create(Funnels.integerFunnel(), size,0.01);
    public static void main(String[] args) {
        for (int i = 1; i <= size; i++) {
            bloomFilter.put(i);
        }

        int count = 0;

        //故意取1000000个不在过滤器里的值，看看有多少个会被认为在过滤器里
        for (int i = size + 1; i <= size*2; i++) {
            if (bloomFilter.mightContain(i)) {
                count++;
            }
        }
        System.out.println("误判的数量：" + count);
    }
}

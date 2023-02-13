package org.example;

import java.util.UUID;
// 여러 출처 : https://javacan.tistory.com/entry/use-64radix-for-generating-short-sessionid

/**
 * 방법
 * 1. uuid를 구한다. =>81561694-33ba-4486-8ef5-7dc126d9d8e6
 * 2. uuid의 LeastSignificantBits()를 구한다.=> -8145466082495702810
 * 3. 이를 Long.toString() 사용하는데, 이때 radix = 36으로 하면 13글자로 줄어듦..
 * - radix=64로 설정 시, 20글자로 줄어듦
 * -> 원리는??
 * 출처: https://stackoverflow.com/questions/20994768/how-to-reduce-length-of-uuid-generated-using-randomuuid
 *
 * getLessSignificantBits()가 getMostSignificantBits()보다 더 랜덤하다.
 * Long.toString(uuid.getLessSignificantBits(), Character.MAX_RADIX)
 */
public class Main {
    public static void main(String[] args) {
        //UUID Test
        UUID uuid = UUID.randomUUID();
        System.out.println(uuid.toString());

        long leastSignificantBits = uuid.getLeastSignificantBits();
        long mostSignificantBits = uuid.getMostSignificantBits();
        String s = Long.toString(mostSignificantBits, Character.MAX_RADIX) + Long.toString(leastSignificantBits, Character.MAX_RADIX);

        System.out.println(s.length()); // 27 자릿수
    }
}
package io.github.lourier.toolkit.common.stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @Date: 2023/11/10 14:56
 * @Author: Lourier
 */
public class SeqTest {

    @Test
    public void testEmpty() {
        Seq<Integer> empty = Seq.empty();
        List<Integer> list = empty.toList();
        Assertions.assertEquals(0, list.size());
    }

}

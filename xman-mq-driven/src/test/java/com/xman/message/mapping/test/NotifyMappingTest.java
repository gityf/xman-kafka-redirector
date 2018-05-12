package com.xman.message.mapping.test;

import com.xman.message.AbstractTest;
import com.xman.message.notify.NotifyMapping;
import com.xman.message.reflect.Invocation;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yangxiang@
 * @Date 2015/11/27
 * @Time 16:28
 */
public class NotifyMappingTest extends AbstractTest {

    public static final String[] scanPackages = {"com.yiche.common.service.md"};

    public static ConcurrentHashMap<String, List<Invocation>> getMappings() throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfiguration.class);

        NotifyMapping notifyMapping = new NotifyMapping(NotifyMappingTest.scanPackages, context);
        ConcurrentHashMap<String, List<Invocation>> mappings = notifyMapping.getMappings();
        if (mappings == null || mappings.size() == 0) {
            return null;
        }
        return mappings;
    }

    @Test
    public void testGetMappings() throws Exception {
        Map<String, List<Invocation>> mappings = getMappings();
        assertNotNull(mappings);
    }

}

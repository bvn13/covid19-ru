package com.bvn13.covid19.scheduler;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.StandardServletEnvironment;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Component
public class PropertiesPrinter extends PropertySourcesPlaceholderConfigurer {

    private static final String ENVIRONMENT_PROPERTIES = "environmentProperties";

    @Override
    public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory) throws BeansException {

        super.postProcessBeanFactory(beanFactory);

        final StandardServletEnvironment propertySources =
                (StandardServletEnvironment) super.getAppliedPropertySources().get(ENVIRONMENT_PROPERTIES).getSource();

        propertySources.getPropertySources().forEach(propertySource -> {
            if (propertySource.getSource() instanceof Map) {
                // it will print systemProperties, systemEnvironment, application.properties and other overrides of
                // application.properties
                System.out.println("#######" + propertySource.getName() + "#######");

                final Map<String, String> properties = mapValueAsString((Map<String, Object>) propertySource.getSource());
                System.out.println(toString(properties));
            }
        });
    }

    private Map<String, String> mapValueAsString(final Map<String, Object> map) {
        return map.entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey(), entry -> toString(entry.getValue())));
    }

    private String toString(final Object object)  {
        return Optional.ofNullable(object).map(value -> value.toString()).orElse(null);
    }

    private String toString(Map<String, String> properties) {
        StringBuilder sb = new StringBuilder();
        Set<String> keys = new TreeSet<>(properties.keySet());
        keys.forEach((k) -> {
            sb.append(k).append(" = ").append(properties.get(k)).append("\n");
        });
        return sb.toString();
    }
}

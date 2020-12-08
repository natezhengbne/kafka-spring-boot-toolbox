package com.github.natezhengbne.toolbox.annotation.scanner;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Component
public abstract class BaseAnnotationScanner {

    @Value("${kafka.scanner.basePackage:com.github.natezhengbne}")
    @Setter
    @Getter
    protected String basePackage;

    protected List<Method> getMappingAnnotatedMethods(Class<? extends Annotation> methodAnnotation, Class<? extends Annotation> classAnnotation) {
        List<Method> allMethods = new ArrayList<>();
        for (BeanDefinition b : getMappingAnnotatedClasses(classAnnotation)) {
            try {
                allMethods.addAll(getMethodsAnnotatedWith(Class.forName(b.getBeanClassName()), methodAnnotation));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Exception while loading class to search for annotations", e);
            }
        }
        return allMethods;
    }

    private Set<BeanDefinition> getMappingAnnotatedClasses(Class<? extends Annotation> annotation) {
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(annotation));
        return scanner.findCandidateComponents(basePackage);
    }

    private List<Method> getMethodsAnnotatedWith(final Class<?> type, final Class<? extends Annotation> annotation) {
        final List<Method> methods = new ArrayList<>();
        Class<?> klass = type;
        while (klass != Object.class) {
            final List<Method> allMethods = new ArrayList<>(Arrays.asList(klass.getDeclaredMethods()));
            for (final Method method : allMethods) {
                if (method.isAnnotationPresent(annotation)) {
                    methods.add(method);
                }
            }
            klass = klass.getSuperclass();
        }
        return methods;
    }
}

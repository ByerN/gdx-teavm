package com.github.xpenatan.gdx.examples.tests;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

public class JacksonTest {

    public static class A {
        List<X> list;

        public A(List<X> list) {
            this.list = list;
        }

        public A() {
        }

        public List<X> getList() {
            return list;
        }

        public void setList(List<X> list) {
            this.list = list;
        }
    }

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME
    )
    @JsonSubTypes({
            @JsonSubTypes.Type(value = B.class),
            @JsonSubTypes.Type(value = C.class)
    })
    interface X {
        void testMethod();
    }

    public static class B implements X {
        public B(int a) {
            this.a = a;
        }

        public B() {
        }

        int a;

        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }

        @Override
        public void testMethod() {

        }
    }

    public static class C implements X {
        public C(int a) {
            this.a = a;
        }

        public C() {
        }

        int a;

        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }

        @Override
        public void testMethod() {

        }
    }
}

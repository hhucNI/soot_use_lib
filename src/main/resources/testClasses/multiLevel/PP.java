class PP {
    public static void main(String[] args) {
        A x = new B();
        x.foo();
        A y = new C();
        y.foo();
    }

}

class A {
    void foo() {
        System.out.println("A.FOO");
    }
}

class B extends A {
}

class C extends B {
    void foo() {
        System.out.println("C.foo");
    }
}

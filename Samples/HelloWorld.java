class HelloWorld {
    public static void main(String[] a){
        System.out.println(new Print().PrintNumber(10));
    }
}

class Print {
    public int PrintNumber(int n) {
        while(n < 21) {
            System.out.println(n);
            n = n + 1;
        }
        return 0;
    }

    public int PrintNumb(int n) {
        System.out.println(100);
        return n;
    }

    public int PrintFactorial(int n) {
        int num;
        int num2;
        if (n < 1) { 
            System.out.println(500);
            num = 1;
        }
        else {
            System.out.println(300);
            num2 = n - 1;
            //num = this.PrintNumb(num2); 
            num = 10;
        }

        return num;
    }
}

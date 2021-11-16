package edu.yu.introtoalgs;

public class EstimateSecretAlgorithmsClient2 {

  public static void main(String[] args) {
    algo3();
  }

  public static void algo1() {
    System.out.println("Algo 1");
    Integer n = 1;
    SecretAlgorithm1 algo = new SecretAlgorithm1();

    for (int i = 1; i < 10; i++) {
      algo.setup(n);
      algo.execute();
    }

    for (int i = 1; i < 14; i++) {
      n = n * 2;
      algo.setup(n);
      long start = System.nanoTime();
      algo.execute();
      long end = System.nanoTime();
    //  System.out.print("Test " + i + ": " + n + " - "); 
      System.out.print(end - start);
      System.out.println();
    } 
    System.out.println("done");
  }
  
  public static void algo2() {
    System.out.println("Algo 2");
    Integer n = 10;
    SecretAlgorithm2 algo = new SecretAlgorithm2();

    for (int i = 1; i < 10; i++) {
      algo.setup(n);
      algo.execute();
    }

    for (int i = 1; i < 14; i++) {
      n = n * 2;
      algo.setup(n);
      long start = System.nanoTime();
      algo.execute();
      long end = System.nanoTime();
    //  System.out.print("Test " + i + ": " + n + " - "); 
      System.out.print(end - start);
      System.out.println();
    } 
    System.out.println("done");
  }

  public static void algo3() {
    System.out.println("Algo 3");
    Integer n = 10;
    SecretAlgorithm3 algo = new SecretAlgorithm3();

    for (int i = 1; i < 10; i++) {
      algo.setup(n);
      algo.execute();
    }

    for (int i = 1; i < 14; i++) {
      n = n * 2;
      
      algo.setup(n);
      long start = System.nanoTime();
      algo.execute();
      long end = System.nanoTime();
    //  System.out.print("Test " + i + ": " + n + " - "); 
      System.out.print(end - start);
      System.out.println();
    } 
    System.out.println("done");
  }

  public static void algo4() {
    System.out.println("Algo 4");
    Integer n = 66666;
    SecretAlgorithm4 algo = new SecretAlgorithm4();

    for (int i = 1; i < 100; i++) {
      algo.setup(n);
      algo.execute();
    }

    for (int i = 1; i < 14; i++) {
      n = n * 2;
      
      algo.setup(n);
      long start = System.nanoTime();
      algo.execute();
      long end = System.nanoTime();
    //  System.out.print("Test " + i + ": " + n + " - "); 
      System.out.print(end - start);
      System.out.println();
    } 
    System.out.println("done");
  }
  
}

  
   
